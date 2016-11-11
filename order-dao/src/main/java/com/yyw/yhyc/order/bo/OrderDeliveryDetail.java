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

public class OrderDeliveryDetail extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	
	  */
	private java.lang.Integer orderDeliveryDetailId;

	/**
	  *	订单行号
	  */
	private java.lang.String orderLineNo;

	/**
	  *	订单ID主键
	  */
	private java.lang.Integer orderId;

	/**
	  *	订单编号
	  */
	private java.lang.String flowId;

	/**
	  *	订单详情ID
	  */
	private java.lang.Integer orderDetailId;

	/**
	  *	发货批次号
	  */
	private java.lang.String batchNumber;

	/**
	  *	当前批次发货数量
	  */
	private java.lang.Integer deliveryProductCount;

	/**
	  *	发货状态 1 发货成功 0 发货失败
	  */
	private java.lang.Integer deliveryStatus;

	/**
	  *	导入文件url
	  */
	private java.lang.String importFileUrl;

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
	 * 确认收货数量
	 */
	private java.lang.Integer recieveCount;
	
	/**
	 * 有效期至
	 */
	private String validUntil;
	/**
	 * 可退还数量
     */
	private java.lang.Integer canReturnCount;
	/**
	  *	
	  */
	public java.lang.Integer getOrderDeliveryDetailId() 
	{
		return orderDeliveryDetailId;
	}
	
	/**
	  *	
	  */
	public void setOrderDeliveryDetailId(java.lang.Integer orderDeliveryDetailId) 
	{
		this.orderDeliveryDetailId = orderDeliveryDetailId;
	}
	
	/**
	  *	订单行号
	  */
	public java.lang.String getOrderLineNo() 
	{
		return orderLineNo;
	}
	
	/**
	  *	订单行号
	  */
	public void setOrderLineNo(java.lang.String orderLineNo) 
	{
		this.orderLineNo = orderLineNo;
	}
	
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
	  *	发货批次号
	  */
	public java.lang.String getBatchNumber() 
	{
		return batchNumber;
	}
	
	/**
	  *	发货批次号
	  */
	public void setBatchNumber(java.lang.String batchNumber) 
	{
		this.batchNumber = batchNumber;
	}
	
	/**
	  *	当前批次发货数量
	  */
	public java.lang.Integer getDeliveryProductCount() 
	{
		return deliveryProductCount;
	}
	
	/**
	  *	当前批次发货数量
	  */
	public void setDeliveryProductCount(java.lang.Integer deliveryProductCount) 
	{
		this.deliveryProductCount = deliveryProductCount;
	}
	
	/**
	  *	发货状态 1 发货成功 0 发货失败
	  */
	public java.lang.Integer getDeliveryStatus() 
	{
		return deliveryStatus;
	}
	
	/**
	  *	发货状态 1 发货成功 0 发货失败
	  */
	public void setDeliveryStatus(java.lang.Integer deliveryStatus) 
	{
		this.deliveryStatus = deliveryStatus;
	}
	
	/**
	  *	导入文件url
	  */
	public java.lang.String getImportFileUrl() 
	{
		return importFileUrl;
	}
	
	/**
	  *	导入文件url
	  */
	public void setImportFileUrl(java.lang.String importFileUrl) 
	{
		this.importFileUrl = importFileUrl;
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

	public Integer getRecieveCount() {
		return recieveCount;
	}

	public void setRecieveCount(Integer recieveCount) {
		this.recieveCount = recieveCount;
	}

	public Integer getCanReturnCount() {
		return canReturnCount;
	}

	public void setCanReturnCount(Integer canReturnCount) {
		this.canReturnCount = canReturnCount;
	}
	
	

	public String getValidUntil() {
		return validUntil;
	}

	public void setValidUntil(String validUntil) {
		this.validUntil = validUntil;
	}

	@Override
	public String toString() {
		return "OrderDeliveryDetail{" +
				"orderDeliveryDetailId=" + orderDeliveryDetailId +
				", orderLineNo='" + orderLineNo + '\'' +
				", orderId=" + orderId +
				", flowId='" + flowId + '\'' +
				", orderDetailId=" + orderDetailId +
				", batchNumber='" + batchNumber + '\'' +
				", deliveryProductCount=" + deliveryProductCount +
				", deliveryStatus=" + deliveryStatus +
				", importFileUrl='" + importFileUrl + '\'' +
				", remark='" + remark + '\'' +
				", createUser='" + createUser + '\'' +
				", createTime='" + createTime + '\'' +
				", updateUser='" + updateUser + '\'' +
				", updateTime='" + updateTime + '\'' +
				", recieveCount=" + recieveCount +
				", canReturnCount=" + canReturnCount +
				", validUntil=" + validUntil +
				'}';
	}
}

