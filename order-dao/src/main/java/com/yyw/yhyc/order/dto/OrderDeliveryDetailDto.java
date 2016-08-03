package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.OrderDeliveryDetail;

/**
 * Created by liqiang on 2016/7/29.
 */
public class OrderDeliveryDetailDto extends OrderDeliveryDetail {

    private String productCode;    //商品编码
    private String productName;    //商品名称
    private String brandName;      //品名
    private String formOfDrug;     //剂型
    private String specification; //商品规格
    private String manufactures;  //生产厂家
    private Integer productCount; //购买单品数量
    private Integer custId;        //客户ID
    private Integer supplyId;     //供应商ID

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
}
