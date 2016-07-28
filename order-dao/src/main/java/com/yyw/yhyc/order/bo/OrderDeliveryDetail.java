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

public class OrderDeliveryDetail extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	
	  */
	private java.lang.Integer orderDeliveryDetailId;

	/**
	  *	订单ID主键
	  */
	private java.lang.Integer orderId;

	/**
	  *	订单详情ID
	  */
	private java.lang.Integer orderDetailId;

	/**
	  *	发货批次号
	  */
	private java.lang.String batchNumber;

	/**
	  *	购买单品数量
	  */
	private java.lang.Integer deliveryProductCount;

	/**
	  *	
	  */
	private java.lang.String remark;

	/**
	  *	
	  */
	public java.lang.Integer getOrderDeliveryDetailId() 
	{
		return orderDeliveryDetailId;
	}
	
	/**
	  *	
	  */
	public void setOrderDeliveryDetailId(java.lang.Integer orderDeliveryDetailId) 
	{
		this.orderDeliveryDetailId = orderDeliveryDetailId;
	}
	
	/**
	  *	订单ID主键
	  */
	public java.lang.Integer getOrderId() 
	{
		return orderId;
	}
	
	/**
	  *	订单ID主键
	  */
	public void setOrderId(java.lang.Integer orderId) 
	{
		this.orderId = orderId;
	}
	
	/**
	  *	订单详情ID
	  */
	public java.lang.Integer getOrderDetailId() 
	{
		return orderDetailId;
	}
	
	/**
	  *	订单详情ID
	  */
	public void setOrderDetailId(java.lang.Integer orderDetailId) 
	{
		this.orderDetailId = orderDetailId;
	}
	
	/**
	  *	发货批次号
	  */
	public java.lang.String getBatchNumber() 
	{
		return batchNumber;
	}
	
	/**
	  *	发货批次号
	  */
	public void setBatchNumber(java.lang.String batchNumber) 
	{
		this.batchNumber = batchNumber;
	}
	
	/**
	  *	购买单品数量
	  */
	public java.lang.Integer getDeliveryProductCount() 
	{
		return deliveryProductCount;
	}
	
	/**
	  *	购买单品数量
	  */
	public void setDeliveryProductCount(java.lang.Integer deliveryProductCount) 
	{
		this.deliveryProductCount = deliveryProductCount;
	}
	
	/**
	  *	
	  */
	public java.lang.String getRemark() 
	{
		return remark;
	}
	
	/**
	  *	
	  */
	public void setRemark(java.lang.String remark) 
	{
		this.remark = remark;
	}
	
	public String toString()
	{
		return "OrderDeliveryDetail [" + 
					"orderDeliveryDetailId=" + orderDeliveryDetailId + 
					", orderId=" + orderId + 
					", orderDetailId=" + orderDetailId + 
					", batchNumber=" + batchNumber + 
					", deliveryProductCount=" + deliveryProductCount + 
					", remark=" + remark + 
				"]";
	}
}

