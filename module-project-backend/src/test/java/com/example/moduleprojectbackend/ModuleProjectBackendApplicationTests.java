package com.example.moduleprojectbackend;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.TypeReference;
import com.example.moduleprojectbackend.utils.Const;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ModuleProjectBackendApplicationTests {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void Test() {
        String jsonString = "{\"type\":\"test\",\"email\":\"123@123\",\"code\":123}";
        Map<String, Object> map = JSON.parseObject(jsonString, new TypeReference<Map<String, Object>>() {});
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    @Test
    void Test163Mail() {
        SimpleMailMessage testMessage = new SimpleMailMessage();
        testMessage.setFrom("15160284336@163.com");
        testMessage.setTo("1056527538@qq.com");
        testMessage.setSubject("Test Email2");
        testMessage.setText("This is a test email.");

        javaMailSender.send(testMessage);
    }

    @Test
    void TestRedis() {
        RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
        System.out.println(connection.ping());
    }

    @Test
    void TestRabbitMQ() {
        Map<String, Object> data = Map.of("type","test","email", "123@123", "code", 123);
        rabbitTemplate.convertAndSend(Const.MQ_MAIL, JSONObject.toJSONString(data, JSONWriter.Feature.WriteNulls));
    }

}
