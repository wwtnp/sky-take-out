package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealMapper setmealMapper;

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

    /**
     * 根据id查询菜品
     * @param ids
     * @return
     */
    @Transactional //涉及多张表操作，需要开启事务
    @Override
    public void deleteBatch(List<Long> ids) {
        //1. 删除菜品之前，要看下是否该菜品是起售状态，如果起售状态，则不能删除
        for (Long id : ids){
            Dish dish = dishMapper.getById(id);
            if (dish.getStatus().equals(StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        //2. 需要判断菜品是否被套餐关联
        Integer count = setmealMapper.countByDishIds(ids);
        if (count > 0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        //3. 删除菜品基本信息
        dishMapper.deleteBatch(ids);

        //4. 删除菜品口味列表信息
        dishFlavorMapper.deleteBatch(ids);

    }

    /**
     * 根据id查询菜品
     * @param id
     * @return 菜品VO
     */
    @Override
    public DishVO getByIdWithFlavor(Long id) {
        //1. 查询菜品基本信息
        Dish dish = dishMapper.getById(id);

        //2. 查询菜品口味列表信息
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);

        // 3. 组装 VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        if (flavors != null && !flavors.isEmpty()){
            dishVO.setFlavors(flavors);
        }
        return dishVO;
    }

    /**
     * 更新菜品
     * @param dishDTO
     */
    @Transactional
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        // 1. 更新菜品基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);

        // 2. 删除原有口味数据
        dishFlavorMapper.deleteBatch(Arrays.asList(dishDTO.getId()));

        // 3. 获取新的口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();

        // 4. 给口味补 dishId
        if (flavors != null && !flavors.isEmpty()){
            flavors.forEach(flavor -> flavor.setDishId(dishDTO.getId()));

        }

        //5. 保存新的口味数据
        dishFlavorMapper.insertBatch(flavors);
    }
}
