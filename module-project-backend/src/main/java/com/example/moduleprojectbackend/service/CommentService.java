package com.example.moduleprojectbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.moduleprojectbackend.entity.dto.Comment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommentService {
    List<Comment> getCommentsByProductId(Integer productId);
}
