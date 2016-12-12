package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yaoex.druggmp.dubbo.service.bean.PromotionDto;
import com.yaoex.druggmp.dubbo.service.bean.PromotionRuleDto;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.OrderPromotionHistory;
import com.yyw.yhyc.order.dto.OrderProductInfoDto;
import com.yyw.yhyc.order.dto.OrderPromotionDto;
import com.yyw.yhyc.order.dto.OrderPromotionProductDto;
import com.yyw.yhyc.order.dto.OrderPromotionRuleDto;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.mapper.OrderPromotionHistoryMapper;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;

/**
 * 处理订单的满减业务和特价活动业务
 * @author wangkui01
 *
 */
@Service("orderFullReductionService")
public class OrderFullReductionService {
	private Log log = LogFactory.getLog(OrderFullReductionService.class);
	@Autowired
	private CalculationPromotionShareService calculationPromotionShareService;
	@Reference
	private IPromotionDubboManageService promotionDubboManageService;
	@Autowired
	private OrderPromotionHistoryMapper orderPromotionHistoryMapper;
	
	
	/**
	 * 将已经拆好单的订单集合,根据每个订单对应的商品和商品参加的促销id,组装成对应的参加调商品组接口，然后
	 * 获取到该商品参加的特价活动和满减活动的规则，然后计算需要减掉的满减金额
	 * @param allShoppingCart
	 * @return
	 */
	public List<ShoppingCartListDto> processFullReduction(List<ShoppingCartListDto> allShoppingCart){
		
		if(UtilHelper.isEmpty(allShoppingCart)){
			return allShoppingCart;
		}
	
		//组装调doubble的服务参数
		List<Map<String,Object>> promotionIdList=this.getAllOrderPromotionId(allShoppingCart);
		if(UtilHelper.isEmpty(promotionIdList)){
			return allShoppingCart;
		}
		
	  Map<Integer,OrderPromotionDto> responseMap=this.processPormotionDubboMethodByAllPromotionCollection(promotionIdList);
		
		
	  for(ShoppingCartListDto shoppingCartDto : allShoppingCart){
		  
		  UsermanageEnterprise sellerBean=shoppingCartDto.getSeller();
		  UsermanageEnterprise buyerBean=shoppingCartDto.getBuyer();
		  String supplyId=sellerBean.getEnterpriseId(); //提供商的id
		  String custId=buyerBean.getEnterpriseId(); //采购商的id
		  //该订单买的商品
		  List<ShoppingCartDto> buyProductList=shoppingCartDto.getShoppingCartDtoList();
		  Map<Integer,List<OrderProductInfoDto>> currentOrderParamterMap=this.getPromotionParamter(buyProductList, supplyId, custId);
		  
		   if(currentOrderParamterMap!=null && currentOrderParamterMap.keySet().size()>0){
			   
			      if(!UtilHelper.isEmpty(responseMap)){
			    	  this.processMakeUpShoppingCartDto(buyProductList, responseMap,custId);
			    	  this.calculationProductPromotionShareMoney(buyProductList, responseMap);
			      }
			   
		   }else{
			   continue;
		   }
		  
	  }
		return allShoppingCart;
		
	}
	
	
	/**
	 * 将所有订单的参加的商品促销的id ,掉服务获取对应的实体对象
	 * @param promotionList
	 * @return
	 */
	public Map<Integer,OrderPromotionDto> processPormotionDubboMethodByAllPromotionCollection(List<Map<String,Object>> promotionList){
			 if(UtilHelper.isEmpty(promotionList)){
				 return null;
			 }
			 
		 List<PromotionDto> responsePromotionList=null;
			try{
				log.info("查询所有订单中参加促销的商品信息调iPromotionDubboManageService服务接口,请求参数params：" + promotionList);
				long startTime = System.currentTimeMillis();
				
				
				//responsePromotionList= promotionDubboManageService.queryPromotionByList(promotionList);
				
				//模拟数据
				PromotionDto test1=new PromotionDto();
				test1.setId(123);
				test1.setPromotionName("test123");
				test1.setEnterpriseId("79397");
				test1.setPromotionType((byte)2);//活动类型;1:特价活动;2:单品满减;3:多品满减;4:满送积分;5:满赠
				test1.setLevelIncre((byte)0);
				test1.setLimitNum(2);
				test1.setPromotionMethod((byte)0);//满减方式;0:减总金额;1:减每件金额
				test1.setPromotionPre((byte)0); //活动条件;0:按金额;1:按件数
				
				List<PromotionRuleDto> promotionRureList=new ArrayList<PromotionRuleDto>();
				
				PromotionRuleDto rule1=new PromotionRuleDto();
				rule1.setPromotionId(123);
				rule1.setPromotionSum(20);
				rule1.setPromotionMinu("5");
				
				promotionRureList.add(rule1);
				
				 List<String> spuCodeList=new ArrayList<String>();
				 spuCodeList.add("116224");
				
				 
				 test1.setPromotionRules(promotionRureList);
				 test1.setSpuCode(spuCodeList);
				
				 responsePromotionList=new ArrayList<PromotionDto>();
				 responsePromotionList.add(test1);
				
				long endTime = System.currentTimeMillis();
				log.info("查询所有订单中参加促销的商品信息(调用活动的iPromotionDubboManageService接口),耗时" + (endTime - startTime) + "毫秒，响应参数：responsePromotionList=" + responsePromotionList);
			}catch (Exception e){
				log.error("查询所有订单中参加促销的商品信息(调用活动的iPromotionDubboManageService接口),dubbo服务查询异常：errorMesssage = " + e.getMessage(),e);
				return null;
			}
			
			if(UtilHelper.isEmpty(responsePromotionList)){
				return null;
			}
			
			
			Map<Integer,OrderPromotionDto> returnMap=new HashMap<Integer,OrderPromotionDto>();
			//转换对应的实体
			for(PromotionDto promotionBean : responsePromotionList){
				
				Integer promotionId=promotionBean.getId();
				String promotionName=promotionBean.getPromotionName();
				String enterpriseId=promotionBean.getEnterpriseId();
				int promotionType=(int)promotionBean.getPromotionType();
				int promotionPre=(int)promotionBean.getPromotionPre();
				int promotionMethod=(int)promotionBean.getPromotionMethod();
				int levelIncre=(int)promotionBean.getLevelIncre();
				Integer limitNum=promotionBean.getLimitNum();
				
				OrderPromotionDto orderPromotionDto=new OrderPromotionDto();
				orderPromotionDto.setPromotionId(promotionId);
				orderPromotionDto.setPromotionName(promotionName);
				orderPromotionDto.setEnterpriseId(enterpriseId);
				orderPromotionDto.setPromotionState("1");//备注:由于接口返回的促销都会有效的，所以这边默认都是有效
				orderPromotionDto.setPromotionType(promotionType);
				orderPromotionDto.setPromotionPre(promotionPre);
				orderPromotionDto.setPromotionMethod(promotionMethod);
				orderPromotionDto.setLevelIncre(levelIncre);
				orderPromotionDto.setLimitNum(limitNum);
				
				if(promotionType!=1){ //该促销是非特价活动那么取出规则
					
					List<PromotionRuleDto> promotionRuleList=promotionBean.getPromotionRules();
					if(UtilHelper.isEmpty(promotionRuleList)){
						continue;
					}
					List<OrderPromotionRuleDto> orderPromitionRuleList=new ArrayList<OrderPromotionRuleDto>();
					
					for(PromotionRuleDto ruleDto : promotionRuleList){
						Integer promotionSum=ruleDto.getPromotionSum();
						String promotionMinu=ruleDto.getPromotionMinu();
						
						OrderPromotionRuleDto orderPromotionBean=new OrderPromotionRuleDto();
						orderPromotionBean.setPromotionSum(new BigDecimal(promotionSum));
						orderPromotionBean.setPromotionMinu(promotionMinu);
						
						orderPromitionRuleList.add(orderPromotionBean);
					}
					
					orderPromotionDto.setPromotionRuleList(orderPromitionRuleList);	
					
					//该促销下的商品信息
				    List<String> productCodeList=promotionBean.getSpuCode();
				    if(!UtilHelper.isEmpty(productCodeList)){
				    	
				    	List<OrderPromotionProductDto> promotionProductDtoList=new ArrayList<OrderPromotionProductDto>();
				    	
				    	for(String spuCode : productCodeList){
				    		OrderPromotionProductDto productDto=new OrderPromotionProductDto();
				    		productDto.setSpuCode(spuCode);
				    		promotionProductDtoList.add(productDto);
				    	}
				    	
				    	orderPromotionDto.setPromotionProductDtoList(promotionProductDtoList);
				    }
					
				}
				
				returnMap.put(promotionId, orderPromotionDto);
				
			}
			
			return returnMap;
			
			
		 
		 
	}
	
