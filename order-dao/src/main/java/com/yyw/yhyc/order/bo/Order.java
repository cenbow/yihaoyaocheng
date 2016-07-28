/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:48
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.order.bo.Model;

public class Order extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	订单ID主键
	  */
	private java.lang.Integer orderId;

	/**
	  *	订单号
	  */
	private java.lang.String flowId;

	/**
	  *	客户ID
	  */
	private java.lang.Integer custId;

	/**
	  *	供应商ID
	  */
	private java.lang.Integer supplyId;

	/**
	  *	取消原因
	  */
	private java.lang.String cancelResult;

	/**
	  *	订单优惠后金额
	  */
	private java.math.BigDecimal orgTotal;

	/**
	  *	运费金额
	  */
	private java.math.BigDecimal freight;

	/**
	  *	订单总金额
	  */
	private java.math.BigDecimal orderTotal;

	/**
	  *	最后支付金额
	  */
	private java.math.BigDecimal finalPay;

	/**
	  *	商品总数量
	  */
	private java.lang.Integer totalCount;

	/**
	  *	订单状态
	  */
	private java.lang.String orderStatus;

	/**
	  *	备注信息
	  */
	private java.lang.String remark;

	/**
	  *	买家留言
	  */
	private java.lang.String leaveMessage;

	/**
	  *	订单合并付款ID
	  */
	private java.lang.Integer orderCombinedId;

	/**
	  *	订单转账ID
	  */
	private java.lang.Integer orderTransferId;

	/**
	  *	卖家是否确认结算
	  */
	private java.lang.String confirmSettlement;

	/**
	  *	结算金额
	  */
	private java.math.BigDecimal settlementMoney;

	/**
	  *	优惠金额
	  */
	private java.math.BigDecimal preferentialMoney;

	/**
	  *	优惠金额备注
	  */
	private java.lang.String preferentialRemark;

	/**
	  *	支付状态
	  */
	private java.lang.String payStatus;

	/**
	  *	支付类型表ID
	  */
	private java.lang.String payTypeId;

	/**
	  *	订单生成时间
	  */
	private java.lang.String createTime;

	/**
	  *	支付时间
	  */
	private java.lang.String payTime;

	/**
	  *	取消时间
	  */
	private java.lang.String cancelTime;

	/**
	  *	发货时间
	  */
	private java.lang.String deliverTime;

	/**
	  *	收货时间
	  */
	private java.lang.String receiveTime;

	/**
	  *	结算时间
	  */
	private java.lang.String settlementTime;

	/**
	  *	订单最后更新时间
	  */
	private java.lang.String updateDate;

	/**
	  *	发票类型 1 增值税专用发票 2 增值税普通发票
	  */
	private java.lang.Integer billType;

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
	  *	订单号
	  */
	public java.lang.String getFlowId() 
	{
		return flowId;
	}
	
	/**
	  *	订单号
	  */
	public void setFlowId(java.lang.String flowId) 
	{
		this.flowId = flowId;
	}
	
	/**
	  *	客户ID
	  */
	public java.lang.Integer getCustId() 
	{
		return custId;
	}
	
	/**
	  *	客户ID
	  */
	public void setCustId(java.lang.Integer custId) 
	{
		this.custId = custId;
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
	  *	取消原因
	  */
	public java.lang.String getCancelResult() 
	{
		return cancelResult;
	}
	
	/**
	  *	取消原因
	  */
	public void setCancelResult(java.lang.String cancelResult) 
	{
		this.cancelResult = cancelResult;
	}
	
	/**
	  *	订单优惠后金额
	  */
	public java.math.BigDecimal getOrgTotal() 
	{
		return orgTotal;
	}
	
	/**
	  *	订单优惠后金额
	  */
	public void setOrgTotal(java.math.BigDecimal orgTotal) 
	{
		this.orgTotal = orgTotal;
	}
	
	/**
	  *	运费金额
	  */
	public java.math.BigDecimal getFreight() 
	{
		return freight;
	}
	
	/**
	  *	运费金额
	  */
	public void setFreight(java.math.BigDecimal freight) 
	{
		this.freight = freight;
	}
	
	/**
	  *	订单总金额
	  */
	public java.math.BigDecimal getOrderTotal() 
	{
		return orderTotal;
	}
	
	/**
	  *	订单总金额
	  */
	public void setOrderTotal(java.math.BigDecimal orderTotal) 
	{
		this.orderTotal = orderTotal;
	}
	
	/**
	  *	最后支付金额
	  */
	public java.math.BigDecimal getFinalPay() 
	{
		return finalPay;
	}
	
	/**
	  *	最后支付金额
	  */
	public void setFinalPay(java.math.BigDecimal finalPay) 
	{
		this.finalPay = finalPay;
	}
	
	/**
	  *	商品总数量
	  */
	public java.lang.Integer getTotalCount() 
	{
		return totalCount;
	}
	
	/**
	  *	商品总数量
	  */
	public void setTotalCount(java.lang.Integer totalCount) 
	{
		this.totalCount = totalCount;
	}
	
	/**
	  *	订单状态
	  */
	public java.lang.String getOrderStatus() 
	{
		return orderStatus;
	}
	
	/**
	  *	订单状态
	  */
	public void setOrderStatus(java.lang.String orderStatus) 
	{
		this.orderStatus = orderStatus;
	}
	
	/**
	  *	备注信息
	  */
	public java.lang.String getRemark() 
	{
		return remark;
	}
	
	/**
	  *	备注信息
	  */
	public void setRemark(java.lang.String remark) 
	{
		this.remark = remark;
	}
	
	/**
	  *	买家留言
	  */
	public java.lang.String getLeaveMessage() 
	{
		return leaveMessage;
	}
	
	/**
	  *	买家留言
	  */
	public void setLeaveMessage(java.lang.String leaveMessage) 
	{
		this.leaveMessage = leaveMessage;
	}
	
	/**
	  *	订单合并付款ID
	  */
	public java.lang.Integer getOrderCombinedId() 
	{
		return orderCombinedId;
	}
	
	/**
	  *	订单合并付款ID
	  */
	public void setOrderCombinedId(java.lang.Integer orderCombinedId) 
	{
		this.orderCombinedId = orderCombinedId;
	}
	
	/**
	  *	订单转账ID
	  */
	public java.lang.Integer getOrderTransferId() 
	{
		return orderTransferId;
	}
	
	/**
	  *	订单转账ID
	  */
	public void setOrderTransferId(java.lang.Integer orderTransferId) 
	{
		this.orderTransferId = orderTransferId;
	}
	
	/**
	  *	卖家是否确认结算
	  */
	public java.lang.String getConfirmSettlement() 
	{
		return confirmSettlement;
	}
	
	/**
	  *	卖家是否确认结算
	  */
	public void setConfirmSettlement(java.lang.String confirmSettlement) 
	{
		this.confirmSettlement = confirmSettlement;
	}
	
	/**
	  *	结算金额
	  */
	public java.math.BigDecimal getSettlementMoney() 
	{
		return settlementMoney;
	}
	
	/**
	  *	结算金额
	  */
	public void setSettlementMoney(java.math.BigDecimal settlementMoney) 
	{
		this.settlementMoney = settlementMoney;
	}
	
	/**
	  *	优惠金额
	  */
	public java.math.BigDecimal getPreferentialMoney() 
	{
		return preferentialMoney;
	}
	
	/**
	  *	优惠金额
	  */
	public void setPreferentialMoney(java.math.BigDecimal preferentialMoney) 
	{
		this.preferentialMoney = preferentialMoney;
	}
	
	/**
	  *	优惠金额备注
	  */
	public java.lang.String getPreferentialRemark() 
	{
		return preferentialRemark;
	}
	
	/**
	  *	优惠金额备注
	  */
	public void setPreferentialRemark(java.lang.String preferentialRemark) 
	{
		this.preferentialRemark = preferentialRemark;
	}
	
	/**
	  *	支付状态
	  */
	public java.lang.String getPayStatus() 
	{
		return payStatus;
	}
	
	/**
	  *	支付状态
	  */
	public void setPayStatus(java.lang.String payStatus) 
	{
		this.payStatus = payStatus;
	}
	
	/**
	  *	支付类型表ID
	  */
	public java.lang.String getPayTypeId() 
	{
		return payTypeId;
	}
	
	/**
	  *	支付类型表ID
	  */
	public void setPayTypeId(java.lang.String payTypeId) 
	{
		this.payTypeId = payTypeId;
	}
	
	/**
	  *	订单生成时间
	  */
	public java.lang.String getCreateTime() 
	{
		return createTime;
	}
	
	/**
	  *	订单生成时间
	  */
	public void setCreateTime(java.lang.String createTime) 
	{
		this.createTime = createTime;
	}
	
	/**
	  *	支付时间
	  */
	public java.lang.String getPayTime() 
	{
		return payTime;
	}
	
	/**
	  *	支付时间
	  */
	public void setPayTime(java.lang.String payTime) 
	{
		this.payTime = payTime;
	}
	
	/**
	  *	取消时间
	  */
	public java.lang.String getCancelTime() 
	{
		return cancelTime;
	}
	
	/**
	  *	取消时间
	  */
	public void setCancelTime(java.lang.String cancelTime) 
	{
		this.cancelTime = cancelTime;
	}
	
	/**
	  *	发货时间
	  */
	public java.lang.String getDeliverTime() 
	{
		return deliverTime;
	}
	
	/**
	  *	发货时间
	  */
	public void setDeliverTime(java.lang.String deliverTime) 
	{
		this.deliverTime = deliverTime;
	}
	
	/**
	  *	收货时间
	  */
	public java.lang.String getReceiveTime() 
	{
		return receiveTime;
	}
	
	/**
	  *	收货时间
	  */
	public void setReceiveTime(java.lang.String receiveTime) 
	{
		this.receiveTime = receiveTime;
	}
	
	/**
	  *	结算时间
	  */
	public java.lang.String getSettlementTime() 
	{
		return settlementTime;
	}
	
	/**
	  *	结算时间
	  */
	public void setSettlementTime(java.lang.String settlementTime) 
	{
		this.settlementTime = settlementTime;
	}
	
	/**
	  *	订单最后更新时间
	  */
	public java.lang.String getUpdateDate() 
	{
		return updateDate;
	}
	
	/**
	  *	订单最后更新时间
	  */
	public void setUpdateDate(java.lang.String updateDate) 
	{
		this.updateDate = updateDate;
	}
	
	/**
	  *	发票类型 1 增值税专用发票 2 增值税普通发票
	  */
	public java.lang.Integer getBillType() 
	{
		return billType;
	}
	
	/**
	  *	发票类型 1 增值税专用发票 2 增值税普通发票
	  */
	public void setBillType(java.lang.Integer billType) 
	{
		this.billType = billType;
	}
	
	public String toString()
	{
		return "Order [" + 
					"orderId=" + orderId + 
					", flowId=" + flowId + 
					", custId=" + custId + 
					", supplyId=" + supplyId + 
					", cancelResult=" + cancelResult + 
					", orgTotal=" + orgTotal + 
					", freight=" + freight + 
					", orderTotal=" + orderTotal + 
					", finalPay=" + finalPay + 
					", totalCount=" + totalCount + 
					", orderStatus=" + orderStatus + 
					", remark=" + remark + 
					", leaveMessage=" + leaveMessage + 
					", orderCombinedId=" + orderCombinedId + 
					", orderTransferId=" + orderTransferId + 
					", confirmSettlement=" + confirmSettlement + 
					", settlementMoney=" + settlementMoney + 
					", preferentialMoney=" + preferentialMoney + 
					", preferentialRemark=" + preferentialRemark + 
					", payStatus=" + payStatus + 
					", payTypeId=" + payTypeId + 
					", createTime=" + createTime + 
					", payTime=" + payTime + 
					", cancelTime=" + cancelTime + 
					", deliverTime=" + deliverTime + 
					", receiveTime=" + receiveTime + 
					", settlementTime=" + settlementTime + 
					", updateDate=" + updateDate + 
					", billType=" + billType + 
				"]";
	}
}

