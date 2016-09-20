package com.yyw.yhyc.order.appdto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huhaibing on 2016/9/8.
 */
public class OrdeProductBean extends Product implements Serializable {

    public OrdeProductBean() {
    }
    private Integer quantity;	//	购买数量
    private List<BatchBean> batchList;		//批次号列表

    public OrdeProductBean(Integer quantity, List<BatchBean> batchList) {
        this.quantity = quantity;
        this.batchList = batchList;
    }

    public List<BatchBean> getBatchList() {
        return batchList;
    }

    public void setBatchList(List<BatchBean> batchList) {
        this.batchList = batchList;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
