/**
 * Created By: XI
 * Created On: 2016-10-25 10:38:15
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.mapper;

import java.util.List;

import com.yyw.yhyc.order.bo.OrderIssuedLog;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface OrderIssuedLogMapper extends GenericIBatisMapper<OrderIssuedLog, Integer> {

	public List<OrderIssuedLog> listPaginationByProperty(Pagination<OrderIssuedLog> pagination, OrderIssuedLog orderIssuedLog);
}
