package com.yyw.yhyc.order.dto;

/**
 * Created by zhangqiang on 2016/8/5.
 */
public class UserDto {
    private int custId;//用户id
    private String custName;//用户名称
    private String userName;//帐号名称

    public int getCustId() {
        return custId;
    }

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

    @Override
    public String toString() {
        return "UserDto{" +
                "custId=" + custId +
                ", custName='" + custName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
