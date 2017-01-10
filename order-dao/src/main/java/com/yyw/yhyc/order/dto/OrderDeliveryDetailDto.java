package com.yyw.yhyc.order.dto;

import java.math.BigDecimal;

import com.yyw.yhyc.order.bo.OrderDeliveryDetail;

/**
 * Created by liqiang on 2016/7/29.
 */
public class OrderDeliveryDetailDto extends OrderDeliveryDetail {

    private String productCode;    //商品编码
    private String productName;    //商品名
    private String brandName;      //品名
    private String formOfDrug;     //剂型
    private String specification; //商品规格
    private String manufactures;  //生产厂家
    private Integer productCount; //购买单品数量
    private Integer custId;        //客户ID
    private Integer supplyId;     //供应商ID
    private  String returnType;      //退换货类型  1:退货\r\n            2:换货\r\n            3:补货 4 拒收‘,
    private String returnDesc;   //退换货说明
    private String userType;     //1、采购商2、供应商
    private String shortName;   //通用名
    private String spuCode; //spuCode
    private String partDelivery; //是否部分发货
    private BigDecimal cancelmMoney; //卖家发货是部分发货,且不补发货物,剩下的货物金额
    private String selectDeliveryAddressId; //选择的补货的发货地址id
    
    

    public String getSelectDeliveryAddressId() {
		return selectDeliveryAddressId;
	}

	public void setSelectDeliveryAddressId(String selectDeliveryAddressId) {
		this.selectDeliveryAddressId = selectDeliveryAddressId;
	}

	public String getPartDelivery() {
		return partDelivery;
	}

	public void setPartDelivery(String partDelivery) {
		this.partDelivery = partDelivery;
	}

	public BigDecimal getCancelmMoney() {
		return cancelmMoney;
	}

	public void setCancelmMoney(BigDecimal cancelmMoney) {
		this.cancelmMoney = cancelmMoney;
	}

	public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }
    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnDesc() {
        return returnDesc;
    }

    public void setReturnDesc(String returnDesc) {
        this.returnDesc = returnDesc;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getManufactures() {
        return manufactures;
    }

    public void setManufactures(String manufactures) {
        this.manufactures = manufactures;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getFormOfDrug() {
        return formOfDrug;
    }

    public void setFormOfDrug(String formOfDrug) {
        this.formOfDrug = formOfDrug;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
