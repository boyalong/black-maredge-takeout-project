package com.boyalong.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyalong.reggie.common.BaseContext;
import com.boyalong.reggie.common.R;
import com.boyalong.reggie.entity.ShoppingCart;
import com.boyalong.reggie.mapper.ShoppingCartMapper;
import com.boyalong.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {


    //添加商品到购物车
    @Override
    @Transactional
    public R<ShoppingCart> add(ShoppingCart shoppingCart) {
        //查询当前用户id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        //查询当前套餐或菜品是否在购物车中
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) {
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = this.getOne(queryWrapper);

        if (cartServiceOne != null) {
            //如果存在+1
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            this.updateById(cartServiceOne);
        } else {
            //如果不存在，添加到购物车
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return R.success(cartServiceOne);
    }

    /**
     * 删除购物车商品
     * @param shoppingCart
     * @return
     */
    @Override
    @Transactional
    public R<ShoppingCart> deleteFromShoppingCart(ShoppingCart shoppingCart) {
        //查询当前菜品是否在购物车中
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        if (dishId != null) {
            //添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
            ShoppingCart cart1 = this.getOne(queryWrapper);
            cart1.setNumber(cart1.getNumber() - 1);
            Integer lastNumber = cart1.getNumber();
            if (lastNumber > 0) {
                //购物车内菜品数量减少
                this.updateById(cart1);
            } else if (lastNumber == 0) {
                //删除购物车内菜品
                this.removeById(cart1.getId());
            } else {
                return R.error("数量最少为0");
            }

            return R.success(cart1);
        }
        //查询当前菜品是否在购物车中
        Long setmealId = shoppingCart.getSetmealId();
        if (setmealId != null) {
            //购物车内套餐数量减少
            queryWrapper.eq(ShoppingCart::getSetmealId, setmealId);
            ShoppingCart cart2 = this.getOne(queryWrapper);
            cart2.setNumber(cart2.getNumber() - 1);
            Integer lastNumber = cart2.getNumber();
            if (lastNumber > 0) {
                //购物车内套餐数量减少
                this.updateById(cart2);
            } else if (lastNumber == 0) {
                //删除购物车内套餐
                this.removeById(cart2.getId());
            } else {
                return R.error("数量最少为0");
            }
            return R.success(cart2);
        }
        return R.error("操作异常");
    }

    /**
     * 查看购物车
     * @param shoppingCart
     * @return
     */
    @Override
    public R<List<ShoppingCart>> list(ShoppingCart shoppingCart) {

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId())
                .orderByDesc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = this.list(queryWrapper);

        return R.success(list);

    }

    /**
     * 清空购物车
     * @return
     */
    @Override
    @Transactional
    public void clean() {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

        this.remove(queryWrapper);
    }
}

