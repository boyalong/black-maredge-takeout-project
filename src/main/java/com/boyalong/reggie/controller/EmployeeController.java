package com.boyalong.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boyalong.reggie.common.R;
import com.boyalong.reggie.entity.Employee;
import com.boyalong.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")  //获取员工的id，并且把这个id存到session中   //接收前端的json数据,这个json数据是在请求体中的
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        return employeeService.login(request,employee);
    }

    /**
     * 退出功能
     * ①在controller中创建对应的处理方法来接受前端的请求，请求方式为post；
     * ②清理session中的用户id
     * ③返回结果（前端页面会进行跳转到登录页面）
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清除session中保存的当前员工信息
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
//        log.info("新增员工，员工信息：{}",employee.toString());
        //设置初始密码为123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置创建时间和更新时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //获取当前用户id
//        Long empID = (long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empID);     //创建人的id,就是当前用户的id（在进行添加操作的id）
//        employee.setUpdateUser(empID);    /最后的更新人是谁

        //增加员工

        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 员工信息分页查询
     * @param page  当前页数
     * @param pageSize 当前页最多存放数据条数,就是这一页查几条数据
     * @param name 根据name查询员工的信息
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
//        log.info("page = {},pageSize = {},name = {}",page,pageSize,name);

        //构造分页构造器   就是page对象
        Page<Employee> pageInfo = new Page(page,pageSize);

        //构建条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

        //排序条件
        queryWrapper.orderByDesc(Employee::getCreateTime);

        //执行查询  这里不用封装了mybatis-plus帮我们做好了
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 修改员工信息
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){

//        Long empId = (long)request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

    /**
     * 根据员工id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
