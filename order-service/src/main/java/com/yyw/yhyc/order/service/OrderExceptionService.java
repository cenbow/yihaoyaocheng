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

import com.yyw.yhyc.order.bo.*;
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
	private OrderDeliveryDetailMapper orderDeliveryDetailMapper;

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

	@Autowired
	public void setOrderDeliveryDetailMapper(OrderDeliveryDetailMapper orderDeliveryDetailMapper) {
		this.orderDeliveryDetailMapper = orderDeliveryDetailMapper;
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
			throws Exception {
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
	public int findByCount(OrderException orderException) throws Exception {
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
		if(UtilHelper.isEmpty(orderExceptionDto)) {
			return orderExceptionDto;
		}
		orderExceptionDto.setBillTypeName(BillTypeEnum.getBillTypeName(orderExceptionDto.getBillType()));
		/* 计算商品总额 */
		if( !UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())){
			BigDecimal productPriceCount = new BigDecimal(0);
			for(OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()){
				if(UtilHelper.isEmpty(orderReturnDto) || UtilHelper.isEmpty(orderReturnDto.getReturnPay())) continue;
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
						orderStatusCountMap.put(buyerOrderExceptionStatusEnum.getType(), oed.getOrderCount());
					}
				}
			}
		}
		log.info("orderStatusCountMap:" + orderStatusCountMap);

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

		List<OrderExceptionDto> list = orderExceptionMapper.listPaginationSellerByProperty(pagination, orderExceptionDto);
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
		map.put("pagination", pagination);
		map.put("orderExceptionDto", orderExceptionDto);
		return map;
	}


	/**
	 * 审核拒收、退货订单详情（异常订单详情）
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public OrderExceptionDto getRejectOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception{
		orderExceptionDto = orderExceptionMapper.getOrderExceptionDetailsForReview(orderExceptionDto);
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
	 * 退货订单详情（异常订单详情）
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public OrderExceptionDto getReturnOrderDetails(OrderExceptionDto orderExceptionDto,Integer type) throws Exception{
		orderExceptionDto = orderExceptionMapper.getOrderExceptionDetailsForReturn(orderExceptionDto);
		if(!UtilHelper.isEmpty(orderExceptionDto) && !UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())){
			BigDecimal productPriceCount = new BigDecimal(0);
			for(OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()){
				if(UtilHelper.isEmpty(orderReturnDto)) continue;
				productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
			}
			orderExceptionDto.setProductPriceCount(productPriceCount);
			if(type == 1){ //买家视角
				orderExceptionDto.setOrderStatusName(getBuyerRefundOrderStatusEnum(orderExceptionDto.getOrderStatus(),orderExceptionDto.getPayType()).getValue());
			}else if(type==2){//卖家视角
				orderExceptionDto.setOrderStatusName(getSellerRefundOrderStatusEnum(orderExceptionDto.getOrderStatus(),orderExceptionDto.getPayType()).getValue());
			}
		}
		return orderExceptionDto;
	}

	/**
	 * 审核换货订单详情（异常订单详情）
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public OrderExceptionDto getChangeOrderDetails(OrderExceptionDto orderExceptionDto) throws Exception{
		orderExceptionDto = orderExceptionMapper.getOrderExceptionDetailsForReview(orderExceptionDto);
		if(!UtilHelper.isEmpty(orderExceptionDto) && !UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())){
			BigDecimal productPriceCount = new BigDecimal(0);
			for(OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()){
				if(UtilHelper.isEmpty(orderReturnDto)) continue;
				productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
			}
			orderExceptionDto.setProductPriceCount(productPriceCount);
			orderExceptionDto.setOrderStatusName(getSellerChangeGoodsOrderExceptionStatus(orderExceptionDto.getOrderStatus(),orderExceptionDto.getPayType()).getValue());
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
		orderTrace.setNodeName(SystemOrderExceptionStatusEnum.getName(oe.getOrderStatus()) + oe.getRemark());
		orderTrace.setDealStaff(userDto.getUserName());
		orderTrace.setRecordDate(now);
		orderTrace.setRecordStaff(userDto.getUserName());
		orderTrace.setOrderStatus(oe.getOrderStatus());
		orderTrace.setCreateTime(now);
		orderTrace.setCreateUser(userDto.getUserName());
		orderTraceMapper.save(orderTrace);
		//拒收订单卖家审核通过生成结算记录
		if(SystemOrderExceptionStatusEnum.BuyerConfirmed.getType().equals(orderException.getOrderStatus()))
		this.saveRefuseOrderSettlement(userDto.getCustId(), oe);

	}

	/**
	 * 卖家审核换货订单
	 * @param userDto
	 * @param orderException
	 */
	public void sellerReviewChangeOrder(UserDto userDto,OrderException orderException){
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(orderException) || UtilHelper.isEmpty(orderException.getExceptionId()))
			throw new RuntimeException("参数异常");

		// 验证审核状态
		if(!(SystemChangeGoodsOrderStatusEnum.WaitingBuyerDelivered.getType().equals(orderException.getOrderStatus())))
			throw new RuntimeException("参数异常");

		OrderException oe = orderExceptionMapper.getByPK(orderException.getExceptionId());
		if(UtilHelper.isEmpty(oe))
			throw new RuntimeException("未找到换货订单");
		if(userDto.getCustId() != oe.getSupplyId()){
			log.info("换货订单不属于该卖家,OrderException:"+oe+",UserDto:"+userDto);
			throw new RuntimeException("未找到换货订单");
		}
		//判断是否是拒收订单
		if(!"2".equals(oe.getReturnType())){
			log.info("换货订单不属于该卖家,OrderException:"+oe+",UserDto:"+userDto);
			throw new RuntimeException("该订单不是换货订单");
		}
		if(!SystemChangeGoodsOrderStatusEnum.WaitingConfirmation.getType().equals(oe.getOrderStatus())){
			log.info("换货订单状态不正确,OrderException:"+oe);
			throw new RuntimeException("换货订单状态不正确");
		}
		String now = systemDateMapper.getSystemDate();
		oe.setRemark(orderException.getRemark());
		oe.setOrderStatus(orderException.getOrderStatus());
		oe.setUpdateUser(userDto.getUserName());
		oe.setUpdateTime(now);
		int count = orderExceptionMapper.update(oe);
		if(count == 0){
			log.error("OrderException info :"+oe);
			throw new RuntimeException("换货订单审核失败");
		}
		//插入日志表
		OrderTrace orderTrace = new OrderTrace();
		orderTrace.setOrderId(oe.getExceptionId());
		orderTrace.setNodeName(SystemChangeGoodsOrderStatusEnum.getName(oe.getOrderStatus()) + oe.getRemark());
		orderTrace.setDealStaff(userDto.getUserName());
		orderTrace.setRecordDate(now);
		orderTrace.setRecordStaff(userDto.getUserName());
		orderTrace.setOrderStatus(oe.getOrderStatus());
		orderTrace.setCreateTime(now);
		orderTrace.setCreateUser(userDto.getUserName());
		orderTraceMapper.save(orderTrace);

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
						orderStatusCountMap.put(buyerOrderExceptionStatusEnum.getType(), oed.getOrderCount());
					}
				}
			}
		}
		log.info("orderStatusCountMap:" + orderStatusCountMap);
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
	/**
	 * 卖家审核退货订单
	 * @param userDto
	 * @param orderException
	 */
	public void modifyReviewReturnOrder(UserDto userDto,OrderException orderException){
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(orderException) || UtilHelper.isEmpty(orderException.getExceptionId()))
			throw new RuntimeException("参数异常");

		// 验证审核状态
		if(!(SystemRefundOrderStatusEnum.SellerConfirmed.getType().equals(orderException.getOrderStatus()) || SystemRefundOrderStatusEnum.SellerClosed.getType().equals(orderException.getOrderStatus())))
			throw new RuntimeException("参数异常");

		OrderException oe = orderExceptionMapper.getByPK(orderException.getExceptionId());
		if(UtilHelper.isEmpty(oe))
			throw new RuntimeException("未找到退货订单");
		if(userDto.getCustId() != oe.getSupplyId()){
			log.info("退货订单不属于该卖家,OrderException:"+oe+",UserDto:"+userDto);
			throw new RuntimeException("未找到退货订单");
		}
		//判断是否是退货订单
		if(!"1".equals(oe.getReturnType())){
			log.info("退货订单不属于该卖家,OrderException:"+oe+",UserDto:"+userDto);
			throw new RuntimeException("该订单不是退货订单");
		}
		if(!SystemRefundOrderStatusEnum.BuyerApplying.getType().equals(oe.getOrderStatus())){
			log.info("退货订单状态不正确,OrderException:"+oe);
			throw new RuntimeException("退货订单状态不正确");
		}
		String now = systemDateMapper.getSystemDate();
		oe.setRemark(orderException.getRemark());
		oe.setOrderStatus(orderException.getOrderStatus());
		oe.setUpdateUser(userDto.getUserName());
		oe.setUpdateTime(now);
		int count = orderExceptionMapper.update(oe);
		if(count == 0){
			log.error("OrderException info :"+oe);
			throw new RuntimeException("退货订单审核失败");
		}

		//插入日志表
		OrderTrace orderTrace = new OrderTrace();
		orderTrace.setOrderId(oe.getExceptionId());
		orderTrace.setNodeName(SystemRefundOrderStatusEnum.getName(oe.getOrderStatus())+oe.getRemark());
		orderTrace.setDealStaff(userDto.getUserName());
		orderTrace.setRecordDate(now);
		orderTrace.setRecordStaff(userDto.getUserName());
		orderTrace.setOrderStatus(oe.getOrderStatus());
		orderTrace.setCreateTime(now);
		orderTrace.setCreateUser(userDto.getUserName());
		orderTraceMapper.save(orderTrace);
	}
	/**
	 * 修改状态
	 * @param orderException
	 * @return
	 */
	public int updateOrderStatus(OrderException orderException){
		return  orderExceptionMapper.updateOrderStatus(orderException);
	}
	/**
	 * 供应商补货订单管理-分页查询
	 * @param pagination
	 * @param orderExceptionDto
     * @return
     */
	public Map<String, Object> listPgSellerReplenishmentOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) {
		Map<String,Object> resultMap = new HashMap<String,Object>();

		/* 非法参数过滤 */
		if(UtilHelper.isEmpty(pagination) || UtilHelper.isEmpty(orderExceptionDto)) throw new RuntimeException("参数错误");
		log.info("request orderExceptionDto :"+orderExceptionDto.toString());

		/* 转换日期查询条件 */
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

		/* 查询供应商补货订单总金额 */
		BigDecimal orderTotalMoney = orderExceptionMapper.findSellerReplenishmentOrderTotal(orderExceptionDto);
		log.info("orderTotalMoney:" + orderTotalMoney);

		/* 供应商补货订单查询 */
		List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPaginationSellerReplenishmentOrder(pagination, orderExceptionDto);
		log.info("orderExceptionDtoList:"+orderExceptionDtoList);

		/* 根据供应商的视角 获取补货订单状态名称 */
		int orderCount = 0;
		SellerReplenishmentOrderStatusEnum sellerReplenishmentOrderStatusEnum;
		if (!UtilHelper.isEmpty(orderExceptionDtoList)) {
			orderCount = pagination.getTotal();
			for(OrderExceptionDto oed : orderExceptionDtoList) {
				sellerReplenishmentOrderStatusEnum = getSellerReplenishmentOrderStatus(oed.getOrderStatus());
				if (!UtilHelper.isEmpty(sellerReplenishmentOrderStatusEnum)) {
					oed.setOrderStatusName(sellerReplenishmentOrderStatusEnum.getValue());
				} else {
					oed.setOrderStatusName("未知状态");
				}
			}
		}
		pagination.setResultList(orderExceptionDtoList);

		/* 补货订单状态统计 */
		Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();
		List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findSellerReplenishmentStatusCount(orderExceptionDto);;

		if (!UtilHelper.isEmpty(orderExceptionDtos)) {
			for (OrderExceptionDto oed : orderExceptionDtos) {
				sellerReplenishmentOrderStatusEnum = getSellerReplenishmentOrderStatus(oed.getOrderStatus());
				if(sellerReplenishmentOrderStatusEnum != null){
					if(orderStatusCountMap.containsKey(sellerReplenishmentOrderStatusEnum.getType())){
						orderStatusCountMap.put(sellerReplenishmentOrderStatusEnum.getType(),orderStatusCountMap.get(sellerReplenishmentOrderStatusEnum.getType())+oed.getOrderCount());
					}else{
						orderStatusCountMap.put(sellerReplenishmentOrderStatusEnum.getType(),oed.getOrderCount());
					}
				}
			}
		}
		log.info("orderStatusCountMap:" + orderStatusCountMap);

		/* 把需要响应到页面的数据塞入 map 中 */
		resultMap.put("orderStatusCount", orderStatusCountMap);
		resultMap.put("orderList", pagination);
		resultMap.put("orderCount", orderCount);
		resultMap.put("orderTotalMoney", orderTotalMoney == null ? 0 : orderTotalMoney);
		return resultMap;
	}

	/**
	 * 供应商换货订单查询
	 * @param pagination
	 * @param orderExceptionDto
	 * @return
	 */
	public Map<String, Object> listPgSellerChangeGoodsOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto){
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
		BigDecimal orderTotalMoney = orderExceptionMapper.findSellerChangeGoodsExceptionOrderTotal(orderExceptionDto);
		log.info("orderTotalMoney:"+orderTotalMoney);

		List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPgSellerChangeGoodsOrder(pagination, orderExceptionDto);
		log.info("orderExceptionDtoList:"+orderExceptionDtoList);

		SellerChangeGoodsOrderStatusEnum sellerOrderExceptionStatusEnum;
		if(!UtilHelper.isEmpty(orderExceptionDtoList)){
			orderCount = pagination.getTotal();
			for(OrderExceptionDto oed : orderExceptionDtoList){
				sellerOrderExceptionStatusEnum = getSellerChangeGoodsOrderExceptionStatus(oed.getOrderStatus(),oed.getPayType());
				if(!UtilHelper.isEmpty(sellerOrderExceptionStatusEnum))
					oed.setOrderStatusName(sellerOrderExceptionStatusEnum.getValue());
				else
					oed.setOrderStatusName("未知状态");
			}
		}
		pagination.setResultList(orderExceptionDtoList);
		Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
		List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findSellerChangeGoodsOrderStatusCount(orderExceptionDto);;

		if (!UtilHelper.isEmpty(orderExceptionDtos)) {
			for (OrderExceptionDto oed : orderExceptionDtos) {
				//卖家视角订单状态数量
				sellerOrderExceptionStatusEnum = getSellerChangeGoodsOrderExceptionStatus(oed.getOrderStatus(),oed.getPayType());
				if(sellerOrderExceptionStatusEnum != null){
					if(orderStatusCountMap.containsKey(sellerOrderExceptionStatusEnum.getType())){
						orderStatusCountMap.put(sellerOrderExceptionStatusEnum.getType(),orderStatusCountMap.get(sellerOrderExceptionStatusEnum.getType())+oed.getOrderCount());
					}else{
						orderStatusCountMap.put(sellerOrderExceptionStatusEnum.getType(),oed.getOrderCount());
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
	 * 获取供应商视角换货异常订单状态
	 * @param systemExceptionOrderStatus
	 * @param payType
	 * @return
	 */
	SellerChangeGoodsOrderStatusEnum getSellerChangeGoodsOrderExceptionStatus(String systemExceptionOrderStatus,int payType){
		SellerChangeGoodsOrderStatusEnum sellerOrderExceptionStatusEnum = null;
		if(SystemChangeGoodsOrderStatusEnum.WaitingConfirmation.getType().equals(systemExceptionOrderStatus)){//申请中
			sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.WaitingConfirmation;//待确认
		}
		if(SystemChangeGoodsOrderStatusEnum.Canceled.getType().equals(systemExceptionOrderStatus)){//已取消
			sellerOrderExceptionStatusEnum=SellerChangeGoodsOrderStatusEnum.Canceled;
		}
		if(SystemChangeGoodsOrderStatusEnum.Closed.getType().equals(systemExceptionOrderStatus)){//卖家已关闭
			sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.Closed;//已关闭
		}
		if(SystemChangeGoodsOrderStatusEnum.WaitingBuyerDelivered.getType().equals(systemExceptionOrderStatus)){//待买家发货
			sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.WaitingBuyerDelivered;//待买家发货
		}
		if(SystemChangeGoodsOrderStatusEnum.WaitingSellerReceived.getType().equals(systemExceptionOrderStatus)){//买家已发货
			sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.WaitingSellerReceived;//
		}

		if(SystemChangeGoodsOrderStatusEnum.WaitingSellerDelivered.getType().equals(systemExceptionOrderStatus)){//卖家已收货
			sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.WaitingSellerDelivered;//
		}

		if(SystemChangeGoodsOrderStatusEnum.WaitingBuyerReceived.getType().equals(systemExceptionOrderStatus)){//卖家已发货
			sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.WaitingBuyerReceived;//
		}

		if(SystemChangeGoodsOrderStatusEnum.Finished.getType().equals(systemExceptionOrderStatus)){//买家已收货
			sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.Finished;//
		}

		if(SystemChangeGoodsOrderStatusEnum.AutoFinished.getType().equals(systemExceptionOrderStatus)){//买家自动收货
			sellerOrderExceptionStatusEnum = SellerChangeGoodsOrderStatusEnum.Finished;//
		}

		return sellerOrderExceptionStatusEnum;
	}

	/**
	 * 买家视角退货订单状态
	 * @param systemStatus
	 * @param payType
     * @return
     */
	BuyerRefundOrderStatusEnum getBuyerRefundOrderStatusEnum(String systemStatus,int payType){
		if(SystemRefundOrderStatusEnum.BuyerApplying.getType().equals(systemStatus))
			return BuyerRefundOrderStatusEnum.BuyerApplying;
		if(SystemRefundOrderStatusEnum.BuyerCanceled.getType().equals(systemStatus))
			return BuyerRefundOrderStatusEnum.Canceled;
		if(SystemRefundOrderStatusEnum.SellerConfirmed.getType().equals(systemStatus))
			return BuyerRefundOrderStatusEnum.WaitingBuyerDelivered;
		if(SystemRefundOrderStatusEnum.SellerClosed.getType().equals(systemStatus))
			return BuyerRefundOrderStatusEnum.Closed;
		if(SystemRefundOrderStatusEnum.BuyerDelivered.getType().equals(systemStatus))
			return BuyerRefundOrderStatusEnum.WaitingSellerReceived;
		if(SystemRefundOrderStatusEnum.SellerReceived.getType().equals(systemStatus)||SystemRefundOrderStatusEnum.SystemAutoConfirmReceipt.getType().equals(systemStatus)){
			if(payType == SystemPayTypeEnum.PayOffline.getPayType() || payType == SystemPayTypeEnum.PayOnline.getPayType())
				return BuyerRefundOrderStatusEnum.refunding;
			if(payType == SystemPayTypeEnum.PayPeriodTerm.getPayType())
				return BuyerRefundOrderStatusEnum.Finished;
		}
		if(SystemRefundOrderStatusEnum.Refunded.getType().equals(systemStatus))
			return BuyerRefundOrderStatusEnum.Finished;
		return null;
	}

	/**
	 * 卖家视角退货订单状态
	 * @param systemStatus
	 * @param payType
	 * @return
	 */
	SellerRefundOrderStatusEnum getSellerRefundOrderStatusEnum(String systemStatus,int payType){
		if(SystemRefundOrderStatusEnum.BuyerApplying.getType().equals(systemStatus))
			return SellerRefundOrderStatusEnum.BuyerApplying;
		if(SystemRefundOrderStatusEnum.BuyerCanceled.getType().equals(systemStatus))
			return SellerRefundOrderStatusEnum.Canceled;
		if(SystemRefundOrderStatusEnum.SellerConfirmed.getType().equals(systemStatus))
			return SellerRefundOrderStatusEnum.WaitingBuyerDelivered;
		if(SystemRefundOrderStatusEnum.SellerClosed.getType().equals(systemStatus))
			return SellerRefundOrderStatusEnum.Closed;
		if(SystemRefundOrderStatusEnum.BuyerDelivered.getType().equals(systemStatus))
			return SellerRefundOrderStatusEnum.WaitingSellerReceived;
		if(SystemRefundOrderStatusEnum.SellerReceived.getType().equals(systemStatus)||SystemRefundOrderStatusEnum.SystemAutoConfirmReceipt.getType().equals(systemStatus)){
			if(payType == SystemPayTypeEnum.PayOffline.getPayType() || payType == SystemPayTypeEnum.PayOnline.getPayType())
				return SellerRefundOrderStatusEnum.refunding;
			if(payType == SystemPayTypeEnum.PayPeriodTerm.getPayType())
				return SellerRefundOrderStatusEnum.Finished;
		}
		if(SystemRefundOrderStatusEnum.Refunded.getType().equals(systemStatus))
			return SellerRefundOrderStatusEnum.Finished;
		return null;
	}

	/**
	 * 采购商退货订单查询
	 * @param pagination
	 * @param orderExceptionDto
	 * @return
	 */
	public Map<String, Object> listPgBuyerRefundOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto){
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
		BigDecimal orderTotalMoney = orderExceptionMapper.findBuyerRefundOrderTotal(orderExceptionDto);

		log.info("orderTotalMoney:"+orderTotalMoney);

		List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPaginationBuyerRefundOrder(pagination, orderExceptionDto);
		log.info("orderExceptionDtoList:"+orderExceptionDtoList);


		BuyerRefundOrderStatusEnum buyerRefundOrderStatusEnum;
		if(!UtilHelper.isEmpty(orderExceptionDtoList)){
			orderCount = pagination.getTotal();
			for(OrderExceptionDto oed : orderExceptionDtoList){
				buyerRefundOrderStatusEnum = getBuyerRefundOrderStatusEnum(oed.getOrderStatus(),oed.getPayType());
				if(!UtilHelper.isEmpty(buyerRefundOrderStatusEnum))
					oed.setOrderStatusName(buyerRefundOrderStatusEnum.getValue());
				else
					oed.setOrderStatusName("未知状态");
			}
		}

		pagination.setResultList(orderExceptionDtoList);

		Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
		List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findBuyerRefundOrderStatusCount(orderExceptionDto);

		if (!UtilHelper.isEmpty(orderExceptionDtos)) {
			for (OrderExceptionDto oed : orderExceptionDtos) {
				//卖家视角订单状态
				buyerRefundOrderStatusEnum = getBuyerRefundOrderStatusEnum(oed.getOrderStatus(),oed.getPayType());
				if(buyerRefundOrderStatusEnum != null){
					if(orderStatusCountMap.containsKey(buyerRefundOrderStatusEnum.getType())){
						orderStatusCountMap.put(buyerRefundOrderStatusEnum.getType(),orderStatusCountMap.get(buyerRefundOrderStatusEnum.getType())+oed.getOrderCount());
					}else{
						orderStatusCountMap.put(buyerRefundOrderStatusEnum.getType(),oed.getOrderCount());
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
	 * 买家取消退货订单
	 * @param userDto
	 * @param exceptionId
     */
	public void updateRefundOrderStatusForBuyer(UserDto userDto,Integer exceptionId){
		if(UtilHelper.isEmpty(userDto.getCustId()) || UtilHelper.isEmpty(exceptionId)){
			throw new RuntimeException("参数错误");
		}
		OrderException orderException =  orderExceptionMapper.getByPK(exceptionId);
		log.info(orderException);
		if(UtilHelper.isEmpty(orderException)){
			log.info("can not find order ,exceptionId:"+exceptionId);
			throw new RuntimeException("未找到订单");
		}
		//判断订单是否属于该买家
		if(userDto.getCustId() == orderException.getCustId()){
			if(SystemRefundOrderStatusEnum.BuyerApplying.getType().equals(orderException.getOrderStatus())){//买家已申请
				orderException.setOrderStatus(SystemRefundOrderStatusEnum.BuyerCanceled.getType());//标记订单为用户取消状态
				String now = systemDateMapper.getSystemDate();
				orderException.setUpdateUser(userDto.getUserName());
				orderException.setUpdateTime(now);
				int count = orderExceptionMapper.update(orderException);
				if(count == 0){
					log.info("orderException info :"+orderException);
					throw new RuntimeException("订单取消失败");
				}
				//插入日志表
				OrderTrace orderTrace = new OrderTrace();
				orderTrace.setOrderId(orderException.getExceptionId());
				orderTrace.setNodeName("买家取消退货订单");
				orderTrace.setDealStaff(userDto.getUserName());
				orderTrace.setRecordDate(now);
				orderTrace.setRecordStaff(userDto.getUserName());
				orderTrace.setOrderStatus(orderException.getOrderStatus());
				orderTrace.setCreateTime(now);
				orderTrace.setCreateUser(userDto.getUserName());
				orderTraceMapper.save(orderTrace);
			}else{
				log.info("orderException status error ,orderStatus:"+orderException.getOrderStatus());
				throw new RuntimeException("订单状态不正确");
			}
		}else{
			log.info("db orderException not equals to request exceptionId ,exceptionId:"+exceptionId+",db exceptionId:"+orderException.getExceptionId());
			throw new RuntimeException("未找到订单");
		}
	}

	/**
	 * 补货订单订单详情
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public OrderExceptionDto getReplenishmentDetails(OrderExceptionDto orderExceptionDto) throws Exception{
		Integer userType = orderExceptionDto.getUserType();
		orderExceptionDto = orderExceptionMapper.getReplenishmentDetails(orderExceptionDto);
		if(UtilHelper.isEmpty(orderExceptionDto)) {
			return orderExceptionDto;
		}
		if(userType==2){  //供应商的时候取发货信息
			OrderDeliveryDetail orderDeliveryDetail = new OrderDeliveryDetail();
			orderDeliveryDetail.setFlowId(orderExceptionDto.getExceptionOrderId());
			List<OrderDeliveryDetail> list=orderDeliveryDetailMapper.listByProperty(orderDeliveryDetail);
			if(list.size()>0){
				orderDeliveryDetail=(OrderDeliveryDetail)list.get(0);
				if(orderDeliveryDetail.getDeliveryStatus()==0){
					orderExceptionDto.setImportStatusName("导入失败");
					orderExceptionDto.setFileName("下载失败原因");
				}else{
					orderExceptionDto.setImportStatusName("导入成功");
					orderExceptionDto.setFileName("下载批号列表");
				}
				orderExceptionDto.setImportFileUrl(orderDeliveryDetail.getImportFileUrl());
			}
		}
		orderExceptionDto.setBillTypeName(BillTypeEnum.getBillTypeName(orderExceptionDto.getBillType()));
		/* 计算商品总额 */
		if( !UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())){
			BigDecimal productPriceCount = new BigDecimal(0);
			for(OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()){
				if(UtilHelper.isEmpty(orderReturnDto) || UtilHelper.isEmpty(orderReturnDto.getReturnPay()))
					continue;
				productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
			}
			orderExceptionDto.setProductPriceCount(productPriceCount);
		}

		return orderExceptionDto;
	}



	/**
	 * 卖家退货订单查询
	 * @param pagination
	 * @param orderExceptionDto
	 * @return
	 */
	public Map<String, Object> listPgSellerRefundOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto){
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
		BigDecimal orderTotalMoney = orderExceptionMapper.findSellerRefundOrderTotal(orderExceptionDto);

		log.info("orderTotalMoney:"+orderTotalMoney);

		List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPaginationSellerRefundOrder(pagination, orderExceptionDto);
		log.info("orderExceptionDtoList:"+orderExceptionDtoList);


		BuyerRefundOrderStatusEnum buyerRefundOrderStatusEnum;
		if(!UtilHelper.isEmpty(orderExceptionDtoList)){
			orderCount = pagination.getTotal();
			for(OrderExceptionDto oed : orderExceptionDtoList){
				buyerRefundOrderStatusEnum = getBuyerRefundOrderStatusEnum(oed.getOrderStatus(),oed.getPayType());
				if(!UtilHelper.isEmpty(buyerRefundOrderStatusEnum))
					oed.setOrderStatusName(buyerRefundOrderStatusEnum.getValue());
				else
					oed.setOrderStatusName("未知状态");
			}
		}

		pagination.setResultList(orderExceptionDtoList);

		Map<String, Integer> orderStatusCountMap = new HashMap<String, Integer>();//订单状态统计
		List<OrderExceptionDto> orderExceptionDtos = orderExceptionMapper.findSellerRefundOrderStatusCount(orderExceptionDto);

		if (!UtilHelper.isEmpty(orderExceptionDtos)) {
			for (OrderExceptionDto oed : orderExceptionDtos) {
				//卖家视角订单状态
				buyerRefundOrderStatusEnum = getBuyerRefundOrderStatusEnum(oed.getOrderStatus(),oed.getPayType());
				if(buyerRefundOrderStatusEnum != null){
					if(orderStatusCountMap.containsKey(buyerRefundOrderStatusEnum.getType())){
						orderStatusCountMap.put(buyerRefundOrderStatusEnum.getType(),orderStatusCountMap.get(buyerRefundOrderStatusEnum.getType())+oed.getOrderCount());
					}else{
						orderStatusCountMap.put(buyerRefundOrderStatusEnum.getType(),oed.getOrderCount());
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

	public void repConfirmReceipt(String exceptionOrderId,UserDto userDto) {

		OrderException orderException = orderExceptionMapper.getByExceptionOrderId(exceptionOrderId);
		if (UtilHelper.isEmpty(orderException)) {
			log.info("订单不存在，编号为：" + exceptionOrderId);
			throw new RuntimeException("未找到订单");
		}
		if (userDto.getCustId() == orderException.getCustId()) {
			if (SystemReplenishmentOrderStatusEnum.BuyerRejectApplying.getType().equals(orderException.getOrderStatus())) {//买家已申请
				orderException.setOrderStatus(SystemReplenishmentOrderStatusEnum.BuyerReceived.getType());//买家已收货
				String now = systemDateMapper.getSystemDate();
				orderException.setUpdateUser(userDto.getUserName());
				orderException.setUpdateTime(now);
				int count = orderExceptionMapper.update(orderException);
				if (count == 0) {
					log.info("orderException info :" + orderException);
					throw new RuntimeException("订单收货失败");
				}
				//插入日志表
				OrderTrace orderTrace = new OrderTrace();
				orderTrace.setOrderId(orderException.getExceptionId());
				orderTrace.setNodeName("拒收订单收货");
				orderTrace.setDealStaff(userDto.getUserName());
				orderTrace.setRecordDate(now);
				orderTrace.setRecordStaff(userDto.getUserName());
				orderTrace.setOrderStatus(orderException.getOrderStatus());
				orderTrace.setCreateTime(now);
				orderTrace.setCreateUser(userDto.getUserName());
				orderTraceMapper.save(orderTrace);
			} else {
				log.info("订单状态不正确:" + orderException.getOrderStatus());
				throw new RuntimeException("订单状态不正确");
			}

		} else {
			log.info("订单不存在");
			throw new RuntimeException("未找到订单");
		}
	}

}