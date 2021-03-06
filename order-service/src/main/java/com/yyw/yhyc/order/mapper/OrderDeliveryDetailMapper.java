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

import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.order.dto.OrderDeliveryDetailDto;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface OrderDeliveryDetailMapper extends GenericIBatisMapper<OrderDeliveryDetail, Integer> {

	public List<OrderDeliveryDetailDto> listPaginationByProperty(Pagination<OrderDeliveryDetailDto> pagination, OrderDeliveryDetailDto orderDeliveryDetailDto);

	public List<OrderDeliveryDetail> listByIds(List<Integer> orderDeliveryDetailIds);

	public List<OrderDeliveryDetailDto> listPaginationReturnByProperty(Pagination<OrderDeliveryDetailDto> pagination, OrderDeliveryDetailDto orderDeliveryDetailDto);

	public List<OrderDeliveryDetailDto> listPaginationReplenishment(Pagination<OrderDeliveryDetailDto> pagination, OrderDeliveryDetailDto orderDeliveryDetailDto);

	public List<OrderDeliveryDetailDto> listPaginationOrderDeliveryDetailDto(Pagination<OrderDeliveryDetailDto> pagination, OrderDeliveryDetailDto orderDeliveryDetailDto);

	public List<OrderDeliveryDetail> updateRecieveCount(OrderDeliveryDetail orderDeliveryDetail);
}
