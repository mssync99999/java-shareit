package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//403
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserWrongException extends RuntimeException {
    public UserWrongException(String message) {
        super(message);
    }
}
