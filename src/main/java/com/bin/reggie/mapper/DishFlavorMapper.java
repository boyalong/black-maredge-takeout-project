package com.bin.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bin.reggie.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}
