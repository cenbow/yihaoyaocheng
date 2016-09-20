package com.yyw.yhyc.order.appdto;

import java.io.Serializable;

/**
 * Created by luweibin on 2016/9/9.
 */
public class AddressBean implements Serializable {
    private int addressType;
    private long addressId;
    private String deliveryName;
    private String deliveryPhone;
    private String addressProvince;
    private String addressCity;
    private String addressCounty;
    private String addressDetail;

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getAddressProvince() {
        return addressProvince;
    }

    public void setAddressProvince(String addressProvince) {
        this.addressProvince = addressProvince;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressCounty() {
        return addressCounty;
    }

    public void setAddressCounty(String addressCounty) {
        this.addressCounty = addressCounty;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getAddressInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.addressProvince)
                .append(this.addressCity)
                .append(this.addressCounty)
                .append(this.addressDetail);
        return sb.toString();
    }
}
