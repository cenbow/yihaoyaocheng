/**
 * Created By: XI
 * Created On: 2016-12-9 10:42:31
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.mapper;

import java.util.List;

import com.yyw.yhyc.order.bo.OrderLog;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface OrderLogMapper extends GenericIBatisMapper<OrderLog, java.lang.Long> {

	public List<OrderLog> listPaginationByProperty(Pagination<OrderLog> pagination, OrderLog orderLog);
}
