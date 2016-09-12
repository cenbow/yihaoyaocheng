package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.OrderDelivery;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuyang on 2016/9/10.
 */
public class OrderIssuedDto implements Serializable {
    /**
     *
     */
    private static final long	serialVersionUID	= 1L;
    private String orderCode;         //网站订单编号

    private String customerId;        //下单客户对应的ERP系统客户ID

    private Double orderAmount;       //网站订单金额

    private List<OrderIssuedItemDto> orderItemList;   //子订单

    private String leaveMessage;        //买家留言

    private String createTime;      //下单时间

    private OrderDelivery orderDelivery;    //收货信息

    private Integer orderId;

    private Integer custId;

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public List<OrderIssuedItemDto> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<OrderIssuedItemDto> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public OrderDelivery getOrderDelivery() {
        return orderDelivery;
    }

    public void setOrderDelivery(OrderDelivery orderDelivery) {
        this.orderDelivery = orderDelivery;
    }
}
