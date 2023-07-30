package com.example.reactorv2.service;

import com.example.reactorv2.domain.Course;
import com.example.reactorv2.model.CourseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface CourseService {
    Mono<CourseDTO> save(CourseDTO course);
    Mono<CourseDTO> findById(Long id);
    Flux<CourseDTO> findAllCourse();
}
