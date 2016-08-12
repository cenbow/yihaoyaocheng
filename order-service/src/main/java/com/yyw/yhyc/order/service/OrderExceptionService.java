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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.order.dto.OrderExceptionDto;

import com.yyw.yhyc.order.dto.OrderReturnDto;
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
import com.yyw.yhyc.order.mapper.OrderExceptionMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.order.mapper.OrderSettlementMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;

@Service("orderExceptionService")
public class OrderExceptionService {

	private Log log = LogFactory.getLog(OrderExceptionService.class);

	private OrderExceptionMapper	orderExceptionMapper;
	private OrderSettlementMapper orderSettlementMapper;
	private SystemDateMapper systemDateMapper;
	private OrderMapper	orderMapper;

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
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderException getByPK(java.lang.Integer primaryKey) throws Exception
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
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderExceptionMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
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
		orderExceptionDto = orderExceptionMapper.getOrderExceptionDetails(orderExceptionDto);

		if(!UtilHelper.isEmpty(orderExceptionDto) && !UtilHelper.isEmpty(orderExceptionDto.getOrderReturnList())){
			BigDecimal productPriceCount = new BigDecimal(0);
			for(OrderReturnDto orderReturnDto : orderExceptionDto.getOrderReturnList()){
				if(UtilHelper.isEmpty(orderReturnDto)) continue;
				productPriceCount = productPriceCount.add(orderReturnDto.getReturnPay());
			}
			orderExceptionDto.setProductPriceCount(productPriceCount);
		}
		return orderExceptionDto;
	}
	/**
	 * 拒收订单卖家审核通过生成结算记录
	 * @param custId
	 * @param orderExceptionDto
	 * @throws Exception
	 */
	private void saveRefuseOrderSettlement(Integer custId,OrderExceptionDto orderExceptionDto) throws Exception{
		Order order = orderMapper.getByPK(orderExceptionDto.getOrderId());
		if(UtilHelper.isEmpty(order)||!custId.equals(order.getSupplyId())){
			throw new RuntimeException("未找到订单");
		}
		String now = systemDateMapper.getSystemDate();
		OrderSettlement orderSettlement = new OrderSettlement();
		orderSettlement.setBusinessType(2);
		orderSettlement.setFlowId(orderExceptionDto.getExceptionOrderId());
		orderSettlement.setCustId(orderExceptionDto.getCustId());
		orderSettlement.setCustName(orderExceptionDto.getCustName());
		orderSettlement.setSupplyId(orderExceptionDto.getSupplyId());
		orderSettlement.setSupplyName(orderExceptionDto.getSupplyName());
		orderSettlement.setConfirmSettlement("1");
		orderSettlement.setPayTypeId(order.getPayTypeId());
		orderSettlement.setSettlementTime(now);
		orderSettlement.setCreateUser(orderExceptionDto.getCustName());
		orderSettlement.setCreateTime(now);
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
		BigDecimal orderTotalMoney = null;

		List<OrderExceptionDto> orderExceptionDtoList = orderExceptionMapper.listPaginationBuyerRejectOrder(pagination, orderExceptionDto);
		log.info("orderExceptionDtoList:"+orderExceptionDtoList);
		pagination.setResultList(orderExceptionDtoList);

		resultMap.put("rejectOrderStatusCount", null);
		resultMap.put("rejectOrderList", pagination);
		resultMap.put("rejectOrderCount", orderCount);
		resultMap.put("rejectOrderTotalMoney", orderTotalMoney == null? 0:orderTotalMoney);
		return resultMap;
	}


	public Pagination<OrderExceptionDto> listPaginationSellerByProperty(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto) throws Exception{

		List<OrderExceptionDto> list = orderExceptionMapper.listPaginationSellerByProperty(pagination,orderExceptionDto);
		pagination.setResultList(list);
		return pagination;
	}

}