package com.example.reactorv2.service;

import com.example.reactorv2.domain.Course;
import com.example.reactorv2.domain.Student;
import com.example.reactorv2.mapper.CourseMapper;
import com.example.reactorv2.model.CourseDTO;
import com.example.reactorv2.repositories.CourseRepository;
import com.example.reactorv2.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseMapper courseMapper;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    @Override
    public Mono<CourseDTO> save(CourseDTO courseDTO) {
        return Mono.just(courseMapper.fromDto(courseDTO))
                .flatMap(course -> {
                    List<Student> students = course.getStudents();
                    course.setStudents(new ArrayList<>());
                    return studentRepository.saveAll(students)
                            .map(student -> {
                                course.addStudent(student);
                                return course;
                            }).last();
                }).flatMap(courseRepository::save)
                .map(courseMapper::toDto);
    }

    @Override
    public Mono<CourseDTO> findById(Long courseId) {
        return courseRepository.findById(courseId)
                .flatMap(course -> studentRepository.findAllById(course.getStudentsId())
                        .map(student -> {
                            course.addStudent(student);
                            return course;
                        }).last()).map(courseMapper::toDto);
    }

    @Override
    public Flux<CourseDTO> findAllCourse() {
        return courseRepository.findAll()
                .flatMap(course -> studentRepository.findAllById(course.getStudentsId())
                        .map(student -> {
                            course.addStudent(student);
                            return course;
                        }).last()).map(courseMapper::toDto);
    }

    //todo   write delete course

}
