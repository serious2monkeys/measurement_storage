package ru.doronin.demonstration.measurement_storage.user.jpa;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.doronin.demonstration.measurement_storage.persistance.jpa.DomainImpl;
import ru.doronin.demonstration.measurement_storage.user.Role;
import ru.doronin.demonstration.measurement_storage.user.base.User;

import javax.persistence.*;

/**
 * Реализация сущности "Пользователь"
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "USERS")
public class UserImpl extends DomainImpl implements User {

    @Column(name = "LOGIN", nullable = false, unique = true)
    private String login;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false)
    private Role role;

    /**
     * Проверки перед сохранением в базу
     */
    @PrePersist
    void beforeSave() {
        if (role == null) {
            role = Role.PLAIN_CUSTOMER;
        }
    }
}
