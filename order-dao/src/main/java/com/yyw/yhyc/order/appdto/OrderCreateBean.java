package com.yyw.yhyc.order.appdto;

import java.io.Serializable;
import java.util.List;

/**
 * 提交订单的bean
 * Created by lizhou on 2016/9/21
 */
public class OrderCreateBean implements Serializable{

    private static final long serialVersionUID = -3782754674350009148L;

    private Integer addressId; //收货地址id
    private int payType;
    private int billType;
    private List<OrderBean> orderList;

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getBillType() {
        return billType;
    }

    public void setBillType(int billType) {
        this.billType = billType;
    }

    public List<OrderBean> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderBean> orderList) {
        this.orderList = orderList;
    }

    @Override
    public String toString() {
        return "OrderCreateBean{" +
                "addressId=" + addressId +
                ", payType=" + payType +
                ", billType=" + billType +
                ", orderList=" + orderList +
                '}';
    }
}
