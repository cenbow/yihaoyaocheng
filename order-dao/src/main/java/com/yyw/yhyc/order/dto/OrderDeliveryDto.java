package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.OrderDelivery;
import org.springframework.web.multipart.MultipartFile;

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

    //上传路径
    private String path;
    //文件名
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    UserDto userDto;

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
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
