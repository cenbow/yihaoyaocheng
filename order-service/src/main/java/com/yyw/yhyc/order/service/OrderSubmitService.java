package com.yyw.yhyc.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.search.remote.yhyc.ProductSearchInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yao.trade.interfaces.credit.model.CreditDubboResult;
import com.yao.trade.interfaces.credit.model.CreditParams;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.framework.core.model.util.StringUtil;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OrderPayStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;
import com.yyw.yhyc.usermanage.service.UsermanageEnterpriseService;

@Service("orderSubmitService")
public class OrderSubmitService {
	private Log logger = LogFactory.getLog(OrderSubmitService.class);
	@Autowired
	private UsermanageEnterpriseService enterpriseService;
	@Autowired
	private OrderCreateService orderCreateService;
	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private SystemDateService systemDateService;
	
	public Map<String,Object> createOrderSubmit(HttpServletRequest request,OrderCreateDto orderCreateDto,UserDto userDto,ICustgroupmanageDubbo iCustgroupmanageDubbo,IProductDubboManageService productDubboManageService,ProductSearchInterface productSearchInterface,IPromotionDubboManageService iPromotionDubboManageService,CreditDubboServiceInterface creditDubboService) throws Exception{
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		for(OrderDto orderDto : orderCreateDto.getOrderDtoList()){
			/* 校验采购商状态、资质 */
			UsermanageEnterprise buyer = enterpriseService.getByEnterpriseId(userDto.getCustId()+"");
			if(UtilHelper.isEmpty(buyer)){
				logger.info("统一校验订单商品接口-buyer ：" + buyer);
				map.put("result", false);
				map.put("message", "采购商不存在");
				map.put("goToShoppingCart", true);
				return map;
			}
			
			//验证买家留言长度
			String leaveMessage=orderDto.getLeaveMessage();
			if(!UtilHelper.isEmpty(leaveMessage) && leaveMessage.length()>500){
				leaveMessage=leaveMessage.substring(0, 499);
				orderDto.setLeaveMessage(leaveMessage);
			}
			orderDto.setBuyer(buyer);
			orderDto.setCustId(Integer.valueOf(buyer.getEnterpriseId()));
			orderDto.setCustName(buyer.getEnterpriseName());

			/* 校验要供应商状态、资质 */
			UsermanageEnterprise seller = enterpriseService.getByEnterpriseId(orderDto.getSupplyId() + "");
			if(UtilHelper.isEmpty(seller)){
				logger.info("统一校验订单商品接口-seller ：" + seller);
				map.put("result", false);
				map.put("message", "供应商不存在");
				map.put("goToShoppingCart", true);
				return map;
			}
			orderDto.setSeller(seller);
			orderDto.setSupplyName(seller.getEnterpriseName());
			/* 商品信息校验 ： 检验商品上架、下架状态、价格、库存、订单起售量等一系列信息 */
			
			/*map = orderService.validateProducts(userDto,orderDto,iCustgroupmanageDubbo,productDubboManageService,
					productSearchInterface,iPromotionDubboManageService);*/
			map = this.orderCreateService.validateProducts(userDto,orderDto,iCustgroupmanageDubbo,productDubboManageService,
			productSearchInterface,iPromotionDubboManageService);
			boolean result = (boolean) map.get("result");
			if(!result){
				return map;
			}

			/**销售顾问信息**/
			if(StringUtil.isNotEmpty(orderDto.getAdviserName())){
				String [] adviserInfo = orderDto.getAdviserName().split(";");
				orderDto.setAdviserCode(adviserInfo[0]);
				orderDto.setAdviserName(adviserInfo[1]);
				orderDto.setAdviserPhoneNumber(adviserInfo[2]);
				if(adviserInfo.length > 3){
					orderDto.setAdviserRemark(adviserInfo[3]);
				}
			}
		}
		//订单来源 限用pc
		orderCreateDto.setSource(1);
		Map<String,Object> newOrderMap = orderService.createOrder(orderCreateDto,iPromotionDubboManageService);
		List<Order> orderList = (List<Order>) newOrderMap.get("orderNewList");

		String orderIdStr = "";
		if(!UtilHelper.isEmpty(orderList)){
			for(Order order : orderList){
				if(UtilHelper.isEmpty(order)) continue;
				if(UtilHelper.isEmpty(orderIdStr)){
					orderIdStr += order.getOrderId();
				}else{
					orderIdStr += ","+order.getOrderId();
				}
		       orderLogService.insertOrderLog(request,"1",userDto.getCustId(),order.getFlowId(),orderCreateDto.getSource());
			}
		}
		map = new HashMap<String,Object>();
		map.put("url","/order/createOrderSuccess?orderIds="+orderIdStr);

		/* 生成账期订单后，调用接口更新资信可用额度 */
		List<Order> orderNewList = (List<Order>) newOrderMap.get("orderNewList");
		logger.info("创建订单接口-资信可用额度更新接口,creditDubboService = " + creditDubboService);
		if (!UtilHelper.isEmpty(orderNewList) && !UtilHelper.isEmpty(creditDubboService)) {
			CreditParams creditParams = null;
			for(Order order : orderNewList){
				if(UtilHelper.isEmpty(order)) continue;
				if (!SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(order.getPayTypeId())) {
					continue;
				}
				creditParams = new CreditParams();
				creditParams.setStatus("1");  //创建订单设置为1，收货时设置2，已还款设置4，（取消订单）已退款设置为5，创建退货订单设置为6
				creditParams.setFlowId(order.getFlowId());//订单编号
				creditParams.setOrderTotal(order.getOrgTotal());//订单优惠后的金额
				creditParams.setBuyerCode(order.getCustId() + "");
				creditParams.setBuyerName(order.getCustName());
				creditParams.setSellerCode(order.getSupplyId() + "");
				creditParams.setSellerName(order.getSupplyName());
				creditParams.setPaymentDays(order.getPaymentTerm());//账期
				CreditDubboResult creditDubboResult = null;
				try{
					logger.info("创建订单接口-生成账期订单后，调用接口更新资信可用额度,请求参数 creditParams= " + creditParams);
					creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
					logger.info("创建订单接口-生成账期订单后，调用接口更新资信可用额度,响应参数creditDubboResult= " + creditDubboResult);

					/* 账期订单信息发送成功后，更新该订单的支付状态与支付时间 */
					if(!UtilHelper.isEmpty(creditDubboResult) && "1".equals(creditDubboResult.getIsSuccessful())){
						order.setOrderStatus(SystemOrderStatusEnum.BuyerAlreadyPaid.getType());
						order.setPayStatus(OrderPayStatusEnum.PAYED.getPayStatus());
						order.setPayTime(systemDateService.getSystemDate());
						this.orderService.update(order);
						logger.info("创建订单接口-生成账期订单后，成功更新资信可用额度,更新订单信息，order=" + order);
					}else{
						logger.info("创建订单接口-生成账期订单后，更新资信可用额度失败..message" + (UtilHelper.isEmpty(creditDubboResult) ? "null" : creditDubboResult.getMessage()));
					}
				}catch (Exception e){
					logger.error(e.getMessage());
					throw new RuntimeException("提交订单失败!");
				}

			}
		}
		
		return map;
	}

}
