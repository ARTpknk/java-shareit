package ru.practicum.shareit.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ShareItBadRequest extends RuntimeException {
    public ShareItBadRequest(String message) {
        super(message);
    }
}
