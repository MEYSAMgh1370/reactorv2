package com.example.reactorv2.service;

import com.example.reactorv2.domain.Course;
import com.example.reactorv2.domain.StudentCourse;
import com.example.reactorv2.mapper.CourseMapper;
import com.example.reactorv2.mapper.StudentMapper;
import com.example.reactorv2.model.CourseDTO;
import com.example.reactorv2.model.StudentDTO;
import com.example.reactorv2.repositories.CourseRepository;
import com.example.reactorv2.repositories.StudentCourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;
    private final StudentCourseService studentCourseService;

    private final StudentService studentService;
    private final StudentCourseRepository studentCourseRepository;


    @Override
    public Mono<CourseDTO> save(CourseDTO courseDTO) {

        Course course = courseMapper.fromDto(courseDTO);
        return courseRepository.save(course)
                .map(courseMapper::toDto)
                .flatMap(savedCourseDTO -> {
                    Mono<CourseDTO> result = Mono.just(savedCourseDTO);
                    if(courseDTO.getStudents().size() < savedCourseDTO.getCapacity()) {
                        List<StudentCourse> studentCourses = new ArrayList<>();
                        for (StudentDTO studentDTO : courseDTO.getStudents()) {
                            studentCourses.add(StudentCourse.builder().courseId(courseDTO.getId())
                                    .studentId(studentDTO.getId()).build());
                        }
                        return studentCourseService.saveByIds(studentCourses).collectList().then(result);
                    }
                    return result;
                });
    }

    @Override
    public Mono<CourseDTO> findById(Long courseId) {
        return courseRepository.findById(courseId)
                .map(courseMapper::toDto)
                .flatMap(courseDTO ->
                        studentCourseService.findStudentByCourseID(courseId)
                        .collectList()
                        .map(foundedStudent -> {
                            courseDTO.setStudents(foundedStudent);
                            return courseDTO;
                            }));
    }

    @Override
    public Flux<CourseDTO> findByIds(List<Long> courseIds) {
        return Flux.fromIterable(courseIds)
                .flatMap(courseId ->
                        courseRepository.findById(courseId)
                                .map(courseMapper::toDto)
                                .flatMap(courseDTO ->
                                        studentCourseService.findStudentByCourseID(courseId)
                                        .collectList()
                                        .map(foundedStudent -> {
                                            courseDTO.setStudents(foundedStudent);
                                            return courseDTO;
                                        })));
    }

    @Override
    public Flux<CourseDTO> findAllCourse() {
        return courseRepository.findAll()
                .map(courseMapper::toDto);
    }

    @Override
    public Mono<Void> deleteById(Long courseId){

        return studentCourseRepository.deleteAllByCourseId(courseId)
                .then(courseRepository.deleteById(courseId));
    }



    @Override
    public Mono<CourseDTO> update(CourseDTO courseDTO){

        return courseRepository.findById(courseDTO.getId())
                    .map(foundedCourse -> {
                        foundedCourse.setCapacity(courseDTO.getCapacity());
                        foundedCourse.setName(courseDTO.getName());
                        return foundedCourse;
                    }).flatMap(courseRepository::save)
                        .map(courseMapper::toDto)
                .flatMap(updatedCourseDTO -> {
                    Mono<CourseDTO> result = Mono.just(updatedCourseDTO);
                    if(courseDTO.getStudents().size() < updatedCourseDTO.getCapacity()) {
                        List<StudentCourse> studentCourses = new ArrayList<>();
                        for (StudentDTO studentDTO : courseDTO.getStudents()) {
                            studentCourses.add(StudentCourse.builder().courseId(courseDTO.getId())
                                    .studentId(studentDTO.getId()).build());
                        }
                        return studentCourseRepository.deleteAllByCourseId(updatedCourseDTO.getId())
                                .then(studentCourseService.saveByIds(studentCourses).collectList()).then(result);
                    }
                    return result;
                });
    }

    @Override
    public Mono<CourseDTO> patchCourse(CourseDTO courseDTO) {
        return courseRepository.findById(courseDTO.getId())
                .map(foundCourse -> {
                    if (StringUtils.hasText(courseDTO.getName())) {
                        foundCourse.setName(courseDTO.getName());
                    }

                    if (courseDTO.getCapacity() != null) {
                        foundCourse.setCapacity(courseDTO.getCapacity());
                    }

                    return foundCourse;
                }).flatMap(courseRepository::save)
                .map(courseMapper::toDto);
    }

    @Override
    public Mono<CourseDTO> addStudentToCourse(Long courseId, Long studentId) {

        return courseRepository.findById(courseId)
                .flatMap(foundedCourse -> studentCourseService.findStudentByCourseID(courseId).collectList()
                        .flatMap(foundedStudents -> {
                            CourseDTO courseDTO = courseMapper.toDto(foundedCourse);
                            courseDTO.setStudents(foundedStudents);
                            if(foundedStudents.size() < foundedCourse.getCapacity())
                            {
                                return studentCourseService.saveByIds(courseId,studentId)
                                        .flatMap( savedStudentCourse -> studentService.findStudentById(studentId)
                                                .flatMap(foundedStudent -> {
                                                    courseDTO.getStudents().add(foundedStudent);
                                                    return Mono.just(courseDTO);
                                                }));
                            }
                            return Mono.just(courseDTO);
                        }));

    }
}
