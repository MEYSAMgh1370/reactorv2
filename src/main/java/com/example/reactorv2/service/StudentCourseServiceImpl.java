package com.example.reactorv2.service;

import com.example.reactorv2.domain.StudentCourse;
import com.example.reactorv2.mapper.CourseMapper;
import com.example.reactorv2.mapper.StudentCourseMapper;
import com.example.reactorv2.mapper.StudentMapper;
import com.example.reactorv2.model.CourseDTO;
import com.example.reactorv2.model.StudentCourseDTO;
import com.example.reactorv2.model.StudentDTO;
import com.example.reactorv2.repositories.CourseRepository;
import com.example.reactorv2.repositories.StudentCourseRepository;
import com.example.reactorv2.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StudentCourseServiceImpl implements StudentCourseService {

/*    @Override
    public Flux<CourseDTO> findByIds(List<Long> courseIds) {
        return Flux.fromIterable(courseIds)
                .flatMap(courseId ->
                        courseRepository.findById(courseId)
                                .map(courseMapper::toDto));
    }*/
    private final StudentCourseRepository studentCourseRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentCourseMapper studentCourseMapper;
    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;

    @Override
    public Mono<StudentCourse> save(Mono<StudentCourseDTO> studentCourseDTO) {
        return studentCourseDTO.map(studentCourseMapper::fromDto)
                .flatMap(studentCourseRepository::save);
    }

    @Override
    public Mono<StudentCourse> saveByIds(Long courseId, Long studentId) {

        return studentCourseRepository.save(StudentCourse.builder().courseId(courseId)
                .studentId(studentId).build());
    }

    @Override
    public Flux<CourseDTO> findCoursesByStudentID(Long studentId) {

        return studentCourseRepository.findStudentCourseByStudentId(studentId)
                .flatMap(foundedStudentCourse -> {
                    Long courseId =foundedStudentCourse.getCourseId();
                    return courseRepository.findById(courseId)
                            .map(courseMapper::toDto);
                });
    }

    @Override
    public Flux<StudentDTO> findStudentByCourseID(Long courseId) {

        return studentCourseRepository.findStudentCourseByCourseId(courseId)
                .flatMap(foundedStudentCourse -> {
                    Long studentId =foundedStudentCourse.getStudentId();
                    return studentRepository.findById(studentId)
                            .map(studentMapper::toDto);
                });
    }

}
