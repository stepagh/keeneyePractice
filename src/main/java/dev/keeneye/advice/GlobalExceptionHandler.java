package dev.keeneye.advice;

import dev.keeneye.exceptions.*;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(ResourceNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(problemDetail);
    }

    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ProblemDetail> handleInvalidEntity(InvalidEntityException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(problemDetail);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleUsernameAlreadyExists(UsernameAlreadyExistsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(GroupNotEmptyBeforeDeletionException.class)
    public ResponseEntity<ProblemDetail> handleGroupNotEmptyBeforeDeletion(GroupNotEmptyBeforeDeletionException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<ProblemDetail> handleRefreshTokenExpired(RefreshTokenExpiredException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    @ExceptionHandler(FileIsEmptyException.class)
    public ResponseEntity<ProblemDetail> handleFileIsEmpty(FileIsEmptyException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(CsvParsingException.class)
    public ResponseEntity<ProblemDetail> handleFileIsEmpty(CsvParsingException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(InvalidFileFormatException.class)
    public ResponseEntity<ProblemDetail> handleFileIsEmpty(InvalidFileFormatException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ProblemDetail> handleIllegalState(IllegalStateException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ProblemDetail> handleFileIsEmpty(IncorrectPasswordException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
    @ExceptionHandler(CsvProcessingException.class)
    public ResponseEntity<ProblemDetail> handleCsvProcessingException(CsvProcessingException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                e.getMessage()
        );
        problemDetail.setProperty("errors", e.getErrors());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

}
