package com.boyalong.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyalong.reggie.dto.SetmealDto;
import com.boyalong.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    //添加套餐及其对应菜品信息
    public void setWithDish(SetmealDto setmealDto);

    //删除套餐及其对应菜品
    public void removeWithDish(List<Long> ids);

    /**
     * 根据id更新菜品状态
     * @param status
     * @param ids
     * @return
     */
    public String updateStatusById(int status, List<Long> ids);

    /**
     * 回显套餐数据：根据套餐id查询套餐
     * @return
     */
    SetmealDto getDate(Long id);
}
