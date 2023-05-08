package ru.practicum.shareit.item.exception;

public class EmptyOwnerFieldException extends RuntimeException {
    public EmptyOwnerFieldException(String message) {
        super(message);
    }
}
