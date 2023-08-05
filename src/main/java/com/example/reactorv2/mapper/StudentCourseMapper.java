package com.example.reactorv2.mapper;

import com.example.reactorv2.domain.Course;
import com.example.reactorv2.domain.StudentCourse;
import com.example.reactorv2.model.CourseDTO;
import com.example.reactorv2.model.StudentCourseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentCourseMapper {


    StudentCourse fromDto(final StudentCourseDTO studentCourseDTO);

    StudentCourseDTO toDto(final StudentCourse studentCourse);
}
