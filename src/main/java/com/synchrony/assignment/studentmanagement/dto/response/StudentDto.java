package com.synchrony.assignment.studentmanagement.dto.response;

import lombok.Data;

@Data
public class StudentDto {
    private Long id;
    private String name;
    private String age;
    private String studentClass;
    private String phoneNumber;
}
