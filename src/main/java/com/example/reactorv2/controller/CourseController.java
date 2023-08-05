package com.example.reactorv2.controller;

import com.example.reactorv2.model.CourseDTO;
import com.example.reactorv2.service.CourseService;
import com.example.reactorv2.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final StudentService studentService;

    public static final String COURSE_PATH = "/api/v2/beer";
    public static final String COURSE_PATH_ID = COURSE_PATH + "{courseId}";
    public static final String COURSE_STUDENT_PATH_ID = COURSE_PATH + "{courseId}" + "{studentId}";

    @PatchMapping(COURSE_STUDENT_PATH_ID)
    public Mono<CourseDTO> addStudentToCourse(@PathVariable("courseId") Long courseId, @PathVariable("studentId") Long studentId ) {
        return courseService.addStudentToCourse(courseId,studentId);
    }
}
