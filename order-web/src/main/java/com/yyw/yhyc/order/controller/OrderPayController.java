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
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.service.OrderPayService;
import com.yyw.yhyc.order.service.SystemDateService;
import com.yyw.yhyc.order.utils.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping(value = "/orderPay")
public class OrderPayController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderPayController.class);

	@Autowired
	private OrderPayService orderPayService;

	@Autowired
	private SystemDateService systemDateService;

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
	 * 从订单中心跳到 确认支付页面
	 * @param flowIds
	 * @return
     */
	@RequestMapping(value = "/confirmPay", method = RequestMethod.GET)
	public ModelAndView confirmPay(@RequestParam("flowIds") String flowIds) throws Exception {

		UserDto userDto = super.getLoginUser();
		if(userDto == null ){
			throw new Exception("登陆超时");
		}

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("orderPay/confirmPay");

		if(UtilHelper.isEmpty(flowIds)) return modelAndView;

		modelAndView.addObject("flowIds",flowIds);

		return modelAndView;
	}


	/**
	 * 从创建订单页跳转后，组装好数据后，表单提交数据到(已选的)银行
	 * @param flowIds 订单编号集合
	 * @param payTypeId 支付方式id
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	public ModelAndView pay(@RequestParam("flowIds") String flowIds,@RequestParam("payTypeId") int payTypeId) throws Exception {
		UserDto userDto = super.getLoginUser();
		if(userDto == null ){
			throw new Exception("登陆超时");
		}

		if(OnlinePayTypeEnum.getPayName(payTypeId) == null )throw new Exception("非法参数");
		if(UtilHelper.isEmpty(flowIds)) throw new Exception("非法参数");

		/* 创建支付流水号 */
		String payFlowId = RandomUtil.createOrderPayFlowId(systemDateService.getSystemDateByformatter("%Y%m%d%H%i%s"),userDto.getCustId());
		/* 在线支付订单前，预处理订单数据 */
		OrderPay orderPay = orderPayService.preHandler(userDto ,flowIds,payTypeId,payFlowId);
		if(UtilHelper.isEmpty(orderPay)) throw new Exception("非法参数");

		Map<String,Object> payRequestParamMap = null;
		ModelAndView modelAndView = new ModelAndView();
		if(OnlinePayTypeEnum.MerchantBank.getPayType() == payTypeId){
			//TODO
			modelAndView.setViewName("orderPay/cmb_pay");
		}else if(OnlinePayTypeEnum.UnionPayNoCard.getPayType() == payTypeId || OnlinePayTypeEnum.UnionPayB2C.getPayType()== payTypeId){
			//TODO
			modelAndView.setViewName("orderPay/china_pay");
		}else{
			throw new Exception("非法参数");
		}
		modelAndView.addObject("flowIds",flowIds);
		modelAndView.addObject("payRequestParamMap",payRequestParamMap);
		return modelAndView;
	}
}
