package com.nttdata.beca.exception.records;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse(String Message, HttpStatus httpStatus, LocalDateTime timeStamp) {
}
