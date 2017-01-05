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
 * 计算不是层级递增的逻辑规则计算
 * @author wangkui01
 *
 */
@Service("calculationNoLevelIncrePromotion")
public class CalculationNoLevelIncrePromotion {
	private Log log = LogFactory.getLog(CalculationNoLevelIncrePromotion.class);
	private double zheKou=0.1d;
	
	private void saveOrderPromotionDetailInfo(OrderPromotionDto promotionDto,ShoppingCartDto cartDto,BigDecimal shareMoney){
		
		if(cartDto.getPromotionDetailInfoList()==null){
			List<OrderPromotionDetailDto> list=new ArrayList<OrderPromotionDetailDto>();
			OrderPromotionDetailDto dto=new OrderPromotionDetailDto();
			dto.setPromotionId(promotionDto.getPromotionId());
			dto.setPromotionType(promotionDto.getPromotionType());
			dto.setShareMoney(shareMoney);
			dto.setPromotionName(promotionDto.getPromotionName());
			dto.setPromotionDto(promotionDto);
			list.add(dto);
			cartDto.setPromotionDetailInfoList(list);
		}else{
			OrderPromotionDetailDto dto=new OrderPromotionDetailDto();
			dto.setPromotionId(promotionDto.getPromotionId());
			dto.setPromotionType(promotionDto.getPromotionType());
			dto.setShareMoney(shareMoney);
			dto.setPromotionName(promotionDto.getPromotionName());
			dto.setPromotionDto(promotionDto);
			cartDto.getPromotionDetailInfoList().add(dto);
			
		}
		
	}
	
	
	/**
	 * 根据金额获取对应的规则档位
	 * @param promotionRuleList
	 * @param productMoney
	 * @return
	 */
	public OrderPromotionRuleDto getRuleByMoney(List<OrderPromotionRuleDto> promotionRuleList,BigDecimal productMoney){
		OrderPromotionRuleDto returnBean=null;
		if(!UtilHelper.isEmpty(promotionRuleList)){
			
			List<OrderPromotionRuleDto> dayuMoneyList=new ArrayList<OrderPromotionRuleDto>();
			
			for(OrderPromotionRuleDto currentRuleDto: promotionRuleList){
				
				BigDecimal promotionSum=currentRuleDto.getPromotionSum();
				 if(productMoney.compareTo(promotionSum)==1 || productMoney.compareTo(promotionSum)==0){
					 dayuMoneyList.add(currentRuleDto);
				 }
				
			}
			
			//取出最大的那个
			if(dayuMoneyList!=null && dayuMoneyList.size()>0){
				
				for(OrderPromotionRuleDto currentRuleDto: dayuMoneyList){
					
					BigDecimal promotionSum=currentRuleDto.getPromotionSum();
					
					 if(returnBean!=null){
						 
						BigDecimal currentManJian=returnBean.getPromotionSum();
						
						 if(promotionSum.compareTo(currentManJian)==1){
							 returnBean=currentRuleDto;
						 }
						
					 }else{
						 returnBean=currentRuleDto;
					 }
					
				}
			}
			
		}
		
		return returnBean;
	}
	
	
	/***********************************************单品--按照金额****************************************************/
	
