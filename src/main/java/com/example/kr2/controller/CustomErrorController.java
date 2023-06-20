package com.example.kr2.controller;

import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleBindErrors(MethodArgumentNotValidException exception, Model model) {
        Map<String, String> errorMap = new HashMap<>();

        exception.getFieldErrors().forEach(fieldError -> errorMap.put(fieldError.getField(), fieldError.getDefaultMessage()));
        model.addAttribute("errors", errorMap);
        return "error";
    }
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(Model model) {
        model.addAttribute("errorMessage", "The requested criminal does not exist.");
        return "error";
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolationException(ConstraintViolationException exception, Model model) {
        Map<String, String> errorMap = new HashMap<>();

        exception.getConstraintViolations().forEach(violation -> {
            String field = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
            String message = violation.getMessage();
            errorMap.put(field, message);
        });

        model.addAttribute("errors", errorMap);
        return "error";
    }

}

