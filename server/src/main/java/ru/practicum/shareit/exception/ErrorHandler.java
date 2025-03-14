package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    private static final String ERROR_LOG_MESSAGE = "Server error ({}): {}";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.warn(ERROR_LOG_MESSAGE, HttpStatus.NOT_FOUND, e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFoundException(final EmptyIdException e) {
        log.warn(ERROR_LOG_MESSAGE, HttpStatus.BAD_REQUEST, e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFoundException(final InvalidOwnerException e) {
        log.warn(ERROR_LOG_MESSAGE, HttpStatus.BAD_REQUEST, e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotFoundException(final NotUniqueEmailException e) {
        log.warn(ERROR_LOG_MESSAGE, HttpStatus.CONFLICT, e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOtherException(final Throwable e) {
        log.warn(ERROR_LOG_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOtherException(final InvalidBookingStatusException e) {
        log.warn(ERROR_LOG_MESSAGE, HttpStatus.BAD_REQUEST, e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleOtherException(final UserDoesNotHaveBookedItem e) {
        log.warn(ERROR_LOG_MESSAGE, HttpStatus.BAD_REQUEST, e.getMessage());

        return new ErrorResponse(e.getMessage());
    }

}
