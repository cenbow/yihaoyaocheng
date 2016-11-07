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
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.order.dto.OrderSettlementDto;

import java.util.List;
import java.util.Map;

public interface OrderSettlementMapper extends GenericIBatisMapper<OrderSettlement, Integer> {

	public List<OrderSettlementDto> listPaginationDtoByProperty(Pagination<OrderSettlementDto> pagination, OrderSettlementDto orderSettlementDto);

	public OrderSettlement getByProperty(Map<String,Object> map);
	public OrderSettlement getByPropertyByReturnCheckFile(Map<String,Object> map);

	public int updateConfirmSettlement(OrderSettlement orderSettlement);
}
