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

import java.util.List;

import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface OrderDetailMapper extends GenericIBatisMapper<OrderDetail, Integer> {

	public List<OrderDetail> listPaginationByProperty(Pagination<OrderDetail> pagination, OrderDetail orderDetail);

	public List<OrderDetail> listOrderDetailInfoByOrderId(int orderId);

	public List<OrderDetail> listByIds(List<Integer> orderDetailIds);
	
	public List<OrderDetail> listOrderDetailInfoByOrderFlowId(String flowId);
}
