package com.bin.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bin.reggie.common.R;
import com.bin.reggie.entity.Category;
import com.bin.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增菜品分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){

        Page<Category> pageInfo = new Page(page,pageSize);
        //构建条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        //排序条件
        queryWrapper.orderByAsc(Category::getSort);

        //执行查询
       categoryService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long id){

//        categoryService.removeById(id);

        categoryService.remove(id);
        return R.success("删除成功");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);

        return R.success("修改分类信息成功");

    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);

        return R.success(list);
    }

}
