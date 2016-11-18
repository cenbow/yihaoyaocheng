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
import org.search.remote.yhyc.ProductSearchInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		List<ShoppingCartListDto> allShoppingCart = shoppingCartService.index(userDto,iProductDubboManageService,iPromotionDubboManageService);
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
	public Map<String, Object> addShoppingCart(@RequestBody ShoppingCart shoppingCart) throws Exception {

		/* 获取登陆用户的企业信息 */
		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId())){
			throw  new Exception("登陆超时");
		}
		return shoppingCartService.addShoppingCart(shoppingCart,userDto,iPromotionDubboManageService,iProductDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
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
					productInfoDto.setSpuCode(temp.getSpuCode());
					productInfoDto.setProductPrice(temp.getProductPrice());
					productInfoDto.setProductCount(temp.getProductCount());
					productInfoDto.setPromotionId(temp.getPromotionId());
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
}
