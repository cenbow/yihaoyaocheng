package com.yyw.yhyc.order.dto;

import com.sun.org.apache.xpath.internal.operations.String;
import com.yyw.yhyc.order.bo.OrderSettlement;

/**
 * Created by liuhaikuo on 2016/7/28.
 */
public class OrderSettlementDto extends OrderSettlement{
    private String province;            //省
    private String city;                //市
    private String area;                //区
    private String startTime;           //下单时间 开始
    private String endTime;             //下单时间 结束
    private String payType;             //支付类型

    private String payTypeName;         //支付名称
    private String businessTypeName ;  //业务类型名
    private String orderCreateTime ;  //下单时间
    private String confirmSettlementName; //结算状态

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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }
}
