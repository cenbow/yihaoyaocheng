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

import com.yyw.yhyc.order.bo.Model;

public class OrderCombined extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	订单合并ID
	  */
	private java.lang.Integer orderCombinedId;

	/**
	  *	支付类型表ID
	  */
	private java.lang.Integer payTypeId;

	/**
	  *	客户ID
	  */
	private java.lang.Integer custId;

	/**
	  *	下单人
	  */
	private java.lang.String custName;

	/**
	  *	合并订单数
	  */
	private java.lang.Integer combinedNumber;

	/**
	  *	应付总价
	  */
	private java.math.BigDecimal copeTotal;

	/**
	  *	实付总价
	  */
	private java.math.BigDecimal pocketTotal;

	/**
	  *	运费
	  */
	private java.math.BigDecimal freightPrice;

	/**
	  *	备注
	  */
	private java.lang.String remark;

	/**
	  *	在线支付流水号
	  */
	private java.lang.String payFlowId;

	/**
	  *	
	  */
	private java.lang.String payTime;

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
	  *	订单合并ID
	  */
	public java.lang.Integer getOrderCombinedId() 
	{
		return orderCombinedId;
	}
	
	/**
	  *	订单合并ID
	  */
	public void setOrderCombinedId(java.lang.Integer orderCombinedId) 
	{
		this.orderCombinedId = orderCombinedId;
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
	  *	下单人
	  */
	public java.lang.String getCustName() 
	{
		return custName;
	}
	
	/**
	  *	下单人
	  */
	public void setCustName(java.lang.String custName) 
	{
		this.custName = custName;
	}
	
	/**
	  *	合并订单数
	  */
	public java.lang.Integer getCombinedNumber() 
	{
		return combinedNumber;
	}
	
	/**
	  *	合并订单数
	  */
	public void setCombinedNumber(java.lang.Integer combinedNumber) 
	{
		this.combinedNumber = combinedNumber;
	}
	
	/**
	  *	应付总价
	  */
	public java.math.BigDecimal getCopeTotal() 
	{
		return copeTotal;
	}
	
	/**
	  *	应付总价
	  */
	public void setCopeTotal(java.math.BigDecimal copeTotal) 
	{
		this.copeTotal = copeTotal;
	}
	
	/**
	  *	实付总价
	  */
	public java.math.BigDecimal getPocketTotal() 
	{
		return pocketTotal;
	}
	
	/**
	  *	实付总价
	  */
	public void setPocketTotal(java.math.BigDecimal pocketTotal) 
	{
		this.pocketTotal = pocketTotal;
	}
	
	/**
	  *	运费
	  */
	public java.math.BigDecimal getFreightPrice() 
	{
		return freightPrice;
	}
	
	/**
	  *	运费
	  */
	public void setFreightPrice(java.math.BigDecimal freightPrice) 
	{
		this.freightPrice = freightPrice;
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
	  *	在线支付流水号
	  */
	public java.lang.String getPayFlowId() 
	{
		return payFlowId;
	}
	
	/**
	  *	在线支付流水号
	  */
	public void setPayFlowId(java.lang.String payFlowId) 
	{
		this.payFlowId = payFlowId;
	}
	
	/**
	  *	
	  */
	public java.lang.String getPayTime() 
	{
		return payTime;
	}
	
	/**
	  *	
	  */
	public void setPayTime(java.lang.String payTime) 
	{
		this.payTime = payTime;
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
		return "OrderCombined [" + 
					"orderCombinedId=" + orderCombinedId + 
					", payTypeId=" + payTypeId + 
					", custId=" + custId + 
					", custName=" + custName + 
					", combinedNumber=" + combinedNumber + 
					", copeTotal=" + copeTotal + 
					", pocketTotal=" + pocketTotal + 
					", freightPrice=" + freightPrice + 
					", remark=" + remark + 
					", payFlowId=" + payFlowId + 
					", payTime=" + payTime + 
					", createUser=" + createUser + 
					", createTime=" + createTime + 
					", updateUser=" + updateUser + 
					", updateTime=" + updateTime + 
				"]";
	}
}

