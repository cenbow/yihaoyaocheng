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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.helper.DateHelper;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.OrderReturnDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.mapper.OrderDeliveryDetailMapper;
import com.yyw.yhyc.order.mapper.OrderDetailMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.order.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderReturnMapper;

@Service("orderReturnService")
public class OrderReturnService {

	private OrderReturnMapper	orderReturnMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderDeliveryDetailMapper orderDeliveryDetailMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderExceptionService orderExceptionService;

	@Autowired
	public void setOrderReturnMapper(OrderReturnMapper orderReturnMapper)
	{
		this.orderReturnMapper = orderReturnMapper;
	}

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderReturn getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderReturnMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderReturn> list() throws Exception
	{
		return orderReturnMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderReturn> listByProperty(OrderReturn orderReturn)
			throws Exception
	{
		return orderReturnMapper.listByProperty(orderReturn);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderReturn> listPaginationByProperty(Pagination<OrderReturn> pagination, OrderReturn orderReturn) throws Exception
	{
		List<OrderReturn> list = orderReturnMapper.listPaginationByProperty(pagination, orderReturn);

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
		return orderReturnMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderReturnMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderReturn
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderReturn orderReturn) throws Exception
	{
		return orderReturnMapper.deleteByProperty(orderReturn);
	}

	/**
	 * 保存记录
	 * @param orderReturn
	 * @return
	 * @throws Exception
	 */
	public void save(OrderReturn orderReturn) throws Exception
	{
		orderReturnMapper.save(orderReturn);
	}

	/**
	 * 更新记录
	 * @param orderReturn
	 * @return
	 * @throws Exception
	 */
	public int update(OrderReturn orderReturn) throws Exception
	{
		return orderReturnMapper.update(orderReturn);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderReturn
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderReturn orderReturn) throws Exception
	{
		return orderReturnMapper.findByCount(orderReturn);
	}
	/**
	 *	退/换货
     */
	public String saveProductReturn(List<OrderReturn> returnList, UserDto userDto)throws Exception{
		String code = "0";
		if(returnList!=null && returnList.size()>0){
			List<Integer> orderDeliveryDetailIdList = new ArrayList<>();
			List<Integer> orderDetailIdList = new ArrayList<>();
			List<OrderReturn> saveReturnList  = new ArrayList<>();
			Map<Integer,Integer> orderDeliveryCountMap = new HashMap<>();
			Map<Integer,OrderDetail> orderDetailMap = new HashMap<>();
			Map<Integer,BigDecimal> returnPriceMap = new HashMap<>();
			OrderReturn orderReturn = returnList.get(0);
			Order order = orderMapper.getOrderbyFlowId(orderReturn.getFlowId());
			OrderException condition = new OrderException();
			condition.setFlowId(order.getFlowId());
			condition.setReturnType(orderReturn.getReturnType());
			Integer roundNum = orderExceptionService.findByCount(condition);

			for (OrderReturn or: returnList) {
                orderDeliveryDetailIdList.add(or.getOrderDeliveryDetailId());
                orderDetailIdList.add(or.getOrderDetailId());
                orderDeliveryCountMap.put(or.getOrderDeliveryDetailId(),or.getReturnCount());
				saveReturnList.add(or);
            }
            //订单详情列表 map
            List<OrderDetail> orderDetailList = orderDetailMapper.listByIds(orderDetailIdList);
            for (OrderDetail od:orderDetailList) {
                orderDetailMap.put(od.getOrderDetailId(),od);
            }

			//退换货订单总金额
			List<OrderDeliveryDetail> OrderDeliveryDetailList = orderDeliveryDetailMapper.listByIds(orderDeliveryDetailIdList);
            BigDecimal orderExceptionMoney = new BigDecimal(0).setScale(2);
            for (OrderDeliveryDetail odd:OrderDeliveryDetailList) {
                Integer canReturnCount = odd.getCanReturnCount()==null?odd.getRecieveCount():odd.getCanReturnCount();
                Integer stractCount = orderDeliveryCountMap.get(odd.getOrderDeliveryDetailId());
                if(stractCount!=null&&stractCount>0){
                    odd.setCanReturnCount(canReturnCount-stractCount);
                    OrderDetail od = orderDetailMap.get(odd.getOrderDetailId());
                    if(od.getProductPrice()!=null){
                    	returnPriceMap.put(odd.getOrderDeliveryDetailId(),od.getProductPrice().multiply(new BigDecimal(stractCount)));
						orderExceptionMoney = orderExceptionMoney.add(od.getProductPrice().multiply(new BigDecimal(stractCount)));
                    }
                }
                //更新收货详情
                orderDeliveryDetailMapper.update(odd);
            }

            //保存退货异常订单
            OrderException oe = parseOrderException(order,userDto,orderReturn,orderExceptionMoney,roundNum);
			orderExceptionService.save(oe);

            //保存退换货详情
            for (OrderReturn or: saveReturnList) {
                or.setCustId(order.getCustId());
                or.setOrderId(order.getOrderId());
                or.setCreateTime(DateHelper.nowString());
                or.setCreateUser(userDto.getUserName());
                or.setReturnStatus("1");
                or.setExceptionOrderId(oe.getExceptionOrderId());
				or.setReturnPay(returnPriceMap.get(or.getOrderDeliveryDetailId()));
            }
			orderReturnMapper.saveBatch(returnList);
			code = "1";
		}
		return  "{\"code\":"+code+"}";
	}

	private OrderException parseOrderException(Order order,UserDto userDto,OrderReturn orderReturn,BigDecimal orderExceptionMoney,Integer roundNum){
		OrderException oe = new OrderException();
		oe.setOrderId(order.getOrderId());
		oe.setCustId(order.getCustId());
		oe.setSupplyId(order.getSupplyId());
		oe.setSupplyName(order.getSupplyName());
		oe.setFlowId(order.getFlowId());
		oe.setCustName(order.getCustName());
		oe.setOrderMoneyTotal(orderExceptionMoney);
		oe.setOrderMoney(orderExceptionMoney);
		oe.setCreateUser(userDto.getUserName());
		oe.setReturnType(orderReturn.getReturnType());
		oe.setReturnDesc(orderReturn.getReturnDesc());
		oe.setRemark(orderReturn.getReturnDesc());
		oe.setCreateTime(DateHelper.nowString());
		oe.setOrderCreateTime(DateHelper.nowString());
		oe.setOrderStatus("1");

		if(orderReturn.getReturnType().equals("1")){
			oe.setExceptionOrderId("TH"+order.getFlowId()+ RandomUtil.createRoundNum(roundNum,2));
		}else if(orderReturn.getReturnType().equals("2")){
			oe.setExceptionOrderId("HH"+order.getFlowId()+RandomUtil.createRoundNum(roundNum,2));
		}
		return oe;
	}

	public List<OrderReturnDto> listOrderReturn(String orderExceptionId){
		return orderReturnMapper.getReturnByExceptionOrderId(orderExceptionId);
	}
}