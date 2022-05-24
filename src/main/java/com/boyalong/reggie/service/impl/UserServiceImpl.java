package com.boyalong.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyalong.reggie.entity.User;
import com.boyalong.reggie.mapper.UserMapper;
import com.boyalong.reggie.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}
