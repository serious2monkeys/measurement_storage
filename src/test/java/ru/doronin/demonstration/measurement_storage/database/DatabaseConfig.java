package ru.doronin.demonstration.measurement_storage.database;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaRepositories(basePackages = "ru.doronin.demonstration.measurement_storage")
@EntityScan(basePackages = "ru.doronin.demonstration.measurement_storage")
@EnableTransactionManagement
@Configuration
@EnableAutoConfiguration
public class DatabaseConfig {
}
