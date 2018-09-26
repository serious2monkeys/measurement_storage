package ru.doronin.demonstration.measurement_storage.measurement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Контрольные показатели для счетчиков воды
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WaterMetrics {
    private BigDecimal coldWaterConsumption, hotWaterConsumption;
}
