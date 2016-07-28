/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:50
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.order.bo.Model;

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
	  *	商品编码
	  */
	private java.lang.String productCode;

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
	
	public String toString()
	{
		return "ShoppingCart [" + 
					"shoppingCartId=" + shoppingCartId + 
					", custId=" + custId + 
					", skuId=" + skuId + 
					", specification=" + specification + 
					", supplyId=" + supplyId + 
					", productId=" + productId + 
					", productCode=" + productCode + 
					", productName=" + productName + 
					", manufactures=" + manufactures + 
					", productPrice=" + productPrice + 
					", productSettlementPrice=" + productSettlementPrice + 
					", productCount=" + productCount + 
					", remark=" + remark + 
				"]";
	}
}

