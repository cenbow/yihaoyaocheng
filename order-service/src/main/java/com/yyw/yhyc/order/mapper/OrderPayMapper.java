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
package com.yyw.yhyc.order.mapper;

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.order.dto.OrderPayDto;

import java.util.List;

public interface OrderPayMapper extends GenericIBatisMapper<OrderPay, Integer> {

	public List<OrderPay> listPaginationByProperty(Pagination<OrderPay> pagination, OrderPay orderPay);

	public OrderPay getByPayFlowId(String payFlowId);

	public OrderPay getPayFlowIdByPayAccountNo(String payaccountno);

	public List<OrderPayDto> listOrderPayDtoByProperty(OrderPay orderPay);
}
