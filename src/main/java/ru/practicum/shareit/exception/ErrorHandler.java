package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    /*
        //400 ValidationException
        @ExceptionHandler //(ValidationException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ErrorResponse handleValidationException(ValidationException e) {
            log.info(e.getMessage());
            return new ErrorResponse("ошибка валидации", getMessage());
        }


        //400
        @ExceptionHandler //(BadStateException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ErrorResponse handleBadStateException(BadStateException e) {
            log.info(e.getMessage());
            return new ErrorResponse("ошибка запроса", e.getMessage());
        }

        //404
        @ExceptionHandler //(NotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ErrorResponse handleNotFoundException(NotFoundException e) {
            log.info(e.getMessage());
            return new ErrorResponse("объект не найден", e.getMessage());
        }


        //403 UserWrongException
        @ExceptionHandler //(UserWrongException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        public ErrorResponse handleUserWrongException(UserWrongException e) {
            log.info(e.getMessage());
            return new ErrorResponse("доступ ограничен", e.getMessage());
        }



    //500
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(Exception e) {
        log.info(e.getMessage());
        return new ErrorResponse("возникло исключение", e.getMessage());
    }
*/
}

