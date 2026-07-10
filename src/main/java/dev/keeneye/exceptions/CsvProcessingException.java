package dev.keeneye.exceptions;

import java.util.List;

public class CsvProcessingException extends RuntimeException {
    private final List<String> errors;

    public CsvProcessingException(String message, List<String> errors) {
        super(message);
        this.errors = errors != null ? List.copyOf(errors) : List.of();
    }

    public List<String> getErrors() {
        return errors;
    }
}
