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
package com.yyw.yhyc.order.mapper;

import java.util.List;

import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.dto.OrderExceptionDto;

public interface OrderExceptionMapper extends GenericIBatisMapper<OrderException, java.lang.Integer> {

	public List<OrderException> listPaginationByProperty(Pagination<OrderException> pagination, OrderException orderException);

	/**
	 * 退货订单详情
	 * @param orderExceptionDto
	 * @return
	 */
	public OrderExceptionDto getOrderExceptionDetails(OrderExceptionDto orderExceptionDto);
}
