/**
 * Created By: XI
 * Created On: 2016-12-8 9:28:10
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

import com.yyw.yhyc.order.bo.OrderPromotionHistory;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface OrderPromotionHistoryMapper extends GenericIBatisMapper<OrderPromotionHistory, java.lang.Long> {

	public List<OrderPromotionHistory> listPaginationByProperty(Pagination<OrderPromotionHistory> pagination, OrderPromotionHistory orderPromotionHistory);
	
	/**
	 * 根据促销的id和对应的买家，查询该买家促销使用历史
	 * @param parameter
	 * @return
	 */
	public OrderPromotionHistory getObjectByCustIdAndPromotiondId(Map<String,Object> parameter);
}
