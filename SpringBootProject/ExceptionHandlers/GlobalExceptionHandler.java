package com.hololitt.SpringBootProject.ExceptionHandlers;

import com.hololitt.SpringBootProject.exceptions.LanguageCardNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException exception){
        System.out.println(exception.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
@ExceptionHandler(LanguageCardNotFoundException.class)
    public ResponseEntity<String> handleLangaugeCardNotFoundException(LanguageCardNotFoundException exception){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}
}
