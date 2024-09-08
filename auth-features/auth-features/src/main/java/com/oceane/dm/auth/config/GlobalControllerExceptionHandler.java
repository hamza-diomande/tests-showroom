package com.oceane.dm.auth.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Class used to intercept exception and turn them into {@link ResponseEntity}.
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    /**
     * Intercept {@link MethodArgumentNotValidException} and return an error 400
     *
     * @param e the exception
     * @return the response entity
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String error = e.getAllErrors().stream()
                .findFirst()
                .map(ObjectError::getDefaultMessage)
                .orElse("");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Intercept {@link ConstraintViolationException} and return an error 400
     *
     * @param e the exception
     * @return the response entity
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        String error = e.getConstraintViolations().stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
