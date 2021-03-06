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

import com.yyw.yhyc.helper.SpringBeanHelper;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.AccountPayInfo;
import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.service.AccountPayInfoService;
import com.yyw.yhyc.order.service.OrderPayService;
import com.yyw.yhyc.order.service.SystemDateService;
import com.yyw.yhyc.order.service.SystemPayTypeService;
import com.yyw.yhyc.order.utils.RandomUtil;
import com.yyw.yhyc.pay.interfaces.PayService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = {"/api/orderPay","/order/api/orderPay"})
public class OrderPayController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(OrderPayController.class);

	@Autowired
	private OrderPayService orderPayService;

	@Autowired
	private SystemPayTypeService systemPayTypeService;

	@Autowired
	private SystemDateService systemDateService;
	@Autowired
	private AccountPayInfoService accountPayInfoService;

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
     * 
     * @param orderIds
     * @param payTypeId
     * @return
     * @throws Exception
     */
	@RequestMapping(value = "/enterprise/bankInfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> bankInfo(@RequestParam("enterpriseId")Integer enterpriseId) throws Exception {
		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId())){
			return error(STATUS_CODE_INVALID_TOKEN,"登陆超时");
		}

		logger.info("app端获取商家银行信息 enterpriseId=="+enterpriseId);
		
		AccountPayInfo payInfo=this.accountPayInfoService.getByCustId(enterpriseId);
		if(UtilHelper.isEmpty(payInfo)){
			return error(STATUS_CODE_SYSTEM_EXCEPTION,"enterpriseId的账户信息为空");
		}
		if(UtilHelper.isEmpty(payInfo.getSubbankName())){
			return error(STATUS_CODE_SYSTEM_EXCEPTION,"收款开户支行为空");
		}
		if(UtilHelper.isEmpty(payInfo.getReceiveAccountName())){
			return error(STATUS_CODE_SYSTEM_EXCEPTION,"收款款账号名称为空");
		}
		if(UtilHelper.isEmpty(payInfo.getReceiveAccountNo())){
			return error(STATUS_CODE_SYSTEM_EXCEPTION,"收款账号为空");
		}

		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("bankName", payInfo.getSubbankName());
		resultMap.put("accountName", payInfo.getReceiveAccountName());
		resultMap.put("account", payInfo.getReceiveAccountNo());
		return ok(resultMap);
	}

	/**
	 * App端支付接口
	 * @param orderIds 订单编号集合,以英文的逗号（“,”）隔开
	 * @param payTypeId 支付方式id （4 : 表示使用银联无卡支付 , 9 : 表示使用银联手机支付）
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pay", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> pay(@RequestParam("orderIds") String orderIds, @RequestParam("payTypeId") int payTypeId) throws Exception {
		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId())){
			return error(STATUS_CODE_INVALID_TOKEN,"登陆超时");
		}

		logger.info("App端支付接口：请求参数orderIds = " + orderIds  + ",payTypeId = " + payTypeId);

		/* 新增银联手机支付 ，代替原有的银联无卡支付 。但为了App不再改参数，银联无卡支付 归于 银联手机支付 */
		if( OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId() == payTypeId ){
			payTypeId = OnlinePayTypeEnum.UnionPayMobile.getPayTypeId();
		}

		SystemPayType systemPayType = systemPayTypeService.getByPK(payTypeId);
		if(UtilHelper.isEmpty(systemPayType)){
			return error("非法参数");
		}
		if(!SystemPayTypeEnum.PayOnline.getPayType().equals(systemPayType.getPayType())){
			return error("非法参数");
		}
		if(UtilHelper.isEmpty(orderIds)){
			return error("非法参数");
		}

		/* 在线支付订单前，预处理订单数据 */
		String payFlowId = RandomUtil.createOrderPayFlowId(orderIds);
		OrderPay orderPay = orderPayService.preHandler(userDto, orderIds, payTypeId, payFlowId);
		if(UtilHelper.isEmpty(orderPay)){
			throw new Exception("非法参数");
		}

		/* 在线支付订单前，组装订单数据 */
		PayService payService = (PayService) SpringBeanHelper.getBean(systemPayType.getPayCode());
		Map<String,Object> payRequestParamMap = payService.handleDataBeforeSendPayRequest(orderPay,systemPayType,3);

		Map<String,Object> resultMap = new HashMap<>();

		if( null == payRequestParamMap || payRequestParamMap.size() == 0) return error("非法参数");
		if(!payRequestParamMap.containsKey("MerSplitMsg")) payRequestParamMap.put("MerSplitMsg", "0");
		for(Map.Entry<String, Object> entry:payRequestParamMap.entrySet()){
			String params = "TranReserved;MerId;MerOrderNo;OrderAmt;CurryNo;TranDate;SplitMethod;BusiType;MerPageUrl;MerBgUrl;SplitType;MerSplitMsg;PayTimeOut;MerResv;Version;BankInstNo;CommodityMsg;Signature;AccessType;AcqCode;OrderExpiryTime;TranType;RemoteAddr;Referred;TranTime;TimeStamp;CardTranData";
			if(params.contains(entry.getKey())){
				resultMap.put(entry.getKey(),entry.getValue());
			}
		}
		logger.info("App端支付接口：请求参数orderIds = " + orderIds  + ",payTypeId = " + payTypeId + "\n 响应参数:resultMap=" + resultMap);
		return ok(resultMap);
	}
}
