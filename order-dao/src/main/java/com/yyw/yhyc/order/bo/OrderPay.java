/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:49
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.order.bo.Model;

public class OrderPay extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	
	  */
	private java.lang.Integer orderPayId;

	/**
	  *	订ID
	  */
	private java.lang.Integer orderId;

	/**
	  *	支付类型表ID
	  */
	private java.lang.Integer payTypeId;

	/**
	  *	支付流水号
	  */
	private java.lang.String payFlowId;

	/**
	  *	
	  */
	private java.lang.String payTime;

	/**
	  *	支付状态 0 未支付  1 已支付 
	  */
	private java.lang.String payStatus;

	/**
	  *	付款账号名称
	  */
	private java.lang.String payAccountName;

	/**
	  *	付款账号
	  */
	private java.lang.String payAccountNo;

	/**
	  *	支付金额
	  */
	private java.math.BigDecimal payMoney;

	/**
	  *	订单金额
	  */
	private java.math.BigDecimal orderMoney;

	/**
	  *	收款账号名称
	  */
	private java.lang.String receiveAccountName;

	/**
	  *	收款账号
	  */
	private java.lang.String receiveAccountNo;

	/**
	  *	支付平台返回信息
	  */
	private java.lang.String paymentPlatforReturn;

	/**
	  *	
	  */
	public java.lang.Integer getOrderPayId() 
	{
		return orderPayId;
	}
	
	/**
	  *	
	  */
	public void setOrderPayId(java.lang.Integer orderPayId) 
	{
		this.orderPayId = orderPayId;
	}
	
	/**
	  *	订ID
	  */
	public java.lang.Integer getOrderId() 
	{
		return orderId;
	}
	
	/**
	  *	订ID
	  */
	public void setOrderId(java.lang.Integer orderId) 
	{
		this.orderId = orderId;
	}
	
	/**
	  *	支付类型表ID
	  */
	public java.lang.Integer getPayTypeId() 
	{
		return payTypeId;
	}
	
	/**
	  *	支付类型表ID
	  */
	public void setPayTypeId(java.lang.Integer payTypeId) 
	{
		this.payTypeId = payTypeId;
	}
	
	/**
	  *	支付流水号
	  */
	public java.lang.String getPayFlowId() 
	{
		return payFlowId;
	}
	
	/**
	  *	支付流水号
	  */
	public void setPayFlowId(java.lang.String payFlowId) 
	{
		this.payFlowId = payFlowId;
	}
	
	/**
	  *	
	  */
	public java.lang.String getPayTime() 
	{
		return payTime;
	}
	
	/**
	  *	
	  */
	public void setPayTime(java.lang.String payTime) 
	{
		this.payTime = payTime;
	}
	
	/**
	  *	支付状态 0 未支付  1 已支付 
	  */
	public java.lang.String getPayStatus() 
	{
		return payStatus;
	}
	
	/**
	  *	支付状态 0 未支付  1 已支付 
	  */
	public void setPayStatus(java.lang.String payStatus) 
	{
		this.payStatus = payStatus;
	}
	
	/**
	  *	付款账号名称
	  */
	public java.lang.String getPayAccountName() 
	{
		return payAccountName;
	}
	
	/**
	  *	付款账号名称
	  */
	public void setPayAccountName(java.lang.String payAccountName) 
	{
		this.payAccountName = payAccountName;
	}
	
	/**
	  *	付款账号
	  */
	public java.lang.String getPayAccountNo() 
	{
		return payAccountNo;
	}
	
	/**
	  *	付款账号
	  */
	public void setPayAccountNo(java.lang.String payAccountNo) 
	{
		this.payAccountNo = payAccountNo;
	}
	
	/**
	  *	支付金额
	  */
	public java.math.BigDecimal getPayMoney() 
	{
		return payMoney;
	}
	
	/**
	  *	支付金额
	  */
	public void setPayMoney(java.math.BigDecimal payMoney) 
	{
		this.payMoney = payMoney;
	}
	
	/**
	  *	订单金额
	  */
	public java.math.BigDecimal getOrderMoney() 
	{
		return orderMoney;
	}
	
	/**
	  *	订单金额
	  */
	public void setOrderMoney(java.math.BigDecimal orderMoney) 
	{
		this.orderMoney = orderMoney;
	}
	
	/**
	  *	收款账号名称
	  */
	public java.lang.String getReceiveAccountName() 
	{
		return receiveAccountName;
	}
	
	/**
	  *	收款账号名称
	  */
	public void setReceiveAccountName(java.lang.String receiveAccountName) 
	{
		this.receiveAccountName = receiveAccountName;
	}
	
	/**
	  *	收款账号
	  */
	public java.lang.String getReceiveAccountNo() 
	{
		return receiveAccountNo;
	}
	
	/**
	  *	收款账号
	  */
	public void setReceiveAccountNo(java.lang.String receiveAccountNo) 
	{
		this.receiveAccountNo = receiveAccountNo;
	}
	
	/**
	  *	支付平台返回信息
	  */
	public java.lang.String getPaymentPlatforReturn() 
	{
		return paymentPlatforReturn;
	}
	
	/**
	  *	支付平台返回信息
	  */
	public void setPaymentPlatforReturn(java.lang.String paymentPlatforReturn) 
	{
		this.paymentPlatforReturn = paymentPlatforReturn;
	}
	
	public String toString()
	{
		return "OrderPay [" + 
					"orderPayId=" + orderPayId + 
					", orderId=" + orderId + 
					", payTypeId=" + payTypeId + 
					", payFlowId=" + payFlowId + 
					", payTime=" + payTime + 
					", payStatus=" + payStatus + 
					", payAccountName=" + payAccountName + 
					", payAccountNo=" + payAccountNo + 
					", payMoney=" + payMoney + 
					", orderMoney=" + orderMoney + 
					", receiveAccountName=" + receiveAccountName + 
					", receiveAccountNo=" + receiveAccountNo + 
					", paymentPlatforReturn=" + paymentPlatforReturn + 
				"]";
	}
}

