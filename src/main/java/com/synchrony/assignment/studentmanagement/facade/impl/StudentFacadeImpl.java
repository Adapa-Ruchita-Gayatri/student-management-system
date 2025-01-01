package com.synchrony.assignment.studentmanagement.facade.impl;

import com.synchrony.assignment.studentmanagement.dto.request.CreateStudent;
import com.synchrony.assignment.studentmanagement.dto.response.StudentDto;
import com.synchrony.assignment.studentmanagement.facade.StudentFacade;
import com.synchrony.assignment.studentmanagement.mapper.StudentMapper;
import com.synchrony.assignment.studentmanagement.model.Student;
import com.synchrony.assignment.studentmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentFacadeImpl implements StudentFacade {
    @Autowired
    private StudentService studentService;

    public Student addStudent(CreateStudent createStudent) {
        Student student = studentService.addStudent(createStudent);
        return student;
    }

    public List<StudentDto> getStudentByName(String name) {
        List<Student> students = studentService.getStudentByName(name);
        List<StudentDto> studentDtos = students.stream().map((s) -> {
            return StudentMapper.toDto(s);
        }).collect(Collectors.toList());
        return studentDtos;
    }

    public Boolean deleteStudent(Long id) {
        return studentService.deleteStudent(id);
    }

    public Boolean updateStudentInfoById(CreateStudent createStudent, Long id) {
       return studentService.updateStudentInfoById(createStudent, id);
    }

    public List<StudentDto> getAllStudentsInfo(int limit, int offset) {
        List<Student> students = studentService.getAllStudentsInfo(limit, offset);
        List<StudentDto> studentDtos = students.stream().map((s) -> {
            return StudentMapper.toDto(s);
        }).collect(Collectors.toList());
        return studentDtos;
    }
}
