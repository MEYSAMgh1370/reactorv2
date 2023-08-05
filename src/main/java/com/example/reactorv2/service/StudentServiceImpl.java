package com.example.reactorv2.service;

import com.example.reactorv2.mapper.StudentMapper;
import com.example.reactorv2.model.StudentDTO;
import com.example.reactorv2.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
    public Mono<StudentDTO> findStudentById (Long studentId) {
        return studentRepository.findById(studentId)
                .map(studentMapper::toDto);
    }

    @Override
    public Mono<StudentDTO> saveNewStudent (Mono<StudentDTO> studentDTO) {
        return studentDTO.map(studentMapper::fromDto)
                .flatMap(studentRepository::save)
                .map(studentMapper::toDto);
    }
// todo id ro az studentdto begire
    @Override
    public Mono<StudentDTO> updateStudent( Mono<StudentDTO> studentDTO) {
        return studentDTO.map(studentMapper::fromDto)
                .flatMap(student -> studentRepository.findById(student.getId())
                    .map(foundStudent -> {
                        foundStudent.setName(student.getName());
                        foundStudent.setFamilyName(student.getFamilyName());
                        foundStudent.setBirthDay(student.getBirthDay());
                        return foundStudent;
                    }).flatMap(studentRepository::save)
                .map(studentMapper::toDto));
    }

    @Override
    public Mono<StudentDTO> patchStudent(StudentDTO studentDTO) {
        return studentRepository.findById(studentDTO.getId())
                .map(foundBeer -> {
                    if(StringUtils.hasText(studentDTO.getName())){
                        foundBeer.setName(studentDTO.getName());
                    }

                    if(StringUtils.hasText(studentDTO.getFamilyName())){
                        foundBeer.setFamilyName(studentDTO.getFamilyName());
                    }

                    if(StringUtils.hasText(studentDTO.getFatherName())){
                        foundBeer.setFatherName(studentDTO.getFatherName());
                    }

                    return foundBeer;
                }).flatMap(studentRepository::save)
                .map(studentMapper::toDto);
    }


    @Override
    public Mono<Void> deleteStudentById(Long studentId) {
        return studentRepository.deleteById(studentId);
    }
}