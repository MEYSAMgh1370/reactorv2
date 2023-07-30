package com.example.reactorv2.mapper;

import com.example.reactorv2.domain.Course;
import com.example.reactorv2.model.CourseDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel="spring", uses = {StudentMapper.class})
public interface CourseMapper {


     Course fromDto(final CourseDTO courseDTO);

     CourseDTO toDto(final Course course);
}
