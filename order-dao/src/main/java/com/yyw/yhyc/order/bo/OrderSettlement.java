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

import com.yyw.yhyc.bo.Model;

import java.math.BigDecimal;

public class OrderSettlement extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	订单ID主键
	  */
	private java.lang.Integer orderSettlementId;

	/**
	  *	业务类型  1 采购应付 或  销售应收 ;  2  退款应付  或 退款应收
	  */
	private java.lang.Integer businessType;

	/**
	  *	订单ID
	  */
	private java.lang.Integer orderId;

	/**
	  *	订单编号
	  */
	private java.lang.String flowId;

	/**
	  *	客户ID
	  */
	private java.lang.Integer custId;

	/**
	  *	采购商名称
	  */
	private java.lang.String custName;

	/**
	  *	供应商ID
	  */
	private java.lang.Integer supplyId;

	/**
	  *	供应商名称
	  */
	private java.lang.String supplyName;

	/**
	  *	卖家是否确认结算 0 未结算 1已结算
	  */
	private java.lang.String confirmSettlement;

	/**
	  *	结算金额
	  */
	private java.math.BigDecimal settlementMoney;

	/**
	  *	支付类型表ID
	  */
	private java.lang.Integer payTypeId;

	/**
	  *	结算时间
	  */
	private java.lang.String settlementTime;

	/**
	  *	
	  */
	private java.lang.String orderTime;

	/**
	  *	备注信息
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
	 * 订单退款结算金额
     */
	private java.math.BigDecimal refunSettlementMoney;
	/**
	 * 差异金额
     */
	private java.math.BigDecimal differentMoney;
	/**
	 * 省
     */
	private java.lang.String province;
	/**
	 * 市
     */
	private java.lang.String city;
	/**
	 * 区域
     */
	private java.lang.String area;
	/**
	  *	订单ID主键
	  */
	public java.lang.Integer getOrderSettlementId() 
	{
		return orderSettlementId;
	}
	
	/**
	  *	订单ID主键
	  */
	public void setOrderSettlementId(java.lang.Integer orderSettlementId) 
	{
		this.orderSettlementId = orderSettlementId;
	}
	
	/**
	  *	业务类型  1 采购应付 或  销售应收 ;  2  退款应付  或 退款应收
	  */
	public java.lang.Integer getBusinessType() 
	{
		return businessType;
	}
	
	/**
	  *	业务类型  1 采购应付 或  销售应收 ;  2  退款应付  或 退款应收
	  */
	public void setBusinessType(java.lang.Integer businessType) 
	{
		this.businessType = businessType;
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
	public java.lang.String getOrderTime() 
	{
		return orderTime;
	}
	
	/**
	  *	
	  */
	public void setOrderTime(java.lang.String orderTime) 
	{
		this.orderTime = orderTime;
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

	public BigDecimal getRefunSettlementMoney() {
		return refunSettlementMoney;
	}

	public void setRefunSettlementMoney(BigDecimal refunSettlementMoney) {
		this.refunSettlementMoney = refunSettlementMoney;
	}

	public BigDecimal getDifferentMoney() {
		return differentMoney;
	}

	public void setDifferentMoney(BigDecimal differentMoney) {
		this.differentMoney = differentMoney;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Override
	public String toString() {
		return "OrderSettlement{" +
				"orderSettlementId=" + orderSettlementId +
				", businessType=" + businessType +
				", orderId=" + orderId +
				", flowId='" + flowId + '\'' +
				", custId=" + custId +
				", custName='" + custName + '\'' +
				", supplyId=" + supplyId +
				", supplyName='" + supplyName + '\'' +
				", confirmSettlement='" + confirmSettlement + '\'' +
				", settlementMoney=" + settlementMoney +
				", payTypeId=" + payTypeId +
				", settlementTime='" + settlementTime + '\'' +
				", orderTime='" + orderTime + '\'' +
				", remark='" + remark + '\'' +
				", createUser='" + createUser + '\'' +
				", createTime='" + createTime + '\'' +
				", updateUser='" + updateUser + '\'' +
				", updateTime='" + updateTime + '\'' +
				", refunSettlementMoney=" + refunSettlementMoney +
				", differentMoney=" + differentMoney +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", area='" + area + '\'' +
				'}';
	}
}

