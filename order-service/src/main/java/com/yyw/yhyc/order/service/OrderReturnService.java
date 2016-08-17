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

import java.util.ArrayList;
import java.util.List;

import com.yyw.yhyc.helper.DateHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.mapper.OrderDeliveryDetailMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderReturn;
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
			Order order = orderMapper.getOrderbyFlowId(returnList.get(0).getFlowId());
			List<Integer> orderDeliveryIdList = new ArrayList<>();
			for (OrderReturn or: returnList) {
				or.setCustId(order.getCustId());
				or.setOrderId(order.getOrderId());
				or.setCreateTime(DateHelper.nowString());
				//or.setCreateUser(userDto.getUserName());
				or.setReturnStatus("1");
				orderDeliveryIdList.add(or.getOrderDeliveryDetailId());
				orderReturnMapper.save(or);
			}
			//TODO  扣减可用数量
			List<OrderDeliveryDetail> OrderDeliveryDetailList = orderDeliveryDetailMapper.list();


			code = "1";
		}
		return  "{\"code\":"+code+"}";
	}
}