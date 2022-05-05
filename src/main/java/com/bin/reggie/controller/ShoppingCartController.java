package com.bin.reggie.controller;

import com.bin.reggie.common.R;
import com.bin.reggie.entity.ShoppingCart;
import com.bin.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        return shoppingCartService.add(shoppingCart);
    }

    /**
     * 从购物车删除
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        return shoppingCartService.deleteFromShoppingCart(shoppingCart);
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(ShoppingCart shoppingCart){
        return shoppingCartService.list(shoppingCart);
    }


    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){
        shoppingCartService.clean();
        return R.success("清空购物车成功");
    }
}