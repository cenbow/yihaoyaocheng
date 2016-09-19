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

import java.util.HashMap;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.manage.ProductInventoryManage;
import com.yyw.yhyc.product.mapper.ProductInventoryMapper;
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
		//当加入的数量加上原有的数量大于可见库存 只能买当前最大库存
		int countByid=0;
		if(!UtilHelper.isEmpty(shoppingCarts)){
			countByid=shoppingCarts.get(0).getProductCount();
		}
		ProductInventory product = productInventoryMapper.findBySupplyIdSpuCode(shoppingCart.getSupplyId(), shoppingCart.getSpuCode());
		if((countByid+shoppingCart.getProductCount())>product.getFrontInventory()){
			shoppingCart.setProductCount(product.getFrontInventory()-countByid);
		}
		if(count>=100 && UtilHelper.isEmpty(shoppingCarts))
			throw new Exception("进货单最多只能添加100个品种，请先下单。");

		shoppingCart.setProductSettlementPrice(shoppingCart.getProductPrice());
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

				/* 最小起批量、最小拆零包装   */
				Map map = new HashMap();
				map.put("spu_code", shoppingCartDto.getSpuCode());
				map.put("seller_code", shoppingCartDto.getSupplyId());

				int minimumPacking = 1;
				String unit = "";
				Integer saleStart = 1;
				List productList = null;
				Integer putaway_status = 0;
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
					minimumPacking = UtilHelper.isEmpty(productJson.get("minimum_packing")) ? 1 : (int) productJson.get("minimum_packing");
					saleStart = UtilHelper.isEmpty(productJson.get("wholesale_num")) ? 1 : Integer.valueOf(productJson.get("wholesale_num")+"") ;
					unit = UtilHelper.isEmpty(productJson.get("unit")) ? "" : UtilHelper.isEmpty(productJson.get("unit")+"") ? "" : productJson.get("unit")+"";
					putaway_status = UtilHelper.isEmpty(productJson.get("putaway_status")+"") ? null : Integer.valueOf( productJson.get("putaway_status")+"");
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
}