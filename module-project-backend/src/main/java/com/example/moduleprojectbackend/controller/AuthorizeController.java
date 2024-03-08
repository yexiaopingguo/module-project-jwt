package com.example.moduleprojectbackend.controller;

import com.example.moduleprojectbackend.entity.RestBean;
import com.example.moduleprojectbackend.entity.vo.request.EmailRegisterVO;
import com.example.moduleprojectbackend.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

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
        return this.messageHandle(() ->
                accountService.registerEmailVerifyCode(type, String.valueOf(email), request.getRemoteAddr()));
    }

    @PostMapping("/register")
    public String register(@RequestBody @Valid EmailRegisterVO vo){
        return this.messageHandle(() ->
                accountService.registerEmailAccount(vo));
    }

    private String messageHandle(Supplier<String> action){
        String message = action.get();
        if(message == null)
            return RestBean.success().asJsonString();
        else
            return RestBean.failure(400, message).asJsonString();
    }

}
