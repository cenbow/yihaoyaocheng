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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.helper.DateHelper;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.order.bo.OrderReturn;
import com.yyw.yhyc.order.dto.OrderDeliveryDetailDto;
import com.yyw.yhyc.order.mapper.OrderDetailMapper;
import com.yyw.yhyc.order.mapper.OrderReturnMapper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.mapper.OrderDeliveryDetailMapper;

@Service("orderDeliveryDetailService")
public class OrderDeliveryDetailService {

	private OrderDeliveryDetailMapper	orderDeliveryDetailMapper;

	private OrderDetailMapper orderDetailMapper;

	private OrderReturnMapper orderReturnMapper;

	@Autowired
	public void setOrderReturnMapper(OrderReturnMapper orderReturnMapper) {
		this.orderReturnMapper = orderReturnMapper;
	}

	@Autowired
	public void setOrderDetailMapper(OrderDetailMapper orderDetailMapper) {
		this.orderDetailMapper = orderDetailMapper;
	}

	@Autowired
	public void setOrderDeliveryDetailMapper(OrderDeliveryDetailMapper orderDeliveryDetailMapper)
	{
		this.orderDeliveryDetailMapper = orderDeliveryDetailMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderDeliveryDetail getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderDeliveryDetailMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDeliveryDetail> list() throws Exception
	{
		return orderDeliveryDetailMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDeliveryDetail> listByProperty(OrderDeliveryDetail orderDeliveryDetail)
			throws Exception
	{
		return orderDeliveryDetailMapper.listByProperty(orderDeliveryDetail);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderDeliveryDetailDto> listPaginationByProperty(Pagination<OrderDeliveryDetailDto> pagination, OrderDeliveryDetailDto orderDeliveryDetailDto) throws Exception
	{
		List<OrderDeliveryDetailDto> list = orderDeliveryDetailMapper.listPaginationByProperty(pagination, orderDeliveryDetailDto);

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
		return orderDeliveryDetailMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderDeliveryDetailMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		return orderDeliveryDetailMapper.deleteByProperty(orderDeliveryDetail);
	}

	/**
	 * 保存记录
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public void save(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		orderDeliveryDetailMapper.save(orderDeliveryDetail);
	}

	/**
	 * 更新记录
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public int update(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		return orderDeliveryDetailMapper.update(orderDeliveryDetail);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderDeliveryDetail
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		return orderDeliveryDetailMapper.findByCount(orderDeliveryDetail);
	}

	public  Map<String,String> confirmReceipt(List<OrderDeliveryDetailDto> list) throws Exception{

		Map<String, String> returnMap = new HashMap<String, String>();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		String returnType = "";
		String returnDesc = "";
		String flowId = "";
		if (UtilHelper.isEmpty(list)||list.size()==0){
			returnMap.put("code","0");
			returnMap.put("msg","参数为空");
			return returnMap;
		}

		//更新批次收货数量
		for (OrderDeliveryDetailDto dto : list){

			if (UtilHelper.isEmpty(dto.getOrderDeliveryDetailId())){
				returnMap.put("code","0");
				returnMap.put("msg","收发货详情id为空");
				return returnMap;
			}

			if (UtilHelper.isEmpty(dto.getOrderDetailId())){
				returnMap.put("code","0");
				returnMap.put("msg","订单详情id为空");
				return returnMap;
			}

			OrderDeliveryDetail orderDeliveryDetail = orderDeliveryDetailMapper.getByPK(dto.getOrderDeliveryDetailId());
			orderDeliveryDetail.setRecieveCount(dto.getRecieveCount());
			orderDeliveryDetailMapper.update(orderDeliveryDetail);
			if(!UtilHelper.isEmpty(dto.getReturnType())){
				returnType=dto.getReturnType();
				returnDesc=dto.getReturnDesc();
				flowId=dto.getFlowId();
			}
			if(UtilHelper.isEmpty(map.get(dto.getOrderDetailId()))){
				map.put(dto.getOrderDetailId(), dto.getRecieveCount());
			}else {
				map.put(dto.getOrderDetailId(), map.get(dto.getOrderDetailId())+dto.getRecieveCount());
			}
		}
		//更新订单详情总收货数量//并判断采购数量和收货数量是否相同
		for (Integer orderdetailId:map.keySet()) {
			OrderDetail orderDetail = orderDetailMapper.getByPK(orderdetailId);
			orderDetail.setRecieveCount(map.get(orderdetailId));
			orderDetailMapper.update(orderDetail);
			if(orderDetail.getProductCount()!=orderDetail.getRecieveCount()){
				if(UtilHelper.isEmpty(returnType)||returnType.equals("")){
					 throw new Exception("采购商与收获数不同,拒收类型为空");
				}
					OrderReturn orderReturn=new OrderReturn();
					orderReturn.setOrderDetailId(orderDetail.getOrderDetailId());
					orderReturn.setOrderId(orderDetail.getOrderId());
					//orderReturn.setCustId("");当前登录的id
					orderReturn.setReturnCount(orderDetail.getProductCount() - orderDetail.getRecieveCount());
					BigDecimal bigDecimal = new BigDecimal(orderReturn.getReturnCount());
					orderReturn.setReturnPay(orderDetail.getProductPrice().multiply(bigDecimal));
					orderReturn.setReturnType(returnType);
					orderReturn.setReturnDesc(returnDesc);
					orderReturn.setFlowId(flowId);
					orderReturn.setCreateTime(DateHelper.nowString());
					orderReturn.setUpdateTime(DateHelper.nowString());
					/*orderReturn.setCreateUser(); 当前登录人
					orderReturn.setUpdateUser();*/
				}
			}
			returnMap.put("code","1");
			returnMap.put("msg","操作成功");
			return returnMap;
	}
}