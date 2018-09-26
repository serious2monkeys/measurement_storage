package ru.doronin.demonstration.measurement_storage.measurement.jpa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.doronin.demonstration.measurement_storage.measurement.ElectricityMetrics;
import ru.doronin.demonstration.measurement_storage.measurement.base.ElectricityMeasurement;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Хранимые показатели счетчиков электроэнергии
 */
@Entity
@DiscriminatorValue("ELECTRICITY")
@Data
@EqualsAndHashCode(callSuper = false)
public class ElectricityMeasurementImpl extends MeasurementImpl<ElectricityMetrics> implements ElectricityMeasurement {

    /**
     * {@inheritDoc}
     */
    @Override
    public ElectricityMetrics getValue() {
        return GSON.fromJson(stringValue, ElectricityMetrics.class);
    }
}
