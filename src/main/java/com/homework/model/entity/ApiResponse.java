package com.homework.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private Boolean success;
    private String message;
    private HttpStatus status;
    private T payload;
    private ZonedDateTime timestamp;
}
