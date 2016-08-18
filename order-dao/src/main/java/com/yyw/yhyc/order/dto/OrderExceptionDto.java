package com.yyw.yhyc.order.dto;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by liqiang on 2016/8/8
 */
public class OrderExceptionDto extends OrderException {

    private static final long serialVersionUID = 418384253516688599L;

    private int payType;                                    //支付类型
    private String payTypeName;                             //支付类型名称
    private List<OrderReturnDto>  orderReturnList;         //退货商品列表
    private OrderDelivery orderDelivery;                    //订单收货信息
    private UsermanageEnterprise  usermanageEnterprise;    //订单发货信息
    private Integer  userType;                                  //1、采购商2、供应商
    private Integer billType;                               //发票类型 1 增值税专用发票 2 增值税普通发票
    private String billTypeName;                            //发票类型名称
    private String importStatusName;                       //导入状态名称
    private String importFileUrl;                          //导入文件url
    private String fileName;                                //文件名称


    /* 商品总金额 */
    private BigDecimal productPriceCount;

    /* 订单总金额 */
    private BigDecimal orderPriceCount;

    /* 异常订单状态 */
    private String orderStatusName;

    /* 原订单信息 */
    private Order order;

    /*拒收查询开始时间*/
    private String startTime;

    /*拒收查询结束时间*/
    private String endTime;

    private String province;            //省
    private String city;                //市
    private String area;                //区
    private Integer type;


    private List<String> payTypes; //支付类型

    private int orderCount;             //订单数量
    private int waitingConfirmCount;   // 待确认 数量
    private int refundingCount ;     //退款中数量

    public OrderExceptionDto() {
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getPayTypeName() {
        return payTypeName;
    }

    public void setPayTypeName(String payTypeName) {
        this.payTypeName = payTypeName;
    }

    public List<OrderReturnDto> getOrderReturnList() {
        return orderReturnList;
    }

    public void setOrderReturnList(List<OrderReturnDto> orderReturnList) {
        this.orderReturnList = orderReturnList;
    }

    public OrderDelivery getOrderDelivery() {
        return orderDelivery;
    }

    public void setOrderDelivery(OrderDelivery orderDelivery) {
        this.orderDelivery = orderDelivery;
    }

    public UsermanageEnterprise getUsermanageEnterprise() {
        return usermanageEnterprise;
    }

    public void setUsermanageEnterprise(UsermanageEnterprise usermanageEnterprise) {
        this.usermanageEnterprise = usermanageEnterprise;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public BigDecimal getProductPriceCount() {
        return productPriceCount;
    }

    public void setProductPriceCount(BigDecimal productPriceCount) {
        this.productPriceCount = productPriceCount;
    }

    public BigDecimal getOrderPriceCount() {
        return orderPriceCount;
    }

    public void setOrderPriceCount(BigDecimal orderPriceCount) {
        this.orderPriceCount = orderPriceCount;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<String> getPayTypes() {
        return payTypes;
    }

    public void setPayTypes(List<String> payTypes) {
        this.payTypes = payTypes;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getWaitingConfirmCount() {
        return waitingConfirmCount;
    }

    public void setWaitingConfirmCount(int waitingConfirmCount) {
        this.waitingConfirmCount = waitingConfirmCount;
    }

    public int getRefundingCount() {
        return refundingCount;
    }

    public void setRefundingCount(int refundingCount) {
        this.refundingCount = refundingCount;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getBillTypeName() {
        return billTypeName;
    }

    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }

    public String getImportStatusName() {
        return importStatusName;
    }

    public void setImportStatusName(String importStatusName) {
        this.importStatusName = importStatusName;
    }

    public String getImportFileUrl() {
        return importFileUrl;
    }

    public void setImportFileUrl(String importFileUrl) {
        this.importFileUrl = importFileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "OrderExceptionDto{" +
                "payType=" + payType +
                ", payTypeName='" + payTypeName + '\'' +
                ", orderReturnList=" + orderReturnList +
                ", orderDelivery=" + orderDelivery +
                ", usermanageEnterprise=" + usermanageEnterprise +
                ", userType=" + userType +
                ", productPriceCount=" + productPriceCount +
                ", orderPriceCount=" + orderPriceCount +
                ", orderStatusName='" + orderStatusName + '\'' +
                ", order=" + order +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", type=" + type +
                ", payTypes=" + payTypes +
                ", orderCount=" + orderCount +
                ", waitingConfirmCount=" + waitingConfirmCount +
                ", refundingCount=" + refundingCount +
                ", billType=" + billType +
                ", billTypeName=" + billTypeName +
                ", importStatusName=" + importStatusName +
                ", importFileUrl=" + importFileUrl +
                ", fileName=" + fileName +
                '}';
    }
}
