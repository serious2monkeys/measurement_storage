package ru.doronin.demonstration.measurement_storage.measurement.jpa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.doronin.demonstration.measurement_storage.measurement.WaterMetrics;
import ru.doronin.demonstration.measurement_storage.measurement.base.WaterMeasurement;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Показания счетчиков воды, сохраняемые в базу
 */
@Entity
@DiscriminatorValue("WATER")
@Data
@EqualsAndHashCode(callSuper = false)
public class WaterMeasurementImpl extends MeasurementImpl<WaterMetrics> implements WaterMeasurement {

    /**
     * {@inheritDoc}
     */
    @Override
    public WaterMetrics getValue() {
        return GSON.fromJson(stringValue, WaterMetrics.class);
    }
}