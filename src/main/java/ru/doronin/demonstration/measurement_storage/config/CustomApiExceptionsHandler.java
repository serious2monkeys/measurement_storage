package ru.doronin.demonstration.measurement_storage.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.doronin.demonstration.measurement_storage.api.exceptions.SimpleApiError;

/**
 * Простейшая обработка ошибок при работе с API
 */
@ControllerAdvice
public class CustomApiExceptionsHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers, HttpStatus status,
                                                                          WebRequest request) {
        return new ResponseEntity<>(new SimpleApiError(HttpStatus.BAD_REQUEST, ex.getMessage()),
                headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return new ResponseEntity<>(new SimpleApiError(HttpStatus.FORBIDDEN, ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> defaultHandler(Exception ex, HttpHeaders headers) {
        return new ResponseEntity<>(new SimpleApiError(HttpStatus.BAD_REQUEST, ex.getMessage()),
                headers, HttpStatus.BAD_REQUEST);
    }
}
