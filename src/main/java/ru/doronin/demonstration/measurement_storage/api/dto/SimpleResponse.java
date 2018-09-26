package ru.doronin.demonstration.measurement_storage.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Объект для передачи простых сообщений
 */
@Data
@AllArgsConstructor
public class SimpleResponse {
    private HttpStatus status;
    private String message;
}
