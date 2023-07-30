package com.example.reactorv2.service;

import com.example.reactorv2.mapper.StudentMapper;
import com.example.reactorv2.model.StudentDTO;
import com.example.reactorv2.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public Flux<StudentDTO> listStudent() {
        return studentRepository.findAll().map(studentMapper::toDto);
    }

    @Override
    public Mono<StudentDTO> findStudentById(Long studentId) {
        return studentRepository.findById(studentId)
                .map(studentMapper::toDto);
    }

    @Override
    public Mono<StudentDTO> saveNewStudent(StudentDTO studentDTO) {
        return studentRepository.save(studentMapper.fromDto(studentDTO))
                .map(studentMapper::toDto);
    }

    @Override
    public Mono<StudentDTO> UpdateStudent(Long studentId, StudentDTO studentDTO) {
        return studentRepository.findById(studentId)
                .map(foundStudent -> {
                    foundStudent.setName(studentDTO.getName());
                    foundStudent.setFamilyName(studentDTO.getFamilyName());
                    foundStudent.setBirthDay(studentDTO.getBirthDay());
                    return foundStudent;
                }).flatMap(studentRepository::save)
                .map(studentMapper::toDto);
    }

    @Override
    public Mono<Void> deleteStudentById(Long studentId) {
        return studentRepository.deleteById(studentId);
    }
}