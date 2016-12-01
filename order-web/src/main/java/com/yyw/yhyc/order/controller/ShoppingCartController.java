/**
 *
 * Created By: XI
 * Created On: 2016-7-28 17:34:56
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.usermanage.interfaces.IUsermanageAuditDubbo;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yaoex.usermanage.model.UsermanageDrugScope;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.order.service.ShoppingCartService;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.product.service.ProductInventoryService;
import com.yyw.yhyc.utils.CacheUtil;
import org.search.remote.yhyc.ProductSearchInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yyw.yhyc.order.inteceptor.GetUserInteceptor.CACHE_PREFIX;

@Controller
@RequestMapping(value = "/shoppingCart")
public class ShoppingCartController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private ProductInventoryService productInventoryService;

	@Reference
	private IProductDubboManageService iProductDubboManageService;

	@Autowired
	private OrderService orderService;

	@Reference
	private ICustgroupmanageDubbo iCustgroupmanageDubbo;

	@Reference
	private ProductSearchInterface productSearchInterface;

	@Reference
	private IPromotionDubboManageService iPromotionDubboManageService;

	@Reference
	private IUsermanageAuditDubbo usermanageAudit;


	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public ShoppingCart getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return shoppingCartService.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<ShoppingCart> listPgShoppingCart(RequestModel<ShoppingCart> requestModel) throws Exception
	{
		Pagination<ShoppingCart> pagination = new Pagination<ShoppingCart>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return shoppingCartService.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@RequestBody ShoppingCart shoppingCart) throws Exception
	{
		shoppingCartService.save(shoppingCart);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(@RequestBody RequestListModel<Integer> requestListModel) throws Exception
	{
		shoppingCartService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void update(@RequestBody ShoppingCart shoppingCart) throws Exception
	{
		shoppingCartService.update(shoppingCart);
	}

	/**
	 * 进货单页面
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView model = new ModelAndView();
		UserDto userDto = super.getLoginUser();

		Long startTime = System.currentTimeMillis();
		List<ShoppingCartListDto> allShoppingCart = shoppingCartService.index(userDto,productSearchInterface,iCustgroupmanageDubbo);
		Long endTime = System.currentTimeMillis();
		logger.info("-----------------进货单列表Rest接口-----------------\n获取数据、处理所有数据总耗时：" + (endTime -  startTime) + "毫秒");

		model.addObject("allShoppingCart",allShoppingCart);
		model.setViewName("shoppingCart/index");
		return model;
	}

	/**
	 * 更新购物车中数量
	 * @param shoppingCart
	 * 
	 * @throws Exception
     */
	@RequestMapping(value = "/updateNum", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateNum(@RequestBody ShoppingCart shoppingCart) throws Exception {
		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId())){
			throw new Exception("登陆超时");
		}
		return shoppingCartService.updateNum(shoppingCart,userDto,iPromotionDubboManageService,iProductDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
	}


	/**
	 * 添加商品到进货单
	 * 请求数据格式如下：

	 {
		 "custId": 6066,                //采购商企业编号
		 "supplyId": "6067",            //供应商企业编号
		 "spuCode": "010BAA3040006",    // 商品SPU编码
		 "productId": "7",             //商品id
		 "productCount": 1,            //商品数量
		 "productPrice": 12,           //商品单价
		 "productCodeCompany": "3545451",  //商品的本公司编码
		 "promotionId":"123"            //活动商品的活动id（没有该字段则不传）
	 }

	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/addShoppingCart", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addShoppingCart(@RequestBody ShoppingCart shoppingCart){
		logger.info("_________________加入进货单(唯一http入口)________________________,\n原始数据：shoppingCart = " + shoppingCart);
		/* 获取登陆用户的企业信息 */
		UserDto userDto = getUserDto(request);
		Map<String, Object> resultMap = new HashMap<>();
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId()) || userDto.getCustId() <= 0){
			resultMap.put("state", "F");
			resultMap.put("loginFlag",0);
			resultMap.put("message","登陆超时");
			return resultMap;
		}else{
			resultMap.put("loginFlag",1);
		}

		/* 加入进货单前，校验用户、商品是否在销售区域范围内 */
		boolean checkUserAndProductResult = checkUserAndProduct(userDto,shoppingCart.getSpuCode(),shoppingCart.getSupplyId());
		if(!checkUserAndProductResult){
			logger.error("_________________加入进货单(唯一http入口)________________________添加进货单失败! 用户或者商品不在销售区域范围内" );
			resultMap.put("state", "F");
			resultMap.put("message","不好意思，您的所在地无法购买此商品，去官网看看其他商品吧。");
			return resultMap;
		}

		try{
			resultMap = shoppingCartService.addShoppingCart(shoppingCart,userDto,iPromotionDubboManageService,iProductDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
		}catch (Exception e){
			logger.error("_________________加入进货单(唯一http入口)________________________添加进货单失败! 报错信息：" + e.getMessage(),e);
			resultMap.put("state", "F");
			resultMap.put("message","添加进货单失败!");
			return resultMap;
		}
		logger.info("_________________加入进货单(唯一http入口)________________________,\n 返回数据：resultMap = " + resultMap);
		return resultMap;
	}





	/**
	 * 检查购物车中的商品合法信息
	 * @param shoppingCartList
	 * @throws Exception
	 */
	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> check(@RequestBody List<ShoppingCart> shoppingCartList) throws Exception {
		UserDto userDto = super.getLoginUser();

		Map<String,Object> resultMap = new HashMap<>();
		if(UtilHelper.isEmpty(shoppingCartList)){
			resultMap.put("result",false);
			resultMap.put("message","您的进货单中没有商品");
			return resultMap;
		}
		/* 查找出当前买家的进货单里面，有哪些供应商 */
		ShoppingCart shoppingCartQuery = new ShoppingCart();
		shoppingCartQuery.setCustId(userDto.getCustId());
		List<ShoppingCart> custIdAndSupplyIdList = shoppingCartService.listDistinctCustIdAndSupplyId(shoppingCartQuery);
		if(UtilHelper.isEmpty(custIdAndSupplyIdList)){
			resultMap.put("result",false);
			resultMap.put("message","您的进货单中没有商品");
			return resultMap;
		}

		OrderDto orderDto = null;
		List<ProductInfoDto> productInfoDtoList = null;
		ProductInfoDto productInfoDto = null;
		for(ShoppingCart custIdAndSupplyId : custIdAndSupplyIdList){
			if(UtilHelper.isEmpty(custIdAndSupplyId)) continue;
			orderDto = new OrderDto();
			orderDto.setCustId(custIdAndSupplyId.getCustId());
			orderDto.setSupplyId(custIdAndSupplyId.getSupplyId());

			/* 遍历进货单中 选中的商品 */
			productInfoDtoList = new ArrayList<>();
			for( ShoppingCart shoppingCart : shoppingCartList){
				if(UtilHelper.isEmpty(shoppingCart)) continue;
				if(custIdAndSupplyId.getSupplyId().equals(shoppingCart.getSupplyId())){
					productInfoDto = new ProductInfoDto();
					ShoppingCart temp = shoppingCartService.getByPK(shoppingCart.getShoppingCartId());
					if(UtilHelper.isEmpty(temp)) continue;
					productInfoDto.setId(temp.getProductId());
					productInfoDto.setProductName(temp.getProductName());
					productInfoDto.setSpuCode(temp.getSpuCode());
					productInfoDto.setProductPrice(temp.getProductPrice());
					productInfoDto.setProductCount(temp.getProductCount());
					productInfoDto.setPromotionId(temp.getPromotionId());
					productInfoDto.setPromotionName(temp.getPromotionName());
					productInfoDto.setFromWhere(temp.getFromWhere());
					productInfoDtoList.add(productInfoDto);
				}
			}
			if(productInfoDtoList.size() == 0) continue;
			orderDto.setProductInfoDtoList(productInfoDtoList);

			/* 商品信息校验 ： 检验商品上架、下架状态、价格、库存、订单起售量等一系列信息 */
			resultMap = orderService.validateProducts(userDto, orderDto,iCustgroupmanageDubbo,iProductDubboManageService,
					productSearchInterface,iPromotionDubboManageService);
			boolean result = (boolean) resultMap.get("result");
			if(!result){
				return resultMap;
			}
		}
		resultMap.put("result",true);
		return resultMap;
	}

	/**
	 * 获取当前登陆的用户信息
	 * @param request
	 * @return
	 */
	private UserDto getUserDto(HttpServletRequest request){
		logger.info("request.getHeader(\"mySessionId\")=" + request.getHeader("mySessionId"));
		String sessionId = request.getHeader("mySessionId");
		if(UtilHelper.isEmpty(sessionId)){
			return null;
		}
		String user = CacheUtil.getSingleton().get(CACHE_PREFIX + sessionId);
		logger.info("user-->" + user);
		//用户信息
		if(!UtilHelper.isEmpty(user)) {
			Map userMap = JSONObject.parseObject(user, HashMap.class);
			if(UtilHelper.isEmpty(userMap)){
				return null;
			}
			UserDto userDto = new UserDto();
			userDto.setUser(userMap);
			userDto.setUserName(userMap.get("username")+"");
			userDto.setCustId(Integer.valueOf(userMap.get("enterprise_id")+""));
			return userDto;
		}
		return null;
	}


	/**
	 * 加入进货单前，校验用户、商品是否在销售区域范围内
	 * @param userDto
	 * @param spuCode
	 * @param sellerCode
     */
	private boolean checkUserAndProduct(UserDto userDto,String spuCode, Integer sellerCode) {
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(spuCode) || UtilHelper.isEmpty(sellerCode)){
			return false;
		}
		List<String> sellerList = new ArrayList<String>();
		Map<String, Object> enInfo = usermanageAudit.querySellerByBuyerCode(String.valueOf(userDto.getCustId()));
		if ("success".equals(enInfo.get("status"))) {
			if (enInfo.get("data") != null) {
				List<Map<String, Object>> sellerMapList  = (List<Map<String, Object>>)((Map)enInfo.get("data")).get("sellerList");
				for (Map<String, Object> temp : sellerMapList) {
					sellerList.add(String.valueOf(temp.get("seller_code")));
				}
			}
		}

		logger.info("sellerList =====>" + sellerList);
		if (!sellerList.contains(String.valueOf(sellerCode))) {
			logger.error("加入进货单前，校验用户、商品是否在销售区域范围内-----不好意思，您的所在地无法购买此商品，去官网看看其他商品吧。\n sellerCode= " + sellerCode + ",sellerList = " + sellerList);
			return false;
		} else {
			String scope = getUseProductScope(userDto);
			String drugSecondCategory = "";
			Map paramMap = new HashMap();
			paramMap.put("spu_code", spuCode);
			paramMap.put("seller_code", sellerCode);
			logger.info("加入进货单前，校验用户、商品是否在销售区域范围内-----调用iProductDubboManageService.selectProductBySPUCodeAndSellerCode接口,请求参数：" + paramMap.toString());
			List<Map<String,Object>> list = iProductDubboManageService.selectProductBySPUCodeAndSellerCode(paramMap);
			logger.info("加入进货单前，校验用户、商品是否在销售区域范围内-----调用iProductDubboManageService.selectProductBySPUCodeAndSellerCode接口,响应参数：" + list.toString());
			if(list != null && list.size() > 0){
				drugSecondCategory = String.valueOf(list.get(0).get("drug_second_category"));
			}
			logger.info("checkShoppingCart ：scope = " + scope + ", drugSecondCategory = " + drugSecondCategory);
			if (UtilHelper.isEmpty(scope) || UtilHelper.isEmpty(drugSecondCategory) || scope.indexOf(drugSecondCategory) < 0) {
				logger.error("不好意思，您的所在地无法购买此商品，去官网看看其他商品吧。\n scope = " + scope + ",drugSecondCategory = " + drugSecondCategory + ",scope.indexOf(drugSecondCategory) = " + scope.indexOf(drugSecondCategory));
				return false;
			}
		}
		return true;
	}

	/**
	 * 加入进货单前，校验用户是否在销售区域范围内
	 * @param userDto
	 * @return
     */
	private String getUseProductScope(UserDto userDto) {
		String scope = "";
		if ( UtilHelper.isEmpty(userDto) || userDto.getCustId() <= 0) {
			return scope;
		}
		logger.info("加入进货单前，校验用户是否在销售区域范围内-----调用usermanageAudit.loadEnterpriseInfoForAudit接口,请求参数：" + userDto.getCustId());
		Map<String, Object> useInfor = usermanageAudit.loadEnterpriseInfoForAudit(userDto.getCustId());
		logger.info("加入进货单前，校验用户是否在销售区域范围内-----调用usermanageAudit.loadEnterpriseInfoForAudit接口,响应参数：" + useInfor);
		if (useInfor != null && "success".equals(useInfor.get("status"))) {
			Map<String, Object> data = (Map<String, Object>)useInfor.get("data");
			if(UtilHelper.isEmpty(data) || UtilHelper.isEmpty(data.get("drugScopeList"))){
				return scope;
			}
			List<UsermanageDrugScope> drugScopes = (List<UsermanageDrugScope>)data.get("drugScopeList");
			for (UsermanageDrugScope drugScope : drugScopes) {
				scope = scope + drugScope.getDrugScopeId() + ",";
			}
			if (!"".equals(scope)) {
				scope = scope.substring(0, scope.length() - 1);
			}
		}
		logger.info("加入进货单前，校验用户是否在销售区域范围内-----返回结果：scope = " + scope);
		return scope;
	}
}
