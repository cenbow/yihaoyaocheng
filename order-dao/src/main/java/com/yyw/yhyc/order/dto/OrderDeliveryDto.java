package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.OrderDelivery;

import java.io.File;

/**
 * Created by lizhou on 2016/7/31
 */
public class OrderDeliveryDto extends OrderDelivery {

    private static final long serialVersionUID = -7162110480396906836L;

    /* 收货人省名称 */
    private String receiveProvinceName;

    /* 收货人市名称 */
    private String receiveCityName;

    /* 收货人区县名称 */
    private String receiveRegionName;

    /* 用户收发货地址id */
    private int receiverAddressId;

    /* 批号导入EXCEL */
    private File excelPath;

    public File getExcelPath() {
        return excelPath;
    }

    public void setExcelPath(File excelPath) {
        this.excelPath = excelPath;
    }

    public int getReceiverAddressId() {
        return receiverAddressId;
    }

    public void setReceiverAddressId(int receiverAddressId) {
        this.receiverAddressId = receiverAddressId;
    }

    public String getReceiveProvinceName() {
        return receiveProvinceName;
    }

    public void setReceiveProvinceName(String receiveProvinceName) {
        this.receiveProvinceName = receiveProvinceName;
    }

    public String getReceiveCityName() {
        return receiveCityName;
    }

    public void setReceiveCityName(String receiveCityName) {
        this.receiveCityName = receiveCityName;
    }

    public String getReceiveRegionName() {
        return receiveRegionName;
    }

    public void setReceiveRegionName(String receiveRegionName) {
        this.receiveRegionName = receiveRegionName;
    }

    @Override
    public String toString() {
        return "OrderDeliveryDto{" +
                "receiveProvinceName='" + receiveProvinceName + '\'' +
                ", receiveCityName='" + receiveCityName + '\'' +
                ", receiveRegionName='" + receiveRegionName + '\'' +
                '}';
    }
}
