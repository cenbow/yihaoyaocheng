package com.yyw.yhyc.order.bo;

import java.util.List;

import com.yyw.yhyc.bo.Model;
import com.yyw.yhyc.product.dto.ProductBeanDto;

/**
 * Created by liqiang on 2016/9/9.
 */
public class ManufacturerOrder extends Model {

    private static final long	serialVersionUID	= 1L;

    /**
     *	订单编号
     */
    private String flowId;

    /**
     *	供应商ID
     */
    private Integer supplyId;

    /**
     *	供应商名称
     */
    private String supplyName;

    /**
     *	发货时间
     */
    private String deliverTime;

    /**
     *	预计送达时间
     */
    private String deliveryDate;

    /**
     *	配送方式 1 自有物流 2 第三方物流
     */
    private Integer deliveryMethod;

    /**
     *	订单状态  1、发货 0、取消
     */
    private String orderStatus;
    
    
    /**
     * 所发的商品的信息
     */
    private List<ProductBeanDto> sendProductList;
    
    /**
     * 错误信息
     */
    private String errorMsg;
    
    
     

    public List<ProductBeanDto> getSendProductList() {
		return sendProductList;
	}

	public void setSendProductList(List<ProductBeanDto> sendProductList) {
		this.sendProductList = sendProductList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public String getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(String deliverTime) {
        this.deliverTime = deliverTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Integer getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(Integer deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
	public String toString() {
		return "ManufacturerOrder [flowId=" + flowId + ", supplyId=" + supplyId
				+ ", supplyName=" + supplyName + ", deliverTime=" + deliverTime
				+ ", deliveryDate=" + deliveryDate + ", deliveryMethod="
				+ deliveryMethod + ", orderStatus=" + orderStatus
				+ ", sendProductList=" + sendProductList + ", errorMsg="
				+ errorMsg + "]";
	}
}
