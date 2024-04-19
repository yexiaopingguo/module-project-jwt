package com.example.moduleprojectbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.moduleprojectbackend.entity.RestBean;
import com.example.moduleprojectbackend.entity.dto.Product;
import com.example.moduleprojectbackend.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private ProductService productService;

    @GetMapping("/test")
    public void test(HttpServletRequest request,
                     HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.success("test success").asJsonString());
    }

    @GetMapping("/products")
    public Page<Product> getProducts(
            @RequestParam String sortMethod,
            @RequestParam String sortOrder,
            @RequestParam Integer index,
            @RequestParam Integer number) {

        return productService.getProducts(sortMethod, sortOrder, index, number);
    }

}