	/**
	 * 获取订单中所有的促销ID
	 * @param allShoppingCart
	 * @return
	 */
	private List<Map<String,Object>> getAllOrderPromotionId(List<ShoppingCartListDto> allShoppingCart){
		
		if(UtilHelper.isEmpty(allShoppingCart)){
			return null;
		}
		
		
		List<Map<String,Object>> returnList=new ArrayList<Map<String,Object>>();
		
		for(ShoppingCartListDto shoppingCartDto : allShoppingCart){
			
			  UsermanageEnterprise sellerBean=shoppingCartDto.getSeller();
			  String supplyId=sellerBean.getEnterpriseId(); //提供商的id
			  
			 //该订单买的商品
			 List<ShoppingCartDto> buyProductList=shoppingCartDto.getShoppingCartDtoList();
			 
			  if(!UtilHelper.isEmpty(buyProductList)){
				  
				  for(ShoppingCartDto buyerProductBean : buyProductList){
					  
					  OrderProductInfoDto productInfoBean=new OrderProductInfoDto();
					  
					   String supCode=buyerProductBean.getSpuCode();
					   
					   productInfoBean.setSpuCode(supCode);
					   productInfoBean.setSellerCode(supplyId);
					  
					   Map<String,Object>  currentMap=new HashMap<String,Object>();
					   
					   List<String> promitionIdList=new ArrayList<String>();
						
						 String promotionIdList=buyerProductBean.getPromotionCollectionId(); //该商品参与的活动
						 
						  if(!UtilHelper.isEmpty(promotionIdList)){
							  
							  String[] promotionIdArray=promotionIdList.split(",");
							  for(String currentPromotionId : promotionIdArray){
								  promitionIdList.add(currentPromotionId);
							  }
						  }
						  
						  currentMap.put("productInfoBean", productInfoBean);
						  currentMap.put("promotionList", promitionIdList);
						  
						  returnList.add(currentMap);
				  }
			  }
		}
		return returnList;
	}
	
	
	/**
	 * 讲计算出来的商品均摊金额，合并到每个订单中，每个订单需要优惠多少金额
	 * @param allShoppingCart
	 */
	public Map<String,Object> processCalculationOrderShareMoney(List<ShoppingCartListDto> allShoppingCart){
		
		Map<String,Object> returnMap=new HashMap<String,Object>();
		if(UtilHelper.isEmpty(allShoppingCart)){
			returnMap.put("allShoppingCart", allShoppingCart);
			return returnMap;
		}
		
		//所有订单的优惠金额
		BigDecimal allOrderMoney=new BigDecimal(0);
		for(ShoppingCartListDto shoppingCartDto : allShoppingCart){
			
			BigDecimal orderMoney=new BigDecimal(0);
			
			List<ShoppingCartDto> buyerProductList=shoppingCartDto.getShoppingCartDtoList();
			
			 if(!UtilHelper.isEmpty(buyerProductList)){
				 for(ShoppingCartDto currentBuyProduct :  buyerProductList){
					 
					 BigDecimal shareMoney=currentBuyProduct.getShareMoney();
					 if(shareMoney!=null){
						 orderMoney=orderMoney.add(shareMoney);
					 }
				 }
			 }
			 
			 allOrderMoney=allOrderMoney.add(orderMoney);
			 shoppingCartDto.setOrderFullReductionMoney(orderMoney);
		}
		
		returnMap.put("allShoppingCart", allShoppingCart);
		returnMap.put("allOrderShareMoney", allOrderMoney);//所有订单的优惠金额
		
	    return returnMap;
		
	}
	
	
	/**
	 * 计算单品均摊金额
	 * @param siglePromotionMap
	 * @param responseMap
	 */
	private void calculationSiglePromotion(Map<Integer,List<ShoppingCartDto>> siglePromotionMap,Map<Integer,OrderPromotionDto> responseMap){
		 
		if(!UtilHelper.isEmpty(siglePromotionMap)){
			
			for(Integer currentPromotionId : siglePromotionMap.keySet()){
				
				//获取促销实体
				OrderPromotionDto promotionBean=responseMap.get(currentPromotionId);
				List<ShoppingCartDto> promotionProductList=siglePromotionMap.get(currentPromotionId);
				this.calculationPromotionShareService.calculationPromotionByPromitionBean(promotionBean, promotionProductList);
				
			}
			
		}
	}
	
