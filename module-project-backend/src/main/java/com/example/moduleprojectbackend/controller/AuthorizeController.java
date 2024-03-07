package com.example.moduleprojectbackend.controller;

import com.example.moduleprojectbackend.entity.RestBean;
import com.example.moduleprojectbackend.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    @Resource
    AccountService accountService;

    @GetMapping("/ask-code")
    public String askVerifyCode(@RequestParam @Email String email,
                                @RequestParam @Pattern(regexp = "(register|reset)") String type,
                                HttpServletRequest request){
        String message = accountService.registerEmailVerifyCode(type, email, request.getRemoteAddr());
        return (message == null) ? RestBean.success().asJsonString() : RestBean.failure(400, message).asJsonString();
    }
}
