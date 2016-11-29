package com.yyw.yhyc.order.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yao.trade.interfaces.credit.model.CreditDubboResult;
import com.yao.trade.interfaces.credit.model.CreditParams;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yyw.yhyc.helper.SpringBeanHelper;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.order.bo.OrderTrace;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.OrderLogDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.manage.OrderPayManage;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.order.mapper.OrderSettlementMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.order.mapper.SystemPayTypeMapper;
import com.yyw.yhyc.pay.interfaces.PayService;
import com.yyw.yhyc.product.manage.ProductInventoryManage;

@Service("orderCancelService")
public class OrderCancelService {
	private Log log = LogFactory.getLog(OrderCancelService.class);
	@Autowired
	private OrderMapper	orderMapper;
	@Autowired
	private SystemPayTypeMapper systemPayTypeMapper;
	@Autowired
	private SystemDateMapper systemDateMapper;
	@Autowired
	private OrderSettlementMapper orderSettlementMapper;
	@Autowired
	private OrderPayManage orderPayManage;
	@Autowired
	private OrderTraceService orderTraceService;
	@Autowired
	private ProductInventoryManage productInventoryManage;
	@Autowired
	private  OrderSettlementService orderSettlementService;
	
	
	/**
	 * 供应商取消订单
	 * @param userDto
	 * @param orderId
	 * @param iPromotionDubboManageService 
	 */
	public void  updateOrderStatusForSeller(UserDto userDto,Integer orderId,String cancelResult, IPromotionDubboManageService iPromotionDubboManageService,CreditDubboServiceInterface creditDubboService){
		if(UtilHelper.isEmpty(userDto.getCustId()) || UtilHelper.isEmpty(orderId) || UtilHelper.isEmpty(cancelResult)){
			throw new RuntimeException("参数错误");
		}
		Order order =  orderMapper.getByPK(orderId);
		log.debug(order);
		if(UtilHelper.isEmpty(order)){
			log.error("can not find order ,orderId:"+orderId);
			throw new RuntimeException("未找到订单");
		}
		//判断订单是否属于该卖家
		if(userDto.getCustId() == order.getSupplyId()){
			if((SystemOrderStatusEnum.BuyerOrdered.getType().equals(order.getOrderStatus()) ) || SystemOrderStatusEnum.BuyerAlreadyPaid.getType().equals(order.getOrderStatus())){//已下单订单+买家已付款订单
				order.setOrderStatus(SystemOrderStatusEnum.SellerCanceled.getType());//标记订单为卖家取消状态
				String now = systemDateMapper.getSystemDate();
				order.setCancelResult("卖家取消");
				order.setRemark(cancelResult);
				order.setUpdateUser(userDto.getUserName());
				order.setUpdateTime(now);
				order.setCancelTime(now);
				int count = orderMapper.update(order);
				if(count == 0){
					log.error("order info :"+order);
					throw new RuntimeException("订单取消失败");
				}

				SystemPayType systemPayType = systemPayTypeMapper.getByPK(order.getPayTypeId());
				//如果是银联在线支付，生成结算信息，类型为订单取消退款
				if(OnlinePayTypeEnum.UnionPayB2C.getPayTypeId().equals(systemPayType.getPayTypeId())
						||OnlinePayTypeEnum.UnionPayNoCard.getPayTypeId().equals(systemPayType.getPayTypeId())
				        ||OnlinePayTypeEnum.UnionPayMobile.getPayTypeId().equals(systemPayType.getPayTypeId())
						||OnlinePayTypeEnum.UnionPayB2B.getPayTypeId().equals(systemPayType.getPayTypeId())
						||OnlinePayTypeEnum.AlipayWeb.getPayTypeId().equals(systemPayType.getPayTypeId())
						||OnlinePayTypeEnum.AlipayApp.getPayTypeId().equals(systemPayType.getPayTypeId())){
					OrderSettlement orderSettlement = orderSettlementService.parseOnlineSettlement(5,null,null,userDto.getUserName(),null,order);
					orderSettlementMapper.save(orderSettlement);
				}

				if(systemPayType.getPayType().equals(SystemPayTypeEnum.PayOnline.getPayType())) {
					PayService payService = (PayService) SpringBeanHelper.getBean(systemPayType.getPayCode());
					payService.handleRefund(userDto, 1, order.getFlowId(), "卖家主动取消订单");

				}


				//插入日志表
				 OrderTrace orderTrace=new OrderTrace();
				 orderTrace.setNodeName("卖家取消订单");
				 orderTrace.setCreateTime(now);
				 orderTrace.setCreateUser(userDto.getUserName());
				 orderTrace.setOrderId(orderId);
				 orderTrace.setOrderStatus(order.getOrderStatus());
				 orderTrace.setUpdateTime(now);
				 orderTrace.setUpdateUser(userDto.getUserName());
				 try {
					this.orderTraceService.save(orderTrace);
				} catch (Exception e) {
					throw new RuntimeException("保存日志失败");
				}

				//释放冻结库存
				productInventoryManage.releaseInventory(order.getOrderId(),order.getSupplyName(),userDto.getUserName(),iPromotionDubboManageService);
			}else{
				log.error("order status error ,orderStatus:"+order.getOrderStatus());
				throw new RuntimeException("订单状态不正确");
			}
		}else{
			log.error("db orderId not equals to request orderId ,orderId:"+orderId+",db orderId:"+order.getOrderId());
			throw new RuntimeException("未找到订单");
		}
		
		
		try {
			if(UtilHelper.isEmpty(creditDubboService)){
				log.error("CreditDubboServiceInterface creditDubboService is null");
				throw new RuntimeException("资信接口调用失败！无资信服务！");
			}else{
				Order od =  this.orderMapper.getByPK(order.getOrderId());
				SystemPayType systemPayType= this.systemPayTypeMapper.getByPK(od.getPayTypeId());
				if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())){
					CreditParams creditParams = new CreditParams();
					creditParams.setSourceFlowId(od.getFlowId());//订单编码
					creditParams.setBuyerCode(od.getCustId() + "");
					creditParams.setSellerCode(od.getSupplyId() + "");
					creditParams.setBuyerName(od.getCustName());
					creditParams.setSellerName(od.getSupplyName());
					creditParams.setOrderTotal(od.getOrgTotal());//订单原始金额
					creditParams.setFlowId(od.getFlowId());//订单编码
					creditParams.setStatus("5");//创建订单设置为1，收货时设置2，已还款设置4，（取消订单）已退款设置为5，创建退货订单设置为6
					// TODO: 2016/8/24 暂时忽略资信接口调用错误 
					try{
						CreditDubboResult creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
						if(UtilHelper.isEmpty(creditDubboResult) || "0".equals(creditDubboResult.getIsSuccessful())){
							throw new RuntimeException(creditDubboResult !=null?creditDubboResult.getMessage():"接口调用失败！");
						}
					}catch (Exception e){
						log.error("invoke creditDubboService.updateCreditRecord(..) error,msg:"+e.getMessage());
						throw new RuntimeException("调用接口调用失败！");
					}
				}
			}
		} catch (Exception e) {
			log.error("orderService.getByPK error, pk: "+order.getOrderId()+",errorMsg:"+e.getMessage());
			e.printStackTrace();
		}
	}
}
