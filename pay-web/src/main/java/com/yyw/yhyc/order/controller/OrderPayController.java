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
import com.alibaba.fastjson.JSONObject;
import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yao.trade.interfaces.credit.model.RefundCallBack;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.SpringBeanHelper;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderCombined;
import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.OrderPayStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.service.*;
import com.yyw.yhyc.order.utils.RandomUtil;
import com.yyw.yhyc.pay.interfaces.PayService;
import com.yyw.yhyc.utils.CacheUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.yyw.yhyc.order.inteceptor.GetUserInteceptor.CACHE_PREFIX;

@Controller
@RequestMapping(value = {"/orderPay","/api/orderPay","/pay/api/orderPay"})
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

	@Reference(timeout = 50000)
	private CreditDubboServiceInterface creditDubboService;

	private OrderService orderService;
	@Autowired
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}



	private OrderCombinedService orderCombinedService;

	@Autowired
	public void setOrderCombinedService(OrderCombinedService orderCombinedService) {
		this.orderCombinedService = orderCombinedService;
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
		}else if(OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId() == payTypeId || OnlinePayTypeEnum.UnionPayB2C.getPayTypeId()== payTypeId || OnlinePayTypeEnum.UnionPayB2B.getPayTypeId() == payTypeId){
			modelAndView.setViewName("orderPay/china_pay");
		}else if(OnlinePayTypeEnum.AlipayWeb.getPayTypeId() == payTypeId){
			modelAndView.setViewName("orderPay/alipay_web");
		}else{
			throw new Exception("非法参数");
		}

		/* 在线支付订单前，预处理订单数据 */
		String payFlowId = RandomUtil.createOrderPayFlowId(flowIds);
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
			throw new Exception("登陆超时");
		}
		logger.info("账期还款订单："+listStr+"payTypeId:"+payTypeId+"supplyId:"+supplyId);
		SystemPayType systemPayType = systemPayTypeService.getByPK(payTypeId);
		if(UtilHelper.isEmpty(systemPayType)){
			throw new Exception("登陆超时");
		}

		ModelAndView modelAndView = new ModelAndView();
		if(OnlinePayTypeEnum.MerchantBank.getPayTypeId() == payTypeId){
			modelAndView.setViewName("orderPay/cmb_pay");
		}else if(OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId() == payTypeId || OnlinePayTypeEnum.UnionPayB2C.getPayTypeId()== payTypeId || OnlinePayTypeEnum.UnionPayB2B.getPayTypeId() == payTypeId){
			modelAndView.setViewName("orderPay/china_pay");
		}else{
			throw new Exception("非法参数");
		}

		/* 在线支付订单前，预处理订单数据 */
		String payFlowId = RandomUtil.createOrderPayFlowId(listStr);
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

	/**
	 * App银联支付（同步响应的页面）
	 * @return
     */
	@RequestMapping(value = "/chinaPayAppSubmitSuccess", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView chinaPayAppSubmitSuccess(){
		return new ModelAndView("orderPay/chinaPayAppSubmitSuccess");
	}




	/**
	 * 招行E+支付： 支付前,校验收款人的银行卡信息
	 * 按照招行E+的接口要求：如果用户使用了招行E+支付订单，则订单供应商的银行卡信息必须是完整、无误的.
	 * @return
	 */
	@RequestMapping(value = "/validateBankCard", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,String> validateBankCard(@RequestParam("flowIds") String flowIds,@RequestParam("payTypeId") int payTypeId) throws Exception {
		logger.info("[在线支付]---支付前校验收款人的银行卡信息，请求参数flowIds = " + flowIds + "payTypeId = "+ payTypeId );

		/* 数据校验 */
		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto)){
			userDto = getUserDto(request); //跨域请求(带了sessionId参数)
		}
		if(userDto == null ) throw new Exception("登陆超时");
		SystemPayType systemPayType = systemPayTypeService.getByPK(payTypeId);
		if( UtilHelper.isEmpty(systemPayType)) throw new Exception("非法参数");
		if( ! SystemPayTypeEnum.PayOnline.getPayType().equals(systemPayType.getPayType())) throw new Exception("非法参数");
		if( UtilHelper.isEmpty(flowIds)) throw new Exception("非法参数");

		String [] flowIdArray = flowIds.split(",");
		if( UtilHelper.isEmpty(flowIdArray) || flowIdArray.length == 0) throw new Exception("非法参数");

		Map<String,String> resultMap = new HashMap<>();

		for( String flowId : flowIdArray ) {
			if( UtilHelper.isEmpty(flowId)) continue;
			Order order = orderService.getOrderbyFlowId(flowId);
			if( UtilHelper.isEmpty(order)) continue;
			if( ! SystemOrderStatusEnum.BuyerOrdered.getType().equals(order.getOrderStatus())) continue;

			/* 招行E+支付 */
//			if(OnlinePayTypeEnum.CmbEPlus.getPayTypeId() == payTypeId){
//				return validateBankCardForCmbEPlusPay(order);
//			}
		}
		logger.info("[在线支付]---支付前校验收款人的银行卡信息，返回数据resultMap = " + resultMap );
		if(UtilHelper.isEmpty(resultMap) || !resultMap.containsKey("statusCode")){
			resultMap.put("statusCode","200");
			resultMap.put("message","供应商银行卡信息正常");
		}
		return resultMap;
	}

	/**
	 * 招行E+支付,支付前校验收款人的银行卡信息
	 * @param order
	 * @return
	 * @throws Exception
     */
