package ru.doronin.demonstration.measurement_storage.persistance.jpa;

import lombok.Data;
import ru.doronin.demonstration.measurement_storage.persistance.base.Domain;

import javax.persistence.*;

/**
 * Общая реализация Domain
 */
@Data
@MappedSuperclass
public abstract class DomainImpl implements Domain {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
}
