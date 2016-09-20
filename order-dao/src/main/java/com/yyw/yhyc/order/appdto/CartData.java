package com.yyw.yhyc.order.appdto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by luweibin on 2016/9/7.
 */
public class CartData implements Serializable {
    private int selectedTypeCount;                  //商品品种数
    private int selectedTotalQuantity;              //选中的商品总个数
    private int totalCount;                         //购物车的商品品种总数
    private BigDecimal selectedTotalPrice;          //选中的商品总价格
    private boolean selectedAll;                    //是否全部选中
    private List<CartGroupData> shopCartList;       //进货单列表

    public int getSelectedTypeCount() {
        return selectedTypeCount;
    }

    public void setSelectedTypeCount(int selectedTypeCount) {
        this.selectedTypeCount = selectedTypeCount;
    }

    public int getSelectedTotalQuantity() {
        return selectedTotalQuantity;
    }

    public void setSelectedTotalQuantity(int selectedTotalQuantity) {
        this.selectedTotalQuantity = selectedTotalQuantity;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getSelectedTotalPrice() {
        return selectedTotalPrice;
    }

    public void setSelectedTotalPrice(BigDecimal selectedTotalPrice) {
        this.selectedTotalPrice = selectedTotalPrice;
    }

    public boolean isSelectedAll() {
        return selectedAll;
    }

    public void setSelectedAll(boolean selectedAll) {
        this.selectedAll = selectedAll;
    }

    public List<CartGroupData> getShopCartList() {
        return shopCartList;
    }

    public void setShopCartList(List<CartGroupData> shopCartList) {
        this.shopCartList = shopCartList;
    }

    @Override
    public String toString() {
        return "CartData{" +
                "selectedTypeCount=" + selectedTypeCount +
                ", selectedTotalQuantity=" + selectedTotalQuantity +
                ", totalCount=" + totalCount +
                ", selectedTotalPrice=" + selectedTotalPrice +
                ", selectedAll=" + selectedAll +
                ", shopCartList=" + shopCartList +
                '}';
    }
}
