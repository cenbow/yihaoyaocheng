/**
 * Created By: XI
 * Created On: 2016-10-24 15:52:00
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.bo.Model;

public class OrderIssuedException extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	订单下发ID主键
	  */
	private java.lang.Integer issuedExceptionId;

	/**
	  *	订单编号
	  */
	private java.lang.String flowId;

	/**
	  *	采购商名称
	  */
	private java.lang.String custName;

	/**
	  *	采购商ID-买家
	  */
	private java.lang.Integer custId;

	/**
	  *	供应商名称
	  */
	private java.lang.String supplyName;

	/**
	  *	供应商ID
	  */
	private java.lang.Integer supplyId;

	/**
	 * 支付类型 
	 */
	private java.lang.Integer payType;
	/**
	 * 支付类型名称
	 */
	private java.lang.String payTypeName;
	/**
	  *	订单生成时间
	  */
	private java.lang.String orderCreateTime;

	/**
	  *	收货人
	  */
	private java.lang.String receivePerson;

	/**
	  *	收货详细地址
	  */
	private java.lang.String receiveAddress;

	/**
	  *	收货人联系电话
	  */
	private java.lang.String receiveContactPhone;

	/**
	  *	处理状态 0：处理中；1：待处理；2：已完成
	  */
	private java.lang.Integer dealStatus;

	/**
	  *	异常类型（0下发超时未返回，1无关联企业用户，2下发返回错误，3下发失败）
	  */
	private java.lang.Integer exceptionType;

	/**
	  *	处理时间
	  */
	private java.lang.String operateTime;

	/**
	  *	操作人（用户名）
	  */
	private java.lang.String operator;

	/**
	  *	订单下发ID主键
	  */
	public java.lang.Integer getIssuedExceptionId() 
	{
		return issuedExceptionId;
	}
	
	/**
	  *	订单下发ID主键
	  */
	public void setIssuedExceptionId(java.lang.Integer issuedExceptionId) 
	{
		this.issuedExceptionId = issuedExceptionId;
	}
	
	/**
	  *	订单编号
	  */
	public java.lang.String getFlowId() 
	{
		return flowId;
	}
	
	/**
	  *	订单编号
	  */
	public void setFlowId(java.lang.String flowId) 
	{
		this.flowId = flowId;
	}
	
	/**
	  *	采购商名称
	  */
	public java.lang.String getCustName() 
	{
		return custName;
	}
	
	/**
	  *	采购商名称
	  */
	public void setCustName(java.lang.String custName) 
	{
		this.custName = custName;
	}
	
	/**
	  *	采购商ID-买家
	  */
	public java.lang.Integer getCustId() 
	{
		return custId;
	}
	
	/**
	  *	采购商ID-买家
	  */
	public void setCustId(java.lang.Integer custId) 
	{
		this.custId = custId;
	}
	
	/**
	  *	供应商名称
	  */
	public java.lang.String getSupplyName() 
	{
		return supplyName;
	}
	
	/**
	  *	供应商名称
	  */
	public void setSupplyName(java.lang.String supplyName) 
	{
		this.supplyName = supplyName;
	}
	
	/**
	  *	供应商ID
	  */
	public java.lang.Integer getSupplyId() 
	{
		return supplyId;
	}
	
	/**
	  *	供应商ID
	  */
	public void setSupplyId(java.lang.Integer supplyId) 
	{
		this.supplyId = supplyId;
	}
	
	/**
	  *	订单生成时间
	  */
	public java.lang.String getOrderCreateTime() 
	{
		return orderCreateTime;
	}
	
	/**
	  *	订单生成时间
	  */
	public void setOrderCreateTime(java.lang.String orderCreateTime) 
	{
		this.orderCreateTime = orderCreateTime;
	}
	
	/**
	  *	收货人
	  */
	public java.lang.String getReceivePerson() 
	{
		return receivePerson;
	}
	
	/**
	  *	收货人
	  */
	public void setReceivePerson(java.lang.String receivePerson) 
	{
		this.receivePerson = receivePerson;
	}
	
	/**
	  *	收货详细地址
	  */
	public java.lang.String getReceiveAddress() 
	{
		return receiveAddress;
	}
	
	/**
	  *	收货详细地址
	  */
	public void setReceiveAddress(java.lang.String receiveAddress) 
	{
		this.receiveAddress = receiveAddress;
	}
	
	/**
	  *	收货人联系电话
	  */
	public java.lang.String getReceiveContactPhone() 
	{
		return receiveContactPhone;
	}
	
	/**
	  *	收货人联系电话
	  */
	public void setReceiveContactPhone(java.lang.String receiveContactPhone) 
	{
		this.receiveContactPhone = receiveContactPhone;
	}
	
	/**
	  *	处理状态 0：处理中；1：待处理；2：已完成
	  */
	public java.lang.Integer getDealStatus() 
	{
		return dealStatus;
	}
	
	/**
	  *	处理状态 0：处理中；1：待处理；2：已完成
	  */
	public void setDealStatus(java.lang.Integer dealStatus) 
	{
		this.dealStatus = dealStatus;
	}
	
	/**
	  *	异常类型（0下发超时未返回，1无关联企业用户，2下发返回错误，3下发失败）
	  */
	public java.lang.Integer getExceptionType() 
	{
		return exceptionType;
	}
	
	/**
	  *	异常类型（0下发超时未返回，1无关联企业用户，2下发返回错误，3下发失败）
	  */
	public void setExceptionType(java.lang.Integer exceptionType) 
	{
		this.exceptionType = exceptionType;
	}
	
	/**
	  *	处理时间
	  */
	public java.lang.String getOperateTime() 
	{
		return operateTime;
	}
	
	/**
	  *	处理时间
	  */
	public void setOperateTime(java.lang.String operateTime) 
	{
		this.operateTime = operateTime;
	}
	
	/**
	  *	操作人（用户名）
	  */
	public java.lang.String getOperator() 
	{
		return operator;
	}
	
	/**
	  *	操作人（用户名）
	  */
	public void setOperator(java.lang.String operator) 
	{
		this.operator = operator;
	}
	
	public String toString()
	{
		return "OrderIssuedException [" + 
					"issuedExceptionId=" + issuedExceptionId + 
					", flowId=" + flowId + 
					", custName=" + custName + 
					", custId=" + custId + 
					", supplyName=" + supplyName + 
					", supplyId=" + supplyId + 
					", orderCreateTime=" + orderCreateTime + 
					", receivePerson=" + receivePerson + 
					", receiveAddress=" + receiveAddress + 
					", receiveContactPhone=" + receiveContactPhone + 
					", dealStatus=" + dealStatus + 
					", exceptionType=" + exceptionType + 
					", operateTime=" + operateTime + 
					", operator=" + operator + 
				"]";
	}

	/**
	 * @return the payType
	 */
	public java.lang.Integer getPayType() {
		return payType;
	}

	/**
	 * @param payType the payType to set
	 */
	public void setPayType(java.lang.Integer payType) {
		this.payType = payType;
	}

	/**
	 * @return the payTypeName
	 */
	public java.lang.String getPayTypeName() {
		return payTypeName;
	}

	/**
	 * @param payTypeName the payTypeName to set
	 */
	public void setPayTypeName(java.lang.String payTypeName) {
		this.payTypeName = payTypeName;
	}
}

