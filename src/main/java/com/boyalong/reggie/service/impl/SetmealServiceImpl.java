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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void setWithDish(SetmealDto setmealDto){
        //保存套餐基本信息，操作setmeal
        this.save(setmealDto);

        //保存套餐和菜品的关联信息，操作setmeal_dish ,执行insert操作
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //注意上面拿到的setmealDishes是没有setmeanlId这个的值的
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;//这里返回的就是集合的泛型
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
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids){
        //查询套餐的状态，看是否可以删除
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


    /**
     * 回显套餐数据：根据套餐id查询套餐
     * @return
     */
    @Override
    public SetmealDto getDate(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
        //在关联表中查询，setmealdish
        queryWrapper.eq(id!=null,SetmealDish::getSetmealId,id);

        if (setmeal != null){
            BeanUtils.copyProperties(setmeal,setmealDto);
            List<SetmealDish> list = setmealDishService.list(queryWrapper);
            setmealDto.setSetmealDishes(list);
            return setmealDto;
        }
        return null;
    }

}
