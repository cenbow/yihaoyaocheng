package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.dto.OrderPromotionDetailDto;
import com.yyw.yhyc.order.dto.OrderPromotionDto;
import com.yyw.yhyc.order.dto.OrderPromotionRuleDto;
import com.yyw.yhyc.order.dto.ShoppingCartDto;

/**
 * 计算满减是层级递增的规则
 * @author wangkui01
 *
 */
@Service("calculationLevelIncreService")
public class CalculationLevelIncreService {
	
	private Log log = LogFactory.getLog(CalculationLevelIncreService.class);
	
	private void saveOrderPromotionDetailInfo(OrderPromotionDto promotionDto,ShoppingCartDto cartDto,BigDecimal shareMoney){
		
		if(cartDto.getPromotionDetailInfoList()==null){
			List<OrderPromotionDetailDto> list=new ArrayList<OrderPromotionDetailDto>();
			OrderPromotionDetailDto dto=new OrderPromotionDetailDto();
			dto.setPromotionId(promotionDto.getPromotionId());
			dto.setPromotionType(promotionDto.getPromotionType());
			dto.setShareMoney(shareMoney);
			list.add(dto);
			cartDto.setPromotionDetailInfoList(list);
		}else{
			OrderPromotionDetailDto dto=new OrderPromotionDetailDto();
			dto.setPromotionId(promotionDto.getPromotionId());
			dto.setPromotionType(promotionDto.getPromotionType());
			dto.setShareMoney(shareMoney);
			cartDto.getPromotionDetailInfoList().add(dto);
			
		}
		
	}
	
	/***********************************以下是按照单品-金额*********************************************/
	
	/**
	 * 层级递减--单品--按照总金额--减总金额
	 * 选择活动条件为按金额，满减方式为减总金额时，下方显示满100元立减10元。
	 * @param promotionDto
	 * @param promotionProductList
	 */
	public void sigleByMoneyDescAllMoney(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		if(!UtilHelper.isEmpty(promotionProductList)){
			//获取规则配置
			List<OrderPromotionRuleDto> promotionRuleList=promotionDto.getPromotionRuleList();
			
			for(ShoppingCartDto shoppingCartBean : promotionProductList){
				
				BigDecimal shareMoney=shoppingCartBean.getShareMoney(); //已经均摊的金额
				if(shareMoney==null){
					shareMoney=new BigDecimal(0);
				}
				//商品的数量
				BigDecimal productCountNum=new BigDecimal(shoppingCartBean.getProductCount());
				//商品的总金额
				BigDecimal produceAllMoney=shoppingCartBean.getProductPrice().multiply(productCountNum);
				
			
				if(!UtilHelper.isEmpty(promotionRuleList)){
					
					OrderPromotionRuleDto ruleBean=promotionRuleList.get(0);
					//满多少钱
					BigDecimal promotionSum=ruleBean.getPromotionSum();
					//减多少钱
					String promotionMinu=ruleBean.getPromotionMinu();
					BigDecimal promotionMinuMoney=new BigDecimal(promotionMinu);
					
					//算出商品的总金额是满减的多少倍数
					double timesValue=produceAllMoney.doubleValue()/promotionSum.doubleValue();
					double inceNum=Math.floor(timesValue);
				    BigDecimal subtractMoney=promotionMinuMoney.multiply(new BigDecimal(inceNum));
					
				    shareMoney=shareMoney.add(subtractMoney);
					shoppingCartBean.setShareMoney(shareMoney);
					
					this.saveOrderPromotionDetailInfo(promotionDto, shoppingCartBean, subtractMoney);
				}
				
				
				
				
			}
		}
		
	}
	
	
	
