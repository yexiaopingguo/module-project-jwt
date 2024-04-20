package com.example.moduleprojectbackend.entity.dto;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.util.Date;

@Data
@TableName("db_comment")
@AllArgsConstructor
public class Comment {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer productId;
    private Integer userId;
    private Integer score;
    private String comment;
    private Date commentDate;
}

