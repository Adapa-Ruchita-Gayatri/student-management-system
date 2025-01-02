package com.synchrony.assignment.studentmanagement.controller;

import com.synchrony.assignment.studentmanagement.dto.request.CreateStudent;
import com.synchrony.assignment.studentmanagement.dto.response.ResponseWrapper;
import com.synchrony.assignment.studentmanagement.dto.response.StudentDto;
import com.synchrony.assignment.studentmanagement.facade.StudentFacade;
import com.synchrony.assignment.studentmanagement.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/student")
public class StudentController {

    private static final String NO_ERROR_MSG = "No error Recorded";

    @Autowired
    private StudentFacade studentFacade;

    @PostMapping
    public ResponseWrapper<Student> addStudent(@RequestBody CreateStudent createStudent) {
        return new ResponseWrapper<>(true, studentFacade.addStudent(createStudent), NO_ERROR_MSG);
    }

    @GetMapping("/{name}")
    public ResponseWrapper<List<StudentDto>> getStudentByName(@PathVariable String name) {
        return new ResponseWrapper<>(true, studentFacade.getStudentByName(name), NO_ERROR_MSG);
    }

    @DeleteMapping("/{id}")
    public ResponseWrapper<Boolean> deleteStudent(@PathVariable Long id) {
        return new ResponseWrapper<>(true, studentFacade.deleteStudent(id), NO_ERROR_MSG);
    }

    @PutMapping("/{id}")
    public ResponseWrapper<Boolean> updateStudentInfoById(@RequestBody CreateStudent createStudent, @PathVariable Long id) {
        return new ResponseWrapper<>(true, studentFacade.updateStudentInfoById(createStudent, id), NO_ERROR_MSG);
    }

    @GetMapping("/all")
    public ResponseWrapper<List<StudentDto>> getAllStudentsInfo(@RequestParam String sortField, @RequestParam String sortOrder, @RequestParam int limit, @RequestParam int offset) {
        return new ResponseWrapper<>(true, studentFacade.getAllStudentsInfo(limit, offset), NO_ERROR_MSG);
    }
}
