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
package com.yyw.yhyc.order.service;

import java.util.List;

import com.yyw.yhyc.order.dto.OrderSettlementDto;
import com.yyw.yhyc.order.helper.UtilHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderSettlementMapper;

@Service("orderSettlementService")
public class OrderSettlementService {

	private OrderSettlementMapper	orderSettlementMapper;

	@Autowired
	public void setOrderSettlementMapper(OrderSettlementMapper orderSettlementMapper)
	{
		this.orderSettlementMapper = orderSettlementMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderSettlement getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderSettlementMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderSettlement> list() throws Exception
	{
		return orderSettlementMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderSettlement> listByProperty(OrderSettlement orderSettlement)
			throws Exception
	{
		return orderSettlementMapper.listByProperty(orderSettlement);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderSettlementDto> listPaginationByProperty(Pagination<OrderSettlementDto> pagination, OrderSettlementDto orderSettlementDto) throws Exception
	{
		List<OrderSettlementDto> list = orderSettlementMapper.listPaginationDtoByProperty(pagination, orderSettlementDto);
		if(!UtilHelper.isEmpty(list)){
			for (OrderSettlementDto osd :list) {
				if(orderSettlementDto.getType()==1){//type = 1 应收
					if(osd.getBusinessType()==1){
						osd.setBusinessTypeName("销售应收");
					}else if(osd.getBusinessType()==2){
						osd.setBusinessTypeName("退款应收");
					}
				}else{// type =2 应付
					if(osd.getBusinessType()==1){
						osd.setBusinessTypeName("采购应付");
					}else if(osd.getBusinessType()==2){
						osd.setBusinessTypeName("退款应付");
					}
				}
			}
		}
		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderSettlementMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderSettlementMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderSettlement orderSettlement) throws Exception
	{
		return orderSettlementMapper.deleteByProperty(orderSettlement);
	}

	/**
	 * 保存记录
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public void save(OrderSettlement orderSettlement) throws Exception
	{
		orderSettlementMapper.save(orderSettlement);
	}

	/**
	 * 更新记录
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public int update(OrderSettlement orderSettlement) throws Exception
	{
		return orderSettlementMapper.update(orderSettlement);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderSettlement
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderSettlement orderSettlement) throws Exception
	{
		return orderSettlementMapper.findByCount(orderSettlement);
	}
}