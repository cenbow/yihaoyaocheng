package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.order.bo.OrderDetail;

import java.util.List;

/**
 * Created by jiangshuai on 2016/7/29.
 */
public class OrderDetailsDto extends Order {

    //收货信息
    private OrderDelivery orderDelivery;
    //订单详情
    private List<OrderDetail> details;
    //支付类型枚举名
    private String payTypeName;

    private OrderDeliveryDetail orderDeliveryDetail;

    public OrderDeliveryDetail getOrderDeliveryDetail() {
        return orderDeliveryDetail;
    }

    public void setOrderDeliveryDetail(OrderDeliveryDetail orderDeliveryDetail) {
        this.orderDeliveryDetail = orderDeliveryDetail;
    }

    public OrderDelivery getOrderDelivery() {
        return orderDelivery;
    }

    public void setOrderDelivery(OrderDelivery orderDelivery) {
        this.orderDelivery = orderDelivery;
    }

    public List<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetail> details) {
        this.details = details;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }
}
