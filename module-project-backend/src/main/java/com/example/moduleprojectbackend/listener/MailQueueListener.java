package com.example.moduleprojectbackend.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {

    @Resource
    JavaMailSender sender;

    @Value("${spring.mail.username}")
    String username;

    @RabbitHandler
    public void sendMailMessage(String jsonString) {
        Map<String, Object> data = JSON.parseObject(jsonString, new TypeReference<Map<String, Object>>() {});
        String email = data.get("email").toString();
        Integer code = (Integer) data.get("code");
        SimpleMailMessage message = switch (data.get("type").toString()) {
            case "register" ->
                    createMessage("Welcome to register our website",
                            "Your email registration verification code is : "+code+". Only valid for 3 minutes. To protect the security of your account, please do not disclose the verification code information to others.",
                            email);
            case "reset" ->
                    createMessage("Your password reset email",
                            "You are performing a password reset operation, verification code: "+code+". Only valid for 3 minutes. If it is not done by yourself, please ignore it.",
                            email);
            default -> null;
        };
        if(message == null) return;
        sender.send(message);
    }

    private SimpleMailMessage createMessage(String title, String content, String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);
        message.setFrom(username);
        return message;
    }
}