	/**
	 *  层级递减--单品--按照总金额--减每件金额
	 *  选择活动条件为按金额，满减方式为减每件金额时，下方显示满100元每件8折
	 * @param promotionDto
	 * @param promotionProductList
	 */
	public void sigleByMoneyDescEveryMoney(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		if(!UtilHelper.isEmpty(promotionProductList)){
			//获取规则配置
			List<OrderPromotionRuleDto> promotionRuleList=promotionDto.getPromotionRuleList();
			
			for(ShoppingCartDto shoppingCartBean : promotionProductList){
				
				BigDecimal shareMoney=shoppingCartBean.getShareMoney(); //已经均摊的金额
				if(shareMoney==null){
					shareMoney=new BigDecimal(0);
				}
				//商品的数量
				BigDecimal productCountNum=new BigDecimal(shoppingCartBean.getProductCount());
				//商品的总金额
				BigDecimal produceAllMoney=shoppingCartBean.getProductPrice().multiply(productCountNum);
				
			
				if(!UtilHelper.isEmpty(promotionRuleList)){
					
					OrderPromotionRuleDto ruleBean=promotionRuleList.get(0);
					//满多少钱
					BigDecimal promotionSum=ruleBean.getPromotionSum();
					//打多少折扣
					String promotionMinu=ruleBean.getPromotionMinu();
					BigDecimal promotionMinuMoney=new BigDecimal(promotionMinu);
					promotionMinuMoney=promotionMinuMoney.multiply(new BigDecimal(0.01));
					//算出商品总金额是满减金额的多少倍数
					double subValue=produceAllMoney.doubleValue()/promotionSum.doubleValue();
					double inceNum=Math.floor(subValue);
					//打的折扣
					double subtractMoney=Math.pow(promotionMinuMoney.doubleValue(),inceNum);
					
					double currentShareMoney=(1-subtractMoney)*produceAllMoney.doubleValue();
					
					shareMoney=shareMoney.add(new BigDecimal(currentShareMoney));
					
					shoppingCartBean.setShareMoney(shareMoney);
					
					this.saveOrderPromotionDetailInfo(promotionDto, shoppingCartBean, new BigDecimal(currentShareMoney));
				}
				
				
				
				
			}
		}
		
	}
	
	
	/**********************************************以下是按照单品--件数************************************************/
	
	/**
	 * 层级递减--单品--按照件数--减总金额
	 * 选择活动条件为按件数，满减方式为减总金额时，下方显示满10件立减10元。
	 * @param promotionDto
	 * @param promotionProductList
	 */
	public void sigleByBuyNumberDescAllMoney(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		if(!UtilHelper.isEmpty(promotionProductList)){
			//获取规则配置
			List<OrderPromotionRuleDto> promotionRuleList=promotionDto.getPromotionRuleList();
			
			for(ShoppingCartDto shoppingCartBean : promotionProductList){
				
				BigDecimal shareMoney=shoppingCartBean.getShareMoney(); //已经均摊的金额
				if(shareMoney==null){
					shareMoney=new BigDecimal(0);
				}
				//商品的数量
				BigDecimal productCountNum=new BigDecimal(shoppingCartBean.getProductCount());
			
				if(!UtilHelper.isEmpty(promotionRuleList)){
					
					OrderPromotionRuleDto ruleBean=promotionRuleList.get(0);
					//满多少件
					BigDecimal promotionSum=ruleBean.getPromotionSum();
					//减多少钱
					String promotionMinu=ruleBean.getPromotionMinu();
					BigDecimal promotionMinuMoney=new BigDecimal(promotionMinu);
					//算出商品总件数是满减件数的多少倍数
					double subValue=productCountNum.doubleValue()/promotionSum.doubleValue();
					double inceNum=Math.floor(subValue);
					
					BigDecimal subtractMoney=promotionMinuMoney.multiply(new BigDecimal(inceNum));
					
					shareMoney=shareMoney.add(subtractMoney);
					
					shoppingCartBean.setShareMoney(shareMoney);
					
					this.saveOrderPromotionDetailInfo(promotionDto, shoppingCartBean, subtractMoney);
				}
				
				
				
				
			}
		}
		
	}
	
	
	/**
	 *  层级递减--单品--按照件数--减每件金额
	 *  选择活动条件为按件数，满减方式为减每件金额时，下方显示满10件每件8折
	 * @param promotionDto
	 * @param promotionProductList
	 */
	public void sigleByBuyNumberDescEveryMoney(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		if(!UtilHelper.isEmpty(promotionProductList)){
			//获取规则配置
			List<OrderPromotionRuleDto> promotionRuleList=promotionDto.getPromotionRuleList();
			
			for(ShoppingCartDto shoppingCartBean : promotionProductList){
				
				BigDecimal shareMoney=shoppingCartBean.getShareMoney(); //已经均摊的金额
				if(shareMoney==null){
					shareMoney=new BigDecimal(0);
				}
				//商品的数量
				BigDecimal productCountNum=new BigDecimal(shoppingCartBean.getProductCount());
				//商品的总金额
				BigDecimal produceAllMoney=shoppingCartBean.getProductPrice().multiply(productCountNum);
				
			
				if(!UtilHelper.isEmpty(promotionRuleList)){
					
					OrderPromotionRuleDto ruleBean=promotionRuleList.get(0);
					//满多少件
					BigDecimal promotionSum=ruleBean.getPromotionSum();
					//打多少折扣
					String promotionMinu=ruleBean.getPromotionMinu();
					BigDecimal promotionMinuMoney=new BigDecimal(promotionMinu);
					promotionMinuMoney=promotionMinuMoney.multiply(new BigDecimal(0.01));
					//算出商品总件数需要减去的钱数
					double subValue=productCountNum.doubleValue()/promotionSum.doubleValue();
					double inceNum=Math.floor(subValue);
					//打的折扣
					double subtractMoney=Math.pow(promotionMinuMoney.doubleValue(),inceNum);
					
					double currentShareMoney=(1-subtractMoney)*produceAllMoney.doubleValue();
					
					shareMoney=shareMoney.add(new BigDecimal(currentShareMoney));
					
					shoppingCartBean.setShareMoney(shareMoney);
					
					this.saveOrderPromotionDetailInfo(promotionDto, shoppingCartBean, new BigDecimal(currentShareMoney));
				}
				
				
				
				
			}
		}
		
	}
	
	
	
