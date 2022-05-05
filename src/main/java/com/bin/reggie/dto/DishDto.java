package com.bin.reggie.dto;

import com.bin.reggie.entity.Dish;
import com.bin.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class DishDto extends Dish {

    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;

}
