package com.nttdata.beca.exception.handler;

import com.nttdata.beca.exception.BecaTrainingNotFoundException;
import com.nttdata.beca.exception.records.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class BecaTrainingExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BecaTrainingNotFoundException.class})
    public ResponseEntity<ErrorResponse> BecaNotFoundHandler(BecaTrainingNotFoundException exception){
        HttpStatus noContent = HttpStatus.NOT_FOUND;
        ErrorResponse notFoundException = new ErrorResponse(exception.getMessage(),noContent, LocalDateTime.now());
        return new ResponseEntity<>(notFoundException,noContent);
    }
}
