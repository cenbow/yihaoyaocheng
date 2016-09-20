package com.yyw.yhyc.order.appdto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by luweibin on 2016/9/6.
 */
public class CartProductBean implements Serializable,Cloneable {
    private long shoppingCartId;
    private int quantity;
    private int oldQuantity;
    private boolean selectedProduct;
    private long productId;
    private String productPicUrl;
    private String productName;
    private String spec;
    private String unit;
    private BigDecimal productPrice;
    private BigDecimal recommendPrice;
    private int stockCount;
    private int baseCount;
    private int stepCount;
    private int  statusDesc;
    private String factoryName;
    private long factoryId ;
    private String vendorName;

    public long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(boolean selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductPicUrl() {
        return productPicUrl;
    }

    public void setProductPicUrl(String productPicUrl) {
        this.productPicUrl = productPicUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public BigDecimal getRecommendPrice() {
        return recommendPrice;
    }

    public void setRecommendPrice(BigDecimal recommendPrice) {
        this.recommendPrice = recommendPrice;
    }

    public int getStockCount() {
        return stockCount;
    }

    public void setStockCount(int stockCount) {
        this.stockCount = stockCount;
    }

    public int getBaseCount() {
        return baseCount;
    }

    public void setBaseCount(int baseCount) {
        this.baseCount = baseCount;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public int getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(int statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public long getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(long factoryId) {
        this.factoryId = factoryId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public int getOldQuantity() {
        return oldQuantity;
    }

    public void setOldQuantity(int oldQuantity) {
        this.oldQuantity = oldQuantity;
    }

    @Override
    public String toString() {
        return "CartProductBean{" +
                "shoppingCartId=" + shoppingCartId +
                ", quantity=" + quantity +
                ", oldQuantity=" + oldQuantity +
                ", selectedProduct=" + selectedProduct +
                ", productId=" + productId +
                ", productPicUrl='" + productPicUrl + '\'' +
                ", productName='" + productName + '\'' +
                ", spec='" + spec + '\'' +
                ", unit='" + unit + '\'' +
                ", productPrice=" + productPrice +
                ", recommendPrice=" + recommendPrice +
                ", stockCount=" + stockCount +
                ", baseCount=" + baseCount +
                ", stepCount=" + stepCount +
                ", statusDesc=" + statusDesc +
                ", factoryName='" + factoryName + '\'' +
                ", factoryId=" + factoryId +
                ", vendorName='" + vendorName + '\'' +
                '}';
    }
}
