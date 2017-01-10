/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:49
 * <p/>
 * Amendment History:
 * <p/>
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 **/
package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.order.dto.OrderLogDto;
import com.yyw.yhyc.order.dto.OrderSettlementDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OrderExceptionTypeEnum;
import com.yyw.yhyc.order.enmu.SystemOrderExceptionStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.manage.OrderPayManage;
import com.yyw.yhyc.order.mapper.OrderExceptionMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;
import com.yyw.yhyc.usermanage.mapper.UsermanageEnterpriseMapper;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderSettlementMapper;

@Service("orderSettlementService")
public class OrderSettlementService {

	private static final Logger log = LoggerFactory.getLogger(OrderSettlementService.class);

	private OrderSettlementMapper orderSettlementMapper;

	private SystemDateMapper systemDateMapper;

	@Autowired
	private OrderExceptionMapper orderExceptionMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private UsermanageEnterpriseMapper usermanageEnterpriseMapper;
	@Autowired
	private OrderTraceService orderTraceService;

	@Autowired
	public void setOrderSettlementMapper(OrderSettlementMapper orderSettlementMapper) {
		this.orderSettlementMapper = orderSettlementMapper;
	}

	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}

	/**
	 * 通过主键查询实体对象
	 *
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderSettlement getByPK(java.lang.Integer primaryKey) throws Exception {
		return orderSettlementMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 *
	 * @return
	 * @throws Exception
	 */
	public List<OrderSettlement> list() throws Exception {
		return orderSettlementMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 *
	 * @return
	 * @throws Exception
	 */
	public List<OrderSettlement> listByProperty(OrderSettlement orderSettlement) throws Exception {
		return orderSettlementMapper.listByProperty(orderSettlement);
	}

	/**
	 * 根据查询条件查询分页记录
	 *
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderSettlementDto> listPaginationByProperty(Pagination<OrderSettlementDto> pagination,
																   OrderSettlementDto orderSettlementDto) throws Exception {
		List<OrderSettlementDto> list = orderSettlementMapper.listPaginationDtoByProperty(pagination,
				orderSettlementDto);
		if (!UtilHelper.isEmpty(list)) {
			for (OrderSettlementDto osd : list) {
				if (orderSettlementDto.getType() == 1) {// type = 1 卖家
					if (osd.getBusinessType() == 1) {
						osd.setBusinessTypeName("销售货款");
					} else if (osd.getBusinessType() == 2) {
						osd.setBusinessTypeName("退款货款");
					} else if (osd.getBusinessType() == 3) {
						osd.setBusinessTypeName("拒收退款");
					} else if(osd.getBusinessType() == 4){
						osd.setBusinessTypeName("取消订单退款");
					}else if(osd.getBusinessType() == 5){
						osd.setBusinessTypeName("取消发货退款");
					}else{
						osd.setBusinessTypeName("未知状态");
					}
				} else {// type =2 买家
					if (osd.getBusinessType() == 1) {
						osd.setBusinessTypeName("采购货款");
					} else if (osd.getBusinessType() == 2) {
						osd.setBusinessTypeName("退款货款");
					} else if (osd.getBusinessType() == 3) {
						osd.setBusinessTypeName("拒收退款");
					} else if (osd.getBusinessType() == 4){
						osd.setBusinessTypeName("取消订单退款");
					}else if(osd.getBusinessType() == 5){
						osd.setBusinessTypeName("取消发货退款");
					}else{
						osd.setBusinessTypeName("未知状态");
					}
				}

				if (OrderSettlement.confirm_settlement_done.equals(osd.getConfirmSettlement())) {
					osd.setConfirmSettlementName("已结算");
				} else if (OrderSettlement.confirm_settlement_doing.equals(osd.getConfirmSettlement())) {
					osd.setConfirmSettlementName("结算中");
				} else {
					osd.setConfirmSettlementName("未结算");
				}

				// 目前只有 采购业务，且线上支付、账期支付，才有支付流水号。退货、拒收、取消订单业务都没有支付流水号
				if (osd.getSettleFlowId() == null) {
					osd.setSettleFlowId("");
				}

			}
		}
		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 *
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception {
		return orderSettlementMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 *
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception {
		orderSettlementMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 *
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderSettlement orderSettlement) throws Exception {
		return orderSettlementMapper.deleteByProperty(orderSettlement);
	}

	/**
	 * 保存记录
	 *
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public void save(OrderSettlement orderSettlement) throws Exception {
		orderSettlementMapper.save(orderSettlement);
	}

	/**
	 * 更新记录
	 *
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public int update(OrderSettlement orderSettlement) throws Exception {
		return orderSettlementMapper.update(orderSettlement);
	}

	/**
	 * 根据条件查询记录条数
	 *
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderSettlement orderSettlement) throws Exception {
		return orderSettlementMapper.findByCount(orderSettlement);
	}

	/**
	 * 退款结算
	 *
	 * @param orderSettlement
	 */
	public void updateRefundSettlement(OrderSettlement orderSettlement) {
		if (UtilHelper.isEmpty(orderSettlement) || UtilHelper.isEmpty(orderSettlement.getRefunSettlementMoney())
				|| UtilHelper.isEmpty(orderSettlement.getOrderSettlementId())) {
			throw new RuntimeException("参数错误");
		}

		OrderSettlement os = orderSettlementMapper.getByPK(orderSettlement.getOrderSettlementId());

		if (UtilHelper.isEmpty(os))
			throw new RuntimeException("未找到结算订单");

		if (os.getSupplyId().intValue() != orderSettlement.getSupplyId().intValue())
			throw new RuntimeException("未找到结算订单");

		if (os.getBusinessType() == 1)
			throw new RuntimeException("结算订单类型不正确");

		if (OrderSettlement.confirm_settlement_done.equals(os.getConfirmSettlement()))
			throw new RuntimeException("订单已结算");

		String nowTime = systemDateMapper.getSystemDate();
		os.setRefunSettlementMoney(orderSettlement.getRefunSettlementMoney());
		os.setUpdateTime(nowTime);
		os.setUpdateUser(orderSettlement.getUpdateUser());
		os.setSettlementTime(nowTime);
		os.setRemark(orderSettlement.getRemark());
		os.setConfirmSettlement(OrderSettlement.confirm_settlement_done);

		// 修改异常订单里的结算状态
		OrderException orderException = new OrderException();
		orderException.setExceptionOrderId(os.getFlowId());
		List<OrderException> lo = orderExceptionMapper.listByProperty(orderException);
		if (!UtilHelper.isEmpty(lo)) {
			orderException = lo.get(0);
			if ("1".equals(orderException.getReturnType())) {
				orderException.setOrderStatus("8");
			} else if ("4".equals(orderException.getReturnType())) {
				orderException.setOrderStatus("4");
			}
			orderExceptionMapper.update(orderException);
			// 拒收 异常订单结算 原订单状态变成部分收货
			if ("4".equals(orderException.getReturnType())) {
				Order order = orderMapper.getOrderbyFlowId(orderException.getFlowId());
				order.setOrderStatus(SystemOrderStatusEnum.BuyerPartReceived.getType());
				order.setUpdateTime(nowTime);
				orderMapper.update(order);
			}
		}
		int result = orderSettlementMapper.update(os);
		if (result == 0)
			throw new RuntimeException("结算失败");

		//插入日志
		OrderLogDto orderLogDto=new OrderLogDto();
		orderLogDto.setOrderId(orderException.getOrderId());
		orderLogDto.setNodeName("退款结算==flowID" + orderException.getExceptionOrderId());
		orderLogDto.setOrderStatus(orderException.getOrderStatus());
		orderLogDto.setRemark("请求参数orderSettlement==["+orderSettlement.toString()+"]");
		this.orderTraceService.saveOrderLog(orderLogDto);

	}

	/**
	 * 正常订单 just for 银联支付
	 *
	 * @param type
	 *            业务类型 1 在线支付 买家已付款 (进入应付) 2 买家全部收货或者买家部分收货或者系统自动确认收货时(进入应收) 3
	 *            拒收订单状态为卖家已确认 (进入应付) 4 退货订单状态为卖家已收货或系统自动确认收货时(进入应收) 5
	 *            卖家取消、运营后台取消、过期未发货自动取消(进入应付)
	 * @param orderSettlement
	 * @return
	 */
	public OrderSettlement parseOnlineSettlement(Integer type, Integer custId, Integer supplyId, String createUser,
												 OrderSettlement orderSettlement, Order order) {
		if (orderSettlement == null)
			orderSettlement = new OrderSettlement();
		parseSettlementProvince(orderSettlement, order.getCustId() + "");
		String now = systemDateMapper.getSystemDate();
		// 默认的结算属性
		orderSettlement.setOrderId(order.getOrderId());
		orderSettlement.setFlowId(order.getFlowId());
		orderSettlement.setCustName(order.getCustName());
		orderSettlement.setSupplyName(order.getSupplyName());
		orderSettlement.setCreateTime(now);
		orderSettlement.setOrderTime(order.getCreateTime());
		orderSettlement.setSettlementTime(now);
		orderSettlement.setPayTypeId(order.getPayTypeId());
		orderSettlement.setCreateUser(createUser);

		custId = custId == null ? order.getCustId() : custId;
		supplyId = supplyId == null ? order.getSupplyId() : supplyId;

		switch (type) {
			case 1:
				// 生成买家结算
				orderSettlement.setBusinessType(1);
				orderSettlement.setCustId(custId);
				orderSettlement.setConfirmSettlement("1");// 生成结算信息时都是已结算
				orderSettlement.setCreateUser(order.getCustName());
				orderSettlement.setSettlementMoney(order.getOrgTotal());
				break;
			case 2:
				// 包装卖家结算信息;
				orderSettlement.setBusinessType(1);
				orderSettlement.setCustId(null);
				orderSettlement.setSupplyId(supplyId);
				orderSettlement.setConfirmSettlement("0");
				orderSettlement.setSettlementMoney(order.getOrgTotal());
				break;
			case 3:
				orderSettlement.setCustId(custId);
				orderSettlement.setSupplyId(null);
				orderSettlement.setConfirmSettlement("0");
				break;
			case 4:
				// 退款 暂时不做调整
				break;
			case 5:
				orderSettlement.setBusinessType(4);
				orderSettlement.setCustId(order.getCustId());
				orderSettlement.setConfirmSettlement("0");// 生成结算信息时都未结算

				orderSettlement.setSettlementMoney(order.getOrgTotal());
				break;
			case 6:
				orderSettlement.setBusinessType(1);// 拒收退款
				orderSettlement.setCustId(custId);
				orderSettlement.setSupplyId(supplyId);
				orderSettlement.setConfirmSettlement("0");// 生成结算信息时都是未结算

				orderSettlement.setSettlementMoney(order.getOrgTotal());
				break;
			default:
				break;
		}
		return orderSettlement;
	}

	/**
	 * for all orderSettlement 设置省市区代码
	 *
	 * @param orderSettlement
	 * @param custId
	 *            采购商id
	 */
	public void parseSettlementProvince(OrderSettlement orderSettlement, String custId) {
		UsermanageEnterprise ue = usermanageEnterpriseMapper.getByEnterpriseId(custId);
		if (ue != null) {// 不为空，设置省市区代码
			orderSettlement.setProvince(ue.getProvince());
			orderSettlement.setCity(ue.getCity());
			orderSettlement.setArea(ue.getDistrict());
		}
	}

	/**
	 * just for 在线-招行支付 退货退款成功回调 flowId order 的flowId 或者是 exceptionOrder 的
	 * exceptionOrderId type 1 销售货款 2 退货货款 3 拒收货款 4 取消订单退款 settleFlowId 结算流水号
	 */
	public void updateSettlementByMap(String flowId, Integer type, String settleFlowId,Integer supplyId,BigDecimal payToMoney) {
		log.info("同步回调->更新结算信息->订单:" + flowId + ";业务类型:" + type+";供应商或采购商ID:" +supplyId+";结算流水号："+settleFlowId);
		OrderSettlement condition=new OrderSettlement();
		condition.setFlowId(flowId);
		condition.setBusinessType(type);
		if(!UtilHelper.isEmpty(flowId)&&!UtilHelper.isEmpty(type)
				&&!UtilHelper.isEmpty(settleFlowId)&&!UtilHelper.isEmpty(supplyId)
				&&(type.intValue()==4||type.intValue()==3)){
			condition.setCustId(supplyId);
			condition.setSettleFlowId(settleFlowId);
			condition.setRefunSettlementMoney(payToMoney);
			condition.setConfirmSettlement(OrderSettlement.confirm_settlement_done);
			orderSettlementMapper.updateSettlementPayFlowId(condition);
		}else if(!UtilHelper.isEmpty(flowId)&&!UtilHelper.isEmpty(type)
				&&!UtilHelper.isEmpty(settleFlowId)&&!UtilHelper.isEmpty(supplyId)
				&&type.intValue()==1){
			condition.setSettleFlowId(settleFlowId);
			condition.setConfirmSettlement(OrderSettlement.confirm_settlement_done);
			condition.setSupplyId(supplyId);
			condition.setRefunSettlementMoney(payToMoney);
			orderSettlementMapper.updateSettlementPayFlowId(condition);
		}else if(!UtilHelper.isEmpty(flowId)&&!UtilHelper.isEmpty(type)
				&&!UtilHelper.isEmpty(settleFlowId)&&!UtilHelper.isEmpty(supplyId)
				&&(type.intValue()==5)){//取消发货退款结算
			condition.setCustId(supplyId);
			condition.setSettleFlowId(settleFlowId);
			condition.setRefunSettlementMoney(payToMoney);
			condition.setConfirmSettlement(OrderSettlement.confirm_settlement_done);
			orderSettlementMapper.updateSettlementPayFlowId(condition);
		}else{
			log.info("更新结算信息更新结算信息表-传入空值参数");
		}

	}

	public void updateSettlementByCheckFile(String flowId, Integer type) {
		log.info("订单:" + flowId + ",类型为 " + type);
		Map<String, Object> condition = new HashedMap();
		OrderSettlement orderSettlement = null;
		if (type == 1) {
			condition.put("flowId", flowId);
			condition.put("businessType", 1);
			orderSettlement = orderSettlementMapper.getByProperty(condition);
		} else {
			condition.put("flowId", flowId);
			orderSettlement = orderSettlementMapper.getByPropertyByReturnCheckFile(condition);
		}

		if (orderSettlement != null) {
			orderSettlement.setConfirmSettlement(OrderSettlement.confirm_settlement_done);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			orderSettlement.setUpdateTime(sdf.format(new Date()));
			orderSettlementMapper.update(orderSettlement);
		} else {
			log.info("更新结算信息->未找到有效订单:" + flowId);
		}

	}

	/**
	 * 退款回调返回成功状态后修改 结算表 的 结算状态 为 1已结算（银行对账完毕）
	 *
	 * @param settleFlowId
	 */
	public void updateConfirmSettlement(String settleFlowId) {
		log.error("updateConfirmSettlement method==" + settleFlowId);
		OrderSettlement orderSettlement = null;
		Map<String, Object> condition = new HashedMap();
		condition.put("settleFlowId", settleFlowId);
		condition.put("confirmSettlement", OrderSettlement.confirm_settlement_done);
		orderSettlement = orderSettlementMapper.getByProperty(condition);
		if (orderSettlement == null) {
			orderSettlement.setSettleFlowId(settleFlowId);
			orderSettlement.setConfirmSettlement(OrderSettlement.confirm_settlement_done);
			orderSettlementMapper.updateConfirmSettlement(orderSettlement);
		}

	}


	/**
	 * 支付宝退款回调返回成功状态后修改  结算表 的 结算状态 为  1已结算（银行对账完毕）
	 * @param settleFlowId 如果存在异常订单ID，settleFlowId就是异常订单ID 如果不存在就是原订单ID
	 */
	public void updateSettlementByMapInfo(String settleFlowId,String tradeNo)
	{
		log.error("更新结算状态updateSettlementByMapInfo method==" + settleFlowId + ", " + tradeNo);
		Map<String,Object> conditionInfo = new HashedMap();
		conditionInfo.put("flowId", settleFlowId);
		Order order = orderMapper.getOrderbyFlowId(settleFlowId);
		if(!UtilHelper.isEmpty(order)){
			if(SystemOrderStatusEnum.SellerDelivered.getType().equals(order.getOrderStatus())){
				conditionInfo.put("businessType", 5);
			}else{
				conditionInfo.put("businessType", 4);
			}
		}
		OrderSettlement orderSettlementInfo = orderSettlementMapper.getByProperty(conditionInfo);
		if (!UtilHelper.isEmpty(orderSettlementInfo)&&!UtilHelper.isEmpty(order)) {
			log.error("更新卖家取消结算信息");
			orderSettlementInfo.setSettleFlowId(tradeNo);
			orderSettlementInfo.setRefunSettlementMoney(orderSettlementInfo.getSettlementMoney());
			orderSettlementInfo.setConfirmSettlement(OrderSettlement.confirm_settlement_done);
			orderSettlementMapper.updateSettlementPayFlowId(orderSettlementInfo);
		} else {
			OrderException exceptionInfo = new OrderException();
			exceptionInfo.setFlowId(settleFlowId);
			if(SystemOrderStatusEnum.BuyerPartReceived.getType().equals(order.getOrderStatus())){
				exceptionInfo.setReturnType(OrderExceptionTypeEnum.REJECT.getType());
			}else{
				exceptionInfo.setReturnType(OrderExceptionTypeEnum.RETURN.getType());
			}
			List<OrderException> listInfo = orderExceptionMapper.listByProperty(exceptionInfo);
			if (listInfo != null && listInfo.size() > 0) {
				OrderException exception = listInfo.get(0);
				if (!UtilHelper.isEmpty(exception)) {
					log.error("取出异常结算数据exception_order_id:" + exception.getExceptionOrderId());
					Map<String, Object> condition = new HashedMap();
					condition.put("flowId", exception.getExceptionOrderId());
					OrderSettlement orderSettlement = orderSettlementMapper.getByProperty(condition);
					if (orderSettlement != null) {
						log.error("开始更新数据");
						orderSettlement.setRefunSettlementMoney(orderSettlement.getSettlementMoney());
						orderSettlement.setSettleFlowId(tradeNo);
						orderSettlement.setConfirmSettlement(OrderSettlement.confirm_settlement_done);
						orderSettlementMapper.updateSettlementPayFlowId(orderSettlement);
					}

				} else {
					log.error("无数据");
				}
			}
		}
	}
}