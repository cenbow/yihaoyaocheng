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
import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yao.trade.interfaces.credit.model.RefundCallBack;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.SpringBeanHelper;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.service.OrderPayService;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.order.service.SystemDateService;
import com.yyw.yhyc.order.service.SystemPayTypeService;
import com.yyw.yhyc.order.utils.RandomUtil;
import com.yyw.yhyc.pay.interfaces.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping(value = "/orderPay")
public class OrderPayController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderPayController.class);

	@Autowired
	private OrderPayService orderPayService;

	@Autowired
	private SystemDateService systemDateService;

	private SystemPayTypeService systemPayTypeService;
	@Autowired
	public void setSystemPayTypeService(SystemPayTypeService systemPayTypeService) {
		this.systemPayTypeService = systemPayTypeService;
	}

	@Reference(timeout = 50000,version ="0.0.2")
	private CreditDubboServiceInterface creditDubboService;

	private OrderService orderService;
	@Autowired
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}




	/**
	 * 从订单中心跳到 确认支付页面
	 * @param orderId 订单id
	 * @return
     */
	@RequestMapping(value = "/confirmPay", method = RequestMethod.GET)
	public ModelAndView confirmPay(@RequestParam("orderId") Integer orderId) throws Exception {

		UserDto userDto = super.getLoginUser();
		if(userDto == null || UtilHelper.isEmpty(userDto.getCustId())){
			throw new Exception("登陆超时");
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("orderPay/confirmPay");

		if(UtilHelper.isEmpty(orderId)) return modelAndView;
		Order order = orderService.getByPK(orderId);
		if(UtilHelper.isEmpty(order)){
			throw new Exception("非法参数!");
		}
		if(userDto.getCustId() != order.getCustId()){
			throw new Exception("非法参数!");
		}
		if(!SystemOrderStatusEnum.BuyerOrdered.getType().equals(order.getOrderStatus())){
			throw new Exception("订单状态异常!");
		}
		modelAndView.addObject("order",order);
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
		SystemPayType systemPayType = systemPayTypeService.getByPK(payTypeId);
		if(UtilHelper.isEmpty(systemPayType)){
			throw new Exception("登陆超时");
		}

		if(!SystemPayTypeEnum.PayOnline.getPayType().equals(systemPayType.getPayType()))throw new Exception("非法参数");
		if(UtilHelper.isEmpty(flowIds)) throw new Exception("非法参数");

		ModelAndView modelAndView = new ModelAndView();
		if(OnlinePayTypeEnum.MerchantBank.getPayTypeId() == payTypeId){
			modelAndView.setViewName("orderPay/cmb_pay");
		}else if(OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId() == payTypeId || OnlinePayTypeEnum.UnionPayB2C.getPayTypeId()== payTypeId){
			modelAndView.setViewName("orderPay/china_pay");
		}else{
			throw new Exception("非法参数");
		}

		/* 在线支付订单前，预处理订单数据 */
		String payFlowId = RandomUtil.createOrderPayFlowId(systemDateService.getSystemDateByformatter("%Y%m%d%H%i%s"),userDto.getCustId());
		OrderPay orderPay = orderPayService.preHandler(userDto, flowIds, payTypeId, payFlowId);
		if(UtilHelper.isEmpty(orderPay)) throw new Exception("非法参数");

		/* 在线支付订单前，组装订单数据 */
		PayService payService = (PayService) SpringBeanHelper.getBean(systemPayType.getPayCode());
		Map<String,Object>  payRequestParamMap = payService.handleDataBeforeSendPayRequest(orderPay,systemPayType,2);
		modelAndView.addObject("payRequestParamMap",payRequestParamMap);
		return modelAndView;
	}

	/**
	 * 从创建订单页跳转后，组装好数据后，表单提交数据到(已选的)银行
	 * @param listStr 订单编号集合
	 * @param payTypeId 支付方式id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/accountPay", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView RepaymentOfAccountPay(@RequestParam("listStr") String listStr,@RequestParam("payTypeId") int payTypeId,@RequestParam("supplyId") String supplyId) throws Exception {
		UserDto userDto = super.getLoginUser();
		if(userDto == null ){
			userDto=new UserDto();
			userDto.setCustId(6066);
		}
		SystemPayType systemPayType = systemPayTypeService.getByPK(payTypeId);
		if(UtilHelper.isEmpty(systemPayType)){
			throw new Exception("登陆超时");
		}

		ModelAndView modelAndView = new ModelAndView();
		if(OnlinePayTypeEnum.MerchantBank.getPayTypeId() == payTypeId){
			modelAndView.setViewName("orderPay/cmb_pay");
		}else if(OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId() == payTypeId || OnlinePayTypeEnum.UnionPayB2C.getPayTypeId()== payTypeId){
			modelAndView.setViewName("orderPay/china_pay");
		}else{
			throw new Exception("非法参数");
		}

		/* 在线支付订单前，预处理订单数据 */
		String payFlowId = RandomUtil.createOrderPayFlowId(systemDateService.getSystemDateByformatter("%Y%m%d%H%i%s"),userDto.getCustId());
		OrderPay orderPay = orderPayService.repaymentOfAccount(userDto, listStr, payTypeId, payFlowId, supplyId);
		if(UtilHelper.isEmpty(orderPay)) throw new Exception("非法参数");

		/* 在线支付订单前，组装订单数据 */
		PayService payService = (PayService) SpringBeanHelper.getBean(systemPayType.getPayCode());
		Map<String,Object>  payRequestParamMap = payService.handleDataBeforeSendPayRequest(orderPay,systemPayType,1);
		modelAndView.addObject("payRequestParamMap",payRequestParamMap);
		return modelAndView;
	}


	/**
	 * 招行支付成功回调
	 * @return
     */
	@RequestMapping(value = "/cmbchinaPaySuccess", method = RequestMethod.POST)
	public String cmbchinaPaySuccess(){
		PayService payService = (PayService) SpringBeanHelper.getBean("cmbPayService");
		return payService.paymentCallback(super.request);
	}

	/**
	 * 招行分账成功回调
	 * @return
	 */
	@RequestMapping(value = "/cmbchinaSplitSuccess", method = RequestMethod.POST)
	public String cmbchinaSplitSuccess(){
		PayService payService = (PayService) SpringBeanHelper.getBean("cmbPayService");
		return payService.spiltPaymentCallback(super.request);
	}


	/**
	 * 银联支付成功回调
	 * @return
	 */
	@RequestMapping(value = "/chinaPayCallback", method = RequestMethod.POST)
	public String chinaPayCallback(){
		PayService payService = (PayService) SpringBeanHelper.getBean("chinaPayService");
		payService.paymentCallback(super.request);
		return "success";
	}

	/**
	 * 账期还款银联支付成功回调
	 * @return
	 */
	@RequestMapping(value = "/chinaPayOfAccountCallback", method = RequestMethod.POST)
	public String chinaPayOfAccountCallback(){
		PayService payService = (PayService) SpringBeanHelper.getBean("chinaPayService");
		Map<String,String> map= payService.paymentOfAccountCallback(super.request);
		RefundCallBack refundCallBack=new RefundCallBack();
		refundCallBack.setIsSuccess(map.get("isSuccess"));
		refundCallBack.setMessage(map.get("message"));
		refundCallBack.setOrderCodes(map.get("orderCodes"));
		if(UtilHelper.isEmpty(creditDubboService)){
			logger.error("CreditDubboServiceInterface creditDubboService is null");
		}else {
			creditDubboService.updateBankInfoCallback(refundCallBack);
		}
		return "success";
	}

	/**
	 * 银联确认收货回调
	 * @return
	 */
	@RequestMapping(value = "/chinaPaySpiltPaymentCallback", method = RequestMethod.POST)
	public String chinaPaySpiltPaymentCallback(){
		//PayService payService = (PayService) SpringBeanHelper.getBean("chinaPayService");
		//payService.spiltPaymentCallback(super.request);
		return "success";
	}

	/**
	 * 银联退款回调
	 * @return
	 */
	@RequestMapping(value = "/chinaPayRedundCallBack", method = RequestMethod.POST)
	public String chinaPayRedundCallBack(){
		//PayService payService = (PayService) SpringBeanHelper.getBean("chinaPayService");
		//payService.redundCallBack(super.request);
		return "success";
	}
}
