package com.example.moduleprojectbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.moduleprojectbackend.entity.dto.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ProductMapper extends BaseMapper<Product> {
    @Select("SELECT product_id, name, price, sales, image_url, detail, add_date FROM db_product ORDER BY ${sortColumn} ${sortOrder} LIMIT #{offset}, #{number}")
    Page<Product> getProducts(Page<Product> page, @Param("sortColumn") String sortColumn, @Param("sortOrder") String sortOrder, @Param("offset") Integer offset, @Param("number") Integer number);
}
