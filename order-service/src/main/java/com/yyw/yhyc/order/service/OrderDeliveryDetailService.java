/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:49
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.helper.DateHelper;
import com.yyw.yhyc.helper.SpringBeanHelper;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.OrderDeliveryDetailDto;
import com.yyw.yhyc.order.dto.OrderLogDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.*;
import com.yyw.yhyc.order.manage.OrderPayManage;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.pay.interfaces.PayService;
import com.yyw.yhyc.usermanage.bo.UsermanageReceiverAddress;
import com.yyw.yhyc.usermanage.mapper.UsermanageReceiverAddressMapper;

import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.bo.Pagination;

@Service("orderDeliveryDetailService")
public class OrderDeliveryDetailService {

	private static final Logger log = LoggerFactory.getLogger(OrderDeliveryDetailService.class);


	private OrderDeliveryDetailMapper	orderDeliveryDetailMapper;

	private OrderDetailMapper orderDetailMapper;

	private OrderReturnMapper orderReturnMapper;

	private SystemDateMapper systemDateMapper;

	private OrderMapper orderMapper;

	private OrderExceptionMapper orderExceptionMapper;

	private OrderTraceMapper orderTraceMapper;
  
	@Autowired
	private OrderTraceService orderTraceService;
	
	@Autowired
	private SystemPayTypeService systemPayTypeService;

	@Autowired
	private OrderSettlementMapper orderSettlementMapper;

	@Autowired
	private OrderSettlementService orderSettlementService;
	@Autowired
	private UsermanageReceiverAddressMapper receiverAddressMapper;
	
	 @Autowired
	 private OrderReceiveService orderReceviveService;


	@Autowired
	public void setOrderTraceMapper(OrderTraceMapper orderTraceMapper) {
		this.orderTraceMapper = orderTraceMapper;
	}

	@Autowired
	public void setOrderExceptionMapper(OrderExceptionMapper orderExceptionMapper) {
		this.orderExceptionMapper = orderExceptionMapper;
	}

