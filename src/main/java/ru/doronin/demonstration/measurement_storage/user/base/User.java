package ru.doronin.demonstration.measurement_storage.user.base;

import ru.doronin.demonstration.measurement_storage.persistance.base.Domain;
import ru.doronin.demonstration.measurement_storage.user.Role;

/**
 * Общий функционал пользователя
 */
public interface User extends Domain {
    /**
     * Запрос логина пользователя
     *
     * @return - строка
     */
    String getLogin();

    /**
     * Установка значения логина
     *
     * @param login - логин
     */
    void setLogin(String login);

    /**
     * Получение пароля пользователя
     *
     * @return - зашифрованный пароль
     */
    String getPassword();

    /**
     * Установка пароля пользователя
     *
     * @param password - пароль в зашифрованном виде
     */
    void setPassword(String password);

    /**
     * Получение имени пользователя
     *
     * @return - строковое представление
     */
    String getName();

    /**
     * Установка имени пользователя
     *
     * @param name - имя пользователя
     */
    void setName(String name);

    /**
     * Запрос роли пользователя
     *
     * @return - значение enum Role
     */
    Role getRole();

    /**
     * Установка роли пользователя
     *
     * @param role - роль
     */
    void setRole(Role role);
}
