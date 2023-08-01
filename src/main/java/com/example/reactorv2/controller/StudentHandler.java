package com.example.reactorv2.controller;

import com.example.reactorv2.model.StudentDTO;
import com.example.reactorv2.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StudentHandler {

    private final StudentService studentService;
    private final Validator validator;

    private void validate(StudentDTO studentDTO) {
        Errors errors = new BeanPropertyBindingResult(studentDTO, "beerDto");
        validator.validate(studentDTO, errors);

        if (errors.hasErrors()){
            throw new ServerWebInputException(errors.toString());
        }
    }
    public Mono<ServerResponse> listStudents(ServerRequest request){
        return ServerResponse.ok()
                .body(studentService.listStudent(), StudentDTO.class);
    }

    public Mono<ServerResponse> findStudentById(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(studentService.findStudentById(Long.valueOf(request.pathVariable("studentId"))), StudentDTO.class);
    }

    public Mono<ServerResponse> createStudent(ServerRequest request) {
        return studentService.saveNewStudent(request.bodyToMono(StudentDTO.class))
                        .flatMap(studentDTO -> ServerResponse
                                .created(UriComponentsBuilder
                                        .fromPath(StudentRouterConfig.STUDENT_PATH_ID)
                                        .build(studentDTO.getId()))
                                .build());
    }

    public Mono<ServerResponse> updateStudent(ServerRequest request) {
        return request.bodyToMono(StudentDTO.class)
                .doOnNext(this::validate)
                .flatMap(studentDTO -> studentService
                        .updateStudent(Long.valueOf(request.pathVariable("studentId")),Mono.just(studentDTO)))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> deleteStudent(ServerRequest request) {
            return studentService.findStudentById(Long.valueOf(request.pathVariable("studentId")))
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .flatMap(studentDTO -> studentService.deleteStudentById(studentDTO.getId()))
                    .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchStudentById(ServerRequest request) {
        return request.bodyToMono(StudentDTO.class)
                .doOnNext(this::validate)
                .flatMap(studentDTO -> studentService
                        .patchStudent(Long.valueOf(request.pathVariable("beerId")),studentDTO))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }
}
