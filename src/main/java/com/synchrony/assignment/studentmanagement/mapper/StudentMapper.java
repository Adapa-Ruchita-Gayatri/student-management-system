package com.synchrony.assignment.studentmanagement.mapper;

import com.synchrony.assignment.studentmanagement.dto.request.CreateStudent;
import com.synchrony.assignment.studentmanagement.dto.response.StudentDto;
import com.synchrony.assignment.studentmanagement.model.Student;

public class StudentMapper {
    public static Student toEntity (CreateStudent createStudent) {
        Student s = new Student();
        s.setName(createStudent.getName());
        s.setAge(createStudent.getAge());
        s.setStudentClass(createStudent.getStudentClass());
        s.setPhoneNumber(createStudent.getPhoneNumber());
        return s;
    }

    public static StudentDto toDto (Student student) {
        StudentDto s = new StudentDto();
        s.setId(student.getId());
        s.setName(student.getName());
        s.setAge(student.getAge());
        s.setStudentClass(student.getStudentClass());
        s.setPhoneNumber(student.getPhoneNumber());
        return s;
    }

    public static StudentDto fromRequest(CreateStudent createStudent, Long id) {
        StudentDto s = new StudentDto();
        s.setId(id);
        s.setName(createStudent.getName());
        s.setAge(createStudent.getAge());
        s.setStudentClass(createStudent.getStudentClass());
        s.setPhoneNumber(createStudent.getPhoneNumber());
        return s;
    }
}
