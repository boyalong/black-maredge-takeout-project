package com.boyalong.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyalong.reggie.common.R;
import com.boyalong.reggie.entity.Employee;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    R<Employee> login(HttpServletRequest request, Employee employee);

}
