package com.example.moduleprojectbackend;

import com.example.moduleprojectbackend.utils.Const;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class ModuleProjectBackendApplicationTests {

    @Resource
    StringRedisTemplate template;

    @Test
    void contextLoads() {
        template.opsForValue().set("test", "nb");
    }

}
