package com.bookmanagement.app.exception;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    
    public ApiError() {
    }
    
    public ApiError(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
}
