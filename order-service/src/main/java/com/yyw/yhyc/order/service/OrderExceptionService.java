/**
 * Created By: XI
 * Created On: 2016-8-8 10:31:59
 *
 * Amendment History:
 *
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import com.yyw.yhyc.order.bo.OrderTrace;
import com.yyw.yhyc.order.dto.OrderExceptionDto;

import com.yyw.yhyc.order.dto.OrderReturnDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.*;
import com.yyw.yhyc.order.enmu.SystemOrderExceptionStatusEnum;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.utils.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.helper.UtilHelper;

@Service("orderExceptionService")
public class OrderExceptionService {

	private Log log = LogFactory.getLog(OrderExceptionService.class);

	private OrderExceptionMapper	orderExceptionMapper;
	private OrderSettlementMapper orderSettlementMapper;
	private SystemDateMapper systemDateMapper;
	private OrderMapper	orderMapper;
	private OrderTraceMapper orderTraceMapper;

	@Autowired
	public void setOrderExceptionMapper(OrderExceptionMapper orderExceptionMapper)
	{
		this.orderExceptionMapper = orderExceptionMapper;
	}
	@Autowired
	public void setOrderSettlementMapper(OrderSettlementMapper orderSettlementMapper) {
		this.orderSettlementMapper = orderSettlementMapper;
	}
	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}
	@Autowired
	public void setOrderMapper(OrderMapper orderMapper)
	{
		this.orderMapper = orderMapper;
	}

	@Autowired
	public void setOrderTraceMapper(OrderTraceMapper orderTraceMapper) {
		this.orderTraceMapper = orderTraceMapper;
	}
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderException getByPK(Integer primaryKey) throws Exception
	{
		return orderExceptionMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderException> list() throws Exception
	{
		return orderExceptionMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderException> listByProperty(OrderException orderException)
			throws Exception
	{
		return orderExceptionMapper.listByProperty(orderException);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderException> listPaginationByProperty(Pagination<OrderException> pagination, OrderException orderException) throws Exception
	{
		List<OrderException> list = orderExceptionMapper.listPaginationByProperty(pagination, orderException);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(Integer primaryKey) throws Exception
	{
		return orderExceptionMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<Integer> primaryKeys) throws Exception
	{
		orderExceptionMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderException
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderException orderException) throws Exception
	{
		return orderExceptionMapper.deleteByProperty(orderException);
	}

	/**
	 * 保存记录
	 * @param orderException
	 * @return
	 * @throws Exception
	 */
	public void save(OrderException orderException) throws Exception
	{
		orderExceptionMapper.save(orderException);
	}

	/**
	 * 更新记录
	 * @param orderException
	 * @return
	 * @throws Exception
	 */
	public int update(OrderException orderException) throws Exception
	{
		return orderExceptionMapper.update(orderException);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderException
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderException orderException) throws Exception
	{
		return orderExceptionMapper.findByCount(orderException);
	}

	/**
	 * 退货订单详情
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public OrderExceptionDto getOrderExceptionDetails(OrderExceptionDto orderExceptionDto) throws Exception{
		Integer userType = orderExceptionDto.getUserType();
		orderExceptionDto = orderExceptionMapper.getOrderExceptionDetails(orderExceptionDto);
		/* 计算商品总额 */
		if(!UtilHelper.isEmpty(orderExceptionDto) && !UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())){
			BigDecimal productPriceCount = new BigDecimal(0);
			for(OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()){
				if(UtilHelper.isEmpty(orderReturnDto)) continue;
				productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
			}
			orderExceptionDto.setProductPriceCount(productPriceCount);
		}

		/* 获得拒收订单的状态名 */
		if (userType == 1) {
			BuyerOrderExceptionStatusEnum buyerOrderExceptionStatusEnum = getBuyerOrderExceptionStatus(orderExceptionDto.getOrderStatus(),orderExceptionDto.getPayType());
			orderExceptionDto.setOrderStatusName(buyerOrderExceptionStatusEnum.getValue());
		} else if (userType == 2) {
			SellerOrderExceptionStatusEnum sellerOrderExceptionStatus = getSellerOrderExceptionStatus(orderExceptionDto.getOrderStatus(),orderExceptionDto.getPayType());
			orderExceptionDto.setOrderStatusName(sellerOrderExceptionStatus.getValue());
		}
		return orderExceptionDto;
	}
	/**
	 * 拒收订单卖家审核通过生成结算记录
	 * @param custId
	 * @param orderException
	 * @throws Exception
	 */
	private void saveRefuseOrderSettlement(Integer custId,OrderException orderException){
		Order order = orderMapper.getByPK(orderException.getOrderId());
		if(UtilHelper.isEmpty(order)||!custId.equals(order.getSupplyId())){
			throw new RuntimeException("未找到订单");
		}
		String now = systemDateMapper.getSystemDate();
		OrderSettlement orderSettlement = new OrderSettlement();
		orderSettlement.setBusinessType(2);
		orderSettlement.setFlowId(orderException.getExceptionOrderId());
		orderSettlement.setCustId(orderException.getCustId());
		orderSettlement.setCustName(orderException.getCustName());
		orderSettlement.setSupplyId(orderException.getSupplyId());
		orderSettlement.setSupplyName(orderException.getSupplyName());
		orderSettlement.setConfirmSettlement("1");
		orderSettlement.setPayTypeId(order.getPayTypeId());
		orderSettlement.setSettlementTime(now);
		orderSettlement.setCreateUser(orderException.getCustName());
		orderSettlement.setCreateTime(now);
		orderSettlement.setOrderTime(order.getCreateTime());
		orderSettlement.setSettlementMoney(orderException.getOrderMoney());
		orderSettlement.setRefunSettlementMoney(orderException.getOrderMoney());
		orderSettlementMapper.save(orderSettlement);
	}

	/**
	 * 采购商拒收订单查询
	 * @param pagination
	 * @param orderExceptionDto
     * @return
     */
	public Map<String, Object> listPgBuyerRejectOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto){
		Map<String,Object> resultMap = new HashMap<String,Object>();

		if(UtilHelper.isEmpty(orderExceptionDto))
			throw new RuntimeException("参数错误");
		log.info("request orderExceptionDto :"+orderExceptionDto.toString());
		if(!UtilHelper.isEmpty(orderExceptionDto.getEndTime())){
			try {
				Date endTime = DateUtils.formatDate(orderExceptionDto.getEndTime(),"yyyy-MM-dd");
				Date endTimeAddOne = DateUtils.addDays(endTime,1);
				orderExceptionDto.setEndTime(DateUtils.getStringFromDate(endTimeAddOne));
			} catch (ParseException e) {
				log.error("datefromat error,date: "+orderExceptionDto.getEndTime());
				e.printStackTrace();
				throw new RuntimeException("日期错误");
			}

		}

		int orderCount = 0;
		BigDecimal orderTotalMoney = orderExceptionMapper.findBuyerExceptionOrderTotal(orderExceptionDto);

		log.info("orderTotalMoney:"+orderTotalMoney);

		List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPaginationBuyerRejectOrder(pagination, orderExceptionDto);
		log.info("orderExceptionDtoList:"+orderExceptionDtoList);


		BuyerOrderExceptionStatusEnum buyerOrderExceptionStatusEnum;
		if(!UtilHelper.isEmpty(orderExceptionDtoList)){
			orderCount = pagination.getTotal();
			for(OrderExceptionDto oed : orderExceptionDtoList){
				buyerOrderExceptionStatusEnum = getBuyerOrderExceptionStatus(oed.getOrderStatus(),oed.getPayType());
				if(!UtilHelper.isEmpty(buyerOrderExceptionStatusEnum))
					oed.setOrderStatusName(buyerOrderExceptionStatusEnum.getValue());
				else
					oed.setOrderStatusName("未知状态");
			}
		}

		pagination.setResultList(orderExceptionDtoList);

		Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
		List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findBuyerRejectOrderStatusCount(orderExceptionDto);;

		if (!UtilHelper.isEmpty(orderExceptionDtos)) {
			for (OrderExceptionDto oed : orderExceptionDtos) {
				//卖家视角订单状态
				buyerOrderExceptionStatusEnum = getBuyerOrderExceptionStatus(oed.getOrderStatus(),oed.getPayType());
				if(buyerOrderExceptionStatusEnum != null){
					if(orderStatusCountMap.containsKey(buyerOrderExceptionStatusEnum.getType())){
						orderStatusCountMap.put(buyerOrderExceptionStatusEnum.getType(),orderStatusCountMap.get(buyerOrderExceptionStatusEnum.getType())+oed.getOrderCount());
					}else{
						orderStatusCountMap.put(buyerOrderExceptionStatusEnum.getType(),oed.getOrderCount());
					}
				}
			}
		}
		log.info("orderStatusCountMap:"+orderStatusCountMap);

		resultMap.put("rejectOrderStatusCount", orderStatusCountMap);
		resultMap.put("rejectOrderList", pagination);
		resultMap.put("rejectOrderCount", orderCount);
		resultMap.put("rejectOrderTotalMoney", orderTotalMoney == null? 0:orderTotalMoney);
		return resultMap;
	}

	/**
	 * 获取采购商视角异常订单状态
	 * @param systemExceptionOrderStatus
	 * @param payType
	 * @return
	 */
	BuyerOrderExceptionStatusEnum getBuyerOrderExceptionStatus(String systemExceptionOrderStatus,int payType){
		BuyerOrderExceptionStatusEnum buyerOrderExceptionStatusEnum = null;
		if(SystemOrderExceptionStatusEnum.RejectApplying.getType().equals(systemExceptionOrderStatus)){//拒收申请中
			buyerOrderExceptionStatusEnum = BuyerOrderExceptionStatusEnum.WaitingConfirmation;//待确认
		}
		if(SystemOrderExceptionStatusEnum.BuyerConfirmed.getType().equals(systemExceptionOrderStatus)){//卖家已确认
			if(payType == 1 || payType == 2){//在线支付+账期支付
				buyerOrderExceptionStatusEnum = BuyerOrderExceptionStatusEnum.Refunded;//已完成
			}
			if(payType == 3){//线下支付
				buyerOrderExceptionStatusEnum = BuyerOrderExceptionStatusEnum.Refunding;//退款中
			}
		}
		if(SystemOrderExceptionStatusEnum.SellerClosed.getType().equals(systemExceptionOrderStatus)){//卖家已关闭
			buyerOrderExceptionStatusEnum = BuyerOrderExceptionStatusEnum.Closed;//已关闭
		}
		if(SystemOrderExceptionStatusEnum.Refunded.getType().equals(systemExceptionOrderStatus) && payType == 3){//已退款+线下支付
			buyerOrderExceptionStatusEnum = BuyerOrderExceptionStatusEnum.Refunded;//已关闭
		}
		return buyerOrderExceptionStatusEnum;
	}

	/**
	 * 获取卖家视角异常订单状态
	 * @param systemExceptionOrderStatus
	 * @param payType
	 * @return
	 */
	SellerOrderExceptionStatusEnum getSellerOrderExceptionStatus(String systemExceptionOrderStatus, int payType){
		SellerOrderExceptionStatusEnum sellerOrderExceptionStatusEnum = null;
		if(SystemOrderExceptionStatusEnum.RejectApplying.getType().equals(systemExceptionOrderStatus)){//拒收申请中
			sellerOrderExceptionStatusEnum = SellerOrderExceptionStatusEnum.WaitingConfirmation;//待确认
		}
		if(SystemOrderExceptionStatusEnum.BuyerConfirmed.getType().equals(systemExceptionOrderStatus)){//卖家已确认
			if(payType == 1 || payType == 2){//在线支付+账期支付
				sellerOrderExceptionStatusEnum = SellerOrderExceptionStatusEnum.Refunded;//已完成
			}
			if(payType == 3){//线下支付
				sellerOrderExceptionStatusEnum = SellerOrderExceptionStatusEnum.Refunding;//退款中
			}
		}
		if(SystemOrderExceptionStatusEnum.SellerClosed.getType().equals(systemExceptionOrderStatus)){//卖家已关闭
			sellerOrderExceptionStatusEnum = SellerOrderExceptionStatusEnum.Closed;//已关闭
		}
		if(SystemOrderExceptionStatusEnum.Refunded.getType().equals(systemExceptionOrderStatus) && payType == 3){//已退款+线下支付
			sellerOrderExceptionStatusEnum = SellerOrderExceptionStatusEnum.Refunded;//已关闭
		}
		return sellerOrderExceptionStatusEnum;
	}



	public Map<String,Object> listPaginationSellerByProperty(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) throws Exception{

		List<OrderExceptionDto> list = orderExceptionMapper.listPaginationSellerByProperty(pagination,orderExceptionDto);
		Integer sourceType = orderExceptionDto.getType() ;
		//查询总价
		BigDecimal totalMoney = orderExceptionMapper.findSellerExceptionOrderTotal(orderExceptionDto);
		//查询
		Integer waitCount = pagination.getTotal();
		Integer refundCount = pagination.getTotal();
		orderExceptionDto.setType(2);
		if(sourceType != 2){
			waitCount = orderExceptionMapper.findSellerRejectOrderStatusCount(orderExceptionDto);
		}
		orderExceptionDto.setType(3);
		if(sourceType!=3){
			refundCount = orderExceptionMapper.findSellerRejectOrderStatusCount(orderExceptionDto);
		}
		orderExceptionDto.setWaitingConfirmCount(waitCount);
		orderExceptionDto.setRefundingCount(refundCount);
		orderExceptionDto.setOrderMoneyTotal(totalMoney);
		orderExceptionDto.setType(sourceType);
		if(!UtilHelper.isEmpty(list)){ //查全部的时候转换
			for (OrderExceptionDto oed:list ) {
				switch (sourceType){
					case 1 :
						SellerOrderExceptionStatusEnum statusEnum= getSellerOrderExceptionStatus(oed.getOrderStatus(),oed.getPayType());
						oed.setOrderStatusName(statusEnum.getValue());
						break;
					case 2:
						oed.setOrderStatusName(SellerOrderExceptionStatusEnum.WaitingConfirmation.getValue());
						break;
					case 3:
						oed.setOrderStatusName(SellerOrderExceptionStatusEnum.Refunding.getValue());
						break;
					case 4:
						oed.setOrderStatusName(SellerOrderExceptionStatusEnum.Refunded.getValue());
						break;
					case 5:
						oed.setOrderStatusName(SellerOrderExceptionStatusEnum.Closed.getValue());
						break;
					default:
						oed.setOrderStatusName(SellerOrderExceptionStatusEnum.Closed.getValue());
						break;
				}
			}
		}
		pagination.setResultList(list);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pagination",pagination);
		map.put("orderExceptionDto",orderExceptionDto);
		return map;
	}


	/**
	 * 审核退货订单详情
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public OrderExceptionDto getRejectOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception{
		orderExceptionDto = orderExceptionMapper.getOrderExceptionDetails(orderExceptionDto);
		if(!UtilHelper.isEmpty(orderExceptionDto) && !UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())){
			BigDecimal productPriceCount = new BigDecimal(0);
			for(OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()){
				if(UtilHelper.isEmpty(orderReturnDto)) continue;
				productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
			}
			orderExceptionDto.setProductPriceCount(productPriceCount);
			orderExceptionDto.setOrderStatusName(getSellerOrderExceptionStatus(orderExceptionDto.getOrderStatus(),orderExceptionDto.getPayType()).getValue());
		}
		return orderExceptionDto;
	}

	/**
	 * 卖家审核拒收订单
	 * @param userDto
	 * @param orderException
     */
	public void sellerReviewRejectOrder(UserDto userDto,OrderException orderException){
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(orderException) || UtilHelper.isEmpty(orderException.getExceptionId()))
			throw new RuntimeException("参数异常");

		// 验证审核状态
		if(!(SystemOrderExceptionStatusEnum.BuyerConfirmed.getType().equals(orderException.getOrderStatus()) || SystemOrderExceptionStatusEnum.SellerClosed.getType().equals(orderException.getOrderStatus())))
			throw new RuntimeException("参数异常");

		OrderException oe = orderExceptionMapper.getByPK(orderException.getExceptionId());
		if(UtilHelper.isEmpty(oe))
			throw new RuntimeException("未找到拒收订单");
		if(userDto.getCustId() != oe.getSupplyId()){
			log.info("拒收订单不属于该卖家,OrderException:"+oe+",UserDto:"+userDto);
			throw new RuntimeException("未找到拒收订单");
		}
		//判断是否是拒收订单
		if(!"4".equals(oe.getReturnType())){
			log.info("拒收订单不属于该卖家,OrderException:"+oe+",UserDto:"+userDto);
			throw new RuntimeException("该订单不是拒收订单");
		}
		if(!SystemOrderExceptionStatusEnum.RejectApplying.getType().equals(oe.getOrderStatus())){
			log.info("拒收订单状态不正确,OrderException:"+oe);
			throw new RuntimeException("拒收订单状态不正确");
		}
		String now = systemDateMapper.getSystemDate();
		oe.setRemark(orderException.getRemark());
		oe.setOrderStatus(orderException.getOrderStatus());
		oe.setUpdateUser(userDto.getUserName());
		oe.setUpdateTime(now);
		int count = orderExceptionMapper.update(oe);
		if(count == 0){
			log.error("OrderException info :"+oe);
			throw new RuntimeException("拒收订单审核失败");
		}

		//插入日志表
		OrderTrace orderTrace = new OrderTrace();
		orderTrace.setOrderId(oe.getExceptionId());
		orderTrace.setNodeName(SystemOrderExceptionStatusEnum.getName(oe.getOrderStatus())+oe.getRemark());
		orderTrace.setDealStaff(userDto.getUserName());
		orderTrace.setRecordDate(now);
		orderTrace.setRecordStaff(userDto.getUserName());
		orderTrace.setOrderStatus(oe.getOrderStatus());
		orderTrace.setCreateTime(now);
		orderTrace.setCreateUser(userDto.getUserName());
		orderTraceMapper.save(orderTrace);

		//拒收订单卖家审核通过生成结算记录
		this.saveRefuseOrderSettlement(userDto.getCustId(),oe);

	}

	/**
	 * 采购商换货订单查询
	 * @param pagination
	 * @param orderExceptionDto
	 * @return
	 */
	public Map<String, Object> listPgBuyerChangeGoodsOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto){
		Map<String,Object> resultMap = new HashMap<String,Object>();

		if(UtilHelper.isEmpty(orderExceptionDto))
			throw new RuntimeException("参数错误");
		log.info("request orderExceptionDto :"+orderExceptionDto.toString());
		if(!UtilHelper.isEmpty(orderExceptionDto.getEndTime())){
			try {
				Date endTime = DateUtils.formatDate(orderExceptionDto.getEndTime(),"yyyy-MM-dd");
				Date endTimeAddOne = DateUtils.addDays(endTime,1);
				orderExceptionDto.setEndTime(DateUtils.getStringFromDate(endTimeAddOne));
			} catch (ParseException e) {
				log.error("datefromat error,date: "+orderExceptionDto.getEndTime());
				e.printStackTrace();
				throw new RuntimeException("日期错误");
			}
		}
		int orderCount = 0;
		BigDecimal orderTotalMoney = orderExceptionMapper.findBuyerChangeGoodsExceptionOrderTotal(orderExceptionDto);
		log.info("orderTotalMoney:"+orderTotalMoney);

		List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPgBuyerChangeGoodsOrder(pagination, orderExceptionDto);
		log.info("orderExceptionDtoList:"+orderExceptionDtoList);

		BuyerChangeGoodsOrderStatusEnum buyerOrderExceptionStatusEnum;
		if(!UtilHelper.isEmpty(orderExceptionDtoList)){
			orderCount = pagination.getTotal();
			for(OrderExceptionDto oed : orderExceptionDtoList){
				buyerOrderExceptionStatusEnum = getBuyerChangeGoodsOrderExceptionStatus(oed.getOrderStatus(),oed.getPayType());
				if(!UtilHelper.isEmpty(buyerOrderExceptionStatusEnum))
					oed.setOrderStatusName(buyerOrderExceptionStatusEnum.getValue());
				else
					oed.setOrderStatusName("未知状态");
			}
		}
		pagination.setResultList(orderExceptionDtoList);
		Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
		List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findBuyerChangeGoodsOrderStatusCount(orderExceptionDto);;

		if (!UtilHelper.isEmpty(orderExceptionDtos)) {
			for (OrderExceptionDto oed : orderExceptionDtos) {
				//买家视角订单状态数量
				buyerOrderExceptionStatusEnum = getBuyerChangeGoodsOrderExceptionStatus(oed.getOrderStatus(),oed.getPayType());
				if(buyerOrderExceptionStatusEnum != null){
					if(orderStatusCountMap.containsKey(buyerOrderExceptionStatusEnum.getType())){
						orderStatusCountMap.put(buyerOrderExceptionStatusEnum.getType(),orderStatusCountMap.get(buyerOrderExceptionStatusEnum.getType())+oed.getOrderCount());
					}else{
						orderStatusCountMap.put(buyerOrderExceptionStatusEnum.getType(),oed.getOrderCount());
					}
				}
			}
		}
		log.info("orderStatusCountMap:"+orderStatusCountMap);
		resultMap.put("rejectOrderStatusCount", orderStatusCountMap);
		resultMap.put("rejectOrderList", pagination);
		resultMap.put("rejectOrderCount", orderCount);
		resultMap.put("rejectOrderTotalMoney", orderTotalMoney == null? 0:orderTotalMoney);
		return resultMap;
	}

	/**
	 * 获取采购商视角换货异常订单状态
	 * @param systemExceptionOrderStatus
	 * @param payType
	 * @return
	 */
	BuyerChangeGoodsOrderStatusEnum getBuyerChangeGoodsOrderExceptionStatus(String systemExceptionOrderStatus,int payType){
		BuyerChangeGoodsOrderStatusEnum buyerOrderExceptionStatusEnum = null;
		if(SystemChangeGoodsOrderStatusEnum.WaitingConfirmation.getType().equals(systemExceptionOrderStatus)){//申请中
			buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.WaitingConfirmation;//待确认
		}
		if(SystemChangeGoodsOrderStatusEnum.Canceled.getType().equals(systemExceptionOrderStatus)){//已取消
			buyerOrderExceptionStatusEnum=BuyerChangeGoodsOrderStatusEnum.Canceled;
		}
		if(SystemChangeGoodsOrderStatusEnum.Closed.getType().equals(systemExceptionOrderStatus)){//卖家已关闭
			buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.Closed;//已关闭
		}
		if(SystemChangeGoodsOrderStatusEnum.WaitingBuyerDelivered.getType().equals(systemExceptionOrderStatus)){//待买家发货
			buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.WaitingBuyerDelivered;//待买家发货
		}
		if(SystemChangeGoodsOrderStatusEnum.WaitingSellerReceived.getType().equals(systemExceptionOrderStatus)){//买家已发货
			buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.WaitingSellerReceived;//
		}

		if(SystemChangeGoodsOrderStatusEnum.WaitingSellerDelivered.getType().equals(systemExceptionOrderStatus)){//卖家已收货
			buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.WaitingSellerDelivered;//
		}

		if(SystemChangeGoodsOrderStatusEnum.WaitingBuyerReceived.getType().equals(systemExceptionOrderStatus)){//卖家已发货
			buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.WaitingBuyerReceived;//
		}

		if(SystemChangeGoodsOrderStatusEnum.Finished.getType().equals(systemExceptionOrderStatus)){//买家已收货
			buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.Finished;//
		}

		if(SystemChangeGoodsOrderStatusEnum.AutoFinished.getType().equals(systemExceptionOrderStatus)){//买家自动收货
			buyerOrderExceptionStatusEnum = BuyerChangeGoodsOrderStatusEnum.Finished;//
		}

		return buyerOrderExceptionStatusEnum;
	}





	/**
	 * 采购商补货订单查询
	 * @param pagination
	 * @param orderExceptionDto
	 * @return
	 */
	public Map<String, Object> listPgBuyerReplenishmentOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto){
		Map<String,Object> resultMap = new HashMap<String,Object>();

		if(UtilHelper.isEmpty(orderExceptionDto))
			throw new RuntimeException("参数错误");
		log.info("request orderExceptionDto :"+orderExceptionDto.toString());
		if(!UtilHelper.isEmpty(orderExceptionDto.getEndTime())){
			try {
				Date endTime = DateUtils.formatDate(orderExceptionDto.getEndTime(),"yyyy-MM-dd");
				Date endTimeAddOne = DateUtils.addDays(endTime,1);
				orderExceptionDto.setEndTime(DateUtils.getStringFromDate(endTimeAddOne));
			} catch (ParseException e) {
				log.error("datefromat error,date: "+orderExceptionDto.getEndTime());
				e.printStackTrace();
				throw new RuntimeException("日期错误");
			}

		}

		int orderCount = 0;
		BigDecimal orderTotalMoney = orderExceptionMapper.findBuyerReplenishmentOrderTotal(orderExceptionDto);

		log.info("orderTotalMoney:"+orderTotalMoney);

		List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPaginationBuyerReplenishmentOrder(pagination, orderExceptionDto);
		log.info("orderExceptionDtoList:"+orderExceptionDtoList);


		BuyerReplenishmentOrderStatusEnum buyerReplenishmentOrderStatusEnum;
		if(!UtilHelper.isEmpty(orderExceptionDtoList)){
			orderCount = pagination.getTotal();
			for(OrderExceptionDto oed : orderExceptionDtoList){
				buyerReplenishmentOrderStatusEnum = getBuyerReplenishmentOrderStatus(oed.getOrderStatus());
				if(!UtilHelper.isEmpty(buyerReplenishmentOrderStatusEnum))
					oed.setOrderStatusName(buyerReplenishmentOrderStatusEnum.getValue());
				else
					oed.setOrderStatusName("未知状态");
			}
		}

		pagination.setResultList(orderExceptionDtoList);

		Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
		List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findBuyerReplenishmentStatusCount(orderExceptionDto);;

		if (!UtilHelper.isEmpty(orderExceptionDtos)) {
			for (OrderExceptionDto oed : orderExceptionDtos) {
				//卖家视角订单状态
				buyerReplenishmentOrderStatusEnum = getBuyerReplenishmentOrderStatus(oed.getOrderStatus());
				if(buyerReplenishmentOrderStatusEnum != null){
					if(orderStatusCountMap.containsKey(buyerReplenishmentOrderStatusEnum.getType())){
						orderStatusCountMap.put(buyerReplenishmentOrderStatusEnum.getType(),orderStatusCountMap.get(buyerReplenishmentOrderStatusEnum.getType())+oed.getOrderCount());
					}else{
						orderStatusCountMap.put(buyerReplenishmentOrderStatusEnum.getType(),oed.getOrderCount());
					}
				}
			}
		}
		log.info("orderStatusCountMap:"+orderStatusCountMap);

		resultMap.put("orderStatusCount", orderStatusCountMap);
		resultMap.put("orderList", pagination);
		resultMap.put("orderCount", orderCount);
		resultMap.put("orderTotalMoney", orderTotalMoney == null? 0:orderTotalMoney);
		return resultMap;
	}

	/**
	 * 获取买家视角补货订单状态
	 * @param systemReplementOrderStatus
	 * @return
     */
	BuyerReplenishmentOrderStatusEnum getBuyerReplenishmentOrderStatus(String systemReplementOrderStatus){
		if(SystemReplenishmentOrderStatusEnum.BuyerRejectApplying.getType().equals(systemReplementOrderStatus))//买家已申请
			return BuyerReplenishmentOrderStatusEnum.WaitingConfirmation;
		if(SystemReplenishmentOrderStatusEnum.SellerConfirmed.getType().equals(systemReplementOrderStatus))//卖家已确认
			return BuyerReplenishmentOrderStatusEnum.WaitingDelivered;
		if(SystemReplenishmentOrderStatusEnum.SellerClosed.getType().equals(systemReplementOrderStatus))//卖家已关闭
			return BuyerReplenishmentOrderStatusEnum.Closed;
		if(SystemReplenishmentOrderStatusEnum.SellerDelivered.getType().equals(systemReplementOrderStatus))//卖家已发货
			return BuyerReplenishmentOrderStatusEnum.WaitingReceived;
		if(SystemReplenishmentOrderStatusEnum.BuyerReceived.getType().equals(systemReplementOrderStatus) || SystemReplenishmentOrderStatusEnum.SystemAutoConfirmReceipt.getType().equals(systemReplementOrderStatus))//买家已收货+系统自动确认收货
			return BuyerReplenishmentOrderStatusEnum.Finished;
		return null;
	}

	/**
	 * 获取卖家视角补货订单状态
	 * @param systemReplementOrderStatus
	 * @return
	 */
	SellerReplenishmentOrderStatusEnum getSellerReplenishmentOrderStatus(String systemReplementOrderStatus){
		if(SystemReplenishmentOrderStatusEnum.BuyerRejectApplying.getType().equals(systemReplementOrderStatus))//买家已申请
			return SellerReplenishmentOrderStatusEnum.WaitingConfirmation;
		if(SystemReplenishmentOrderStatusEnum.SellerConfirmed.getType().equals(systemReplementOrderStatus))//卖家已确认
			return SellerReplenishmentOrderStatusEnum.WaitingDelivered;
		if(SystemReplenishmentOrderStatusEnum.SellerClosed.getType().equals(systemReplementOrderStatus))//卖家已关闭
			return SellerReplenishmentOrderStatusEnum.Closed;
		if(SystemReplenishmentOrderStatusEnum.SellerDelivered.getType().equals(systemReplementOrderStatus))//卖家已发货
			return SellerReplenishmentOrderStatusEnum.WaitingReceived;
		if(SystemReplenishmentOrderStatusEnum.BuyerReceived.getType().equals(systemReplementOrderStatus) || SystemReplenishmentOrderStatusEnum.SystemAutoConfirmReceipt.getType().equals(systemReplementOrderStatus))//买家已收货+系统自动确认收货
			return SellerReplenishmentOrderStatusEnum.Finished;
		return null;
	}

}