	/********************************************以下是多品--按照金额******************************************************/
	
	 /**
		 * 层级递减--多品--按照总金额--减总金额
		 * 选择活动条件为按金额，满减方式为减总金额时，下方显示满100元立减10元。
		 * @param promotionDto
		 * @param promotionProductList
		 */
		public void multiProductByMoneyDescAllMoney(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
			
			if(!UtilHelper.isEmpty(promotionProductList)){
				//获取规则配置
				List<OrderPromotionRuleDto> promotionRuleList=promotionDto.getPromotionRuleList();
				
				//该笔订单的金额
				BigDecimal orderAllMoney=new BigDecimal(0);
				
				for(ShoppingCartDto shoppingCartBean : promotionProductList){
					//商品的数量
					BigDecimal productCountNum=new BigDecimal(shoppingCartBean.getProductCount());
					//商品的总金额
					BigDecimal produceAllMoney=shoppingCartBean.getProductPrice().multiply(productCountNum);
					
					orderAllMoney=orderAllMoney.add(produceAllMoney);
					
				}
				
				
			
				//算整个订单优惠的多少金额
				BigDecimal shareAllMoney=new BigDecimal(0);
				if(!UtilHelper.isEmpty(promotionRuleList)){
					
					OrderPromotionRuleDto ruleBean=promotionRuleList.get(0);
					//满多少钱
					BigDecimal promotionSum=ruleBean.getPromotionSum();
					//减多少钱
					String promotionMinu=ruleBean.getPromotionMinu();
					BigDecimal promotionMinuMoney=new BigDecimal(promotionMinu);
					//算出商品总金额是满减金额的多少倍数
					double subValue=orderAllMoney.doubleValue()/promotionSum.doubleValue();
					double inceNum=Math.floor(subValue);
					BigDecimal subtractMoney=promotionMinuMoney.multiply(new BigDecimal(inceNum));
					shareAllMoney=shareAllMoney.add(subtractMoney);
				}
				
				//分摊到每个商品上金额
				BigDecimal lastShareMoney=new BigDecimal(0);
				for(int i=0;i<promotionProductList.size();i++){
					
					ShoppingCartDto currentShoppingCartBean=promotionProductList.get(i);
					
					BigDecimal reallyShareMoney=currentShoppingCartBean.getShareMoney();
					if(reallyShareMoney==null){
						reallyShareMoney=new BigDecimal(0);
					}
					//商品的数量
					BigDecimal productCountNum=new BigDecimal(currentShoppingCartBean.getProductCount());
					//商品的总金额
					BigDecimal produceAllMoney=currentShoppingCartBean.getProductPrice().multiply(productCountNum);
					
					BigDecimal currentShareMoney=produceAllMoney.divide(orderAllMoney,2,BigDecimal.ROUND_HALF_UP).multiply(shareAllMoney);
					
					if(i!=promotionProductList.size()-1){
						lastShareMoney=lastShareMoney.add(currentShareMoney);
						reallyShareMoney=reallyShareMoney.add(currentShareMoney);
						
						this.saveOrderPromotionDetailInfo(promotionDto, currentShoppingCartBean, currentShareMoney);
						
					}else{
						BigDecimal subValue=shareAllMoney.subtract(lastShareMoney);
						reallyShareMoney=reallyShareMoney.add(subValue);
						
						this.saveOrderPromotionDetailInfo(promotionDto, currentShoppingCartBean,subValue);
					}
					currentShoppingCartBean.setShareMoney(reallyShareMoney);
					
					
				}
			}
			
		}
		
		
		/**
		 * 层级递减--多品--按照总金额--减每件金额
		 * 选择活动条件为按金额，满减方式为减每件金额时，下方显示满100元每件8折
		 * @param promotionDto
		 * @param promotionProductList
		 */
		public void multiProductByMoneyDescEveryMoney(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
			
			if(!UtilHelper.isEmpty(promotionProductList)){
				//获取规则配置
				List<OrderPromotionRuleDto> promotionRuleList=promotionDto.getPromotionRuleList();
				
				//该笔订单的金额
				BigDecimal orderAllMoney=new BigDecimal(0);
				
				for(ShoppingCartDto shoppingCartBean : promotionProductList){
					//商品的数量
					BigDecimal productCountNum=new BigDecimal(shoppingCartBean.getProductCount());
					//商品的总金额
					BigDecimal produceAllMoney=shoppingCartBean.getProductPrice().multiply(productCountNum);
					
					orderAllMoney=orderAllMoney.add(produceAllMoney);
					
				}
				
				
			
				//算整个订单优惠的多少金额
				BigDecimal shareAllMoney=new BigDecimal(0);
				if(!UtilHelper.isEmpty(promotionRuleList)){
					
					OrderPromotionRuleDto ruleBean=promotionRuleList.get(0);
					//满多少钱
					BigDecimal promotionSum=ruleBean.getPromotionSum();
					//打多少折
					String promotionMinu=ruleBean.getPromotionMinu();
					BigDecimal promotionMinuMoney=new BigDecimal(promotionMinu);
					promotionMinuMoney=promotionMinuMoney.multiply(new BigDecimal(0.01));
					//算出商品总金额需要减去的钱数
					double subValue=orderAllMoney.doubleValue()/promotionSum.doubleValue();
					double inceNum=Math.floor(subValue);
					
					double subtractMoney=Math.pow(promotionMinuMoney.doubleValue(),inceNum);
					
					double currentShareAllMoney=(1-subtractMoney)*orderAllMoney.doubleValue();
					
					shareAllMoney=shareAllMoney.add(new BigDecimal(currentShareAllMoney));
					
				}
				
				//分摊到每个商品上金额
				BigDecimal lastShareMoney=new BigDecimal(0);
				for(int i=0;i<promotionProductList.size();i++){
					
					ShoppingCartDto currentShoppingCartBean=promotionProductList.get(i);
					
					BigDecimal reallyShareMoney=currentShoppingCartBean.getShareMoney();
					if(reallyShareMoney==null){
						reallyShareMoney=new BigDecimal(0);
					}
					//商品的数量
					BigDecimal productCountNum=new BigDecimal(currentShoppingCartBean.getProductCount());
					//商品的总金额
					BigDecimal produceAllMoney=currentShoppingCartBean.getProductPrice().multiply(productCountNum);
					
					BigDecimal currentShareMoney=produceAllMoney.divide(orderAllMoney,2,BigDecimal.ROUND_HALF_UP).multiply(shareAllMoney);
					
					if(i!=promotionProductList.size()-1){
						lastShareMoney=lastShareMoney.add(currentShareMoney);
						reallyShareMoney=reallyShareMoney.add(currentShareMoney);
						
						this.saveOrderPromotionDetailInfo(promotionDto, currentShoppingCartBean, currentShareMoney);
					}else{
						BigDecimal subValue=shareAllMoney.subtract(lastShareMoney);
						reallyShareMoney=reallyShareMoney.add(subValue);
						
						this.saveOrderPromotionDetailInfo(promotionDto, currentShoppingCartBean, subValue);
					}
					currentShoppingCartBean.setShareMoney(reallyShareMoney);
					
				}
			}
			
		}
	
