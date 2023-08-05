package com.example.reactorv2.repositories;

import com.example.reactorv2.domain.StudentCourse;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface StudentCourseRepository extends ReactiveCrudRepository<StudentCourse, Long> {
    Flux<StudentCourse> findStudentCourseByStudentId(Long studentId);
    Flux<StudentCourse> findStudentCourseByCourseId(Long courseId);

    Mono<Void> deleteAllByCourseId(Long courseId);

}
