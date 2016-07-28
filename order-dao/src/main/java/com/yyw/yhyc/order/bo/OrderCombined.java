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
	  *	创建时间
	  */
	private java.lang.String createTime;

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
	  *	创建时间
	  */
	public java.lang.String getCreateTime() 
	{
		return createTime;
	}
	
	/**
	  *	创建时间
	  */
	public void setCreateTime(java.lang.String createTime) 
	{
		this.createTime = createTime;
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
					", createTime=" + createTime + 
					", remark=" + remark + 
					", payFlowId=" + payFlowId + 
					", payTime=" + payTime + 
				"]";
	}
}

