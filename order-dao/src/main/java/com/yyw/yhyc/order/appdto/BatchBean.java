package com.yyw.yhyc.order.appdto;

/**
 * Created by huhaibing on 2016/9/8.
 */
public class BatchBean {

    private String productId;           //商品ID
    private String batchId;             //批次号Id
    private Integer buyNumber;          //购买数量

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Integer getBuyNumber() {
        return buyNumber;
    }

    public void setBuyNumber(Integer buyNumber) {
        this.buyNumber = buyNumber;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
