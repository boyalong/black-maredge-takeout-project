package com.boyalong.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyalong.reggie.entity.OrderDetail;
import com.boyalong.reggie.mapper.OrderDetailMapper;
import com.boyalong.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}