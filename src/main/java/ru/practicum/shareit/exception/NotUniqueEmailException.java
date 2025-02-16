package ru.practicum.shareit.exception;

public class NotUniqueEmailException extends IllegalArgumentException {

    public NotUniqueEmailException(String message) {
        super(message);
    }

}
