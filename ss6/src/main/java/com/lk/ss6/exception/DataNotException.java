package com.lk.ss6.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotException extends RuntimeException {

    public DataNotException() {
        super("资源不存在");
    }

    public DataNotException(String message) {
        super(message);
    }

    public DataNotException(String message, Throwable cause) {
        super(message, cause);
    }
}
