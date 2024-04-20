package com.example.moduleprojectbackend.controller;

import com.alibaba.fastjson2.JSONObject;
import com.example.moduleprojectbackend.entity.RestBean;
import com.example.moduleprojectbackend.entity.dto.Account;
import com.example.moduleprojectbackend.entity.dto.Comment;
import com.example.moduleprojectbackend.service.AccountService;
import com.example.moduleprojectbackend.service.CommentService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/private")
public class PrivateController {

    @Resource
    AccountService accountService;

    @Resource
    CommentService commentService;

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
        response.getWriter().write(RestBean.success("test success").asJsonString());
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
            jsonObject.put("id", account.getId());
            jsonObject.put("username", account.getUsername());
            jsonObject.put("email", account.getEmail());
            jsonObject.put("role", account.getRole());
            response.getWriter().write(RestBean.success(jsonObject).asJsonString());
        } else {
            response.getWriter().write(RestBean.failure(404, "no user found").asJsonString());
        }

    }

    @PostMapping("/addComment")
    public String addComment(@RequestParam Integer productId,
                             @RequestParam Integer userId,
                             @RequestParam Integer score,
                             @RequestParam String comment) {
        int result = commentService.addComment(productId, userId, score, comment);
        if (result > 0) {
            return RestBean.success().asJsonString();
        } else {
            return RestBean.failure(401, "add failure").asJsonString();
        }
    }
}
