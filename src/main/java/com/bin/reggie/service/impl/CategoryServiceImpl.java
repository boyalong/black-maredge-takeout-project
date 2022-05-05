package com.bin.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.reggie.common.CustomException;
import com.bin.reggie.entity.Category;
import com.bin.reggie.entity.Dish;
import com.bin.reggie.entity.Setmeal;
import com.bin.reggie.mapper.CategoryMapper;
import com.bin.reggie.service.CategoryService;
import com.bin.reggie.service.DishService;
import com.bin.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id){
        //查询是否关联菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);

        if(count > 0){
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        //查询是否关联套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if(count1 > 0){
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除
        super.removeById(id);
    }

}
