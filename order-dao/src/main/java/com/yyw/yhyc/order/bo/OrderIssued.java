/**
 * Created By: XI
 * Created On: 2016-9-10 10:28:42
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.bo.Model;

public class OrderIssued extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	订单下发ID主键
	  */
	private java.lang.Integer orderIssuedId;

	/**
	  *	订单编号
	  */
	private java.lang.String flowId;

	/**
	  *	供应商ID
	  */
	private java.lang.Integer supplyId;

	/**
	  *	供应商名称
	  */
	private java.lang.String supplyName;
	
	/**
	  *	下发状态 0：失败；1：成功
	  */
	private java.lang.String issuedStatus;

	/**
	  *	下发次数
	  */
	private java.lang.Integer issuedCount;

	/**
	  *	订单下发生成时间
	  */
	private java.lang.String createTime;

	/**
	  *	订单下发最后更新时间
	  */
	private java.lang.String updateTime;
	/**
	 * 有无客户关联关系 0无 1有
	 */
	private java.lang.Integer cusRelationship;
	/**
	 * 是否已扫描标识0否1是
	 */
	private java.lang.Integer isScan;
	/**
	  *	订单下发ID主键
	  */
	public java.lang.Integer getOrderIssuedId() 
	{
		return orderIssuedId;
	}
	
	/**
	  *	订单下发ID主键
	  */
	public void setOrderIssuedId(java.lang.Integer orderIssuedId) 
	{
		this.orderIssuedId = orderIssuedId;
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
	  *	下发状态 0：失败；1：成功
	  */
	public java.lang.String getIssuedStatus() 
	{
		return issuedStatus;
	}
	
	/**
	  *	下发状态 0：失败；1：成功
	  */
	public void setIssuedStatus(java.lang.String issuedStatus) 
	{
		this.issuedStatus = issuedStatus;
	}
	
	/**
	  *	下发次数
	  */
	public java.lang.Integer getIssuedCount() 
	{
		return issuedCount;
	}
	
	/**
	  *	下发次数
	  */
	public void setIssuedCount(java.lang.Integer issuedCount) 
	{
		this.issuedCount = issuedCount;
	}
	
	/**
	  *	订单下发生成时间
	  */
	public java.lang.String getCreateTime() 
	{
		return createTime;
	}
	
	/**
	  *	订单下发生成时间
	  */
	public void setCreateTime(java.lang.String createTime) 
	{
		this.createTime = createTime;
	}
	
	/**
	  *	订单下发最后更新时间
	  */
	public java.lang.String getUpdateTime() 
	{
		return updateTime;
	}
	
	/**
	  *	订单下发最后更新时间
	  */
	public void setUpdateTime(java.lang.String updateTime) 
	{
		this.updateTime = updateTime;
	}
	
	public String toString()
	{
		return "OrderIssued [" + 
					"orderIssuedId=" + orderIssuedId + 
					", flowId=" + flowId + 
					", supplyId=" + supplyId + 
					", issuedStatus=" + issuedStatus + 
					", issuedCount=" + issuedCount + 
					", createTime=" + createTime + 
					", updateTime=" + updateTime + 
					", cusRelationship=" + cusRelationship + 
					", isScan=" + isScan + 
				"]";
	}

	/**
	 * @return the cusRelationship
	 */
	public java.lang.Integer getCusRelationship() {
		return cusRelationship;
	}

	/**
	 * @param cusRelationship the cusRelationship to set
	 */
	public void setCusRelationship(java.lang.Integer cusRelationship) {
		this.cusRelationship = cusRelationship;
	}

	/**
	 * @return the isScan
	 */
	public java.lang.Integer getIsScan() {
		return isScan;
	}

	/**
	 * @param isScan the isScan to set
	 */
	public void setIsScan(java.lang.Integer isScan) {
		this.isScan = isScan;
	}

	/**
	 * @return the supplyName
	 */
	public java.lang.String getSupplyName() {
		return supplyName;
	}

	/**
	 * @param supplyName the supplyName to set
	 */
	public void setSupplyName(java.lang.String supplyName) {
		this.supplyName = supplyName;
	}
}