	/**
	 * 计算多品均摊金额
	 * @param multiplePromotionMap
	 * @param responseMap
	 */
	private void calculatioMultiplePromotion(Map<Integer,List<ShoppingCartDto>> multiplePromotionMap,Map<Integer,OrderPromotionDto> responseMap){
	   
		if(!UtilHelper.isEmpty(multiplePromotionMap)){
			
			for(Integer currentPromotionId : multiplePromotionMap.keySet()){
				//获取促销实体
				OrderPromotionDto promotionBean=responseMap.get(currentPromotionId);
				List<ShoppingCartDto> promotionProductList=multiplePromotionMap.get(currentPromotionId);
				this.calculationPromotionShareService.calculationPromotionByPromitionBean(promotionBean, promotionProductList);
				
			}
			
		}
	}
	
	/**
	 * 计算参加满减活动的商品分摊金额
	 * 1.将参加的满减活动的商品拆分出单品满减和多品满减
	 * 2.分开计算单品和多品，多品的话需要均摊到每个商品，而单品不需要均摊
	 *
	 */
	public void calculationProductPromotionShareMoney(List<ShoppingCartDto> buyProductList,Map<Integer,OrderPromotionDto> responseMap){
		 if(UtilHelper.isEmpty(buyProductList)){
			 return;
		 }
		
		 //保存单品满减
		 Map<Integer,List<ShoppingCartDto>> siglePromotionMap=new HashMap<Integer,List<ShoppingCartDto>>();
		 
		 //保存多品满减,多品需要均摊价格
		 Map<Integer,List<ShoppingCartDto>> multiPromotionMap=new HashMap<Integer,List<ShoppingCartDto>>();
		 
		 for(ShoppingCartDto currentCartDto : buyProductList){
			 List<OrderPromotionDto> fullReductionPromotionList=currentCartDto.getFullReductionPromotionList();
			 
			 if(!UtilHelper.isEmpty(fullReductionPromotionList)){
				 
				 for(OrderPromotionDto promotionDto : fullReductionPromotionList){
					 
					 if(promotionDto.getPromotionType().intValue()==2){ //单品满减
						     if(siglePromotionMap.get(promotionDto.getPromotionId())!=null){
						    	 siglePromotionMap.get(promotionDto.getPromotionId()).add(currentCartDto);
						     }else{
						    	 List<ShoppingCartDto> sigleList=new ArrayList<ShoppingCartDto>();
						    	 sigleList.add(currentCartDto);
						    	 siglePromotionMap.put(promotionDto.getPromotionId(), sigleList);
						    	 
						     }
					 }
					 
					 if(promotionDto.getPromotionType().intValue()==3){//多品满减
					     if(multiPromotionMap.get(promotionDto.getPromotionId())!=null){
					    	  multiPromotionMap.get(promotionDto.getPromotionId()).add(currentCartDto);
					     }else{
					    	 List<ShoppingCartDto> multiList=new ArrayList<ShoppingCartDto>();
					    	 multiList.add(currentCartDto);
					    	 multiPromotionMap.put(promotionDto.getPromotionId(), multiList);
					    	 
					     }
					 }
					 
				 }
			 }
			 
		 }
		 
		 //计算单品满减
		 this.calculationSiglePromotion(siglePromotionMap, responseMap);
		 //计算多品满减
		 this.calculatioMultiplePromotion(multiPromotionMap, responseMap);
		
	}
	
	
	
	
	
