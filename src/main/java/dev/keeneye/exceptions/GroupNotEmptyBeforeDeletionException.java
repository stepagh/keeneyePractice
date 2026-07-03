package dev.keeneye.exceptions;

public class GroupNotEmptyBeforeDeletionException extends RuntimeException {
    public GroupNotEmptyBeforeDeletionException(String message) {
        super(message);
    }
}
