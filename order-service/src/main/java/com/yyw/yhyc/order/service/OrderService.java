/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:48
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.order.dto.OrderDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderMapper;

@Service("orderService")
public class OrderService {

	Log log = LogFactory.getLog(OrderService.class);

	private OrderMapper	orderMapper;

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
	public Order getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<Order> list() throws Exception
	{
		return orderMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<Order> listByProperty(Order order)
			throws Exception
	{
		return orderMapper.listByProperty(order);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<Order> listPaginationByProperty(Pagination<Order> pagination, Order order) throws Exception
	{
		List<Order> list = orderMapper.listPaginationByProperty(pagination, order);

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
		return orderMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(Order order) throws Exception
	{
		return orderMapper.deleteByProperty(order);
	}

	/**
	 * 保存记录
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public void save(Order order) throws Exception
	{
		orderMapper.save(order);
	}

	/**
	 * 更新记录
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int update(Order order) throws Exception
	{
		return orderMapper.update(order);
	}

	/**
	 * 根据条件查询记录条数
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int findByCount(Order order) throws Exception
	{
		return orderMapper.findByCount(order);
	}

	/**
	 * 创建订单
	 * @param orderCreateDto
	 * @throws Exception
	 */
	public List<OrderDto> createOrder(OrderCreateDto orderCreateDto) throws Exception{

		return null;
	}

    /**
     * 校验要购买的商品(通用方法)
     * @param productInfoDtoList
     * @throws Exception
     */
    public boolean validateProducts(List<ProductInfoDto> productInfoDtoList) {
        //todo
        return false;
    }
	public Map<String,Object> listPgBuyerOrder(Pagination<OrderDto> pagination, OrderDto orderDto){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		orderDto = new OrderDto();
		orderDto.setCustId(1);
		orderDto.setOrderId(1);
		orderDto.setSupplyName("");
		List<OrderDto> orderDtoList =  orderMapper.findOrderStatusCount(orderDto);
		log.debug(orderDtoList);
		return resultMap;
	}
}