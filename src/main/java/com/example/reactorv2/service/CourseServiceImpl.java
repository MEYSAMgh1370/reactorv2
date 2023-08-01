package com.example.reactorv2.service;

import com.example.reactorv2.mapper.CourseMapper;
import com.example.reactorv2.model.CourseDTO;
import com.example.reactorv2.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;

    @Override
    public Mono<CourseDTO> save(Mono<CourseDTO> courseDTO) {
        return courseDTO.map(courseMapper::fromDto)
                .flatMap(courseRepository::save)
                .map(courseMapper::toDto);
    }

    @Override
    public Mono<CourseDTO> findById(Long courseId) {
        return courseRepository.findById(courseId)
                .map(courseMapper::toDto);
    }

    @Override
    public Flux<CourseDTO> findByIds(List<Long> courseIds) {
        return Flux.fromIterable(courseIds)
                .flatMap(courseId ->
                        courseRepository.findById(courseId)
                                .map(courseMapper::toDto));
    }

    @Override
    public Flux<CourseDTO> findAllCourse() {
        return courseRepository.findAll()
                .map(courseMapper::toDto);
    }

    public Mono<Void> deleteById(Long courseId){
        return courseRepository.deleteById(courseId);
    }

    @Override
    public Mono<CourseDTO> updateById(Long courseId, Mono<CourseDTO> courseDTO){
        return courseDTO.map(courseMapper::fromDto)
                .flatMap(course -> courseRepository.findById(courseId)
                    .map(foundedCourse -> {
                        foundedCourse.setId(course.getId());
                        foundedCourse.setStudentIds(course.getStudentIds());
                        foundedCourse.setName(course.getName());
                        return foundedCourse;
                    }).flatMap(courseRepository::save)
                        .map(courseMapper::toDto));
    }

}
