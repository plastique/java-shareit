package ru.practicum.shareit.exception;

public class InvalidOwnerException extends IllegalArgumentException {

    public InvalidOwnerException(String message) {
        super(message);
    }

}