//	private Map<String,String> validateBankCardForCmbEPlusPay(Order order) throws Exception {
//		Map<String,String> resultMap = new HashMap<>();
//		AccountPayInfo accountPayInfo = accountPayInfoService.getByCustId(order.getSupplyId());
//		logger.info("[招行E+支付]---支付前校验收款人的银行卡信息，accountPayInfo = " + accountPayInfo );
//		if(UtilHelper.isEmpty(accountPayInfo)){
//			resultMap.put("statusCode","100");
//			resultMap.put("message","订单编号为" + order.getFlowId() + "的订单收款账号配置有误，请联系平台客服！");
//			return resultMap;
//		}
//		if( UtilHelper.isEmpty(accountPayInfo.getReceiveAccountName()) ||  UtilHelper.isEmpty(accountPayInfo.getReceiveAccountNo())
//				|| UtilHelper.isEmpty(accountPayInfo.getSubbankName()) || UtilHelper.isEmpty(accountPayInfo.getProvinceName())
//				|| UtilHelper.isEmpty(accountPayInfo.getCityName())){
//			logger.error("[招行E+支付]---支付前校验收款人的银行卡信息 信息部分缺失!");
//			resultMap.put("statusCode","100");
//			resultMap.put("message","订单编号为" + order.getFlowId() + "的订单收款账号配置有误，请联系平台客服！");
//			return resultMap;
//		}
//
//		/* 根据用户的开户行全称  查询[招行E+]是否有对应的银行编码 */
//		int bankCode  = CmbEplusBankCode.getBankCodeByNameLike(accountPayInfo.getSubbankName());
//		logger.info("[招行E+支付]---发送请求前，根据供应商的开户行全称（"+accountPayInfo.getSubbankName()+"）查询[招行E+]是否有对应的银行编码(bankCode=" + bankCode + ",bankName=" + CmbEplusBankCode.getBankNameByCode(bankCode) + ")");
//		if( bankCode <= 0){
//			logger.error("[招行E+支付]---发送请求前，根据供应商的开户行全称（"+accountPayInfo.getSubbankName()+"）在[招行E+]没有对应的银行编码(bankCode=" + bankCode + ",bankName=" + CmbEplusBankCode.getBankNameByCode(bankCode) + ")");
//			resultMap.put("statusCode","100");
//			resultMap.put("message","订单编号为" + order.getFlowId() + "的订单收款账号配置有误，请联系平台客服！");
//			return resultMap;
//		}
//
//		resultMap.put("statusCode","200");
//		resultMap.put("message","供应商银行卡信息正常");
//		return resultMap;
//
//	}


	/**
	 * 查询订单支付状态
	 * @param flowIds
	 * @return
	 * @throws Exception
     */
	@RequestMapping(value = "/checkOrderPayedStatus", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,String> checkOrderPayedStatus(@RequestParam("flowIds") String flowIds) throws Exception {
		logger.info("[在线支付]---查询订单支付状态，请求参数flowIds = " + flowIds );

		/* 数据校验 */
		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto)){
			userDto = getUserDto(request); //跨域请求(带了sessionId参数)
		}
		if(userDto == null ) throw new Exception("登陆超时");

		if( UtilHelper.isEmpty(flowIds)) throw new Exception("非法参数");
		String [] flowIdArray = flowIds.split(",");
		if( UtilHelper.isEmpty(flowIdArray) || flowIdArray.length == 0) throw new Exception("非法参数");

		Map<String,String> resultMap = new HashMap<>();

		int payedCount = 0;
		for( String flowId : flowIdArray ) {
			if( UtilHelper.isEmpty(flowId)) continue;
			Order order = orderService.getOrderbyFlowId(flowId);
			if( UtilHelper.isEmpty(order) || UtilHelper.isEmpty(order.getOrderCombinedId())) continue;
			OrderCombined orderCombined = orderCombinedService.getByPK(order.getOrderCombinedId());
			if(UtilHelper.isEmpty(orderCombined) || UtilHelper.isEmpty(orderCombined.getPayFlowId())) continue;
			OrderPay orderPay = orderPayService.getByPayFlowId(orderCombined.getPayFlowId());
			if( !UtilHelper.isEmpty(orderPay) && OrderPayStatusEnum.PAYED.getPayStatus().equals(orderPay.getPayStatus())){
				//已付款
				payedCount += 1;
			}
		}

	 	if(payedCount > 0) {
			resultMap.put("statusCode","200");
			resultMap.put("message","已付款");
			resultMap.put("payedStatus","true");
		}else {
			resultMap.put("statusCode","200");
			resultMap.put("message","未付款");
			resultMap.put("payedStatus","false");
		}
		logger.info("[在线支付]---查询订单支付状态，返回数据resultMap = " + resultMap );
		return resultMap;
	}


	/**
	 * 获取当前登陆的用户信息
	 * @param request
	 * @return
	 */
	private UserDto getUserDto(HttpServletRequest request){
		logger.info("request.getHeader(\"mySessionId\")=" + request.getHeader("mySessionId"));
		String sessionId = request.getHeader("mySessionId");
		if(UtilHelper.isEmpty(sessionId)){
			return null;
		}
		String user = CacheUtil.getSingleton().get(CACHE_PREFIX + sessionId);
		logger.info("user-->" + user);
		//用户信息
		if(!UtilHelper.isEmpty(user)) {
			Map userMap = JSONObject.parseObject(user, HashMap.class);
			if(UtilHelper.isEmpty(userMap)){
				return null;
			}
			UserDto userDto = new UserDto();
			userDto.setUser(userMap);
			userDto.setUserName(userMap.get("username")+"");
			userDto.setCustId(Integer.valueOf(userMap.get("enterprise_id")+""));
			return userDto;
		}
		return null;
	}
}
