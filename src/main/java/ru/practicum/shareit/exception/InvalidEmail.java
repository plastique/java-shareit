package ru.practicum.shareit.exception;

public class InvalidEmail extends IllegalArgumentException {

    public InvalidEmail(String message) {
        super(message);
    }

}
