package ru.doronin.demonstration.measurement_storage.user.jpa;

import org.springframework.stereotype.Repository;
import ru.doronin.demonstration.measurement_storage.persistance.jpa.DomainRepository;
import ru.doronin.demonstration.measurement_storage.user.base.User;

import java.util.Optional;

/**
 * Необходимый функционал для работы
 */
@Repository
public interface UserRepository<D extends User> extends DomainRepository<UserImpl> {
    Optional<D> findByLogin(String login);
}
