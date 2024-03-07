package com.example.moduleprojectbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.moduleprojectbackend.entity.dto.Account;
import com.example.moduleprojectbackend.mapper.AccountMapper;
import com.example.moduleprojectbackend.service.AccountService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.findAccountByNameOrEmail(username);
        if(account == null)
            throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    @Override
    public Account findAccountByNameOrEmail(String text){
        return this.query()
                .eq("username", text).or()
                .eq("email", text)
                .one();
    }

    @Override
    public String registerEmailVerifyCode(String type, String email, String address){
//        synchronized (address.intern()) {
//            if(!this.verifyLimit(address))
//                return "请求频繁，请稍后再试";
//            Random random = new Random();
//            int code = random.nextInt(899999) + 100000;
//            Map<String, Object> data = Map.of("type",type,"email", email, "code", code);
//            rabbitTemplate.convertAndSend(Const.MQ_MAIL, data);
//            stringRedisTemplate.opsForValue()
//                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
//            return null;
//        }
        return null;
    }
}
