package com.yyw.yhyc.order.dto;

public class OrderLogDto {
	private Integer orderId;
	private String nodeName;
	private String orderStatus;
	private String paymentPlatforReturn; //平台支付返回信息
	private String remark; //备注
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getPaymentPlatforReturn() {
		return paymentPlatforReturn;
	}
	public void setPaymentPlatforReturn(String paymentPlatforReturn) {
		this.paymentPlatforReturn = paymentPlatforReturn;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
