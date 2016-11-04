/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:50
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.appdto.AddressBean;
import com.yyw.yhyc.order.appdto.CartData;
import com.yyw.yhyc.order.appdto.CartGroupData;
import com.yyw.yhyc.order.appdto.CartProductBean;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.ShoppingCartFromWhereEnum;
import com.yyw.yhyc.order.manage.OrderManage;
import com.yyw.yhyc.order.mapper.OrderDetailMapper;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.dto.ProductPromotionDto;
import com.yyw.yhyc.product.manage.ProductInventoryManage;
import com.yyw.yhyc.product.mapper.ProductInventoryMapper;
import com.yyw.yhyc.usermanage.bo.UsermanageReceiverAddress;
import com.yyw.yhyc.usermanage.mapper.UsermanageReceiverAddressMapper;
import com.yyw.yhyc.utils.MyConfigUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.search.remote.yhyc.ProductSearchInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.ShoppingCartMapper;

@Service("shoppingCartService")
public class ShoppingCartService {

	private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

	private ShoppingCartMapper	shoppingCartMapper;
	private ProductInventoryManage productInventoryManage;
	@Autowired
	private ProductInventoryMapper productInventoryMapper;

	@Autowired
	private UsermanageReceiverAddressMapper receiverAddressMapper;

	@Autowired
	public void setProductInventoryManage(ProductInventoryManage productInventoryManage) {
		this.productInventoryManage = productInventoryManage;
	}

	@Autowired
	public void setShoppingCartMapper(ShoppingCartMapper shoppingCartMapper)
	{
		this.shoppingCartMapper = shoppingCartMapper;
	}

	@Autowired
	private OrderManage orderManage;


