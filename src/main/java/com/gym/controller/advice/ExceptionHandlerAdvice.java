package com.gym.controller.advice;

import com.gym.domain.exception.ExceptionMessage;
import com.gym.domain.exception.ResourceNotFoundException;
import com.gym.domain.exception.UserNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleResourceNotFoundException(ResourceNotFoundException exp,
                                                                            HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), exp.getMessage());
        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleResourceNotFoundException(NoResultException exp,
                                                                            HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), "No result(s) found.");
        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleResourceNotFoundException(UserNotFoundException exp,
                                                                            HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), "User not found.");
        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionMessage> handleResourceNotFoundException(MethodArgumentNotValidException exp,
                                                                            HttpServletRequest req){
        var response = new ExceptionMessage(HttpStatus.BAD_REQUEST.value(), req.getRequestURI(), "Invalid data.");
        return new ResponseEntity(response,HttpStatus.BAD_REQUEST);
    }

}
