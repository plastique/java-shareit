package ru.practicum.shareit.exception;

public class InvalidEmailException extends IllegalArgumentException {

    public InvalidEmailException(String message) {
        super(message);
    }

}
