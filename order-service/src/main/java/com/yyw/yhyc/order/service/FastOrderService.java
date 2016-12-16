/**
 * Created By: hedanjun
 * Created On: 2016-12-13 14:41:50
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.search.remote.yhyc.ProductSearchInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.search.model.dto.ProductSearchParamDto;
import com.search.model.yhyc.ProductDrug;
import com.search.model.yhyc.ProductPromotionInfo;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yaoex.usermanage.model.custgroup.CustGroupDubboRet;
import com.yyw.yhyc.exception.ServiceException;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.dto.OrderPromotionDto;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.enmu.ProductStatusEnum;
import com.yyw.yhyc.order.mapper.ShoppingCartMapper;
import com.yyw.yhyc.order.utils.StringUtil;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.dto.ProductPromotionDto;
import com.yyw.yhyc.product.mapper.ProductInventoryMapper;

@Service("fastOrderService")
public class FastOrderService {
	private static final Logger logger = LoggerFactory.getLogger(FastOrderService.class);

	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	@Autowired
	private ProductInventoryMapper productInventoryMapper;
	
	@Reference 
	private ICustgroupmanageDubbo iCustgroupmanageDubbo;				//客户组接口
	@Reference 
	private ProductSearchInterface productSearchInterface;				//搜索接口
	@Reference 
	private IPromotionDubboManageService iPromotionDubboManageService;	//搜索接口
	@Reference 
	private IProductDubboManageService iProductDubboManageService;		//商品接口 
	
	/**
	 * 查询进货单列表
	 * @param custId
	 * @param fromWhere
	 * @return
	 */
	public List<ShoppingCartListDto> listShoppingCart(Integer custId, int fromWhere) {
		if(UtilHelper.isEmpty(custId)){
			return null;
		}
		long startTime = System.currentTimeMillis();
		//1、 查询极速下单数据
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(custId);
		shoppingCart.setFromWhere(fromWhere);
		List<ShoppingCartListDto> allShoppingCart = shoppingCartMapper.listAllShoppingCart(shoppingCart);
		if( UtilHelper.isEmpty(allShoppingCart) ){
			return allShoppingCart;
		}
		logger.info("success listForFastOrder step1 query, use " +( System.currentTimeMillis() - startTime) + "ms");
		
		//2、 返回极速下单数据
		String enterpriseId = String.valueOf(custId);
		List<ShoppingCartListDto> shoppingCartListList = handleShoppingCartList(allShoppingCart,enterpriseId);
		logger.info("success listForFastOrder step2 filling, use " +( System.currentTimeMillis() - startTime) + "ms");
		return shoppingCartListList;
	}
	
	/**
	 * 根据搜索结果填充商品详情信息，处理集合
	 * @param allShoppingCart
	 * @param productSearchInterface
     * @return
     */
	private List<ShoppingCartListDto> handleShoppingCartList(List<ShoppingCartListDto> shoppingCartListList,String enterpriseId) {
		if(UtilHelper.isEmpty(shoppingCartListList)){
			return shoppingCartListList;
		}
		//1、批量查询前, 参数拼接
		Set<String> productCodeSet = new HashSet<String>();
		for(ShoppingCartListDto shoppingCartList : shoppingCartListList) {
			if ( shoppingCartList==null ) continue;
			for (ShoppingCartDto shoppingCartDto : shoppingCartList.getShoppingCartDtoList()) {
				if ( shoppingCartDto==null ) continue;
				productCodeSet.add( shoppingCartDto.getSpuCode()+"-"+shoppingCartDto.getSupplyId() );
			}
		}
		//2、 批量查询商品
		List<ProductDrug> productDrugList = searchProductBatch(enterpriseId,productCodeSet);
		//3、把List封装成Map
		Map<String,ProductDrug> productDrugMap = new HashMap<>();
		for(ProductDrug productDrug : productDrugList){
			if(UtilHelper.isEmpty(productDrug)) continue;
			productDrugMap.put(productDrug.getId(),productDrug);
		}
		//4、根据搜索结果填充商品详情信息
		for(ShoppingCartListDto shoppingCartList : shoppingCartListList) {
			if ( shoppingCartList==null ) continue;
			for (ShoppingCartDto shoppingCartDto : shoppingCartList.getShoppingCartDtoList()) {
				if ( shoppingCartList==null ) continue;
				ProductDrug productDrug = productDrugMap.get( shoppingCartDto.getSpuCode()+"-"+shoppingCartDto.getSupplyId() );
				shoppingCartDto = handleShoppingCartDto(shoppingCartDto,productDrug);
			}
		}
		return shoppingCartListList;
	}
	
	/**
	 * 根据搜索结果填充商品详情信息，处理单条
	 * @param shoppingCartDto
	 * @param productDrug
     * @return
     */
	private ShoppingCartDto handleShoppingCartDto(ShoppingCartDto shoppingCartDto ,ProductDrug productDrug) {
		if(UtilHelper.isEmpty(shoppingCartDto)){
			return null;
		}
		//1、搜索结果里面搜不到该商品，表示该商品已下架 
		if(UtilHelper.isEmpty(productDrug)){
			shoppingCartDto.setNormalStatus(false);
			shoppingCartDto.setPutawayStatus(2);
			shoppingCartDto.setUnNormalStatusReason("已下架");
			shoppingCartDto.setStatusEnum(ProductStatusEnum.OffTheShelf.getStatus());
			return shoppingCartDto;
		}
//		//获取商品图片 
//		String productImgUrl = getProductImgUrl(productDrug.getPic_path());
//		shoppingCartDto.setProductImageUrl(productImgUrl);

		//2、填充其他值
		int minimumPacking = Integer.valueOf(productDrug.getMinimum_packing());
		shoppingCartDto.setMinimumPacking( minimumPacking ); 						//最小拆零包装数量
		shoppingCartDto.setUnit( productDrug.getUnit_cn() );						//最小拆零包装单位
		shoppingCartDto.setSaleStart( Integer.valueOf(productDrug.getState()) );	//起售量
		shoppingCartDto.setUpStep( minimumPacking ); 								//每次增加、减少的 递增数量
		shoppingCartDto.setPutawayStatus( Integer.valueOf(productDrug.getState()) ); //上下架状态

		//3、查询商品库存 
		int stockAmount = StringUtil.strToInt(productDrug.getStock_amount());
		if(stockAmount <= 0 || stockAmount < minimumPacking || UtilHelper.isEmpty(shoppingCartDto.getProductCount()) || stockAmount < shoppingCartDto.getProductCount()){
			shoppingCartDto.setExistProductInventory(false);
			shoppingCartDto.setProductInventory(0);
		}else{
			shoppingCartDto.setExistProductInventory(true);
			shoppingCartDto.setProductInventory(stockAmount);
		}

		//4、填充特价活动
		com.search.model.yhyc.ProductPromotion productPromotion  = productDrug.getProductPromotion();
		int promotionId = UtilHelper.isEmpty(productPromotion) || UtilHelper.isEmpty(productPromotion.getPromotion_id()) ? 0 : Integer.valueOf(productPromotion.getPromotion_id());
		if( promotionId > 0 && !UtilHelper.isEmpty(shoppingCartDto.getPromotionId()) && promotionId == shoppingCartDto.getPromotionId()) {
			shoppingCartDto.setPromotionPrice(productPromotion.getPromotion_price());
			shoppingCartDto.setPromotionMinimumPacking(productPromotion.getMinimum_packing());
			shoppingCartDto.setPromotionLimitNum(productPromotion.getLimit_num());
			shoppingCartDto.setPromotionSumInventory(productPromotion.getSum_inventory());
			shoppingCartDto.setPromotionCurrentInventory(productPromotion.getCurrent_inventory());
			shoppingCartDto.setPromotionType(UtilHelper.isEmpty(productPromotion.getPromotion_type()) ? 0 : Integer.valueOf(productPromotion.getPromotion_type()));
		}
		
		//5、填充满减活动
		shoppingCartDto.setProductPromotionInfos(productDrug.getProductPromotionInfos());
		
		//6、各种校验
		//商品渠道审核状态：0:待审核，1：审核通过，2：审核不通过
		int isChannel = UtilHelper.isEmpty(productDrug.getIs_channel()) ? 0 : Integer.valueOf( productDrug.getIs_channel());
		int channelId = UtilHelper.isEmpty(productDrug.getChannel_id()) ? 0 : Integer.valueOf( productDrug.getChannel_id());
		int applyChannelStatus = UtilHelper.isEmpty(productDrug.getApplyChannelStatus()) ? 0 : Integer.valueOf( productDrug.getApplyChannelStatus());

		/* 库存校验 */
		if( !shoppingCartDto.isExistProductInventory()){
			shoppingCartDto.setNormalStatus(false);
			shoppingCartDto.setUnNormalStatusReason("商品库存不足");
			shoppingCartDto.setStatusEnum(ProductStatusEnum.Shortage.getStatus());
		/* 价格校验 */
		}else if(UtilHelper.isEmpty(shoppingCartDto.getProductPrice()) || new BigDecimal("0").compareTo(shoppingCartDto.getProductPrice()) >= 0){
			shoppingCartDto.setNormalStatus(false);
			shoppingCartDto.setUnNormalStatusReason("商品价格异常");
			shoppingCartDto.setStatusEnum(ProductStatusEnum.NotDisplayPrice.getStatus());
		/* 已下架 */
		}else if(shoppingCartDto.getPutawayStatus() == null || shoppingCartDto.getPutawayStatus() != 1){
			shoppingCartDto.setNormalStatus(false);
			shoppingCartDto.setUnNormalStatusReason("商品已下架");
			shoppingCartDto.setStatusEnum(ProductStatusEnum.OffTheShelf.getStatus());
		/* 活动商品参加的活动已失效 : 进货单中保存了活动id, 但接口中查询不到活动信息 */
		}else if( (!UtilHelper.isEmpty(shoppingCartDto.getPromotionId()) && shoppingCartDto.getPromotionId() > 0 )  && UtilHelper.isEmpty(shoppingCartDto.getPromotionPrice())  ) {
			shoppingCartDto.setNormalStatus(false);
			shoppingCartDto.setUnNormalStatusReason("活动商品参加的活动已失效");
			shoppingCartDto.setStatusEnum(ProductStatusEnum.NotDisplayPrice.getStatus());
		}else if( 1 == isChannel && channelId == 0 ) {
			/* 商品渠道审核状态(applyChannelStatus) ：0:待审核，1：审核通过，2：审核不通过*/
			if(applyChannelStatus == 0 ){
				shoppingCartDto.setNormalStatus(false);
				shoppingCartDto.setUnNormalStatusReason("商品渠道待审核");
				shoppingCartDto.setStatusEnum(ProductStatusEnum.ChannelNotCheck.getStatus());
			}else if(applyChannelStatus == 1 ){
				/* 商品已加入渠道 */
				shoppingCartDto.setNormalStatus(true);
				shoppingCartDto.setStatusEnum(ProductStatusEnum.Normal.getStatus());
			}else{
				shoppingCartDto.setNormalStatus(false);
				shoppingCartDto.setUnNormalStatusReason("商品需加入渠道");
				shoppingCartDto.setStatusEnum(ProductStatusEnum.NotAddChannel.getStatus());
			}
		}else{
			shoppingCartDto.setNormalStatus(true);
			shoppingCartDto.setStatusEnum(ProductStatusEnum.Normal.getStatus());
		}
		return shoppingCartDto;
	}

	/**
	 * 加入进货单
	 * @param shoppingCart 进货单对象
	 * @return 成功失败标识（state：[S-->成功, F-->失败]），进货单商品数量，进货单订单金额
	 * @throws Exception
	 */
	public Map<String, Object> addShoppingCart(ShoppingCart shoppingCart) throws ServiceException {
		long startTime = System.currentTimeMillis();
		//1、参数校验
        if( UtilHelper.isEmpty( shoppingCart.getSupplyId()) ){
			throw new ServiceException("非法参数, supplyId can't be null");
		}
        if( UtilHelper.isEmpty( shoppingCart.getSpuCode()) ){
			throw new ServiceException("非法参数, spuCode can't be null");
		}
		//2、校验商品总数是否超过100
		ShoppingCart condition = new ShoppingCart();
		condition.setCustId(shoppingCart.getCustId());
		condition.setFromWhere(shoppingCart.getFromWhere());
		int count = shoppingCartMapper.findByCount(condition);
		if (count>=100) {
			logger.info("最多只能添加100个商品，您目前已经有"+count+"个商品。请先下单。");
			throw new ServiceException("最多只能添加100个商品，您目前已经有"+count+"个商品。请先下单。");
		}
		//3、 单个商品的数量限制
		if( shoppingCart.getProductCount()<=0||shoppingCart.getProductCount()>9999999 ){
			logger.info("购买数量不能小于0，也不能大于9999999，你的数量是 " + shoppingCart.getProductCount());
			throw new ServiceException("购买数量不能小于0，也不能大于9999999，你的数量是 " + shoppingCart.getProductCount());
		}
		//4、 校验商品库存 
		checkStock(shoppingCart.getSupplyId(), shoppingCart.getSpuCode(),shoppingCart.getProductCount());
		//5、 执行新增操作
		shoppingCart.setProductSettlementPrice(shoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
		shoppingCartMapper.save(shoppingCart);
		//6、返回结果
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", "S");
		logger.info("success addShoppingCart , use " +( System.currentTimeMillis() - startTime) + "ms");
		return map;
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception {
		shoppingCartMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 修改进货单商品的数量
	 * @param shoppingCart
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public int updateNum(ShoppingCart shoppingCart) throws Exception{
		long startTime = System.currentTimeMillis();
		int updateCount = 0;
		//1、参数校验
        if( UtilHelper.isEmpty( shoppingCart.getShoppingCartId()) ){
			throw new ServiceException("非法参数, shoppingCartId can't be null");
		}
        if( UtilHelper.isEmpty( shoppingCart.getProductCount()) ){
			throw new ServiceException("非法参数, productCount can't be null");
		}
        //2、 单个商品的数量限制
  		if( shoppingCart.getProductCount()<=0||shoppingCart.getProductCount()>9999999 ){
  			logger.info("商品数量不能小于0，也不能大于9999999，你的数量是 " + shoppingCart.getProductCount());
  			throw new ServiceException("商品数量不能小于0，也不能大于9999999，你的数量是 " + shoppingCart.getProductCount());
  		}
  		//3、查询已存在的记录
		ShoppingCart oldShoppingCart =  shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());
		if( UtilHelper.isEmpty(oldShoppingCart) ){
			throw new ServiceException("原始数据不存在,请刷新页面重试！");
		}
		String custId = oldShoppingCart.getCustId().toString();
		String supplyId = oldShoppingCart.getSupplyId().toString();
		String spuCode = oldShoppingCart.getSpuCode();
		String userName = shoppingCart.getUpdateUser();
		int leftBuyNum = shoppingCart.getProductCount();
		//4、查询商品信息
		ProductDrug productDrug = searchProduct(custId,supplyId,spuCode);		
		//5、修改特价商品数量
		if ( !UtilHelper.isEmpty(oldShoppingCart.getPromotionId()) ) {
			//5.1、查询特价活动信息
			ProductPromotionDto productPromotionDto = queryPromotionDto(custId,supplyId,spuCode,oldShoppingCart.getPromotionId());
			//5.2、获取可以购买特价商品的数量
			int canBuyNum = getCanBuyNum(productPromotionDto,oldShoppingCart,leftBuyNum);
			//5.3、购买特价商品
			if(canBuyNum>0){
				oldShoppingCart.setProductPrice(productPromotionDto.getPromotionPrice());
				oldShoppingCart.setProductCount(canBuyNum);
				oldShoppingCart.setProductSettlementPrice( oldShoppingCart.getProductPrice().multiply(new BigDecimal(oldShoppingCart.getProductCount())) );
				oldShoppingCart.setUpdateUser(userName);
				updateCount+=shoppingCartMapper.update(oldShoppingCart);
			}
			//5.4、购买原价商品的数量
			int step = Integer.parseInt(productDrug.getMinimum_packing());
			leftBuyNum = (leftBuyNum-canBuyNum)/step*step;
		} 
		//6、 修改原价商品数量
		if(leftBuyNum>0){
			//6.1、 校验原价商品库存
			checkStock(Integer.parseInt(supplyId),spuCode,leftBuyNum);
			//6.2、查询是否存在原价记录
			ShoppingCart condition = new ShoppingCart();
			condition.setCustId(oldShoppingCart.getCustId());
			condition.setSupplyId(oldShoppingCart.getSupplyId());
			condition.setSpuCode(oldShoppingCart.getSpuCode());
			condition.setFromWhere(oldShoppingCart.getFromWhere());
			List<ShoppingCart> shoppingCarts = shoppingCartMapper.listByProperty(condition);
			
			//6.3、如果有则直接修改数量
			if( !UtilHelper.isEmpty(shoppingCarts)){
				ShoppingCart oldNormalShoppingCart = shoppingCarts.get(0);
				oldNormalShoppingCart.setProductPrice(new BigDecimal(productDrug.getShowPrice()));
				oldNormalShoppingCart.setProductCount(leftBuyNum);
				oldNormalShoppingCart.setProductSettlementPrice(oldShoppingCart.getProductPrice().multiply(new BigDecimal(oldNormalShoppingCart.getProductCount())));
				oldNormalShoppingCart.setUpdateUser(userName);
				updateCount+=shoppingCartMapper.update(oldNormalShoppingCart);
			//6.4、如果没有则新增一条记录
			}else{
				ShoppingCart normalShoppingCart = new ShoppingCart();
				normalShoppingCart.setCustId(oldShoppingCart.getCustId());
				normalShoppingCart.setSupplyId(oldShoppingCart.getSupplyId());
				normalShoppingCart.setSpuCode(oldShoppingCart.getSpuCode());
				normalShoppingCart.setSpecification(oldShoppingCart.getSpecification());
				normalShoppingCart.setProductId(oldShoppingCart.getProductId());
				normalShoppingCart.setProductCodeCompany(oldShoppingCart.getProductCodeCompany());
				normalShoppingCart.setProductName(oldShoppingCart.getProductName());
				normalShoppingCart.setManufactures(oldShoppingCart.getManufactures());
				normalShoppingCart.setSpuCode(oldShoppingCart.getSpuCode());
				normalShoppingCart.setFromWhere(oldShoppingCart.getFromWhere());
				normalShoppingCart.setProductPrice(new BigDecimal(productDrug.getShowPrice()));
				normalShoppingCart.setProductCount(leftBuyNum);
				normalShoppingCart.setProductSettlementPrice(normalShoppingCart.getProductPrice().multiply(new BigDecimal(normalShoppingCart.getProductCount())));
				normalShoppingCart.setCreateUser(userName);
				shoppingCartMapper.save(normalShoppingCart);
				updateCount++;
			}
		}
		logger.info("success updateNum "+updateCount+" records, use " +( System.currentTimeMillis() - startTime) + "ms");
		return updateCount;
	}
	
	/**
	 * 查询活动信息
	 * @param buyerEnterpriseId        采购商企业id
	 * @param sellerEnterpriseId       供应商企业id
	 * @param spuCode                  商品的spuCode
	 * @param promotionId              活动id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ProductPromotionDto queryPromotionDto(String buyerEnterpriseId, String sellerEnterpriseId, String spuCode, Integer promotionId) {
		try{
			Map<String,Object> params=new HashMap<String,Object>();
			params.put("spuCode",spuCode);
			params.put("promotionId", promotionId);
			params.put("buyerCode", buyerEnterpriseId);
			params.put("sellerCode", sellerEnterpriseId);
			Map map = iPromotionDubboManageService.getProductGroupBySpuCodeAndSellerCode(params);
			if(UtilHelper.isEmpty(map) ){
				return null;
			}
			ProductPromotionDto productPromotionDto = new ProductPromotionDto();
			productPromotionDto.setPromotionId(UtilHelper.isEmpty(map.get("promotionId")+"") ? 0 : Integer.parseInt(map.get("promotionId")+""));
			productPromotionDto.setSpuCode(UtilHelper.isEmpty(map.get("spuCode")+"") ? "" : map.get("spuCode")+"");
			productPromotionDto.setPromotionPrice(UtilHelper.isEmpty(map.get("promotionPrice")+"") ? new BigDecimal("0") : new BigDecimal(map.get("promotionPrice")+""));
			productPromotionDto.setMinimumPacking(UtilHelper.isEmpty(map.get("minimumPacking")+"") ? 0 : Integer.parseInt(map.get("minimumPacking")+""));
			productPromotionDto.setLimitNum(UtilHelper.isEmpty(map.get("limitNum")+"") ? 0 : Integer.parseInt(map.get("limitNum")+""));
			productPromotionDto.setSumInventory(UtilHelper.isEmpty(map.get("sumInventory")+"") ? 0 : Integer.parseInt(map.get("sumInventory")+""));
			productPromotionDto.setCurrentInventory(UtilHelper.isEmpty(map.get("currentInventory")+"") ? 0 : Integer.parseInt(map.get("currentInventory")+""));
			productPromotionDto.setSort(UtilHelper.isEmpty(map.get("sort")+"") ? 0 : Integer.parseInt(map.get("sort")+""));
			productPromotionDto.setPromotionType(UtilHelper.isEmpty(map.get("promotionType")+"") ? 0 : Integer.parseInt(map.get("promotionType")+""));
			productPromotionDto.setPromotionName(UtilHelper.isEmpty(map.get("promotionName")+"") ? "" : map.get("promotionName")+"");
			logger.info("success queryProductPromotionDto , productPromotionDto=" + productPromotionDto);
			return productPromotionDto;
		}catch (Exception e){
			logger.error("exception when queryProductPromotionDto " + e.getMessage(),e);
			return null;
		}
	}

	/**
	 * 获取特价商品的可购买数量
	 * 1、活动实时库存	2、剩余可购买数量=（每人限购数量 -已购买数量）	 3、本次购买数量		4、活动起批量 
	 * @param productPromotionDto
	 * @param buyedInHistory
	 * @return
	 */
	private int getCanBuyNum(ProductPromotionDto productPromotionDto , ShoppingCart oldShoppingCart , int buyNum) {
		int buyedInHistory = shoppingCartMapper.countBuyedNumInHistory(oldShoppingCart);//已购买数量
		int currentNum = productPromotionDto.getCurrentInventory();			//1、活动实时库存
		int leftLimitNum = productPromotionDto.getLimitNum()-buyedInHistory;//2、剩余可购买数量
		//活动实时库存、剩余可购买数量、本次购买数量 三者取最小值
		int minOne = currentNum < leftLimitNum ? currentNum : leftLimitNum; 
		minOne = buyNum < minOne ? buyNum : minOne;							
		int step = productPromotionDto.getMinimumPacking();					//4、活动起批量
		return (minOne/step)*step; 
	}
	
	/**
	 * 校验普通(原价)商品的库存
	 * @param supplyId
	 * @param spuCode
	 * @param buyNum
	 * @throws ServiceException
	 */
	private void checkStock(Integer supplyId, String spuCode, Integer buyNum) throws ServiceException {
		ProductInventory productInventory = productInventoryMapper.findBySupplyIdSpuCode(supplyId, spuCode);
		if( UtilHelper.isEmpty(productInventory) || UtilHelper.isEmpty(productInventory.getFrontInventory()) || productInventory.getFrontInventory()<= 0 ){
			throw new ServiceException("商品没有库存");
		}
		if( buyNum>productInventory.getFrontInventory()){
			throw new ServiceException("商品库存不足");
		}
	}
	
	/**
	 * 搜索商品-单条
	 * @param buyerEnterprizeId    买家企业id
	 * @param sellerEnterprizeId   商家企业id
	 * @param spuCode              商品spuCode
	 * @return
	 * @throws Exception 
	 */
	private ProductDrug searchProduct(String buyerEnterprizeId, String sellerEnterprizeId, String spuCode) throws Exception{
		if( UtilHelper.isEmpty(spuCode)|| UtilHelper.isEmpty(buyerEnterprizeId) || UtilHelper.isEmpty(sellerEnterprizeId) ) {
			throw new ServiceException("查询商品价格失败");
		}
		String groupParam = getGroupParam(buyerEnterprizeId); 
		return productSearchInterface.findProductShowPriceForProductDrug(buyerEnterprizeId, sellerEnterprizeId, spuCode, groupParam);
	}
	
	/**
	 * 搜索商品-批量
	 * @param enterpriseId
	 * @param productCodeSet
	 * @return
	 */
	private List<ProductDrug> searchProductBatch(String buyerCode,Set<String> productCodeSet){
		ProductSearchParamDto productSearchParamDto = new ProductSearchParamDto();
		productSearchParamDto.setProductCodes(productCodeSet);			//批量查询拼接的参数：productCode+“-”+sellerCode
		productSearchParamDto.setBuyerCode(buyerCode);					//登录用户的企业ID
		String groupParam = getGroupParam(buyerCode);
		if ( !UtilHelper.isEmpty(groupParam) ) {
			productSearchParamDto.setGroupCodes(groupParam);			//登录用户的用户组参数
		}
		logger.info("searchBatchProduct , param=["+productSearchParamDto+"]");
		List<ProductDrug> drugList = productSearchInterface.searchBatchProduct(productSearchParamDto);
		logger.info("searchBatchProduct , result="+JSON.toJSONString(drugList));
		return drugList;
	}

	/**
	 * 获取用户的客户组信息，如果有多个客户组则用“,”连接
	 * @param enterpriseId
	 * @return
	 */
	private String getGroupParam(String enterpriseId) {
		String groupParam = "";
		CustGroupDubboRet ret = iCustgroupmanageDubbo.queryGroupBycustId(enterpriseId);
		if (ret.getIsSuccess() == 1) {
			List<Map<String,Object>> list = ret.getData();
			for (Map<String, Object> map : list) {
				groupParam += map.get("group_code") + ",";
			}
			if ( !UtilHelper.isEmpty(groupParam) ) {
				groupParam = groupParam.substring(0, (groupParam.length() - 1));
			}
		}
		return groupParam;
	}

}