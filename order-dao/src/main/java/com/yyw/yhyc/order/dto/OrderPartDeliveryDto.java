package com.yyw.yhyc.order.dto;

import java.math.BigDecimal;

/**
 * 订单部分发货的时候剩余的货物实体
 * @author wangkui01
 *
 */
public class OrderPartDeliveryDto {
	/**
	  *	订单ID主键
	  */
	private Integer orderId;

	/**
	  *	订单编号
	  */
	private String flowId;
	
	/**
	 * 订单的子单主键
	 */
	private Integer orderDetailId;
	/**
	 * 部分发货商品的code
	 */
	private String produceCode;
	
	private BigDecimal producePrice;
	
	/**
	 * 未发货的商品数量
	 */
	private Integer noDeliveryNum;
	
	/**
	 * 发货的商品数量
	 */
	private Integer sendDeliveryNum;
	
	
	

	public Integer getSendDeliveryNum() {
		return sendDeliveryNum;
	}

	public void setSendDeliveryNum(Integer sendDeliveryNum) {
		this.sendDeliveryNum = sendDeliveryNum;
	}

	public BigDecimal getProducePrice() {
		return producePrice;
	}

	public void setProducePrice(BigDecimal producePrice) {
		this.producePrice = producePrice;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public Integer getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(Integer orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public String getProduceCode() {
		return produceCode;
	}

	public void setProduceCode(String produceCode) {
		this.produceCode = produceCode;
	}

	public Integer getNoDeliveryNum() {
		return noDeliveryNum;
	}

	public void setNoDeliveryNum(Integer noDeliveryNum) {
		this.noDeliveryNum = noDeliveryNum;
	}

	@Override
	public String toString() {
		return "OrderPartDeliveryDto [orderId=" + orderId + ", flowId="
				+ flowId + ", orderDetailId=" + orderDetailId
				+ ", produceCode=" + produceCode + ", noDeliveryNum="
				+ noDeliveryNum + "]";
	}
 
	
	
	

}
