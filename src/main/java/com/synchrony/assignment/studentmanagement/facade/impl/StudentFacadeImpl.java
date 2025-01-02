package com.synchrony.assignment.studentmanagement.facade.impl;

import com.synchrony.assignment.studentmanagement.dto.request.CreateStudent;
import com.synchrony.assignment.studentmanagement.dto.response.StudentDto;
import com.synchrony.assignment.studentmanagement.facade.StudentFacade;
import com.synchrony.assignment.studentmanagement.mapper.StudentMapper;
import com.synchrony.assignment.studentmanagement.model.Student;
import com.synchrony.assignment.studentmanagement.service.RedisService;
import com.synchrony.assignment.studentmanagement.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudentFacadeImpl implements StudentFacade {
    @Autowired
    private StudentService studentService;

    @Autowired
    private RedisService<StudentDto> redisGenericService;

    public Student addStudent(CreateStudent createStudent) {
        Student student = studentService.addStudent(createStudent);
        return student;
    }

    private String getStudentKey(Long id) {
        return "student_" + id;
    }

    private String getNameResultsKey(String name) {
        return "search_" + name;
    }

    public StudentDto getStudentById(Long id) {
        String key = getStudentKey(id);
        if(redisGenericService.bucketExists(key)) {
            return redisGenericService.getBucket(key);
        }
        Student result = studentService.getStudentById(id);
        StudentDto modifiedResult = StudentMapper.toDto(result);
        redisGenericService.saveBucket(key, modifiedResult);
        return modifiedResult;
    }

    public List<StudentDto> getStudentByName(String name) {
        String redisNameResultsKey = getNameResultsKey(name);
         if(redisGenericService.listExists(redisNameResultsKey)) {
             return redisGenericService.getList(redisNameResultsKey);
         }
        List<Student> students = studentService.getStudentByName(name);
        List<StudentDto> studentDtos = students.stream().map((s) -> {
            return StudentMapper.toDto(s);
        }).collect(Collectors.toList());
        redisGenericService.saveList(redisNameResultsKey, studentDtos);
        return studentDtos;
    }

    public Boolean deleteStudent(Long id) {

        Boolean isDeleted = studentService.deleteStudent(id);
        String key = getStudentKey(id);
        if(isDeleted) {
            if(redisGenericService.bucketExists(key)) {
                redisGenericService.deleteBucket(key);
            }
        }
        return isDeleted;
    }

    public Boolean updateStudentInfoById(CreateStudent createStudent, Long id) {
        StudentDto initialInfo = getStudentById(id);
        Boolean isUpdated = studentService.updateStudentInfoById(createStudent, id);

       String key = getStudentKey(id);
       if(isUpdated) {
           StudentDto result = StudentMapper.fromRequest(createStudent, id);
           if(redisGenericService.bucketExists(key)) {
                redisGenericService.saveBucket(key, result);
           }
           String initialNameResultsKey = getNameResultsKey(initialInfo.getName());
           if(!Objects.equals(initialInfo.getName(), result.getName()) && redisGenericService.listExists(initialNameResultsKey)) {
               redisGenericService.deleteKeysByPattern(initialNameResultsKey);
           }
       }
       return isUpdated;
    }

    public List<StudentDto> getAllStudentsInfo(int limit, int offset) {
        List<Student> students = studentService.getAllStudentsInfo(limit, offset);
        List<StudentDto> studentDtos = students.stream().map((s) -> {
            return StudentMapper.toDto(s);
        }).collect(Collectors.toList());
        return studentDtos;
    }
}
