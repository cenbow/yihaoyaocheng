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
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.facade.ShoppingCartFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/order/shoppingCart")
public class ShoppingCartController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

	@Reference
	private ShoppingCartFacade shoppingCartFacade;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public ShoppingCart getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return shoppingCartFacade.getByPK(key);
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

		return shoppingCartFacade.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(ShoppingCart shoppingCart) throws Exception
	{
		shoppingCartFacade.save(shoppingCart);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		shoppingCartFacade.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(ShoppingCart shoppingCart) throws Exception
	{
		shoppingCartFacade.update(shoppingCart);
	}
}