	@Autowired
	private OrderDetailMapper orderDetailMapper;


	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public ShoppingCart getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return shoppingCartMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ShoppingCart> list() throws Exception
	{
		return shoppingCartMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ShoppingCart> listByProperty(ShoppingCart shoppingCart)
			throws Exception
	{
		return shoppingCartMapper.listByProperty(shoppingCart);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<ShoppingCart> listPaginationByProperty(Pagination<ShoppingCart> pagination, ShoppingCart shoppingCart) throws Exception
	{
		List<ShoppingCart> list = shoppingCartMapper.listPaginationByProperty(pagination, shoppingCart);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return shoppingCartMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		shoppingCartMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(ShoppingCart shoppingCart) throws Exception
	{
		return shoppingCartMapper.deleteByProperty(shoppingCart);
	}

	/**
	 * 保存记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public void save(ShoppingCart shoppingCart) throws Exception
	{
		shoppingCartMapper.save(shoppingCart);
	}

	/**
	 * 更新记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public int update(ShoppingCart shoppingCart) throws Exception
	{
		return shoppingCartMapper.update(shoppingCart);
	}

	/**
	 * 根据条件查询记录条数
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public int findByCount(ShoppingCart shoppingCart) throws Exception
	{
		return shoppingCartMapper.findByCount(shoppingCart);
	}

	/**
	 * 查询当前采购商下面，进货单中所有的商品
	 * @param shoppingCart
	 * @return
     */
	public List<ShoppingCartListDto> listAllShoppingCart(ShoppingCart shoppingCart) {
		return shoppingCartMapper.listAllShoppingCart(shoppingCart);
	}

	public List<ShoppingCart>  listDistinctCustIdAndSupplyId(ShoppingCart shoppingCart){
		if(UtilHelper.isEmpty(shoppingCart) || UtilHelper.isEmpty(shoppingCart.getCustId())){
			return null;
		}
		return shoppingCartMapper.listDistinctCustIdAndSupplyId(shoppingCart);
	}

	public List<ShoppingCart>  listDistinctCustIdAndSupplyId(Integer custId){
		if(UtilHelper.isEmpty(custId) || custId <= 0){
			return null;
		}
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(custId);
		return listDistinctCustIdAndSupplyId(shoppingCart);
	}


	/**
	 *	修改进货单中商品的数量(公用、核心逻辑)
	 * @param shoppingCart
	 * @param userDto
     */
	public Map<String,Object> updateNum(ShoppingCart shoppingCart, UserDto userDto, IPromotionDubboManageService iPromotionDubboManageService,
										IProductDubboManageService iProductDubboManageService, ICustgroupmanageDubbo iCustgroupmanageDubbo,  ProductSearchInterface productSearchInterface) throws Exception{
		Map<String,Object> resultMap = new HashMap<>();
		if(UtilHelper.isEmpty(shoppingCart) || UtilHelper.isEmpty(shoppingCart.getShoppingCartId())){
			resultMap.put("resultCount",0);
			return resultMap ;
		}
		ShoppingCart oldShoppingCart =  shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());
		if(UtilHelper.isEmpty(oldShoppingCart)){
			resultMap.put("resultCount",0);
			return resultMap ;
		}

		if(!UtilHelper.isEmpty(shoppingCart.getProductCount()) && shoppingCart.getProductCount() > 9999999 ){
			logger.info("更新进货单商品数量超过限制！购买数量不能大于9999999，shoppingCart.getProductCount() = " + shoppingCart.getProductCount());
			throw  new Exception("购买数量不能大于9999999");
		}


		/* 检查该商品库存数量 */
		ProductInventory productInventory = new ProductInventory();
		productInventory.setSupplyId(oldShoppingCart.getSupplyId());//设置供应商Id
		productInventory.setSpuCode(oldShoppingCart.getSpuCode());//设置SPUCODE
		productInventory.setFrontInventory(shoppingCart.getProductCount());//获取当前数量
		Map<String, Object> map = productInventoryManage.findInventoryNumber(productInventory);
		String code = map.get("code").toString();
		if("0".equals(code) || "1".equals(code)){
			logger.info("检查购物车库存数量 ：商品(spuCode=" + oldShoppingCart.getSpuCode() + ")库存校验失败!resultMap=" + map );
			throw  new Exception("修改商品数量失败!");
		}

		/* 活动商品修改商品数量逻辑 */
		if( !UtilHelper.isEmpty(oldShoppingCart.getPromotionId()) && oldShoppingCart.getPromotionId() > 0 ) {
			oldShoppingCart.setProductCount(shoppingCart.getProductCount());
			oldShoppingCart.setProductSettlementPrice(oldShoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
			ShoppingCartDto normalProductShoppingCart = handleActivityProduct(oldShoppingCart,userDto,iPromotionDubboManageService,iProductDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
			resultMap.put("resultCount",2);
			resultMap.put("normalProductShoppingCart",normalProductShoppingCart);

		/* 普通商品修改商品数量逻辑 */
		}else{
			ShoppingCart newShoppingCart = new ShoppingCart();
			newShoppingCart.setShoppingCartId(shoppingCart.getShoppingCartId());
			newShoppingCart.setProductCount(shoppingCart.getProductCount());
			newShoppingCart.setProductSettlementPrice( oldShoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
			newShoppingCart.setUpdateUser(userDto.getUserName());
			newShoppingCart.setFromWhere(shoppingCart.getFromWhere());
			int  resultCount = shoppingCartMapper.update(newShoppingCart);
			resultMap.put("resultCount",resultCount);
		}
		return resultMap ;
	}

	/**
	 * 加入进货单(公用、核心逻辑)
	 * @param shoppingCart 进货单对象
	 * @return 成功失败标识（state：[S-->成功, F-->失败]），进货单商品数量，进货单订单金额
	 * @throws Exception
	 */
	public Map<String, Object> addShoppingCart(ShoppingCart shoppingCart,UserDto userDto,IPromotionDubboManageService iPromotionDubboManageService,
											   IProductDubboManageService iProductDubboManageService,ICustgroupmanageDubbo iCustgroupmanageDubbo,  ProductSearchInterface productSearchInterface) throws Exception{

		/* 默认添加商品的来源是进货单 */
		if(UtilHelper.isEmpty(shoppingCart.getFromWhere())){
			shoppingCart.setFromWhere(ShoppingCartFromWhereEnum.SHOPPING_CART.getFromWhere());
		}

		/* 加入进货单：查询商品是否存在 */
		ShoppingCart condition = new ShoppingCart();
		condition.setCustId(shoppingCart.getCustId());
		condition.setSpuCode(shoppingCart.getSpuCode());
		condition.setSupplyId(shoppingCart.getSupplyId());
		condition.setFromWhere(shoppingCart.getFromWhere());
		logger.info("加入进货单：查询商品是否存在，查询条件condition=" + condition);
		List<ShoppingCart> shoppingCarts = shoppingCartMapper.listByProperty(condition);
		logger.info("加入进货单：查询商品是否存在，查询结果shoppingCarts=" + shoppingCarts);

		/* 查询已添加商品的品种总数 */
		condition = new ShoppingCart();
		condition.setCustId(shoppingCart.getCustId());
		condition.setFromWhere(shoppingCart.getFromWhere());
		int count = shoppingCartMapper.findByCount(condition);
		if(count>=100 && UtilHelper.isEmpty(shoppingCarts))
			throw new Exception("最多只能添加100个品种，请先下单。");

		/* 单个商品的最大数量限制 */
		if(!UtilHelper.isEmpty(shoppingCart.getProductCount()) && shoppingCart.getProductCount() > 9999999 ){
			logger.info("更新进货单商品数量超过限制！购买数量不能大于9999999，shoppingCart.getProductCount() = " + shoppingCart.getProductCount());
			throw  new Exception("购买数量不能大于9999999");
		}

		/* 校验商品库存 */
		/* 当前加入商品的数量 + 购物车中已经加入的数量 > 可见库存, 则只能买当前最大库存 */
		int countByid=0;
		if(!UtilHelper.isEmpty(shoppingCarts)){
			countByid=shoppingCarts.get(0).getProductCount();
		}
		ProductInventory product = productInventoryMapper.findBySupplyIdSpuCode(shoppingCart.getSupplyId(), shoppingCart.getSpuCode());
		if(UtilHelper.isEmpty(product) || UtilHelper.isEmpty(product.getFrontInventory()) || product.getFrontInventory() <= 0){
			throw new Exception("商品没有库存");
		}
		if((countByid+shoppingCart.getProductCount())>product.getFrontInventory()){
			shoppingCart.setProductCount(product.getFrontInventory());
		}else{
			shoppingCart.setProductCount(countByid + shoppingCart.getProductCount());
		}
		shoppingCart.setProductSettlementPrice(shoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));


		ShoppingCartDto newNormalProductShoppingCart = null;
		/* 判断该商品是否是活动商品 */
		if(!UtilHelper.isEmpty(shoppingCart.getPromotionId()) && shoppingCart.getPromotionId() > 0 ){
			/* 处理活动商品 */
			logger.info("加入进货单：处理活动商品，shoppingCart=" + shoppingCart);
			newNormalProductShoppingCart = handleActivityProduct(shoppingCart,userDto,iPromotionDubboManageService,iProductDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
		}else{
			/* 处理普通商品(原来的逻辑，代码不变) */
			logger.info("加入进货单：处理普通商品");
			/* 新添加商品  或 添加已存在的商品逻辑 */
			if(UtilHelper.isEmpty(shoppingCarts)){
				if(UtilHelper.isEmpty(shoppingCart.getProductCodeCompany())){
					shoppingCart.setProductCodeCompany(shoppingCart.getSpuCode());
				}
				shoppingCartMapper.save(shoppingCart);
			}else {//已经存在商品
				shoppingCart.setUpdateUser(shoppingCart.getCreateUser());
				shoppingCartMapper.updateProductCount(shoppingCart);
			}
		}


		/* 统计已加入商品的品种总数 和 进货单的商品总额 */
		ShoppingCart query = new ShoppingCart();
		query.setCustId(shoppingCart.getCustId());
		query.setFromWhere(shoppingCart.getFromWhere());
		Map<String, java.math.BigDecimal>  statisticsMap = shoppingCartMapper.queryShoppingCartStatistics(query);


		//封装返回信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", "S");
		map.put("productCount", statisticsMap.get("productCount") != null ? statisticsMap.get("productCount").intValue() : 0);
		map.put("sumPrice", statisticsMap.get("sumPrice"));
		if(!UtilHelper.isEmpty(newNormalProductShoppingCart)){
			map.put("newNormalProductShoppingCart",newNormalProductShoppingCart);
		}
		return map;
	}


	/**
	 * 极速下单 获取商品列表
	 * @param userDto
	 * @param iProductDubboManageService
	 * @param fromWhere
     * @return
     */
	public List<ShoppingCartListDto> listForFastOrder(UserDto userDto, IProductDubboManageService iProductDubboManageService, int fromWhere,IPromotionDubboManageService iPromotionDubboManageService) {
		if(UtilHelper.isEmpty(userDto)){
			logger.info("当前登陆人的信息,userDto=" + userDto);
			return null;
		}

		/* 获取极速下单中的商品信息 */
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(userDto.getCustId());
		shoppingCart.setFromWhere(fromWhere);
		List<ShoppingCartListDto> allShoppingCart = shoppingCartMapper.listForFastOrder(shoppingCart);
		logger.info("数据库中查询的信息,allShoppingCart=" + allShoppingCart);

		if(UtilHelper.isEmpty(allShoppingCart)){
			return allShoppingCart;
		}

		/* 处理商品信息： */
		return handleProductInfo(allShoppingCart,iProductDubboManageService,iPromotionDubboManageService);
	}

	/**
	 * 购物车首页数据获取(PC端、App端通用逻辑)
	 * @param userDto
	 * @param iProductDubboManageService
     * @return
     */
	public List<ShoppingCartListDto> index(UserDto userDto, IProductDubboManageService iProductDubboManageService,IPromotionDubboManageService iPromotionDubboManageService){
		if(UtilHelper.isEmpty(userDto)){
			return null;
		}

		/* 获取购物车中的商品信息 */
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(userDto.getCustId());
		long startTime = System.currentTimeMillis();
		List<ShoppingCartListDto> allShoppingCart = shoppingCartMapper.listAllShoppingCart(shoppingCart);
		long endTime = System.currentTimeMillis();
		logger.info("购物车页面-从数据库中取的数据,耗时："+ (endTime - startTime) +"毫秒，allShoppingCart=" + allShoppingCart);

		if(UtilHelper.isEmpty(allShoppingCart)){
			return allShoppingCart;
		}

		/* 处理商品信息： */
		startTime = System.currentTimeMillis();
		List<ShoppingCartListDto> allShoppingCartAfterHandler  = handleProductInfo(allShoppingCart,iProductDubboManageService,iPromotionDubboManageService);
		endTime = System.currentTimeMillis();
		logger.info("购物车页面-处理所有商品的数据(包含处理每个商品调用Dubbo服务的时间),耗时："+ (endTime - startTime) +"毫秒");
		return allShoppingCartAfterHandler;

	}

	/**
	 * 处理商品信息
	 * @param allShoppingCart
	 * @param iProductDubboManageService
     * @return
     */
	private List<ShoppingCartListDto> handleProductInfo(List<ShoppingCartListDto> allShoppingCart, IProductDubboManageService iProductDubboManageService,IPromotionDubboManageService iPromotionDubboManageService) {
		if(UtilHelper.isEmpty(allShoppingCart)){
			return allShoppingCart;
		}
		long startTime ,endTime;
		for(ShoppingCartListDto shoppingCartListDto : allShoppingCart){
			if(UtilHelper.isEmpty(shoppingCartListDto) || UtilHelper.isEmpty(shoppingCartListDto.getShoppingCartDtoList()) ||
					UtilHelper.isEmpty(shoppingCartListDto.getSeller())){
				continue;
			}
			BigDecimal productPriceCount = new BigDecimal(0);
			startTime = System.currentTimeMillis();
			for(ShoppingCartDto shoppingCartDto : shoppingCartListDto.getShoppingCartDtoList()){
				if(UtilHelper.isEmpty(shoppingCartDto)) continue;

				shoppingCartDto = handleProductInfo(shoppingCartDto,iProductDubboManageService,iPromotionDubboManageService);

				if(UtilHelper.isEmpty(shoppingCartDto)) continue;

				/* 如果该商品没有缺货、没有下架、价格合法，则统计该供应商下的已买商品总额 */
				if( shoppingCartDto.isExistProductInventory() && 1 == shoppingCartDto.getPutawayStatus() && shoppingCartDto.getProductPrice().compareTo(new BigDecimal(0)) > 0){
					productPriceCount = productPriceCount.add(shoppingCartDto.getProductSettlementPrice());
				}
			}
			endTime = System.currentTimeMillis();
			logger.info("购物车页面-处理商品信息：该供应商(enterprizeCode =" + shoppingCartListDto.getSeller().getEnterpriseId() +",企业名称="+ shoppingCartListDto.getSeller().getEnterpriseName()+")下的所有商品处理，耗时："+(endTime-startTime)+"毫秒");

			/* 计算是否符合订单起售金额 */
			shoppingCartListDto.setProductPriceCount(productPriceCount);
			logger.info("购物车页面-计算是否符合订单起售金额:供应商[" + shoppingCartListDto.getSeller().getEnterpriseName() + "]("+shoppingCartListDto.getSeller().getEnterpriseId()+")的订单起售金额="
					+ shoppingCartListDto.getSeller().getOrderSamount() + ",在该供应商下购买的商品总额=" + productPriceCount);
			if(UtilHelper.isEmpty(shoppingCartListDto.getSeller().getOrderSamount()) || productPriceCount.compareTo(shoppingCartListDto.getSeller().getOrderSamount()) > 0){
				shoppingCartListDto.setNeedPrice(new BigDecimal(0));
			}else{
				BigDecimal needPrice = shoppingCartListDto.getSeller().getOrderSamount().subtract(productPriceCount);
				shoppingCartListDto.setNeedPrice(needPrice);
			}
		}
		return allShoppingCart;
	}

	public String getProductImgUrl(String spuCode, IProductDubboManageService iProductDubboManageService) {
		String filePath = "http://oms.yaoex.com/static/images/product_default_img.jpg";
		if(UtilHelper.isEmpty(spuCode) || UtilHelper.isEmpty(iProductDubboManageService)){
			return filePath;
		}
		long startTime = System.currentTimeMillis();
		Map map = new HashMap();
		map.put("spu_code", spuCode);
		map.put("type_id", "1");
		List picUrlList = null;
		try{
			logger.info("查询图片接口:请求参数：map=" + map);
			picUrlList = iProductDubboManageService.selectByTypeIdAndSPUCode(map);
			long endTime = System.currentTimeMillis();
			logger.info("查询图片接口:耗时"+(endTime - startTime)+"毫秒，响应参数：picUrlList=" + picUrlList);
		}catch (Exception e){
			logger.error("查询图片接口:响应异常：" + e.getMessage(),e);
			return filePath;
		}
		if(UtilHelper.isEmpty(picUrlList) ){
			logger.error("调用图片接口：无响应");
			return filePath;
		}
		JSONObject productJson = JSONObject.fromObject(picUrlList.get(0));
		String file_path = (String)productJson.get("file_path");
		if (UtilHelper.isEmpty(file_path)){
			return filePath;
		}

		/* 图片中文处理，只针对特定部位URL编码 */
		String head =  file_path.substring(0,file_path.lastIndexOf("/")+1);
		String body =  file_path.substring(file_path.lastIndexOf("/")+1,file_path.lastIndexOf("."));
		String foot =  file_path.substring(file_path.lastIndexOf("."),file_path.length());
		try {
			file_path =  head + URLEncoder.encode(body,"UTF-8") + foot;
		} catch (UnsupportedEncodingException e) {
			logger.error("查询图片接口:URLEncoder编码(UTF-8)异常:"+e.getMessage(),e);
			return filePath;
		}
		long endTime = System.currentTimeMillis();
		logger.info("查询图片接口:处理完成！耗时" + (endTime - startTime) + "毫秒");
		return  MyConfigUtil.IMG_DOMAIN + file_path;


	}

	/**
	 * 将进货单DTO转换成APP对应实体
	 * @param shoppingCartListDtos
	 * @return
	 */
	private CartData changeShopCartDtosToApp(List<ShoppingCartListDto> shoppingCartListDtos){
		if(UtilHelper.isEmpty(shoppingCartListDtos)) return null;
		CartData cartData = new CartData();
		int totalCount = 0;
		List<CartGroupData> shopCartList = new ArrayList<CartGroupData>();
		for(ShoppingCartListDto scds : shoppingCartListDtos){
			totalCount+=scds.getShoppingCartDtoList().size();
			CartGroupData cartGroupData = new CartGroupData();
			List<CartProductBean> products = new ArrayList<CartProductBean>();
			cartGroupData.setSupplyId(Long.parseLong(scds.getSeller().getEnterpriseId()));
			cartGroupData.setSupplyName(scds.getSeller().getEnterpriseName());
			cartGroupData.setProductTotalPrice(scds.getProductPriceCount());
			cartGroupData.setMinSalePrice(scds.getSeller().getOrderSamount());
			for (ShoppingCartDto scd : scds.getShoppingCartDtoList()){
				CartProductBean cartProductBean = new CartProductBean();
				cartProductBean.setShoppingCartId(scd.getShoppingCartId());
				cartProductBean.setQuantity(scd.getProductCount());
				cartProductBean.setProductId(scd.getProductId());
				cartProductBean.setProductPicUrl(scd.getProductImageUrl());
				cartProductBean.setProductName(scd.getProductName());
				cartProductBean.setSpec(scd.getSpecification());
				cartProductBean.setUnit(scd.getUnit());
				cartProductBean.setProductPrice(scd.getProductPrice());
				cartProductBean.setStockCount(scd.getProductInventory());
				cartProductBean.setBaseCount(scd.getSaleStart());
				cartProductBean.setStepCount(scd.getUpStep());
				cartProductBean.setStatusDesc(scd.getPutawayStatus());
				cartProductBean.setFactoryName(scd.getManufactures());
				cartProductBean.setVendorId(Integer.valueOf(scds.getSeller().getEnterpriseId()));
				cartProductBean.setVendorName(scds.getSeller().getEnterpriseName());
				cartProductBean.setSpuCode(scd.getSpuCode());
				products.add(cartProductBean);
			}
			cartGroupData.setProducts(products);
			shopCartList.add(cartGroupData);
		}
		cartData.setShopCartList(shopCartList);
		cartData.setTotalCount(totalCount);
		return cartData;
	}
	/**
	 * 删除进货单商品For App
	 * @param custId
	 * @param shoppingCartIds
	 * @return
	 */
	public  Map<String,Object> deleteShopCarts(Integer custId,List<Integer> shoppingCartIds, IProductDubboManageService iProductDubboManageService,IPromotionDubboManageService iPromotionDubboManageService){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		UserDto userDto = new UserDto();
		userDto.setCustId(custId);
		try{
			this.deleteByPKeys(shoppingCartIds);
		}catch (Exception e){
			resultMap.put("statusCode","-3");
			resultMap.put("message","删除进货单失败!");
			return resultMap;
		}

		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(custId);
		List<ShoppingCartListDto> shoppingCartListDtos = this.index(userDto, iProductDubboManageService,iPromotionDubboManageService);
		CartData cartData = this.changeShopCartDtosToApp(shoppingCartListDtos);
		resultMap.put("statusCode", "0");
		resultMap.put("data", cartData);
		return resultMap;
	}
	/**
	 * 更新进货单商品For App
	 * @param custId
	 * @param
	 * @return
	 */
	public  Map<String,Object> updateShopCart(Integer custId,Integer shoppingCartId,Integer quantity, IProductDubboManageService iProductDubboManageService,
											  IPromotionDubboManageService iPromotionDubboManageService,ICustgroupmanageDubbo iCustgroupmanageDubbo,  ProductSearchInterface productSearchInterface){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setShoppingCartId(shoppingCartId);
		shoppingCart.setProductCount(quantity);
		UserDto userDto = new UserDto();
		userDto.setCustId(custId);
		try {
			this.updateNum(shoppingCart, userDto,iPromotionDubboManageService,iProductDubboManageService,iCustgroupmanageDubbo, productSearchInterface);
		}catch (Exception e){
			resultMap.put("statusCode","-3");
			resultMap.put("message","更新进货单失败!");
			return resultMap;
		}
		ShoppingCart sc = new ShoppingCart();
		sc.setCustId(custId);
		List<ShoppingCartListDto> shoppingCartListDtos = this.index(userDto, iProductDubboManageService,iPromotionDubboManageService);
		if(UtilHelper.isEmpty(shoppingCartListDtos)){
			resultMap.put("statusCode","0");
			return resultMap;
		}
		CartData cartData = this.changeShopCartDtosToApp(shoppingCartListDtos);
		resultMap.put("statusCode", "0");
		resultMap.put("data",cartData);
		return resultMap;
	}

	public Map<String,Object> getShopCartList(UserDto userDto, IProductDubboManageService iProductDubboManageService,IPromotionDubboManageService iPromotionDubboManageService) {
		List<ShoppingCartListDto> allShoppingCart = this.index(userDto,iProductDubboManageService,iPromotionDubboManageService);
		CartData cartData = changeShopCartDtosToApp(allShoppingCart);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("statusCode", "0");
		resultMap.put("data",cartData);
		resultMap.put("message", "成功");
		return resultMap;
	}

	/**
	 * 收货地址列表
	 * @param userDto
	 * @return
     */
	public Object getDeliveryAddress(UserDto userDto){
		Integer custId = userDto.getCustId();
		UsermanageReceiverAddress receiverAddress = new UsermanageReceiverAddress();
		receiverAddress.setEnterpriseId(custId + "");
		List<UsermanageReceiverAddress> receiverAddressList = receiverAddressMapper.listByProperty(receiverAddress);
		return convertDataForApp(receiverAddressList);
	}

	private List<AddressBean> convertDataForApp(List<UsermanageReceiverAddress> receiverAddressList) {
		if(UtilHelper.isEmpty(receiverAddressList)) return null;
		List<AddressBean> addressBeanList = new ArrayList<>();
		AddressBean addressBean = null;
		for(UsermanageReceiverAddress address :receiverAddressList){
			if (UtilHelper.isEmpty(address)) continue;
			addressBean = new AddressBean();
			addressBean.setAddressId(address.getId());

//			app接口定义的addressType: 0是默认收货地址，1 其他地址
//			数据库中使用的addresssType: 0非默认地址；1:默认地址，
			Integer addressType = null;
			if(UtilHelper.isEmpty(address.getDefaultAddress()+"")){
				addressType = 1;
			}else if("1".equals(address.getDefaultAddress()+"")){
				addressType = 0;
			}else{
				addressType = 1;
			}

			addressBean.setAddressType(addressType);
			addressBean.setAddressProvince(address.getProvinceName());
			addressBean.setAddressCity(address.getCityName());
			addressBean.setAddressCounty(address.getDistrictName());
			addressBean.setAddressDetail(address.getAddress());
			addressBean.setDeliveryName(address.getReceiverName());
			addressBean.setDeliveryPhone(address.getContactPhone());
			addressBeanList.add(addressBean);
		}
		return addressBeanList;
	}



	/**
	 * 功能描述：处理活动商品的相关逻辑(包括控销、限购逻辑等)
	 * 使用场景：添加进货单、修改进货单时
	 * @param shoppingCart  原始数据
	 * @param userDto 	   当前登陆人的用户信息
	 * @return  超出活动商品限购数量后，进货单中新增的普通商品数据
	 */
	private ShoppingCartDto handleActivityProduct(ShoppingCart shoppingCart ,UserDto userDto,IPromotionDubboManageService iPromotionDubboManageService,
												  IProductDubboManageService iProductDubboManageService,ICustgroupmanageDubbo iCustgroupmanageDubbo,  ProductSearchInterface productSearchInterface) throws Exception {
		if(UtilHelper.isEmpty(shoppingCart)) return null;

		/* 该商品是否是活动商品 */
		if(UtilHelper.isEmpty(shoppingCart.getPromotionId()) || shoppingCart.getPromotionId() <= 0){
			return null;
		}

		/* 活动商品添加、修改前，获取该商品是否已经添加，如已经添加，则获取已经添加的数量 */
		ShoppingCart queryCondition = new ShoppingCart();
		queryCondition.setCustId(shoppingCart.getCustId());
		queryCondition.setSpuCode(shoppingCart.getSpuCode());
		queryCondition.setSupplyId(shoppingCart.getSupplyId());
		queryCondition.setFromWhere(shoppingCart.getFromWhere());
		queryCondition.setPromotionId(shoppingCart.getPromotionId());
		logger.info("加入进货单：查询活动商品是否存在，查询条件condition=" + queryCondition);
		List<ShoppingCart> shoppingCarts = shoppingCartMapper.listByProperty(queryCondition);
		logger.info("加入进货单：查询活动商品是否存在，查询结果shoppingCarts=" + shoppingCarts);
		/* 已经添加到购物车中的数量 */
		int addedInShoppingCart = 0 ;
		if(!UtilHelper.isEmpty(shoppingCarts) ){
			addedInShoppingCart = shoppingCarts.get(0).getProductCount();
		}

		Map<String,ShoppingCart> exceedActivityLimitedNumMap = null;
		/* 表示添加活动商品到购物车 */
		if(UtilHelper.isEmpty(shoppingCart.getShoppingCartId()) || shoppingCart.getShoppingCartId() <= 0){
			/* 校验是否超过活动商品限购数量 */
			exceedActivityLimitedNumMap = isExceedActivityLimitedNum(shoppingCart,addedInShoppingCart,iPromotionDubboManageService,userDto,iCustgroupmanageDubbo,productSearchInterface);
			if(!UtilHelper.isEmpty(exceedActivityLimitedNumMap) && !UtilHelper.isEmpty(exceedActivityLimitedNumMap.get("activityProduct"))){
				ShoppingCart activityProductShoppingCart = exceedActivityLimitedNumMap.get("activityProduct");
				if(!UtilHelper.isEmpty(activityProductShoppingCart)){
					String userName = UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getUserName()) ? "" : userDto.getUserName();
					activityProductShoppingCart.setCreateUser( userName);
					shoppingCartMapper.save(activityProductShoppingCart);
				}
			}

		/* 表示修改购物车中商品的数量 */
		}else{

			/* 校验是否超过活动商品限购数量 */
			exceedActivityLimitedNumMap = isExceedActivityLimitedNum(shoppingCart,addedInShoppingCart,iPromotionDubboManageService,userDto,iCustgroupmanageDubbo,productSearchInterface);
			if(!UtilHelper.isEmpty(exceedActivityLimitedNumMap) && !UtilHelper.isEmpty(exceedActivityLimitedNumMap.get("activityProduct"))){
				ShoppingCart activityProductShoppingCart = exceedActivityLimitedNumMap.get("activityProduct");
				if(!UtilHelper.isEmpty(activityProductShoppingCart)){
					String userName = UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getUserName()) ? "" : userDto.getUserName();
					ShoppingCart shoppingCartOrigin = shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());
					shoppingCartOrigin.setProductCount( activityProductShoppingCart.getProductCount());
					shoppingCartOrigin.setProductSettlementPrice(shoppingCartOrigin.getProductPrice().multiply(new BigDecimal(shoppingCartOrigin.getProductCount())));
					shoppingCartOrigin.setUpdateUser(userName);
					shoppingCartMapper.update(shoppingCartOrigin);

				}
			}
		}

		if(UtilHelper.isEmpty(exceedActivityLimitedNumMap)){
			return null;
		}

		/* 若超出活动商品限购数量，则新增一条普通商品数据到进货单 */
		ShoppingCartDto shoppingCartDto = null;
		ShoppingCart normalProductShoppingCart = exceedActivityLimitedNumMap.get("normalProduct");
		if(!UtilHelper.isEmpty(normalProductShoppingCart)){

			/* 判断是否已经以原价新增过该商品，如果新增过，则修改数量 */
			queryCondition = new ShoppingCart();
			queryCondition.setCustId(shoppingCart.getCustId());
			queryCondition.setSpuCode(shoppingCart.getSpuCode());
			queryCondition.setSupplyId(shoppingCart.getSupplyId());
			queryCondition.setFromWhere(shoppingCart.getFromWhere());
			logger.info("加入进货单：是否已经以原价新增过该商品，查询条件condition=" + queryCondition);
			shoppingCarts = shoppingCartMapper.listByProperty(queryCondition);
			logger.info("加入进货单：是否已经以原价新增过该商品，查询结果shoppingCarts=" + shoppingCarts);
			if(!UtilHelper.isEmpty(shoppingCarts) && shoppingCarts.size() == 1){
				queryCondition = shoppingCarts.get(0);
			}else{
				queryCondition.setProductCount(0);
				logger.error("加入进货单：是否已经以原价新增过该商品，数据库有多条该商品数据");
			}
			if(!UtilHelper.isEmpty(queryCondition.getPromotionId())){
				return null;
			}

			/* 查询商品库存 */
			ProductInventory product = productInventoryMapper.findBySupplyIdSpuCode(shoppingCart.getSupplyId(), shoppingCart.getSpuCode());
			if(UtilHelper.isEmpty(product) || UtilHelper.isEmpty(product.getFrontInventory()) || product.getFrontInventory() <= 0){
				throw new Exception("商品没有库存");
			}
			if((queryCondition.getProductCount() + normalProductShoppingCart.getProductCount())>product.getFrontInventory()){
				normalProductShoppingCart.setProductCount(product.getFrontInventory());
			}else{
				normalProductShoppingCart.setProductCount(queryCondition.getProductCount() + normalProductShoppingCart.getProductCount());
			}
			normalProductShoppingCart.setProductSettlementPrice(normalProductShoppingCart.getProductPrice().multiply(new BigDecimal(normalProductShoppingCart.getProductCount())));

			/* 修改原数据的数量 */
			if(!UtilHelper.isEmpty(queryCondition) && !UtilHelper.isEmpty(queryCondition.getShoppingCartId())){
				normalProductShoppingCart.setPromotionId(null);
				normalProductShoppingCart.setPromotionName(null);
				String userName = UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getUserName()) ? "" :userDto.getUserName();
				normalProductShoppingCart.setCreateUser(userName);
				shoppingCartMapper.update(normalProductShoppingCart);

			/* 新增一条数据 */
			}else{
				normalProductShoppingCart.setPromotionId(null);
				normalProductShoppingCart.setPromotionName(null);
				String userName = UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getUserName()) ? "" :userDto.getUserName();
				normalProductShoppingCart.setCreateUser(userName);
				shoppingCartMapper.save(normalProductShoppingCart);
			}

			List<ShoppingCart> newShoppingCartList = shoppingCartMapper.listByProperty(normalProductShoppingCart);
			if(newShoppingCartList != null && newShoppingCartList.size() == 1){
				normalProductShoppingCart = newShoppingCartList.get(0);
			}
			logger.info("若超出活动商品限购数量，则新增一条普通商品数据到进货单:normalProductShoppingCart=" + normalProductShoppingCart);

			/*  查询该商品的相关信息 */
			shoppingCartDto = convert(normalProductShoppingCart);
			shoppingCartDto = handleProductInfo(shoppingCartDto,iProductDubboManageService,iPromotionDubboManageService);
		}

		return shoppingCartDto;
	}


