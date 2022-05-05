package com.bin.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.reggie.common.CustomException;
import com.bin.reggie.dto.DishDto;
import com.bin.reggie.entity.Dish;
import com.bin.reggie.entity.DishFlavor;
import com.bin.reggie.mapper.DishMapper;
import com.bin.reggie.service.DishFlavorService;
import com.bin.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    /**
     * 新增菜品同时保存对应的口味信息
     * @param dishDto
     */
    @Transactional
    public void saveWithFlavor(@RequestBody DishDto dishDto) {
        //保存菜品基本信息
        this.save(dishDto);
        //保存口味信息
        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item)->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }



    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        //清理对应口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        //添加新的对应口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item)->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id更新菜品状态
     * @param status
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public String updateStatusById(int status, List<Long> ids) {
        for (Long id :ids){
            LambdaUpdateWrapper<Dish> DishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            DishLambdaUpdateWrapper.eq(Dish::getId, id).set(Dish::getStatus,status);
            this.update(DishLambdaUpdateWrapper);
        }
        return "更新菜品成功  ";
    }

    /**
     * 根据id删除菜品和对应的口味
     * @param id
     * @return
     */
    @Override
    @Transactional
    public void deleteWithFlavor(List<Long> id) {
        //判断菜品状态
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.in(Dish::getId,id);
        dishQueryWrapper.eq(Dish::getStatus,1);
        int count = this.count(dishQueryWrapper);
        if (count > 0){
            //不能删除，抛出业务异常
            throw new CustomException("菜品正在售卖中，不能删除");
        }
        //删除菜品
        this.removeByIds(id);
        //删除菜品关联的口味的数据
        LambdaQueryWrapper<DishFlavor> flavorQueryWrapper = new LambdaQueryWrapper<>();
        flavorQueryWrapper.in(DishFlavor::getDishId,id);
        dishFlavorService.remove(flavorQueryWrapper);
    }


}
