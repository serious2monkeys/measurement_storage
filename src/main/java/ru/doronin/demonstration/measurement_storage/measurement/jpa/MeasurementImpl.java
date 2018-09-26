package ru.doronin.demonstration.measurement_storage.measurement.jpa;

import com.google.gson.Gson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.doronin.demonstration.measurement_storage.measurement.MeasurementType;
import ru.doronin.demonstration.measurement_storage.measurement.base.Measurement;
import ru.doronin.demonstration.measurement_storage.persistance.jpa.DomainImpl;
import ru.doronin.demonstration.measurement_storage.user.base.User;
import ru.doronin.demonstration.measurement_storage.user.jpa.UserImpl;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Общая реализация сущности хранимых измерений
 *
 * @param <T> - тип значения хранимых измерений
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")
@Table(name = "MEASUREMENTS")
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class MeasurementImpl<T> extends DomainImpl implements Measurement<T> {
    protected static final Gson GSON = new Gson();

    @ManyToOne(targetEntity = UserImpl.class)
    @JoinColumn(name = "OWNER", nullable = false)
    protected User owner;

    @Column(name = "REGISTERED", nullable = false)
    protected LocalDateTime registered;

    @Column(name = "VALUE", nullable = false)
    protected String stringValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", nullable = false, insertable = false, updatable = false)
    private MeasurementType type;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(T value) {
        stringValue = GSON.toJson(value);
    }

    /**
     * Действия, выполняемые перед сохранением объекта в базу
     */
    @PrePersist
    void beforeSave() {
        if (registered == null) {
            registered = LocalDateTime.now();
        }
    }
}
