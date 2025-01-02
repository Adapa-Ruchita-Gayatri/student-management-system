package com.synchrony.assignment.studentmanagement.service;

import com.synchrony.assignment.studentmanagement.dto.request.CreateStudent;
import com.synchrony.assignment.studentmanagement.exception.StudentNotFoundException;
import com.synchrony.assignment.studentmanagement.exception.StudentRecordDeletionException;
import com.synchrony.assignment.studentmanagement.exception.StudentRecordUpdateException;
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

    /**
     * Adds a new student to the database.
     * 
     * This method takes a CreateStudent object, converts it to a Student entity,
     * saves it to the database, and returns the saved Student object.
     *
     * @param createStudent The CreateStudent object containing the details of the student to be added.
     *                      This object should contain all necessary information for creating a new student.
     * @return The Student object that was saved to the database, including any auto-generated fields (e.g., ID).
     */
    public Student addStudent(CreateStudent createStudent) {
        Student student = studentRepository.save(StudentMapper.toEntity(createStudent));
        return student;
    }

    /**
     * Retrieves a student from the database by their ID.
     * 
     * This method searches for a student in the database using the provided ID.
     * If found, it returns the Student object. If not found, it throws a
     * StudentNotFoundException.
     *
     * @param id The unique identifier of the student to retrieve.
     * @return The Student object corresponding to the given ID.
     * @throws StudentNotFoundException If no student is found with the given ID.
     */
    public Student getStudentById(Long id) {
        Optional<Student> result =  studentRepository.findById(id);
        if(result.isPresent()) {
            return result.get();
        }
        throw new StudentNotFoundException("Student record not found");
    }

    /**
     * Retrieves a list of students from the database whose names contain the specified string.
     * 
     * This method searches for students in the database whose names partially match the provided name parameter.
     * The search is case-insensitive and returns all students whose names contain the given string.
     *
     * @param name The string to search for within student names. This parameter is case-insensitive.
     * @return A List of Student objects whose names contain the specified string. 
     *         Returns an empty list if no matching students are found.
     */
    public List<Student> getStudentByName(String name) {
        return studentRepository.findByNameContaining(name);
    }
    /**
     * Deletes a student from the database based on their ID.
     * 
     * This method attempts to delete a student record from the database using the provided ID.
     * If the deletion is successful, it returns true. If the student record is not found or
     * the deletion fails, it throws a StudentRecordDeletionException.
     *
     * @param id The unique identifier of the student to be deleted.
     * @return true if the student record was successfully deleted.
     * @throws StudentRecordDeletionException If the student record could not be deleted or was not found.
     */
    public boolean deleteStudent(Long id) {
        Boolean isDeleted = studentRepository.deleteByStudentId(id) > 0;
        if(isDeleted) {
            return isDeleted;
        } else {
            throw new StudentRecordDeletionException("Student Record is not deleted or not found");
        }
    }

    /**
     * Updates the information of a student in the database based on their ID.
     * 
     * This method attempts to update a student's record in the database using the provided
     * CreateStudent object and student ID. If the update is successful, it returns true.
     * If the student record is not found or the update fails, it throws a
     * StudentRecordUpdateException.
     *
     * @param createStudent The CreateStudent object containing the updated information
     *                      for the student. This should include the new name, age,
     *                      class, and phone number.
     * @param id The unique identifier of the student whose information is to be updated.
     * @return true if the student record was successfully updated.
     * @throws StudentRecordUpdateException If the student record could not be updated or was not found.
     */
    public boolean updateStudentInfoById(CreateStudent createStudent, Long id) {
        Boolean isUpdated = studentRepository.updateById(createStudent.getName(), createStudent.getAge(), createStudent.getStudentClass(), createStudent.getPhoneNumber(), id) > 0;
        if(isUpdated) {
           return isUpdated;
        } else {
            throw new StudentRecordUpdateException("Student Record is not updated or not found");
        }
    }

    /**
     * Retrieves a list of students from the database with pagination and optional name filtering.
     * 
     * This method fetches student information from the database, applying pagination parameters
     * and an optional name filter. It allows for efficient retrieval of large datasets by 
     * limiting the number of records returned and specifying an offset.
     *
     * @param limit The maximum number of student records to return. This parameter is used for 
     *              pagination to control the size of the result set.
     * @param offset The number of records to skip before starting to return the results. This 
     *               parameter is used for pagination to determine the starting point of the result set.
     * @param name An optional parameter to filter students by name. If provided, only students 
     *             whose names contain this string (case-insensitive) will be returned. If null 
     *             or empty, no name filtering is applied.
     * @return A List of Student objects matching the specified criteria. The list is limited 
     *         by the 'limit' parameter and starts from the position specified by the 'offset' 
     *         parameter. If no students match the criteria, an empty list is returned.
     */
    public List<Student> getAllStudentsInfo(Integer limit, Integer offset, String name) {
        return studentRepository.findAllStudentsInfo(limit, offset, name);
    }
}
