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
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.appdto.OrderBean;
import com.yyw.yhyc.order.appdto.OrderCreateBean;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.ShoppingCartFromWhereEnum;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.order.service.ShoppingCartService;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;
import com.yyw.yhyc.usermanage.service.UsermanageEnterpriseService;
import org.search.remote.yhyc.ProductSearchInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = {"/api/cart","/order/api/cart"})
public class ShoppingCartController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Reference
	private IProductDubboManageService iProductDubboManageService;

	@Autowired
	private OrderService orderService;

	@Reference
	private ICustgroupmanageDubbo iCustgroupmanageDubbo;

	@Autowired
	private UsermanageEnterpriseService enterpriseService;

	@Reference
	private ProductSearchInterface productSearchInterface;

	@Reference
	private IPromotionDubboManageService iPromotionDubboManageService;

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
	public void add(ShoppingCart shoppingCart) throws Exception
	{
		shoppingCartService.save(shoppingCart);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		shoppingCartService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(ShoppingCart shoppingCart) throws Exception
	{
		shoppingCartService.update(shoppingCart);
	}

	/**
	 * 根据多条主键值删除记录
	 * @return
	 */
	@RequestMapping(value = "/deleteShopCarts", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> deleteShopCarts(@RequestBody Map<String,List<Integer>> shoppingCartIdList) throws Exception {
		UserDto userDto = super.getLoginUser();
		int custId = userDto.getCustId();
		return shoppingCartService.deleteShopCarts(custId,shoppingCartIdList.get("shoppingCartIdList"),iProductDubboManageService,iPromotionDubboManageService);
	}

	/**
	 * 更新进货单数量
	 * @return
	 */
	@RequestMapping(value = "/updateShopCart", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateShopCart(@RequestBody Map<String,Integer> shoppingCart) throws Exception {
		UserDto userDto = super.getLoginUser();
		int custId = userDto.getCustId();
		Integer shoppingCartId = shoppingCart.get("shoppingCartId");
		Integer quantity = shoppingCart.get("quantity");
		return shoppingCartService.updateShopCart(custId,shoppingCartId,quantity,iProductDubboManageService,iPromotionDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
	}

	/**
	 * 获得进货单品种数量
	 * @return
     */
	@RequestMapping(value = "/cartAccount", method = RequestMethod.GET/*, headers = "api-version=1"*/)
	@ResponseBody
	public Map<String,Object> cartAccount(){
		UserDto userDto = super.getLoginUser();
		int custId = userDto.getCustId();

		Map<String,Object> result = new HashMap<>();

		if(UtilHelper.isEmpty(custId)) {
			return error(STATUS_CODE_REQUEST_PARAM_ERROR,"参数custId不能为空！");
		}

		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(custId);
		shoppingCart.setFromWhere(ShoppingCartFromWhereEnum.SHOPPING_CART.getFromWhere());
		try {
			int count = shoppingCartService.findByCount(shoppingCart);
			Map<String, Object> data = new HashMap<>();
			data.put("count", count);
			return ok(data);
		}catch (Exception e){
			logger.error(e.getMessage(), e);
			return error(STATUS_CODE_SYSTEM_EXCEPTION,e.getMessage());
		}
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
	 }

	 * @param shoppingCart
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/addShopCart", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addShoppingCart(@RequestBody ShoppingCart shoppingCart) throws Exception {

		/* 获取登陆用户的企业信息 */
		UserDto userDto = super.getLoginUser();

		Map<String, Object> map = new HashMap<>();
		if(UtilHelper.isEmpty(shoppingCart)){
			return error(STATUS_CODE_REQUEST_PARAM_ERROR,"非法参数");
		}
		Map<String, Object> result = null;
		try{
			shoppingCart.setCustId(userDto.getCustId());
			shoppingCart.setCreateUser(userDto.getUserName());
			result = shoppingCartService.addShoppingCart(shoppingCart,userDto,iPromotionDubboManageService,iProductDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
			result.put("totalCount",result.get("productCount"));
			result.put("result","成功");
		}catch (Exception e){
			logger.error(e.getMessage(),e);
			return  error(STATUS_CODE_SYSTEM_EXCEPTION,e.getMessage());
		}

		if(!UtilHelper.isEmpty(result) && "S".equals(result.get("state")+"")){
			return ok(result);
		}else{
			result = new HashMap<>();
			result.put("totalCount",0);
			result.put("result","添加商品到进货单失败");
			return returnResult(STATUS_CODE_REQUEST_SUCCESS,"成功",result);
		}
	}

	/**
	 * 进货单列表
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/getShopCartList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopCartList() throws Exception {
		/* 获取登陆用户的企业信息 */
		UserDto userDto = super.getLoginUser();
		return shoppingCartService.getShopCartList(userDto,iProductDubboManageService,iPromotionDubboManageService);
	}

	/**
	 * 获取收获的采购商的地址列表
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/getDeliveryAddress", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getDeliveryAddress() throws Exception {

		/* 获取登陆用户的企业信息 */
		UserDto userDto = super.getLoginUser();

		Object data = shoppingCartService.getDeliveryAddress(userDto);
		return ok(data);
	}

	/**
	 * 提交订单接口
	   请求参数的数据格式如下：
	 {
		 "addressId": 1,
		 "payType": 1,
		 "billType": 1,
		 "orderList": [
			 {
			 	"supplyId": 6067,
			 	"shopCartIdList": [12,123],
			 	"leaveMsg": "请速发货!"
			 },

			 {
	 			"supplyId": 6065,
	 			"shopCartIdList": [13,133],
	 			"leaveMsg": "请速发货!"
	 		 }
	 	 ]
	 }


	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/submitShopCart", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> submitShopCart(@RequestBody OrderCreateBean orderCreateBean,@RequestHeader("os") String os) throws Exception {

		/* 获取登陆用户的企业信息 */
		UserDto userDto = super.getLoginUser();

		//订单来源
		if(UtilHelper.isNoEmpty(os)&&os.equals("os")){
			orderCreateBean.setSource(3);
		}else if(UtilHelper.isNoEmpty(os)&&os.equals("android")){
			orderCreateBean.setSource(2);
		}

		/* 把APP端的数据格式，转成与PC通用的数据格式 */
		OrderCreateDto orderCreateDto = convertDataForApp(userDto,orderCreateBean);

		if(UtilHelper.isEmpty(orderCreateDto))throw new Exception("非法参数");

		/* 校验采购商状态、资质 */
		UsermanageEnterprise buyer = enterpriseService.getByEnterpriseId(userDto.getCustId()+"");
		if(UtilHelper.isEmpty(buyer)){
			return error(STATUS_CODE_REQUEST_PARAM_ERROR,"采购商不存在");
		}
		orderCreateDto.setUserDto(userDto);

		if(UtilHelper.isEmpty(orderCreateDto.getOrderDtoList()))throw new Exception("非法参数");
		Map<String,Object> map = new HashMap<String, Object>();
		for(OrderDto orderDto : orderCreateDto.getOrderDtoList()){

			orderDto.setBuyer(buyer);
			orderDto.setCustId(Integer.valueOf(buyer.getEnterpriseId()));
			orderDto.setCustName(buyer.getEnterpriseName());

			/* 校验要供应商状态、资质 */
			UsermanageEnterprise seller = enterpriseService.getByEnterpriseId(orderDto.getSupplyId() + "");
			if(UtilHelper.isEmpty(seller)){
				return error(STATUS_CODE_REQUEST_PARAM_ERROR,"供应商不存在");
			}
			orderDto.setSeller(seller);
			orderDto.setSupplyName(seller.getEnterpriseName());

			/* 商品信息校验 ： 检验商品上架、下架状态、价格、库存、订单起售量等一系列信息 */
			map = orderService.validateProducts(userDto,orderDto,iCustgroupmanageDubbo,iProductDubboManageService, productSearchInterface,iPromotionDubboManageService);
			boolean result = (boolean) map.get("result");
			String message = (String) map.get("message");
			if(!result){
				return error(STATUS_CODE_SYSTEM_EXCEPTION,message);
			}
		}

		/* 创建订单 */
		Map<String,Object> newOrderMap = orderService.createOrder(orderCreateDto,iPromotionDubboManageService);
		List<Order> orderList = (List<Order>) newOrderMap.get("orderNewList");

		List<String> flowIdList = new ArrayList<>();
		if(!UtilHelper.isEmpty(orderList)){
			for(Order order : orderList){
				if(UtilHelper.isEmpty(order)) continue;
				flowIdList.add(order.getFlowId());
			}
		}
		Map dataMap = new HashMap();
		dataMap.put("orderIdList",flowIdList);
		return ok(dataMap);
	}

	/**
	 * 提交订单：把APP端的数据格式，转成与PC通用的数据格式
	 * @param userDto
	 * @param orderCreateBean
	 * @return
	 * @throws Exception
     */
	private OrderCreateDto convertDataForApp(UserDto userDto, OrderCreateBean orderCreateBean) throws Exception {
		if(UtilHelper.isEmpty(userDto) ||UtilHelper.isEmpty(userDto.getCustId())){
			throw new Exception("非法参数");
		}
		if(UtilHelper.isEmpty(orderCreateBean)){
			throw new Exception("非法参数");
		}
		if(UtilHelper.isEmpty(orderCreateBean.getAddressId()) || orderCreateBean.getAddressId() <= 0){
			throw new Exception("非法地址参数");
		}
		if(orderCreateBean.getPayType() != 1){
			throw new Exception("非法支付类型");
		}
		if(orderCreateBean.getBillType() != 1 && orderCreateBean.getBillType() != 2 ){
			throw new Exception("非法发票类型");
		}
		if(UtilHelper.isEmpty(orderCreateBean.getOrderList())){
			throw new Exception("非法参数");
		}

		OrderCreateDto orderCreateDto = new OrderCreateDto();
		orderCreateDto.setBillType(orderCreateBean.getBillType());
		orderCreateDto.setReceiveAddressId(orderCreateBean.getAddressId());

		List<OrderDto> orderDtoList = new ArrayList<>();
		OrderDto orderDto = null;
		List<ProductInfoDto> productInfoDtoList = null;
		ProductInfoDto productInfoDto = null;

		ShoppingCart shoppingCart = null;

		for(OrderBean orderBean : orderCreateBean.getOrderList()){
			if(UtilHelper.isEmpty(orderBean) || UtilHelper.isEmpty(orderBean.getSupplyId()) ||
					UtilHelper.isEmpty(orderBean.getShopCartIdList())){
				continue;
			}
			productInfoDtoList = new ArrayList<>();
			for(Integer shopCartId : orderBean.getShopCartIdList()){
				if(UtilHelper.isEmpty(shopCartId)) continue;
				shoppingCart = shoppingCartService.getByPK(shopCartId);
				if(UtilHelper.isEmpty(shoppingCart) || !orderBean.getSupplyId().equals(shoppingCart.getSupplyId())
						|| userDto.getCustId() != shoppingCart.getCustId() ){
					continue;
				}
				productInfoDto = new ProductInfoDto();
				productInfoDto.setId(shoppingCart.getProductId());
				productInfoDto.setSpuCode(shoppingCart.getSpuCode());
				productInfoDto.setProductPrice(shoppingCart.getProductPrice());
				productInfoDto.setProductCount(shoppingCart.getProductCount());
				productInfoDto.setProductCodeCompany(shoppingCart.getProductCodeCompany());
				productInfoDto.setPromotionId(shoppingCart.getPromotionId());
				productInfoDto.setPromotionName(shoppingCart.getPromotionName());
				productInfoDtoList.add(productInfoDto);
			}

			if(UtilHelper.isEmpty(productInfoDtoList) ) continue;

			orderDto = new OrderDto();
			orderDto.setCustId(userDto.getCustId());
			orderDto.setSupplyId(orderBean.getSupplyId());
			orderDto.setBillType(orderBean.getBillType());
			orderDto.setPayTypeId(OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId()); //App端是用的银联无卡支付方式
			orderDto.setLeaveMessage(orderBean.getLeaveMsg());
			orderDto.setProductInfoDtoList(productInfoDtoList);
			orderDto.setSource(orderCreateBean.getSource());//二期订单来源

			/* 销售顾问信息 */
			orderDto.setAdviserCode(orderBean.getAdviser_code());
			orderDto.setAdviserName(orderBean.getAdviser_name());
			orderDto.setAdviserPhoneNumber(orderBean.getPhone_number());
			orderDto.setAdviserRemark(orderBean.getRemark());

			orderDtoList.add(orderDto);
		}
		orderCreateDto.setOrderDtoList(orderDtoList);
		return orderCreateDto;
	}

}
