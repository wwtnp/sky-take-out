package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transactional  //涉及多张表操作，需要开启事务
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        // 1. DTO 转 Dish
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 2. 保存菜品基本信息
        dishMapper.insert(dish);

        // 3. 获取菜品id
        Long dishId = dish.getId();

        // 4. 获取口味列表
        List<DishFlavor> flavors = dishDTO.getFlavors();

        // 5. 给每个口味补 dishId
        if (flavors != null && !flavors.isEmpty()){
            flavors.forEach(flavor -> flavor.setDishId(dishId));

            // 6. 保存口味
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return 菜品分页查询结果
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //1. 开启分页
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        //2. 调用mapper的分页查询方法，并强转返回结果为Page
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        //3. 返回结果
        return new PageResult(page.getTotal(), page.getResult());
    }
}
