package com.quasar.operation.controllers.v1;

import com.quasar.operation.exceptions.RequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({RequestException.class})
    public ResponseEntity<RequestException> customRequestException(RequestException e) {
        return getEntityFromRequestException(e);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<RequestException> methodNotAllowed() {
        return getEntityFromRequestException(new RequestException(METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<RequestException> notFound() {
        return getEntityFromRequestException(new RequestException(NOT_FOUND));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class,
            ValidationException.class})
    public ResponseEntity<RequestException> badRequest(Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            String errors = ((MethodArgumentNotValidException) e).getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .distinct()
                    .collect(joining(" "));
            return getEntityFromRequestException(new RequestException(BAD_REQUEST, errors));
        } else if (e instanceof ConstraintViolationException) {
            String errors = ((ConstraintViolationException) e).getConstraintViolations()
                    .stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(joining(" "));
            return getEntityFromRequestException(new RequestException(BAD_REQUEST, errors));
        } else {
            return getEntityFromRequestException(new RequestException(BAD_REQUEST));
        }
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<RequestException> otherExceptions(Exception e) {
        LOGGER.error("Unexpected exception: ", e);
        return getEntityFromRequestException(new RequestException(INTERNAL_SERVER_ERROR));
    }

    private ResponseEntity<RequestException> getEntityFromRequestException(RequestException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e);
    }
}
