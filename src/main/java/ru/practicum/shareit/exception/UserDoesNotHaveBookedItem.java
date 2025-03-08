package ru.practicum.shareit.exception;

public class UserDoesNotHaveBookedItem extends RuntimeException {

    public UserDoesNotHaveBookedItem(String message) {
        super(message);
    }

}
