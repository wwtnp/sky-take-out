package com.sky.service;

public interface ShopService {
    /**
     * 设置店铺的营业状态
     * @param status
     */
    void setStatus(Integer status);

    /**
     * 获取店铺的营业状态
     * @return 营业状态
     */
    Integer getStatus();

}
