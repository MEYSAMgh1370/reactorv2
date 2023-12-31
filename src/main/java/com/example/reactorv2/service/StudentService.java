package com.example.reactorv2.service;

import com.example.reactorv2.model.StudentDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {

    Flux<StudentDTO> listStudent();
    Mono<StudentDTO> findStudentById(Long studentId);
    Mono<StudentDTO> saveNewStudent(Mono<StudentDTO> studentDTO);
    Mono<StudentDTO> updateStudent( Mono<StudentDTO> studentDTO);
    Mono<StudentDTO> patchStudent(StudentDTO studentDTO);
    Mono<Void> deleteStudentById(Long studentId);
}
