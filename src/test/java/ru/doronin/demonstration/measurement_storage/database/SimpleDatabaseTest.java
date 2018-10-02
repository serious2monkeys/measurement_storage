package ru.doronin.demonstration.measurement_storage.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.doronin.demonstration.measurement_storage.config.ApplicationConfiguration;
import ru.doronin.demonstration.measurement_storage.measurement.ElectricityMetrics;
import ru.doronin.demonstration.measurement_storage.measurement.WaterMetrics;
import ru.doronin.demonstration.measurement_storage.measurement.base.ElectricityMeasurement;
import ru.doronin.demonstration.measurement_storage.measurement.base.WaterMeasurement;
import ru.doronin.demonstration.measurement_storage.measurement.jpa.ElectricityMeasurementImpl;
import ru.doronin.demonstration.measurement_storage.measurement.jpa.MeasurementRepository;
import ru.doronin.demonstration.measurement_storage.measurement.jpa.WaterMeasurementImpl;
import ru.doronin.demonstration.measurement_storage.user.Role;
import ru.doronin.demonstration.measurement_storage.user.base.User;
import ru.doronin.demonstration.measurement_storage.user.jpa.UserImpl;
import ru.doronin.demonstration.measurement_storage.user.jpa.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static ru.doronin.demonstration.measurement_storage.measurement.MeasurementType.ELECTRICITY;
import static ru.doronin.demonstration.measurement_storage.measurement.MeasurementType.WATER;

/**
 * Тестирование функционирования базы данных
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DatabaseConfig.class})
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@SuppressWarnings("unchecked")
public class SimpleDatabaseTest {
    private final static String LOGIN = "test_login";
    private final static String LOGIN_FOR_SEARCH = "find_me";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MeasurementRepository measurementRepository;

    /**
     * Тестирование компонента для непосредственной работы с таблицами пользователей
     */
    @Test
    public void testUserRepository() {
        //Изначально таблица пустая
        assertEquals(0, userRepository.findAll().size());

        User plainUser = UserImpl.builder()
                .login(LOGIN)
                .name("test")
                .password("no_matter")
                .role(Role.PLAIN_CUSTOMER)
                .build();
        userRepository.save(plainUser);

        List<User> userList = userRepository.findAll();
        //Пользователь успешно сохранился
        assertEquals(1, userList.size());
        assertEquals(LOGIN, userList.get(0).getLogin());
        assertEquals(Role.PLAIN_CUSTOMER, userList.get(0).getRole());


        // Сохраним еще одного пользоателя
        userRepository.save(UserImpl.builder()
                .login(LOGIN_FOR_SEARCH)
                .name("another")
                .password("123")
                .role(Role.ADMIN)
                .build());

        userList = userRepository.findAll();
        //Убедимся, что найдены оба пользователя
        assertEquals(2, userList.size());

        Optional<User> searchResult = userRepository.findByLogin(LOGIN_FOR_SEARCH);
        //Убедимся, что поиск по логину работает
        assertTrue(searchResult.isPresent());
        User user = searchResult.get();
        assertEquals(user.getLogin(), LOGIN_FOR_SEARCH);

        /**
         * Проверим работу удаления
         */
        userRepository.delete(user);
        List<User> remaining = userRepository.findAll();
        assertTrue(remaining.stream().allMatch(item -> LOGIN.equals(item.getLogin())));
        assertEquals(1, remaining.size());
    }

    /**
     * Тестирование компонента работы с таблицей измерений
     */
    @Test
    public void testMeasurementRepository() {
        //Таблица пустая
        assertEquals(0, measurementRepository.findAll().size());
        User sampleUser = UserImpl.builder()
                .login(LOGIN)
                .name("test")
                .password("no_matter")
                .role(Role.PLAIN_CUSTOMER)
                .build();
        userRepository.save(sampleUser);

        WaterMeasurement waterMeasurement = new WaterMeasurementImpl();
        waterMeasurement.setValue(new WaterMetrics(new BigDecimal(100), new BigDecimal(200)));
        waterMeasurement.setOwner(sampleUser);
        measurementRepository.save(waterMeasurement);

        ElectricityMeasurement electricityMeasurement = new ElectricityMeasurementImpl();
        electricityMeasurement.setValue(new ElectricityMetrics(new BigDecimal(200), new BigDecimal(300)));
        electricityMeasurement.setOwner(sampleUser);
        measurementRepository.save(electricityMeasurement);

        //Убедимся, что измерения сохранились
        assertEquals(2, measurementRepository.findAll().size());

        //Проверим работу выборок
        assertEquals(2, measurementRepository.findAllByOwnerOrderByRegisteredDesc(sampleUser).size());
        assertEquals(1, measurementRepository.findAllByOwnerAndTypeOrderByRegisteredDesc(sampleUser, ELECTRICITY).size());
        List<WaterMeasurement> measurements = measurementRepository.findAllByOwnerAndTypeOrderByRegisteredDesc(sampleUser, WATER);
        assertEquals(1, measurements.size());

        //Проверяем хранимое значение
        assertEquals(100.0, measurements.get(0).getValue().getColdWaterConsumption().doubleValue(), 0.001);

        //Изменяем значение
        measurements.get(0).setValue(new WaterMetrics(new BigDecimal(150), new BigDecimal(100)));
        measurementRepository.save(measurements.get(0));

        //Убеждаемся, что изменение сохранилось
        waterMeasurement = (WaterMeasurement) measurementRepository.findAllByOwnerAndTypeOrderByRegisteredDesc(sampleUser, WATER).get(0);
        assertEquals(150.0, waterMeasurement.getValue().getColdWaterConsumption().doubleValue(), 0.001);
    }
}
