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
import java.util.Map;

import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.order.dto.OrderSettlementDto;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface OrderSettlementMapper extends GenericIBatisMapper<OrderSettlement, Integer> {

	public List<OrderSettlementDto> listPaginationDtoByProperty(Pagination<OrderSettlementDto> pagination, OrderSettlementDto orderSettlementDto);

	public OrderSettlement getByProperty(Map<String,Object> map);
}
