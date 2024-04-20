package com.example.moduleprojectbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.moduleprojectbackend.entity.RestBean;
import com.example.moduleprojectbackend.entity.dto.Comment;
import com.example.moduleprojectbackend.entity.dto.Product;
import com.example.moduleprojectbackend.service.CommentService;
import com.example.moduleprojectbackend.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/test")
    public void test(HttpServletRequest request,
                     HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.success("test success").asJsonString());
    }

    @GetMapping("/getProducts")
    public String getProducts(
            @RequestParam String sortMethod,
            @RequestParam String sortOrder,
            @RequestParam Integer index,
            @RequestParam Integer number) {

        return RestBean.success(productService.getProducts(sortMethod, sortOrder, index, number)).asJsonString();
    }

    @GetMapping("/getProductById")
    public String getProductById(
            @RequestParam Integer productId) {

        return RestBean.success(productService.getProductById(productId)).asJsonString();
    }

    @GetMapping("/getProductByName")
    public String getProductByName(
            @RequestParam String name) {

        return RestBean.success(productService.getProductByName(name)).asJsonString();
    }

    @GetMapping("/getComments")
    public String getCommentsByProductId(@RequestParam Integer productId) {
        return RestBean.success(commentService.getCommentsByProductId(productId)).asJsonString();
    }

}
