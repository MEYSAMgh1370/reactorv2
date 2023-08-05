package com.example.reactorv2.service;

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
    public Mono<CourseDTO> save(Mono<CourseDTO> courseDTO) {
        courseDTO.map(coursedto -> {
            if(coursedto.getStudents().size() < coursedto.getCapacity()) {
                for (StudentDTO studentDTO : coursedto.getStudents()) {
                    studentCourseService.saveByIds(coursedto.getId() ,studentDTO.getId());
                }
            }
            return coursedto;
        });
        return courseDTO.map(courseMapper::fromDto)
                .flatMap(courseRepository::save)
                .map(courseMapper::toDto);
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
        studentCourseRepository.deleteAllByCourseId(courseId);
        return courseRepository.deleteById(courseId);
    }

    @Override
    public Mono<CourseDTO> update(Mono<CourseDTO> courseDTO){
        courseDTO.map(courseDTO1 -> Flux.fromIterable(courseDTO1.getStudents())
                .map(studentDTO -> studentCourseService.saveByIds(courseDTO1.getId(),studentDTO.getId())));
        return courseDTO
                .flatMap(course -> courseRepository.findById(course.getId())
                    .map(foundedCourse -> {
                        foundedCourse.setCapacity(course.getCapacity());
                        foundedCourse.setName(course.getName());
                        return foundedCourse;
                    }).flatMap(courseRepository::save)
                        .map(courseMapper::toDto));
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
        studentCourseService.saveByIds(courseId,studentId);
        return courseRepository.findById(courseId)
                .map(courseMapper::toDto)
                .map(foundedCourse ->{
                    if (foundedCourse.getStudents().size() < foundedCourse.getCapacity()) {
                    foundedCourse.getStudents().add(studentService.findStudentById(studentId).block());}
                    return foundedCourse;
                });

    }
}
