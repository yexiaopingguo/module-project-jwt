package com.example.moduleprojectbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.moduleprojectbackend.entity.dto.Comment;
import com.example.moduleprojectbackend.mapper.CommentMapper;
import com.example.moduleprojectbackend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
