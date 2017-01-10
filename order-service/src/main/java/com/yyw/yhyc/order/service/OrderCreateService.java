package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omg.PortableInterceptor.INACTIVE;
import org.search.remote.yhyc.ProductSearchInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esotericsoftware.kryo.util.Util;
import com.search.model.yhyc.ProductDrug;
import com.search.model.yhyc.ProductPromotion;
import com.search.model.yhyc.ProductPromotionInfo;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.OrderProductInfoDto;
import com.yyw.yhyc.order.dto.OrderPromotionDetailDto;
import com.yyw.yhyc.order.dto.OrderPromotionDto;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.ShoppingCartFromWhereEnum;
import com.yyw.yhyc.order.manage.OrderManage;
import com.yyw.yhyc.order.manage.OrderPayManage;
import com.yyw.yhyc.order.mapper.OrderCombinedMapper;
import com.yyw.yhyc.order.mapper.OrderDeliveryDetailMapper;
import com.yyw.yhyc.order.mapper.OrderDeliveryMapper;
import com.yyw.yhyc.order.mapper.OrderDetailMapper;
import com.yyw.yhyc.order.mapper.OrderExceptionMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.order.mapper.OrderPayMapper;
import com.yyw.yhyc.order.mapper.OrderSettlementMapper;
import com.yyw.yhyc.order.mapper.OrderTraceMapper;
import com.yyw.yhyc.order.mapper.ShoppingCartMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.order.mapper.SystemPayTypeMapper;
import com.yyw.yhyc.product.bo.ProductInfo;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.product.dto.ProductPromotionDto;
import com.yyw.yhyc.product.manage.ProductInventoryManage;
import com.yyw.yhyc.product.mapper.ProductInfoMapper;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;
import com.yyw.yhyc.usermanage.mapper.UsermanageEnterpriseMapper;
import com.yyw.yhyc.usermanage.mapper.UsermanageReceiverAddressMapper;
/**
 * 订单的创建业务处理
 * @author wangkui01
 *
 */
@Service("orderCreateService")
public class OrderCreateService {
	
	private Log log = LogFactory.getLog(OrderCreateService.class);
	
	@Autowired
	private OrderMapper	orderMapper;
	@Autowired
	private SystemPayTypeMapper systemPayTypeMapper;
	@Autowired
	private SystemDateMapper systemDateMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	@Autowired
	private OrderDeliveryDetailMapper orderDeliveryDetailMapper;
	@Autowired
	private OrderDeliveryMapper orderDeliveryMapper;
	@Autowired
	private OrderTraceMapper orderTraceMapper;
	@Autowired
	private OrderPayMapper orderPayMapper;
	@Autowired
	private OrderCombinedMapper orderCombinedMapper;
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	@Autowired
	private ProductInfoMapper productInfoMapper;
	@Autowired
	private OrderSettlementMapper orderSettlementMapper;
	@Autowired
	private UsermanageReceiverAddressMapper receiverAddressMapper;
	@Autowired
	private UsermanageEnterpriseMapper enterpriseMapper;
	@Autowired
	private OrderExceptionMapper  orderExceptionMapper;
	@Autowired
	private OrderPayManage orderPayManage;
	@Autowired
	private OrderTraceService orderTraceService;
	@Autowired
	private ProductInventoryManage productInventoryManage;
	@Autowired
	private  OrderSettlementService orderSettlementService;
	@Autowired
	private OrderManage orderManage;
	@Autowired
	private OrderFullReductionService orderFullReductionService;
	
	/**
	 * 该方法只用来处理创建订单的时候，校验订单的合法性
	 * @param userDto
	 * @param orderDto
	 * @param iCustgroupmanageDubbo
	 * @param productDubboManageService
	 * @param productSearchInterface
	 * @param iPromotionDubboManageService
	 * @return
	 */
	public Map<String,Object> validateProducts(UserDto userDto, OrderDto orderDto,
			   ICustgroupmanageDubbo iCustgroupmanageDubbo, IProductDubboManageService productDubboManageService,
			   ProductSearchInterface productSearchInterface,IPromotionDubboManageService iPromotionDubboManageService){
		
		/* 区分是来自进货单的商品还是极速下单的商品 */
		int productFromFastOrderCount = 0;

		/* 批量搜索商品详情信息的数据组装 */
		Set<String> productCodeSet = new HashSet<String>();

		if( ! UtilHelper.isEmpty(orderDto) &&  !UtilHelper.isEmpty(orderDto.getProductInfoDtoList())){
			for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()) {
				if (UtilHelper.isEmpty(productInfoDto)) continue;
				if (productInfoDto.getFromWhere() != null && ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere() == productInfoDto.getFromWhere()) {
					productFromFastOrderCount ++;
				}
				if(!UtilHelper.isEmpty(productInfoDto.getSpuCode()) && !UtilHelper.isEmpty(orderDto.getSupplyId()) && orderDto.getSupplyId() > 0){
					productCodeSet.add(productInfoDto.getSpuCode() + "-" + orderDto.getSupplyId() );
				}
			}
		}
		log.info("统一校验订单商品接口：区分是来自进货单的商品还是极速下单的商品，productFromFastOrderCount=" + productFromFastOrderCount );

