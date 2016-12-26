package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.dto.OrderPromotionDto;
import com.yyw.yhyc.order.dto.ShoppingCartDto;

/**
 * 计算分摊金额业务逻辑
 * @author wangkui01
 *
 */
@Service("calculationPromotionShareService")
public class CalculationPromotionShareService {
	private Log log = LogFactory.getLog(CalculationPromotionShareService.class);
	
	@Autowired
	private CalculationLevelIncreService levelIncreService; //计算递增的满减规则
	@Autowired
	private CalculationNoLevelIncrePromotion normalPromotionService;//计算不是递增的满减规则
	
	
	
	/**
	 * 根据买的商品总钱数，从小到大排序
	 * @param shoppingCartDtoList
	 * @return
	 */
	private void processShoppingCartDtoSort(List<ShoppingCartDto> shoppingCartDtoList){
		
		  Collections.sort(shoppingCartDtoList,new Comparator(){
			  
			@Override
			public int compare(Object o1, Object o2) {
				ShoppingCartDto fistBean=(ShoppingCartDto)o1;
				ShoppingCartDto secondBean=(ShoppingCartDto)o2;
				
				BigDecimal fistMoney=fistBean.getProductPrice().multiply(new BigDecimal(fistBean.getProductCount()));
				BigDecimal secondMoney=secondBean.getProductPrice().multiply(new BigDecimal(secondBean.getProductCount()));
				
				return fistMoney.compareTo(secondMoney);
			}
			  
		  });  
		
	}
	
	/**
	 * 开始计算促销分摊金额
	 */
	public void calculationPromotionByPromitionBean(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		   //将需要的商品进行排序从小到大
		   this.processShoppingCartDtoSort(promotionProductList);
		
		    int levelIncre=promotionDto.getLevelIncre();
		    if(levelIncre==1){ //层级递减
		    	
		    	this.calculationLevelIncrePromotion(promotionDto, promotionProductList);
		    	
		    }else if(levelIncre==0){ //不是层级递减
		    	
		    	this.calculationNomarlPromotion(promotionDto, promotionProductList);
		    	
		    }
		    
		
		    
		    
	}
	
	
	/**
	 * 计算不是按照层级递减的逻辑算优惠金额
	 * @param promotionDto
	 * @param promotionProductList
	 */
	private void calculationNomarlPromotion(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
		
		   int promotionType=promotionDto.getPromotionType();
		    int promotionPre=promotionDto.getPromotionPre();
		    int promotionMethod=promotionDto.getPromotionMethod();
		    
		   if(promotionType==2){ //单品
	    	
	    	  if(promotionPre==0){//按照金额
	    		  
	    		     if(promotionMethod==0){//减总金额
	    		    	 
	    		    	 this.normalPromotionService.sigleByMoneyCalculationMethod(promotionDto, promotionProductList);
	    		    	 
	    		     }
	    		     
	    		     if(promotionMethod==1){//减每件金额
	    		    	 this.normalPromotionService.sigleByMoneyDescEveryCalculationMethod(promotionDto, promotionProductList);
	    		     }
	    		  
	    	  }
	    	  
	    	  if(promotionPre==1){//按照件数
	    		  
	    		     if(promotionMethod==0){//减总金额
	    		    	 this.normalPromotionService.sigleByBuyNumberCalculationMethod(promotionDto, promotionProductList);
	    		    	 
	    		     }
	    		     
	    		     if(promotionMethod==1){//减每件金额
	    		    	 this.normalPromotionService.sigleByBuyNumberDesEveryCalculationMethod(promotionDto, promotionProductList);
	    		     }
	    		  
	    	  }
	    	
	    }
	    
	    if(promotionType==3){//多品
	    	
	    
	    	if(promotionPre==0){//按照金额
	    		 
				     if(promotionMethod==0){//减总金额
				    	 
				    	 this.normalPromotionService.mutilplyByMoneyCalculationMethod(promotionDto, promotionProductList);
				    	 
				     }
				     
				     if(promotionMethod==1){//减每件金额
				    	 this.normalPromotionService.mutilplyByMoneyDescEveryCalculationMethod(promotionDto, promotionProductList);
				     }
   	        }
	    	 
   	      if(promotionPre==1){//按照件数
   		  
   		     if(promotionMethod==0){//减总金额
   		    	 this.normalPromotionService.mutilplyByBuyNumberCalculationMethod(promotionDto, promotionProductList);
   		     }
   		     
   		     if(promotionMethod==1){//减每件金额
   		    	 
   		    	 this.normalPromotionService.mutilplyByBuyNumberDescEveryCalculationMethod(promotionDto, promotionProductList);
   		    	 
   		     }
   		  
   	  }
	    	
	    }
		
		
		
	}
	
	
    /**
     * 计算层级递减
     * @param promotionDto
     * @param promotionProductList
     */
    private void calculationLevelIncrePromotion(OrderPromotionDto promotionDto,List<ShoppingCartDto> promotionProductList){
    	
    	    int promotionType=promotionDto.getPromotionType();
		    int promotionPre=promotionDto.getPromotionPre();
		    int promotionMethod=promotionDto.getPromotionMethod();
		    
		   if(promotionType==2){ //单品
	    	
	    	  if(promotionPre==0){//按照金额
	    		  
	    		     if(promotionMethod==0){//减总金额
	    		    	 this.levelIncreService.sigleByMoneyDescAllMoney(promotionDto, promotionProductList);
	    		     }
	    		     
	    		     if(promotionMethod==1){//减每件金额
	    		    	 this.levelIncreService.sigleByMoneyDescEveryMoney(promotionDto, promotionProductList);
	    		     }
	    		  
	    	  }
	    	  
	    	  if(promotionPre==1){//按照件数
	    		  
	    		     if(promotionMethod==0){//减总金额
	    		    	 
	    		    	 this.levelIncreService.sigleByBuyNumberDescAllMoney(promotionDto, promotionProductList);
	    		    	 
	    		     }
	    		     
	    		     if(promotionMethod==1){//减每件金额
	    		    	 
	    		    	 this.levelIncreService.sigleByBuyNumberDescEveryMoney(promotionDto, promotionProductList);
	    		    	 
	    		    	 
	    		     }
	    		  
	    	  }
	    	
	    }
	    
	    if(promotionType==3){//多品
	    	
	    	 if(promotionPre==0){//按照金额
    		     if(promotionMethod==0){//减总金额
    		    	 this.levelIncreService.multiProductByMoneyDescAllMoney(promotionDto, promotionProductList);
    		     }
    		     if(promotionMethod==1){//减每件金额
    		    	 this.levelIncreService.multiProductByMoneyDescEveryMoney(promotionDto, promotionProductList);
    		     }
    	  }
    	  if(promotionPre==1){//按照件数
    		     if(promotionMethod==0){//减总金额
    		    	 this.levelIncreService.multiProductByBuyNumberDescAllMoney(promotionDto, promotionProductList);
    		     }
    		     
    		     if(promotionMethod==1){//减每件金额
    		    	 this.levelIncreService.multiProductByBuyNumberDescEveryMoney(promotionDto, promotionProductList);
    		     }
    		  
    	  }
	    	
	    }
		
	}

}
