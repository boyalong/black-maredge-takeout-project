package com.boyalong.reggie.dto;

import com.boyalong.reggie.entity.OrderDetail;
import com.boyalong.reggie.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @Author: boyalong
 * @Description:
 */
@Data
public class OrderDto extends Orders {

    private List<OrderDetail> orderDetails;
}
