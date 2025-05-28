package project.ottshare.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.ottshare.dto.error.ErrorResponse;
import project.ottshare.exception.OttSharingRoomNotFoundException;
import project.ottshare.exception.SharingUserNotFoundException;
import project.ottshare.exception.UserNotFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        log.error(e.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "User not found");
    }

    @ExceptionHandler(SharingUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleSharingUserNotFoundException(SharingUserNotFoundException e) {
        log.error(e.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Sharing user not found");
    }

    @ExceptionHandler(OttSharingRoomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleOttSharingRoomNotFoundException(OttSharingRoomNotFoundException e) {
        log.error(e.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Ott sharing room not found");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(Exception e) {
        log.error("Unknown error occurred", e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unknown error occurred");
    }

    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message) {
        });
    }
}
