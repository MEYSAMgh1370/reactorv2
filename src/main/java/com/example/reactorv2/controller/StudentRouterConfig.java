package com.example.reactorv2.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Configuration
@RequiredArgsConstructor
public class StudentRouterConfig {

    public static final String STUDENT_PATH = "/api/beer";
    public static final String STUDENT_PATH_ID = "/{studentId}";

    private final StudentHandler studentHandler;

    @Bean
    public RouterFunction<ServerResponse> studentRoutes(){
        return route()
                .GET(STUDENT_PATH, accept(APPLICATION_JSON), studentHandler::listStudents)
                .GET(STUDENT_PATH_ID, accept(APPLICATION_JSON), studentHandler::findStudentById)
                .POST(STUDENT_PATH, accept(APPLICATION_JSON), studentHandler::createStudent)
                .PUT(STUDENT_PATH, accept(APPLICATION_JSON), studentHandler::updateStudent)
                .DELETE(STUDENT_PATH_ID, accept(APPLICATION_JSON), studentHandler::deleteStudent)
                .PATCH(STUDENT_PATH_ID, accept(APPLICATION_JSON), studentHandler::patchStudentById)
                .build();
    }
}
