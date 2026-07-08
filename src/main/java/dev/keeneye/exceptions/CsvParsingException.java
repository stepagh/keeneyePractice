package dev.keeneye.exceptions;

public class CsvParsingException extends RuntimeException {
    public CsvParsingException(String message) {
        super(message);
    }
}