	/**
	 * 单品--按照总金额--减总金额
	 * 选择活动条件为按金额，满减方式为减总金额时，下方显示满100元立减10元。
	 * @param promotionDto
	 * @param promotionProductList
	 */
	public void sigleByMoneyCalculationMethod(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		  if(!UtilHelper.isEmpty(promotionProductList)){
			  
			  for(ShoppingCartDto shoppingCartDto : promotionProductList){
				  
				  Integer productCount=shoppingCartDto.getProductCount();
				  BigDecimal productPrice=shoppingCartDto.getProductPrice();
				  
				  BigDecimal productMoney=productPrice.multiply(new BigDecimal(productCount));
				  
				  OrderPromotionRuleDto ruleDto=this.getRuleByMoney(promotionDto.getPromotionRuleList(), productMoney);
				
				  if(ruleDto!=null){
					  
					  String subMoney=ruleDto.getPromotionMinu();
					  BigDecimal subShareMoney=new BigDecimal(subMoney);
					  
					  BigDecimal currentShareMoney=shoppingCartDto.getShareMoney();
					  if(currentShareMoney==null){
						  currentShareMoney=new BigDecimal(0);
					  }
					  
					  currentShareMoney=currentShareMoney.add(subShareMoney);
					  
					  shoppingCartDto.setShareMoney(currentShareMoney);
					  
					  this.saveOrderPromotionDetailInfo(promotionDto, shoppingCartDto, subShareMoney);
					  
				  }
				  
				  
			  }
			  
		  }
		
	}
	
	
	/**
	 * 单品--按照总金额--减每件金额
	 * 选择活动条件为按金额，满减方式为减每件金额时，下方显示满100元每件8折
	 * @param promotionDto
	 * @param promotionProductList
	 */
	public void sigleByMoneyDescEveryCalculationMethod(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		  if(!UtilHelper.isEmpty(promotionProductList)){
			  
			  for(ShoppingCartDto shoppingCartDto : promotionProductList){
				  
				  Integer productCount=shoppingCartDto.getProductCount();
				  BigDecimal productPrice=shoppingCartDto.getProductPrice();
				  
				  BigDecimal productMoney=productPrice.multiply(new BigDecimal(productCount));
				  
				  OrderPromotionRuleDto ruleDto=this.getRuleByMoney(promotionDto.getPromotionRuleList(), productMoney);
				
				  if(ruleDto!=null){
					  
					  String subMoney=ruleDto.getPromotionMinu();
					  BigDecimal subShareMoney=new BigDecimal(subMoney);
					  subShareMoney=subShareMoney.multiply(new BigDecimal(zheKou));
					  
					  BigDecimal  oneValue=new BigDecimal(1);
					  //实际的优惠的金额
					  BigDecimal calcaluShareMoney=(oneValue.subtract(subShareMoney)).multiply(productMoney);
					  
					  BigDecimal currentShareMoney=shoppingCartDto.getShareMoney();
					  if(currentShareMoney==null){
						  currentShareMoney=new BigDecimal(0);
					  }
					  
					  currentShareMoney=currentShareMoney.add(calcaluShareMoney);
					  
					  shoppingCartDto.setShareMoney(currentShareMoney);
					  
					  this.saveOrderPromotionDetailInfo(promotionDto, shoppingCartDto, calcaluShareMoney);
				  }
				  
				  
			  }
			  
		  }
		
	}
	
	
	
	
	
	/***********************************************单品--按照件数****************************************************/
	/**
	 * 单品--按照件数--减总金额
	 * 选择活动条件为按件数，满减方式为减总金额时，下方显示满10件立减10元。
	 * @param promotionDto
	 * @param promotionProductList
	 */
	public void sigleByBuyNumberCalculationMethod(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		  if(!UtilHelper.isEmpty(promotionProductList)){
			  
			  for(ShoppingCartDto shoppingCartDto : promotionProductList){
				  
				  Integer productCount=shoppingCartDto.getProductCount();
				  
				  OrderPromotionRuleDto ruleDto=this.getRuleByMoney(promotionDto.getPromotionRuleList(),new BigDecimal(productCount));
				
				  if(ruleDto!=null){
					  
					  String subMoney=ruleDto.getPromotionMinu();
					  BigDecimal subShareMoney=new BigDecimal(subMoney);
					  
					  BigDecimal currentShareMoney=shoppingCartDto.getShareMoney();
					  if(currentShareMoney==null){
						  currentShareMoney=new BigDecimal(0);
					  }
					  
					  currentShareMoney=currentShareMoney.add(subShareMoney);
					  
					  shoppingCartDto.setShareMoney(currentShareMoney);
					  
					  this.saveOrderPromotionDetailInfo(promotionDto, shoppingCartDto, subShareMoney);
				  }
				  
				  
			  }
			  
		  }
		
	}
	
	
	
