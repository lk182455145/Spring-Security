package com.lk.ss2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleException(final MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        System.out.println("----------------111111---------------------------");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if(error instanceof FieldError){
                errorMap.put(((FieldError) error).getField(),error.getDefaultMessage());
            }else {
                errorMap.put("error",error.getDefaultMessage());
            }
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }

}