	@Autowired
	public void setOrderMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}

	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}

	@Autowired
	public void setOrderReturnMapper(OrderReturnMapper orderReturnMapper) {
		this.orderReturnMapper = orderReturnMapper;
	}

	@Autowired
	public void setOrderDetailMapper(OrderDetailMapper orderDetailMapper) {
		this.orderDetailMapper = orderDetailMapper;
	}

	@Autowired
	public void setOrderDeliveryDetailMapper(OrderDeliveryDetailMapper orderDeliveryDetailMapper)
	{
		this.orderDeliveryDetailMapper = orderDeliveryDetailMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderDeliveryDetail getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderDeliveryDetailMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDeliveryDetail> list() throws Exception
	{
		return orderDeliveryDetailMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDeliveryDetail> listByProperty(OrderDeliveryDetail orderDeliveryDetail)
			throws Exception
	{
		return orderDeliveryDetailMapper.listByProperty(orderDeliveryDetail);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderDeliveryDetailDto> listPaginationByProperty(Pagination<OrderDeliveryDetailDto> pagination, OrderDeliveryDetailDto orderDeliveryDetailDto) throws Exception
	{
		List<OrderDeliveryDetailDto> list = orderDeliveryDetailMapper.listPaginationByProperty(pagination, orderDeliveryDetailDto);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderDeliveryDetailDto> listPaginationReturnByProperty(Pagination<OrderDeliveryDetailDto> pagination, OrderDeliveryDetailDto orderDeliveryDetailDto) throws Exception
	{
		List<OrderDeliveryDetailDto> list = orderDeliveryDetailMapper.listPaginationReturnByProperty(pagination, orderDeliveryDetailDto);

		pagination.setResultList(list);

		return pagination;
	}
	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderDeliveryDetailMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderDeliveryDetailMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		return orderDeliveryDetailMapper.deleteByProperty(orderDeliveryDetail);
	}

	/**
	 * 保存记录
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public void save(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		orderDeliveryDetailMapper.save(orderDeliveryDetail);
	}

	/**
	 * 更新记录
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public int update(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		return orderDeliveryDetailMapper.update(orderDeliveryDetail);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		return orderDeliveryDetailMapper.findByCount(orderDeliveryDetail);
	}

	/**
	 * 确认收货
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public  Map<String,String> updateConfirmReceipt(List<OrderDeliveryDetailDto> list,UserDto user) throws Exception{

		Map<String, String> returnMap = new HashMap<String, String>();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		String returnType = "";
		String returnDesc = "";
		String flowId = "";
		String exceptionOrderId="";//异常订单号
		String selectAddressId="";
		Integer orderDeliveryDetailId=null;
		 String now=systemDateMapper.getSystemDate();//系统当前时间
		if (UtilHelper.isEmpty(list)||list.size()==0){
			returnMap.put("code","0");
			returnMap.put("msg","参数不能为空");
			return returnMap;
		}else{
			flowId=list.get(0).getFlowId();
			orderDeliveryDetailId=list.get(0).getOrderDeliveryDetailId();
			returnType=list.get(0).getReturnType();
			returnDesc=list.get(0).getReturnDesc();
		    if(!UtilHelper.isEmpty(returnType) && returnType.equals("3")){//补货类型
		    	selectAddressId=list.get(0).getSelectDeliveryAddressId();
		    }
		}

		//统计退款总金额
		BigDecimal moneyTotal=new BigDecimal(0);


		if (UtilHelper.isEmpty(flowId)){
			returnMap.put("code","0");
			returnMap.put("msg","订单编号不能为空");
			return returnMap;
		}

		//如果收货异常根据异常类型更新订单状态
		Order order = orderMapper.getOrderbyFlowId(flowId);
		if (UtilHelper.isEmpty(order)){
			returnMap.put("code","0");
			returnMap.put("msg","订单不存在");
			return returnMap;
		}

		if (!order.getOrderStatus().equals(SystemOrderStatusEnum.SellerDelivered.getType())){
			returnMap.put("code","0");
			returnMap.put("msg","当前状态不可收货");
			return returnMap;
		}

		//根据类型生产异常订单号
		if(!UtilHelper.isEmpty(returnType)&&!returnType.equals("")){
			if (returnType.equals("4"))//拒收
				exceptionOrderId="JS"+flowId;
			else if (returnType.equals("3"))//补货
				exceptionOrderId="BH"+flowId;
		}

		//更新批次收货数量
		for (OrderDeliveryDetailDto dto : list){

			if (UtilHelper.isEmpty(dto.getOrderDeliveryDetailId())){
				returnMap.put("code","0");
				returnMap.put("msg","收发货详情不能id为空");
				return returnMap;
			}

			if (UtilHelper.isEmpty(dto.getOrderDetailId())){
				returnMap.put("code","0");
				returnMap.put("msg","订单详情id不能为空");
				return returnMap;
			}

			if (UtilHelper.isEmpty(dto.getRecieveCount())){
				returnMap.put("code","0");
				returnMap.put("msg","确认收货数量不能为空");
				return returnMap;
			}

			OrderDeliveryDetail orderDeliveryDetail = orderDeliveryDetailMapper.getByPK(dto.getOrderDeliveryDetailId());
			if (UtilHelper.isEmpty(orderDeliveryDetail)){
				returnMap.put("code","0");
				returnMap.put("msg","发货信息不存在");
				return returnMap;
			}
			orderDeliveryDetail.setRecieveCount(dto.getRecieveCount());
			orderDeliveryDetailMapper.update(orderDeliveryDetail);
			if(!UtilHelper.isEmpty(returnType)&&!returnType.equals("")){
				//根据发货两比对如果不同则生成退换货信息
				if(orderDeliveryDetail.getDeliveryProductCount()>orderDeliveryDetail.getRecieveCount()){
					OrderDetail orderDetail = orderDetailMapper.getByPK(dto.getOrderDetailId());
					OrderReturn orderReturn=new OrderReturn();
					orderReturn.setOrderDetailId(orderDeliveryDetail.getOrderDetailId());
					orderReturn.setOrderId(orderDeliveryDetail.getOrderId());
					orderReturn.setCustId(user.getCustId());
					orderReturn.setReturnCount(orderDeliveryDetail.getDeliveryProductCount() - orderDeliveryDetail.getRecieveCount());
					
					//如果该操作是拒收的，同时商品参加了满减活动，那么拒收的金额要减掉商品的优惠金额后，再算
					if(returnType.equals("4") && !UtilHelper.isEmpty(orderDetail.getPreferentialCollectionMoney()) ){//拒收
						String[] moneyList=orderDetail.getPreferentialCollectionMoney().split(",");
						BigDecimal shareMoney=new BigDecimal(0);
						for(String currentMoney : moneyList){
							BigDecimal value=new BigDecimal(currentMoney);
							shareMoney=shareMoney.add(value);
						}
						BigDecimal orderDetailMoney=orderDetail.getProductSettlementPrice(); //该笔商品的结算金额
						BigDecimal lastOrderDetailShareMoney=orderDetailMoney.subtract(shareMoney); //减去优惠后的钱
						BigDecimal bigDecimal = new BigDecimal(orderReturn.getReturnCount());
						BigDecimal allRecord=new BigDecimal(orderDeliveryDetail.getDeliveryProductCount());
						
						double currentReturnMoneyTotal=(bigDecimal.doubleValue()/allRecord.doubleValue())*(lastOrderDetailShareMoney.doubleValue());
						BigDecimal currentReturnMoneyValue=new BigDecimal(currentReturnMoneyTotal);
						currentReturnMoneyValue=currentReturnMoneyValue.setScale(2,BigDecimal.ROUND_HALF_UP);
						
						orderReturn.setReturnPay(currentReturnMoneyValue);
						moneyTotal=moneyTotal.add(currentReturnMoneyValue);
					}else{
						BigDecimal bigDecimal = new BigDecimal(orderReturn.getReturnCount());
						BigDecimal currentReturnMoney=orderDetail.getProductPrice().multiply(bigDecimal);
						orderReturn.setReturnPay(currentReturnMoney);
						moneyTotal=moneyTotal.add(currentReturnMoney);
					}
					
					
					orderReturn.setReturnType(returnType);
					orderReturn.setReturnDesc(returnDesc);
					orderReturn.setFlowId(flowId);
					orderReturn.setReturnStatus("1");//未处理
					orderReturn.setCreateTime(now);
					orderReturn.setUpdateTime(now);
					orderReturn.setCreateUser(user.getUserName());
					orderReturn.setUpdateUser(user.getUserName());
					orderReturn.setExceptionOrderId(exceptionOrderId);
					orderReturn.setOrderDeliveryDetailId(orderDeliveryDetail.getOrderDeliveryDetailId());
					orderReturn.setBatchNumber(orderDeliveryDetail.getBatchNumber());
					orderReturn.setProductCode(orderDetail.getProductCode());
					orderReturnMapper.save(orderReturn);
				}
			}
			if(UtilHelper.isEmpty(map.get(dto.getOrderDetailId()))){
				map.put(dto.getOrderDetailId(), dto.getRecieveCount());
			}else {
				map.put(dto.getOrderDetailId(), map.get(dto.getOrderDetailId())+dto.getRecieveCount());
			}
		}

		//更新订单详情总收货数量//并判断采购数量和收货数量是否相同
		for (Integer orderdetailId:map.keySet()) {
			OrderDetail orderDetail = orderDetailMapper.getByPK(orderdetailId);
			orderDetail.setRecieveCount(map.get(orderdetailId));
			orderDetailMapper.update(orderDetail);
			/*if(orderDetail.getProductCount().intValue()!=orderDetail.getRecieveCount().intValue()){
				if(UtilHelper.isEmpty(returnType)||returnType.equals(""))
					 throw new Exception("采购商与收获数不同,拒收类型为空");
			}*/
		}


		if(!UtilHelper.isEmpty(returnType) &&!returnType.equals("")){
			if (returnType.equals("4"))
				order.setOrderStatus(SystemOrderStatusEnum.Rejecting.getType());
			else if (returnType.equals("3"))
				order.setOrderStatus(SystemOrderStatusEnum.Replenishing.getType());
		}else {
			order.setOrderStatus(SystemOrderStatusEnum.BuyerAllReceived.getType());
		}
		order.setReceiveTime(now);
		order.setReceiveType(1);//买家确认收货
		order.setUpdateTime(now);
		order.setUpdateUser(user.getUserName());
		orderMapper.update(order);

		//生成结算信息 (补货不产生结算)
		saveOrderSettlement(order,moneyTotal);

		//插入日志表
		OrderLogDto orderLogDto=new OrderLogDto();
		orderLogDto.setOrderId(order.getOrderId());
		orderLogDto.setNodeName("买家确认收货");
		orderLogDto.setOrderStatus(order.getOrderStatus());
		orderLogDto.setRemark("请求参数list["+list.toString()+"]");
		this.orderTraceService.saveOrderLog(orderLogDto);
		
		/*OrderTrace orderTrace = new OrderTrace();
		orderTrace.setOrderId(order.getOrderId());
		orderTrace.setNodeName("买家确认收货");
		orderTrace.setDealStaff(user.getUserName());
		orderTrace.setRecordDate(now);
		orderTrace.setRecordStaff(user.getUserName());
		orderTrace.setOrderStatus(order.getOrderStatus());
		orderTrace.setCreateTime(now);
		orderTrace.setCreateUser(user.getUserName());
		orderTraceMapper.save(orderTrace);*/

		//生成异常订单
		if (!UtilHelper.isEmpty(returnType) &&!returnType.equals("")){
			OrderException orderException=new OrderException();
			orderException.setOrderId(order.getOrderId());
			orderException.setFlowId(flowId);
			orderException.setCreateTime(now);
			orderException.setCreateUser(user.getUserName());
			orderException.setReturnType(returnType);
			orderException.setReturnDesc(returnDesc);
			if(returnType.equals("3")){
				orderException.setOrderStatus(SystemReplenishmentOrderStatusEnum.BuyerRejectApplying.getType());//待确认
			}else if(returnType.equals("4")){
				orderException.setOrderStatus(SystemOrderExceptionStatusEnum.RejectApplying.getType());//待确认
			}
			orderException.setCustId(order.getCustId());
			orderException.setCustName(order.getCustName());
			orderException.setSupplyId(order.getSupplyId());
			orderException.setSupplyName(order.getSupplyName());
			orderException.setOrderCreateTime(now);
			orderException.setOrderMoney(moneyTotal);
			orderException.setOrderMoneyTotal(order.getOrderTotal());
			orderException.setExceptionOrderId(exceptionOrderId);
			orderException.setUpdateTime(now);
			orderException.setUpdateUser(user.getUserName());
			orderExceptionMapper.save(orderException);
			
			//如果买家的是补货类型的那么，需要保存买家的选择的补货收货地址
			 if(!UtilHelper.isEmpty(returnType) && returnType.equals("3") && !UtilHelper.isEmpty(selectAddressId)){//补货地址
				 
			     UsermanageReceiverAddress receiverAddress = receiverAddressMapper.getByPK(Integer.valueOf(selectAddressId));
			     
			     OrderReceive orderReceiveBean=new OrderReceive();
			     orderReceiveBean.setExceptionOrderId(exceptionOrderId);
			     orderReceiveBean.setFlowId(flowId);
			     orderReceiveBean.setBuyerReceiveAddress(receiverAddress.getProvinceName() + receiverAddress.getCityName() + receiverAddress.getDistrictName() + receiverAddress.getAddress());
			     orderReceiveBean.setBuyerReceiveCity(receiverAddress.getCityCode());
			     orderReceiveBean.setBuyerReceiveContactPhone(receiverAddress.getContactPhone());
			     orderReceiveBean.setBuyerReceivePerson(receiverAddress.getReceiverName());
			     orderReceiveBean.setBuyerReceiveProvince(receiverAddress.getProvinceCode());
			     orderReceiveBean.setBuyerReceiveRegion(receiverAddress.getDistrictCode());
			     orderReceiveBean.setCreateTime(now);
			     orderReceiveBean.setCreateUser(user.getUserName());
			     orderReceiveBean.setUpdateTime(now);
			     orderReceiveBean.setUpdateUser(user.getUserName());
			     this.orderReceviveService.save(orderReceiveBean);
				 
			 }

			//插入异常订单日志
			OrderLogDto exceptionOrderLogDto=new OrderLogDto();
			exceptionOrderLogDto.setOrderId(order.getOrderId());
			
			if(!UtilHelper.isEmpty(returnType) && returnType.equals("3")){
				exceptionOrderLogDto.setNodeName("买家确认收货申请补货：" + SystemReplenishmentOrderStatusEnum.BuyerRejectApplying.getValue()+" flowId=="+orderException.getExceptionOrderId());
			}else if(!UtilHelper.isEmpty(returnType) && returnType.equals("4")){
				exceptionOrderLogDto.setNodeName("买家确认收货申请拒收：" + SystemOrderExceptionStatusEnum.RejectApplying.getValue()+" flowId="+orderException.getExceptionOrderId());
			}
			exceptionOrderLogDto.setOrderStatus(orderException.getOrderStatus());
			exceptionOrderLogDto.setRemark("请求参数list["+list.toString()+"]");
			this.orderTraceService.saveOrderLog(exceptionOrderLogDto);
			
			/*OrderTrace orderTrace1 = new OrderTrace();
			orderTrace1.setOrderId(orderException.getExceptionId());
			if(returnType.equals("3")){
				orderTrace1.setNodeName("确认收货：" + SystemReplenishmentOrderStatusEnum.BuyerRejectApplying.getValue());
			}else if(returnType.equals("4")){
				orderTrace1.setNodeName("确认收货：" + SystemOrderExceptionStatusEnum.RejectApplying.getValue());
			}
			orderTrace1.setDealStaff(user.getUserName());
			orderTrace1.setRecordDate(now);
			orderTrace1.setRecordStaff(user.getUserName());
			orderTrace1.setOrderStatus(orderException.getOrderStatus());
			orderTrace1.setCreateTime(now);
			orderTrace1.setCreateUser(user.getUserName());
			orderTraceMapper.save(orderTrace1);*/

		}
		//正常全部收货确认收货分账
		SystemPayType systemPayType = systemPayTypeService.getByPK(order.getPayTypeId());
		if(order.getOrderStatus().equals(SystemOrderStatusEnum.BuyerAllReceived.getType())
				&&systemPayType.getPayType().equals(SystemPayTypeEnum.PayOnline.getPayType())){
			PayService payService = (PayService) SpringBeanHelper.getBean(systemPayType.getPayCode());
			payService.handleRefund(user,1,order.getFlowId(),"");
		}
		returnMap.put("code","1");
		returnMap.put("msg","操作成功");
		return returnMap;
	}

	/**
	 * 卖家收货（账期支付，银联支付）,自动收货，手动收货，生成结算记录
	 * @param order
	 * @throws Exception
	 */
	public void saveOrderSettlement(Order order,BigDecimal moneyTotal) throws Exception{
		if(UtilHelper.isEmpty(order)){
			throw new RuntimeException("未找到订单");
		}
		//当为账期支付时
		String now = systemDateMapper.getSystemDate();
		SystemPayType systemPayType= systemPayTypeService.getByPK(order.getPayTypeId());
		OrderSettlement orderSettlement = new OrderSettlement();
		if(SystemOrderStatusEnum.Rejecting.getType().equals(order.getOrderStatus())||
				SystemOrderStatusEnum.Replenishing.getType().equals(order.getOrderStatus())){
			//拒收 换货 会在审核，和买家确认收货时产生结算
			return;
		}
		boolean saveFlag = true;
		if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())){
			//非拒收账期产生结算，拒收账期在审核通过产生
		    orderSettlement.setBusinessType(1);
		    orderSettlement.setOrderId(order.getOrderId());
		    orderSettlement.setFlowId(order.getFlowId());
		    orderSettlement.setCustId(order.getCustId());
		    orderSettlement.setCustName(order.getCustName());
		    orderSettlement.setSupplyId(order.getSupplyId());
		    orderSettlement.setSupplyName(order.getSupplyName());
		    orderSettlement.setConfirmSettlement("0");//生成结算信息时都是未结算
		    orderSettlement.setPayTypeId(order.getPayTypeId());
		    orderSettlement.setSettlementTime(now);
		    orderSettlement.setCreateUser(order.getCustName());
		    orderSettlement.setCreateTime(now);
		    orderSettlement.setOrderTime(order.getCreateTime());
		    orderSettlement.setSettlementMoney(order.getOrgTotal());
		}else if(SystemPayTypeEnum.PayOnline.getPayType().equals(systemPayType.getPayType())){//在线支付
			if(OnlinePayTypeEnum.MerchantBank.getPayTypeId().equals(systemPayType.getPayTypeId()) ){
				//如果是招商支付则加上 买家id 银联支付不加
				orderSettlement.setCustId(order.getCustId());
			}
			orderSettlement.setBusinessType(1);
			orderSettlement.setOrderId(order.getOrderId());
			orderSettlement.setFlowId(order.getFlowId());
			orderSettlement.setCustName(order.getCustName());
			orderSettlement.setSupplyId(order.getSupplyId());
			orderSettlement.setSupplyName(order.getSupplyName());
			orderSettlement.setConfirmSettlement("0");//
			orderSettlement.setPayTypeId(order.getPayTypeId());
			orderSettlement.setSettlementTime(now);
			orderSettlement.setCreateUser(order.getCustName());
			orderSettlement.setCreateTime(now);
			orderSettlement.setOrderTime(order.getCreateTime());

			//拒收的 结算金额需要减去拒收的金额
			if(moneyTotal!=null){
				orderSettlement.setSettlementMoney(order.getOrgTotal().subtract(moneyTotal));
			}else {
				orderSettlement.setSettlementMoney(order.getOrgTotal());
			}
		}
		log.info("HHJJ拒收:orderId:"+order.getOrderId());
		log.info("HHJJ拒收支付类型systemPayType.getPayTypeId:"+systemPayType.getPayTypeId());
		log.info("HHJJ拒收结算moneyTotal:"+moneyTotal);
		log.info("HHJJ拒收:order.getOrderStatus:"+order.getOrderStatus());
		//如果全部拒收则不生成结算信息 支付宝结算时加 适用其它支付方式
		//当是线下支付时 SettlementMoney 会为空 不会生成结算
		if(!UtilHelper.isEmpty(orderSettlement.getSettlementMoney())&&!orderSettlement.getSettlementMoney().equals(BigDecimal.ZERO)){
			orderSettlementService.parseSettlementProvince(orderSettlement,order.getCustId()+"");
			orderSettlementMapper.save(orderSettlement);
		}

	}

	/**
	 * 补货订单确认收货商品列表
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderDeliveryDetailDto> listPaginationReplenishment(Pagination<OrderDeliveryDetailDto> pagination, OrderDeliveryDetailDto orderDeliveryDetailDto) throws Exception
	{
		List<OrderDeliveryDetailDto> list = orderDeliveryDetailMapper.listPaginationReplenishment(pagination, orderDeliveryDetailDto);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 订单收货商品列表
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderDeliveryDetailDto> listPaginationOrderDeliveryDetailDto(Pagination<OrderDeliveryDetailDto> pagination, OrderDeliveryDetailDto orderDeliveryDetailDto) throws Exception
	{
		List<OrderDeliveryDetailDto> list = orderDeliveryDetailMapper.listPaginationOrderDeliveryDetailDto(pagination, orderDeliveryDetailDto);

		pagination.setResultList(list);

		return pagination;
	}


	/**
	 * 自动确认收货更新收货数量
	 * @return
	 * @throws Exception
	 */
	public void orderDeliveryDetailCount(Order od){
		OrderDeliveryDetail ode=new OrderDeliveryDetail();
		ode.setFlowId(od.getFlowId());
		orderDeliveryDetailMapper.updateRecieveCount(ode);
		List<OrderDeliveryDetail> list =orderDeliveryDetailMapper.listByProperty(ode);
		for(OrderDeliveryDetail odetail:list){
			OrderDetail orderDetail=orderDetailMapper.getByPK(odetail.getOrderDetailId());
			orderDetail.setUpdateTime(systemDateMapper.getSystemDate());
			if(UtilHelper.isEmpty(orderDetail.getRecieveCount())){
				orderDetail.setRecieveCount(0);
			}
			orderDetail.setRecieveCount(orderDetail.getRecieveCount()+odetail.getRecieveCount());
			orderDetailMapper.update(orderDetail);
		}
	}
}