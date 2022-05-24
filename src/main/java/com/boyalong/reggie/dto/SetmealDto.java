package com.boyalong.reggie.dto;

import com.boyalong.reggie.entity.Setmeal;
import com.boyalong.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
