package com.urlshortener.controllers;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.urlshortener.exceptions.EntityNotFoundException;
import com.urlshortener.exceptions.FailedSendMailException;
import com.urlshortener.exceptions.StandardException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardException> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        Object message = Arrays.stream(Objects.requireNonNull(exception.getDetailMessageArguments())).toArray()[1];
        StandardException standardException = new StandardException(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "method argument not valid",
                message.toString(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardException);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardException> handleEntityNotFound(EntityNotFoundException exception,
            HttpServletRequest request) {
        StandardException standardException = new StandardException(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "entity not found",
                exception.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardException);
    }

    @ExceptionHandler(FailedSendMailException.class)
    public ResponseEntity<StandardException> handleFailedSendMail(FailedSendMailException exception,
            HttpServletRequest request) {
        StandardException standardException = new StandardException(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "send email fail",
                exception.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standardException);
    }
}
