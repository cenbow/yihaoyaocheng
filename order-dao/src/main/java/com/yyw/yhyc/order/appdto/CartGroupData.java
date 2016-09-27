package com.yyw.yhyc.order.appdto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by luweibin on 2016/9/6.
 */
public class CartGroupData implements Serializable,Cloneable{
    private long supplyId;
    private String supplyName;
    private boolean checkGroup;//是否选中该供应商所有商品
    private BigDecimal productTotalPrice;
    private List<CartProductBean> products;
    private BigDecimal minSalePrice;//订单起售金额

    public long getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(long supplyId) {
        this.supplyId = supplyId;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public boolean isCheckGroup() {
        return checkGroup;
    }

    public void setCheckGroup(boolean checkGroup) {
        this.checkGroup = checkGroup;
    }

    public BigDecimal getProductTotalPrice() {
        return productTotalPrice;
    }

    public void setProductTotalPrice(BigDecimal productTotalPrice) {
        this.productTotalPrice = productTotalPrice;
    }

    public List<CartProductBean> getProducts() {
        return products;
    }

    public void setProducts(List<CartProductBean> products) {
        this.products = products;
    }

    public BigDecimal getMinSalePrice() {
        return minSalePrice;
    }

    public void setMinSalePrice(BigDecimal minSalePrice) {
        this.minSalePrice = minSalePrice;
    }

    @Override
    public String toString() {
        return "CartGroupData{" +
                "supplyId=" + supplyId +
                ", supplyName='" + supplyName + '\'' +
                ", checkGroup=" + checkGroup +
                ", productTotalPrice=" + productTotalPrice +
                ", products=" + products +
                ", minSalePrice=" + minSalePrice +
                '}';
    }
}
