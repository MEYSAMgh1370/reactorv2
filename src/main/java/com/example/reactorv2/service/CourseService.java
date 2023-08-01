package com.example.reactorv2.service;

import com.example.reactorv2.domain.Course;
import com.example.reactorv2.model.CourseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CourseService {
    Mono<CourseDTO> save(Mono<CourseDTO> course);
    Mono<CourseDTO> findById(Long id);

    Flux<CourseDTO> findByIds(List<Long> courseIds);

    Flux<CourseDTO> findAllCourse();
    Mono<Void> deleteById(Long id);

    Mono<CourseDTO> updateById(Long courseId, Mono<CourseDTO> courseDTO);
}
