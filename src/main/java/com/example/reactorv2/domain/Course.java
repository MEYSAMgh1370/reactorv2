package com.example.reactorv2.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Course {

    @Id
    private Long id;

    private @NonNull String name;

    private @NonNull Integer capacity;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;


/*    @Transient
    private List<Student> students = new ArrayList<>();

    public void addStudent(final Student student) {
        this.students.add(student);
        if (student.getId() != null) {
            studentsId.add(student.getId());
        }
    }*/
}
