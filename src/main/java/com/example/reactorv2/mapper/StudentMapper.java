package com.example.reactorv2.mapper;

import com.example.reactorv2.domain.Student;
import com.example.reactorv2.model.StudentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    Student fromDto(StudentDTO studentDTO);

    StudentDTO toDto(Student student);
}
