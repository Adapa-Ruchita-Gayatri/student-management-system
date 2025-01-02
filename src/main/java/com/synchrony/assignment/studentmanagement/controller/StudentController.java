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

    /**
     * Adds a new student to the system.
     * 
     * This endpoint creates a new student record based on the provided information.
     * 
     * @param createStudent The CreateStudent object containing the details of the student to be added.
     *                      This object should include all necessary information for creating a new student record.
     * @return ResponseWrapper<Student> A wrapper object containing:
     *         - A boolean indicating the success of the operation (true in this case)
     *         - The newly created Student object
     *         - An error message (which is set to a default "No error Recorded" in this case)
     */
    @PostMapping
    public ResponseWrapper<Student> addStudent(@RequestBody CreateStudent createStudent) {
        return new ResponseWrapper<>(true, studentFacade.addStudent(createStudent), NO_ERROR_MSG);
    }

    /**
     * Retrieves a list of students by their name.
     * 
     * This endpoint searches for students whose names match the provided name parameter
     * and returns their information as a list of StudentDto objects.
     * 
     * @param name The name of the student(s) to search for. This is a path variable
     *             and should be provided as part of the URL.
     * @return ResponseWrapper<List<StudentDto>> A wrapper object containing:
     *         - A boolean indicating the success of the operation (true in this case)
     *         - A List of StudentDto objects representing the matching students
     *         - An error message (which is set to a default "No error Recorded" in this case)
     */
    @GetMapping("/{name}")
    public ResponseWrapper<List<StudentDto>> getStudentByName(@PathVariable String name) {
        return new ResponseWrapper<>(true, studentFacade.getStudentByName(name), NO_ERROR_MSG);
    }

    /**
     * Deletes a student from the system based on their ID.
     * 
     * This endpoint removes a student record from the database using the provided student ID.
     * 
     * @param id The unique identifier of the student to be deleted. This is a path variable
     *           and should be provided as part of the URL.
     * @return ResponseWrapper<Boolean> A wrapper object containing:
     *         - A boolean indicating the success of the operation (true if successful)
     *         - A Boolean value indicating whether the deletion was successful (true) or not (false)
     *         - An error message (which is set to a default "No error Recorded" in this case)
     */
    @DeleteMapping("/{id}")
    public ResponseWrapper<Boolean> deleteStudent(@PathVariable Long id) {
        return new ResponseWrapper<>(true, studentFacade.deleteStudent(id), NO_ERROR_MSG);
    }

    /**
     * Updates the information of an existing student in the system.
     * 
     * This endpoint modifies the details of a student record identified by the provided ID.
     * 
     * @param createStudent The CreateStudent object containing the updated information for the student.
     *                      This object should include all the fields that need to be updated.
     * @param id The unique identifier of the student whose information needs to be updated.
     *           This is a path variable and should be provided as part of the URL.
     * @return ResponseWrapper<Boolean> A wrapper object containing:
     *         - A boolean indicating the success of the operation (true in this case)
     *         - A Boolean value indicating whether the update was successful (true) or not (false)
     *         - An error message (which is set to a default "No error Recorded" in this case)
     */
    @PutMapping("/{id}")
    public ResponseWrapper<Boolean> updateStudentInfoById(@RequestBody CreateStudent createStudent, @PathVariable Long id) {
        return new ResponseWrapper<>(true, studentFacade.updateStudentInfoById(createStudent, id), NO_ERROR_MSG);
    }

    /**
     * Retrieves a paginated list of all students' information, optionally filtered by name.
     * 
     * This endpoint returns a list of students, with pagination support and the ability to
     * filter results by a student's name. The results are returned as StudentDto objects.
     * 
     * @param limit The maximum number of student records to return in a single request.
     *              This parameter is used for pagination to control the size of each page.
     * @param offset The number of records to skip before starting to return results.
     *               This parameter is used for pagination to determine which page of results to return.
     * @param name A string to filter students by name. If provided, only students whose names
     *             contain this string (case-insensitive) will be returned.
     * @return ResponseWrapper<List<StudentDto>> A wrapper object containing:
     *         - A boolean indicating the success of the operation (true in this case)
     *         - A List of StudentDto objects representing the matching students
     *         - An error message (which is set to a default "No error Recorded" in this case)
     */
    @GetMapping("/all")
    public ResponseWrapper<List<StudentDto>> getAllStudentsInfo(@RequestParam Integer limit, @RequestParam Integer offset, @RequestParam String name) {
        return new ResponseWrapper<>(true, studentFacade.getAllStudentsInfo(limit, offset, name), NO_ERROR_MSG);
    }
}
