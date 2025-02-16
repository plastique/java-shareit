package ru.practicum.shareit.exception;

public class EmptyIdException extends IllegalArgumentException {

    public EmptyIdException(String message) {
        super(message);
    }

}