	/**
	 * 将对应的返回促销的信息赋值给买的每个商品上去,也就是如果该商品参加了多个促销，那么需要将对应促销赋值到该购物商品的属性上去
	 * @param buyProductList
	 * @param responseMap
	 */
	private void processMakeUpShoppingCartDto(List<ShoppingCartDto> buyProductList,Map<Integer,OrderPromotionDto> responseMap,String custId){
		   if(!UtilHelper.isEmpty(buyProductList)){
			   
			   for(ShoppingCartDto currentCartDto : buyProductList){
				   
				   String promotionIdList=currentCartDto.getPromotionCollectionId();
				   if(!UtilHelper.isEmpty(promotionIdList)){
					   List<OrderPromotionDto> fullReductionPromotionList=new ArrayList<OrderPromotionDto>();
					   String[] currentPromotionIdArray=promotionIdList.split(",");
					   for(String innerPromotionId : currentPromotionIdArray){
						   
						   Integer promotionIdInteger=Integer.valueOf(innerPromotionId);
						   OrderPromotionDto orderPromotionDto=responseMap.get(promotionIdInteger);
						   
						   if(orderPromotionDto!=null){
							   
							   //验证该用户是否已经达到最大参与次数
							   if(orderPromotionDto.getLimitNum()!=null && orderPromotionDto.getLimitNum().intValue()!=-1){
								    
								    int partNum=this.getUserPartPromotionNum(custId, orderPromotionDto.getPromotionId());
								    if(partNum>=orderPromotionDto.getLimitNum().intValue()){
								       log.error("商品编码为supCode=["+currentCartDto.getSpuCode()+"],不能参加促销ID=["+orderPromotionDto.getPromotionId()+"],因为改用户已经参加了num次数=["+partNum+"],该促销限制次数为==["+orderPromotionDto.getLimitNum().intValue()+"]");
								       continue;
								    }
							   }
							   
							   boolean productPartPromotionState=this.checkProcutInfoPartPromotionState(currentCartDto.getSpuCode(), orderPromotionDto);
							   if(productPartPromotionState){
								   log.error("商品编码为supCode=["+currentCartDto.getSpuCode()+"],不能参加促销ID=["+orderPromotionDto.getPromotionId()+"],因为该商品已经从该促销中删除掉了!");
								   continue;
							   }
						    	Integer promotionType=orderPromotionDto.getPromotionType();
						    	if(promotionType!=null && (promotionType.intValue()==2 || promotionType.intValue()==3)){
						    		 //商品参加了单品满减或者多品满减
						    		 fullReductionPromotionList.add(orderPromotionDto);
						    	 }
						    	
						    }
						   
					   }
					   
					   currentCartDto.setFullReductionPromotionList(fullReductionPromotionList);
					   
				   }
				   
			   }
		   }
		
	}
	
