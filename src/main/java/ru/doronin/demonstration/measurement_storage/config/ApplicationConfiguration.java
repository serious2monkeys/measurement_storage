package ru.doronin.demonstration.measurement_storage.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import ru.doronin.demonstration.measurement_storage.api.dto.MeasurementDto;
import ru.doronin.demonstration.measurement_storage.measurement.base.Measurement;
import ru.doronin.demonstration.measurement_storage.measurement.jpa.ElectricityMeasurementImpl;
import ru.doronin.demonstration.measurement_storage.measurement.jpa.HeatingMeasurementImpl;
import ru.doronin.demonstration.measurement_storage.measurement.jpa.MeasurementImpl;
import ru.doronin.demonstration.measurement_storage.measurement.jpa.WaterMeasurementImpl;
import ru.doronin.demonstration.measurement_storage.persistance.base.Domain;

import java.time.LocalDateTime;

/**
 * Общие настройки приложения
 */
@Configuration
public class ApplicationConfiguration {
    /**
     * Настройка Jackson-преобразователя в json
     *
     * @return
     */
    @Bean
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        return mapper;
    }

    /**
     * Настройки преобразователя объектов
     *
     * @return - настроенный преобразователь
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<Measurement, MeasurementDto> typeMap = modelMapper.createTypeMap(Measurement.class, MeasurementDto.class)
                .addMapping(source -> source.getOwner().getLogin(), MeasurementDto::setOwner)
                .addMapping(Domain::getId, MeasurementDto::setId);

        typeMap.include(MeasurementImpl.class, MeasurementDto.class)
                .include(WaterMeasurementImpl.class, MeasurementDto.class)
                .include(HeatingMeasurementImpl.class, MeasurementDto.class)
                .include(ElectricityMeasurementImpl.class, MeasurementDto.class);

        modelMapper.createTypeMap(MeasurementDto.class, Measurement.class)
                .setConverter(createDtoConverter())
                .include(MeasurementDto.class, MeasurementImpl.class)
                .include(MeasurementDto.class, WaterMeasurementImpl.class)
                .include(MeasurementDto.class, ElectricityMeasurementImpl.class)
                .include(MeasurementDto.class, HeatingMeasurementImpl.class);

        return modelMapper;
    }

    /**
     * Настройка обратного конвертера
     *
     * @return
     */
    private Converter<MeasurementDto, Measurement> createDtoConverter() {
        return context -> {
            MeasurementDto source = context.getSource();
            Measurement output;
            switch (source.getType()) {
                case ELECTRICITY:
                    output = new ElectricityMeasurementImpl();
                    break;
                case WATER:
                    output = new WaterMeasurementImpl();
                    break;
                case HEATING:
                    output = new HeatingMeasurementImpl();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown measurement type");
            }
            if (!StringUtils.isEmpty(source.getRegistered())) {
                output.setRegistered(LocalDateTime.parse(source.getRegistered()));
            }
            output.setType(source.getType());
            output.setId(source.getId());
            output.setValue(source.getValue());
            return output;
        };
    }

}