	/********************************************以下是多品--按照件数******************************************************/
		
		 /**
		 * 层级递减--多品--按照总件数--减总金额
		 * 选择活动条件为按件数，满减方式为减总金额时，下方显示满10件立减10元。
		 * @param promotionDto
		 * @param promotionProductList
		 */
		public void multiProductByBuyNumberDescAllMoney(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
			
			if(!UtilHelper.isEmpty(promotionProductList)){
				//获取规则配置
				List<OrderPromotionRuleDto> promotionRuleList=promotionDto.getPromotionRuleList();
				
				//该笔订单的总数量
				BigDecimal orderAllCount=new BigDecimal(0);
				
				for(ShoppingCartDto shoppingCartBean : promotionProductList){
					//商品的数量
					BigDecimal productCountNum=new BigDecimal(shoppingCartBean.getProductCount());
					orderAllCount=orderAllCount.add(productCountNum);
					
				}
				
				
			
				//算整个订单优惠的多少金额
				BigDecimal shareAllMoney=new BigDecimal(0);
				if(!UtilHelper.isEmpty(promotionRuleList)){
					
					OrderPromotionRuleDto ruleBean=promotionRuleList.get(0);
					//满多少钱
					BigDecimal promotionSum=ruleBean.getPromotionSum();
					//减多少钱
					String promotionMinu=ruleBean.getPromotionMinu();
					BigDecimal promotionMinuMoney=new BigDecimal(promotionMinu);
					//算出商品总件数需要减去的钱数
					double subValue=orderAllCount.doubleValue()/promotionSum.doubleValue();
					double inceNum=Math.floor(subValue);
					//计算减多少钱
					BigDecimal subtractMoney=promotionMinuMoney.multiply(new BigDecimal(inceNum));
					shareAllMoney=shareAllMoney.add(subtractMoney);
				}
				
				//分摊到每个商品上金额
				BigDecimal lastShareMoney=new BigDecimal(0);
				for(int i=0;i<promotionProductList.size();i++){
					
					ShoppingCartDto currentShoppingCartBean=promotionProductList.get(i);
					
					BigDecimal reallyShareMoney=currentShoppingCartBean.getShareMoney();
					if(reallyShareMoney==null){
						reallyShareMoney=new BigDecimal(0);
					}
					//商品的数量
					BigDecimal productCountNum=new BigDecimal(currentShoppingCartBean.getProductCount());
					
					BigDecimal currentShareMoney=productCountNum.divide(orderAllCount,2,BigDecimal.ROUND_HALF_UP).multiply(shareAllMoney);
					
					if(i!=promotionProductList.size()-1){
						lastShareMoney=lastShareMoney.add(currentShareMoney);
						reallyShareMoney=reallyShareMoney.add(currentShareMoney);
						
						this.saveOrderPromotionDetailInfo(promotionDto, currentShoppingCartBean, currentShareMoney);
					}else{
						BigDecimal subValue=shareAllMoney.subtract(lastShareMoney);
						reallyShareMoney=reallyShareMoney.add(subValue);
						this.saveOrderPromotionDetailInfo(promotionDto, currentShoppingCartBean, subValue);
					}
					currentShoppingCartBean.setShareMoney(reallyShareMoney);
					
				}
			}
			
		}
		
		
		
