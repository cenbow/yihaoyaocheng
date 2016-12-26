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
    /**
	  *	供应商名称
	  */
	private java.lang.String supplyName;
	private  Integer supplyId; //
    private String createTime;      //下单时间

    private OrderDelivery orderDelivery;    //收货信息

    private Integer orderId;

    private String custId;

    private Integer payType; //支付类型

    public Integer getPayType() {
        return payType;
    }

    /**
     * erp订单编号
     */
    private java.lang.String erpOrderCode;

    public String getErpOrderCode() {
        return erpOrderCode;
    }

    public void setErpOrderCode(String erpOrderCode) {
        this.erpOrderCode = erpOrderCode;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
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

	/**
	 * @return the supplyName
	 */
	public java.lang.String getSupplyName() {
		return supplyName;
	}

	/**
	 * @param supplyName the supplyName to set
	 */
	public void setSupplyName(java.lang.String supplyName) {
		this.supplyName = supplyName;
	}

	/**
	 * @return the custId
	 */
	public String getCustId() {
		return custId;
	}

	/**
	 * @param custId the custId to set
	 */
	public void setCustId(String custId) {
		this.custId = custId;
	}

	public Integer getSupplyId() {
		return supplyId;
	}

	public void setSupplyId(Integer supplyId) {
		this.supplyId = supplyId;
	}
	
	
}
