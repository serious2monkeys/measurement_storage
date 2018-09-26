package ru.doronin.demonstration.measurement_storage.measurement.jpa;

import org.springframework.stereotype.Repository;
import ru.doronin.demonstration.measurement_storage.measurement.MeasurementType;
import ru.doronin.demonstration.measurement_storage.measurement.base.Measurement;
import ru.doronin.demonstration.measurement_storage.persistance.jpa.DomainRepository;
import ru.doronin.demonstration.measurement_storage.user.base.User;

import java.util.List;

@Repository
public interface MeasurementRepository<D extends Measurement> extends DomainRepository<MeasurementImpl> {

    /**
     * Поиск измерений по владельцу
     *
     * @param owner - владелец
     * @return
     */
    List<D> findAllByOwnerOrderByRegisteredDesc(User owner);

    /**
     * Поиск измерений заданного типа, принадлежащик указанному владельцу
     *
     * @param owner - владелец
     * @param type  - тип
     * @return
     */
    List<D> findAllByOwnerAndTypeOrderByRegisteredDesc(User owner, MeasurementType type);

    /**
     * Поиск всех измерений
     *
     * @return
     */
    List<D> findAllByOrderByRegisteredDesc();

    /**
     * Поиск измерений по типу
     *
     * @param type - тип измерений
     * @return
     */
    List<D> findAllByTypeOrderByRegisteredDesc(MeasurementType type);
}
