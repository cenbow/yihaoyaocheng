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
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.OrderDeliveryDetailDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.SystemOrderExceptionStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemRefundOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemReplenishmentOrderStatusEnum;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.order.mapper.OrderDetailMapper;
import com.yyw.yhyc.order.mapper.OrderReturnMapper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.bo.Pagination;

@Service("orderDeliveryDetailService")
public class OrderDeliveryDetailService {

	private OrderDeliveryDetailMapper	orderDeliveryDetailMapper;

	private OrderDetailMapper orderDetailMapper;

	private OrderReturnMapper orderReturnMapper;

	private SystemDateMapper systemDateMapper;

	private OrderMapper orderMapper;

	private OrderExceptionMapper orderExceptionMapper;

	private OrderTraceMapper orderTraceMapper;

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
	public  Map<String,String> confirmReceipt(List<OrderDeliveryDetailDto> list,UserDto user) throws Exception{

		Map<String, String> returnMap = new HashMap<String, String>();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		String returnType = "";
		String returnDesc = "";
		String flowId = "";
		String exceptionOrderId="";//异常订单号
		 String now=systemDateMapper.getSystemDate();//系统当前时间
		if (UtilHelper.isEmpty(list)||list.size()==0){
			returnMap.put("code","0");
			returnMap.put("msg","参数不能为空");
			return returnMap;
		}else{
			flowId=list.get(0).getFlowId();
			returnType=list.get(0).getReturnType();
			returnDesc=list.get(0).getReturnDesc();
		}

		//统计退款总金额
		BigDecimal moneyTotal=new BigDecimal(0);
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

			if (UtilHelper.isEmpty(dto.getFlowId())){
				returnMap.put("code","0");
				returnMap.put("msg","订单编号不能为空");
				return returnMap;
			}

			OrderDeliveryDetail orderDeliveryDetail = orderDeliveryDetailMapper.getByPK(dto.getOrderDeliveryDetailId());
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
					BigDecimal bigDecimal = new BigDecimal(orderReturn.getReturnCount());
					moneyTotal = moneyTotal.add(orderDetail.getProductPrice().multiply(bigDecimal));
					orderReturn.setReturnPay(orderDetail.getProductPrice().multiply(bigDecimal));
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
			if(orderDetail.getProductCount()!=orderDetail.getRecieveCount()){
				if(UtilHelper.isEmpty(returnType)||returnType.equals(""))
					 throw new Exception("采购商与收获数不同,拒收类型为空");
			}
		}

		//如果收货异常根据异常类型更新订单状态
		Order order = orderMapper.getOrderbyFlowId(flowId);
		if(!UtilHelper.isEmpty(returnType) &&!returnType.equals("")){
			if (returnType.equals("4"))
				order.setOrderStatus(SystemOrderStatusEnum.Rejecting.getType());
			else if (returnType.equals("3"))
				order.setOrderStatus(SystemOrderStatusEnum.Replenishing.getType());
		}else {
			order.setOrderStatus(SystemOrderStatusEnum.BuyerAllReceived.getType());
		}
		order.setReceiveTime(systemDateMapper.getSystemDate());
		order.setReceiveType(1);//买家确认收货
		order.setUpdateTime(systemDateMapper.getSystemDate());
		order.setUpdateUser(user.getUserName());
		orderMapper.update(order);

		//插入日志表
		OrderTrace orderTrace = new OrderTrace();
		orderTrace.setOrderId(order.getOrderId());
		orderTrace.setNodeName("买家确认收货");
		orderTrace.setDealStaff(user.getUserName());
		orderTrace.setRecordDate(now);
		orderTrace.setRecordStaff(user.getUserName());
		orderTrace.setOrderStatus(order.getOrderStatus());
		orderTrace.setCreateTime(now);
		orderTrace.setCreateUser(user.getUserName());
		orderTraceMapper.save(orderTrace);

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

			//插入异常订单日志
			OrderTrace orderTrace1 = new OrderTrace();
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
			orderTraceMapper.save(orderTrace1);

		}
			returnMap.put("code","1");
			returnMap.put("msg","操作成功");
			return returnMap;
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
}