package com.synchrony.assignment.studentmanagement.repository;

import com.synchrony.assignment.studentmanagement.model.Student;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {
    @Query("SELECT s FROM Student s WHERE s.name = :name order by updatedAt asc")
    List<Student> findByNameContaining(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM Student s WHERE s.id = :id")
    int deleteByStudentId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Student s SET s.name = :name, s.age = :age, s.studentClass = :studentClass, s.phoneNumber = :phoneNumber WHERE s.id = :id")
    int updateById(@Param("name") String name, @Param("age") String age, @Param("studentClass") String studentClass, @Param("phoneNumber") String phoneNumber, @Param("id") Long id);

    @Query(value = "SELECT * FROM student s WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY updated_at DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Student> findAllStudentsInfo(Integer limit,Integer offset, String name);
}