		 /**
		 * 层级递减--多品--按照总件数--减每件金额
		 * 选择活动条件为按件数，满减方式为减每件金额时，下方显示满10件每件8折
		 * @param promotionDto
		 * @param promotionProductList
		 */
		public void multiProductByBuyNumberDescEveryMoney(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
			
			if(!UtilHelper.isEmpty(promotionProductList)){
				//获取规则配置
				List<OrderPromotionRuleDto> promotionRuleList=promotionDto.getPromotionRuleList();
				
				//该笔订单的总数量
				BigDecimal orderAllCount=new BigDecimal(0);
				//该笔订单总金额
				BigDecimal orderAllMoney=new BigDecimal(0);
				for(ShoppingCartDto shoppingCartBean : promotionProductList){
					//商品的数量
					BigDecimal productCountNum=new BigDecimal(shoppingCartBean.getProductCount());
					BigDecimal productMoney=productCountNum.multiply(shoppingCartBean.getProductPrice());
					orderAllCount=orderAllCount.add(productCountNum);
					orderAllMoney=orderAllMoney.add(productMoney);
					
				}
				
				
			
				//算整个订单优惠的多少金额
				BigDecimal shareAllMoney=new BigDecimal(0);
				if(!UtilHelper.isEmpty(promotionRuleList)){
					
					OrderPromotionRuleDto ruleBean=promotionRuleList.get(0);
					//满多少钱
					BigDecimal promotionSum=ruleBean.getPromotionSum();
					//每件打多少折扣
					String promotionMinu=ruleBean.getPromotionMinu();
					BigDecimal promotionMinuMoney=new BigDecimal(promotionMinu);
					promotionMinuMoney=promotionMinuMoney.multiply(new BigDecimal(0.01));
					//算出商品总件数需要减去的钱数
					double subValue=orderAllCount.doubleValue()/promotionSum.doubleValue();
					double inceNum=Math.floor(subValue);
					
					//打的折扣
					double subtractMoney=Math.pow(promotionMinuMoney.doubleValue(),inceNum);
					
					//优惠的总金额
					double currentShareMoney=(1-subtractMoney)*orderAllMoney.doubleValue();
					
					shareAllMoney=shareAllMoney.add(new BigDecimal(currentShareMoney));
				}
				
				//分摊到每个商品上金额
				BigDecimal lastShareMoney=new BigDecimal(0);
				for(int i=0;i<promotionProductList.size();i++){
					
					ShoppingCartDto currentShoppingCartBean=promotionProductList.get(i);
					
					BigDecimal reallyShareMoney=currentShoppingCartBean.getShareMoney();
					if(reallyShareMoney==null){
						reallyShareMoney=new BigDecimal(0);
					}
					//商品的数量
					BigDecimal productCountNum=new BigDecimal(currentShoppingCartBean.getProductCount());
					
					BigDecimal currentShareMoney=productCountNum.divide(orderAllCount,2,BigDecimal.ROUND_HALF_UP).multiply(shareAllMoney);
					
					if(i!=promotionProductList.size()-1){
						lastShareMoney=lastShareMoney.add(currentShareMoney);
						reallyShareMoney=reallyShareMoney.add(currentShareMoney);
						this.saveOrderPromotionDetailInfo(promotionDto, currentShoppingCartBean, currentShareMoney);
					}else{
						BigDecimal subValue=shareAllMoney.subtract(lastShareMoney);
						reallyShareMoney=reallyShareMoney.add(subValue);
						
						this.saveOrderPromotionDetailInfo(promotionDto, currentShoppingCartBean, subValue);
					}
					currentShoppingCartBean.setShareMoney(reallyShareMoney);
					
					
				}
			}
			
		}
	

}
