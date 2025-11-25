package com.etuniesp.fabrica_software.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> notFound(EntityNotFoundException ex) {
        return Map.of("timestamp", Instant.now(), "error", "NOT_FOUND", "message", ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> conflict(DataIntegrityViolationException ex) {
        return Map.of("timestamp", Instant.now(), "error", "CONFLICT", "message", ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> illegalState(IllegalStateException ex) {
        return Map.of("timestamp", Instant.now(), "error", "BAD_REQUEST", "message", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> illegalArgument(IllegalArgumentException ex) {
        return Map.of("timestamp", Instant.now(), "error", "BAD_REQUEST", "message", ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> runtimeException(RuntimeException ex) {
        // Log detalhado para debug
        ex.printStackTrace();
        return Map.of(
            "timestamp", Instant.now(), 
            "error", "INTERNAL_SERVER_ERROR", 
            "message", ex.getMessage() != null ? ex.getMessage() : "Erro interno do servidor",
            "cause", ex.getCause() != null ? ex.getCause().getMessage() : "N/A"
        );
    }
}
