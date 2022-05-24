package com.boyalong.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyalong.reggie.dto.DishDto;
import com.boyalong.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味，同时操作dish，dish_flavor
    void saveWithFlavor(DishDto dishDto);

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    DishDto getByIdWithFlavor(Long id);


    //更新菜品信息，同时更新对应的口味信息
    void updateWithFlavor(DishDto dishDto);

    /**
     * 根据id更新菜品状态
     * @param status
     * @param ids
     * @return
     */
    String updateStatusById(int status, List<Long> ids);

    //删除菜品,同时删除菜品对应的口味
    void deleteWithFlavor(List<Long> id);
}
