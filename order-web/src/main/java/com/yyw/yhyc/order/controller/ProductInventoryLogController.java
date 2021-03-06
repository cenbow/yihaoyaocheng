/**
 *
 * Created By: XI
 * Created On: 2016-8-29 11:24:18
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yyw.yhyc.product.bo.ProductInventoryLog;
import com.yyw.yhyc.product.service.ProductInventoryLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.bo.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
@RequestMapping(value = "/order/productInventoryLog")
public class ProductInventoryLogController extends BaseJsonController{
	private static final Logger logger = LoggerFactory.getLogger(ProductInventoryLogController.class);

	@Autowired
	private ProductInventoryLogService productInventoryLogService;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public ProductInventoryLog getByPK(Integer key) throws Exception
	{
		return productInventoryLogService.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<ProductInventoryLog> listPgProductInventoryLog(@RequestBody RequestModel<ProductInventoryLog> requestModel) throws Exception
	{
		Pagination<ProductInventoryLog> pagination = new Pagination<ProductInventoryLog>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return productInventoryLogService.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@RequestBody ProductInventoryLog productInventoryLog) throws Exception
	{
		productInventoryLogService.save(productInventoryLog);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(@RequestBody RequestListModel<Integer> requestListModel) throws Exception
	{
		productInventoryLogService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(@RequestBody ProductInventoryLog productInventoryLog) throws Exception
	{
		productInventoryLogService.update(productInventoryLog);
	}
}
