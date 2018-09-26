package ru.doronin.demonstration.measurement_storage.persistance.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Общий вид JPARepository
 */
@NoRepositoryBean
public interface DomainRepository<D extends DomainImpl> extends JpaRepository<D, Long> {
}
