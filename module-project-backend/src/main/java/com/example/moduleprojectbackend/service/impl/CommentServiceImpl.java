package com.example.moduleprojectbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.moduleprojectbackend.entity.dto.Comment;
import com.example.moduleprojectbackend.mapper.CommentMapper;
import com.example.moduleprojectbackend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<Comment> getCommentsByProductId(Integer productId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_id", productId);
        return commentMapper.selectList(queryWrapper);
    }

    @Override
    public int addComment(Integer productId, Integer userId, Integer score, String comment) {

        // 创建一个 Date 对象表示当前时间
        Date currentTime = new Date();

// 将 Date 对象转换为 Timestamp 对象
        Timestamp timestamp = new Timestamp(currentTime.getTime());

        int rowsAffected = commentMapper.insertComment(productId, userId, score, comment, timestamp);
        return rowsAffected > 0 ? 1 : 0;
    }
}
