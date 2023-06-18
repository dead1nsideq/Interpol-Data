package com.example.kr2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Value for Criminal record or language is blank")
public class ValidationException extends RuntimeException {
}
