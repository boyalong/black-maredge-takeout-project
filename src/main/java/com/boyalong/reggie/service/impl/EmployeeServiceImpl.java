package com.boyalong.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyalong.reggie.common.R;
import com.boyalong.reggie.entity.Employee;
import com.boyalong.reggie.mapper.EmployeeMapper;
import com.boyalong.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

@Service                                   //这两个泛型一个是实体类对应的mapper,一个是实体类
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @Override
    public R<Employee> login(HttpServletRequest request, Employee employee) {
        //        1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //        2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        //在设计数据库的时候我们对username使用了唯一索引,所以这里可以使用getOne方法
        Employee emp = employeeMapper.selectOne(queryWrapper);

        //        3、如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("登录失败,用户不存在");
        }
        //        4、密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败，密码错误");
        }
        //        5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        //        6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());

        //把从数据库中查询到的用户返回出去
        return R.success(emp);
    }


}
