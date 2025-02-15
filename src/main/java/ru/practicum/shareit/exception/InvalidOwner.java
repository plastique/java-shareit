package ru.practicum.shareit.exception;

public class InvalidOwner extends IllegalArgumentException {

    public InvalidOwner(String message) {
        super(message);
    }

}
