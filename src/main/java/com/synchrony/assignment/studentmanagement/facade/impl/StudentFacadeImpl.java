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
    
    
    /**
     * Adds a new student to the system.
     * 
     * This method takes a CreateStudent object containing the new student's information,
     * passes it to the studentService to create a new Student entity, and returns the
     * created Student object.
     *
     * @param createStudent A CreateStudent object containing the information for the new student.
     * @return A Student object representing the newly created student in the system.
     */
    public Student addStudent(CreateStudent createStudent) {
        Student student = studentService.addStudent(createStudent);
        return student;
    }

    
    /**
     * Generates a unique key for a student based on their ID.
     * This key is used for caching student information in Redis.
     *
     * @param id The unique identifier of the student.
     * @return A String representing the Redis key for the student, in the format "student_[id]".
     */
    private String getStudentKey(Long id) {
        return "student_" + id;
    }

    /**
     * Generates a unique key for caching search results based on a student's name.
     * This key is used for storing and retrieving search results in Redis.
     *
     * @param name The name of the student used in the search query.
     * @return A String representing the Redis key for the search results, 
     *         in the format "search_[name]".
     */
    private String getNameResultsKey(String name) {
        return "search_" + name;
    }

    /**
     * Retrieves a student's information by their ID, utilizing Redis caching for improved performance.
     * 
     * This method first checks if the student's data is available in the Redis cache.
     * If found, it returns the cached data. Otherwise, it fetches the data from the
     * primary data source, caches it in Redis for future use, and then returns it.
     *
     * @param id The unique identifier of the student to retrieve.
     * @return A StudentDto object containing the student's information. If the student
     *         is found in the cache, the cached data is returned. Otherwise, the data
     *         is fetched from the primary source, cached, and then returned.
     */
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
    /**
     * Retrieves a list of students by their name, utilizing Redis caching for improved performance.
     * 
     * This method first checks if the search results for the given name are available in the Redis cache.
     * If found, it returns the cached data. Otherwise, it fetches the data from the primary data source,
     * converts it to DTOs, caches the results in Redis for future use, and then returns them.
     *
     * @param name The name of the student(s) to search for.
     * @return A List of StudentDto objects containing information about students matching the given name.
     *         If the results are found in the cache, the cached data is returned. Otherwise, the data
     *         is fetched from the primary source, cached, and then returned.
     */
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
    
    
    /**
     * Deletes a student from the system and removes their cached information.
     * 
     * This method attempts to delete a student using the provided ID. If the deletion
     * is successful, it also removes any cached information for that student from Redis.
     *
     * @param id The unique identifier of the student to be deleted.
     * @return A Boolean value indicating whether the deletion was successful.
     *         Returns true if the student was successfully deleted, false otherwise.
     */
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

    /**
     * Updates the information of a student identified by their ID and manages related cache entries.
     * 
     * This method updates a student's information in the primary data store and, if successful,
     * updates or invalidates related cache entries. It handles the following scenarios:
     * 1. Updates the student information in the primary data store.
     * 2. If successful, updates the cached student information.
     * 3. If the student's name has changed, it invalidates any cached search results for the old name.
     *
     * @param createStudent A CreateStudent object containing the updated information for the student.
     * @param id The unique identifier of the student whose information is to be updated.
     * @return A Boolean value indicating whether the update was successful.
     *         Returns true if the student information was successfully updated, false otherwise.
     */
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

    /**
     * Retrieves a list of student information based on specified criteria.
     * 
     * This method fetches student data from the studentService, applies pagination,
     * and optionally filters by name. The retrieved Student objects are then
     * converted to StudentDto objects for data transfer.
     *
     * @param limit The maximum number of students to retrieve.
     * @param offset The number of students to skip before starting to collect the result set.
     * @param name An optional parameter to filter students by name. If null or empty, all students are considered.
     * @return A List of StudentDto objects containing the requested student information.
     */
    public List<StudentDto> getAllStudentsInfo(Integer limit,Integer offset, String name) {
        List<Student> students = studentService.getAllStudentsInfo(limit, offset, name);
        List<StudentDto> studentDtos = students.stream().map((s) -> {
            return StudentMapper.toDto(s);
        }).collect(Collectors.toList());
        return studentDtos;
    }
}
