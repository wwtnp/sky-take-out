package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.anno.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DishMapper {
    /**
     * 根据分类id统计菜品数量
     *
     * @param categoryId
     * @return 菜品数量
     */
    @Select("SELECT COUNT(id) FROM dish WHERE category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * 新增菜品
     *
     * @param dish
     */
    @AutoFill(OperationType.INSERT)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into dish values (null, #{name}, #{categoryId}, #{price}, #{image}, #{description}, " +
            "#{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser});")
    void insert(Dish dish);

    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return 菜品分页查询结果
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     *
     * @param id
     * @return 菜品
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据id删除菜品
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id修改菜品
     *
     * @param dish
     */
    @AutoFill(OperationType.UPDATE)
    @Update("update dish set " +
            "name = #{name}, " +
            "category_id = #{categoryId}, " +
            "price = #{price}, " +
            "image = #{image}, " +
            "description = #{description}, " +
            "status = #{status}, " +
            "update_time = #{updateTime}, " +
            "update_user = #{updateUser} " +
            "where id = #{id}")
    void update(Dish dish);
}
