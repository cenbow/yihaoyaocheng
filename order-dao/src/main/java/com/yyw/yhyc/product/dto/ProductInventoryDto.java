package com.yyw.yhyc.product.dto;

import com.yyw.yhyc.product.bo.ProductInventory;

import java.util.List;

/**
 * Created by liqiang on 2016/8/29.
 */
public class ProductInventoryDto extends ProductInventory {
    private String productcodeCompany;     //本公司产品编码
    private String productName;             //商品名
    private String shortName;               //通用名称
    private String spec;                     //规格
    private String factoryName;             //生产企业
    private Integer minimumPacking;        //最小拆零包装
    private String beginUpdateTime;        //修改开始时间
    private String endUpdateIime;          //修改结束时间
    private String warningStatus;          //预警状态1、缺货2、库存预警
    private List<ProductInventory> productInventoryList;


    public Integer getMinimumPacking() {
        return minimumPacking;
    }

    public void setMinimumPacking(Integer minimumPacking) {
        this.minimumPacking = minimumPacking;
    }

    public String getProductcodeCompany() {
        return productcodeCompany;
    }

    public void setProductcodeCompany(String productcodeCompany) {
        this.productcodeCompany = productcodeCompany;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public String getBeginUpdateTime() {
        return beginUpdateTime;
    }

    public void setBeginUpdateTime(String beginUpdateTime) {
        this.beginUpdateTime = beginUpdateTime;
    }

    public String getEndUpdateIime() {
        return endUpdateIime;
    }

    public void setEndUpdateIime(String endUpdateIime) {
        this.endUpdateIime = endUpdateIime;
    }

    public String getWarningStatus() {
        return warningStatus;
    }

    public void setWarningStatus(String warningStatus) {
        this.warningStatus = warningStatus;
    }

    public List<ProductInventory> getProductInventoryList() {
        return productInventoryList;
    }

    public void setProductInventoryList(List<ProductInventory> productInventoryList) {
        this.productInventoryList = productInventoryList;
    }


    @Override
    public String toString() {
        return "ProductInventoryDto{" +
                "productcodeCompany='" + productcodeCompany + '\'' +
                ", productName='" + productName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", spec='" + spec + '\'' +
                ", factoryName='" + factoryName + '\'' +
                ", minimumPacking=" + minimumPacking +
                ", beginUpdateTime='" + beginUpdateTime + '\'' +
                ", endUpdateIime='" + endUpdateIime + '\'' +
                ", warningStatus='" + warningStatus + '\'' +
                ", productInventoryList=" + productInventoryList +
                '}';
    }
}
