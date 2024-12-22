package ru.practicum.shareit.exception;

public class BadStateException extends IllegalArgumentException {
    public BadStateException(String message) {
        super(message);
    }
}
