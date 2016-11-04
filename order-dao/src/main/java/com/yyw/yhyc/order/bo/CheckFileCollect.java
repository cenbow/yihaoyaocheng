package com.yyw.yhyc.order.bo;

import java.math.BigDecimal;
import java.util.Date;

public class CheckFileCollect {
  
	private int id;
	
	private String file_version;
	
	private int countPay;
	
	private BigDecimal countPayAmt;
	
	private int countPaySuc;
	
	private String merId;
	
	private BigDecimal countPaySucAmt;
	
	
	private int countPayCancel;
	
	private BigDecimal countPayCancelAmt;
	
	private int countPayCancelSuc;
	
	private BigDecimal countPayCancelAmtSuc;
	
	private int countRefundSuc;
	
	private BigDecimal countRefundAmtSuc;
	
	private int countRefundCancel;
	
	private BigDecimal countRefundCancelAmt;
	
	private int  countRefundCancelSuc;
	
	private BigDecimal countRefundCancelAmtSuc;
	
	private Date create_time;

	

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFile_version() {
		return file_version;
	}

	public void setFile_version(String file_version) {
		this.file_version = file_version;
	}

	public int getCountPay() {
		return countPay;
	}

	public void setCountPay(int countPay) {
		this.countPay = countPay;
	}

	public int getCountPaySuc() {
		return countPaySuc;
	}

	public void setCountPaySuc(int countPaySuc) {
		this.countPaySuc = countPaySuc;
	}

	public BigDecimal getCountPayAmt() {
		return countPayAmt;
	}

	public void setCountPayAmt(BigDecimal countPayAmt) {
		this.countPayAmt = countPayAmt;
	}

	public int getCountPayCancel() {
		return countPayCancel;
	}

	public void setCountPayCancel(int countPayCancel) {
		this.countPayCancel = countPayCancel;
	}

	public BigDecimal getCountPayCancelAmt() {
		return countPayCancelAmt;
	}

	public void setCountPayCancelAmt(BigDecimal countPayCancelAmt) {
		this.countPayCancelAmt = countPayCancelAmt;
	}

	public int getCountPayCancelSuc() {
		return countPayCancelSuc;
	}

	public void setCountPayCancelSuc(int countPayCancelSuc) {
		this.countPayCancelSuc = countPayCancelSuc;
	}

	public BigDecimal getCountPayCancelAmtSuc() {
		return countPayCancelAmtSuc;
	}

	public void setCountPayCancelAmtSuc(BigDecimal countPayCancelAmtSuc) {
		this.countPayCancelAmtSuc = countPayCancelAmtSuc;
	}

	public int getCountRefundSuc() {
		return countRefundSuc;
	}

	public void setCountRefundSuc(int countRefundSuc) {
		this.countRefundSuc = countRefundSuc;
	}

	public BigDecimal getCountRefundAmtSuc() {
		return countRefundAmtSuc;
	}

	public void setCountRefundAmtSuc(BigDecimal countRefundAmtSuc) {
		this.countRefundAmtSuc = countRefundAmtSuc;
	}

	public int getCountRefundCancel() {
		return countRefundCancel;
	}

	public void setCountRefundCancel(int countRefundCancel) {
		this.countRefundCancel = countRefundCancel;
	}

	public BigDecimal getCountRefundCancelAmt() {
		return countRefundCancelAmt;
	}

	public void setCountRefundCancelAmt(BigDecimal countRefundCancelAmt) {
		this.countRefundCancelAmt = countRefundCancelAmt;
	}

	public int getCountRefundCancelSuc() {
		return countRefundCancelSuc;
	}

	public void setCountRefundCancelSuc(int countRefundCancelSuc) {
		this.countRefundCancelSuc = countRefundCancelSuc;
	}

	public BigDecimal getCountRefundCancelAmtSuc() {
		return countRefundCancelAmtSuc;
	}

	public void setCountRefundCancelAmtSuc(BigDecimal countRefundCancelAmtSuc) {
		this.countRefundCancelAmtSuc = countRefundCancelAmtSuc;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public BigDecimal getCountPaySucAmt() {
		return countPaySucAmt;
	}

	public void setCountPaySucAmt(BigDecimal countPaySucAmt) {
		this.countPaySucAmt = countPaySucAmt;
	}
	
	
     
}
