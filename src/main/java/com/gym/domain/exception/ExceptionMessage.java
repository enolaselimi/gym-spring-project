package com.gym.domain.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ExceptionMessage {
    private LocalDateTime date = LocalDateTime.now();
    private Integer statusCode;
    private String path;
    private String message;

    public ExceptionMessage(Integer statusCode, String path, String message) {
        this.statusCode = statusCode;
        this.path = path;
        this.message = message;
    }
}
