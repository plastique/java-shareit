package ru.practicum.shareit.exception;

public class InvalidBookingStatusException extends IllegalArgumentException {

    public InvalidBookingStatusException(String message) {
        super(message);
    }

}
