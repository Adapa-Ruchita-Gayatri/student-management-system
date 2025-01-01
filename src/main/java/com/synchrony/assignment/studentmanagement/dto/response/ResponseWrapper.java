package com.synchrony.assignment.studentmanagement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseWrapper<T> {
    private boolean success;
    private T data;
    private String message;
}
