package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;

import java.util.List;

/**
 * Created by liqiang on 2016/8/8.
 */
public class OrderExceptionDto extends OrderException {
    private int payType;                                    //支付类型
    private String payTypeName;                             //支付类型名称
    private List<OrderReturnDto>  orderReturnList;         //退货商品列表
    private OrderDelivery orderDelivery;                    //订单收货信息
    private UsermanageEnterprise  usermanageEnterprise;    //订单发货信息
    private int  userType;                                  //1、采购商2、供应商

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

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
