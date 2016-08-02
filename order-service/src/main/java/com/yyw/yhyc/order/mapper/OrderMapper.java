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
package com.yyw.yhyc.order.mapper;

import java.util.List;
import java.util.Map;

import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.dto.OrderDetailsDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface OrderMapper extends GenericIBatisMapper<Order, Integer> {

	public List<Order> listPaginationByProperty(Pagination<Order> pagination, Order order);

	public OrderDetailsDto getOrderDetails(String flowId);

	public List<OrderDto> listPgBuyerOrder(Pagination<OrderDto> pagination, OrderDto orderDto);

	public List<OrderDto> findOrderStatusCount(OrderDto orderDto);

	public List<OrderDto> listPgSellerOrder(Pagination<OrderDto> pagination, OrderDto orderDto);

	public List<OrderDto> findSellerOrderStatusCount(OrderDto orderDto);

	public void cancelOrderForNoPay();

	public List<Order> listOrderFor7DayNoDelivery();

	public void cancelOrderFor7DayNoDelivery(List<Integer> li);

	public List<Order> listOrderForDeliveryAfter7Day();

	public void doneOrderForDeliveryAfter7Day(List<Integer> li);


}