	/**
	 * ShoppingCart 转换成 ShoppingCartDto
	 * TODO 有时间优化这个方法(反射)
	 * @param shoppingCart
	 * @return
     */
	private ShoppingCartDto convert(ShoppingCart shoppingCart) {
		if(UtilHelper.isEmpty(shoppingCart)) return null;
		ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
		shoppingCartDto.setShoppingCartId(shoppingCart.getShoppingCartId());
		shoppingCartDto.setCustId(shoppingCart.getCustId());
		shoppingCartDto.setSkuId(shoppingCart.getSkuId());
		shoppingCartDto.setSpecification(shoppingCart.getSpecification());
		shoppingCartDto.setSupplyId(shoppingCart.getSupplyId());
		shoppingCartDto.setProductId(shoppingCart.getProductId());
		shoppingCartDto.setSpuCode(shoppingCart.getSpuCode());
		shoppingCartDto.setProductName(shoppingCart.getProductName());
		shoppingCartDto.setManufactures(shoppingCart.getManufactures());
		shoppingCartDto.setProductPrice(shoppingCart.getProductPrice());
		shoppingCartDto.setProductSettlementPrice(shoppingCart.getProductSettlementPrice());
		shoppingCartDto.setProductCount(shoppingCart.getProductCount());
		shoppingCartDto.setRemark(shoppingCart.getRemark());
		shoppingCartDto.setCreateUser(shoppingCart.getCreateUser());
		shoppingCartDto.setCreateTime(shoppingCart.getCreateTime());
		shoppingCartDto.setUpdateUser(shoppingCart.getUpdateUser());
		shoppingCartDto.setUpdateTime(shoppingCart.getUpdateTime());
		shoppingCartDto.setProductCodeCompany(shoppingCart.getProductCodeCompany());
		shoppingCartDto.setFromWhere(shoppingCart.getFromWhere());
		shoppingCartDto.setPromotionId(shoppingCart.getPromotionId());
		shoppingCartDto.setPromotionName(shoppingCart.getPromotionName());
		return shoppingCartDto;
	}

