package ru.practicum.shareit.exception;

public class ItemUnavailableException extends IllegalArgumentException {

    public ItemUnavailableException(String message) {
        super(message);
    }

}
