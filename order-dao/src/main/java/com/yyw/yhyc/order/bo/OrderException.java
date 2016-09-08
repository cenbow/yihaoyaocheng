/**
 * Created By: XI
 * Created On: 2016-8-8 14:53:12
 *
 * Amendment History:
 *
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.bo.Model;

public class OrderException extends Model{

	/**
	 *
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 *
	 */
	private java.lang.Integer exceptionId;

	/**
	 *	1:退货
	 2:换货
	 3:补货 4: 拒收
	 */
	private java.lang.String returnType;

	/**
	 *	异常订单编码
	 */
	private java.lang.String exceptionOrderId;

	/**
	 *	原订单ID主键
	 */
	private java.lang.Integer orderId;

	/**
	 *	原订单编号
	 */
	private java.lang.String flowId;

	/**
	 *	异常订单的状态 1 待确认 2 退款中 3 已完成 4 已关闭
	 */
	private java.lang.String orderStatus;

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
	 *	备注(确认/关闭说明)
	 */
	private java.lang.String remark;

	/**
	 *	原订单金额
	 */
	private java.math.BigDecimal orderMoneyTotal;

	/**
	 *	异常订单金额
	 */
	private java.math.BigDecimal orderMoney;

	/**
	 *
	 */
	private java.lang.String createUser;

	/**
	 *
	 */
	private java.lang.String createTime;

	/**
	 *
	 */
	private java.lang.String updateUser;

	/**
	 *
	 */
	private java.lang.String updateTime;

	/**
	 *	订单生成时间
	 */
	private java.lang.String orderCreateTime;

	/**
	 *	退换货描述(拒收说明)
	 */
	private java.lang.String returnDesc;

	/**
	 *	发货时间
	 */
	private java.lang.String deliverTime;

	/**
	 *	收货时间
	 */
	private java.lang.String receiveTime;

	/**
	 * 买家发货时间（换货）
	 */
	private java.lang.String buyerDeliverTime;

	/**
	 * 卖家收货时间（换货）
	 */
	private java.lang.String sellerReceiveTime;

	/**
	 * 审核时间
	 */
	private java.lang.String reviewTime;


	/**
	 *
	 */
	public java.lang.Integer getExceptionId()
	{
		return exceptionId;
	}

	/**
	 *
	 */
	public void setExceptionId(java.lang.Integer exceptionId)
	{
		this.exceptionId = exceptionId;
	}

	/**
	 *	1:退货
	 2:换货
	 3:补货 4: 拒收
	 */
	public java.lang.String getReturnType()
	{
		return returnType;
	}

	/**
	 *	1:退货
	 2:换货
	 3:补货 4: 拒收
	 */
	public void setReturnType(java.lang.String returnType)
	{
		this.returnType = returnType;
	}

	/**
	 *	异常订单编码
	 */
	public java.lang.String getExceptionOrderId()
	{
		return exceptionOrderId;
	}

	/**
	 *	异常订单编码
	 */
	public void setExceptionOrderId(java.lang.String exceptionOrderId)
	{
		this.exceptionOrderId = exceptionOrderId;
	}

	/**
	 *	原订单ID主键
	 */
	public java.lang.Integer getOrderId()
	{
		return orderId;
	}

	/**
	 *	原订单ID主键
	 */
	public void setOrderId(java.lang.Integer orderId)
	{
		this.orderId = orderId;
	}

	/**
	 *	原订单编号
	 */
	public java.lang.String getFlowId()
	{
		return flowId;
	}

	/**
	 *	原订单编号
	 */
	public void setFlowId(java.lang.String flowId)
	{
		this.flowId = flowId;
	}

	/**
	 *	异常订单的状态 1 待确认 2 退款中 3 已完成 4 已关闭
	 */
	public java.lang.String getOrderStatus()
	{
		return orderStatus;
	}

	/**
	 *	异常订单的状态 1 待确认 2 退款中 3 已完成 4 已关闭
	 */
	public void setOrderStatus(java.lang.String orderStatus)
	{
		this.orderStatus = orderStatus;
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
	 *	备注(确认/关闭说明)
	 */
	public java.lang.String getRemark()
	{
		return remark;
	}

	/**
	 *	备注(确认/关闭说明)
	 */
	public void setRemark(java.lang.String remark)
	{
		this.remark = remark;
	}

	/**
	 *	原订单金额
	 */
	public java.math.BigDecimal getOrderMoneyTotal()
	{
		return orderMoneyTotal;
	}

	/**
	 *	原订单金额
	 */
	public void setOrderMoneyTotal(java.math.BigDecimal orderMoneyTotal)
	{
		this.orderMoneyTotal = orderMoneyTotal;
	}

	/**
	 *	异常订单金额
	 */
	public java.math.BigDecimal getOrderMoney()
	{
		return orderMoney;
	}

	/**
	 *	异常订单金额
	 */
	public void setOrderMoney(java.math.BigDecimal orderMoney)
	{
		this.orderMoney = orderMoney;
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
	 *
	 */
	public java.lang.String getCreateTime()
	{
		return createTime;
	}

	/**
	 *
	 */
	public void setCreateTime(java.lang.String createTime)
	{
		this.createTime = createTime;
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
	 *
	 */
	public java.lang.String getUpdateTime()
	{
		return updateTime;
	}

	/**
	 *
	 */
	public void setUpdateTime(java.lang.String updateTime)
	{
		this.updateTime = updateTime;
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
	 *	退换货描述(拒收说明)
	 */
	public java.lang.String getReturnDesc()
	{
		return returnDesc;
	}

	/**
	 *	退换货描述(拒收说明)
	 */
	public void setReturnDesc(java.lang.String returnDesc)
	{
		this.returnDesc = returnDesc;
	}

	public String getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(String deliverTime) {
		this.deliverTime = deliverTime;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getBuyerDeliverTime() {
		return buyerDeliverTime;
	}

	public void setBuyerDeliverTime(String buyerDeliverTime) {
		this.buyerDeliverTime = buyerDeliverTime;
	}

	public String getSellerReceiveTime() {
		return sellerReceiveTime;
	}

	public void setSellerReceiveTime(String sellerReceiveTime) {
		this.sellerReceiveTime = sellerReceiveTime;
	}

	public String getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(String reviewTime) {
		this.reviewTime = reviewTime;
	}

	public String toString()
	{
		return "OrderException [" +
				"exceptionId=" + exceptionId +
				", returnType=" + returnType +
				", exceptionOrderId=" + exceptionOrderId +
				", orderId=" + orderId +
				", flowId=" + flowId +
				", orderStatus=" + orderStatus +
				", custName=" + custName +
				", custId=" + custId +
				", supplyName=" + supplyName +
				", supplyId=" + supplyId +
				", remark=" + remark +
				", orderMoneyTotal=" + orderMoneyTotal +
				", orderMoney=" + orderMoney +
				", createUser=" + createUser +
				", createTime=" + createTime +
				", updateUser=" + updateUser +
				", updateTime=" + updateTime +
				", orderCreateTime=" + orderCreateTime +
				", returnDesc=" + returnDesc +
				", deliverTime=" + deliverTime +
				", receiveTime=" + receiveTime +
				", buyerDeliverTime=" + buyerDeliverTime +
				", sellerReceiveTime=" + sellerReceiveTime +
				", reviewTime=" + reviewTime +
				"]";
	}
}

