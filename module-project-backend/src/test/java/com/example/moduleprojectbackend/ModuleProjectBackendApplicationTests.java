package com.example.moduleprojectbackend;

import com.example.moduleprojectbackend.utils.Const;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class ModuleProjectBackendApplicationTests {

    @Autowired
    private JavaMailSender javaMailSender;
    @Test
    void TestMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("15160284336@163.com");
        message.setTo("1056527538@qq.com");
        message.setSubject("Test Email1");
        message.setText("This is a test email.");

        javaMailSender.send(message);
    }



}
