/**
 *
 * Created By: XI
 * Created On: 2016-7-28 17:34:55
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.controller;

import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.order.bo.AccountPayInfo;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;

import com.yyw.yhyc.order.service.AccountPayInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping(value = "/order/accountPayInfo")
public class AccountPayInfoController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(AccountPayInfoController.class);

	@Autowired
	private AccountPayInfoService accountPayInfoService;
	private Integer custId;
	
	public Integer getCustId() {
		return custId;
	}
	public void setCustId(Integer custId) {
		this.custId = custId;
	}
	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public AccountPayInfo getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return accountPayInfoService.getByPK(key);
	}
	/**
	* 通过供应商custId查询
	* @return
	*/
	@RequestMapping(value = "/getByCustId/{custId}", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView getByCustId(@PathVariable("custId")Integer custId) throws Exception
	{
		AccountPayInfo accountPayInfo =accountPayInfoService.getByCustId(custId);
		ModelAndView model = new ModelAndView();
		model.addObject("dataMap",accountPayInfo);
		model.setViewName("order/checkAccountInfo");
		return model;
	}
	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<AccountPayInfo> listPgAccountPayInfo(RequestModel<AccountPayInfo> requestModel) throws Exception
	{
		Pagination<AccountPayInfo> pagination = new Pagination<AccountPayInfo>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return accountPayInfoService.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(AccountPayInfo accountPayInfo) throws Exception
	{
		accountPayInfoService.save(accountPayInfo);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		accountPayInfoService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(AccountPayInfo accountPayInfo) throws Exception
	{
		accountPayInfoService.update(accountPayInfo);
	}
}
