package com.boyalong.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boyalong.reggie.common.R;
import com.boyalong.reggie.entity.ShoppingCart;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {


    /**
     * 添加商品到购物车
     * @param shoppingCart
     * @return
     */
    R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart);


    /**
     * 从购物车删除
     * @param shoppingCart
     * @return
     */
    R<ShoppingCart> deleteFromShoppingCart(ShoppingCart shoppingCart);

    /**
     * 查看购物车
     * @param shoppingCart
     * @return
     */
    R<List<ShoppingCart>> list(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * @return
     */
    void clean();
}
