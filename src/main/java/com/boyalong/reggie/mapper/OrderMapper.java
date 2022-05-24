package com.boyalong.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyalong.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