	/**
	 * 单品--按照件数--减每件金额
	 *  选择活动条件为按件数，满减方式为减每件金额时，下方显示满10件每件8折
	 * @param promotionDto
	 * @param promotionProductList
	 */
	public void sigleByBuyNumberDesEveryCalculationMethod(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		  if(!UtilHelper.isEmpty(promotionProductList)){
			  
			  for(ShoppingCartDto shoppingCartDto : promotionProductList){
				  
				  Integer productCount=shoppingCartDto.getProductCount();
				  
                  BigDecimal productPrice=shoppingCartDto.getProductPrice();
				  
				  BigDecimal productMoney=productPrice.multiply(new BigDecimal(productCount));
				  
				  OrderPromotionRuleDto ruleDto=this.getRuleByMoney(promotionDto.getPromotionRuleList(),new BigDecimal(productCount));
				
				  if(ruleDto!=null){
					  
					  String subMoney=ruleDto.getPromotionMinu();
					  BigDecimal subShareMoney=new BigDecimal(subMoney);
					  
                      subShareMoney=subShareMoney.multiply(new BigDecimal(zheKou));
					  
					  BigDecimal  oneValue=new BigDecimal(1);
					  //实际的优惠的金额
					  BigDecimal calcaluShareMoney=(oneValue.subtract(subShareMoney)).multiply(productMoney);
					  
					  BigDecimal currentShareMoney=shoppingCartDto.getShareMoney();
					  if(currentShareMoney==null){
						  currentShareMoney=new BigDecimal(0);
					  }
					  
					  currentShareMoney=currentShareMoney.add(calcaluShareMoney);
					  
					  shoppingCartDto.setShareMoney(currentShareMoney);
					  
					  
					  this.saveOrderPromotionDetailInfo(promotionDto, shoppingCartDto, calcaluShareMoney);
				  }
				  
				  
			  }
			  
		  }
		
	}
	
	
	/***********************************************多品--按照金额****************************************************/
	/**
	 * 多品--按照总金额--减总金额
	 * 选择活动条件为按金额，满减方式为减总金额时，下方显示满100元立减10元。
	 * @param promotionDto
	 * @param promotionProductList
	 */
	public void mutilplyByMoneyCalculationMethod(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		  if(!UtilHelper.isEmpty(promotionProductList)){
			     BigDecimal orderAllMoney=new BigDecimal(0); //订单总金额
			  for(ShoppingCartDto shoppingCartDto : promotionProductList){
				  Integer productCount=shoppingCartDto.getProductCount();
				  BigDecimal productPrice=shoppingCartDto.getProductPrice();
				  
				  BigDecimal productMoney=productPrice.multiply(new BigDecimal(productCount));
				  orderAllMoney=orderAllMoney.add(productMoney);
			  }
			  
			  //算出当前订单金额在规则中处于什么档位
			  OrderPromotionRuleDto ruleDto=this.getRuleByMoney(promotionDto.getPromotionRuleList(),orderAllMoney);
			  if(ruleDto!=null){
				  String subMoney=ruleDto.getPromotionMinu();
				  BigDecimal shareAllMoney=new BigDecimal(subMoney); //当前订单优惠总金额
				  
				   //以下算均摊金额，把该优惠的金额均摊到各商品中去
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
						
						BigDecimal currentShareMoney=produceAllMoney.divide(orderAllMoney,10,BigDecimal.ROUND_HALF_UP).multiply(shareAllMoney);
						
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
	
	
	
	/**
	 * 多品--按照总金额--减每件金额
	 * 选择活动条件为按金额，满减方式为减每件金额时，下方显示满100元每件8折
	 * @param promotionDto
	 * @param promotionProductList
	 */
	public void mutilplyByMoneyDescEveryCalculationMethod(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		  if(!UtilHelper.isEmpty(promotionProductList)){
			     BigDecimal orderAllMoney=new BigDecimal(0); //订单总金额
			  for(ShoppingCartDto shoppingCartDto : promotionProductList){
				  Integer productCount=shoppingCartDto.getProductCount();
				  BigDecimal productPrice=shoppingCartDto.getProductPrice();
				  
				  BigDecimal productMoney=productPrice.multiply(new BigDecimal(productCount));
				  orderAllMoney=orderAllMoney.add(productMoney);
			  }
			  
			  //算出当前订单金额在规则中处于什么档位
			  OrderPromotionRuleDto ruleDto=this.getRuleByMoney(promotionDto.getPromotionRuleList(),orderAllMoney);
			  if(ruleDto!=null){
				  String subMoney=ruleDto.getPromotionMinu();
				  BigDecimal discountValue=new BigDecimal(subMoney); //当前订单优惠的折扣
				  
				  discountValue=discountValue.multiply(new BigDecimal(zheKou));
				  BigDecimal oneValue=new BigDecimal(1);
				  
				  //总商品优惠的金额
				  BigDecimal shareAllMoney=(oneValue.subtract(discountValue)).multiply(orderAllMoney);
				   //以下算均摊金额，把该优惠的金额均摊到各商品中去
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
						
						BigDecimal currentShareMoney=produceAllMoney.divide(orderAllMoney,10,BigDecimal.ROUND_HALF_UP).multiply(shareAllMoney);
						
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
	
	
	
	
	
	
	/***********************************************多品--按照件数****************************************************/
	/**
	 * 多品--按照件数--减总金额
	 * 选择活动条件为按件数，满减方式为减总金额时，下方显示满10件立减10元。
	 * @param promotionDto
	 * @param promotionProductList
	 */
	public void mutilplyByBuyNumberCalculationMethod(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		  if(!UtilHelper.isEmpty(promotionProductList)){
			     BigDecimal orderProductAllNumber=new BigDecimal(0); //订单买的商品总数量
			     
			  for(ShoppingCartDto shoppingCartDto : promotionProductList){
				  Integer productCount=shoppingCartDto.getProductCount();
				  orderProductAllNumber=orderProductAllNumber.add(new BigDecimal(productCount));
			  }
			  
			  //算出当前订单金额在规则中处于什么档位
			  OrderPromotionRuleDto ruleDto=this.getRuleByMoney(promotionDto.getPromotionRuleList(),orderProductAllNumber);
			  if(ruleDto!=null){
				  String subMoney=ruleDto.getPromotionMinu();
				  BigDecimal shareAllMoney=new BigDecimal(subMoney); //当前订单优惠总金额
				  
				   //以下算均摊金额，把该优惠的金额均摊到各商品中去
					BigDecimal lastShareMoney=new BigDecimal(0);
					for(int i=0;i<promotionProductList.size();i++){
						
						ShoppingCartDto currentShoppingCartBean=promotionProductList.get(i);
						
						BigDecimal reallyShareMoney=currentShoppingCartBean.getShareMoney();
						if(reallyShareMoney==null){
							reallyShareMoney=new BigDecimal(0);
						}
						//商品的数量
						BigDecimal productCountNum=new BigDecimal(currentShoppingCartBean.getProductCount());
						
						BigDecimal currentShareMoney=productCountNum.divide(orderProductAllNumber,10,BigDecimal.ROUND_HALF_UP).multiply(shareAllMoney);
						
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
	
	
	
	/**
	 * 多品--按照件数--减每件金额
	 * 选择活动条件为按件数，满减方式为减每件金额时，下方显示满10件每件8折
	 * @param promotionDto
	 * @param promotionProductList
	 */
	public void mutilplyByBuyNumberDescEveryCalculationMethod(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		  if(!UtilHelper.isEmpty(promotionProductList)){
			     BigDecimal orderProductAllNumber=new BigDecimal(0); //订单买的商品总数量
			     BigDecimal orderAllMoney=new BigDecimal(0); //订单总金额
			     
			  for(ShoppingCartDto shoppingCartDto : promotionProductList){
				  Integer productCount=shoppingCartDto.getProductCount();
				  
				  BigDecimal productPrice=shoppingCartDto.getProductPrice();
				  
				  BigDecimal productMoney=productPrice.multiply(new BigDecimal(productCount));
				  orderAllMoney=orderAllMoney.add(productMoney);
				  orderProductAllNumber=orderProductAllNumber.add(new BigDecimal(productCount));
			  }
			  
			  //算出当前订单金额在规则中处于什么档位
			  OrderPromotionRuleDto ruleDto=this.getRuleByMoney(promotionDto.getPromotionRuleList(),orderProductAllNumber);
			  if(ruleDto!=null){
				  String subMoney=ruleDto.getPromotionMinu();
				  BigDecimal discountValue=new BigDecimal(subMoney); //当前订单优惠折扣
				  
				  discountValue=discountValue.multiply(new BigDecimal(zheKou));
				  BigDecimal oneValue=new BigDecimal(1);
				  
				  //总商品优惠的金额
				  BigDecimal shareAllMoney=(oneValue.subtract(discountValue)).multiply(orderAllMoney);
				  
				   //以下算均摊金额，把该优惠的金额均摊到各商品中去
					BigDecimal lastShareMoney=new BigDecimal(0);
					for(int i=0;i<promotionProductList.size();i++){
						
						ShoppingCartDto currentShoppingCartBean=promotionProductList.get(i);
						
						BigDecimal reallyShareMoney=currentShoppingCartBean.getShareMoney();
						if(reallyShareMoney==null){
							reallyShareMoney=new BigDecimal(0);
						}
						//商品的数量
						BigDecimal productCountNum=new BigDecimal(currentShoppingCartBean.getProductCount());
						
						BigDecimal currentShareMoney=productCountNum.divide(orderProductAllNumber,10,BigDecimal.ROUND_HALF_UP).multiply(shareAllMoney);
						
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
	
	
}
