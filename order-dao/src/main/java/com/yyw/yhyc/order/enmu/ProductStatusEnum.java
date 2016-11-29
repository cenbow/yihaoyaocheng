package com.yyw.yhyc.order.enmu;

/**
 * Created by lizhou on 2016/11/29
 * (买家用户所看到的)商品状态枚举值
 */
public enum ProductStatusEnum {

    Normal(0,"正常显示价格"),

    NotLogin(-1,"登陆后可见"),

    NotAddChannel(-2,"加入渠道后可见"),

    NotQualified(-3,"资质认证后可见"),

    ChannelNotCheck(-4,"渠道待审核"),

    Shortage(-5,"缺货"),

    //不显示价格的原因包含：
    // 1. 生产企业搜索出来的商品
    // 2.商品销售区域和买家购买区域不一致
    // 3.自己的商品
    // 4.搜不到该商品
    // 5.商品参与的活动已失效
    NotDisplayPrice(-6,"不显示价格"),

    OffTheShelf(-7,"已下架");


    /* 状态值 */
    private Integer status;

    /* 状态值说明 */
    private String statusDesc;

    ProductStatusEnum(Integer status,String statusDesc){
        this.status = status;
        this.statusDesc = statusDesc;
    }

    public static String getStatusDesc(Integer status) {
        for (ProductStatusEnum productStatusEnum : ProductStatusEnum.values()) {
            if (productStatusEnum.status.equals(status) )
                return productStatusEnum.statusDesc;
        }
        return null;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

}
