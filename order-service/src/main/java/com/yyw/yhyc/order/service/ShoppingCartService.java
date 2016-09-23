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

import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.appdto.AddressBean;
import com.yyw.yhyc.order.appdto.CartData;
import com.yyw.yhyc.order.appdto.CartGroupData;
import com.yyw.yhyc.order.appdto.CartProductBean;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.manage.ProductInventoryManage;
import com.yyw.yhyc.product.mapper.ProductInventoryMapper;
import com.yyw.yhyc.usermanage.bo.UsermanageReceiverAddress;
import com.yyw.yhyc.usermanage.mapper.UsermanageReceiverAddressMapper;
import com.yyw.yhyc.utils.MyConfigUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	 *
	 * @param shoppingCart
	 * @param userDto
     */
	public int updateNum(ShoppingCart shoppingCart, UserDto userDto) throws Exception{
		if(UtilHelper.isEmpty(shoppingCart) || UtilHelper.isEmpty(shoppingCart.getShoppingCartId())){
			return 0 ;
		}
		ShoppingCart oldShoppingCart =  shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());
		if(UtilHelper.isEmpty(oldShoppingCart)){
			return 0;
		}
		ProductInventory productInventory = new ProductInventory();
		productInventory.setSupplyId(oldShoppingCart.getSupplyId());//设置供应商Id
		productInventory.setSpuCode(oldShoppingCart.getSpuCode());//设置SPUCODE
		productInventory.setFrontInventory(shoppingCart.getProductCount());//获取当前数量
		//检查购物车库存数量
		Map<String, Object> map = productInventoryManage.findInventoryNumber(productInventory);
		String code = map.get("code").toString();
		if("0".equals(code) || "1".equals(code)){
			logger.info("检查购物车库存数量 ：商品(spuCode=" + oldShoppingCart.getSpuCode() + ")库存校验失败!resultMap=" + map );
			throw  new Exception(map.get("msg").toString());
		}
		ShoppingCart newShoppingCart = new ShoppingCart();
		newShoppingCart.setShoppingCartId(shoppingCart.getShoppingCartId());
		newShoppingCart.setProductCount(shoppingCart.getProductCount());
		newShoppingCart.setProductSettlementPrice( oldShoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
		newShoppingCart.setUpdateUser(userDto.getUserName());
		return shoppingCartMapper.update(newShoppingCart);
	}

	/**
	 * 加入进货单
	 * @param shoppingCart 进货单对象
	 * @return 成功失败标识（state：[S-->成功, F-->失败]），进货单商品数量，进货单订单金额
	 * @throws Exception
	 */
	public Map<String, Object> addShoppingCart(ShoppingCart shoppingCart) throws Exception{
		//加入进货单信息
		ShoppingCart condition = new ShoppingCart();
		condition.setCustId(shoppingCart.getCustId());
		condition.setSpuCode(shoppingCart.getSpuCode());
		condition.setSupplyId(shoppingCart.getSupplyId());
		List<ShoppingCart> shoppingCarts = shoppingCartMapper.listByProperty(condition);

		condition = new ShoppingCart();
		condition.setCustId(shoppingCart.getCustId());
		int count = shoppingCartMapper.findByCount(condition);
		if(count>=100 && UtilHelper.isEmpty(shoppingCarts))
			throw new Exception("进货单最多只能添加100个品种，请先下单。");

		//当加入的数量加上原有的数量大于可见库存 只能买当前最大库存
		int countByid=0;
		if(!UtilHelper.isEmpty(shoppingCarts)){
			countByid=shoppingCarts.get(0).getProductCount();
		}
		ProductInventory product = productInventoryMapper.findBySupplyIdSpuCode(shoppingCart.getSupplyId(), shoppingCart.getSpuCode());
		if((countByid+shoppingCart.getProductCount())>product.getFrontInventory()){
			shoppingCart.setProductCount(product.getFrontInventory());
		}else{
			shoppingCart.setProductCount(countByid + shoppingCart.getProductCount());
		}

		shoppingCart.setProductSettlementPrice(shoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
		if(UtilHelper.isEmpty(shoppingCarts)){//新增商品
			if(UtilHelper.isEmpty(shoppingCart.getProductCodeCompany())){
				shoppingCart.setProductCodeCompany(shoppingCart.getSpuCode());
			}
			shoppingCartMapper.save(shoppingCart);
		}else {//已经存在商品
			shoppingCart.setUpdateUser(shoppingCart.getCreateUser());
			shoppingCartMapper.updateProductCount(shoppingCart);
		}


		//查询商品数量和进化单金额
		Map<String, java.math.BigDecimal>  statisticsMap = shoppingCartMapper.queryShoppingCartStatistics(shoppingCart.getCustId());

		//封装返回信息
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", "S");
		map.put("productCount", statisticsMap.get("productCount") != null ? statisticsMap.get("productCount").intValue() : 0);
		map.put("sumPrice", statisticsMap.get("sumPrice"));

		return map;
	}

	/**
	 * 购物车首页数据获取
	 * @param userDto
	 * @param iProductDubboManageService
     * @return
     */
	public List<ShoppingCartListDto> index(UserDto userDto, IProductDubboManageService iProductDubboManageService){
		if(UtilHelper.isEmpty(userDto)){
			return null;
		}

		/* 获取购物车中的商品信息 */
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(userDto.getCustId());
		List<ShoppingCartListDto> allShoppingCart = shoppingCartMapper.listAllShoppingCart(shoppingCart);

		if(UtilHelper.isEmpty(allShoppingCart)){
			return allShoppingCart;
		}

		/* 处理商品信息： */
		for(ShoppingCartListDto shoppingCartListDto : allShoppingCart){
			if(UtilHelper.isEmpty(shoppingCartListDto) || UtilHelper.isEmpty(shoppingCartListDto.getShoppingCartDtoList()) ||
					UtilHelper.isEmpty(shoppingCartListDto.getSeller())){
				continue;
			}
			BigDecimal productPriceCount = new BigDecimal(0);
			for(ShoppingCartDto shoppingCartDto : shoppingCartListDto.getShoppingCartDtoList()){
				if(UtilHelper.isEmpty(shoppingCartDto)) continue;

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

				if(UtilHelper.isEmpty(iProductDubboManageService)){
					logger.error("购物车页面-查询商品信息失败,iProductDubboManageService = " + iProductDubboManageService);
					continue;
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
					productList = iProductDubboManageService.selectProductBySPUCodeAndSellerCode(map);
					logger.info("购物车页面-查询商品信息,响应参数:" + JSONArray.fromObject(productList));
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

				/* 如果该商品没有缺货且没有下架，则统计该供应商下的已买商品总额 */
				if( "2".equals(code) && 1 == putaway_status ){
					productPriceCount = productPriceCount.add(shoppingCartDto.getProductSettlementPrice());
				}
			}

			/* 计算是否符合订单起售金额 */
			shoppingCartListDto.setProductPriceCount(productPriceCount);
			if(productPriceCount.compareTo(shoppingCartListDto.getSeller().getOrderSamount()) > 0){
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
		Map map = new HashMap();
		map.put("spu_code", spuCode);
		map.put("type_id", "1");
		List picUrlList = null;
		try{
			logger.info("查询图片接口:请求参数：map=" + map);
			picUrlList = iProductDubboManageService.selectByTypeIdAndSPUCode(map);
			logger.info("查询图片接口:响应参数：picUrlList=" + picUrlList);
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
		}else{
			return MyConfigUtil.IMG_DOMAIN + file_path;
		}
	}

	/**
	 * 将进货单DTO转换成APP对应实体
	 * @param shoppingCartListDtos
	 * @return
	 */
	private CartData changeShopCartDtosToApp(List<ShoppingCartListDto> shoppingCartListDtos){
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
				cartProductBean.setStatusDesc(scd.getPutawayStatus());
				cartProductBean.setFactoryName(scd.getManufactures());
				cartProductBean.setVendorId(Integer.valueOf(scds.getSeller().getEnterpriseId()));
				cartProductBean.setVendorName(scds.getSeller().getEnterpriseName());
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
	public  Map<String,Object> deleteShopCarts(Integer custId,List<Integer> shoppingCartIds, IProductDubboManageService iProductDubboManageService){
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
		List<ShoppingCartListDto> shoppingCartListDtos = this.index(userDto, iProductDubboManageService);
		if(UtilHelper.isEmpty(shoppingCartListDtos)){
			resultMap.put("statusCode","0");
			return resultMap;
		}
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
	public  Map<String,Object> updateShopCart(Integer custId,Integer shoppingCartId,Integer quantity, IProductDubboManageService iProductDubboManageService){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setShoppingCartId(shoppingCartId);
		shoppingCart.setProductCount(quantity);
		UserDto userDto = new UserDto();
		userDto.setCustId(custId);
		try {
			this.updateNum(shoppingCart, userDto);
		}catch (Exception e){
			resultMap.put("statusCode","-3");
			resultMap.put("message","更新进货单失败!");
			return resultMap;
		}
		ShoppingCart sc = new ShoppingCart();
		sc.setCustId(custId);
		List<ShoppingCartListDto> shoppingCartListDtos = this.index(userDto, iProductDubboManageService);
		if(UtilHelper.isEmpty(shoppingCartListDtos)){
			resultMap.put("statusCode","0");
			return resultMap;
		}
		CartData cartData = this.changeShopCartDtosToApp(shoppingCartListDtos);
		resultMap.put("statusCode", "0");
		resultMap.put("data",cartData);
		return resultMap;
	}

	public Map<String,Object> getShopCartList(UserDto userDto, IProductDubboManageService iProductDubboManageService) {
		List<ShoppingCartListDto> allShoppingCart = this.index(userDto,iProductDubboManageService);
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
		Integer custId = 6066;
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
			addressBean.setAddressType("1".equals(address.getDefaultAddress().toString()) ? 0 : 1);
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


}