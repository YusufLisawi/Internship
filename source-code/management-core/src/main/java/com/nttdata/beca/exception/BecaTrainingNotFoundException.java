package com.nttdata.beca.exception;

import lombok.Data;

import java.util.List;


public class BecaTrainingNotFoundException extends RuntimeException{

    public BecaTrainingNotFoundException(String message) {
        super(message);
    }
}
