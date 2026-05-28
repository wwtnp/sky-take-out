package com.sky.service.impl;

import com.sky.constant.RedisConstant;
import com.sky.constant.StatusConstant;
import com.sky.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShopServiceImpl implements ShopService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 设置店铺的营业状态
     * @param status
     */
    @Override
    public void setStatus(Integer status) {
        // 实现设置店铺状态的逻辑
        log.info("设置店铺状态：status={}", status);
        redisTemplate.opsForValue().set(RedisConstant.SHOP_STATUS, status);
    }

    /**
     * 获取店铺的营业状态
     * @return 营业状态
     */
    @Override
    public Integer getStatus() {
        Object status = redisTemplate.opsForValue().get(RedisConstant.SHOP_STATUS);
        if (status == null){
            return StatusConstant.DISABLE;
        }
        return (Integer) status;
    }
}
