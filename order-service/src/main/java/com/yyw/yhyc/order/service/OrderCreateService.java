package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.remote.yhyc.ProductSearchInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.search.model.yhyc.ProductDrug;
import com.search.model.yhyc.ProductPromotion;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.dto.OrderDto;
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

		ProductInfo productInfo = null;
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
			if(UtilHelper.isEmpty(productInfoDto.getSpecialPromotionId()) || productInfoDto.getSpecialPromotionId() <= 0){
				if( productPrice.compareTo(productInfoDto.getProductPrice()) != 0){
					log.error("统一校验订单商品接口-存在价格变化的商品,查询到的价格productPrice = " + productPrice +",记录的商品价格=" + productInfoDto.getProductPrice());
					updateProductPrice(userDto,orderDto.getSupplyId(),productInfoDto.getSpuCode(),productPrice,productInfoDto.getFromWhere(),productInfoDto.getPromotionId());
					return returnFalse("存在价格变化的商品，请返回" + productFromWhere + "重新结算",productFromFastOrderCount);
				}
			}

			/* 校验活动商品相关的限购逻辑 */
			ProductPromotion productPromotion = productDrug.getProductPromotion();

			/* 如果常规商品这种状态是正常的，或者活动商品的活动信息是有效的，则统计该供应商下的已买商品总额 */
			if(  1 == status && productPrice.compareTo(new BigDecimal(0)) > 0 || (!UtilHelper.isEmpty(productPromotion) && new BigDecimal("0").compareTo(productPromotion.getPromotion_price()) < 0 )){
				productPriceCount = productPriceCount.add( productInfoDto.getProductPrice().multiply(new BigDecimal(productInfoDto.getProductCount())) );
			}

			if(UtilHelper.isEmpty(productInfoDto.getSpecialPromotionId()) || productInfoDto.getSpecialPromotionId() < 0){
				continue;
			}

			/* 1、 非空校验*/
			if( !UtilHelper.isEmpty(productInfoDto.getSpecialPromotionId()) && productInfoDto.getSpecialPromotionId() > 0 &&  UtilHelper.isEmpty(productPromotion)){
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
			if(!UtilHelper.isEmpty(productInfoDto.getSpecialPromotionId()) && productInfoDto.getSpecialPromotionId()>0){
				ShoppingCart shoppingCart  = new ShoppingCart();
				shoppingCart.setCustId(Integer.parseInt(buyer.getEnterpriseId()));
				shoppingCart.setSupplyId(Integer.parseInt(seller.getEnterpriseId()));
				shoppingCart.setSpuCode(productInfoDto.getSpuCode());
				shoppingCart.setProductId(productInfoDto.getId());
				shoppingCart.setPromotionId(productInfoDto.getSpecialPromotionId()+"");
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
			ProductPromotionDto temp = orderManage.queryProductWithPromotion(iPromotionDubboManageService,productInfoDto.getSpuCode(),orderDto.getSupplyId()+"",productInfoDto.getSpecialPromotionId(),orderDto.getCustId()+"");
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

		log.info("统一校验订单商品接口 ：校验成功" );
		map.put("result", true);
		return map;
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
	private void updateProductPrice(UserDto userDto, Integer supplyId, String spuCode, BigDecimal newProductPrice, Integer fromWhere,String promotionId){
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

}
