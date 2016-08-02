/**
 * Created By: XI
 * Created On: 2016-7-28 17:34:56
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.bo.Model;

public class OrderTrace extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	
	  */
	private java.lang.Integer traceId;

	/**
	  *	订单ID
	  */
	private java.lang.Integer orderId;

	/**
	  *	日志订单节点说明
	  */
	private java.lang.String nodeName;

	/**
	  *	
	  */
	private java.lang.String startNodeDate;

	/**
	  *	
	  */
	private java.lang.String endNodeDate;

	/**
	  *	处理人
	  */
	private java.lang.String dealStaff;

	/**
	  *	处理结果
	  */
	private java.lang.String dealResult;

	/**
	  *	记录日期
	  */
	private java.lang.String recordDate;

	/**
	  *	记录人
	  */
	private java.lang.String recordStaff;

	/**
	  *	
	  */
	private java.lang.String orderStatus;

	/**
	  *	平台支付返回信息
	  */
	private java.lang.String paymentPlatforReturn;

	/**
	  *	
	  */
	private java.lang.String remark;

	/**
	  *	记录创建者
	  */
	private java.lang.String createUser;

	/**
	  *	记录生成时间
	  */
	private java.lang.String createTime;

	/**
	  *	记录更新者
	  */
	private java.lang.String updateUser;

	/**
	  *	记录更新时间
	  */
	private java.lang.String updateTime;

	/**
	  *	
	  */
	public java.lang.Integer getTraceId() 
	{
		return traceId;
	}
	
	/**
	  *	
	  */
	public void setTraceId(java.lang.Integer traceId) 
	{
		this.traceId = traceId;
	}
	
	/**
	  *	订单ID
	  */
	public java.lang.Integer getOrderId() 
	{
		return orderId;
	}
	
	/**
	  *	订单ID
	  */
	public void setOrderId(java.lang.Integer orderId) 
	{
		this.orderId = orderId;
	}
	
	/**
	  *	日志订单节点说明
	  */
	public java.lang.String getNodeName() 
	{
		return nodeName;
	}
	
	/**
	  *	日志订单节点说明
	  */
	public void setNodeName(java.lang.String nodeName) 
	{
		this.nodeName = nodeName;
	}
	
	/**
	  *	
	  */
	public java.lang.String getStartNodeDate() 
	{
		return startNodeDate;
	}
	
	/**
	  *	
	  */
	public void setStartNodeDate(java.lang.String startNodeDate) 
	{
		this.startNodeDate = startNodeDate;
	}
	
	/**
	  *	
	  */
	public java.lang.String getEndNodeDate() 
	{
		return endNodeDate;
	}
	
	/**
	  *	
	  */
	public void setEndNodeDate(java.lang.String endNodeDate) 
	{
		this.endNodeDate = endNodeDate;
	}
	
	/**
	  *	处理人
	  */
	public java.lang.String getDealStaff() 
	{
		return dealStaff;
	}
	
	/**
	  *	处理人
	  */
	public void setDealStaff(java.lang.String dealStaff) 
	{
		this.dealStaff = dealStaff;
	}
	
	/**
	  *	处理结果
	  */
	public java.lang.String getDealResult() 
	{
		return dealResult;
	}
	
	/**
	  *	处理结果
	  */
	public void setDealResult(java.lang.String dealResult) 
	{
		this.dealResult = dealResult;
	}
	
	/**
	  *	记录日期
	  */
	public java.lang.String getRecordDate() 
	{
		return recordDate;
	}
	
	/**
	  *	记录日期
	  */
	public void setRecordDate(java.lang.String recordDate) 
	{
		this.recordDate = recordDate;
	}
	
	/**
	  *	记录人
	  */
	public java.lang.String getRecordStaff() 
	{
		return recordStaff;
	}
	
	/**
	  *	记录人
	  */
	public void setRecordStaff(java.lang.String recordStaff) 
	{
		this.recordStaff = recordStaff;
	}
	
	/**
	  *	
	  */
	public java.lang.String getOrderStatus() 
	{
		return orderStatus;
	}
	
	/**
	  *	
	  */
	public void setOrderStatus(java.lang.String orderStatus) 
	{
		this.orderStatus = orderStatus;
	}
	
	/**
	  *	平台支付返回信息
	  */
	public java.lang.String getPaymentPlatforReturn() 
	{
		return paymentPlatforReturn;
	}
	
	/**
	  *	平台支付返回信息
	  */
	public void setPaymentPlatforReturn(java.lang.String paymentPlatforReturn) 
	{
		this.paymentPlatforReturn = paymentPlatforReturn;
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
	
	/**
	  *	记录创建者
	  */
	public java.lang.String getCreateUser() 
	{
		return createUser;
	}
	
	/**
	  *	记录创建者
	  */
	public void setCreateUser(java.lang.String createUser) 
	{
		this.createUser = createUser;
	}
	
	/**
	  *	记录生成时间
	  */
	public java.lang.String getCreateTime() 
	{
		return createTime;
	}
	
	/**
	  *	记录生成时间
	  */
	public void setCreateTime(java.lang.String createTime) 
	{
		this.createTime = createTime;
	}
	
	/**
	  *	记录更新者
	  */
	public java.lang.String getUpdateUser() 
	{
		return updateUser;
	}
	
	/**
	  *	记录更新者
	  */
	public void setUpdateUser(java.lang.String updateUser) 
	{
		this.updateUser = updateUser;
	}
	
	/**
	  *	记录更新时间
	  */
	public java.lang.String getUpdateTime() 
	{
		return updateTime;
	}
	
	/**
	  *	记录更新时间
	  */
	public void setUpdateTime(java.lang.String updateTime) 
	{
		this.updateTime = updateTime;
	}
	
	public String toString()
	{
		return "OrderTrace [" + 
					"traceId=" + traceId + 
					", orderId=" + orderId + 
					", nodeName=" + nodeName + 
					", startNodeDate=" + startNodeDate + 
					", endNodeDate=" + endNodeDate + 
					", dealStaff=" + dealStaff + 
					", dealResult=" + dealResult + 
					", recordDate=" + recordDate + 
					", recordStaff=" + recordStaff + 
					", orderStatus=" + orderStatus + 
					", paymentPlatforReturn=" + paymentPlatforReturn + 
					", remark=" + remark + 
					", createUser=" + createUser + 
					", createTime=" + createTime + 
					", updateUser=" + updateUser + 
					", updateTime=" + updateTime + 
				"]";
	}
}

