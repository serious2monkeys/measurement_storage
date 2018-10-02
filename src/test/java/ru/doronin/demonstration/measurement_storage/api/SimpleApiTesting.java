package ru.doronin.demonstration.measurement_storage.api;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.doronin.demonstration.measurement_storage.api.controllers.MeasurementsController;
import ru.doronin.demonstration.measurement_storage.bootstrap.Bootstrapper;
import ru.doronin.demonstration.measurement_storage.config.ApplicationConfiguration;
import ru.doronin.demonstration.measurement_storage.measurement.ElectricityMetrics;
import ru.doronin.demonstration.measurement_storage.measurement.MeasurementsService;
import ru.doronin.demonstration.measurement_storage.measurement.WaterMetrics;
import ru.doronin.demonstration.measurement_storage.measurement.base.ElectricityMeasurement;
import ru.doronin.demonstration.measurement_storage.measurement.base.HeatingMeasurement;
import ru.doronin.demonstration.measurement_storage.measurement.base.Measurement;
import ru.doronin.demonstration.measurement_storage.measurement.base.WaterMeasurement;
import ru.doronin.demonstration.measurement_storage.user.Role;
import ru.doronin.demonstration.measurement_storage.user.UserService;
import ru.doronin.demonstration.measurement_storage.user.base.User;
import ru.doronin.demonstration.measurement_storage.user.jpa.UserImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS;
import static ru.doronin.demonstration.measurement_storage.measurement.MeasurementType.*;

/**
 * Тесты API
 */
@RunWith(MockitoJUnitRunner.class)
@DirtiesContext(classMode = AFTER_CLASS)
public class SimpleApiTesting {

    private static final String LOGIN = "USER";
    private MockMvc mvc;
    @Mock
    private MeasurementsService measurementsService;
    @Mock
    private UserService userService;

    /**
     * Подготовка к тестированию
     */
    @Before
    public void beforeTest() {
        User current = UserImpl.builder().login(LOGIN).password("PASSWORD")
                .name("NAME").role(Role.PLAIN_CUSTOMER).build();
        doReturn(current).when(userService).getCurrent();

        LocalDate sampleDate = LocalDate.now().minusMonths(1);

        ElectricityMeasurement electricityMeasurement = Bootstrapper.makeUpElectricityMeasurement(current, sampleDate,
                new ElectricityMetrics(new BigDecimal(100), new BigDecimal(200)));
        electricityMeasurement.setType(ELECTRICITY);
        electricityMeasurement.setId(1L);

        HeatingMeasurement heatingMeasurement = Bootstrapper.makeUpHeatingMeasurement(current, sampleDate, new BigDecimal(300));
        heatingMeasurement.setType(HEATING);
        heatingMeasurement.setId(2L);

        WaterMeasurement waterMeasurement = Bootstrapper.makeUpWaterMeasurement(current, sampleDate,
                new WaterMetrics(new BigDecimal(150), new BigDecimal(70)));
        waterMeasurement.setType(WATER);
        waterMeasurement.setId(3L);

        List<? extends Measurement> measurements = Arrays.asList(electricityMeasurement, heatingMeasurement, waterMeasurement);

        doReturn(measurements).when(measurementsService).findAllByOwner(eq(current));
        mvc = MockMvcBuilders.standaloneSetup(new MeasurementsController(userService, measurementsService,
                new ApplicationConfiguration().modelMapper())).build();
        RestAssuredMockMvc.mockMvc(mvc);
    }

    /**
     * Тестирование ответа API на запрос
     */
    @Test
    public void testGetMeasurements() {
        RestAssuredMockMvc.given().when().get("/measurements").then().statusCode(200)
                .contentType(ContentType.JSON)
                .assertThat().body("id", hasItems(1, 2, 3))
                .body("owner", hasItems(LOGIN, LOGIN, LOGIN))
                .body("type", hasItems(ELECTRICITY.name(), HEATING.name(), WATER.name()));
    }
}