	/**
	 * 判断是否若超出活动商品限购数量
	 * @param shoppingCart 要检查的数据
	 * @return 活动商品的数据、普通商品的数据
     */
	private Map<String,ShoppingCart> isExceedActivityLimitedNum(ShoppingCart shoppingCart,int addedInShoppingCart, IPromotionDubboManageService iPromotionDubboManageService,
																UserDto userDto,ICustgroupmanageDubbo iCustgroupmanageDubbo,  ProductSearchInterface productSearchInterface) throws Exception {



		/* 接入何家兵的获取活动商品信息的接口,区分出是否超出活动限购数量 */
		if(UtilHelper.isEmpty(iPromotionDubboManageService)) {
			logger.error("购物车页面-查询商品参加活动信息,接口iPromotionDubboManageService:" + iPromotionDubboManageService);
			return null;
		}
		logger.info("购物车页面-查询商品参加活动信息,请求参数:" + shoppingCart);
		ProductPromotionDto productPromotionDto = orderManage.queryProductWithPromotion(iPromotionDubboManageService,shoppingCart.getSpuCode(),
				shoppingCart.getSupplyId()+"",shoppingCart.getPromotionId(),shoppingCart.getCustId()+"");
		logger.info("购物车页面-查询商品参加活动信息,响应参数:" + productPromotionDto);
		if(UtilHelper.isEmpty(productPromotionDto)){
			return null;
		}

		/* 活动商品的限购逻辑(目前只有特价促销这一种活动类型) */
		/* 1、当前购买的数量 < 最小起批量(minimumPacking)，  则不能购买，否则继续往下判断； */
		if(shoppingCart.getProductCount() < productPromotionDto.getMinimumPacking()){
			throw new Exception("该活动商品的购买数量小于活动最小起批量");
		}

//		2、判断是否可以特价购买
//		个人限购 - 活动商品历史购买= 理论上可以以特价购买的数量 ,若结果<0 ，只能以原价购买，购买的数量为：当前购买量。否则可以以特价活动购买，
//
//			如果可以以特价购买：
//			比较活动商品实时库存-理论上可以以特价购买的数量  ，
// 			若结果 <=0 ,以特价活动购买量=活动商品实时库存；否则以特价活动购买量=理论上可以以特价购买的数量

		int canBuyByProductPriceNum = 0; //能够以商品原价购买的数量
		int canBuyByPromotionPriceNum = 0;//能够以特价购买的数量


		/*查询该商品在该活动中的历史购买量*/
		int buyedInHistory = 0;
		if(!UtilHelper.isEmpty(shoppingCart.getPromotionId()) && shoppingCart.getPromotionId() > 0){
			buyedInHistory  = shoppingCartMapper.countBuyedNumInHistory(shoppingCart);
		}
		logger.info("判断是否若超出活动商品限购数量:本次购买数量=" + shoppingCart.getProductCount() + ",\n该商品在该活动中的历史购买量buyedInHistory=" + buyedInHistory+",\n个人限购=" + productPromotionDto.getLimitNum() + ",\n活动实时库存=" + productPromotionDto.getCurrentInventory()) ;

		/* 判断理论上可以以特价购买的数量 */
		int canBuyByPromotionPrice = productPromotionDto.getLimitNum() - buyedInHistory;
		if(canBuyByPromotionPrice <= 0 ){
			canBuyByProductPriceNum = shoppingCart.getProductCount() > addedInShoppingCart ? shoppingCart.getProductCount()-addedInShoppingCart : shoppingCart.getProductCount();
			canBuyByPromotionPriceNum = 0;
		}else{
			/* 先判断购物车里面已经添加了多少 */
			int canBeAdd = canBuyByPromotionPrice - addedInShoppingCart;
			//购物车中还能增加活动商品
			if(canBeAdd > 0  ){
				if(shoppingCart.getProductCount() > canBeAdd ){////继续添加到购物车,并添加一条原价商品
					canBuyByPromotionPriceNum = addedInShoppingCart + canBeAdd;
					canBuyByProductPriceNum = shoppingCart.getProductCount() - canBuyByPromotionPriceNum;
				}else{//继续添加到购物车
					if(productPromotionDto.getCurrentInventory() - shoppingCart.getProductCount() <= 0 ){
						canBuyByPromotionPriceNum = productPromotionDto.getCurrentInventory();
						canBuyByProductPriceNum = shoppingCart.getProductCount() -  productPromotionDto.getCurrentInventory();
					}else{
						canBuyByPromotionPriceNum = shoppingCart.getProductCount();
						canBuyByProductPriceNum = 0;
					}
				}
			}else{
				int canBeReduce =  shoppingCart.getProductCount() - canBuyByPromotionPrice;
				if(canBeReduce > 0){ //新增原价商品
					canBuyByPromotionPriceNum = 0;
					canBuyByProductPriceNum = canBeReduce;
				}else{ //特价商品减少
					//判断活动库存
					if(productPromotionDto.getCurrentInventory() - shoppingCart.getProductCount() <= 0 ){
						canBuyByPromotionPriceNum = productPromotionDto.getCurrentInventory();
						canBuyByProductPriceNum = shoppingCart.getProductCount() -  productPromotionDto.getCurrentInventory();
					}else{
						canBuyByPromotionPriceNum = shoppingCart.getProductCount();
						canBuyByProductPriceNum = 0;
					}

				}
			}
		}




		logger.info("判断是否若超出活动商品限购数量:能够以活动特价购买的商品数量canBuyByPromotionPriceNum=" + canBuyByPromotionPriceNum +",\n能够以商品原价购买的数量canBuyByProductPriceNum=" + canBuyByProductPriceNum) ;

		Map<String,ShoppingCart> resultMap = new HashMap<>();

		ShoppingCart activityProduct = null;
		if(canBuyByPromotionPriceNum > 0){
			activityProduct = new ShoppingCart();
			BeanUtils.copyProperties(shoppingCart,activityProduct);
			activityProduct.setProductCount(canBuyByPromotionPriceNum);
			activityProduct.setProductPrice(productPromotionDto.getPromotionPrice());
			activityProduct.setProductSettlementPrice(productPromotionDto.getPromotionPrice().multiply(new BigDecimal(canBuyByPromotionPriceNum)));
		}

		ShoppingCart normalProduct = null;
		if(canBuyByProductPriceNum > 0){
			/* 调用接口查询商品原价 */
			long startTime = System.currentTimeMillis();
			BigDecimal productPrice = orderManage.getProductPrice(shoppingCart.getSpuCode(),shoppingCart.getCustId(),shoppingCart.getSupplyId(),iCustgroupmanageDubbo,productSearchInterface);
			long endTime = System.currentTimeMillis();
			logger.info("判断是否若超出活动商品限购数量:查询商品原价,耗时("+(endTime - startTime)+")毫秒，productPrice=" + productPrice ) ;
			if(UtilHelper.isEmpty(productPrice) || productPrice.compareTo(new BigDecimal("0")) <= 0){
				logger.error("判断是否若超出活动商品限购数量:查询商品原价失败，productPrice=" + productPrice);
				throw new Exception("查询商品价格失败");
			}
			normalProduct = new ShoppingCart();
			BeanUtils.copyProperties(shoppingCart,normalProduct);
			normalProduct.setPromotionId(null);
			normalProduct.setPromotionName(null);
			normalProduct.setProductCount(canBuyByProductPriceNum);
			normalProduct.setProductPrice(productPrice);
			normalProduct.setProductSettlementPrice(productPrice.multiply(new BigDecimal(canBuyByProductPriceNum)));
		}

		logger.info("判断是否若超出活动商品限购数量:activityProduct(ShoppingCart)=" + activityProduct +",normalProduct(ShoppingCart)=" + normalProduct) ;
		//原活动商品的信息
		resultMap.put("activityProduct",activityProduct);
		//超出活动限购数量后，需要新增的普通商品信息
		resultMap.put("normalProduct",normalProduct);
		return resultMap;
	}


