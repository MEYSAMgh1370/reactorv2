package com.example.reactorv2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseDTO {
    private Long studentId;
    private Long courseId;
}
