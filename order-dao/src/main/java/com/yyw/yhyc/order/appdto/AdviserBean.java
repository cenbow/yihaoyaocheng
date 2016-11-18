package com.yyw.yhyc.order.appdto;

import java.io.Serializable;

/**
 * Created by lizhou on 2016/11/18
 * 销售顾问 实体类
 */
public class AdviserBean implements Serializable {
    private static final long serialVersionUID = -4543928477631432077L;
    /* 销售顾问编码 */
    private String adviserCode;

    /* 销售顾问姓名 */
    private String adviseName;

    /* 销售顾问手机号码 */
    private String phoneNumber;

    /* 销售顾问备注 */
    private String remark;

    public String getAdviserCode() {
        return adviserCode;
    }

    public void setAdviserCode(String adviserCode) {
        this.adviserCode = adviserCode;
    }

    public String getAdviseName() {
        return adviseName;
    }

    public void setAdviseName(String adviseName) {
        this.adviseName = adviseName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "AdviserBean{" +
                "adviserCode='" + adviserCode + '\'' +
                ", adviseName='" + adviseName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
