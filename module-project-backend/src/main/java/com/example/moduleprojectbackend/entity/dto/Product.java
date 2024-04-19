package com.example.moduleprojectbackend.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_product")
@AllArgsConstructor
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer productId;
    private String name;
    private Float price;
    private Integer sales;
    private String imageUrl;
    private String detail;
    private Date addDate;
}
