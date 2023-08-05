package com.example.reactorv2.service;

import com.example.reactorv2.domain.StudentCourse;
import com.example.reactorv2.model.CourseDTO;
import com.example.reactorv2.model.StudentCourseDTO;
import com.example.reactorv2.model.StudentDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentCourseService {
    Mono<StudentCourse> save(Mono<StudentCourseDTO> studentCourseDTO);

    Mono<StudentCourse> saveByIds(Long courseId, Long studentId);

    Flux<CourseDTO> findCoursesByStudentID(Long studentId);

    Flux<StudentDTO> findStudentByCourseID(Long courseId);

}
