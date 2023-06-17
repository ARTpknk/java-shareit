package ru.practicum.shareit.exceptions.model;

public class EmptyOwnerFieldException extends RuntimeException {
    public EmptyOwnerFieldException(String message) {
        super(message);
    }
}
