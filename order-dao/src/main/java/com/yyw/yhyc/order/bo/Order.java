/**
 * Created By: XI
 * Created On: 2016-7-28 17:34:55
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import java.math.BigDecimal;

import com.yyw.yhyc.bo.Model;

public class Order extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	订单ID主键
	  */
	private Integer orderId;

	/**
	  *	订单编号
	  */
	private String flowId;

	/**
	  *	采购商名称
	  */
	private String custName;

	/**
	  *	客户ID
	  */
	private Integer custId;

	/**
	  *	供应商名称
	  */
	private String supplyName;

	/**
	  *	供应商ID
	  */
	private Integer supplyId;

	/**
	  *	取消原因
	  */
	private String cancelResult;

	/**
	  *	订单优惠后金额
	  */
	private java.math.BigDecimal orgTotal;

	/**
	  *	运费金额
	  */
	private java.math.BigDecimal freight;

	/**
	  *	订单总金额
	  */
	private java.math.BigDecimal orderTotal;

	/**
	  *	最后支付金额
	  */
	private java.math.BigDecimal finalPay;

	/**
	  *	商品总数量
	  */
	private Integer totalCount;

	/**
	  *	订单状态
	  */
	private String orderStatus;

	/**
	  *	备注信息
	  */
	private String remark;

	/**
	  *	买家留言
	  */
	private String leaveMessage;

	/**
	  *	订单合并付款ID
	  */
	private Integer orderCombinedId;

	/**
	  *	卖家是否确认结算
	  */
	private String confirmSettlement;

	/**
	  *	结算金额
	  */
	private java.math.BigDecimal settlementMoney;

	/**
	  *	优惠金额
	  */
	private java.math.BigDecimal preferentialMoney;

	/**
	  *	优惠金额备注
	  */
	private String preferentialRemark;

	/**
	  *	支付状态
	  */
	private String payStatus;

	/**
	  *	支付类型表ID
	  */
	private Integer payTypeId;

	/**
	  *	订单生成时间
	  */
	private String createTime;

	/**
	  *	支付时间
	  */
	private String payTime;

	/**
	  *	取消时间
	  */
	private String cancelTime;

	/**
	  *	发货时间
	  */
	private String deliverTime;

	/**
	  *	收货时间
	  */
	private String receiveTime;

	/**
	  *	结算时间
	  */
	private String settlementTime;

	/**
	  *	
	  */
	private String createUser;

	/**
	  *	订单最后更新时间
	  */
	private String updateTime;

	/**
	  *	
	  */
	private String updateUser;

	/**
	  *	发票类型 1 增值税专用发票 2 增值税普通发票
	  */
	private Integer billType;

	/**
	  *	延期收货日志（时间 第n次延期）
	  */
	private String delayLog;

	/**
	  *	1,买家确认收货 2,系统自动确认收货
	  */
	private Integer receiveType;
	
	/**
	  *	账期还款状态 0 未还款  1 已还款
	  */
	private Integer paymentTermStatus;

	/* 订单账期（单位：天） */
	private Integer paymentTerm;

	/**
	 * 延期次数
	 * @return
     */
	private  Integer delayTimes;
	
	private String promotionName; //包含多个促销名称,用,分割


	/**
	 * 	订单支付标记 1：打款成功 2：打款失败 3：退款成功 4：退款失败'
	 * @return
	 */
	private Integer payFlag;

	/* 订单商品种类数量 */
	private int productSortCount;

	private int source;//订单来源
	
	/**
	 * 销售顾问编码
	 */
	private String adviserCode;
	
	/**
	 * 销售顾问姓名
	 */
	private String adviserName;
	
	/**
	 * 销售顾问手机号码
	 */
	private String adviserPhoneNumber; 
	
	/**
	 * 销售顾问备注
	 */
	private String adviserRemark;
	
	/**
	 * 卖家发货是部分发货,且不补发货物,剩下的货物金额
	 */
	private BigDecimal cancelMoney;
	
	/**
	 * 卖家发货是部分发货,所发货物金额
	 */
	private BigDecimal deliveryMoney;
	
	/**
	 * 卖家发货是部分发货,且不补发货物,剩下的货物均摊后的金额
	 */
	private BigDecimal preferentialCancelMoney;
	
	/**
	 * 卖家发货是部分发货,所发货物均摊后的金额
	 */
	private BigDecimal preferentialDeliveryMoney;
	
	/**
	 * 卖家是否部分发货,1-是,0-否
	 */
	private String isDartDelivery;
	
	private String partDeliveryRemark; //部分发货说明
	

	public BigDecimal getPreferentialCancelMoney() {
		return preferentialCancelMoney;
	}

	public void setPreferentialCancelMoney(BigDecimal preferentialCancelMoney) {
		this.preferentialCancelMoney = preferentialCancelMoney;
	}

	public BigDecimal getPreferentialDeliveryMoney() {
		return preferentialDeliveryMoney;
	}

	public void setPreferentialDeliveryMoney(BigDecimal preferentialDeliveryMoney) {
		this.preferentialDeliveryMoney = preferentialDeliveryMoney;
	}

	public BigDecimal getCancelMoney() {
		return cancelMoney;
	}

	public void setCancelMoney(BigDecimal cancelMoney) {
		this.cancelMoney = cancelMoney;
	}

	public BigDecimal getDeliveryMoney() {
		return deliveryMoney;
	}

	public void setDeliveryMoney(BigDecimal deliveryMoney) {
		this.deliveryMoney = deliveryMoney;
	}

	public String getIsDartDelivery() {
		return isDartDelivery;
	}

	public void setIsDartDelivery(String isDartDelivery) {
		this.isDartDelivery = isDartDelivery;
	}

	public Integer getPayFlag() {
		return payFlag;
	}

	public void setPayFlag(Integer payFlag) {
		this.payFlag = payFlag;
	}


	public Integer getPaymentTermStatus() {
		return paymentTermStatus;
	}

	public void setPaymentTermStatus(Integer paymentTermStatus) {
		this.paymentTermStatus = paymentTermStatus;
	}

	/**
	  *	订单ID主键
	  */
	public Integer getOrderId()
	{
		return orderId;
	}
	
	/**
	  *	订单ID主键
	  */
	public void setOrderId(Integer orderId)
	{
		this.orderId = orderId;
	}
	
	/**
	  *	订单编号
	  */
	public String getFlowId()
	{
		return flowId;
	}
	
	/**
	  *	订单编号
	  */
	public void setFlowId(String flowId)
	{
		this.flowId = flowId;
	}
	
	/**
	  *	采购商名称
	  */
	public String getCustName()
	{
		return custName;
	}
	
	/**
	  *	采购商名称
	  */
	public void setCustName(String custName)
	{
		this.custName = custName;
	}
	
	/**
	  *	客户ID
	  */
	public Integer getCustId()
	{
		return custId;
	}
	
	/**
	  *	客户ID
	  */
	public void setCustId(Integer custId)
	{
		this.custId = custId;
	}
	
	/**
	  *	供应商名称
	  */
	public String getSupplyName()
	{
		return supplyName;
	}
	
	/**
	  *	供应商名称
	  */
	public void setSupplyName(String supplyName)
	{
		this.supplyName = supplyName;
	}
	
	/**
	  *	供应商ID
	  */
	public Integer getSupplyId()
	{
		return supplyId;
	}
	
	/**
	  *	供应商ID
	  */
	public void setSupplyId(Integer supplyId)
	{
		this.supplyId = supplyId;
	}
	
	/**
	  *	取消原因
	  */
	public String getCancelResult()
	{
		return cancelResult;
	}
	
	/**
	  *	取消原因
	  */
	public void setCancelResult(String cancelResult)
	{
		this.cancelResult = cancelResult;
	}
	
	/**
	  *	订单优惠后金额
	  */
	public java.math.BigDecimal getOrgTotal() 
	{
		return orgTotal;
	}
	
	/**
	  *	订单优惠后金额
	  */
	public void setOrgTotal(java.math.BigDecimal orgTotal) 
	{
		this.orgTotal = orgTotal;
	}
	
	/**
	  *	运费金额
	  */
	public java.math.BigDecimal getFreight() 
	{
		return freight;
	}
	
	/**
	  *	运费金额
	  */
	public void setFreight(java.math.BigDecimal freight) 
	{
		this.freight = freight;
	}
	
	/**
	  *	订单总金额
	  */
	public java.math.BigDecimal getOrderTotal() 
	{
		return orderTotal;
	}
	
	/**
	  *	订单总金额
	  */
	public void setOrderTotal(java.math.BigDecimal orderTotal) 
	{
		this.orderTotal = orderTotal;
	}
	
	/**
	  *	最后支付金额
	  */
	public java.math.BigDecimal getFinalPay() 
	{
		return finalPay;
	}
	
	/**
	  *	最后支付金额
	  */
	public void setFinalPay(java.math.BigDecimal finalPay) 
	{
		this.finalPay = finalPay;
	}
	
	/**
	  *	商品总数量
	  */
	public Integer getTotalCount()
	{
		return totalCount;
	}
	
	/**
	  *	商品总数量
	  */
	public void setTotalCount(Integer totalCount)
	{
		this.totalCount = totalCount;
	}
	
	/**
	  *	订单状态
	  */
	public String getOrderStatus()
	{
		return orderStatus;
	}
	
	/**
	  *	订单状态
	  */
	public void setOrderStatus(String orderStatus)
	{
		this.orderStatus = orderStatus;
	}
	
	/**
	  *	备注信息
	  */
	public String getRemark()
	{
		return remark;
	}
	
	/**
	  *	备注信息
	  */
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	
	/**
	  *	买家留言
	  */
	public String getLeaveMessage()
	{
		return leaveMessage;
	}
	
	/**
	  *	买家留言
	  */
	public void setLeaveMessage(String leaveMessage)
	{
		this.leaveMessage = leaveMessage;
	}
	
	/**
	  *	订单合并付款ID
	  */
	public Integer getOrderCombinedId()
	{
		return orderCombinedId;
	}
	
	/**
	  *	订单合并付款ID
	  */
	public void setOrderCombinedId(Integer orderCombinedId)
	{
		this.orderCombinedId = orderCombinedId;
	}
	
	/**
	  *	卖家是否确认结算
	  */
	public String getConfirmSettlement()
	{
		return confirmSettlement;
	}
	
	/**
	  *	卖家是否确认结算
	  */
	public void setConfirmSettlement(String confirmSettlement)
	{
		this.confirmSettlement = confirmSettlement;
	}
	
	/**
	  *	结算金额
	  */
	public java.math.BigDecimal getSettlementMoney() 
	{
		return settlementMoney;
	}
	
	/**
	  *	结算金额
	  */
	public void setSettlementMoney(java.math.BigDecimal settlementMoney) 
	{
		this.settlementMoney = settlementMoney;
	}
	
	/**
	  *	优惠金额
	  */
	public java.math.BigDecimal getPreferentialMoney() 
	{
		return preferentialMoney;
	}
	
	/**
	  *	优惠金额
	  */
	public void setPreferentialMoney(java.math.BigDecimal preferentialMoney) 
	{
		this.preferentialMoney = preferentialMoney;
	}
	
	/**
	  *	优惠金额备注
	  */
	public String getPreferentialRemark()
	{
		return preferentialRemark;
	}
	
	/**
	  *	优惠金额备注
	  */
	public void setPreferentialRemark(String preferentialRemark)
	{
		this.preferentialRemark = preferentialRemark;
	}
	
	/**
	  *	支付状态
	  */
	public String getPayStatus()
	{
		return payStatus;
	}
	
	/**
	  *	支付状态
	  */
	public void setPayStatus(String payStatus)
	{
		this.payStatus = payStatus;
	}
	
	/**
	  *	支付类型表ID
	  */
	public Integer getPayTypeId()
	{
		return payTypeId;
	}
	
	/**
	  *	支付类型表ID
	  */
	public void setPayTypeId(Integer payTypeId)
	{
		this.payTypeId = payTypeId;
	}
	
	/**
	  *	订单生成时间
	  */
	public String getCreateTime()
	{
		return createTime;
	}
	
	/**
	  *	订单生成时间
	  */
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	
	/**
	  *	支付时间
	  */
	public String getPayTime()
	{
		return payTime;
	}
	
	/**
	  *	支付时间
	  */
	public void setPayTime(String payTime)
	{
		this.payTime = payTime;
	}
	
	/**
	  *	取消时间
	  */
	public String getCancelTime()
	{
		return cancelTime;
	}
	
	/**
	  *	取消时间
	  */
	public void setCancelTime(String cancelTime)
	{
		this.cancelTime = cancelTime;
	}
	
	/**
	  *	发货时间
	  */
	public String getDeliverTime()
	{
		return deliverTime;
	}
	
	/**
	  *	发货时间
	  */
	public void setDeliverTime(String deliverTime)
	{
		this.deliverTime = deliverTime;
	}
	
	/**
	  *	收货时间
	  */
	public String getReceiveTime()
	{
		return receiveTime;
	}
	
	/**
	  *	收货时间
	  */
	public void setReceiveTime(String receiveTime)
	{
		this.receiveTime = receiveTime;
	}
	
	/**
	  *	结算时间
	  */
	public String getSettlementTime()
	{
		return settlementTime;
	}
	
	/**
	  *	结算时间
	  */
	public void setSettlementTime(String settlementTime)
	{
		this.settlementTime = settlementTime;
	}
	
	/**
	  *	
	  */
	public String getCreateUser()
	{
		return createUser;
	}
	
	/**
	  *	
	  */
	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}
	
	/**
	  *	订单最后更新时间
	  */
	public String getUpdateTime()
	{
		return updateTime;
	}
	
	/**
	  *	订单最后更新时间
	  */
	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}
	
	/**
	  *	
	  */
	public String getUpdateUser()
	{
		return updateUser;
	}
	
	/**
	  *	
	  */
	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}
	
	/**
	  *	发票类型 1 增值税专用发票 2 增值税普通发票
	  */
	public Integer getBillType()
	{
		return billType;
	}
	
	/**
	  *	发票类型 1 增值税专用发票 2 增值税普通发票
	  */
	public void setBillType(Integer billType)
	{
		this.billType = billType;
	}
	
	/**
	  *	延期收货日志（时间 第n次延期）
	  */
	public String getDelayLog()
	{
		return delayLog;
	}
	
	/**
	  *	延期收货日志（时间 第n次延期）
	  */
	public void setDelayLog(String delayLog)
	{
		this.delayLog = delayLog;
	}
	
	/**
	  *	1,买家确认收货 2,系统自动确认收货
	  */
	public Integer getReceiveType()
	{
		return receiveType;
	}
	
	/**
	  *	1,买家确认收货 2,系统自动确认收货
	  */
	public void setReceiveType(Integer receiveType)
	{
		this.receiveType = receiveType;
	}


	public Integer getPaymentTerm() {
		return paymentTerm;
	}

	public void setPaymentTerm(Integer paymentTerm) {
		this.paymentTerm = paymentTerm;
	}

	public Integer getDelayTimes() {
		return delayTimes;
	}

	public void setDelayTimes(Integer delayTimes) {
		this.delayTimes = delayTimes;
	}

	public int getProductSortCount() {
		return productSortCount;
	}

	public void setProductSortCount(int productSortCount) {
		this.productSortCount = productSortCount;
	}

	public String toString() {
		return "Order{" +
				"orderId=" + orderId +
				", flowId='" + flowId + '\'' +
				", custName='" + custName + '\'' +
				", custId=" + custId +
				", supplyName='" + supplyName + '\'' +
				", supplyId=" + supplyId +
				", cancelResult='" + cancelResult + '\'' +
				", orgTotal=" + orgTotal +
				", freight=" + freight +
				", orderTotal=" + orderTotal +
				", finalPay=" + finalPay +
				", totalCount=" + totalCount +
				", orderStatus='" + orderStatus + '\'' +
				", remark='" + remark + '\'' +
				", leaveMessage='" + leaveMessage + '\'' +
				", orderCombinedId=" + orderCombinedId +
				", confirmSettlement='" + confirmSettlement + '\'' +
				", settlementMoney=" + settlementMoney +
				", preferentialMoney=" + preferentialMoney +
				", preferentialRemark='" + preferentialRemark + '\'' +
				", payStatus='" + payStatus + '\'' +
				", payTypeId=" + payTypeId +
				", createTime='" + createTime + '\'' +
				", payTime='" + payTime + '\'' +
				", cancelTime='" + cancelTime + '\'' +
				", deliverTime='" + deliverTime + '\'' +
				", receiveTime='" + receiveTime + '\'' +
				", settlementTime='" + settlementTime + '\'' +
				", createUser='" + createUser + '\'' +
				", updateTime='" + updateTime + '\'' +
				", updateUser='" + updateUser + '\'' +
				", billType=" + billType +
				", delayLog='" + delayLog + '\'' +
				", receiveType=" + receiveType +
				", paymentTermStatus=" + paymentTermStatus +
				", paymentTerm=" + paymentTerm +
				", delayTimes=" + delayTimes +
				", payFlag=" + payFlag +
				", productSortCount=" + productSortCount +
				", source=" + source +
				", adviserCode=" + adviserCode +
				", adviserName=" + adviserName +
				", adviserPhoneNumber=" + adviserPhoneNumber +
				", adviserRemark=" + adviserRemark +
				", cancelMoney=" + cancelMoney +
				", deliveryMoney=" + deliveryMoney +
				", isDartDelivery=" + isDartDelivery +
				'}';
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public String getAdviserCode() {
		return adviserCode;
	}

	public void setAdviserCode(String adviserCode) {
		this.adviserCode = adviserCode;
	}

	public String getAdviserName() {
		return adviserName;
	}

	public void setAdviserName(String adviserName) {
		this.adviserName = adviserName;
	}

	public String getAdviserPhoneNumber() {
		return adviserPhoneNumber;
	}

	public void setAdviserPhoneNumber(String adviserPhoneNumber) {
		this.adviserPhoneNumber = adviserPhoneNumber;
	}

	public String getAdviserRemark() {
		return adviserRemark;
	}

	public void setAdviserRemark(String adviserRemark) {
		this.adviserRemark = adviserRemark;
	}

	public String getPromotionName() {
		return promotionName;
	}

	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}

	public String getPartDeliveryRemark() {
		return partDeliveryRemark;
	}

	public void setPartDeliveryRemark(String partDeliveryRemark) {
		this.partDeliveryRemark = partDeliveryRemark;
	}
	
	

}

