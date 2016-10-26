package com.yyw.yhyc.order.dto;

import java.io.Serializable;

public class OrderIssuedExceptionDto implements Serializable{

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
	  *	订单状态
	  */
	private java.lang.String orderStatus;
	/**
	  *	订单状态名称
	  */
	private java.lang.String orderStatusName;

	/**
	  *	订单生成时间
	  */
	private java.lang.String orderCreateTime;
	private String createBeginTime;    //下单开始时间
	private String createEndTime;      //下单结束时间
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
	  *	处理状态 0：处理中；1：待处理；2：已完成
	  */
	private java.lang.String dealStatusName;
	/**
	 * 支付类型 
	 */
	private java.lang.Integer payType;
	/**
	 * 支付类型名称
	 */
	private java.lang.String payTypeName;
	/**
	  *	异常类型（0下发超时未返回，1无关联企业用户，2下发返回错误，3下发失败）
	  */
	private java.lang.Integer exceptionType;
	/**
	  *	异常类型（0下发超时未返回，1无关联企业用户，2下发返回错误，3下发失败）
	  */
	private java.lang.String exceptionTypeName;
	public java.lang.String getFlowId() {
		return flowId;
	}
	public void setFlowId(java.lang.String flowId) {
		this.flowId = flowId;
	}
	public java.lang.String getCustName() {
		return custName;
	}
	public void setCustName(java.lang.String custName) {
		this.custName = custName;
	}
	public java.lang.Integer getCustId() {
		return custId;
	}
	public void setCustId(java.lang.Integer custId) {
		this.custId = custId;
	}
	public java.lang.String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(java.lang.String supplyName) {
		this.supplyName = supplyName;
	}
	public java.lang.Integer getSupplyId() {
		return supplyId;
	}
	public void setSupplyId(java.lang.Integer supplyId) {
		this.supplyId = supplyId;
	}
	public java.lang.String getOrderStatusName() {
		return orderStatusName;
	}
	public void setOrderStatusName(java.lang.String orderStatusName) {
		this.orderStatusName = orderStatusName;
	}
	public java.lang.String getOrderCreateTime() {
		return orderCreateTime;
	}
	public void setOrderCreateTime(java.lang.String orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}
	/**
	 * @return the receivePerson
	 */
	public java.lang.String getReceivePerson() {
		return receivePerson;
	}
	/**
	 * @param receivePerson the receivePerson to set
	 */
	public void setReceivePerson(java.lang.String receivePerson) {
		this.receivePerson = receivePerson;
	}
	/**
	 * @return the receiveAddress
	 */
	public java.lang.String getReceiveAddress() {
		return receiveAddress;
	}
	/**
	 * @param receiveAddress the receiveAddress to set
	 */
	public void setReceiveAddress(java.lang.String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	/**
	 * @return the receiveContactPhone
	 */
	public java.lang.String getReceiveContactPhone() {
		return receiveContactPhone;
	}
	/**
	 * @param receiveContactPhone the receiveContactPhone to set
	 */
	public void setReceiveContactPhone(java.lang.String receiveContactPhone) {
		this.receiveContactPhone = receiveContactPhone;
	}
	/**
	 * @return the dealStatus
	 */
	public java.lang.Integer getDealStatus() {
		return dealStatus;
	}
	/**
	 * @param dealStatus the dealStatus to set
	 */
	public void setDealStatus(java.lang.Integer dealStatus) {
		this.dealStatus = dealStatus;
	}
	/**
	 * @return the dealStatusName
	 */
	public java.lang.String getDealStatusName() {
		return dealStatusName;
	}
	/**
	 * @param dealStatusName the dealStatusName to set
	 */
	public void setDealStatusName(java.lang.String dealStatusName) {
		this.dealStatusName = dealStatusName;
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
	/**
	 * @return the exceptionType
	 */
	public java.lang.Integer getExceptionType() {
		return exceptionType;
	}
	/**
	 * @param exceptionType the exceptionType to set
	 */
	public void setExceptionType(java.lang.Integer exceptionType) {
		this.exceptionType = exceptionType;
	}
	/**
	 * @return the exceptionTypeName
	 */
	public java.lang.String getExceptionTypeName() {
		return exceptionTypeName;
	}
	/**
	 * @param exceptionTypeName the exceptionTypeName to set
	 */
	public void setExceptionTypeName(java.lang.String exceptionTypeName) {
		this.exceptionTypeName = exceptionTypeName;
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
	 * @return the createBeginTime
	 */
	public String getCreateBeginTime() {
		return createBeginTime;
	}
	/**
	 * @param createBeginTime the createBeginTime to set
	 */
	public void setCreateBeginTime(String createBeginTime) {
		this.createBeginTime = createBeginTime;
	}
	/**
	 * @return the createEndTime
	 */
	public String getCreateEndTime() {
		return createEndTime;
	}
	/**
	 * @param createEndTime the createEndTime to set
	 */
	public void setCreateEndTime(String createEndTime) {
		this.createEndTime = createEndTime;
	}
	/**
	 * @return the orderStatus
	 */
	public java.lang.String getOrderStatus() {
		return orderStatus;
	}
	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(java.lang.String orderStatus) {
		this.orderStatus = orderStatus;
	}
 

}
