package com.bin.reggie.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bin.reggie.common.R;
import com.bin.reggie.entity.Orders;

import java.time.LocalDateTime;


public interface OrderService extends IService<Orders> {


    /**
     * 用户下单
     * @param orders
     */
    void submit(Orders orders);



    /**
     * 订单分页查询
     * @param page
     * @param pageSize
     * @param number
     * @param orderTime
     * @param checkoutTime
     * @return
     */
    Page pageWithOrder(int page, int pageSize, Long number, LocalDateTime orderTime, LocalDateTime checkoutTime);



    /**
     * 订单状态更新
     * @param orders
     * @return
     */
    String updateStatus(Orders orders);


    /**
     * 查看当前用户订单
     * @param page
     * @param pageSize
     * @return
     */
    R<Page> getUserPage(int page, int pageSize);


    /**
     * 再来一单回显购物车数据
     * @param orders
     * @return
     */
    R<String> orderAgain(Orders orders);
}
