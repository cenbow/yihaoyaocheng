package com.yyw.yhyc.order.appdto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by luweibin on 2016/9/6
 */
public class CartProductBean implements Serializable,Cloneable {
    private static final long serialVersionUID = -7384290110347695348L;
    private long shoppingCartId;//购物车ID
    private int quantity;//购买数量
    private int oldQuantity;//
    private boolean selectedProduct;//是否选中商品
    private long productId;//商品Id
    private String productPicUrl;//商品图片
    private String productName;//商品名称
    private String spec;//规格
    private String unit;
    private BigDecimal productPrice;//商品销售价格
    private BigDecimal recommendPrice;//
    private int stockCount;//商品库存
    private int baseCount;  //起售量
    private int stepCount;//递增数量
    private int  statusDesc;//商品状态描述
    private String factoryName;//生产厂商名称
    private long factoryId ;//生产厂商Id
    private String vendorName;//供应商名称
    private Integer vendorId;//供应商id
    private String spuCode;//商品SPU编码

    /* 促销商品活动信息 */
    private ProductPromotion productPromotion;

    /* 商品状态是否正常*/
    private boolean normalStatus ;

    /* 商品状态是不正常的原因*/
    private String unNormalStatusReason;

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

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public String getSpuCode() {
        return spuCode;
    }

    public void setSpuCode(String spuCode) {
        this.spuCode = spuCode;
    }

    public ProductPromotion getProductPromotion() {
        return productPromotion;
    }

    public void setProductPromotion(ProductPromotion productPromotion) {
        this.productPromotion = productPromotion;
    }

    public boolean isNormalStatus() {
        return normalStatus;
    }

    public void setNormalStatus(boolean normalStatus) {
        this.normalStatus = normalStatus;
    }

    public String getUnNormalStatusReason() {
        return unNormalStatusReason;
    }

    public void setUnNormalStatusReason(String unNormalStatusReason) {
        this.unNormalStatusReason = unNormalStatusReason;
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
                ", vendorId=" + vendorId +
                ", spuCode='" + spuCode + '\'' +
                ", productPromotion=" + productPromotion +
                ", normalStatus=" + normalStatus +
                ", unNormalStatusReason='" + unNormalStatusReason + '\'' +
                '}';
    }
}
