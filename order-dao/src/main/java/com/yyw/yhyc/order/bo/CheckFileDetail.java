package com.yyw.yhyc.order.bo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.yyw.yhyc.bo.Model;

public class CheckFileDetail extends Model{
	
	private int id;
	
	private Date TranDate;
	
	private String AcqCode;
	private String MerId;
	
	private String TranReserved;
	
	private String LiqDate;
	
	private String MerOrderNo;
	
	
	private int BankInstNo;
	
	private String CurryNo;
	
	private String TranType;
	
	private String BusiType;
	
	private String OrderStatus;
	
	private String ChannelRespCode;
	
	private String CompleteDate;
	private String CompleteTime;
	
	private BigDecimal OrderAmt;
	
	private String AcqSeqId;
	
	private String AcqDate;
	
	private String AcqTime;
	
	
	private String OriTranDate;
	
	private String OriOrderNo;
	
	private String DCMark;
	
	private String MerResv;
	
	private Date create_time;
	
	private int Pid;

	public int getPid() {
		return Pid;
	}

	public void setPid(int pid) {
		Pid = pid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTranDate() {
		return TranDate;
	}

	public void setTranDate(Date tranDate) {
		TranDate = tranDate;
	}

   
	public String getMerId() {
		return MerId;
	}

	public void setMerId(String merId) {
		MerId = merId;
	}

	public String getTranReserved() {
		return TranReserved;
	}

	public void setTranReserved(String tranReserved) {
		TranReserved = tranReserved;
	}

	

	

	public String getLiqDate() {
		return LiqDate;
	}

	public void setLiqDate(String liqDate) {
		LiqDate = liqDate;
	}

	public String getMerOrderNo() {
		return MerOrderNo;
	}

	public void setMerOrderNo(String merOrderNo) {
		MerOrderNo = merOrderNo;
	}

	public int getBankInstNo() {
		return BankInstNo;
	}

	public void setBankInstNo(int bankInstNo) {
		BankInstNo = bankInstNo;
	}

	public String getCurryNo() {
		return CurryNo;
	}

	public void setCurryNo(String curryNo) {
		CurryNo = curryNo;
	}

	public String getTranType() {
		return TranType;
	}

	public void setTranType(String tranType) {
		TranType = tranType;
	}

	public String getBusiType() {
		return BusiType;
	}

	public void setBusiType(String busiType) {
		BusiType = busiType;
	}

	public String getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}

	public String getChannelRespCode() {
		return ChannelRespCode;
	}

	public String getAcqCode() {
		return AcqCode;
	}

	public void setAcqCode(String acqCode) {
		AcqCode = acqCode;
	}

	public void setChannelRespCode(String channelRespCode) {
		ChannelRespCode = channelRespCode;
	}

	public String getCompleteDate() {
		return CompleteDate;
	}

	public void setCompleteDate(String completeDate) {
		CompleteDate = completeDate;
	}

	public String getCompleteTime() {
		return CompleteTime;
	}

	public void setCompleteTime(String completeTime) {
		CompleteTime = completeTime;
	}

	public BigDecimal getOrderAmt() {
		return OrderAmt;
	}

	public void setOrderAmt(BigDecimal orderAmt) {
		OrderAmt = orderAmt;
	}

	public String getAcqSeqId() {
		return AcqSeqId;
	}

	public void setAcqSeqId(String acqSeqId) {
		AcqSeqId = acqSeqId;
	}

	

	public String getAcqDate() {
		return AcqDate;
	}

	public void setAcqDate(String acqDate) {
		AcqDate = acqDate;
	}

	public String getAcqTime() {
		return AcqTime;
	}

	public void setAcqTime(String acqTime) {
		AcqTime = acqTime;
	}

	public String getOriTranDate() {
		return OriTranDate;
	}

	public void setOriTranDate(String oriTranDate) {
		OriTranDate = oriTranDate;
	}

	public String getOriOrderNo() {
		return OriOrderNo;
	}

	public void setOriOrderNo(String oriOrderNo) {
		OriOrderNo = oriOrderNo;
	}

	public String getDCMark() {
		return DCMark;
	}

	public void setDCMark(String dCMark) {
		DCMark = dCMark;
	}

	public String getMerResv() {
		return MerResv;
	}

	public void setMerResv(String merResv) {
		MerResv = merResv;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}


}
