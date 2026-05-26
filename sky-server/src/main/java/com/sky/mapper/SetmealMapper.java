package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {
    /**
     * 根据分类id统计套餐数量
     * @param categoryId
     * @return
     */
    @Select("SELECT COUNT(id) FROM setmeal WHERE category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 根据菜品id统计套餐数量
     * @param ids
     * @return
     */
    Integer countByDishIds(List<Long> ids);
}
