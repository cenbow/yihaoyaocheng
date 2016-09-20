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

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/order/shoppingCart")
public class ShoppingCartController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

	@Autowired
	private ShoppingCartService shoppingCartService;

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
	 * 获得进货单品种数量
	 * @return
     */
	@RequestMapping(value = "/cartAccount", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> cartAccount(){
		Integer custId = 6066;
		Map<String,Object> result = new HashMap<>();

		if(UtilHelper.isEmpty(custId)) {
			result.put("statusCode", -1);
			result.put("message", "参数custId不能为空！");
			result.put("data", null);

			return result;
		}

		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(custId);
		try {
			int count = shoppingCartService.findByCount(shoppingCart);

			result.put("statusCode", 0);
			result.put("message", "成功");

			Map<String, Integer> data = new HashMap<>();
			data.put("count", count);
			result.put("data", data);

			return result;
		}catch (Exception e){
			logger.error(e.getMessage(), e);

			result.put("statusCode", -3);
			result.put("message", e.getMessage());
			result.put("data", null);

			return result;
		}
	}
}
