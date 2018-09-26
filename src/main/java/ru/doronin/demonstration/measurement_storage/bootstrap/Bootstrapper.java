package ru.doronin.demonstration.measurement_storage.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.doronin.demonstration.measurement_storage.measurement.ElectricityMetrics;
import ru.doronin.demonstration.measurement_storage.measurement.MeasurementsService;
import ru.doronin.demonstration.measurement_storage.measurement.WaterMetrics;
import ru.doronin.demonstration.measurement_storage.measurement.base.ElectricityMeasurement;
import ru.doronin.demonstration.measurement_storage.measurement.base.HeatingMeasurement;
import ru.doronin.demonstration.measurement_storage.measurement.base.WaterMeasurement;
import ru.doronin.demonstration.measurement_storage.measurement.jpa.ElectricityMeasurementImpl;
import ru.doronin.demonstration.measurement_storage.measurement.jpa.HeatingMeasurementImpl;
import ru.doronin.demonstration.measurement_storage.measurement.jpa.WaterMeasurementImpl;
import ru.doronin.demonstration.measurement_storage.user.Role;
import ru.doronin.demonstration.measurement_storage.user.UserService;
import ru.doronin.demonstration.measurement_storage.user.base.User;
import ru.doronin.demonstration.measurement_storage.user.jpa.UserImpl;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * Компонент, отвечающий за начальное заполнение БД
 */
@Component
@Slf4j
public class Bootstrapper implements ApplicationListener<ContextRefreshedEvent> {
    private final UserService userService;
    private final MeasurementsService measurementsService;
    private final Random random = new SecureRandom();

    public Bootstrapper(UserService userService, MeasurementsService measurementsService) {
        this.userService = userService;
        this.measurementsService = measurementsService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("Initialisation started");

        List<User> users = Arrays.asList(
                createUser("first", "Иван", Role.PLAIN_CUSTOMER),
                createUser("second", "Сергей", Role.PLAIN_CUSTOMER),
                createUser("third", "Леонид", Role.PLAIN_CUSTOMER),
                createUser("admin", "Admin", Role.ADMIN));
        users.forEach(this::generateMeasurements);

        log.info("Initialisation done");
    }

    /**
     * Создание пользователя для тестирования
     *
     * @param login - логин
     * @param name  - имя пользователя
     * @param role  - роль
     * @return - сохраненный экземпляр
     */
    private User createUser(String login, String name, Role role) {
        User customer = new UserImpl();
        customer.setLogin(login);
        customer.setPassword(login);
        customer.setName(name);
        customer.setRole(role);
        return userService.save(customer);
    }

    /**
     * Создание измерений для пользователя
     *
     * @param user - пользователь
     */
    private void generateMeasurements(User user) {
        LocalDate startDate = LocalDate.of(2018, 1, 1);

        AtomicReference<WaterMetrics> lastWater = new AtomicReference<>(new WaterMetrics(
                new BigDecimal(random.nextDouble() * 300),
                new BigDecimal(random.nextDouble() * 200)));

        AtomicReference<ElectricityMetrics> electricityMetrics = new AtomicReference<>(new ElectricityMetrics(
                new BigDecimal(random.nextDouble() * 500),
                new BigDecimal(random.nextDouble() * 500)));

        AtomicReference<BigDecimal> lastHeat = new AtomicReference<>(new BigDecimal(random.nextDouble() * 400));

        Stream.iterate(startDate, date -> date.plusMonths(1))
                .limit(ChronoUnit.MONTHS.between(startDate, LocalDate.now()) + 1)
                .forEach(date -> {
                    WaterMeasurement monthlyWater = makeUpWaterMeasurement(user, date, lastWater.get());
                    measurementsService.save(monthlyWater);
                    lastWater.set(monthlyWater.getValue());

                    ElectricityMeasurement monthlyElectricity = makeUpElectricityMeasurement(user, date, electricityMetrics.get());
                    measurementsService.save(monthlyElectricity);
                    electricityMetrics.set(monthlyElectricity.getValue());

                    HeatingMeasurement heatingMeasurement = makeUpHeatingMeasurement(user, date, lastHeat.get());
                    measurementsService.save(heatingMeasurement);
                    lastHeat.set(heatingMeasurement.getValue());
                });
    }

    /**
     * Генерирование показаний счетчика тепла
     *
     * @param user      - пользователь
     * @param date      - дата
     * @param seedValue - опорное значение
     * @return
     */
    private HeatingMeasurement makeUpHeatingMeasurement(User user, LocalDate date, BigDecimal seedValue) {
        HeatingMeasurement heatingMeasurement = new HeatingMeasurementImpl();
        heatingMeasurement.setOwner(user);
        heatingMeasurement.setRegistered(date.atTime(makeUpTime()));
        heatingMeasurement.setValue(seedValue.add(new BigDecimal(random.nextDouble() * 100)).setScale(2, ROUND_HALF_UP));
        return heatingMeasurement;
    }

    /**
     * Генерирование показаний счетчика электроэнергии
     *
     * @param user      - пользователь
     * @param date      - дата
     * @param seedValue - опорное значение
     * @return
     */
    private ElectricityMeasurement makeUpElectricityMeasurement(User user, LocalDate date, ElectricityMetrics seedValue) {
        ElectricityMeasurement electricityMeasurement = new ElectricityMeasurementImpl();
        electricityMeasurement.setOwner(user);
        electricityMeasurement.setRegistered(date.atTime(makeUpTime()));
        electricityMeasurement.setValue(new ElectricityMetrics(
                seedValue.getDayCounter().add(new BigDecimal(random.nextDouble() * 300)).setScale(1, ROUND_HALF_UP),
                seedValue.getNightCounter().add(new BigDecimal(random.nextDouble() * 300)).setScale(1, ROUND_HALF_UP)));
        return electricityMeasurement;
    }

    /**
     * Генерирование показаний счетчика воды
     *
     * @param user      - пользователь
     * @param date      - дата
     * @param seedValue - опорное значение
     * @return
     */
    private WaterMeasurement makeUpWaterMeasurement(User user, LocalDate date, WaterMetrics seedValue) {
        WaterMeasurement waterMeasurement = new WaterMeasurementImpl();
        waterMeasurement.setOwner(user);
        waterMeasurement.setRegistered(date.atTime(makeUpTime()));
        waterMeasurement.setValue(new WaterMetrics(
                seedValue.getColdWaterConsumption().add(new BigDecimal(random.nextDouble() * 200)).setScale(2, ROUND_HALF_UP),
                seedValue.getHotWaterConsumption().add(new BigDecimal(random.nextDouble() * 200)).setScale(2, ROUND_HALF_UP)));
        return waterMeasurement;
    }

    /**
     * Генерирование момента времени
     *
     * @return - произвольный момент времени
     */
    private LocalTime makeUpTime() {
        return LocalTime.of(random.nextInt(23),
                random.nextInt(59),
                random.nextInt(59));
    }
}
