package com.ibm.certificationsserver.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFoundException(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(NotAllowedException.class)
    public  ResponseEntity notAllowedException(RuntimeException e)
    {
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

}
