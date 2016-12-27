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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.search.remote.yhyc.ProductSearchInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.search.model.comparator.ProductPromotionRuleComparator;
import com.search.model.dto.ProductSearchParamDto;
import com.search.model.yhyc.ProductDrug;
import com.search.model.yhyc.ProductPromotion;
import com.search.model.yhyc.ProductPromotionInfo;
import com.search.model.yhyc.ProductPromotionRule;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yaoex.usermanage.model.custgroup.CustGroupDubboRet;
import com.yyw.yhyc.exception.ServiceException;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.ProductStatusEnum;
import com.yyw.yhyc.order.enmu.ShoppingCartFromWhereEnum;
import com.yyw.yhyc.order.mapper.ShoppingCartMapper;
import com.yyw.yhyc.order.utils.StringUtil;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.product.dto.ProductPromotionDto;
import com.yyw.yhyc.product.mapper.ProductInventoryMapper;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;
import com.yyw.yhyc.usermanage.mapper.UsermanageEnterpriseMapper;

@Service("fastOrderService")
public class FastOrderService {
	private static final Logger logger = LoggerFactory.getLogger(FastOrderService.class);
	
	private static final int maxBuyNum = 9999999; 

	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	@Autowired
	private ProductInventoryMapper productInventoryMapper;
	@Autowired
	private UsermanageEnterpriseMapper enterpriseMapper;


	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public ShoppingCart getByPK(java.lang.Integer primaryKey) throws Exception {
		return shoppingCartMapper.getByPK(primaryKey);
	}
	
	/**
	 * 查询进货单列表
	 * @return
	 */
	public List<ShoppingCartListDto> listShoppingCart(ShoppingCart shoppingCart) {
		if(UtilHelper.isEmpty(shoppingCart)){
			return null;
		}
		List<ShoppingCartListDto> allShoppingCart = shoppingCartMapper.listAllShoppingCart(shoppingCart);
		return allShoppingCart;
	}
	
	/**
	 * 查询进货单的买卖双方企业ID
	 * @param custId
	 * @return
	 */
	public List<ShoppingCart>  listDistinctCustIdAndSupplyId(Integer custId){
		if( UtilHelper.isEmpty(custId) ){
			return null;
		}
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(custId);
		return shoppingCartMapper.listDistinctCustIdAndSupplyId(shoppingCart);
	}
	
	/**
	 * 查询进货单列表
	 * @param custId
	 * @param fromWhere
	 * @return
	 */
	public List<ShoppingCartListDto> listShoppingCart(Integer custId, int fromWhere,ProductSearchInterface productSearchInterface,ICustgroupmanageDubbo iCustgroupmanageDubbo) {
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
		List<ShoppingCartListDto> shoppingCartListList = handleShoppingCartList(allShoppingCart,enterpriseId, productSearchInterface, iCustgroupmanageDubbo);
		logger.info("success listForFastOrder step2 filling, use " +( System.currentTimeMillis() - startTime) + "ms");
		return shoppingCartListList;
	}
	
