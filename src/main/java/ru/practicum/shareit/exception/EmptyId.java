package ru.practicum.shareit.exception;

public class EmptyId extends IllegalArgumentException {

    public EmptyId(String message) {
        super(message);
    }

}
