package com.bin.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bin.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DishMapper extends BaseMapper<Dish> {

}
