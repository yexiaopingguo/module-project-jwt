package com.example.moduleprojectbackend.controller.exception;

import com.example.moduleprojectbackend.entity.RestBean;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ValidationController {

//    @ExceptionHandler(ValidationException.class)
//    public void validateError(ValidationException exception) {
//        log.info("Resolved [{}: {}]", exception.getClass().getName(), exception.getMessage());
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String MethodArgumentNotValidError(MethodArgumentNotValidException exception) {
        log.warn("Resolved [{}: {}]", exception.getClass().getName(), exception.getMessage());
        return RestBean.failure(400, "请求参数有误").asJsonString();
    }
}
