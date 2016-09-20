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
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/order/shoppingCart")
public class ShoppingCartController extends BaseJsonController {
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
	@RequestMapping(value = "/deleteShopCart", method = RequestMethod.PUT)
	public Map<String,Object> deleteShopCarts(List<Integer> shoppingCartIds) throws Exception
	{
		Integer custId = 6066;
		return shoppingCartService.deleteShopCarts(custId,shoppingCartIds);
	}

	/**
	 * 更新进货单数量
	 * @return
	 */
	@RequestMapping(value = "/updateShopCart", method = RequestMethod.PUT)
	public Map<String,Object> updateShopCart(Integer shoppingCartId,Integer quantity) throws Exception
	{
		Integer custId = 6066;
		return shoppingCartService.updateShopCart(custId,shoppingCartId,quantity);
	}


	/**
	 * 添加商品到进货单
	 * 请求数据格式如下：

	 {
		 "custId": 6066,
		 "supplyId": "6067",
		 "spuCode": "010BAA3040006",
		 "productId": "7",
		 "productCount": 1,
		 "productPrice": 12,
		 "productCodeCompany": "3545451",
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
			map.put("statusCode", "-1");
			map.put("message", "非法参数");
			return map;
		}
		Map<String, Object> result = shoppingCartService.addShoppingCart(shoppingCart);
		result.put("totalCount",result.get("productCount"));
		if(!UtilHelper.isEmpty(result) && "S".equals(result.get("state")+"")){
			map.put("statusCode", "0");
			map.put("data",result);
		}else{
			map.put("data","");
			map.put("message", "进货单中没有商品");
			map.put("statusCode", "0");
		}
		return map;
	}

	/**
	 * 进货单列表
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/getShopCartList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getShopCartList() throws Exception {
		UserDto userDto = getLoginUser();
		return shoppingCartService.getShopCartList(userDto,iProductDubboManageService);
	}

}
