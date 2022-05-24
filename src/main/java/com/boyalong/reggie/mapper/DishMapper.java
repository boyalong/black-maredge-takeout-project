package com.boyalong.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boyalong.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DishMapper extends BaseMapper<Dish> {

}