	/**
	 * 根据搜索结果填充商品详情信息，处理集合
     * @return
     */
	private List<ShoppingCartListDto> handleShoppingCartList(List<ShoppingCartListDto> shoppingCartListList,String enterpriseId,ProductSearchInterface productSearchInterface,ICustgroupmanageDubbo iCustgroupmanageDubbo) {
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
		List<ProductDrug> productDrugList = searchProductBatch(enterpriseId,productCodeSet, productSearchInterface, iCustgroupmanageDubbo);
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
		
		//给满减活动排序
		sortProductPromotionInfos(productDrug.getProductPromotionInfos());

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
			shoppingCartDto.setUnNormalStatusReason("缺货");
			shoppingCartDto.setStatusEnum(ProductStatusEnum.Shortage.getStatus());
		/* 价格校验 */
		}else if(UtilHelper.isEmpty(shoppingCartDto.getProductPrice()) || new BigDecimal("0").compareTo(shoppingCartDto.getProductPrice()) >= 0){
			shoppingCartDto.setNormalStatus(false);
			shoppingCartDto.setUnNormalStatusReason("价格异常");
			shoppingCartDto.setStatusEnum(ProductStatusEnum.NotDisplayPrice.getStatus());
		/* 已下架 */
		}else if(shoppingCartDto.getPutawayStatus() == null || shoppingCartDto.getPutawayStatus() != 1){
			shoppingCartDto.setNormalStatus(false);
			shoppingCartDto.setUnNormalStatusReason("下架");
			shoppingCartDto.setStatusEnum(ProductStatusEnum.OffTheShelf.getStatus());
		/* 活动商品参加的活动已失效 : 进货单中保存了活动id, 但接口中查询不到活动信息 */
		}else if( (!UtilHelper.isEmpty(shoppingCartDto.getPromotionId()) && shoppingCartDto.getPromotionId() > 0 )  && UtilHelper.isEmpty(shoppingCartDto.getPromotionPrice())  ) {
			shoppingCartDto.setNormalStatus(false);
			shoppingCartDto.setUnNormalStatusReason("失效");
			shoppingCartDto.setStatusEnum(ProductStatusEnum.NotDisplayPrice.getStatus());
		}else if( 1 == isChannel && channelId == 0 ) {
			/* 商品渠道审核状态(applyChannelStatus) ：0:待审核，1：审核通过，2：审核不通过*/
			if(applyChannelStatus == 0 ){
				shoppingCartDto.setNormalStatus(false);
				shoppingCartDto.setUnNormalStatusReason("渠道待审核");
				shoppingCartDto.setStatusEnum(ProductStatusEnum.ChannelNotCheck.getStatus());
			}else if(applyChannelStatus == 1 ){
				/* 商品已加入渠道 */
				shoppingCartDto.setNormalStatus(true);
				shoppingCartDto.setStatusEnum(ProductStatusEnum.Normal.getStatus());
			}else{
				shoppingCartDto.setNormalStatus(false);
				shoppingCartDto.setUnNormalStatusReason("待加入渠道");
				shoppingCartDto.setStatusEnum(ProductStatusEnum.NotAddChannel.getStatus());
			}
		}else{
			shoppingCartDto.setNormalStatus(true);
			shoppingCartDto.setStatusEnum(ProductStatusEnum.Normal.getStatus());
		}
		return shoppingCartDto;
	}
	
	/**
	 * 给满减活动排序
	 * @param productPromotionInfos
	 */
	public static void sortProductPromotionInfos(Set<ProductPromotionInfo> productPromotionInfos){
		if(productPromotionInfos==null){
			return;
		}
		Comparator myComparator = new ProductPromotionRuleComparator();
		Iterator<ProductPromotionInfo> iterator = productPromotionInfos.iterator();
		while(iterator.hasNext()) {
			ProductPromotionInfo productPromotionInfo = iterator.next();
			if (productPromotionInfo != null && productPromotionInfo.getProductPromotionRules() != null) {
				Set<ProductPromotionRule> productPromotionRules = new TreeSet<ProductPromotionRule>(myComparator);
				productPromotionRules.addAll(productPromotionInfo.getProductPromotionRules());
				productPromotionInfo.setProductPromotionRules(productPromotionRules);
			}
		}
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
		if( shoppingCart.getProductCount()<=0||shoppingCart.getProductCount()>maxBuyNum ){
			logger.info("购买数量不能小于0，也不能大于"+maxBuyNum+"，你的数量是 " + shoppingCart.getProductCount());
			throw new ServiceException("购买数量不能小于0，也不能大于"+maxBuyNum+"，你的数量是 " + shoppingCart.getProductCount());
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
	 * @return
	 * @throws Exception
	 */
	public int updateNum(ShoppingCart shoppingCart,IPromotionDubboManageService iPromotionDubboManageService,ProductSearchInterface productSearchInterface,ICustgroupmanageDubbo iCustgroupmanageDubbo) throws Exception{
		long startTime = System.currentTimeMillis();
		int updateCount = 0;
		//1、参数校验
        if( UtilHelper.isEmpty( shoppingCart.getShoppingCartId()) ){
			throw new ServiceException("非法参数, shoppingCartId can't be null");
		}
        if( UtilHelper.isEmpty( shoppingCart.getProductCount()) ){
			throw new ServiceException("非法参数， productCount can't be null");
		}
        //2、 单个商品的数量限制
  		if( shoppingCart.getProductCount()<=0||shoppingCart.getProductCount()>maxBuyNum ){
  			logger.info("商品数量不能小于0，也不能大于"+maxBuyNum+"，你的数量是 " + shoppingCart.getProductCount());
  			throw new ServiceException("商品数量不能小于0，也不能大于"+maxBuyNum+"，你的数量是 " + shoppingCart.getProductCount());
  		}
  		//3、查询已存在的记录
		ShoppingCart oldShoppingCart =  shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());
		if( UtilHelper.isEmpty(oldShoppingCart) ){
			throw new ServiceException("原始数据不存在，请刷新页面重试！");
		}
		String custId = oldShoppingCart.getCustId().toString();
		String supplyId = oldShoppingCart.getSupplyId().toString();
		String spuCode = oldShoppingCart.getSpuCode();
		String userName = shoppingCart.getUpdateUser();
		int leftBuyNum = shoppingCart.getProductCount();
		//4、查询商品信息
		ProductDrug productDrug = searchProduct(custId,supplyId,spuCode,productSearchInterface,iCustgroupmanageDubbo);
		if(UtilHelper.isEmpty(productDrug)){
			throw new ServiceException("商品下架，请刷新页面重试！");
		}
		//5、修改特价商品数量
		if ( !UtilHelper.isEmpty(oldShoppingCart.getPromotionId()) ) {
			//5.1、查询特价活动信息
			ProductPromotionDto productPromotionDto = queryPromotionDto(custId,supplyId,spuCode,oldShoppingCart.getPromotionId(),iPromotionDubboManageService);
			if(UtilHelper.isEmpty(productPromotionDto)){
				throw new ServiceException("活动失效，请刷新页面重试！");
			}
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
	private ProductPromotionDto queryPromotionDto(String buyerEnterpriseId, String sellerEnterpriseId, String spuCode, Integer promotionId,IPromotionDubboManageService iPromotionDubboManageService) {
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
	 * @return
	 */
	private int getCanBuyNum(ProductPromotionDto productPromotionDto , ShoppingCart oldShoppingCart , int buyNum) throws ServiceException {
		Integer limitNum = productPromotionDto.getLimitNum();//每人限购数量
		if( limitNum==null||limitNum==-1 ){
			limitNum = maxBuyNum;
		}
		//未避免繁琐的页面操作，此处不判断历史购买数量，仅在提交时判断.
		//int buyedInHistory = shoppingCartMapper.countBuyedNumInHistory(oldShoppingCart);//已购买数量
		int buyedInHistory = 0;
		int currentNum = productPromotionDto.getCurrentInventory();			//1、活动实时库存
		int leftLimitNum =limitNum-buyedInHistory;//2、剩余可购买数量
		int step = productPromotionDto.getMinimumPacking();					//4、活动起批量
		//活动实时库存、剩余可购买数量、本次购买数量 三者取最小值
		int minOne = currentNum < leftLimitNum ? currentNum : leftLimitNum; 
		minOne = buyNum < minOne ? buyNum : minOne;	
//		if( minOne==leftLimitNum&&buyedInHistory!=0&&limitNum!=maxBuyNum ){
//			throw new ServiceException(oldShoppingCart.getProductName()+"的特价活动，每人限购"+limitNum+"个，你已经购买了"
//					+buyedInHistory+"个,还能买"+((limitNum-buyedInHistory)/step)*step+"个！");
//		}
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
	private ProductDrug searchProduct(String buyerEnterprizeId, String sellerEnterprizeId, String spuCode,ProductSearchInterface productSearchInterface,ICustgroupmanageDubbo iCustgroupmanageDubbo) throws Exception{
		if( UtilHelper.isEmpty(spuCode)|| UtilHelper.isEmpty(buyerEnterprizeId) || UtilHelper.isEmpty(sellerEnterprizeId) ) {
			throw new ServiceException("查询商品价格失败");
		}
		String groupParam = getGroupParam(buyerEnterprizeId,iCustgroupmanageDubbo);
		return productSearchInterface.findProductShowPriceForProductDrug(buyerEnterprizeId, sellerEnterprizeId, spuCode, groupParam);
	}
	
	/**
	 * 搜索商品-批量
	 * @param productCodeSet
	 * @return
	 */
	private List<ProductDrug> searchProductBatch(String buyerCode,Set<String> productCodeSet,ProductSearchInterface productSearchInterface,ICustgroupmanageDubbo iCustgroupmanageDubbo){
		ProductSearchParamDto productSearchParamDto = new ProductSearchParamDto();
		productSearchParamDto.setProductCodes(productCodeSet);			//批量查询拼接的参数：productCode+“-”+sellerCode
		productSearchParamDto.setBuyerCode(buyerCode);					//登录用户的企业ID
		String groupParam = getGroupParam(buyerCode,iCustgroupmanageDubbo);
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
	 * @param custId
	 * @return
	 */
	private String getGroupParam(String custId,ICustgroupmanageDubbo iCustgroupmanageDubbo) {
		String groupParam = "";
		CustGroupDubboRet ret = iCustgroupmanageDubbo.queryGroupBycustId(custId);
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
	
	/**
	 * 提交订单时，若商品价格变动，则不让提交订单，且更新进货单里相关商品的价格
	 * @param userDto
	 * @param supplyId
	 * @param spuCode
	 * @param newProductPrice
	 * @param fromWhere         	购物车中商品来源
	 * @param promotionId        商品参加活动的id
	 */
	private void updateProductPrice(UserDto userDto, Integer supplyId, String spuCode, BigDecimal newProductPrice, Integer fromWhere, Integer promotionId){
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(userDto.getCustId());
		shoppingCart.setSupplyId(supplyId);
		shoppingCart.setSpuCode(spuCode);
		if(!UtilHelper.isEmpty(promotionId) && promotionId > 0 ){
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
			logger.info("商品价格发生变化需要更新，更新数据" + shoppingCart);
			shoppingCartMapper.update(shoppingCart);
		}
	}
	
	/**
	 * 校验要购买的商品
	 * @param userDto 当前登陆人
	 * @param orderDto 要提交订单的商品数据
	 * @return
     */
	public String validateProducts(UserDto userDto, OrderDto orderDto,ProductSearchInterface productSearchInterface,ICustgroupmanageDubbo iCustgroupmanageDubbo,IPromotionDubboManageService iPromotionDubboManageService){
		//1、参数校验 
		if( UtilHelper.isEmpty(orderDto)||UtilHelper.isEmpty(orderDto.getProductInfoDtoList()) ){
			return "商品数据不能为空";
		}
		if ( orderDto.getSupplyId()==userDto.getCustId() ){
			return "不能购买自己的商品";
		}
		
		//2、 批量搜索商品
		Set<String> productCodeSet = new HashSet<String>();
		for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()) {
			productCodeSet.add(productInfoDto.getSpuCode() + "-" + orderDto.getSupplyId() );
		}
		List<ProductDrug> productDrugList = searchProductBatch( String.valueOf(userDto.getCustId()), productCodeSet,productSearchInterface,iCustgroupmanageDubbo);
		//3、把List封装成Map
		Map<String,ProductDrug> productDrugMap = new HashMap<>();
		for(ProductDrug productDrug : productDrugList){
			if(UtilHelper.isEmpty(productDrug)) continue;
			productDrugMap.put(productDrug.getId(),productDrug);
		}

		//4、校验商品信息
		UsermanageEnterprise buyer = enterpriseMapper.getByEnterpriseId(orderDto.getCustId().toString());
		UsermanageEnterprise seller = enterpriseMapper.getByEnterpriseId(orderDto.getSupplyId().toString());
		BigDecimal productPriceCount = new BigDecimal(0);//该供应商下所有商品的总金额（用于判断是否符合供应商的订单起售金额）
		ProductDrug productDrug = null;
		for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()){
			//1、 获取商品信息 
			productDrug = productDrugMap.get( productInfoDto.getSpuCode()+"-"+orderDto.getSupplyId() );
			if( UtilHelper.isEmpty(productDrug)){
				return "搜索不到"+productDrug.getShort_name();
			}

			//2、检查库存 
			int stockAmount = UtilHelper.isEmpty(productDrug.getStock_amount()) ? 0 : Integer.valueOf(productDrug.getStock_amount());
			int minimumPacking = UtilHelper.isEmpty(productDrug.getMinimum_packing()) ? 1 : Integer.valueOf( productDrug.getMinimum_packing());
			if(stockAmount <= 0 || stockAmount < minimumPacking || stockAmount < productInfoDto.getProductCount()){
				logger.info("统一校验订单商品接口 ：商品(spuCode=" + productInfoDto.getSpuCode() + ")库存校验失败! " +
						"\n stockAmount = " + stockAmount + ",minimumPacking=" + minimumPacking + ",productInfoDto.getProductCount()=" + productInfoDto.getProductCount() );
				return productDrug.getShort_name()+"缺货或下架了，请返回极速下单查看";
			}

			//3、 查询商品上下架状态 
			int status = UtilHelper.isEmpty(productDrug.getState()) ? 0 : Integer.valueOf( productDrug.getState());
			int isChannel = UtilHelper.isEmpty(productDrug.getIs_channel()) ? 0 : Integer.valueOf( productDrug.getIs_channel());
			if( 1 != status ){
				logger.info("统一校验订单商品接口-查询商品上下架状态,status:" + status + ",// 0未上架  1上架  2本次下架  3非本次下架");
				return productDrug.getShort_name()+"缺货或下架了，请返回极速下单查看";
			}
			productInfoDto.setIsChannel(isChannel);

			//4、 查询价格
			BigDecimal productPrice = null;
			productPrice = UtilHelper.isEmpty(productDrug.getShowPrice()) ? new BigDecimal(0) : new BigDecimal(productDrug.getShowPrice() + "");
			/* 若商品的最新价格 小于等于0，则提示该商品无法购买 */
			if(productPrice.compareTo(new BigDecimal(0)) <= 0){
				updateProductPrice(userDto,orderDto.getSupplyId(),productInfoDto.getSpuCode(),productPrice,productInfoDto.getFromWhere(),productInfoDto.getPromotionId());
				return "您无法购买"+productDrug.getShort_name()+"，请返回极速下单查看";
			}
			/* 若商品价格变动，则不让提交订单，且更新进货单里相关商品的价格 */
			if(UtilHelper.isEmpty(productInfoDto.getPromotionId()) || productInfoDto.getPromotionId() <= 0){
				if( productPrice.compareTo(productInfoDto.getProductPrice()) != 0){
					updateProductPrice(userDto,orderDto.getSupplyId(),productInfoDto.getSpuCode(),productPrice,productInfoDto.getFromWhere(),productInfoDto.getPromotionId());
					return productDrug.getShort_name()+"的价格发生变化，请返回极速下单查看";
				}
			}

			//5、 校验活动商品相关的限购逻辑 
			ProductPromotion productPromotion = productDrug.getProductPromotion();
			/* 如果常规商品这种状态是正常的，或者活动商品的活动信息是有效的，则统计该供应商下的已买商品总额 */
			if(  1 == status && productPrice.compareTo(new BigDecimal(0)) > 0 || (!UtilHelper.isEmpty(productPromotion) && new BigDecimal("0").compareTo(productPromotion.getPromotion_price()) < 0 )){
				productPriceCount = productPriceCount.add( productInfoDto.getProductPrice().multiply(new BigDecimal(productInfoDto.getProductCount())) );
			}
			if(UtilHelper.isEmpty(productInfoDto.getPromotionId()) || productInfoDto.getPromotionId() < 0){
				continue;
			}
			/* 活动是否失效*/
			if( !UtilHelper.isEmpty(productInfoDto.getPromotionId()) && productInfoDto.getPromotionId() > 0 &&  UtilHelper.isEmpty(productPromotion)){
				return productDrug.getShort_name() +"参加的活动已失效";
			}

			/* 商品的活动价格是否发生变化*/
			if(!UtilHelper.isEmpty(productInfoDto.getProductPrice()) && productInfoDto.getProductPrice().compareTo(productPromotion.getPromotion_price()) != 0){
				updateProductPrice(userDto,orderDto.getSupplyId(),productInfoDto.getSpuCode(),productPromotion.getPromotion_price(),productInfoDto.getFromWhere(),productInfoDto.getPromotionId());
				return productDrug.getShort_name()+"的特价活动，活动价格发生变化，请返回极速下单重新结算";
			}

			/* 校验 购买活动商品的数量 是否合法 */
			if(productPromotion.getLimit_num() > 0 && productInfoDto.getProductCount() < productPromotion.getMinimum_packing()){
				return productDrug.getShort_name()+"的特价活动，购买数量"+ productInfoDto.getProductCount() +"低于最小起批量" + productPromotion.getMinimum_packing();
			}

			/* 查询该商品在该活动中的历史购买量*/
			int buyedInHistory = 0;
			if(!UtilHelper.isEmpty(productInfoDto.getPromotionId()) && productInfoDto.getPromotionId() > 0){
				ShoppingCart shoppingCart  = new ShoppingCart();
				shoppingCart.setCustId(Integer.parseInt(buyer.getEnterpriseId()));
				shoppingCart.setSupplyId(Integer.parseInt(seller.getEnterpriseId()));
				shoppingCart.setSpuCode(productInfoDto.getSpuCode());
				shoppingCart.setProductId(productInfoDto.getId());
				shoppingCart.setPromotionId(productInfoDto.getPromotionId());
				buyedInHistory  = shoppingCartMapper.countBuyedNumInHistory(shoppingCart);
			}

			/* 判断理论上可以以特价购买的数量 */
			int limitNum = productPromotion.getLimit_num();
			limitNum = limitNum==-1?maxBuyNum:limitNum;//-1 时表示不限购
			int canBuyByPromotionPrice = limitNum - buyedInHistory;
			if ( canBuyByPromotionPrice <= 0 ){
				return productDrug.getShort_name()+"的特价活动，每人限购"+productPromotion.getLimit_num() +"件,您已购买了" + buyedInHistory +
						"件,还能购买" + canBuyByPromotionPrice +"件。";
			}

			/* 活动实时库存字段，如果走搜索接口，可能会有问题(比如该字段同步失败)。所以改用活动的dubbo接口去获取该字段的值 */
			ProductPromotionDto temp = queryPromotionDto(orderDto.getCustId().toString(),orderDto.getSupplyId().toString(),productInfoDto.getSpuCode(),productInfoDto.getPromotionId(),iPromotionDubboManageService);
			productPromotion.setCurrent_inventory(temp.getCurrentInventory());

			/* 若还能以特价购买，则根据活动实时库存判断能买多少 */
			if(productPromotion.getLimit_num() > 0){ //如果有个人限购
				if(productPromotion.getCurrent_inventory() - canBuyByPromotionPrice <= 0 ){
					if(productInfoDto.getProductCount() > productPromotion.getCurrent_inventory()  ){
						return productDrug.getShort_name()+"的特价活动，本次购买的活动商品数量"+productInfoDto.getProductCount()+"已超过活动实时库存"+productPromotion.getCurrent_inventory();
					}
				}else{
					if(productInfoDto.getProductCount() > canBuyByPromotionPrice ){
						return productDrug.getShort_name()+"的特价活动，每人限购"+productPromotion.getLimit_num() +"件,您已购买了" + buyedInHistory +
								"件,还能购买" + canBuyByPromotionPrice +"件。";
					}
				}
			}else{ //如果不限购
				if(productInfoDto.getProductCount() > productPromotion.getCurrent_inventory()  ){
					return productDrug.getShort_name()+"的特价活动，本次购买的活动商品数量"+productInfoDto.getProductCount()+"已超过活动实时库存"+productPromotion.getCurrent_inventory();
				}
			}
		}

		if(!UtilHelper.isEmpty(seller.getOrderSamount()) && productPriceCount.compareTo(seller.getOrderSamount()) < 0 ){
			return "你有部分商品金额低于供货商的发货标准!";
		}
		logger.info("统一校验订单商品接口 ：校验成功" );
		return "success";
	}


}