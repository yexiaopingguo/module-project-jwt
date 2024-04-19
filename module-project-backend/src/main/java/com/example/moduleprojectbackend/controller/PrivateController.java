package com.example.moduleprojectbackend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.moduleprojectbackend.entity.RestBean;
import com.example.moduleprojectbackend.entity.dto.Account;
import com.example.moduleprojectbackend.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/private")
public class PrivateController {

    @Resource
    AccountService accountService;

    public UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        return null;
    }

    @GetMapping("/test")
    public void test(HttpServletRequest request,
                     HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.success("测试成功").asJsonString());
    }

    @GetMapping("/getAccInfo")
    public void getAccInfo(HttpServletRequest request,
                     HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=utf-8");

        UserDetails currentUserDetails = getCurrentUserDetails();
        if (currentUserDetails != null) {
            String username = currentUserDetails.getUsername();
            Account account = accountService.findAccountByNameOrEmail(username);

            // 添加属性到 JSON 对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", account.getUsername());
            jsonObject.put("email", account.getEmail());
            jsonObject.put("role", account.getRole());
            response.getWriter().write(RestBean.success(jsonObject).asJsonString());
        } else {
            response.getWriter().write(RestBean.failure(404, "没有找到用户").asJsonString());
        }



    }


}
