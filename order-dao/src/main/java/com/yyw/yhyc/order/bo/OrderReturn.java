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

public class OrderReturn extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	
	  */
	private java.lang.Integer returnId;

	/**
	  *	客户ID
	  */
	private java.lang.Integer custId;

	/**
	  *	订单ID
	  */
	private java.lang.Integer orderId;

	/**
	  *	订单详情ID
	  */
	private java.lang.Integer orderDetailId;

	/**
	  *	退换数量
	  */
	private java.lang.Integer returnCount;

	/**
	  *	退换金额
	  */
	private java.math.BigDecimal returnPay;

	/**
	  *	1:退货 2:换货 3:补货 4:拒收*/
	private java.lang.String returnType;

	/**
	  *	退换状态  1,未处理。2，已处理
	  */
	private java.lang.String returnStatus;

	/**
	  *	退换货描述
	  */
	private java.lang.String returnDesc;

	/**
	  *	处理人
	  */
	private java.lang.String dealPerson;

	/**
	  *	处理结果
	  */
	private java.lang.String dealResult;

	/**
	  *	退换货时间
	  */
	private java.lang.String returnTime;

	/**
	  *	
	  */
	private java.lang.String remark;

	/**
	  *	订单编号
	  */
	private java.lang.String flowId;

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
	 *	异常订单编码
	 */
	private java.lang.String exceptionOrderId;

	/**
	 *	订单异常编号
	 */
	private java.lang.Integer orderDeliveryDetailId;

	/**
	 *	发货批次
	 */
	private String batchNumber;

	public Integer getOrderDeliveryDetailId() {
		return orderDeliveryDetailId;
	}

	public void setOrderDeliveryDetailId(Integer orderDeliveryDetailId) {
		this.orderDeliveryDetailId = orderDeliveryDetailId;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getExceptionOrderId() {
		return exceptionOrderId;
	}

	public void setExceptionOrderId(String exceptionOrderId) {
		this.exceptionOrderId = exceptionOrderId;
	}

	/**
	  *	
	  */
	public java.lang.Integer getReturnId() 
	{
		return returnId;
	}
	
	/**
	  *	
	  */
	public void setReturnId(java.lang.Integer returnId) 
	{
		this.returnId = returnId;
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
	  *	退换数量
	  */
	public java.lang.Integer getReturnCount() 
	{
		return returnCount;
	}
	
	/**
	  *	退换数量
	  */
	public void setReturnCount(java.lang.Integer returnCount) 
	{
		this.returnCount = returnCount;
	}
	
	/**
	  *	退换金额
	  */
	public java.math.BigDecimal getReturnPay() 
	{
		return returnPay;
	}
	
	/**
	  *	退换金额
	  */
	public void setReturnPay(java.math.BigDecimal returnPay) 
	{
		this.returnPay = returnPay;
	}
	
	/**
	  *	1:退货
            2:换货
            3:补货
	 		4:拒收
	  */
	public java.lang.String getReturnType() 
	{
		return returnType;
	}
	
	/**
	  *	1:退货
        2:换货
        3:补货
	 	4:拒收
	  */
	public void setReturnType(java.lang.String returnType) 
	{
		this.returnType = returnType;
	}
	
	/**
	  *	退换状态
	  */
	public java.lang.String getReturnStatus() 
	{
		return returnStatus;
	}
	
	/**
	  *	退换状态
	  */
	public void setReturnStatus(java.lang.String returnStatus) 
	{
		this.returnStatus = returnStatus;
	}
	
	/**
	  *	退换货描述
	  */
	public java.lang.String getReturnDesc() 
	{
		return returnDesc;
	}
	
	/**
	  *	退换货描述
	  */
	public void setReturnDesc(java.lang.String returnDesc) 
	{
		this.returnDesc = returnDesc;
	}
	
	/**
	  *	处理人
	  */
	public java.lang.String getDealPerson() 
	{
		return dealPerson;
	}
	
	/**
	  *	处理人
	  */
	public void setDealPerson(java.lang.String dealPerson) 
	{
		this.dealPerson = dealPerson;
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
	  *	退换货时间
	  */
	public java.lang.String getReturnTime() 
	{
		return returnTime;
	}
	
	/**
	  *	退换货时间
	  */
	public void setReturnTime(java.lang.String returnTime) 
	{
		this.returnTime = returnTime;
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
		return "OrderReturn [" + 
					"returnId=" + returnId + 
					", custId=" + custId + 
					", orderId=" + orderId + 
					", orderDetailId=" + orderDetailId + 
					", returnCount=" + returnCount + 
					", returnPay=" + returnPay + 
					", returnType=" + returnType + 
					", returnStatus=" + returnStatus + 
					", returnDesc=" + returnDesc + 
					", dealPerson=" + dealPerson + 
					", dealResult=" + dealResult + 
					", returnTime=" + returnTime + 
					", remark=" + remark + 
					", flowId=" + flowId + 
					", createUser=" + createUser + 
					", createTime=" + createTime + 
					", updateUser=" + updateUser + 
					", updateTime=" + updateTime + 
				"]";
	}
}

