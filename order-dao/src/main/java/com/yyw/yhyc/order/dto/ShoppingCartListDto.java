package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lizhou on 2016/8/2
 */
public class ShoppingCartListDto implements Serializable{

    private static final long serialVersionUID = 4186154289532275138L;

    /* 买家企业信息 */
    private UsermanageEnterprise buyer;

    /* 供应商企业信息 */
    private UsermanageEnterprise seller;

    /* 买家在供应商中买的商品(购物车条目集合) */
    private List<ShoppingCartDto> shoppingCartDtoList;

    public UsermanageEnterprise getBuyer() {
        return buyer;
    }

    public void setBuyer(UsermanageEnterprise buyer) {
        this.buyer = buyer;
    }

    public UsermanageEnterprise getSeller() {
        return seller;
    }

    public void setSeller(UsermanageEnterprise seller) {
        this.seller = seller;
    }

    public List<ShoppingCartDto> getShoppingCartDtoList() {
        return shoppingCartDtoList;
    }

    public void setShoppingCartDtoList(List<ShoppingCartDto> shoppingCartDtoList) {
        this.shoppingCartDtoList = shoppingCartDtoList;
    }

    @Override
    public String toString() {
        return "ShoppingCartListDto{" +
                "buyer=" + buyer +
                ", seller=" + seller +
                ", shoppingCartDtoList=" + shoppingCartDtoList +
                '}';
    }
}
