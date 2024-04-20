package com.example.moduleprojectbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.moduleprojectbackend.entity.dto.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    @Insert("INSERT INTO db_comment (product_id, user_id, score, comment, comment_date) " +
            "VALUES (#{productId}, #{userId}, #{score}, #{comment}, #{commentDate})")
    int insertComment(Integer productId, Integer userId, Integer score, String comment, Date commentDate);
}
