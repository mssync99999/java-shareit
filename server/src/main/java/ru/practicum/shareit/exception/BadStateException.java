package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//400
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadStateException extends IllegalArgumentException {
    public BadStateException(String message) {
        super(message);
    }
}
