package ru.doronin.demonstration.measurement_storage.measurement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Контрольные показатели для счетчиков электроэнергии
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ElectricityMetrics {
    private BigDecimal dayCounter, nightCounter;
}
