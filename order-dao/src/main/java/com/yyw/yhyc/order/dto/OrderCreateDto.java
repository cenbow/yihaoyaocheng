package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.OrderDelivery;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhou on 2016/7/29
 *
 * OrderCreateDto 只在请求创建订单的时候使用
 */
public class OrderCreateDto implements Serializable {

    private static final long serialVersionUID = 3281292140667356405L;

    /* 订单信息集合 */
    private List<OrderDto> orderDtoList;

    /* 收货地址信息 */
    private OrderDeliveryDto orderDeliveryDto;


    public List<OrderDto> getOrderDtoList() {
        return orderDtoList;
    }

    public void setOrderDtoList(List<OrderDto> orderDtoList) {
        this.orderDtoList = orderDtoList;
    }

    public OrderDeliveryDto getOrderDeliveryDto() {
        return orderDeliveryDto;
    }

    public void setOrderDeliveryDto(OrderDeliveryDto orderDeliveryDto) {
        this.orderDeliveryDto = orderDeliveryDto;
    }

    @Override
    public String toString() {
        return "OrderCreateDto{" +
                "orderDtoList=" + orderDtoList +
                ", orderDeliveryDto=" + orderDeliveryDto +
                '}';
    }
}
