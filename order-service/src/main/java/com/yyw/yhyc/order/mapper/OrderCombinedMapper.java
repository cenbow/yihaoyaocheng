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

import com.yyw.yhyc.order.bo.OrderCombined;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface OrderCombinedMapper extends GenericIBatisMapper<OrderCombined, Integer> {

	public List<OrderCombined> listPaginationByProperty(Pagination<OrderCombined> pagination, OrderCombined orderCombined);

	public OrderCombined findByPayFlowId(String payFlowId);
}
