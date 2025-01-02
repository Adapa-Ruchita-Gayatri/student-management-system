package com.synchrony.assignment.studentmanagement.service;

import com.synchrony.assignment.studentmanagement.dto.request.CreateStudent;
import com.synchrony.assignment.studentmanagement.mapper.StudentMapper;
import com.synchrony.assignment.studentmanagement.model.Student;
import com.synchrony.assignment.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public Student addStudent(CreateStudent createStudent) {
        Student student = studentRepository.save(StudentMapper.toEntity(createStudent));
        return student;
    }

    public Student getStudentById(Long id) {
        Optional<Student> result =  studentRepository.findById(id);
        if(result.isPresent()) {
            return result.get();
        }
        throw new RuntimeException("Student not found");
    }

    public List<Student> getStudentByName(String name) {
        return studentRepository.findByNameContaining(name);
    }

    public boolean deleteStudent(Long id) {
        Boolean isDeleted = studentRepository.deleteByStudentId(id) > 0;
        if(isDeleted) {
            return isDeleted;
        } else {
            throw new RuntimeException("Record is not deleted or not found");
        }
    }

    public boolean updateStudentInfoById(CreateStudent createStudent, Long id) {
        Boolean isUpdated = studentRepository.updateById(createStudent.getName(), createStudent.getAge(), createStudent.getStudentClass(), createStudent.getPhoneNumber(), id) > 0;
        if(isUpdated) {
           return isUpdated;
        } else {
            throw new RuntimeException("Record is not updated or not found");
        }
    }

    public List<Student> getAllStudentsInfo(int limit, int offset, String name) {
        return studentRepository.findAllStudentsInfo(limit, offset, name);
    }
}
