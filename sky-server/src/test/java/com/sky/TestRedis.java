package com.sky;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisTemplate redisTemplate;

    // 测试String类型操作
    @Test
    public void testString() {
        //1. 获取类型操作对象
        ValueOperations valueOperations = redisTemplate.opsForValue();

        //2. 通过类型操作对象，对redis进行CRUD操作
        valueOperations.set("name", "张三");
        String name = (String) valueOperations.get("name");
        System.out.println(name);
        valueOperations.set("code", "123456", 60, TimeUnit.SECONDS);
        valueOperations.setIfAbsent("age", 18);   // 如果age不存在，则设置age为18 相当于SETNX

    }
}
