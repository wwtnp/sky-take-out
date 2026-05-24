package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        Employee employee = employeeMapper.getByUsername(username);

        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 使用 MD5 对密码进行加密，并转换为十六进制字符串
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(employee.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDTO 员工信息
     * @return 员工信息
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();

        // 将 employeeDTO 中的同名属性复制到 employee 中
        BeanUtils.copyProperties(employeeDTO, employee);

        //1.补充缺失的属性
        //补充密码，需要MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE); // 补充状态，默认启用
        employee.setCreateTime(LocalDateTime.now()); // 补充创建时间
        employee.setUpdateTime(LocalDateTime.now()); // 补充更新时间

        employee.setCreateUser(BaseContext.getCurrentId()); // 补充创建人
        employee.setUpdateUser(BaseContext.getCurrentId()); // 补充更新人

        //2.调用Mapper层方法，保存员工信息
        employeeMapper.insert(employee);
    }

    /**
     * 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {

        //1.设置分页
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        //2.调用mapper的分页查询方法，并强转返回结果为Page
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        //3.返回结果
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 启用禁用员工
     *
     * @param status 状态
     * @param id 员工id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        // 构建Employee对象，用于更新
        Employee employee = Employee.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();

        employeeMapper.update(employee);
    }

    /**
     * 根据id查询员工信息
     *
     * @param id
     * @return 员工信息
     */
    @Override
    public Employee getById(Long id) {
        return employeeMapper.getById(id);
    }
}
