package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO 员工信息
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO 分页查询条件
     * @return 员工分页查询结果
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工
     * @param status 状态
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id查询员工
     * @param id 员工ID
     * @return 员工信息
     */
    Employee getById(Long id);

    /**
     * 编辑员工信息
     * @param employeeDTO 员工信息
     */
    void update(EmployeeDTO employeeDTO);
}
