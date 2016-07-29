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

import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.mapper.GenericIBatisMapper;
import com.yyw.yhyc.order.bo.Pagination;

public interface OrderDeliveryMapper extends GenericIBatisMapper<OrderDelivery, Integer> {

	public List<OrderDelivery> listPaginationByProperty(Pagination<OrderDelivery> pagination, OrderDelivery orderDelivery);

	public OrderDelivery getByOrderId(int orderId);
}
