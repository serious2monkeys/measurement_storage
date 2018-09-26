package ru.doronin.demonstration.measurement_storage.api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * Простая обертка для сообщений об ошибках
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SimpleApiError {
    private HttpStatus status;
    private String message;
}
