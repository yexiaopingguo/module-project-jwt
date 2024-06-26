package com.example.moduleprojectbackend.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.moduleprojectbackend.entity.dto.Account;
import com.example.moduleprojectbackend.entity.vo.request.EmailRegisterVO;
import com.example.moduleprojectbackend.mapper.AccountMapper;
import com.example.moduleprojectbackend.service.AccountService;
import com.example.moduleprojectbackend.utils.Const;
import com.example.moduleprojectbackend.utils.FlowUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private RedisTemplate stringRedisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Resource
    FlowUtils flow;

    @Resource
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.findAccountByNameOrEmail(username);
        if(account == null)
            throw new UsernameNotFoundException("wrong username or password");
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
        synchronized (address.intern()) {
            if(!this.verifyLimit(address))
                return "frequent requests, please try again later";
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            Map<String, Object> data = Map.of("type",type,"email", email, "code", code);
            rabbitTemplate.convertAndSend(Const.MQ_MAIL, JSONObject.toJSONString(data, JSONWriter.Feature.WriteNulls));
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        }
    }

    public boolean verifyLimit(String address) {
        String key = Const.VERIFY_EMAIL_LIMIT + address;
        return flow.limitOnceCheck(key, 60);
    }

    public String registerEmailAccount(EmailRegisterVO info){
        String email = info.getEmail();
        String code = this.getEmailVerifyCode(email);
        if(code == null) return "please get the verification code first";
        if(!code.equals(info.getCode())) return "verification code is wrong, please re-enter";
        if(this.existsAccountByEmail(email)) return "this address has been registered";
        String username = info.getUsername();
        if(this.existsAccountByUsername(username)) return "this username has been used by someone else, please change it again";
        String password = passwordEncoder.encode(info.getPassword());
        Account account = new Account(null, info.getUsername(),
                password, email, Const.ROLE_DEFAULT, new Date());
        if(!this.save(account)) {
            return "database error, registration failed";
        } else {
            this.deleteEmailVerifyCode(email);
            return null;
        }
    }

    private String getEmailVerifyCode(String email){
        String key = Const.VERIFY_EMAIL_DATA + email;
        return (String) stringRedisTemplate.opsForValue().get(key);
    }

    private boolean existsAccountByEmail(String email){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email", email));
    }

    private boolean existsAccountByUsername(String username){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username", username))
                || this.baseMapper.exists(Wrappers.<Account>query().eq("email", username));
    }

    private void deleteEmailVerifyCode(String email){
        String key = Const.VERIFY_EMAIL_DATA + email;
        stringRedisTemplate.delete(key);
    }



}
