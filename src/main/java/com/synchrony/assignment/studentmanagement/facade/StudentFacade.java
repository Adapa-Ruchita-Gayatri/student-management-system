package com.synchrony.assignment.studentmanagement.facade;

import com.synchrony.assignment.studentmanagement.dto.request.CreateStudent;
import com.synchrony.assignment.studentmanagement.dto.response.StudentDto;
import com.synchrony.assignment.studentmanagement.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentFacade {
    Student addStudent(CreateStudent createStudent);
    List<StudentDto> getStudentByName(String name);
    Boolean deleteStudent(Long id);
    Boolean updateStudentInfoById(CreateStudent createStudent, Long id);
    List<StudentDto> getAllStudentsInfo(int limit, int offset, String name);
}
