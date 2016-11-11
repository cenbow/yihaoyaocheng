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

public class OrderDetail extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	
	  */
	private java.lang.Integer orderDetailId;

	/**
	  *	订单ID主键
	  */
	private java.lang.Integer orderId;

	/**
	  *	商品主数据ID
	  */
	private java.lang.Integer skuId;

	/**
	  *	商品规格
	  */
	private java.lang.String specification;

	/**
	  *	供应商ID
	  */
	private java.lang.Integer supplyId;

	/**
	  *	商品ID
	  */
	private java.lang.Integer productId;

	/**
	  *	品名
	  */
	private java.lang.String brandName;

	/**
	  *	剂型
	  */
	private java.lang.String formOfDrug;

	/**
	  *	商品名称
	  */
	private java.lang.String productName;

	/**
	  *	商品编码
	  */
	private java.lang.String productCode;

	/**
	  *	
	  */
	private java.lang.Integer manufacturesId;

	/**
	  *	生产厂家
	  */
	private java.lang.String manufactures;

	/**
	  *	商品单价
	  */
	private java.math.BigDecimal productPrice;

	/**
	  *	实际结算价，正常情况下与销售单价一致
	  */
	private java.math.BigDecimal productSettlementPrice;

	/**
	  *	购买单品数量
	  */
	private java.lang.Integer productCount;

	/**
	  *	确认收到药品数量
	  */
	private java.lang.Integer recieveCount;

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
	 * 卖家发货是部分发货,且不补发货物,剩下商品的数量
	 */
	private Integer cancelProduceNum;


	/* 通用名 (二期)*/
	private java.lang.String shortName;

	/* 商品的SPU编码 (三期)*/
	private java.lang.String spuCode;

	/**
	 *	活动ID
	 */
	private int  promotionId;

	/*
	* 活动名称
     */
	private java.lang.String promotionName;
	
	
	


	public Integer getCancelProduceNum() {
		return cancelProduceNum;
	}

	public void setCancelProduceNum(Integer cancelProduceNum) {
		this.cancelProduceNum = cancelProduceNum;
	}

	/**
	  *	
	  */
	public java.lang.Integer getOrderDetailId() 
	{
		return orderDetailId;
	}
	
	/**
	  *	
	  */
	public void setOrderDetailId(java.lang.Integer orderDetailId) 
	{
		this.orderDetailId = orderDetailId;
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
	  *	商品主数据ID
	  */
	public java.lang.Integer getSkuId() 
	{
		return skuId;
	}
	
	/**
	  *	商品主数据ID
	  */
	public void setSkuId(java.lang.Integer skuId) 
	{
		this.skuId = skuId;
	}
	
	/**
	  *	商品规格
	  */
	public java.lang.String getSpecification() 
	{
		return specification;
	}
	
	/**
	  *	商品规格
	  */
	public void setSpecification(java.lang.String specification) 
	{
		this.specification = specification;
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
	  *	商品ID
	  */
	public java.lang.Integer getProductId() 
	{
		return productId;
	}
	
	/**
	  *	商品ID
	  */
	public void setProductId(java.lang.Integer productId) 
	{
		this.productId = productId;
	}
	
	/**
	  *	品名
	  */
	public java.lang.String getBrandName() 
	{
		return brandName;
	}
	
	/**
	  *	品名
	  */
	public void setBrandName(java.lang.String brandName) 
	{
		this.brandName = brandName;
	}
	
	/**
	  *	剂型
	  */
	public java.lang.String getFormOfDrug() 
	{
		return formOfDrug;
	}
	
	/**
	  *	剂型
	  */
	public void setFormOfDrug(java.lang.String formOfDrug) 
	{
		this.formOfDrug = formOfDrug;
	}
	
	/**
	  *	商品名称
	  */
	public java.lang.String getProductName() 
	{
		return productName;
	}
	
	/**
	  *	商品名称
	  */
	public void setProductName(java.lang.String productName) 
	{
		this.productName = productName;
	}
	
	/**
	  *	商品编码
	  */
	public java.lang.String getProductCode() 
	{
		return productCode;
	}
	
	/**
	  *	商品编码
	  */
	public void setProductCode(java.lang.String productCode) 
	{
		this.productCode = productCode;
	}
	
	/**
	  *	
	  */
	public java.lang.Integer getManufacturesId() 
	{
		return manufacturesId;
	}
	
	/**
	  *	
	  */
	public void setManufacturesId(java.lang.Integer manufacturesId) 
	{
		this.manufacturesId = manufacturesId;
	}
	
	/**
	  *	生产厂家
	  */
	public java.lang.String getManufactures() 
	{
		return manufactures;
	}
	
	/**
	  *	生产厂家
	  */
	public void setManufactures(java.lang.String manufactures) 
	{
		this.manufactures = manufactures;
	}
	
	/**
	  *	商品单价
	  */
	public java.math.BigDecimal getProductPrice() 
	{
		return productPrice;
	}
	
	/**
	  *	商品单价
	  */
	public void setProductPrice(java.math.BigDecimal productPrice) 
	{
		this.productPrice = productPrice;
	}
	
	/**
	  *	实际结算价，正常情况下与销售单价一致
	  */
	public java.math.BigDecimal getProductSettlementPrice() 
	{
		return productSettlementPrice;
	}
	
	/**
	  *	实际结算价，正常情况下与销售单价一致
	  */
	public void setProductSettlementPrice(java.math.BigDecimal productSettlementPrice) 
	{
		this.productSettlementPrice = productSettlementPrice;
	}
	
	/**
	  *	购买单品数量
	  */
	public java.lang.Integer getProductCount() 
	{
		return productCount;
	}
	
	/**
	  *	购买单品数量
	  */
	public void setProductCount(java.lang.Integer productCount) 
	{
		this.productCount = productCount;
	}
	
	/**
	  *	确认收到药品数量
	  */
	public java.lang.Integer getRecieveCount() 
	{
		return recieveCount;
	}
	
	/**
	  *	确认收到药品数量
	  */
	public void setRecieveCount(java.lang.Integer recieveCount) 
	{
		this.recieveCount = recieveCount;
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

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getSpuCode() {
		return spuCode;
	}

	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}

	public int getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(int promotionId) {
		this.promotionId = promotionId;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String toString()
	{
		return "OrderDetail [" + 
					"orderDetailId=" + orderDetailId + 
					", orderId=" + orderId + 
					", skuId=" + skuId + 
					", specification=" + specification + 
					", supplyId=" + supplyId + 
					", productId=" + productId + 
					", brandName=" + brandName + 
					", formOfDrug=" + formOfDrug + 
					", productName=" + productName + 
					", productCode=" + productCode + 
					", manufacturesId=" + manufacturesId + 
					", manufactures=" + manufactures + 
					", productPrice=" + productPrice + 
					", productSettlementPrice=" + productSettlementPrice + 
					", productCount=" + productCount + 
					", recieveCount=" + recieveCount + 
					", remark=" + remark + 
					", createUser=" + createUser + 
					", createTime=" + createTime + 
					", updateUser=" + updateUser + 
					", updateTime=" + updateTime +
					", shortName=" + shortName +
					", spuCode=" + spuCode +
			    	", promotionId=" + promotionId +
				    ", promotionName=" + promotionName +
				    ", cancelProduceNum=" + cancelProduceNum +
				"]";
	}


}

