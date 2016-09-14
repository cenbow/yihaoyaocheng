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
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.order.service.ShoppingCartService;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.product.service.ProductInventoryService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
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
	private IProductDubboManageService productDubboManageService;

	@Reference
	private ICustgroupmanageDubbo iCustgroupmanageDubbo;

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

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView model = new ModelAndView();
		UserDto userDto = super.getLoginUser();

		/* 获取购物车中的商品信息 */
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(userDto.getCustId());
		List<ShoppingCartListDto> allShoppingCart = shoppingCartService.listAllShoppingCart(shoppingCart);

		if(UtilHelper.isEmpty(allShoppingCart)){
			model.addObject("allShoppingCart",allShoppingCart);
			model.setViewName("shoppingCart/index");
			return model;
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

				productPriceCount = productPriceCount.add(shoppingCartDto.getProductSettlementPrice());

				if(UtilHelper.isEmpty(iProductDubboManageService)){
					logger.error("购物车页面-查询商品信息失败,iProductDubboManageService = " + iProductDubboManageService);
				}

				/* 最小起批量、最小拆零包装   */
				Map map = new HashMap();
				map.put("spu_code", shoppingCartDto.getSpuCode());
				map.put("seller_code", shoppingCartDto.getSupplyId());

				int minimumPacking = 1;
				String unit = "";
				int saleStart = 1;
				List productList = null;
				Integer putaway_status = null;
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
					saleStart = UtilHelper.isEmpty(productJson.get("wholesale_num")) ? 1 : (int) productJson.get("wholesale_num");
					unit = UtilHelper.isEmpty(productJson.get("unit")) ? "" : UtilHelper.isEmpty(productJson.get("unit")+"") ? "" : productJson.get("unit")+"";
					putaway_status = UtilHelper.isEmpty(productJson.get("putaway_status")+"") ? null : (int) productJson.get("putaway_status");
				}

				shoppingCartDto.setMinimumPacking(minimumPacking); //最小拆零包装数量
				shoppingCartDto.setUnit(unit);//最小拆零包装单位
				shoppingCartDto.setSaleStart(saleStart);//起售量
				shoppingCartDto.setUpStep(minimumPacking); //每次增加、减少的 递增数量
				shoppingCartDto.setPutawayStatus(putaway_status); //上下架状态

				/* 查询商品库存 */
				ProductInventory productInventory = new ProductInventory();
				productInventory.setSupplyId(shoppingCartDto.getSupplyId());
				productInventory.setSpuCode(shoppingCartDto.getSpuCode());
				productInventory.setFrontInventory(shoppingCartDto.getProductCount());
				logger.info("购物车页面-检查商品库存,请求参数productInventory=" + productInventory);
				Map<String, Object> resultMap = productInventoryService.findInventoryNumber(productInventory);
				logger.info("购物车页面-检查商品库存,响应参数resultMap=" + resultMap);

				String frontInventory = resultMap.get("frontInventory") + "";
				shoppingCartDto.setProductInventory(UtilHelper.isEmpty(frontInventory) ? 0 : Integer.valueOf(frontInventory));

				String code = resultMap.get("code").toString();
				if("0".equals(code) || "1".equals(code)){
					shoppingCartDto.setExistProductInventory(false);
				}else{
					shoppingCartDto.setExistProductInventory(true);
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
	public void updateNum(@RequestBody ShoppingCart shoppingCart) throws Exception {
		UserDto userDto = super.getLoginUser();
		int resultCount = shoppingCartService.updateNum(shoppingCart,userDto);
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
					productInfoDtoList.add(productInfoDto);
				}
			}
			if(productInfoDtoList.size() == 0) continue;
			orderDto.setProductInfoDtoList(productInfoDtoList);

			/* 商品信息校验 ： 检验商品上架、下架状态、价格、库存、订单起售量等一系列信息 */
			resultMap = orderService.validateProducts(userDto, orderDto,iCustgroupmanageDubbo,iProductDubboManageService);
			boolean result = (boolean) resultMap.get("result");
			if(!result){
				return resultMap;
			}
		}
		resultMap.put("result",true);
		return resultMap;
	}
}
