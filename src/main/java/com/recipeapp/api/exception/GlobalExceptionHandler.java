package com.recipeapp.api.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        detail.setTitle("Resource not found");
        detail.setType(URI.create("https://recipeapp.dev/errors/not-found"));
        detail.setProperty("timestamp", OffsetDateTime.now());
        return detail;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail handleBadRequest(BadRequestException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detail.setTitle("Bad request");
        detail.setType(URI.create("https://recipeapp.dev/errors/bad-request"));
        detail.setProperty("timestamp", OffsetDateTime.now());
        return detail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        detail.setTitle("Validation failed");
        detail.setDetail("One or more request fields are invalid.");
        detail.setType(URI.create("https://recipeapp.dev/errors/validation"));

        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        detail.setProperty("errors", errors);
        detail.setProperty("timestamp", OffsetDateTime.now());
        return detail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException exception) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        detail.setTitle("Constraint violation");
        detail.setType(URI.create("https://recipeapp.dev/errors/constraint-violation"));
        detail.setProperty("timestamp", OffsetDateTime.now());
        return detail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleIntegrityViolation(DataIntegrityViolationException exception) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        detail.setTitle("Database constraint violation");
        detail.setDetail("A database constraint was violated. Check for duplicate emails or invalid references.");
        detail.setType(URI.create("https://recipeapp.dev/errors/data-integrity"));
        detail.setProperty("timestamp", OffsetDateTime.now());
        return detail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception exception) {
        ProblemDetail detail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        detail.setTitle("Unexpected error");
        detail.setDetail(exception.getMessage());
        detail.setType(URI.create("https://recipeapp.dev/errors/internal"));
        detail.setProperty("timestamp", OffsetDateTime.now());
        return detail;
    }
}
