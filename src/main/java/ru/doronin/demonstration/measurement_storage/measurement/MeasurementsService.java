package ru.doronin.demonstration.measurement_storage.measurement;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.doronin.demonstration.measurement_storage.measurement.base.Measurement;
import ru.doronin.demonstration.measurement_storage.measurement.jpa.MeasurementRepository;
import ru.doronin.demonstration.measurement_storage.user.base.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Служба работы с измерениями коммунальных приборов
 */
@Service
public class MeasurementsService {

    private final MeasurementRepository measurementRepository;

    public MeasurementsService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    /**
     * Поиск измерения по id
     *
     * @param id - id измерения
     * @return
     */
    public Optional<? extends Measurement> findById(long id) {
        return measurementRepository.findById(id);
    }

    /**
     * Сохранение измерения
     *
     * @param measurement - измерение
     * @return - сохраненный экземпляр
     */
    @PreAuthorize("#measurement.id == null " +
            "|| hasRole('ADMIN') || #measurement.owner.getLogin().equals(authentication.getName())")
    @Transactional
    public Measurement save(Measurement measurement) {
        if (measurement.getId() == null) {
            return (Measurement) measurementRepository.save(measurement);
        }
        return update(measurement);
    }

    /**
     * Обновление измерения
     *
     * @param measurement - измерение
     * @return - обновленный экземпляр
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || #measurement.owner.getLogin().equals(authentication.getName())")
    public Measurement update(Measurement measurement) {
        if (measurement.getId() == null) {
            throw new IllegalArgumentException("Id must be specified for update");
        }

        Optional<? extends Measurement> fromDb = measurementRepository.findById(measurement.getId());
        if (fromDb.isPresent()) {
            Measurement toSave = fromDb.get();

            if (measurement.getType() != null
                    && !toSave.getType().equals(measurement.getType())) {
                throw new IllegalArgumentException("Unable to change the measurement type");
            }

            toSave.setValue(measurement.getValue());
            return (Measurement) measurementRepository.save(toSave);
        }

        throw new NoSuchElementException("Unable to find measurement with this Id");
    }

    /**
     * Удаление измерения
     *
     * @param measurement - измерение
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN') || #measurement.owner.getLogin().equals(authentication.getName())")
    public void delete(Measurement measurement) {
        measurementRepository.delete(measurement);
    }

    /**
     * Поиск всех измерений
     *
     * @return - список всех измерений, отсортированных по дате по убыванию
     */
    @Transactional
    public List<? extends Measurement> finaAll() {
        return measurementRepository.findAllByOrderByRegisteredDesc();
    }

    /**
     * Поиск измерений пользователя
     *
     * @param owner - пользователь
     * @return - список измерений
     */
    @Transactional
    public List<? extends Measurement> findAllByOwner(User owner) {
        return measurementRepository.findAllByOwnerOrderByRegisteredDesc(owner);
    }
}
