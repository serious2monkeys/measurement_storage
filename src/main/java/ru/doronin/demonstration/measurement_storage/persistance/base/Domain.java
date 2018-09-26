package ru.doronin.demonstration.measurement_storage.persistance.base;

import java.io.Serializable;

/**
 * Общий вид сущностей
 */
public interface Domain extends Serializable {
    /**
     * Получение идентификатора объекта
     *
     * @return - числовое представление идентификатора
     */
    Long getId();

    /**
     * Установка идентификатора объекта
     *
     * @param id - идентификатор
     */
    void setId(Long id);
}