package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.OrderPay;

import java.math.BigDecimal;

/**
 * Created by jiangshuai on 2016/8/30.
 */
public class OrderPayDto extends OrderPay{

    /**
     *	收款账号
     */
    private java.lang.String receiveAccountNo;

    /**
     *	收款款账号名称
     */
    private java.lang.String receiveAccountName;


    /**
     *	订单优惠后金额
     */
    private java.math.BigDecimal orgTotal;


    /**
     *	客户ID
     */
    private Integer custId;

    /**
     *	供应商ID
     */
    private Integer supplyId;

    @Override
    public String getReceiveAccountNo() {
        return receiveAccountNo;
    }

    @Override
    public void setReceiveAccountNo(String receiveAccountNo) {
        this.receiveAccountNo = receiveAccountNo;
    }

    @Override
    public String getReceiveAccountName() {
        return receiveAccountName;
    }

    @Override
    public void setReceiveAccountName(String receiveAccountName) {
        this.receiveAccountName = receiveAccountName;
    }

    public BigDecimal getOrgTotal() {
        return orgTotal;
    }

    public void setOrgTotal(BigDecimal orgTotal) {
        this.orgTotal = orgTotal;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }
}
