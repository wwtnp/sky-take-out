package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 批量保存口味数据
     * @param dishFlavorList
     */
    void insertBatch(@Param("dishFlavorList") List<DishFlavor> dishFlavorList);

    /**
     * 根据菜品id删除口味数据
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据菜品id查询口味数据
     * @param id
     * @return 口味数据列表
     */
    List<DishFlavor> getByDishId(Long id);
}
