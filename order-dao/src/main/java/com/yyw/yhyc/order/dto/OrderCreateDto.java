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

    private static final long serialVersionUID = 4089608300520010684L;

    /* 采购商企业id */
    private Integer custId;

    /* 收货人地址id */
    private Integer receiveAddressId;

    /* 订单信息集合 */
    private List<OrderDto> orderDtoList;

    /* 收货地址信息 */
    private OrderDeliveryDto orderDeliveryDto;

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getReceiveAddressId() {
        return receiveAddressId;
    }

    public void setReceiveAddressId(Integer receiveAddressId) {
        this.receiveAddressId = receiveAddressId;
    }

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
                "custId=" + custId +
                ", receiveAddressId=" + receiveAddressId +
                ", orderDtoList=" + orderDtoList +
                ", orderDeliveryDto=" + orderDeliveryDto +
                '}';
    }
}