	/**
	 * 检查该商品在该促销中是否已经删除掉了
	 * @param spuCode
	 * @param orderPromotionDto
	 * @return
	 */
	public boolean checkProcutInfoPartPromotionState(String spuCode,OrderPromotionDto orderPromotionDto){
		 boolean flag=true;
		 List<OrderPromotionProductDto> promotionProductDtoList=orderPromotionDto.getPromotionProductDtoList();
		 if(UtilHelper.isEmpty(promotionProductDtoList)){
			flag=true;
			return flag; 
		 }
		 
		 for(OrderPromotionProductDto currentProductBean : promotionProductDtoList){
			 String currentSupCode=currentProductBean.getSpuCode();
			 if(currentSupCode.equals(spuCode)){
				 flag=false;
			 }
		 }
		 return flag;
		 
		 
	}
	
	/**
	 * 获取用户参加的促销的次数,如果次数已经达到上限，那么是不能参加该促销的
	 * @param buyerBean
	 * @param promotionId
	 * @return
	 */
	public int getUserPartPromotionNum(String custId,Integer promotionId){
		
		Map<String,Object> paramterMap=new HashMap<String,Object>();
		paramterMap.put("custId",custId);
		paramterMap.put("promotionId",promotionId);
		
		OrderPromotionHistory promotionHistory=this.orderPromotionHistoryMapper.getObjectByCustIdAndPromotiondId(paramterMap);
		if(promotionHistory!=null){
			  if(promotionHistory.getUseNum()==null){
				  return 0;
			  }else{
				  return promotionHistory.getUseNum();
			  }
		}else{
			return 0;
		}
	}
	
	
	
	/**
	 * 
	 * @param buyProductList
	 * @param supplyId
	 * @param custId
	 * @return
	 */
	public Map<Integer,List<OrderProductInfoDto>> getPromotionParamter( List<ShoppingCartDto> buyProductList,String supplyId,String custId){
		
		 if(!UtilHelper.isEmpty(buyProductList)){
			
			Map<Integer,List<OrderProductInfoDto>> paramterMap=new HashMap<Integer,List<OrderProductInfoDto>>();
			
			
			for(ShoppingCartDto buyerProductBean : buyProductList){
				
				 String promotionIdList=buyerProductBean.getPromotionCollectionId(); //该商品参与的活动
				  if(!UtilHelper.isEmpty(promotionIdList)){
					  
					  String[] promotionIdArray=promotionIdList.split(",");
					  for(String currentPromotionId : promotionIdArray){
						  
						    Integer currentPromotionIntegerValue=Integer.valueOf(currentPromotionId);
						  
						   if(paramterMap.get(currentPromotionIntegerValue)!=null){
							   
							   OrderProductInfoDto currentPomotionBean=new OrderProductInfoDto();
							   currentPomotionBean.setSpuCode(buyerProductBean.getSpuCode());
							   paramterMap.get(currentPromotionIntegerValue).add(currentPomotionBean);
							   
						   }else{
							   
							   List<OrderProductInfoDto> promotionProductList=new ArrayList<OrderProductInfoDto>();
							   
							   OrderProductInfoDto currentPomotionBean=new OrderProductInfoDto();
							   currentPomotionBean.setSpuCode(buyerProductBean.getSpuCode());
							     promotionProductList.add(currentPomotionBean);
							     paramterMap.put(currentPromotionIntegerValue, promotionProductList);
							   
						   }
						  
					  }
					  
				  }
				
			}
			
			
			return paramterMap;
			
		}
		 
		 return null;
	}
	

}