	/**
	 * 处理单个商品的信息
	 * @param shoppingCartDto
	 * @param iProductDubboManageService
     * @return
     */
	private ShoppingCartDto handleProductInfo(ShoppingCartDto shoppingCartDto, IProductDubboManageService iProductDubboManageService,
											  IPromotionDubboManageService iPromotionDubboManageService ) {
		long startTime = System.currentTimeMillis();
		long start,end;
		if(UtilHelper.isEmpty(shoppingCartDto)){
			return null;
		}
		if(UtilHelper.isEmpty(iProductDubboManageService)){
			logger.error("购物车页面-查询商品信息失败,iProductDubboManageService = " + iProductDubboManageService);
			return null;
		}

		/* 查询商品库存 */
		ProductInventory productInventory = new ProductInventory();
		productInventory.setSupplyId(shoppingCartDto.getSupplyId());
		productInventory.setSpuCode(shoppingCartDto.getSpuCode());
		productInventory.setFrontInventory(shoppingCartDto.getProductCount());
		logger.info("购物车页面-检查商品库存,请求参数productInventory=" + productInventory);
		Map<String, Object> resultMap = productInventoryManage.findInventoryNumber(productInventory);
		logger.info("购物车页面-检查商品库存,响应参数resultMap=" + resultMap);

		String frontInventory = resultMap.get("frontInventory") + "";
		shoppingCartDto.setProductInventory(UtilHelper.isEmpty(frontInventory) ? 0 : Integer.valueOf(frontInventory));
		String code = resultMap.get("code").toString();
		if("0".equals(code) || "1".equals(code)){
			shoppingCartDto.setExistProductInventory(false);
		}else{
			shoppingCartDto.setExistProductInventory(true);
		}

		/* 获取商品图片 */
		String productImgUrl = getProductImgUrl(shoppingCartDto.getSpuCode(),iProductDubboManageService);
		shoppingCartDto.setProductImageUrl(productImgUrl);


		/* 最小起批量、最小拆零包装   */
		Map map = new HashMap();
		map.put("spu_code", shoppingCartDto.getSpuCode());
		map.put("seller_code", shoppingCartDto.getSupplyId());

		int minimumPacking = 1;
		String unit = "";
		Integer saleStart = 1;
		List productList = null;
		Integer putaway_status = 0; //（客户组）商品上下架状态：t_product_putaway表中的state字段 （上下架状态 0未上架  1上架  2本次下架  3非本次下架 ）
		try{

			logger.info("购物车页面-查询商品信息,请求参数:" + map);
			start = System.currentTimeMillis();
			productList = iProductDubboManageService.selectProductBySPUCodeAndSellerCode(map);
			end = System.currentTimeMillis();
			logger.info("购物车页面-查询商品信息,耗时"+(end - start)+"毫秒，响应参数:" + JSONArray.fromObject(productList));

		}catch (Exception e){
			logger.error("购物车页面-查询商品信息失败:" + e.getMessage());
		}
		if(UtilHelper.isEmpty(productList) || productList.size() != 1){
			logger.error("购物车页面-查询的商品信息异常" );
		}else{
			JSONObject productJson = JSONObject.fromObject(productList.get(0));
			minimumPacking = UtilHelper.isEmpty(productJson.get("minimum_packing")+"") ? 1 : (int) productJson.get("minimum_packing");
			saleStart = UtilHelper.isEmpty(productJson.get("wholesale_num")+"") ? 1 : Integer.valueOf(productJson.get("wholesale_num")+"") ;
			unit = UtilHelper.isEmpty(productJson.get("unit")+"") ? "" : productJson.get("unit")+"";
			putaway_status = UtilHelper.isEmpty(productJson.get("putaway_status")+"") ? 0 : Integer.valueOf( productJson.get("putaway_status")+"");
		}

		shoppingCartDto.setMinimumPacking(minimumPacking); //最小拆零包装数量
		shoppingCartDto.setUnit(unit);//最小拆零包装单位
		shoppingCartDto.setSaleStart(saleStart);//起售量
		shoppingCartDto.setUpStep(minimumPacking); //每次增加、减少的 递增数量
		shoppingCartDto.setPutawayStatus(putaway_status); //上下架状态


		ProductPromotionDto productPromotionDto  = null;
		if(!UtilHelper.isEmpty(iPromotionDubboManageService) && !UtilHelper.isEmpty(shoppingCartDto.getPromotionId())){
			logger.info("购物车页面-查询商品参加活动信息,请求参数:" + shoppingCartDto);
			productPromotionDto = orderManage.queryProductWithPromotion(iPromotionDubboManageService,shoppingCartDto.getSpuCode(),shoppingCartDto.getSupplyId()+"",
					shoppingCartDto.getPromotionId(),shoppingCartDto.getCustId() + "");
			logger.info("购物车页面-查询商品参加活动信息,响应参数:" + productPromotionDto);
		}else{
			logger.error("购物车页面-查询商品参加活动信息,接口iPromotionDubboManageService:" + iPromotionDubboManageService);
		}

		if(!UtilHelper.isEmpty(productPromotionDto)){
			shoppingCartDto.setPromotionPrice(productPromotionDto.getPromotionPrice());
			shoppingCartDto.setPromotionMinimumPacking(productPromotionDto.getMinimumPacking());
			shoppingCartDto.setPromotionLimitNum(productPromotionDto.getLimitNum());
			shoppingCartDto.setPromotionSumInventory(productPromotionDto.getSumInventory());
			shoppingCartDto.setPromotionCurrentInventory(productPromotionDto.getCurrentInventory());
		}
		long endTime = System.currentTimeMillis();
		logger.info("处理单个商品的信息,总耗时："+(endTime - startTime)+"毫秒");
		return shoppingCartDto;
	}



}