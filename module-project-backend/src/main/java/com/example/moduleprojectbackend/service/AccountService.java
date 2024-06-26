package com.example.moduleprojectbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.moduleprojectbackend.entity.dto.Account;
import com.example.moduleprojectbackend.entity.vo.request.EmailRegisterVO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByNameOrEmail(String text);
    String registerEmailVerifyCode(String type, String email, String address);
    String registerEmailAccount(EmailRegisterVO info);
}