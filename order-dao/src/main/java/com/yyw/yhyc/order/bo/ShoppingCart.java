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

public class ShoppingCart extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	
	  */
	private java.lang.Integer shoppingCartId;

	/**
	  *	企业ID
	  */
	private java.lang.Integer custId;

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
	  *	本公司编码
	  */
	private java.lang.String productCodeCompany;


	/**
	 *	SPU编码
	 */
	private java.lang.String spuCode;

	/**
	  *	商品名称
	  */
	private java.lang.String productName;

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

	/* 来源表示字段（0：来自进货单，1：来自极速下单) */
	private Integer fromWhere;

	/* 活动ID（商品所参加活动的活动id） */
	private Integer promotionId;

	/* 活动名称（商品所参加活动的活动名称） */
	private String promotionName;

/*
	数据库中t_shopping_cart中新增字段
	ALTER TABLE `t_shopping_cart`
	ADD COLUMN `promotion_id`  int(11) NULL DEFAULT NULL COMMENT '活动ID' AFTER `from_where`,
	ADD COLUMN `promotion_name`  varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '活动名称' AFTER `promotion_id`;

*/



	/**
	  *	
	  */
	public java.lang.Integer getShoppingCartId() 
	{
		return shoppingCartId;
	}
	
	/**
	  *	
	  */
	public void setShoppingCartId(java.lang.Integer shoppingCartId) 
	{
		this.shoppingCartId = shoppingCartId;
	}
	
	/**
	  *	企业ID
	  */
	public java.lang.Integer getCustId() 
	{
		return custId;
	}
	
	/**
	  *	企业ID
	  */
	public void setCustId(java.lang.Integer custId) 
	{
		this.custId = custId;
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

	public String getSpuCode() {
		return spuCode;
	}

	public void setSpuCode(String spuCode) {
		this.spuCode = spuCode;
	}


	public String getProductCodeCompany() {
		return productCodeCompany;
	}

	public void setProductCodeCompany(String productCodeCompany) {
		this.productCodeCompany = productCodeCompany;
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

	public Integer getFromWhere() {
		return fromWhere;
	}

	public void setFromWhere(Integer fromWhere) {
		this.fromWhere = fromWhere;
	}

	public Integer getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	@Override
	public String toString() {
		return "ShoppingCart{" +
				"shoppingCartId=" + shoppingCartId +
				", custId=" + custId +
				", skuId=" + skuId +
				", specification='" + specification + '\'' +
				", supplyId=" + supplyId +
				", productId=" + productId +
				", productCodeCompany='" + productCodeCompany + '\'' +
				", spuCode='" + spuCode + '\'' +
				", productName='" + productName + '\'' +
				", manufactures='" + manufactures + '\'' +
				", productPrice=" + productPrice +
				", productSettlementPrice=" + productSettlementPrice +
				", productCount=" + productCount +
				", remark='" + remark + '\'' +
				", createUser='" + createUser + '\'' +
				", createTime='" + createTime + '\'' +
				", updateUser='" + updateUser + '\'' +
				", updateTime='" + updateTime + '\'' +
				", fromWhere=" + fromWhere +
				", promotionId=" + promotionId +
				", promotionName=" + promotionName +
				"} ";
	}
}

