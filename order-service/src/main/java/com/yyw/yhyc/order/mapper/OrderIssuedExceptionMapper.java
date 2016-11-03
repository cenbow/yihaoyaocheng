/**
 * Created By: XI
 * Created On: 2016-10-24 15:52:00
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.mapper;

import java.util.List;

import com.yyw.yhyc.order.bo.OrderIssuedException;
import com.yyw.yhyc.order.dto.OrderIssuedExceptionDto;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface OrderIssuedExceptionMapper extends GenericIBatisMapper<OrderIssuedException, java.lang.Integer> {

	public int updateBySelective(OrderIssuedException orderIssuedException);
	
	public List<OrderIssuedException> listPaginationByProperty(Pagination<OrderIssuedException> pagination, OrderIssuedException orderIssuedException);
	
	public List<OrderIssuedExceptionDto> listPaginationByPropertyEx(Pagination<OrderIssuedExceptionDto> pagination, OrderIssuedExceptionDto orderIssuedExceptionDto);
}
