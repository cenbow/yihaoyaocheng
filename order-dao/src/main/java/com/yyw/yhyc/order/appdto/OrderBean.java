package com.yyw.yhyc.order.appdto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huhaibing on 2016/9/8.
 */
public class OrderBean implements Serializable{

    private static final long serialVersionUID = -2270982949438087902L;
    private String orderId;                     //订单Id
    private String orderStatus;                 //订单状态
    private String createTime;                  //下单时间
    private AddressBean address;                //地址对象
    private String supplyName;                  //供应商名称
    private String leaveMsg;                    //备注信息
    private List<OrderProductBean> productList;  //商品列表
    private String qq;                          //联系方式
    private Integer payType;                    //支付方式
    private Integer deliveryMethod;             //配送方式(1是自有物流 2是第三方物流)
    private Integer billType;                   //发票类型(1是增值税专用发票2是增值税普通发票)
    private double orderTotal;                  //订单总金额
    private double finalPay;                    //支付金额
    private String exceptionOrderId;            //异常订单编码
    private String applyTime;                   //申请时间
    private String returnDesc;                  //申请说明
    private String merchantDesc;                //商家回复
    private Integer varietyNumber;              //品种
    private Integer productNumber;              //商品数量
    private long residualTime;                //支付剩余时间
    private Integer delayTimes;                 //第几次延期(延期次数)
    private Integer postponeTime;               //能延期次数
    private String orderStatusName;            //状态枚举值
    private Integer supplyId;                   //供应商id
    private List<Integer> shopCartIdList;     //购物车id

    /* 销售顾问编码 */
    private String adviser_code;

    /* 销售顾问姓名 */
    private String adviser_name;

    /* 销售顾问手机号码 */
    private String phone_number;

    /* 销售顾问备注 */
    private String remark;

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(Integer deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getExceptionOrderId() {
        return exceptionOrderId;
    }

    public void setExceptionOrderId(String exceptionOrderId) {
        this.exceptionOrderId = exceptionOrderId;
    }

    public double getFinalPay() {
        return finalPay;
    }

    public void setFinalPay(double finalPay) {
        this.finalPay = finalPay;
    }

    public String getLeaveMsg() {
        return leaveMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
    }

    public String getMerchantDesc() {
        return merchantDesc;
    }

    public void setMerchantDesc(String merchantDesc) {
        this.merchantDesc = merchantDesc;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<OrderProductBean> getProductList() {
        return productList;
    }

    public void setProductList(List<OrderProductBean> productList) {
        this.productList = productList;
    }

    public Integer getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(Integer productNumber) {
        this.productNumber = productNumber;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public long getResidualTime() {
        return residualTime;
    }

    public void setResidualTime(long residualTime) {
        this.residualTime = residualTime;
    }

    public String getReturnDesc() {
        return returnDesc;
    }

    public void setReturnDesc(String returnDesc) {
        this.returnDesc = returnDesc;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public Integer getVarietyNumber() {
        return varietyNumber;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public void setVarietyNumber(Integer varietyNumber) {
        this.varietyNumber = varietyNumber;
    }


    public Integer getDelayTimes() {
        return delayTimes;
    }

    public void setDelayTimes(Integer delayTimes) {
        this.delayTimes = delayTimes;
    }

    public Integer getPostponeTime() {
        return postponeTime;
    }

    public void setPostponeTime(Integer postponeTime) {
        this.postponeTime = postponeTime;
    }

    public Integer getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(Integer supplyId) {
        this.supplyId = supplyId;
    }

    public List<Integer> getShopCartIdList() {
        return shopCartIdList;
    }

    public void setShopCartIdList(List<Integer> shopCartIdList) {
        this.shopCartIdList = shopCartIdList;
    }

    public String getAdviser_code() {
        return adviser_code;
    }

    public void setAdviser_code(String adviser_code) {
        this.adviser_code = adviser_code;
    }

    public String getAdviser_name() {
        return adviser_name;
    }

    public void setAdviser_name(String adviser_name) {
        this.adviser_name = adviser_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "orderId='" + orderId + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", createTime='" + createTime + '\'' +
                ", address=" + address +
                ", supplyName='" + supplyName + '\'' +
                ", leaveMsg='" + leaveMsg + '\'' +
                ", productList=" + productList +
                ", qq='" + qq + '\'' +
                ", payType=" + payType +
                ", deliveryMethod=" + deliveryMethod +
                ", billType=" + billType +
                ", orderTotal=" + orderTotal +
                ", finalPay=" + finalPay +
                ", exceptionOrderId='" + exceptionOrderId + '\'' +
                ", applyTime='" + applyTime + '\'' +
                ", returnDesc='" + returnDesc + '\'' +
                ", merchantDesc='" + merchantDesc + '\'' +
                ", varietyNumber=" + varietyNumber +
                ", productNumber=" + productNumber +
                ", residualTime=" + residualTime +
                ", delayTimes=" + delayTimes +
                ", postponeTime=" + postponeTime +
                ", supplyId=" + supplyId +
                ", shopCartIdList=" + shopCartIdList +
                ", adviser_code=" + adviser_code +
                ", adviser_name=" + adviser_name +
                ", phone_number=" + phone_number +
                ", remark=" + remark +
                '}';
    }
}
