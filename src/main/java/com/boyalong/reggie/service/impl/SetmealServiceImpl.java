package com.boyalong.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyalong.reggie.common.CustomException;
import com.boyalong.reggie.dto.SetmealDto;
import com.boyalong.reggie.entity.Setmeal;
import com.boyalong.reggie.entity.SetmealDish;
import com.boyalong.reggie.mapper.SetmealMapper;
import com.boyalong.reggie.service.SetmealDishService;
import com.boyalong.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    public void setWithDish(SetmealDto setmealDto){
        //保存套餐基本信息，操作setmeal
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐菜品信息，操作setmeal_dish
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public String updateStatusById(int status, List<Long> ids) {
        for (Long id :ids){
            LambdaUpdateWrapper<Setmeal> setmealLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            setmealLambdaUpdateWrapper.eq(Setmeal::getId, id).set(Setmeal::getStatus,status);
            this.update(setmealLambdaUpdateWrapper);
        }
        return "更新菜品状态成功";
    }


    /**
     * 删除套餐及其对应菜品
     * @param ids
     */
    public void removeWithDish(List<Long> ids){
        //查询是否在售
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId,ids)
                .eq(Setmeal::getStatus,1);

        //判断是否能删除
        int count = this.count(setmealLambdaQueryWrapper);
        if(count > 0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //如果可以删除先删setmeal表中数据
        this.removeByIds(ids);
        //再删除setmeal_dish中的数据
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }


}
