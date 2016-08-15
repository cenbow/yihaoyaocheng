package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.order.bo.OrderDetail;

import java.math.BigDecimal;
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
    //订单类型枚举值
   private String orderStatusName;

    //订单类型
    private int payType;

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    /**
     *	确认收货金额
     */
    private java.math.BigDecimal receiveTotal;

    /**
     *	商品总金额
     */
    private java.math.BigDecimal productTotal;


    public BigDecimal getProductTotal() {
        return productTotal;
    }

    public void setProductTotal(BigDecimal productTotal) {
        this.productTotal = productTotal;
    }

    private OrderDeliveryDetail orderDeliveryDetail;

    public BigDecimal getReceiveTotal() {
        return receiveTotal;
    }

    public void setReceiveTotal(BigDecimal receiveTotal) {
        this.receiveTotal = receiveTotal;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }


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
