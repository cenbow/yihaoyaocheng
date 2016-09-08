package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.order.bo.OrderDetail;

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
    private String nowTime;            //数据库当前时间
    private long residualTime ;        //支付剩余时间
    private long receivedTime ;         //确认收货剩余时间
    private String province;           //省编码
    private String city;               //市编码
    private String district;           //区编码
    /* 商品信息集合 */
    private List<ProductInfoDto> productInfoDtoList;

    private OrderDelivery orderDelivery;//订单发货信息

    /* 供应商对采供商设置的账期额度，1 表示账期额度可以用。  0 表示账期额度已用完 或 没有设置账期额度 */
    private int accountAmount ;


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

    public long getResidualTime() {
        return residualTime;
    }

    public void setResidualTime(long residualTime) {
        this.residualTime = residualTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public OrderDelivery getOrderDelivery() {
        return orderDelivery;
    }

    public void setOrderDelivery(OrderDelivery orderDelivery) {
        this.orderDelivery = orderDelivery;
    }

    public long getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(long receivedTime) {
        this.receivedTime = receivedTime;
    }

    public int getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(int accountAmount) {
        this.accountAmount = accountAmount;
    }

    @Override
    public String toString() {
        return "OrderDto{" +
                super.toString()+
                "orderCount=" + orderCount +
                ", payType=" + payType +
                ", payTypeName='" + payTypeName + '\'' +
                ", orderStatusName='" + orderStatusName + '\'' +
                ", orderDetailList=" + orderDetailList +
                ", createBeginTime='" + createBeginTime + '\'' +
                ", createEndTime='" + createEndTime + '\'' +
                ", nowTime='" + nowTime + '\'' +
                ", residualTime=" + residualTime +
                ", receivedTime=" + receivedTime +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", productInfoDtoList=" + productInfoDtoList +
                ", orderDelivery=" + orderDelivery +
                ", accountAmount=" + accountAmount +
                '}';
    }
}
