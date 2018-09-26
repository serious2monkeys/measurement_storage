package ru.doronin.demonstration.measurement_storage.api.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.doronin.demonstration.measurement_storage.api.dto.MeasurementDto;
import ru.doronin.demonstration.measurement_storage.api.dto.SimpleResponse;
import ru.doronin.demonstration.measurement_storage.measurement.MeasurementsService;
import ru.doronin.demonstration.measurement_storage.measurement.base.Measurement;
import ru.doronin.demonstration.measurement_storage.user.Role;
import ru.doronin.demonstration.measurement_storage.user.UserService;
import ru.doronin.demonstration.measurement_storage.user.base.User;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Обработка обращений к REST-API измерений
 */
@RestController
@RequestMapping("/measurements")
public class MeasurementsController {
    private final UserService userService;
    private final MeasurementsService measurementsService;
    private final ModelMapper modelMapper;

    public MeasurementsController(UserService userService,
                                  MeasurementsService measurementsService,
                                  ModelMapper modelMapper) {
        this.userService = userService;
        this.measurementsService = measurementsService;
        this.modelMapper = modelMapper;
    }

    /**
     * Список всех измерений, к которым имеет доступ текущий пользователь
     *
     * @return - список, завернутый в JSON
     * @throws AccessDeniedException - в случае отстутствия прав доступа
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<MeasurementDto> getMeasurements() throws AccessDeniedException {
        User current = userService.getCurrent();
        if (current == null) {
            throw new AccessDeniedException("Access denied");
        }

        List<? extends Measurement> measurements = Role.ADMIN.equals(current.getRole())
                ? measurementsService.finaAll()
                : measurementsService.findAllByOwner(current);

        return measurements.stream()
                .map(measurement -> modelMapper.map(measurement, MeasurementDto.class))
                .collect(Collectors.toList());
    }

    /**
     * Создание измерения
     *
     * @param dto - данные измерения
     * @return
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MeasurementDto createMeasurement(@RequestBody MeasurementDto dto) {
        Measurement measurement = modelMapper.map(dto, Measurement.class);
        measurement.setOwner(userService.getCurrent());
        return modelMapper.map(measurementsService.save(measurement), MeasurementDto.class);
    }

    /**
     * Обновление измерения
     *
     * @param dto - данные измерения
     * @return
     */
    @PutMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MeasurementDto updateMeasurement(@RequestBody MeasurementDto dto) {
        Measurement measurement = modelMapper.map(dto, Measurement.class);
        return modelMapper.map(measurementsService.update(measurement), MeasurementDto.class);
    }

    /**
     * Удаление измерения по id
     *
     * @param id - id измерения
     * @return
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public SimpleResponse removeMeasurement(@PathVariable("id") Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Measurement id should be specified");
        }
        Optional<? extends Measurement> measurementOptional = measurementsService.findById(id);
        Measurement measurement = measurementOptional.orElseThrow(() -> new NoSuchElementException("Not found"));
        measurementsService.delete(measurement);
        return new SimpleResponse(HttpStatus.OK, "Done");
    }
}
