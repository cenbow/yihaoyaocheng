package com.yyw.yhyc.order.appdto;

import java.io.Serializable;
import java.util.List;

public class FullReductionPromotion implements Serializable{

	 private static final long serialVersionUID = -1447928343795708680L;
	 
	 /* 促销活动id */
	 private Integer promotionId;
	 
	 /*满减方式;0:减总金额;1:减每件金额(打折)*/
	 private Integer promotionMethod;
	 
	 /*活动条件;0:按金额;1:按件数*/
	 private Integer promotionPre;
	 
	 /*层级递增; 0,不是;1,是*/
	 private Integer levelIncre;
	 
	 /*活动类型:1:特价活动;2:单品满减;3:多品满减;4:满送积分;5:单品满赠；6:多品满赠送;福建中源单品满赠:11;福建中源多品满赠12'*/
	 private int promotionType; 
	 
	 /*用户可参加次数*/
	 private Integer limitNum;
	 
	 /*活动规则*/
	 private List<PromotionRule> productPromotionRules;

	public Integer getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}

	public Integer getPromotionMethod() {
		return promotionMethod;
	}

	public void setPromotionMethod(Integer promotionMethod) {
		this.promotionMethod = promotionMethod;
	}

	public Integer getPromotionPre() {
		return promotionPre;
	}

	public void setPromotionPre(Integer promotionPre) {
		this.promotionPre = promotionPre;
	}

	public Integer getLevelIncre() {
		return levelIncre;
	}

	public void setLevelIncre(Integer levelIncre) {
		this.levelIncre = levelIncre;
	}

	public int getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(int promotionType) {
		this.promotionType = promotionType;
	}

	public Integer getLimitNum() {
		return limitNum;
	}

	public void setLimitNum(Integer limitNum) {
		this.limitNum = limitNum;
	}

	public List<PromotionRule> getProductPromotionRules() {
		return productPromotionRules;
	}

	public void setProductPromotionRules(List<PromotionRule> productPromotionRules) {
		this.productPromotionRules = productPromotionRules;
	}
	 
}
