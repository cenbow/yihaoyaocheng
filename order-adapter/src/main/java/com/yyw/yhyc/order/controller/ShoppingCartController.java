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
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/cart")
public class ShoppingCartController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Reference
	private IProductDubboManageService iProductDubboManageService;

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
	public Map<String,Object> deleteShopCarts(@RequestBody Map<String,List<Integer>> shoppingCartIdList) throws Exception
	{
		HttpServletRequest req = this.request;
		Integer custId = 6066;
		return shoppingCartService.deleteShopCarts(custId,shoppingCartIdList.get("shoppingCartIdList"),iProductDubboManageService);
//		return new HashMap<String,Object>();
	}

	/**
	 * 更新进货单数量
	 * @return
	 */
	@RequestMapping(value = "/updateShopCart", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> updateShopCart(@RequestBody Map<String,Integer> shoppingCart) throws Exception
	{
		HttpServletRequest req = this.request;
		Integer custId = 6066;
		Integer shoppingCartId = shoppingCart.get("shoppingCartId");
		Integer quantity = shoppingCart.get("quantity");
		return shoppingCartService.updateShopCart(custId,shoppingCartId,quantity,iProductDubboManageService);
//		return new HashMap<String,Object>();
	}

	/**
	 * 获得进货单品种数量
	 * @return
     */
	@RequestMapping(value = "/v{version}/cartAccount", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> cartAccount(){
		Integer custId = 6066;
		Map<String,Object> result = new HashMap<>();

		if(UtilHelper.isEmpty(custId)) {
			return error(STATUS_CODE_REQUEST_PARAM_ERROR,"参数custId不能为空！");
		}

		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(custId);
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
		Map<String, Object> map = new HashMap<>();
		if(UtilHelper.isEmpty(shoppingCart)){
			return error(STATUS_CODE_REQUEST_PARAM_ERROR,"非法参数");
		}
		Map<String, Object> result = null;
		try{
			result = shoppingCartService.addShoppingCart(shoppingCart);
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
		UserDto userDto = new UserDto();
		userDto.setCustId(6066);
		return shoppingCartService.getShopCartList(userDto,iProductDubboManageService);
	}

	/**
	 * 获取收获的采购商的地址列表
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/getDeliveryAddress", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getDeliveryAddress() throws Exception {
		UserDto userDto = new UserDto();
		userDto.setCustId(6066);
		Object data = shoppingCartService.getDeliveryAddress(userDto);
		return ok(data);
	}
}
