package com.example.moduleprojectbackend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.moduleprojectbackend.entity.dto.Product;
import com.example.moduleprojectbackend.mapper.ProductMapper;
import com.example.moduleprojectbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Page<Product> getProducts(String sortMethod, String sortOrder, Integer index, Integer number) {
        String sortColumn;
        switch (sortMethod) {
            case "date":
                sortColumn = "add_date";
                break;
            case "sales":
                sortColumn = "sales";
                break;
            case "random":
                sortColumn = "RAND()";
                break;
            default:
                throw new IllegalArgumentException("Invalid sortMethod");
        }
        return productMapper.getProducts(new Page<>(index, number), sortColumn, sortOrder, index * number, number);
    }
}

