package ru.doronin.demonstration.measurement_storage.measurement.jpa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.doronin.demonstration.measurement_storage.measurement.base.HeatingMeasurement;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Хранимая сущность измерения тепла
 */
@Entity
@DiscriminatorValue("HEATING")
@Data
@EqualsAndHashCode(callSuper = false)
public class HeatingMeasurementImpl extends MeasurementImpl<BigDecimal> implements HeatingMeasurement {

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal getValue() {
        return GSON.fromJson(stringValue, BigDecimal.class);
    }
}
