/**
 * Created By: XI
 * Created On: 2016-9-10 10:28:42
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

import com.yyw.yhyc.order.bo.OrderIssued;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.dto.OrderIssuedDto;
import com.yyw.yhyc.order.dto.OrderIssuedItemDto;

public interface OrderIssuedMapper extends GenericIBatisMapper<OrderIssued, java.lang.Integer> {

	public List<OrderIssued> listPaginationByProperty(Pagination<OrderIssued> pagination, OrderIssued orderIssued);
	public List<OrderIssuedDto> findOrderIssuedListBySupplyId(Integer supplyId);
	public List<OrderIssuedItemDto> getItemsById(Integer orderId);
	public OrderIssued findByFlowId(String flowId);
	public List<OrderIssued> getManufacturerOrder(Integer supplyId);
	public List<Map<String,Object>> findOrderIssuedNoRelationshipList(Integer supplyId);
	public int updateBySelective(OrderIssued orderIssued);
}
