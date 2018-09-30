package ru.doronin.demonstration.measurement_storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {"ru.doronin.demonstration.measurement_storage"})
@EntityScan(basePackageClasses = {MeasurementStorageApplication.class, Jsr310JpaConverters.class})
@EnableJpaRepositories(basePackages = {"ru.doronin.demonstration.measurement_storage"})
@EnableTransactionManagement
public class MeasurementStorageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeasurementStorageApplication.class, args);
    }
}
