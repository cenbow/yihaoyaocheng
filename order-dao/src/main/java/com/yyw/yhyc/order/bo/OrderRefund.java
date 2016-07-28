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

import com.yyw.yhyc.order.bo.Model;

public class OrderRefund extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	退款ID
	  */
	private java.lang.Integer refundId;

	/**
	  *	订单ID
	  */
	private java.lang.Integer orderId;

	/**
	  *	订单编号
	  */
	private java.lang.String flowId;

	/**
	  *	退款人
	  */
	private java.lang.Integer custId;

	/**
	  *	处理人
	  */
	private java.lang.Integer supplyId;

	/**
	  *	退款原因
	  */
	private java.lang.String refundType;

	/**
	  *	退款金额
	  */
	private java.math.BigDecimal refundSum;

	/**
	  *	退款运费
	  */
	private java.math.BigDecimal refundFreight;

	/**
	  *	退款说明
	  */
	private java.lang.String refundDesc;

	/**
	  *	退款凭证
	  */
	private java.lang.String refundUrl;

	/**
	  *	退款时间
	  */
	private java.lang.String refundDate;

	/**
	  *	处理结果
	  */
	private java.lang.String dealResult;

	/**
	  *	退款状态
	  */
	private java.lang.String refundStatus;

	/**
	  *	备注
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
	  *	退款ID
	  */
	public java.lang.Integer getRefundId() 
	{
		return refundId;
	}
	
	/**
	  *	退款ID
	  */
	public void setRefundId(java.lang.Integer refundId) 
	{
		this.refundId = refundId;
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
	  *	退款人
	  */
	public java.lang.Integer getCustId() 
	{
		return custId;
	}
	
	/**
	  *	退款人
	  */
	public void setCustId(java.lang.Integer custId) 
	{
		this.custId = custId;
	}
	
	/**
	  *	处理人
	  */
	public java.lang.Integer getSupplyId() 
	{
		return supplyId;
	}
	
	/**
	  *	处理人
	  */
	public void setSupplyId(java.lang.Integer supplyId) 
	{
		this.supplyId = supplyId;
	}
	
	/**
	  *	退款原因
	  */
	public java.lang.String getRefundType() 
	{
		return refundType;
	}
	
	/**
	  *	退款原因
	  */
	public void setRefundType(java.lang.String refundType) 
	{
		this.refundType = refundType;
	}
	
	/**
	  *	退款金额
	  */
	public java.math.BigDecimal getRefundSum() 
	{
		return refundSum;
	}
	
	/**
	  *	退款金额
	  */
	public void setRefundSum(java.math.BigDecimal refundSum) 
	{
		this.refundSum = refundSum;
	}
	
	/**
	  *	退款运费
	  */
	public java.math.BigDecimal getRefundFreight() 
	{
		return refundFreight;
	}
	
	/**
	  *	退款运费
	  */
	public void setRefundFreight(java.math.BigDecimal refundFreight) 
	{
		this.refundFreight = refundFreight;
	}
	
	/**
	  *	退款说明
	  */
	public java.lang.String getRefundDesc() 
	{
		return refundDesc;
	}
	
	/**
	  *	退款说明
	  */
	public void setRefundDesc(java.lang.String refundDesc) 
	{
		this.refundDesc = refundDesc;
	}
	
	/**
	  *	退款凭证
	  */
	public java.lang.String getRefundUrl() 
	{
		return refundUrl;
	}
	
	/**
	  *	退款凭证
	  */
	public void setRefundUrl(java.lang.String refundUrl) 
	{
		this.refundUrl = refundUrl;
	}
	
	/**
	  *	退款时间
	  */
	public java.lang.String getRefundDate() 
	{
		return refundDate;
	}
	
	/**
	  *	退款时间
	  */
	public void setRefundDate(java.lang.String refundDate) 
	{
		this.refundDate = refundDate;
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
	  *	退款状态
	  */
	public java.lang.String getRefundStatus() 
	{
		return refundStatus;
	}
	
	/**
	  *	退款状态
	  */
	public void setRefundStatus(java.lang.String refundStatus) 
	{
		this.refundStatus = refundStatus;
	}
	
	/**
	  *	备注
	  */
	public java.lang.String getRemark() 
	{
		return remark;
	}
	
	/**
	  *	备注
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
		return "OrderRefund [" + 
					"refundId=" + refundId + 
					", orderId=" + orderId + 
					", flowId=" + flowId + 
					", custId=" + custId + 
					", supplyId=" + supplyId + 
					", refundType=" + refundType + 
					", refundSum=" + refundSum + 
					", refundFreight=" + refundFreight + 
					", refundDesc=" + refundDesc + 
					", refundUrl=" + refundUrl + 
					", refundDate=" + refundDate + 
					", dealResult=" + dealResult + 
					", refundStatus=" + refundStatus + 
					", remark=" + remark + 
					", createUser=" + createUser + 
					", createTime=" + createTime + 
					", updateUser=" + updateUser + 
					", updateTime=" + updateTime + 
				"]";
	}
}

