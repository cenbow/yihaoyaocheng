package com.yyw.yhyc.order.appdto;

import java.io.Serializable;

public class PromotionRule implements Serializable{

	 private static final long serialVersionUID = -1447928343795708680L;
	 
	 /*满多少元(件)*/
	 private Double promotionSum;
	 
	 /*减多少元(折)。送多少积分。送什么赠品*/
	 private String promotionMinu;

	public Double getPromotionSum() {
		return promotionSum;
	}

	public void setPromotionSum(Double promotionSum) {
		this.promotionSum = promotionSum;
	}

	public String getPromotionMinu() {
		return promotionMinu;
	}

	public void setPromotionMinu(String promotionMinu) {
		this.promotionMinu = promotionMinu;
	}
	 
}
