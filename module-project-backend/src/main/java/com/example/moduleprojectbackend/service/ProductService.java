package com.example.moduleprojectbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.moduleprojectbackend.entity.dto.Product;

import java.util.List;

public interface ProductService {
    Page<Product> getProducts(String sortMethod, String sortOrder, Integer index, Integer number);
}
