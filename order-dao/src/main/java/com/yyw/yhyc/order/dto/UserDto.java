package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.enmu.CustTypeEnum;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by zhangqiang on 2016/8/5.
 */
public class UserDto  implements Serializable{

    private static final long serialVersionUID = 6520425576062439132L;
    public static final String REQUEST_KEY = "loginUserDto";

    private int custId;//商户id
    private String custName;//商户名称
    private String userName;//帐号名称
    private CustTypeEnum custType;//商户类型
    private String province;//	注册地址：省编码
    private String provinceName;//注册地址：省名称
    private String city;//注册地址：市编码
    private String cityName; //注册地址：市名称
    private String district; //注册地址：区编码
    private String districtName; //注册地址：区名称
    private String registeredAddress; //注册详细地址
    private Map<String, Object> user;

    public int getCustId() {return custId;}

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public CustTypeEnum getCustType() {
        return custType;
    }

    public void setCustType(CustTypeEnum custType) {
        this.custType = custType;
    }

    public String getProvince() {
        return province;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public String getCity() {
        return city;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDistrict() {
        return district;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getRegisteredAddress() {
        return registeredAddress;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    public Map<String, Object> getUser() {
        return user;
    }

    public void setUser(Map<String, Object> user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "custId=" + custId +
                ", custName='" + custName + '\'' +
                ", userName='" + userName + '\'' +
                ", custType=" + custType +
                ", province='" + province + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", city='" + city + '\'' +
                ", cityName='" + cityName + '\'' +
                ", district='" + district + '\'' +
                ", districtName='" + districtName + '\'' +
                ", registeredAddress='" + registeredAddress + '\'' +
                ", user=" + user +
                '}';
    }
}
