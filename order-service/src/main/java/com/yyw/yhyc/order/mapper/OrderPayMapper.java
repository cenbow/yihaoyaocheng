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

import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.order.mapper.GenericIBatisMapper;
import com.yyw.yhyc.order.bo.Pagination;

public interface OrderPayMapper extends GenericIBatisMapper<OrderPay, Integer> {

	public List<OrderPay> listPaginationByProperty(Pagination<OrderPay> pagination, OrderPay orderPay);
}
