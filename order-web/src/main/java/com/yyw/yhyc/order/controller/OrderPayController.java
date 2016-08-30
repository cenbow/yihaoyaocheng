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

import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.service.OrderPayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/order/orderPay")
public class OrderPayController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderPayController.class);

	@Autowired
	private OrderPayService orderPayService;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public OrderPay getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return orderPayService.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<OrderPay> listPgOrderPay(RequestModel<OrderPay> requestModel) throws Exception
	{
		Pagination<OrderPay> pagination = new Pagination<OrderPay>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return orderPayService.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(OrderPay orderPay) throws Exception
	{
		orderPayService.save(orderPay);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderPayService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(OrderPay orderPay) throws Exception
	{
		orderPayService.update(orderPay);
	}


	/**
	 * 招行支付成功回调
	 * @return
     */
	@RequestMapping(value = "/cmbchinaPaySuccess", method = RequestMethod.POST)
	public String cmbchinaPaySuccess(){
		String tmplate =  "<?xml version=\"1.0\" encoding=\"ISO8859-1\"?><DATA><RESPONSE><STSCOD>%s</STSCOD><STSMSG>%s</STSMSG></RESPONSE></DATA>";

		return String.format(tmplate,"1000","失败");
	}

	/**
	 * 招行分账成功回调
	 * @return
	 */
	@RequestMapping(value = "/cmbchinaSplitSuccess", method = RequestMethod.POST)
	public String cmbchinaSplitSuccess(){
		return null;
	}
}
