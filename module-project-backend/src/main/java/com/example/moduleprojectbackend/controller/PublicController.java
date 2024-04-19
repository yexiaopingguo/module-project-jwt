package com.example.moduleprojectbackend.controller;

import com.example.moduleprojectbackend.entity.RestBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/test")
    public void test(HttpServletRequest request,
                     HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.success("测试成功").asJsonString());
    }

}
