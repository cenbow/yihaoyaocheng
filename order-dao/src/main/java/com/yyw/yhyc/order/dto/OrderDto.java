package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.order.bo.OrderDetail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created by zhangqiang on 2016/7/29.
 */
public class OrderDto extends Order {
    private int orderCount;            //订单数量
    private int payType;               //支付类型
    private String payTypeName;        //支付类型名称
    private String orderStatusName;    //订单状态描述
    private List<OrderDetail> orderDetailList;//订单商品列表
    private String createBeginTime;    //下单开始时间
    private String createEndTime;      //下单结束时间
    private String nowTime;
    /* 商品信息集合 */
    private List<ProductInfoDto> productInfoDtoList;


    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public List<OrderDetail> getOrderDetailList() {
        return orderDetailList;
    }

    public void setOrderDetailList(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public String getCreateBeginTime() {
        return createBeginTime;
    }

    public void setCreateBeginTime(String createBeginTime) {
        this.createBeginTime = createBeginTime;
    }

    public String getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = createEndTime;
    }

    public List<ProductInfoDto> getProductInfoDtoList() {
        return productInfoDtoList;
    }

    public void setProductInfoDtoList(List<ProductInfoDto> productInfoDtoList) {
        this.productInfoDtoList = productInfoDtoList;
    }

    public String getNowTime() {
        return nowTime;
    }

    public void setNowTime(String nowTime) {
        this.nowTime = nowTime;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                super.toString()+
                ", orderCount=" + orderCount +
                ", payType=" + payType +
                ", payTypeName='" + payTypeName + '\'' +
                ", orderStatusName='" + orderStatusName + '\'' +
                ", orderDetailList=" + orderDetailList +
                ", createBeginTime='" + createBeginTime + '\'' +
                ", createEndTime='" + createEndTime + '\'' +
                ", nowTime='" + nowTime + '\'' +
                ", productInfoDtoList=" + productInfoDtoList +
                '}';
    }
}
