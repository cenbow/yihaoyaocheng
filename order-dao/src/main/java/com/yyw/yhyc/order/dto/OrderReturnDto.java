package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.OrderReturn;

import java.math.BigDecimal;

/**
 * Created by liqiang on 2016/8/9.
 */
public class OrderReturnDto extends OrderReturn {

    private Integer productId;              //商品id
    private String productName;             //商品名
    private BigDecimal productPrice;       //商品单价
    private String specification;          //商品规格
    private String manufactures;           //生产厂家
    private String imageUrl;                //商品图片
    private String orderLineNo;             //订单行号
    private String formOfDrug;              //剂型
    private String spuCode;                 //商品spu编码
    private String shortName;               //通用名
    private BigDecimal productAllMoney; //商品总的金额
    private String promotionName; //促销名称，给拒收或者补货订单导出展现用
    
    
    public BigDecimal getProductAllMoney() {
		return productAllMoney;
	}

	public void setProductAllMoney(BigDecimal productAllMoney) {
		this.productAllMoney = productAllMoney;
	}

	/**
	  *	供应商ID
	  */
	private java.lang.Integer supplyId;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getManufactures() {
        return manufactures;
    }

    public void setManufactures(String manufactures) {
        this.manufactures = manufactures;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOrderLineNo() {
        return orderLineNo;
    }

    public void setOrderLineNo(String orderLineNo) {
        this.orderLineNo = orderLineNo;
    }

    public String getFormOfDrug() {
        return formOfDrug;
    }

    public void setFormOfDrug(String formOfDrug) {
        this.formOfDrug = formOfDrug;
    }

    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public java.lang.Integer getSupplyId() {
		return supplyId;
	}

	public void setSupplyId(java.lang.Integer supplyId) {
		this.supplyId = supplyId;
	}
	
	

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	@Override
    public String toString() {
        return "OrderReturnDto{" +
                "orderReturn="+super.toString()+
                ", shortName="+shortName+
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", specification='" + specification + '\'' +
                ", manufactures='" + manufactures + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", orderLineNo='" + orderLineNo + '\'' +
                ", formOfDrug='" + formOfDrug + '\'' +
                ", spuCode='" + spuCode + '\'' +
                '}';
    }
}
