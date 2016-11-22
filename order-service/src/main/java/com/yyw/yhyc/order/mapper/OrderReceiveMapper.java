/**
 * Created By: XI
 * Created On: 2016-11-22 10:24:37
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.mapper;

import java.util.List;

import com.yyw.yhyc.order.bo.OrderReceive;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface OrderReceiveMapper extends GenericIBatisMapper<OrderReceive, java.lang.String> {

	public List<OrderReceive> listPaginationByProperty(Pagination<OrderReceive> pagination, OrderReceive orderReceive);
}
