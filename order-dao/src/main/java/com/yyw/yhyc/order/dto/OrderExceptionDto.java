package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liqiang on 2016/8/8
 */
public class OrderExceptionDto extends OrderException {

    private static final long serialVersionUID = 418384253516688599L;

    private int payType;                                    //支付类型
    private String payTypeName;                             //支付类型名称
    private List<OrderReturnDto>  orderReturnList;         //退货商品列表
    private OrderDelivery orderDelivery;                    //订单收货信息
    private UsermanageEnterprise  usermanageEnterprise;    //订单发货信息
    private Integer  userType;                                  //1、采购商2、供应商

    /* 商品总金额 */
    private BigDecimal productPriceCount;

    /* 订单总金额 */
    private BigDecimal orderPriceCount;

    /* 异常订单状态 */
    private String orderStatusName;

    /* 原订单信息 */
    private Order order;

    /*拒收查询开始时间*/
    private String startTime;

    /*拒收查询结束时间*/
    private String endTime;

    public OrderExceptionDto() {
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public List<OrderReturnDto> getOrderReturnList() {
        return orderReturnList;
    }

    public void setOrderReturnList(List<OrderReturnDto> orderReturnList) {
        this.orderReturnList = orderReturnList;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public OrderDelivery getOrderDelivery() {
        return orderDelivery;
    }

    public void setOrderDelivery(OrderDelivery orderDelivery) {
        this.orderDelivery = orderDelivery;
    }

    public UsermanageEnterprise getUsermanageEnterprise() {
        return usermanageEnterprise;
    }

    public void setUsermanageEnterprise(UsermanageEnterprise usermanageEnterprise) {
        this.usermanageEnterprise = usermanageEnterprise;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getProductPriceCount() {
        return productPriceCount;
    }

    public void setProductPriceCount(BigDecimal productPriceCount) {
        this.productPriceCount = productPriceCount;
    }

    public BigDecimal getOrderPriceCount() {
        return orderPriceCount;
    }

    public void setOrderPriceCount(BigDecimal orderPriceCount) {
        this.orderPriceCount = orderPriceCount;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "OrderExceptionDto{" +
                "payType=" + payType +
                ", payTypeName='" + payTypeName + '\'' +
                ", orderReturnList=" + orderReturnList +
                ", orderDelivery=" + orderDelivery +
                ", usermanageEnterprise=" + usermanageEnterprise +
                ", userType=" + userType +
                ", productPriceCount=" + productPriceCount +
                ", orderPriceCount=" + orderPriceCount +
                ", orderStatusName='" + orderStatusName + '\'' +
                ", order=" + order +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
