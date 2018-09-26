package ru.doronin.demonstration.measurement_storage.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.doronin.demonstration.measurement_storage.measurement.MeasurementType;

/**
 * Объект для передачи измерений
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MeasurementDto {
    private Long id;
    private MeasurementType type;
    private String owner;
    private Object value;
    private String registered;
}
