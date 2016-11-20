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
import com.yyw.yhyc.order.appdto.*;
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
	 *	修改进货单中商品的数量(公用、核心逻辑,该方法提供给上层调用)
	 * @param shoppingCart  外部传过来的原始数据
	 * @param userDto        当前登录人的信息
	 * @return 返回数据(Map形式)，部分key值解释如下：

	    resultCount          操作记录条数
		normalProduct        普通商品的shoppingCartId
		normalProductNum     普通商品最终修改的数量
		activityProduct      活动商品的shoppingCartId
		activityProductNum   活动商品最终修改的数量
		normalProductShoppingCart
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
		long startTime = System.currentTimeMillis();
		if(shoppingCart.getProductCount() > oldShoppingCart.getProductCount()){
			//增加数量
			resultMap = this.increaseNum(shoppingCart,userDto,iPromotionDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
			if(!UtilHelper.isEmpty(resultMap) && !UtilHelper.isEmpty(resultMap.get("normalProductInfo"))){
				ShoppingCart normalProductInfo = (ShoppingCart) resultMap.get("normalProductInfo");
				ShoppingCartDto shoppingCartDto = convert(normalProductInfo);
				shoppingCartDto = handleProductInfo(shoppingCartDto,iProductDubboManageService,iPromotionDubboManageService);
				resultMap.put("normalProductInfo",shoppingCartDto);
			}
			long endTime = System.currentTimeMillis();
			logger.info("修改进货单中商品的数量:增加数量耗时" + (endTime - startTime) + "毫秒");

		}else{
			//减少数量
			resultMap =  this.reduceNum(shoppingCart,userDto,iPromotionDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
			long endTime = System.currentTimeMillis();
			logger.info("修改进货单中商品的数量:减少数量耗时" + (endTime - startTime) + "毫秒");
		}

		return  resultMap;
	}

	/**
	 * 加入进货单(公用、核心逻辑)
	 * @param shoppingCart 进货单对象
	 * @return 成功失败标识（state：[S-->成功, F-->失败]），进货单商品数量，进货单订单金额
	 * @throws Exception
	 */
	public Map<String, Object> addShoppingCart(ShoppingCart shoppingCart,UserDto userDto,IPromotionDubboManageService iPromotionDubboManageService,
											   IProductDubboManageService iProductDubboManageService,ICustgroupmanageDubbo iCustgroupmanageDubbo,  ProductSearchInterface productSearchInterface) throws Exception{
		logger.info("_________________加入进货单(公用、核心逻辑)________________________,\n原始数据：shoppingCart = " + shoppingCart);

		if(UtilHelper.isEmpty(shoppingCart)){
			throw new Exception("非法参数");
		}

		if(UtilHelper.isEmpty(shoppingCart.getProductCodeCompany())){
			shoppingCart.setProductCodeCompany(shoppingCart.getSpuCode());
		}

		/* 商品来源的字段：如果该字段没有，则默认添加商品的来源是进货单 */
		if(UtilHelper.isEmpty(shoppingCart.getFromWhere())){
			shoppingCart.setFromWhere(ShoppingCartFromWhereEnum.SHOPPING_CART.getFromWhere());
		}
		logger.info("加入进货单总入口，shoppingCart = " + shoppingCart);


		long startTime = System.currentTimeMillis();
		if( !UtilHelper.isEmpty(shoppingCart.getPromotionId()) &&  shoppingCart.getPromotionId() > 0 ){
			/* 处理活动商品 */
			this.addActivityProduct(shoppingCart,userDto,iPromotionDubboManageService, iCustgroupmanageDubbo, productSearchInterface);
		} else {
			/* 处理普通商品 */
			this.addNormalProduct(shoppingCart,userDto);
		}
		long endTime = System.currentTimeMillis();
		logger.info("加入进货单总入口，加入完成，耗时" +( endTime - startTime) + "毫秒");

		/* 统计已加入商品的品种总数 和 进货单的商品总额 */
		ShoppingCart query = new ShoppingCart();
		query.setCustId(shoppingCart.getCustId());
		query.setFromWhere(shoppingCart.getFromWhere());
		logger.info("加入进货单总入口，加入完成，查询统计信息条件：query = " + query);
		Map<String, java.math.BigDecimal>  statisticsMap = shoppingCartMapper.queryShoppingCartStatistics(query);
		logger.info("加入进货单总入口，加入完成，查询统计信息结果：statisticsMap = " + statisticsMap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", "S");
		map.put("productCount", statisticsMap.get("productCount") != null ? statisticsMap.get("productCount").intValue() : 0);
		map.put("sumPrice", statisticsMap.get("sumPrice"));
		return map;
	}


	/**
	 * 添加进货单接口--添加普通商品到进货单
	 * @param shoppingCart 外部传递过来的原始数据
	 * @return
	 */
	private Map<String,Object> addNormalProduct(ShoppingCart shoppingCart,UserDto userDto) throws Exception {

		/* 加入进货单：查询商品是否存在 */
		ShoppingCart condition = new ShoppingCart();
		condition.setCustId(shoppingCart.getCustId());
		condition.setSpuCode(shoppingCart.getSpuCode());
		condition.setSupplyId(shoppingCart.getSupplyId());
		condition.setFromWhere(shoppingCart.getFromWhere());
		logger.info("加入进货单：查询商品是否存在，查询条件condition=" + condition);
		List<ShoppingCart> shoppingCarts = shoppingCartMapper.listByProperty(condition);
		logger.info("加入进货单：查询商品是否存在，查询结果shoppingCarts=" + shoppingCarts);

		/* 如果没有添加过，则添加 */
		if(UtilHelper.isEmpty(shoppingCarts)){
			return this.saveNormalProductNum(shoppingCart,userDto);
		}

		/* 如果添加过,则累加数据 */
		return this.increaseNormalProductNum(shoppingCart,userDto);
	}

	/**
	 * 添加进货单接口--添加活动商品到进货单（含促销等活动）
	 * @param shoppingCart  外部传递过来的原始数据
	 * @return
	 */
	private Map<String, Object> addActivityProduct(ShoppingCart shoppingCart,UserDto userDto,IPromotionDubboManageService iPromotionDubboManageService,
												   ICustgroupmanageDubbo iCustgroupmanageDubbo,  ProductSearchInterface productSearchInterface) throws Exception {
		if(UtilHelper.isEmpty(shoppingCart) || UtilHelper.isEmpty(shoppingCart.getPromotionId()) || shoppingCart.getPromotionId() <= 0){
			throw new Exception("非法参数");
		}

		/*获取活动商品 实际上还能购买的数量*/
		Map<String,Object> queryMap = queryPromotionProductInfo(shoppingCart,iPromotionDubboManageService);
		int promotionProductNumStillCanBuy = (int) queryMap.get("stillCanBuy");
		shoppingCart = (ShoppingCart) queryMap.get("shoppingCart");


		/* 如果不能再以特价购买改活动商品，则查询该商品的原价，并以原价购买 */
		if(promotionProductNumStillCanBuy <= 0){
			BigDecimal productPrice = orderManage.getProductPrice(shoppingCart.getSpuCode(),shoppingCart.getCustId(),
					shoppingCart.getSupplyId(),iCustgroupmanageDubbo,productSearchInterface);
			if(UtilHelper.isEmpty(productPrice) || productPrice.compareTo(new BigDecimal("0"))<= 0 ){
				throw new Exception("查询商品价格失败");
			}
			shoppingCart.setProductPrice(productPrice);
			shoppingCart.setProductSettlementPrice(shoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
			this.addNormalProduct(shoppingCart,userDto);
			return  null;
		}


		/* 场景二：商品详情页或搜索列表页，页面上多次调用的添加购物车接口，达到累加商品数量的目的*/
		ShoppingCart queryCondition = new ShoppingCart();
		queryCondition.setCustId(shoppingCart.getCustId());
		queryCondition.setSupplyId(shoppingCart.getSupplyId());
		queryCondition.setSpuCode(shoppingCart.getSpuCode());
		queryCondition.setFromWhere(shoppingCart.getFromWhere());
		queryCondition.setPromotionId(shoppingCart.getPromotionId());
		logger.info("加入进货单：查询活动商品是否存在，查询条件condition=" + queryCondition);
		List<ShoppingCart> shoppingCarts = shoppingCartMapper.listByProperty(queryCondition);
		logger.info("加入进货单：查询活动商品是否存在，查询结果shoppingCarts=" + shoppingCarts);

		/* 场景：商品详情页或搜索列表页，页面上多次调用的添加购物车接口，达到累加商品数量的目的*/
		/* 1、购物车中再次次添加活动商品 */
		if(!UtilHelper.isEmpty(shoppingCarts)){
			return this.increaseActivityProductNum(shoppingCart,userDto,iPromotionDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
		}


		String userName = UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getUserName()) ? "" : userDto.getUserName();
		/* 2、购物车中首次添加活动商品 */
		/* 如果还可以以特价购买该活动商品，判断是否超过限购 */
		if(shoppingCart.getProductCount() > 0 && shoppingCart.getProductCount() > promotionProductNumStillCanBuy ){
			/* 先把特价买满;超过部分，则以商品原价去购买 */
			ShoppingCart activityProductShoppingCart = new ShoppingCart();
			BeanUtils.copyProperties(shoppingCart,activityProductShoppingCart);
			activityProductShoppingCart.setProductCount(promotionProductNumStillCanBuy);
			activityProductShoppingCart.setProductSettlementPrice(activityProductShoppingCart.getProductPrice().multiply(new BigDecimal(activityProductShoppingCart.getProductCount())));
			activityProductShoppingCart.setCreateUser(userName);
			shoppingCartMapper.save(activityProductShoppingCart);

			/* 超过限购量的部分，则以原价新增一条数据(saveOrUpdate) */
			BigDecimal productPrice = orderManage.getProductPrice(shoppingCart.getSpuCode(),shoppingCart.getCustId(),shoppingCart.getSupplyId(),iCustgroupmanageDubbo,productSearchInterface);
			if(UtilHelper.isEmpty(productPrice) || productPrice.compareTo(new BigDecimal("0"))<= 0){
				throw new Exception("查询商品价格失败");
			}
			ShoppingCart normalProductShoppingCart = new ShoppingCart();
			BeanUtils.copyProperties(shoppingCart,normalProductShoppingCart);
			normalProductShoppingCart.setShoppingCartId(null);
			normalProductShoppingCart.setPromotionId(null);
			normalProductShoppingCart.setPromotionName(null);
			normalProductShoppingCart.setProductPrice(productPrice);
			normalProductShoppingCart.setProductCount(shoppingCart.getProductCount() - promotionProductNumStillCanBuy);
			normalProductShoppingCart.setProductSettlementPrice(normalProductShoppingCart.getProductPrice().multiply(new BigDecimal(normalProductShoppingCart.getProductCount())));
			this.addNormalProduct(normalProductShoppingCart,userDto);

		}else if(shoppingCart.getProductCount() > 0 && shoppingCart.getProductCount() <= promotionProductNumStillCanBuy ){
			/* 以特价购买该活动商品 */
			shoppingCart.setCreateUser(userName);
			shoppingCart.setProductSettlementPrice(shoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
			shoppingCartMapper.save(shoppingCart);
		}else{
			throw new Exception("非法参数");
		}
		return null;

	}


	/**
	 * 获取活动商品 实际上还能购买的数量
	 * @param shoppingCart
	 * @param iPromotionDubboManageService
	 * @return  Map<String,Object> 里面有两个key。
	 * 			"stillCanBuy" 的key表示实际上还能购买的数量
	 * 		    "shoppingCart" 表示获取活动信息后的 ShoppingCart实体
	 * @throws Exception
     */
	private Map<String,Object> queryPromotionProductInfo(ShoppingCart shoppingCart,IPromotionDubboManageService iPromotionDubboManageService) throws Exception {
		if(UtilHelper.isEmpty(shoppingCart) || UtilHelper.isEmpty(shoppingCart.getPromotionId()) || shoppingCart.getPromotionId() <= 0){
			throw new Exception("非法参数");
		}
		Map<String,Object> resultMap = new HashMap<>();
		int stillCanBuy = 0;
		/* 接入何家兵的获取活动商品信息的接口,区分出是否超出活动限购数量 */
		if(UtilHelper.isEmpty(iPromotionDubboManageService)) {
			logger.error("购物车查询商品参加活动信息-获取该活动商品 理论上还能购买的数量,接口iPromotionDubboManageService:" + iPromotionDubboManageService);
			resultMap.put("stillCanBuy",stillCanBuy);
			return resultMap;
		}
		logger.info("购物车查询商品参加活动信息-获取该活动商品 理论上还能购买的数量,请求参数:" + shoppingCart);
		ProductPromotionDto productPromotionDto = orderManage.queryProductWithPromotion(iPromotionDubboManageService,shoppingCart.getSpuCode(),
				shoppingCart.getSupplyId()+"",shoppingCart.getPromotionId(),shoppingCart.getCustId()+"");
		logger.info("购物车查询商品参加活动信息-获取该活动商品 理论上还能购买的数量,响应参数:" + productPromotionDto);
		if(UtilHelper.isEmpty(productPromotionDto)){
			resultMap.put("stillCanBuy",stillCanBuy);
			return resultMap;
		}

		if(UtilHelper.isEmpty(shoppingCart.getProductPrice()) || shoppingCart.getProductPrice().compareTo(new BigDecimal(0)) <= 0){
			logger.info("非法商品价格! 传来的商品活动价格shoppingCart.getProductPrice() = " + shoppingCart.getProductPrice());
			throw new Exception("非法商品价格");
		}

		/* 活动价格异常 处理 */
		if(!UtilHelper.isEmpty(shoppingCart.getProductPrice()) && shoppingCart.getProductPrice().compareTo(productPromotionDto.getPromotionPrice()) != 0){
			logger.info("该商品活动价格发生变化! 查询到的商品活动价格：= " + productPromotionDto.getPromotionPrice() +", 购物车中的价格 = " + shoppingCart.getProductPrice());

			/* 添加商品到购物车时，传进来的商品价格不对 */
			if(UtilHelper.isEmpty(shoppingCart.getShoppingCartId()) || shoppingCart.getShoppingCartId() <= 0 ){
				shoppingCart.setProductPrice(productPromotionDto.getPromotionPrice());

			/* 修改购物车中活动商品数量时，商品的活动价格在实时变化 */
			}else{
				ShoppingCart oldShoppingCart = shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());
				if(!UtilHelper.isEmpty(oldShoppingCart) && !UtilHelper.isEmpty(oldShoppingCart.getShoppingCartId())){
					ShoppingCart temp = new ShoppingCart();
					temp.setShoppingCartId(oldShoppingCart.getShoppingCartId());
					temp.setProductPrice(productPromotionDto.getPromotionPrice());
					temp.setProductCount(oldShoppingCart.getProductCount());
					temp.setProductSettlementPrice(temp.getProductPrice().multiply(new BigDecimal(temp.getProductCount())));
					shoppingCartMapper.update(temp);
				}
			}
		}

		/* 活动商品的限购逻辑(目前只有特价促销这一种活动类型) */
		/* 当前购买的数量 < 最小起批量(minimumPacking)，  则不能购买 */
		if(shoppingCart.getProductCount() < productPromotionDto.getMinimumPacking()){
			throw new Exception("该活动商品的购买数量("+ shoppingCart.getProductCount() +")小于活动最小起批量("+ productPromotionDto.getMinimumPacking()+")");
		}

		/*查询该商品在该活动中的历史购买量*/
		int buyedInHistory = 0;
		if(!UtilHelper.isEmpty(shoppingCart.getPromotionId()) && shoppingCart.getPromotionId() > 0){
			buyedInHistory  = shoppingCartMapper.countBuyedNumInHistory(shoppingCart);
		}
		logger.info("判断是否若超出活动商品限购数量:本次购买数量=" + shoppingCart.getProductCount() + ",\n该商品在该活动中的历史购买量buyedInHistory=" + buyedInHistory+",\n个人限购=" + productPromotionDto.getLimitNum() + ",\n活动实时库存=" + productPromotionDto.getCurrentInventory()) ;

		/* 获取 理论上还能以特价购买改活动商品的数量 */
		stillCanBuy = productPromotionDto.getLimitNum() - buyedInHistory;

		/* 根据活动库存 判断实际上还能以特价购买改活动商品的数量 */
		if(productPromotionDto.getLimitNum() <= 0){
			/* 不限购 */
			stillCanBuy = productPromotionDto.getCurrentInventory();
		}else if(stillCanBuy > 0 && productPromotionDto.getCurrentInventory() > 0  &&  stillCanBuy > productPromotionDto.getCurrentInventory()){
			/* 超过活动实时库存 */
			stillCanBuy = productPromotionDto.getCurrentInventory();
		}else if(stillCanBuy > 0 && productPromotionDto.getCurrentInventory() > 0 && stillCanBuy <= productPromotionDto.getCurrentInventory()){

		}else{
			stillCanBuy = 0;
		}
		logger.info("购物车查询商品参加活动信息-获取该活动商品 实际上还能购买的数量,stillCanBuy = " + stillCanBuy);
		resultMap.put("stillCanBuy",stillCanBuy);
		shoppingCart.setPromotionName(productPromotionDto.getPromotionName());
		resultMap.put("shoppingCart",shoppingCart);
		return resultMap;
	}



	/**
	 * 更新购物车中数量接口--增加数量
	 * @param shoppingCart  外部传递过来的原始数据
	 * @return
	 */
	private Map<String, Object> increaseNum(ShoppingCart shoppingCart,UserDto userDto,IPromotionDubboManageService iPromotionDubboManageService,
											ICustgroupmanageDubbo iCustgroupmanageDubbo,  ProductSearchInterface productSearchInterface) throws Exception {
		if(UtilHelper.isEmpty(shoppingCart) || UtilHelper.isEmpty(shoppingCart.getShoppingCartId())){
			throw new Exception("非法参数");
		}
		ShoppingCart oldShoppingCart = shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());
		if(UtilHelper.isEmpty(oldShoppingCart)){
			throw new Exception("非法参数");
		}
		shoppingCart.setPromotionId(oldShoppingCart.getPromotionId());
		shoppingCart.setCustId(oldShoppingCart.getCustId());
		shoppingCart.setSupplyId(oldShoppingCart.getSupplyId());
		shoppingCart.setSpuCode(oldShoppingCart.getSpuCode());
		shoppingCart.setProductPrice(oldShoppingCart.getProductPrice());


		/* 判断是否是活动商品 还是普通商品 */
		if (!UtilHelper.isEmpty(oldShoppingCart.getPromotionId()) && oldShoppingCart.getPromotionId() > 0 ) {
			return  this.increaseActivityProductNum(shoppingCart,userDto,iPromotionDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
		} else {
			return  this.increaseNormalProductNum(shoppingCart,userDto);
		}
	}

	/**
	 * 更新购物车中数量接口--增加普通商品数量
	 * @param shoppingCart  外部传递过来的原始数据
	 * @return
	 */
	private Map<String, Object> increaseNormalProductNum(ShoppingCart shoppingCart,UserDto userDto) throws Exception {
		if(UtilHelper.isEmpty(shoppingCart)  || UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(shoppingCart.getProductCount())
				|| shoppingCart.getProductCount() <= 0){
			throw new Exception("非法参数");
		}
		Map<String, Object>  resultMap = new HashMap<>();

		/* 增加普通商品数量的数量有两种场景 */
		/* 场景一：在进货单页面，点击增加数量的按钮，这时传过来的商品数量shoppingCart.getProductCount() 为最终数据库中保存的数量 */
		ShoppingCart oldShoppingCart = null;
		if(!UtilHelper.isEmpty(shoppingCart.getShoppingCartId()) && shoppingCart.getShoppingCartId()  > 0 ){
			oldShoppingCart = shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());

		/* 场景二：在商品详情页、搜索列表页等， 多次点击添加购物车接口，这时传过来的商品数量shoppingCart.getProductCount() 为当次要累加的量 */
		}else{
			ShoppingCart queryCondition = new ShoppingCart();
			queryCondition.setCustId(shoppingCart.getCustId());
			queryCondition.setSupplyId(shoppingCart.getSupplyId());
			queryCondition.setSpuCode(shoppingCart.getSpuCode());
			queryCondition.setFromWhere(shoppingCart.getFromWhere());
			List<ShoppingCart> shoppingCartList = shoppingCartMapper.listByProperty(queryCondition);
			if(!UtilHelper.isEmpty(shoppingCartList)){
				oldShoppingCart = shoppingCartList.get(0);
			}
			if(UtilHelper.isEmpty(oldShoppingCart)){
				throw new Exception("非法数据");
			}
			shoppingCart.setProductCount(oldShoppingCart.getProductCount() + shoppingCart.getProductCount());
			shoppingCart.setProductPrice(oldShoppingCart.getProductPrice());
			shoppingCart.setProductSettlementPrice(shoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
		}
		if(UtilHelper.isEmpty(oldShoppingCart) || UtilHelper.isEmpty(oldShoppingCart.getProductCount()) || oldShoppingCart.getProductCount() <= 0){
			resultMap.put("resultCount",0);
			return resultMap;
		}


		/* 校验商品库存 */
		/* 当前加入商品的数量 + 购物车中已经加入的数量 > 可见库存, 则只能买当前最大库存 */
		ProductInventory product = productInventoryMapper.findBySupplyIdSpuCode(oldShoppingCart.getSupplyId(), oldShoppingCart.getSpuCode());
		if(UtilHelper.isEmpty(product) || UtilHelper.isEmpty(product.getFrontInventory()) || product.getFrontInventory() <= 0){
			throw new Exception("商品没有库存");
		}
		if( shoppingCart.getProductCount() > product.getFrontInventory()){
			shoppingCart.setProductCount(product.getFrontInventory());
		}
		shoppingCart.setShoppingCartId(oldShoppingCart.getShoppingCartId());
		shoppingCart.setProductPrice(oldShoppingCart.getProductPrice());
		shoppingCart.setProductSettlementPrice(shoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));

		return this.update(shoppingCart,userDto);
	}

	/**
	 * 添加购物车或更新购物车中数量接口--增加活动商品数量
	 * @param shoppingCart 外部传递过来的原始数据
	 * @param userDto
	 * @param iPromotionDubboManageService 实际上还能以特价购买的数量；
     * @return
     */
	private Map<String, Object> increaseActivityProductNum(ShoppingCart shoppingCart,UserDto userDto,IPromotionDubboManageService iPromotionDubboManageService,
														   ICustgroupmanageDubbo iCustgroupmanageDubbo,  ProductSearchInterface productSearchInterface) throws Exception {
		if(UtilHelper.isEmpty(shoppingCart) || UtilHelper.isEmpty(shoppingCart.getPromotionId()) || shoppingCart.getPromotionId() <= 0
				|| UtilHelper.isEmpty(userDto) ){
			throw new Exception("非法参数");
		}

		/* 获取该活动商品 实际上还能购买的数量 */
		Map<String,Object> queryMap = queryPromotionProductInfo(shoppingCart,iPromotionDubboManageService);
		int promotionProductNumStillCanBuy = (int) queryMap.get("stillCanBuy");

		/* 如果不能再以特价购买改活动商品，则查询该商品的原价，并以原价购买 */
		if(promotionProductNumStillCanBuy <= 0){
			BigDecimal productPrice = orderManage.getProductPrice(shoppingCart.getSpuCode(),shoppingCart.getCustId(),
					shoppingCart.getSupplyId(),iCustgroupmanageDubbo,productSearchInterface);
			if(UtilHelper.isEmpty(productPrice) || productPrice.compareTo(new BigDecimal("0"))<= 0 ){
				throw new Exception("查询商品价格失败");
			}
			shoppingCart.setProductPrice(productPrice);
			shoppingCart.setProductSettlementPrice(shoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
			this.addNormalProduct(shoppingCart,userDto);
			return  null;
		}

		Map<String,Object> map = new HashMap<>();

		/* 查询购物车中已添加的数量 */
		ShoppingCart oldShoppingCart = null;
		if(!UtilHelper.isEmpty(shoppingCart.getShoppingCartId()) && shoppingCart.getShoppingCartId() > 0){
			/* 场景一：进货单页面，点击增加按钮时，会传主键过来，增加已购买的商品数量 */
			/* 这种场景下 shoppingCart.getProductCount()：表示的是最终看到的的数量 */
			oldShoppingCart = shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());
			shoppingCart.setShoppingCartId(oldShoppingCart.getShoppingCartId());
			shoppingCart.setProductPrice(oldShoppingCart.getProductPrice());
			shoppingCart.setProductSettlementPrice(shoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));

		}else{
			/* 场景二：商品详情页或搜索列表页，页面上多次调用的添加购物车接口，达到累加商品数量的目的*/
			/* 这种场景下 shoppingCart.getProductCount()：表示的是当次要累加的数量 */
			ShoppingCart queryCondition = new ShoppingCart();
			queryCondition.setCustId(shoppingCart.getCustId());
			queryCondition.setSupplyId(shoppingCart.getSupplyId());
			queryCondition.setSpuCode(shoppingCart.getSpuCode());
			queryCondition.setFromWhere(shoppingCart.getFromWhere());
			queryCondition.setPromotionId(shoppingCart.getPromotionId());
			logger.info("加入进货单：查询活动商品是否存在，查询条件condition=" + queryCondition);
			List<ShoppingCart> shoppingCarts = shoppingCartMapper.listByProperty(queryCondition);
			logger.info("加入进货单：查询活动商品是否存在，查询结果shoppingCarts=" + shoppingCarts);

			if(!UtilHelper.isEmpty(shoppingCarts)){
				oldShoppingCart = shoppingCarts.get(0);
			}
			if(UtilHelper.isEmpty(oldShoppingCart)){
				throw new Exception("非法数据");
			}
			shoppingCart.setShoppingCartId(oldShoppingCart.getShoppingCartId());
			shoppingCart.setProductCount(oldShoppingCart.getProductCount() + shoppingCart.getProductCount());
			shoppingCart.setProductPrice(oldShoppingCart.getProductPrice());
			shoppingCart.setProductSettlementPrice(oldShoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
		}

		/* 原数据 是否买满。买满的话，以原价新增一条数据(saveOrUpdate)，买满则，把原数据买满，若有超过部分以原价新增一条数据(saveOrUpdate) */
		if( shoppingCart.getProductCount() <= promotionProductNumStillCanBuy){
			/* 若 原数据的数量 + 当前买的数量 <= 限购量 ，则表示还没买满，更新原数据中的数量 */
			shoppingCart.setUpdateUser(userDto.getUserName());
			logger.info("加入进货单:添加活动商品的数据shoppingCart = " + shoppingCart);
			int resultCount = shoppingCartMapper.update(shoppingCart);
			map.put("activityProduct",shoppingCart.getShoppingCartId());
			map.put("activityProductNum",shoppingCart.getProductCount());
			map.put("resultCount",resultCount);
			return map;
		}

		/* 若 原数据的数量 + 当前买的数量 > 限购量 ，则表示还没买满，原数据中的数量买满。超过限购量的部分，则以原价新增一条数据(saveOrUpdate)*/
		/* 原数据中的数量买满。 */
		ShoppingCart activityProductShoppingCart = new ShoppingCart();
		BeanUtils.copyProperties(shoppingCart,activityProductShoppingCart);
		activityProductShoppingCart.setProductCount(promotionProductNumStillCanBuy);
		activityProductShoppingCart.setProductSettlementPrice(activityProductShoppingCart.getProductPrice().multiply(new BigDecimal(activityProductShoppingCart.getProductCount())));
		activityProductShoppingCart.setUpdateUser(userDto.getUserName());
		logger.info("加入进货单:更新活动商品的数据shoppingCart = " + activityProductShoppingCart);
		int resultCount =  shoppingCartMapper.update(activityProductShoppingCart);
		map.put("resultCount",resultCount);
		map.put("activityProduct",activityProductShoppingCart.getShoppingCartId());
		map.put("activityProductNum",promotionProductNumStillCanBuy);

		/* 超过限购量的部分，则以原价新增一条数据(saveOrUpdate) */
		BigDecimal productPrice = orderManage.getProductPrice(oldShoppingCart.getSpuCode(),oldShoppingCart.getCustId(),oldShoppingCart.getSupplyId(),iCustgroupmanageDubbo,productSearchInterface);
		if(UtilHelper.isEmpty(productPrice) || productPrice.compareTo(new BigDecimal("0"))<= 0){
			throw new Exception("查询商品价格失败");
		}
		ShoppingCart normalProductShoppingCart = new ShoppingCart();
		BeanUtils.copyProperties(oldShoppingCart,normalProductShoppingCart);
		normalProductShoppingCart.setShoppingCartId(null);
		normalProductShoppingCart.setPromotionId(null);
		normalProductShoppingCart.setPromotionName(null);
		normalProductShoppingCart.setProductPrice(productPrice);
		normalProductShoppingCart.setProductCount(shoppingCart.getProductCount() - promotionProductNumStillCanBuy);
		normalProductShoppingCart.setProductSettlementPrice(normalProductShoppingCart.getProductPrice().multiply(new BigDecimal(normalProductShoppingCart.getProductCount())));
		Map<String ,Object> normalProductMap = this.addNormalProduct(normalProductShoppingCart,userDto);

		if(!UtilHelper.isEmpty(normalProductMap)){
			map.put("normalProduct",normalProductMap.get("normalProduct"));
			map.put("normalProductNum",normalProductMap.get("normalProductNum"));
			map.put("normalProductInfo",normalProductMap.get("normalProductInfo"));
		}

		map.put("resultCount",resultCount);
		return map;

	}

	/**
	 * 更新购物车中数量接口--减少数量
	 * @param shoppingCart  外部传递过来的原始数据
	 * @return
	 */
	private Map<String, Object> reduceNum(ShoppingCart shoppingCart,UserDto userDto,IPromotionDubboManageService iPromotionDubboManageService,
										  ICustgroupmanageDubbo iCustgroupmanageDubbo,  ProductSearchInterface productSearchInterface) throws Exception {
		if(UtilHelper.isEmpty(shoppingCart) || UtilHelper.isEmpty(shoppingCart.getShoppingCartId()) || UtilHelper.isEmpty(shoppingCart.getProductCount())){
			throw new Exception("非法参数");
		}
		ShoppingCart oldShoppingCart = shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());
		if(UtilHelper.isEmpty(oldShoppingCart)){
			throw new Exception("非法参数");
		}

		shoppingCart.setPromotionId(oldShoppingCart.getPromotionId());
		shoppingCart.setCustId(oldShoppingCart.getCustId());
		shoppingCart.setSupplyId(oldShoppingCart.getSupplyId());
		shoppingCart.setSpuCode(oldShoppingCart.getSpuCode());
		shoppingCart.setProductPrice(oldShoppingCart.getProductPrice());

		/* 判断是否是活动商品 还是普通商品 */
		if (!UtilHelper.isEmpty(shoppingCart.getPromotionId()) && shoppingCart.getPromotionId() > 0 ) {
			return  this.reduceActivityProductNum(shoppingCart,userDto,iPromotionDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
		} else {
			return  this.reduceNormalProductNum(shoppingCart,userDto);
		}
	}

	/**
	 * 更新购物车中数量接口--减少普通数量
	 * @param shoppingCart  外部传递过来的原始数据
	 * @return
	 */
	private Map<String, Object> reduceNormalProductNum(ShoppingCart shoppingCart,UserDto userDto) throws Exception {
		Map<String, Object>  resultMap = new HashMap<>();
		ShoppingCart oldShoppingCart = shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());
		if(UtilHelper.isEmpty(oldShoppingCart)){
			resultMap.put("resultCount",0);
			return resultMap;
		}

		/* 校验商品库存 */
		/* 当前修改商品的数量  > 可见库存, 则只能买当前最大库存 */
		ProductInventory product = productInventoryMapper.findBySupplyIdSpuCode(oldShoppingCart.getSupplyId(), oldShoppingCart.getSpuCode());
		if(UtilHelper.isEmpty(product) || UtilHelper.isEmpty(product.getFrontInventory()) || product.getFrontInventory() <= 0){
			throw new Exception("商品没有库存");
		}
		if( shoppingCart.getProductCount() > product.getFrontInventory()){
			shoppingCart.setProductCount(product.getFrontInventory());
		}
		shoppingCart.setProductPrice(oldShoppingCart.getProductPrice());
		shoppingCart.setProductSettlementPrice(shoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));

		return this.update(shoppingCart,userDto);
	}

	/**
	 * 更新购物车中数量接口--减少活动商品数量
	 * @param shoppingCart  外部传递过来的原始数据
	 * @return
	 */
	private Map<String, Object> reduceActivityProductNum(ShoppingCart shoppingCart,UserDto userDto,IPromotionDubboManageService iPromotionDubboManageService,
														 ICustgroupmanageDubbo iCustgroupmanageDubbo,  ProductSearchInterface productSearchInterface)throws Exception{
		if(UtilHelper.isEmpty(shoppingCart) || UtilHelper.isEmpty(shoppingCart.getShoppingCartId()) || UtilHelper.isEmpty(userDto)){
			throw new Exception("非法参数");
		}

		if(UtilHelper.isEmpty(iPromotionDubboManageService)){
			throw new Exception("查询活动商品信息失败");
		}
		/* 获取该活动商品 实际上还能购买的数量 */
		Map<String,Object> queryMap = queryPromotionProductInfo(shoppingCart,iPromotionDubboManageService);
		int promotionProductNumStillCanBuy = (int) queryMap.get("stillCanBuy");

		Map<String,Object> map = new HashMap<>();
		if(promotionProductNumStillCanBuy <= 0){
			//若改活动商品不能再次购买，则不做任何操作，直接返回
			map.put("resultCount",0);
			return map;
		}

		ShoppingCart oldShoppingCart = shoppingCartMapper.getByPK(shoppingCart.getShoppingCartId());
		if(UtilHelper.isEmpty(oldShoppingCart)){
			throw new Exception("非法数据");
		}
		int resultCount = 0;
		if(shoppingCart.getProductCount() > 0 && shoppingCart.getProductCount() <= promotionProductNumStillCanBuy){
			/* 允许减少数量 */
			shoppingCart.setUpdateUser(userDto.getUserName());
			resultCount = shoppingCartMapper.update(shoppingCart);
			map.put("activityProduct",shoppingCart.getShoppingCartId());
			map.put("activityProductNum",shoppingCart.getProductCount());
		}else if(shoppingCart.getProductCount() > 0 && shoppingCart.getProductCount() > promotionProductNumStillCanBuy){
			shoppingCart.setProductCount(promotionProductNumStillCanBuy);
			shoppingCart.setProductSettlementPrice(oldShoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
			shoppingCart.setUpdateUser(userDto.getUserName());
			resultCount = shoppingCartMapper.update(shoppingCart);
			map.put("activityProduct",shoppingCart.getShoppingCartId());
			map.put("activityProductNum",shoppingCart.getProductCount());
		}
		map.put("resultCount",resultCount);
		return map;
	}

	/**
	 * 更新购物车中数量接口-- 更新商品数量
	 * @param shoppingCart  外部传递过来的原始数据
	 * @return
	 */
	private Map<String, Object> update(ShoppingCart shoppingCart,UserDto userDto) throws Exception {
		Map<String, Object>  resultMap = new HashMap<>();
		if(UtilHelper.isEmpty(shoppingCart) || UtilHelper.isEmpty(shoppingCart.getShoppingCartId()) || UtilHelper.isEmpty(shoppingCart.getProductCount())){
			resultMap.put("resultCount",0);
			return resultMap;
		}

		/* 修改商品数量 */
		ShoppingCart newShoppingCart = new ShoppingCart();
		newShoppingCart.setShoppingCartId(shoppingCart.getShoppingCartId());
		newShoppingCart.setProductCount(shoppingCart.getProductCount());
		newShoppingCart.setProductSettlementPrice( shoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
		String userName = UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getUserName()) ? "" : userDto.getUserName();
		newShoppingCart.setUpdateUser(userName);
		logger.info("更新商品数量：newShoppingCart = " + newShoppingCart);
		int  resultCount = shoppingCartMapper.update(newShoppingCart);
		resultMap.put("resultCount",resultCount);

		if(UtilHelper.isEmpty(shoppingCart.getPromotionId()) || shoppingCart.getPromotionId() <= 0){
			resultMap.put("normalProduct",shoppingCart.getShoppingCartId());
			resultMap.put("normalProductNum",shoppingCart.getProductCount());
		}else{
			resultMap.put("activityProduct",shoppingCart.getShoppingCartId());
			resultMap.put("activityProductNum",shoppingCart.getProductCount());
		}
		return resultMap;
	}

	/**
	 * 购物车中数量新增一条普通商品数据
	 * @param shoppingCart  外部传递过来的原始数据
	 * @return
	 */
	private Map<String,Object> saveNormalProductNum(ShoppingCart shoppingCart,UserDto userDto) throws Exception {
		/* 查询已添加商品的品种总数 */
		ShoppingCart condition = new ShoppingCart();
		condition.setCustId(shoppingCart.getCustId());
		condition.setFromWhere(shoppingCart.getFromWhere());
		int count = shoppingCartMapper.findByCount(condition);
		if (count >= 100)  throw new Exception("最多只能添加100个品种，请先下单。");

		/* 单个商品的最大数量限制 */
		if(!UtilHelper.isEmpty(shoppingCart.getProductCount()) && shoppingCart.getProductCount() > 9999999 ){
			logger.info("更新进货单商品数量超过限制！购买数量不能大于9999999，shoppingCart.getProductCount() = " + shoppingCart.getProductCount());
			throw  new Exception("购买数量不能大于9999999");
		}

		/* 校验商品库存 */
		/* 当前加入商品的数量 > 可见库存, 则只能买当前最大库存 */
		ProductInventory product = productInventoryMapper.findBySupplyIdSpuCode(shoppingCart.getSupplyId(), shoppingCart.getSpuCode());
		if(UtilHelper.isEmpty(product) || UtilHelper.isEmpty(product.getFrontInventory()) || product.getFrontInventory() <= 0){
			throw new Exception("商品没有库存");
		}

		if((shoppingCart.getProductCount())>product.getFrontInventory()){
			shoppingCart.setProductCount(product.getFrontInventory());
		}
		shoppingCart.setProductSettlementPrice(shoppingCart.getProductPrice().multiply(new BigDecimal(shoppingCart.getProductCount())));
		if(UtilHelper.isEmpty(shoppingCart.getProductCodeCompany())){
			shoppingCart.setProductCodeCompany(shoppingCart.getSpuCode());
		}
		String userName = UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getUserName()) ? "" : userDto.getUserName();
		shoppingCart.setCreateUser(userName);
		logger.info("加入进货单：处理普通商品,shoppingCart=" + shoppingCart);
		shoppingCartMapper.save(shoppingCart);


		/* 返回数据到页面，供页面调用 */
		condition = new ShoppingCart();
		condition.setCustId(shoppingCart.getCustId());
		condition.setSupplyId(shoppingCart.getSupplyId());
		condition.setSpuCode(shoppingCart.getSpuCode());
		condition.setFromWhere(shoppingCart.getFromWhere());
		List<ShoppingCart> shoppingCartList = shoppingCartMapper.listByProperty(condition);
		Map<String, Object>  resultMap = new HashMap<>();
		if(!UtilHelper.isEmpty(shoppingCartList)){
			resultMap.put("normalProduct",shoppingCartList.get(0).getShoppingCartId());
			resultMap.put("normalProductNum",shoppingCartList.get(0).getProductCount());
			resultMap.put("normalProductInfo",shoppingCartList.get(0));
		}
		logger.info("加入进货单：处理普通商品,返回数据resultMap=" + resultMap);
		return resultMap;
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
		ProductPromotion productPromotion = null;
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

				if(!UtilHelper.isEmpty(scd.getPromotionId()) && scd.getPromotionId() > 0 ){
					productPromotion = new ProductPromotion();
					productPromotion.setPromotionId(scd.getPromotionId());
					productPromotion.setPromotionName(scd.getPromotionName());
					productPromotion.setPromotionType(scd.getPromotionType());
					productPromotion.setPromotionPrice(scd.getPromotionPrice());
					productPromotion.setLimitNum(scd.getPromotionLimitNum());
					productPromotion.setMinimumPacking(scd.getMinimumPacking());
					productPromotion.setCurrentInventory(scd.getPromotionCurrentInventory());
					cartProductBean.setProductPromotion(productPromotion);
				}

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
			shoppingCartDto.setPromotionType(productPromotionDto.getPromotionType());
		}
		long endTime = System.currentTimeMillis();
		logger.info("处理单个商品的信息,总耗时："+(endTime - startTime)+"毫秒");
		return shoppingCartDto;
	}



}