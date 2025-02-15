package ru.practicum.shareit.exception;

public class NotUniqueEmail extends IllegalArgumentException {

    public NotUniqueEmail(String message) {
        super(message);
    }

}
