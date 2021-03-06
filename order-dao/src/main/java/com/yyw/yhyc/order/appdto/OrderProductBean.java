package com.yyw.yhyc.order.appdto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by huhaibing on 2016/9/8.
 */
public class OrderProductBean extends Product implements Serializable {

    public OrderProductBean() {
    }
    private Integer quantity;	//	购买数量
    private List<BatchBean> batchList;		//批次号列表
    private String batchNumber; //发货批次
    private BigDecimal productAllMoney; //商品金额=商品的数量*商品的价格
    private BigDecimal productShareMoney; //商品的均摊优惠金额
    private BigDecimal productReturnPay; //商品优惠后的金额

    public OrderProductBean(Integer quantity, List<BatchBean> batchList) {
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

    public String getBatchNumber() {
        return batchNumber;
    }

    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }

	public BigDecimal getProductAllMoney() {
		return productAllMoney;
	}

	public void setProductAllMoney(BigDecimal productAllMoney) {
		this.productAllMoney = productAllMoney;
	}

	public BigDecimal getProductShareMoney() {
		return productShareMoney;
	}

	public void setProductShareMoney(BigDecimal productShareMoney) {
		this.productShareMoney = productShareMoney;
	}

	public BigDecimal getProductReturnPay() {
		return productReturnPay;
	}

	public void setProductReturnPay(BigDecimal productReturnPay) {
		this.productReturnPay = productReturnPay;
	}
	
	
    
    
}
