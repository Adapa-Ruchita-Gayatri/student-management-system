package com.synchrony.assignment.studentmanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class CreateStudent {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Age is required")
    private String age;

    @NotBlank(message = "Student class is required")
    private String studentClass;

    @NotBlank(message = "Phone number is required")
    private BigInteger phoneNumber;
}
