/**
 * Created By: XI
 * Created On: 2016-7-28 17:34:55
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.bo.Model;

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
	  *	订单编号
	  */
	private java.lang.String flowId;

	/**
	  *	采购商名称
	  */
	private java.lang.String custName;

	/**
	  *	客户ID
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
	private java.lang.Integer payTypeId;

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
	  *	
	  */
	private java.lang.String createUser;

	/**
	  *	订单最后更新时间
	  */
	private java.lang.String updateTime;

	/**
	  *	
	  */
	private java.lang.String updateUser;

	/**
	  *	发票类型 1 增值税专用发票 2 增值税普通发票
	  */
	private java.lang.Integer billType;

	/**
	  *	延期收货日志（时间 第n次延期）
	  */
	private java.lang.String delayLog;

	/**
	  *	1,买家确认收货 2,系统自动确认收货
	  */
	private java.lang.Integer receiveType;

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
	  *	
	  */
	public java.lang.String getCreateUser() 
	{
		return createUser;
	}
	
	/**
	  *	
	  */
	public void setCreateUser(java.lang.String createUser) 
	{
		this.createUser = createUser;
	}
	
	/**
	  *	订单最后更新时间
	  */
	public java.lang.String getUpdateTime() 
	{
		return updateTime;
	}
	
	/**
	  *	订单最后更新时间
	  */
	public void setUpdateTime(java.lang.String updateTime) 
	{
		this.updateTime = updateTime;
	}
	
	/**
	  *	
	  */
	public java.lang.String getUpdateUser() 
	{
		return updateUser;
	}
	
	/**
	  *	
	  */
	public void setUpdateUser(java.lang.String updateUser) 
	{
		this.updateUser = updateUser;
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
	
	/**
	  *	延期收货日志（时间 第n次延期）
	  */
	public java.lang.String getDelayLog() 
	{
		return delayLog;
	}
	
	/**
	  *	延期收货日志（时间 第n次延期）
	  */
	public void setDelayLog(java.lang.String delayLog) 
	{
		this.delayLog = delayLog;
	}
	
	/**
	  *	1,买家确认收货 2,系统自动确认收货
	  */
	public java.lang.Integer getReceiveType() 
	{
		return receiveType;
	}
	
	/**
	  *	1,买家确认收货 2,系统自动确认收货
	  */
	public void setReceiveType(java.lang.Integer receiveType) 
	{
		this.receiveType = receiveType;
	}
	
	public String toString()
	{
		return "Order [" + 
					"orderId=" + orderId + 
					", flowId=" + flowId + 
					", custName=" + custName + 
					", custId=" + custId + 
					", supplyName=" + supplyName + 
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
					", createUser=" + createUser + 
					", updateTime=" + updateTime + 
					", updateUser=" + updateUser + 
					", billType=" + billType + 
					", delayLog=" + delayLog + 
					", receiveType=" + receiveType + 
				"]";
	}
}

