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
package com.yyw.yhyc.order.facade.impl;

import java.util.List;
import java.util.Map;

import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDetailsDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.order.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.facade.OrderFacade;
import com.yyw.yhyc.order.service.OrderService;

@Service("orderFacade")
public class OrderFacadeImpl implements OrderFacade {

	private OrderService orderService;

	@Autowired
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	/**
	 * 通过主键查询实体对象
	 *
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public Order getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<Order> list() throws Exception
	{
		return orderService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<Order> listByProperty(Order order)
			throws Exception
	{
		return orderService.listByProperty(order);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<Order> listPaginationByProperty(Pagination<Order> pagination, Order order)
			throws Exception
	{
		return orderService.listPaginationByProperty(pagination, order);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(Order order) throws Exception
	{
		return orderService.deleteByProperty(order);
	}

	/**
	 * 保存记录
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public void save(Order order) throws Exception
	{
		orderService.save(order);
	}

	/**
	 * 更新记录
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int update(Order order) throws Exception
	{
		return orderService.update(order);
	}

	/**
	 * 根据条件查询记录条数
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public int findByCount(Order order) throws Exception
	{
		return orderService.findByCount(order);
	}

	/**
	 * 创建订单
	 * @param orderCreateDto
	 * @throws Exception
	 */
	public List<Order> createOrder(OrderCreateDto orderCreateDto) throws Exception {
		return orderService.createOrder(orderCreateDto);
	}

	/**
	 * 校验要购买的商品(通用方法)
	 *
	 * @param orderDto
	 * @throws Exception
	 */
	public boolean validateProducts(OrderDto orderDto) throws Exception {
		return orderService.validateProducts(orderDto);
	}
	/**
	 * 查采购商订单查询
	 * @param pagination
	 * @param orderDto
	 * @return
	 */
	public Map<String,Object> listPgBuyerOrder(Pagination<OrderDto> pagination, OrderDto orderDto){
		return orderService.listPgBuyerOrder(pagination,orderDto);
	}

	/**
	 * 根据订单号查询订单详情
	 *
	 * @param order
	 * @throws Exception
	 */
	public OrderDetailsDto getOrderDetails(Order order) throws Exception {
		return orderService.getOrderDetails(order);
	}

	/**
	 * 采购商取消订单
	 * @param custId
	 * @param orderId
	 */
	public void  buyerCancelOrder(Integer custId,Integer orderId){
		orderService.updateOrderStatusForBuyer(custId,orderId);
	}

	/**
	 * 销售订单查询
	 * @param pagination
	 * @param orderDto
	 * @return
	 */
	public Map<String,Object> listPgSellerOrder(Pagination<OrderDto> pagination, OrderDto orderDto){
		return orderService.listPgSellerOrder(pagination,orderDto);
	}

	/**
	 * 卖家取消订单
	 * @param custId
	 * @param orderId
	 */
	public void  sellerCancelOrder(Integer custId,Integer orderId,String cancelResult){
		orderService.updateOrderStatusForSeller(custId,orderId,cancelResult);
	}

	public OrderCreateDto checkOrderPage() {
		return orderService.checkOrderPage();
	}
}