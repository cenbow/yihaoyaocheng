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

import com.yyw.yhyc.order.bo.OrderReturn;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.dto.OrderReturnDto;

public interface OrderReturnMapper extends GenericIBatisMapper<OrderReturn, Integer> {

	public List<OrderReturn> listPaginationByProperty(Pagination<OrderReturn> pagination, OrderReturn orderReturn);

	public List<OrderReturnDto> getByExceptionOrderId(String exceptionOrderId);
	
	public List<OrderReturnDto> getOrderReturnByTypeAndExceptionId(Map<String,Object> map);

	public void saveBatch(List<OrderReturn> returnList);

	public List<OrderReturnDto> getReturnByExceptionOrderId(String exceptionOrderId);
}
