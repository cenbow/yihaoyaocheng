package com.yyw.yhyc.order.dto;

import java.util.List;

/**
 * 促销对应的实体类
 * @author wangkui01
 *
 */
public class OrderPromotionDto {
	/**
	 * 促销的id
	 */
	private Integer promotionId;
	/**
	 * 企业id
	 */
	private String enterpriseId;
	/**
	 * 活动名称
	 */
	private String promotionName;
	/**
	 * 促销是否有效：1有效，0无效
	 */
	private String promotionState;
	/**
	 * 活动类型;1:特价活动;2:单品满减;3:多品满减;4:满送积分;5:满赠
	 */
	private Integer promotionType;
	/**
	 * 	活动条件;0:按金额;1:按件数
	 */
	private Integer promotionPre;
	/**
	 * 满减方式;0:减总金额;1:减每件金额
	 */
	private Integer promotionMethod;
	/**
	 * 层级递增;0,不是;1,是
	 */
	private Integer levelIncre;
	/**
	 * 每个用户可参加次数
	 */
	private Integer limitNum;
	
	/**
	 * 特价活动对应的商品库存集合
	 */
	private List<OrderPromotionSpecialProductDto>  specialPromotionListDto;
	
	/**
	 * 促销的商品状态
	 */
	private List<OrderPromotionProductDto> promotionProductDtoList;
	
	/**
	 * 满减活动对应的规则
	 */
	private List<OrderPromotionRuleDto> promotionRuleList;
	
	
	public String getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getPromotionName() {
		return promotionName;
	}
	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}
	public String getPromotionState() {
		return promotionState;
	}
	public void setPromotionState(String promotionState) {
		this.promotionState = promotionState;
	}
	public Integer getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(Integer promotionType) {
		this.promotionType = promotionType;
	}
	public Integer getPromotionPre() {
		return promotionPre;
	}
	public void setPromotionPre(Integer promotionPre) {
		this.promotionPre = promotionPre;
	}
	public Integer getPromotionMethod() {
		return promotionMethod;
	}
	public void setPromotionMethod(Integer promotionMethod) {
		this.promotionMethod = promotionMethod;
	}
	public Integer getLevelIncre() {
		return levelIncre;
	}
	public void setLevelIncre(Integer levelIncre) {
		this.levelIncre = levelIncre;
	}
	public Integer getLimitNum() {
		return limitNum;
	}
	public void setLimitNum(Integer limitNum) {
		this.limitNum = limitNum;
	}
	
	
	public List<OrderPromotionProductDto> getPromotionProductDtoList() {
		return promotionProductDtoList;
	}
	public void setPromotionProductDtoList(
			List<OrderPromotionProductDto> promotionProductDtoList) {
		this.promotionProductDtoList = promotionProductDtoList;
	}
	public Integer getPromotionId() {
		return promotionId;
	}
	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}
	public List<OrderPromotionSpecialProductDto> getSpecialPromotionListDto() {
		return specialPromotionListDto;
	}
	public void setSpecialPromotionListDto(
			List<OrderPromotionSpecialProductDto> specialPromotionListDto) {
		this.specialPromotionListDto = specialPromotionListDto;
	}
	public List<OrderPromotionRuleDto> getPromotionRuleList() {
		return promotionRuleList;
	}
	public void setPromotionRuleList(List<OrderPromotionRuleDto> promotionRuleList) {
		this.promotionRuleList = promotionRuleList;
	}
	@Override
	public String toString() {
		return "OrderPromotionDto [promotionId=" + promotionId
				+ ", enterpriseId=" + enterpriseId + ", promotionName="
				+ promotionName + ", promotionState=" + promotionState
				+ ", promotionType=" + promotionType + ", promotionPre="
				+ promotionPre + ", promotionMethod=" + promotionMethod
				+ ", levelIncre=" + levelIncre + ", limitNum=" + limitNum
				+ ", specialPromotionListDto=" + specialPromotionListDto
				+ ", promotionProductDtoList=" + promotionProductDtoList
				+ ", promotionRuleList=" + promotionRuleList + "]";
	}
	
	
	
	

}
