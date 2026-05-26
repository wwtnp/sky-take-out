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
}
