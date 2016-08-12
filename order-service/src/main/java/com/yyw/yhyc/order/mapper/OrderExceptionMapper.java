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


	/**
	 * 采购商拒收订单查询
	 * @param pagination
	 * @param orderExceptionDto
     * @return
     */
	public List<OrderExceptionDto> listPaginationBuyerRejectOrder(Pagination<OrderExceptionDto> pagination, OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商拒收订单总金额
	 * @param orderExceptionDto
	 * @return
     */
	public java.math.BigDecimal findBuyerExceptionOrderTotal(OrderExceptionDto orderExceptionDto);

	/**
	 * 查询采购商拒收订单状态列表
	 * @param orderExceptionDto
	 * @return
     */
	public List<OrderExceptionDto> findBuyerRejectOrderStatusCount(OrderExceptionDto orderExceptionDto);

	public List<OrderExceptionDto> listPaginationSellerByProperty(Pagination<OrderExceptionDto> pagination, OrderExceptionDto OrderExceptionDto);

}