		Map<String,Object> map = new HashMap<String, Object>();

		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList()) ){
			log.info("统一校验订单商品接口-当前登陆人的信息userDto=" + userDto +",orderDto=" + orderDto);
			return returnFalse("非法参数",productFromFastOrderCount);
		}

		UsermanageEnterprise buyer = enterpriseMapper.getByEnterpriseId(orderDto.getCustId() + "");
		UsermanageEnterprise seller = enterpriseMapper.getByEnterpriseId(orderDto.getSupplyId() + "");
		if(UtilHelper.isEmpty(buyer) || UtilHelper.isEmpty(seller)){
			return returnFalse("非法参数",productFromFastOrderCount);
		}

		/* 校验要采购商与供应商是否相同 */
		if (orderDto.getSupplyId().equals(userDto.getCustId()) ){
			log.info("统一校验订单商品接口 ：不能购买自己的商品" );
			return returnFalse("不能购买自己的商品",productFromFastOrderCount);
		}


		if(UtilHelper.isEmpty(productDubboManageService)){
			log.error("统一校验订单商品接口,查询商品价格，productDubboManageService = " + productDubboManageService);
			return returnFalse("请稍后再试",productFromFastOrderCount);
		}

		if(UtilHelper.isEmpty(iCustgroupmanageDubbo) || UtilHelper.isEmpty(productSearchInterface)){
			log.error("统一校验订单商品接口,Dubbo服务异常 iCustgroupmanageDubbo =  " + iCustgroupmanageDubbo +",productSearchInterface=" + productSearchInterface);
			return returnFalse("请稍后再试",productFromFastOrderCount);
		}

		String custGroupCode = orderManage.getCustGroupCode(orderDto.getCustId(),iCustgroupmanageDubbo);
		Map<String,ProductDrug> productDrugMap = orderManage.searchBatchProduct(productSearchInterface,orderDto.getCustId()+"",custGroupCode,productCodeSet);
		log.info("统一校验订单商品接口,批量搜索商品详情信息 , productDrugMap = " + productDrugMap);
		if(UtilHelper.isEmpty(productDrugMap)){
			log.error("统一校验订单商品接口,批量搜索商品详情信息失败..... , productDrugMap = " + productDrugMap);
			return returnFalse("请稍后再试",productFromFastOrderCount);
		}


		/* 该供应商下所有商品的总金额（用于判断是否符合供应商的订单起售金额） */
		BigDecimal productPriceCount = new BigDecimal(0);

		//校验库存数量，是否可以购买
		ProductInventory productInventory = new ProductInventory();
		productInventory.setSupplyId(orderDto.getSupplyId());
		String productFromWhere = "进货单";

		String productCodeKey = "";
		ProductDrug productDrug = null;
		for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()){
			if(UtilHelper.isEmpty(productInfoDto) || UtilHelper.isEmpty(productInfoDto.getSpuCode())){
				continue;
			}

			if(productInfoDto.getFromWhere() != null && ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere() == productInfoDto.getFromWhere()){
				productFromWhere = "极速下单页";
			}

			/* 校验商品详细信息 */
			productCodeKey =  productInfoDto.getSpuCode() + "-" + orderDto.getSupplyId();
			productDrug = productDrugMap.get(productCodeKey);
			log.info("统一校验订单商品接口,查询商品详细信息，productDrug = " + productDrug);
			if(UtilHelper.isEmpty(productDrug) || ! productInfoDto.getSpuCode().equals(productDrug.getSpu_code())){
				log.error("统一校验订单商品接口,查询商品(spuCode=" + productInfoDto.getSpuCode() +",supplyId=" + orderDto.getSupplyId() + ")详细信息失败.....");
				return returnFalse("查询商品详细信息失败",productFromFastOrderCount);
			}

			/* 检查库存 */
			int stockAmount = UtilHelper.isEmpty(productDrug.getStock_amount()) ? 0 : Integer.valueOf(productDrug.getStock_amount());
			int minimumPacking = UtilHelper.isEmpty(productDrug.getMinimum_packing()) ? 1 : Integer.valueOf( productDrug.getMinimum_packing());
			if(stockAmount <= 0 || stockAmount < minimumPacking || stockAmount < productInfoDto.getProductCount()){
				log.info("统一校验订单商品接口 ：商品(spuCode=" + productInfoDto.getSpuCode() + ")库存校验失败! " +
						"\n stockAmount = " + stockAmount + ",minimumPacking=" + minimumPacking + ",productInfoDto.getProductCount()=" + productInfoDto.getProductCount() );
				return returnFalse("您的进货单中，有部分商品缺货或下架了，请返回" + productFromWhere + "查看",productFromFastOrderCount);
			}

			/* 查询商品上下架状态 */
			int status = UtilHelper.isEmpty(productDrug.getState()) ? 0 : Integer.valueOf( productDrug.getState());
			int isChannel = UtilHelper.isEmpty(productDrug.getIs_channel()) ? 0 : Integer.valueOf( productDrug.getIs_channel());
			if( 1 != status ){
				log.info("统一校验订单商品接口-查询商品上下架状态,status:" + status + ",// 0未上架  1上架  2本次下架  3非本次下架");
				return returnFalse("您的进货单中，有部分商品缺货或下架了，请返回" + productFromWhere + "查看",productFromFastOrderCount);
			}
			productInfoDto.setIsChannel(isChannel);

			/* 查询价格 */
			BigDecimal productPrice = null;
			productPrice = UtilHelper.isEmpty(productDrug.getShowPrice()) ? new BigDecimal(0) : new BigDecimal(productDrug.getShowPrice() + "");
			log.info("统一校验订单商品接口-查询的商品价格 productPrice = " + productPrice);


			/* 若商品的最新价格 小于等于0，则提示该商品无法购买 */
			if(productPrice.compareTo(new BigDecimal(0)) <= 0){
				log.error("统一校验订单商品接口-查询到的商品价格异常 productPrice = " + productPrice);
				updateProductPrice(userDto,orderDto.getSupplyId(),productInfoDto.getSpuCode(),productPrice,productInfoDto.getFromWhere(),productInfoDto.getPromotionId());
				return returnFalse("部分商品您无法购买，请返回" + productFromWhere + "查看",productFromFastOrderCount);
			}
			/* 若商品价格变动，则不让提交订单，且更新进货单里相关商品的价格 */
			if(UtilHelper.isEmpty(productInfoDto.getPromotionId()) || productInfoDto.getPromotionId() <= 0){
				if( productPrice.compareTo(productInfoDto.getProductPrice()) != 0){
					log.error("统一校验订单商品接口-存在价格变化的商品,查询到的价格productPrice = " + productPrice +",记录的商品价格=" + productInfoDto.getProductPrice());
					updateProductPrice(userDto,orderDto.getSupplyId(),productInfoDto.getSpuCode(),productPrice,productInfoDto.getFromWhere(),productInfoDto.getPromotionId());
					return returnFalse("存在价格变化的商品，请返回" + productFromWhere + "重新结算",productFromFastOrderCount);
				}
			}
			
			/*************以下验证商品的满减促销最大力度是否发生了变化****************/
			Set<ProductPromotionInfo> currentPromotionInfoList=productDrug.getProductPromotionInfos();
			if(currentPromotionInfoList!=null && currentPromotionInfoList.size()>0){
				StringBuilder promotionIdString=new StringBuilder();
				StringBuilder promotionNameString=new StringBuilder();
				
				Iterator<ProductPromotionInfo> iteratorList=currentPromotionInfoList.iterator();
				int size=currentPromotionInfoList.size();
				int i=0;
				while(iteratorList.hasNext()){
					ProductPromotionInfo fullDescPromotion=iteratorList.next();
					String currentPromotionId=fullDescPromotion.getPromotion_id();
					String currentPromotionName=fullDescPromotion.getPromotion_name();
					if(i!=size-1){
						promotionIdString.append(currentPromotionId+",");
						promotionNameString.append(currentPromotionName+",");
					}else{
						promotionIdString.append(currentPromotionId);
						promotionNameString.append(currentPromotionName);
					}
					i++;
				}
				log.info("当前商品从搜索接口获取到的最大优惠力度的满减促销id=="+promotionIdString.toString());
				
				if(!UtilHelper.isEmpty(productInfoDto.getPromotionCollectionId())){
					//不相等，说明该商品的满减促销的id发生了变化，需要重新下单
					 if(!productInfoDto.getPromotionCollectionId().equals(promotionIdString.toString())){
						     log.error("校验满减促销最大优惠力度,促销的id发生了变化,原来的id="+productInfoDto.getPromotionCollectionId()+" 变化后的id="+promotionIdString.toString());
						     updateProductPromotionCollectionId(userDto,orderDto.getSupplyId(),productInfoDto.getSpuCode(),productInfoDto.getFromWhere(),promotionNameString.toString(),promotionIdString.toString());
							return returnFalse("存在满减促销变化的商品,请返回" + productFromWhere + "重新结算",productFromFastOrderCount);
					 }
				}
			}
			
			

			/* 校验活动商品相关的限购逻辑 */
			ProductPromotion productPromotion = productDrug.getProductPromotion();
			if(!UtilHelper.isEmpty(productPromotion)){
				OrderPromotionDto currentDto=new OrderPromotionDto();
				currentDto.setPromotionName(productPromotion.getPromotion_name());
				currentDto.setPromotionId(Integer.valueOf(productPromotion.getPromotion_id()));
				orderDto.setSpecialPromotionDto(currentDto);
			}

			/* 如果常规商品这种状态是正常的，或者活动商品的活动信息是有效的，则统计该供应商下的已买商品总额 */
			if(  1 == status && productPrice.compareTo(new BigDecimal(0)) > 0 || (!UtilHelper.isEmpty(productPromotion) && new BigDecimal("0").compareTo(productPromotion.getPromotion_price()) < 0 )){
				productPriceCount = productPriceCount.add( productInfoDto.getProductPrice().multiply(new BigDecimal(productInfoDto.getProductCount())) );
			}

			if(UtilHelper.isEmpty(productInfoDto.getPromotionId()) || productInfoDto.getPromotionId() < 0){
				continue;
			}

			/* 1、 非空校验*/
			if( !UtilHelper.isEmpty(productInfoDto.getPromotionId()) && productInfoDto.getPromotionId() > 0 &&  UtilHelper.isEmpty(productPromotion)){
				return returnFalse("商品("+ productInfoDto.getProductName() +")参加的活动已失效",productFromFastOrderCount);
			}


			/*商品的活动价格是否发生变化*/
			if(!UtilHelper.isEmpty(productInfoDto.getProductPrice()) && productInfoDto.getProductPrice().compareTo(productPromotion.getPromotion_price()) != 0){
				updateProductPrice(userDto,orderDto.getSupplyId(),productInfoDto.getSpuCode(),productPromotion.getPromotion_price(),productInfoDto.getFromWhere(),productInfoDto.getPromotionId());
				return returnFalse("商品的活动价格发生变化，请返回" + productFromWhere + "重新结算",productFromFastOrderCount);
			}


			/* 2、 校验 购买活动商品的数量 是否合法 */
			if(productPromotion.getLimit_num() > 0 && productInfoDto.getProductCount() < productPromotion.getMinimum_packing()){
				/* productPromotionDto.getLimitNum() == -1 时表示不限购 */
				return returnFalse("购买活动商品的数量("+ productInfoDto.getProductCount() +")低于最小起批量(" + productPromotion.getMinimum_packing() + ")",productFromFastOrderCount);
			}

			/* 3、查询该商品在该活动中的历史购买量*/
			int buyedInHistory = 0;
			if(!UtilHelper.isEmpty(productInfoDto.getPromotionId()) && productInfoDto.getPromotionId()>0){
				ShoppingCart shoppingCart  = new ShoppingCart();
				shoppingCart.setCustId(Integer.parseInt(buyer.getEnterpriseId()));
				shoppingCart.setSupplyId(Integer.parseInt(seller.getEnterpriseId()));
				shoppingCart.setSpuCode(productInfoDto.getSpuCode());
				shoppingCart.setProductId(productInfoDto.getId());
				shoppingCart.setPromotionId(productInfoDto.getPromotionId());
				buyedInHistory  = shoppingCartMapper.countBuyedNumInHistory(shoppingCart);
			}

			/* 4、判断理论上可以以特价购买的数量 */
			int canBuyByPromotionPrice = productPromotion.getLimit_num() - buyedInHistory;
			if (productPromotion.getLimit_num() > 0 && canBuyByPromotionPrice <= 0){
				/* productPromotionDto.getLimitNum() == -1 时表示不限购 */
				return  returnFalse("该活动商品每人限购"+productPromotion.getLimit_num() +"件,您已购买了" + buyedInHistory +
						"件,还能购买" + canBuyByPromotionPrice +"件。",productFromFastOrderCount);
			}

			/* 活动实时库存字段，如果走搜索接口，可能会有问题(比如该字段同步失败)。所以改用活动的dubbo接口去获取该字段的值 */
			ProductPromotionDto temp = orderManage.queryProductWithPromotion(iPromotionDubboManageService,productInfoDto.getSpuCode(),orderDto.getSupplyId()+"",productInfoDto.getPromotionId(),orderDto.getCustId()+"");
			productPromotion.setCurrent_inventory(orderManage.getPromotionCurrentInventory(temp));

			/* 5、若还能以特价购买，则根据活动实时库存判断能买多少 */

			if(productPromotion.getLimit_num() > 0){ //如果有个人限购
				if(productPromotion.getCurrent_inventory() - canBuyByPromotionPrice <= 0 ){
					if(productInfoDto.getProductCount() > productPromotion.getCurrent_inventory()  ){
						return returnFalse("本次购买的活动商品数量已超过活动实时库存",productFromFastOrderCount);
					}
				}else{
					if(productInfoDto.getProductCount() > canBuyByPromotionPrice ){
						return returnFalse("本次购买的活动商品数量已超过个人限购数量",productFromFastOrderCount);
					}
				}
			}else{ //如果不限购
				if(productInfoDto.getProductCount() > productPromotion.getCurrent_inventory()  ){
					return returnFalse("本次购买的活动商品数量已超过活动实时库存",productFromFastOrderCount);
				}
			}
		}
		log.info("统一校验订单商品接口:供应商[" + seller.getEnterpriseName() + "]("+seller.getEnterpriseId()+")的订单起售金额=" + seller.getOrderSamount() + ",在该供应商下购买的商品总额=" + productPriceCount);

		if(!UtilHelper.isEmpty(seller.getOrderSamount()) && productPriceCount.compareTo(seller.getOrderSamount()) < 0 ){
			return returnFalse("你有部分商品金额低于供货商的发货标准，此商品无法结算",productFromFastOrderCount);
		}
		
		/********************以下处理验证商品参加满减活动的验证**********************/
		
		Map<String,Object> returnResult=this.processValidateFullDesc(userDto, orderDto, productFromFastOrderCount,iPromotionDubboManageService);
		
		if(!UtilHelper.isEmpty(returnResult)){
			return returnResult;
		}

		log.info("统一校验订单商品接口 ：校验成功" );
		map.put("result", true);
		return map;
	}
	
	/**
	 * 验证商品参加的满减促销需要减的钱
	 * @param userDto
	 * @param orderDto
	 * @param productFromFastOrderCount
	 */
	private Map<String,Object> processValidateFullDesc(UserDto userDto, OrderDto orderDto,int productFromFastOrderCount,IPromotionDubboManageService promotionDubboManageService){
		
		
		Map<String,Object> returnResult=null;
		
		Integer supplyId=orderDto.getSupplyId();
		Integer custId=userDto.getCustId();
		
		List<Map<String,Object>> returnList=new ArrayList<Map<String,Object>>();
		
		//组装该订单商品是否参加了满减促销活动和其他活动
		for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()){
			
			OrderProductInfoDto productInfoParamterBean=new OrderProductInfoDto();
			  
			   String supCode=productInfoDto.getSpuCode();
			   
			   productInfoParamterBean.setSpuCode(supCode);
			   productInfoParamterBean.setSellerCode(orderDto.getSupplyId()+"");
			   
			  /* Map<String,Object>  productInfoParamterBeanMap=new HashMap<String,Object>();
			   productInfoParamterBeanMap.put("spuCode", supCode);
			   productInfoParamterBeanMap.put("sellerCode", orderDto.getSupplyId()+"");*/
			   
			   Map<String,Object>  currentMap=new HashMap<String,Object>();
			   
			   List<String> promitionIdList=new ArrayList<String>();
				
				 String promotionIdList=productInfoDto.getPromotionCollectionId(); //该商品参与的活动
				 
				  if(!UtilHelper.isEmpty(promotionIdList)){
					  
					  String[] promotionIdArray=promotionIdList.split(",");
					  for(String currentPromotionId : promotionIdArray){
						  promitionIdList.add(currentPromotionId);
					  }
					  
					  currentMap.put("productInfoBean", productInfoParamterBean);
					  currentMap.put("promotionList", promitionIdList);
					  returnList.add(currentMap);
				  }
				  
				 
		}
		if(UtilHelper.isEmpty(returnList)){
			return null;
		}
		
		//1.以下是处理该笔订单参与了商品的满减活动,将组装好的请求参数掉dubbo服务，然后组装成key:促销id,value:促销的实体
		 Map<Integer,OrderPromotionDto> responseMap=this.orderFullReductionService.processPormotionDubboMethodByAllPromotionCollection(returnList,promotionDubboManageService);
		
		 //2.将productInfoDt转换成对应的shoppingCartDto
		 List<ShoppingCartDto> returnShoppingCartDtoList=this.productInfoConvertShoppingCartDto(orderDto.getProductInfoDtoList(), supplyId, custId);
		 
		 
		 //3.将该笔验证订单中商品组装成key:促销id,value:商品集合,为后面的计算做准备
		 Map<Integer,List<OrderProductInfoDto>> currentOrderParamterMap=this.orderFullReductionService.getPromotionParamter(returnShoppingCartDtoList, supplyId+"", custId+"");
		
		 
		 //4.开始校验和计算金额
		 if(currentOrderParamterMap!=null && currentOrderParamterMap.keySet().size()>0){
			   
			      if(!UtilHelper.isEmpty(responseMap)){
			    	  //开始校验
			    	  returnResult=this.processMakeUpShoppingCartDto(returnShoppingCartDtoList, responseMap,custId+"",productFromFastOrderCount);
			    	  
			    	  if(returnResult!=null){
			    		  return returnResult;
			    	  }
			    	  
			    	  this.orderFullReductionService.calculationProductPromotionShareMoney(returnShoppingCartDtoList, responseMap);
			    	   //计算该笔订单的总优惠金额
		    	     BigDecimal orderMoney=new BigDecimal(0);
					 if(!UtilHelper.isEmpty(returnShoppingCartDtoList)){
						 for(ShoppingCartDto currentBuyProduct :  returnShoppingCartDtoList){
							 Integer productCount=currentBuyProduct.getProductCount();
							 BigDecimal productPrice=currentBuyProduct.getProductPrice();
							 String productName=currentBuyProduct.getProductName();
							 
							 BigDecimal currentProductMoney=productPrice.multiply(new BigDecimal(productCount));
							 
							 BigDecimal shareMoney=currentBuyProduct.getShareMoney();
							 if(shareMoney!=null){
								 orderMoney=orderMoney.add(shareMoney);
								 if(currentProductMoney.compareTo(shareMoney)<=0){
									 returnResult=returnFalse("名称为["+productName+"]的商品优惠后的金额不能小于等于零,请重新下单",productFromFastOrderCount);
									 break;
								 }
							 }
							
						 }
					 }
					 
					  if(returnResult!=null){
			    		  return returnResult;
			    	  }
					 
					 BigDecimal orderFullReductionMoney=orderDto.getOrderFullReductionMoney();
					 if(orderFullReductionMoney!=null && orderFullReductionMoney.compareTo(orderMoney)!=0){
						 returnResult=returnFalse("优惠金额有问题,请重新下单",productFromFastOrderCount);
						 return returnResult;
					 }
					 
					 //该笔订单总金额不能和优惠总金额相等
					 if(!UtilHelper.isEmpty(orderDto.getProductInfoDtoList())){
						 
							BigDecimal orderTotal = new BigDecimal(0);
							
						 for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()){
								if(null == productInfoDto){
									continue;
								}
							 orderTotal = orderTotal.add(productInfoDto.getProductPrice().multiply(new BigDecimal(productInfoDto.getProductCount())));
					    }
						 
							if(!UtilHelper.isEmpty(orderTotal)){
								orderTotal = orderTotal.setScale(2,BigDecimal.ROUND_HALF_UP);
								if(orderTotal.compareTo(orderMoney)<=0){
									 returnResult=returnFalse("优惠后的订单金额不能为零,请重新下单",productFromFastOrderCount);
									 return returnResult;
								}
						   }
					 }
					  
					 if(returnResult==null){ //当前验证无错误,将shoppingCart的promotionDetailInfoList 赋值给对应的ProductInfoDto
						 if(!UtilHelper.isEmpty(returnShoppingCartDtoList)){
							 
							 for(ShoppingCartDto currentDto :  returnShoppingCartDtoList){
								 
								  List<OrderPromotionDetailDto> detailList=currentDto.getPromotionDetailInfoList();
								     String spuCode=currentDto.getSpuCode();
								     for(ProductInfoDto currentProductInfo : orderDto.getProductInfoDtoList()){
								    	 
								    	   String currentSpuCode=currentProductInfo.getSpuCode();
								    	    if(currentSpuCode.equals(spuCode)){
								    	    	currentProductInfo.setPromotionDetailInfoList(detailList);
								    	    }
								    	 
								     }
								  
								  
							 }
						 }
						 
					 }
			      }
			   
		   }
		 
		 return returnResult;
		
		
		
	}
	
	
	
	
	
	/**
	 * 将对应的返回促销的信息赋值给买的每个商品上去,也就是如果该商品参加了多个促销，那么需要将对应促销赋值到该购物商品的属性上去
	 * @param buyProductList
	 * @param responseMap
	 */
	private Map<String,Object> processMakeUpShoppingCartDto(List<ShoppingCartDto> buyProductList,Map<Integer,OrderPromotionDto> responseMap,String custId,int productFromFastOrderCount){
		 
		Map<String,Object> returnResult=null;
		
		if(!UtilHelper.isEmpty(buyProductList)){
			   
			   for(ShoppingCartDto currentCartDto : buyProductList){
				   
				   String promotionIdList=currentCartDto.getPromotionCollectionId();
				   
				    boolean flag=false;
				    
				   if(!UtilHelper.isEmpty(promotionIdList)){
					   List<OrderPromotionDto> fullReductionPromotionList=new ArrayList<OrderPromotionDto>();
					   String[] currentPromotionIdArray=promotionIdList.split(",");
					   
					   
					   for(String innerPromotionId : currentPromotionIdArray){
						   
						   Integer promotionIdInteger=Integer.valueOf(innerPromotionId);
						   OrderPromotionDto orderPromotionDto=responseMap.get(promotionIdInteger);
						   
						   if(orderPromotionDto!=null){
							   
							   //验证该用户是否已经达到最大参与次数
							   if(orderPromotionDto.getLimitNum()!=null && orderPromotionDto.getLimitNum().intValue()!=-1){
								    int partNum=this.orderFullReductionService.getUserPartPromotionNum(custId, orderPromotionDto.getPromotionId());
								    if(partNum>=orderPromotionDto.getLimitNum().intValue()){
								       log.error("商品名称["+currentCartDto.getProductName()+"],不能参加促销ID=["+orderPromotionDto.getPromotionId()+"],因为改用户已经参加了num次数=["+partNum+"],该促销限制次数为==["+orderPromotionDto.getLimitNum().intValue()+"]");
								       this.updateShoppingCartDeletePromotionInfo(currentCartDto.getShoppingCartId(), orderPromotionDto);
								       returnResult=returnFalse("[商品名称["+currentCartDto.getProductName()+"],不能参加"+orderPromotionDto.getPromotionName()+"促销],因为改用户已经参加了num次数=["+partNum+"],该促销限制次数为==["+orderPromotionDto.getLimitNum().intValue()+"]",productFromFastOrderCount);
								       flag=true;
								       break;
								    }
							   }
							   
							   boolean productPartPromotionState=this.orderFullReductionService.checkProcutInfoPartPromotionState(currentCartDto.getSpuCode(), orderPromotionDto);
							   if(productPartPromotionState){
								   log.error("商品编码为supCode=["+currentCartDto.getSpuCode()+"],不能参加促销ID=["+orderPromotionDto.getPromotionId()+"],因为该商品已经从该促销中删除掉了!");
								   this.updateShoppingCartDeletePromotionInfo(currentCartDto.getShoppingCartId(), orderPromotionDto);
								   returnResult=returnFalse("[商品"+currentCartDto.getProductName()+"],不能参加"+orderPromotionDto.getPromotionName()+"促销],因为该商品已经从该促销中删除掉了!",productFromFastOrderCount);
							       flag=true;
							       break;
							   }
						    	Integer promotionType=orderPromotionDto.getPromotionType();
						    	if(promotionType!=null && (promotionType.intValue()==2 || promotionType.intValue()==3)){
						    		 //商品参加了单品满减或者多品满减
						    		 fullReductionPromotionList.add(orderPromotionDto);
						    	 }
						    	
						    }
						   
					   }
					   if(flag){
						   break;
					   }
					   
					   currentCartDto.setFullReductionPromotionList(fullReductionPromotionList);
					   
				   }
				   
			   }
		   }
		
		
		return returnResult;
		
	}
	
	/**
	 * 当发现商品已经不属于该促销的时候，需要从购物车里面删除掉
	 * @param shoppingCartId
	 * @param orderPromotionDto
	 */
	private void updateShoppingCartDeletePromotionInfo(Integer shoppingCartId,OrderPromotionDto orderPromotionDto){
		
		ShoppingCart bean=this.shoppingCartMapper.getByPK(shoppingCartId);
		 if(UtilHelper.isEmpty(bean)){
			 return;
		 }
		Integer promotionId=orderPromotionDto.getPromotionId();
		String promotionCollectionId=bean.getPromotionCollectionId();
		
		if(!UtilHelper.isEmpty(promotionCollectionId)){
			String[] promotionIdArray=promotionCollectionId.split(",");
			
			if(UtilHelper.isEmpty(promotionIdArray)){
				return;
			}
			List<String> tempList=new ArrayList<String>();
			for(String currentPromotionId : promotionIdArray){
				  String temp=promotionId.toString();
				  if(!temp.equals(currentPromotionId)){
					  tempList.add(currentPromotionId);
				  }
			}
			
			if(!UtilHelper.isEmpty(tempList)){
				StringBuffer sql=new StringBuffer();
				for(int i=0;i<tempList.size();i++){
					
					String value=tempList.get(i);
					if(i!=tempList.size()-1){
						sql.append(value+",");
					}else{
						sql.append(value);
					}
				}
				
				if(sql.length()>0){
					log.info("更新购物车中的促销promotionCollectionId==="+sql.toString());
					bean.setPromotionCollectionId(sql.toString());
					this.shoppingCartMapper.update(bean);
				}
			}else{
				Map<String,Object> paramter=new HashMap<String,Object>();
				paramter.put("shopppingCartId",bean.getShoppingCartId());
				this.shoppingCartMapper.updateShoppingCartPromotionCollectionId(paramter);
			}
		}
		
	}
	
	/**
	 * 将productInfoDto转换成对应的shoppingCartDTo，然后获取对应的促销key-vaue商品信息
	 * @param productInfoList
	 * @param supplyId
	 * @param custId
	 * @return
	 */
	private  List<ShoppingCartDto> productInfoConvertShoppingCartDto(List<ProductInfoDto> productInfoList,Integer supplyId,Integer custId){
		      
		List<ShoppingCartDto> returnShoppingCartDtoList=new ArrayList<ShoppingCartDto>();
		      
		      for(ProductInfoDto currentBean : productInfoList){
		    	  
		    	  ShoppingCartDto cartDto=new ShoppingCartDto();
		    	  cartDto.setProductCount(currentBean.getProductCount());
		    	  cartDto.setProductPrice(currentBean.getProductPrice());
		    	  cartDto.setProductName(currentBean.getProductName());
		    	  cartDto.setSpuCode(currentBean.getSpuCode());
		    	  cartDto.setPromotionCollectionId(currentBean.getPromotionCollectionId());
		    	  cartDto.setShoppingCartId(currentBean.getShoppingCartId());
		    	  returnShoppingCartDtoList.add(cartDto);
		    	  
		      }
		      
		 return returnShoppingCartDtoList;
	}
	
	private Map<String,Object> returnFalse(String message,Integer productFromFastOrderCount){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("result", false);
		map.put("message", message);
		if(UtilHelper.isEmpty(productFromFastOrderCount) || productFromFastOrderCount <= 0){
			/* 跳转到进货单 */
			map.put("goToShoppingCart", true);
		}else{
			/* 跳转到极速下单 */
			map.put("goToFastOrder", true);
		}
		return map;
	}
	
	/**
	 * 提交订单时，若商品价格变动，则不让提交订单，且更新进货单里相关商品的价格
	 * @param userDto
	 * @param supplyId
	 * @param spuCode
	 * @param newProductPrice
	 * @param fromWhere         	购物车中商品来源
	 * @param promotionId        商品参加活动的id
	 */
	private void updateProductPrice(UserDto userDto, Integer supplyId, String spuCode, BigDecimal newProductPrice, Integer fromWhere,Integer promotionId){
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(userDto.getCustId());
		shoppingCart.setSupplyId(supplyId);
		shoppingCart.setSpuCode(spuCode);
		if(!UtilHelper.isEmpty(promotionId)){
			shoppingCart.setPromotionId(promotionId);
		}
		if( !UtilHelper.isEmpty(fromWhere) && (ShoppingCartFromWhereEnum.SHOPPING_CART.getFromWhere() == fromWhere || ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere() == fromWhere)){
			shoppingCart.setFromWhere(fromWhere);
		}

		List<ShoppingCart> shoppingCartList = shoppingCartMapper.listByProperty(shoppingCart);
		if(!UtilHelper.isEmpty(shoppingCartList) && shoppingCartList.size() == 1){
			shoppingCart = shoppingCartList.get(0);
			shoppingCart.setProductPrice(newProductPrice);
			shoppingCart.setProductSettlementPrice(newProductPrice.multiply(new BigDecimal(shoppingCart.getProductCount())));
			shoppingCart.setUpdateUser(userDto.getUserName());
			shoppingCart.setUpdateTime(systemDateMapper.getSystemDate());
			log.info("统一校验订单商品接口,查询商品价格后价格发生变化，更新数据：" + shoppingCart);
			shoppingCartMapper.update(shoppingCart);
		}
	}
	
	private void updateProductPromotionCollectionId(UserDto userDto, Integer supplyId, String spuCode, Integer fromWhere,String promotionName,String promotionCollectionId){
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(userDto.getCustId());
		shoppingCart.setSupplyId(supplyId);
		shoppingCart.setSpuCode(spuCode);
		if( !UtilHelper.isEmpty(fromWhere) && (ShoppingCartFromWhereEnum.SHOPPING_CART.getFromWhere() == fromWhere || ShoppingCartFromWhereEnum.FAST_ORDER.getFromWhere() == fromWhere)){
			shoppingCart.setFromWhere(fromWhere);
		}

		List<ShoppingCart> shoppingCartList = shoppingCartMapper.listByProperty(shoppingCart);
		if(!UtilHelper.isEmpty(shoppingCartList) && shoppingCartList.size() == 1){
			shoppingCart = shoppingCartList.get(0);
			if(!UtilHelper.isEmpty(promotionCollectionId)){
				shoppingCart.setPromotionCollectionId(promotionCollectionId);
			}
			if(!UtilHelper.isEmpty(promotionName)){
				shoppingCart.setPromotionName(promotionName);
			}
			shoppingCart.setUpdateUser(userDto.getUserName());
			shoppingCart.setUpdateTime(systemDateMapper.getSystemDate());
			log.info("校验商品的满减促销最大优惠力度的变化后的购物车信息：" + shoppingCart);
			shoppingCartMapper.update(shoppingCart);
		}
	}

}
