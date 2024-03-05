package com.example.moduleprojectbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.moduleprojectbackend.entity.dto.Account;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByNameOrEmail(String text);
}