package dev.keeneye.exceptions;

public class CsvRowProcessingException extends RuntimeException {
    public CsvRowProcessingException(String message) {
        super(message);
    }
}
