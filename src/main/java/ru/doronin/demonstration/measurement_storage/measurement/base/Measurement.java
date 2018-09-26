package ru.doronin.demonstration.measurement_storage.measurement.base;

import ru.doronin.demonstration.measurement_storage.measurement.MeasurementType;
import ru.doronin.demonstration.measurement_storage.persistance.base.Domain;
import ru.doronin.demonstration.measurement_storage.user.base.User;

import java.time.LocalDateTime;

/**
 * Общий функционал измерения, снятого со счетчика
 */
public interface Measurement<T> extends Domain {

    /**
     * Получение пользователя-владельца
     *
     * @return - экземпляр User
     */
    User getOwner();

    /**
     * Установка владельца
     *
     * @param owner - пользователь
     */
    void setOwner(User owner);

    /**
     * Получение типа измерения
     *
     * @return
     */
    MeasurementType getType();

    /**
     * Установка типа измерения
     *
     * @param type
     */
    void setType(MeasurementType type);

    /**
     * Получение времени регистрации измерения
     *
     * @return - экземпляр LocalDateTime
     */
    LocalDateTime getRegistered();

    /**
     * Установка времени регистрации измерения
     *
     * @param registered - время регистрации
     */
    void setRegistered(LocalDateTime registered);

    /**
     * Получение значения измерения
     *
     * @return - экземпляр типа T
     */
    T getValue();

    /**
     * Установка значения измерения
     *
     * @param value - измерение
     */
    void setValue(T value);
}
