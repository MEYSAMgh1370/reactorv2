package com.example.reactorv2.repositories;

import com.example.reactorv2.domain.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import static org.junit.jupiter.api.Assertions.*;

@DataR2dbcTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Test
    void saveNewStudent() {

    }

    Student getTestStudent() {
        return Student.builder()
                .birthDay(1970,07,07)
    }
}