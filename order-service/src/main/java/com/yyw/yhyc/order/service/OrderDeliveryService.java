/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:48
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.OrderDeliveryDto;

import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.usermanage.bo.UsermanageReceiverAddress;
import com.yyw.yhyc.usermanage.mapper.UsermanageReceiverAddressMapper;
import com.yyw.yhyc.utils.ExcelUtil;
import com.yyw.yhyc.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.bo.Pagination;

import javax.servlet.http.HttpServletRequest;

@Service("orderDeliveryService")
public class OrderDeliveryService {

	private OrderDeliveryMapper	orderDeliveryMapper;

	private OrderDetailMapper orderDetailMapper;

	private OrderDeliveryDetailMapper orderDeliveryDetailMapper;

	private OrderMapper orderMapper;

	private SystemDateMapper systemDateMapper;

	private UsermanageReceiverAddressMapper receiverAddressMapper;

	private String FILE_TEMPLATE_PATH="include/excel/";

	@Autowired
	public void setReceiverAddressMapper(UsermanageReceiverAddressMapper receiverAddressMapper) {
		this.receiverAddressMapper = receiverAddressMapper;
	}

	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}

	@Autowired
	public void setOrderMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}

	@Autowired
	public void setOrderDeliveryMapper(OrderDeliveryMapper orderDeliveryMapper)
	{
		this.orderDeliveryMapper = orderDeliveryMapper;
	}

	@Autowired
	public void setOrderDetailMapper(OrderDetailMapper orderDetailMapper) {
		this.orderDetailMapper = orderDetailMapper;
	}

	@Autowired
	public void setOrderDeliveryDetailMapper(OrderDeliveryDetailMapper orderDeliveryDetailMapper) {
		this.orderDeliveryDetailMapper = orderDeliveryDetailMapper;
	}

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderDelivery getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderDeliveryMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDelivery> list() throws Exception
	{
		return orderDeliveryMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderDelivery> listByProperty(OrderDelivery orderDelivery)
			throws Exception
	{
		return orderDeliveryMapper.listByProperty(orderDelivery);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderDelivery> listPaginationByProperty(Pagination<OrderDelivery> pagination, OrderDelivery orderDelivery) throws Exception
	{
		List<OrderDelivery> list = orderDeliveryMapper.listPaginationByProperty(pagination, orderDelivery);

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
		return orderDeliveryMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderDeliveryMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderDelivery
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderDelivery orderDelivery) throws Exception
	{
		return orderDeliveryMapper.deleteByProperty(orderDelivery);
	}

	/**
	 * 保存记录
	 * @param orderDelivery
	 * @return
	 * @throws Exception
	 */
	public void save(OrderDelivery orderDelivery) throws Exception
	{
		orderDeliveryMapper.save(orderDelivery);
	}

	/**
	 * 更新记录
	 * @param orderDelivery
	 * @return
	 * @throws Exception
	 */
	public int update(OrderDelivery orderDelivery) throws Exception
	{
		return orderDeliveryMapper.update(orderDelivery);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderDelivery
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderDelivery orderDelivery) throws Exception
	{
		return orderDeliveryMapper.findByCount(orderDelivery);
	}

	/**
	 * 确认发货
	 * @param orderDeliveryDto
	 * @return
	 * @throws Exception
	 */

	public Map sendOrderDelivery(OrderDeliveryDto orderDeliveryDto) throws Exception{
		Map<String,String> map=new HashMap<String, String>();
		if(UtilHelper.isEmpty(orderDeliveryDto)){
			map.put("code", "0");
			map.put("msg", "发货信息不能为空");
			return map;
		}
		if(UtilHelper.isEmpty(orderDeliveryDto.getOrderId())){
			map.put("code", "0");
			map.put("msg", "订单id不能为空");
			return map;
		}
		if(UtilHelper.isEmpty(orderDeliveryDto.getReceiverAddressId())){
			map.put("code", "0");
			map.put("msg", "发货地址不能为空");
			return map;
		}
		if(UtilHelper.isEmpty(orderDeliveryDto.getDeliveryMethod())){
			map.put("code", "0");
			map.put("msg", "配送方式不能为空");
			return map;
		}

		//根据orderId查询订单收发货信息是否存在,更新发货信息
		OrderDelivery orderDelivery = orderDeliveryMapper.getByOrderId(orderDeliveryDto.getOrderId());
		orderDeliveryDto.setFlowId(orderDelivery.getFlowId());
		if(UtilHelper.isEmpty(orderDelivery)){
			map.put("code", "0");
			map.put("msg", "订单地址不存在");
			return map;
		}


		//验证批次号并生成订单发货数据
		readExcelOrderDeliveryDetail(orderDeliveryDto.getPath()+orderDeliveryDto.getFileName(),map,orderDeliveryDto);

		return map;
	}
	//读取验证订单批次信息excel
	public Map<String,String> readExcelOrderDeliveryDetail(String excelPath,Map<String,String> map,OrderDeliveryDto orderDeliveryDto){

		List<Map<String,String>> errorList=new ArrayList<Map<String, String>>();
		Map<String,String> errorMap=null;
		Map<String,String> codeMap=new HashMap<String, String>();
		Map <String,Integer> detailMap=new HashMap<String, Integer>();
		int orderId=0;
		String filePath="";

		try{
		List<Map<String, String>> 	list = ExcelUtil.readExcel(excelPath);
		if(list.size()>0){
			list.remove(0);
		}else {
			map.put("code","0");
			map.put("msg","读取文件错误");
		}
		for (Map<String,String> rowMap:list) {
			StringBuffer stringBuffer=new StringBuffer();
			if(UtilHelper.isEmpty(rowMap.get("1"))){
				stringBuffer.append("订单编码不能为空,");
			}
			if(UtilHelper.isEmpty(rowMap.get("2"))){
				stringBuffer.append("商品编码不能为空,");
			}

			if(UtilHelper.isEmpty(rowMap.get("3"))){
				stringBuffer.append("批号为不能空,");
			}

			if(UtilHelper.isEmpty(rowMap.get("4"))){
				stringBuffer.append("数量为空,");
			}

			if(!rowMap.get("1").equals(orderDeliveryDto.getFlowId())){
				stringBuffer.append("订单编码与发货订单编码不相同,");
			}

			//如果有必填为空则记录错误返回下一次循环
			if(stringBuffer.length()>0){
				errorMap=rowMap;
				errorMap.put("5",stringBuffer.toString().replace(stringBuffer.charAt(stringBuffer.length() - 1) + "", "。"));
				errorList.add(errorMap);
				continue;
			}else {
				//验证订单号与商品编码是否存在，都存在则根据商品编码记录批次数量
				Order order=orderMapper.getOrderbyFlowId(rowMap.get("1"));
				if(UtilHelper.isEmpty(order)){
					stringBuffer.append("订单编号不存在,");
				}else {
						orderId=order.getOrderId();
						OrderDetail orderDetail=new OrderDetail();
						orderDetail.setOrderId(order.getOrderId());
						orderDetail.setProductCode(rowMap.get("2"));
						orderDetail.setSupplyId(orderDeliveryDto.getUserDto().getCustId());
						List detailList = orderDetailMapper.listByProperty(orderDetail);
						if(detailList.size()<0){
							stringBuffer.append("商品编码不存在,");
						}
						if(stringBuffer.length()>0){
							errorMap=rowMap;
							errorMap.put("5",stringBuffer.toString().replace(stringBuffer.charAt(stringBuffer.length()-1)+"","。"));
							errorList.add(errorMap);
							continue;
						}else {
							if(UtilHelper.isEmpty(codeMap.get(rowMap.get("2")))){
								codeMap.put(rowMap.get("2"),rowMap.get("4"));
							}else {
								codeMap.put(rowMap.get("2"),String.valueOf(Integer.parseInt(codeMap.get(rowMap.get("2"))) + Integer.parseInt(rowMap.get("4"))));
							}
						}
					}
			}

		}
		//验证商品数量是否相同
			for (String code : codeMap.keySet()){
				OrderDetail orderDetail=new OrderDetail();
				orderDetail.setOrderId(orderId);
				orderDetail.setProductCode(code);
				orderDetail.setSupplyId(orderDeliveryDto.getUserDto().getCustId());
				List<OrderDetail> detailList = orderDetailMapper.listByProperty(orderDetail);
				if(detailList.size()>0){
					orderDetail=detailList.get(0);
					detailMap.put(code,orderDetail.getOrderDetailId());
					if(orderDetail.getProductCount()!=Integer.parseInt(codeMap.get(code))){
						errorMap=new HashMap<String, String>();
						errorMap.put("5",code+"的导入数量不等于采购数量");
						errorList.add(errorMap);
					}
				}

			}
			//生成excel和订单发货信息
			filePath=createOrderdeliverDetail(errorList,orderId,orderDeliveryDto,list,detailMap,excelPath);

			if(errorList.size()>0){
				map.put("code","2");
				map.put("msg","发货失败。");
				map.put("fileName",filePath);
			}else {
				//发货成功更新订单状态
				Order order = orderMapper.getOrderbyFlowId(orderDeliveryDto.getFlowId());
				order.setOrderStatus(SystemOrderStatusEnum.SellerDelivered.getType());
				order.setDeliverTime(systemDateMapper.getSystemDate());
				order.setUpdateTime(systemDateMapper.getSystemDate());
				order.setUpdateUser("登录用户");
				orderMapper.update(order);
				//生成发货信息
				UsermanageReceiverAddress receiverAddress=receiverAddressMapper.getByPK(orderDeliveryDto.getReceiverAddressId());
				OrderDelivery orderDelivery = orderDeliveryMapper.getByOrderId(orderDeliveryDto.getOrderId());
				orderDelivery.setDeliveryMethod(orderDeliveryDto.getDeliveryMethod());
				orderDelivery.setDeliveryContactPerson(orderDeliveryDto.getDeliveryContactPerson());
				orderDelivery.setDeliveryExpressNo(orderDeliveryDto.getDeliveryExpressNo());
				orderDelivery.setDeliveryDate(orderDeliveryDto.getDeliveryDate());
				orderDelivery.setUpdateDate(systemDateMapper.getSystemDate());
				orderDelivery.setDeliveryAddress(receiverAddress.getProvinceName() + receiverAddress.getCityName() + receiverAddress.getDistrictName() + receiverAddress.getAddress());
				orderDelivery.setDeliveryPerson(receiverAddress.getReceiverName());
				orderDelivery.setDeliveryContactPhone(receiverAddress.getContactPhone());
				orderDelivery.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDelivery.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDelivery.setCreateTime(systemDateMapper.getSystemDate());
				orderDeliveryMapper.update(orderDelivery);
				map.put("code","1");
				map.put("msg","发货成功。");
				map.put("fileName",excelPath);
			}

		}catch (Exception e){
			map.put("code", "0");
			map.put("msg", "Excel读取出错");
			e.getMessage();
		}
		return map;
	}
	/*生成订单行号 订单号加6位数字码 */
	public String createOrderLineNo(int i,String flowId){
		int count=6-String.valueOf(i).length();
		StringBuffer stringBuffer=new StringBuffer();
		stringBuffer.append(flowId);
		for(int j=0;j<count;j++){
			stringBuffer.append("0");
		}
	 	return stringBuffer.append(i).toString();
	}

	/*生产excel和订单发货信息 */
	public String createOrderdeliverDetail(List<Map<String,String>> errorList,int orderId,OrderDeliveryDto orderDeliveryDto,List<Map<String,String>> list,Map<String,Integer> detailMap,String excelPath) {
		String filePath = "";
		//生成错误excel和发货记录
		if (errorList.size() > 0) {
			String[] headers = {"序号", "订单编码", "商品编码", "批号", "数量", "失败原因"};
			List<Object[]> dataset = new ArrayList<Object[]>();
			for (Map<String, String> dataMap : errorList) {
				dataset.add(new Object[]{
						dataMap.get("0"), dataMap.get("1"), dataMap.get("2"), dataMap.get("3"), dataMap.get("4"), dataMap.get("5")
				});
			}
			filePath = orderDeliveryDto.getPath()+ExcelUtil.downloadExcel("发货批号导入信息", headers, dataset,orderDeliveryDto.getPath());

			OrderDeliveryDetail orderDeliveryDetail = new OrderDeliveryDetail();
			orderDeliveryDetail.setOrderId(orderId);
			orderDeliveryDetail.setFlowId(orderDeliveryDto.getFlowId());

			List<OrderDeliveryDetail> orderDeliveryDetails = orderDeliveryDetailMapper.listByProperty(orderDeliveryDetail);
			if (orderDeliveryDetails.size() > 0) {
				orderDeliveryDetail = orderDeliveryDetails.get(0);
				orderDeliveryDetail.setUpdateTime(systemDateMapper.getSystemDate());
				orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetail.setDeliveryStatus(0);
				orderDeliveryDetail.setImportFileUrl(filePath);
				orderDeliveryDetailMapper.update(orderDeliveryDetail);
			} else {
				orderDeliveryDetail.setCreateTime(systemDateMapper.getSystemDate());
				orderDeliveryDetail.setUpdateTime(systemDateMapper.getSystemDate());
				orderDeliveryDetail.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetail.setDeliveryStatus(0);
				orderDeliveryDetail.setImportFileUrl(filePath);
				orderDeliveryDetailMapper.save(orderDeliveryDetail);
			}
			return filePath;
		} else {
			OrderDeliveryDetail orderdel = new OrderDeliveryDetail();
			orderdel.setOrderId(orderId);
			orderdel.setFlowId(orderDeliveryDto.getFlowId());
			List<OrderDeliveryDetail> orderDeliveryDetails = orderDeliveryDetailMapper.listByProperty(orderdel);
			if (orderDeliveryDetails.size() > 0) {
				orderDeliveryDetailMapper.deleteByPK(orderDeliveryDetails.get(0).getOrderDeliveryDetailId());
			}
			for (Map<String, String> rowMap : list) {
				int i = 1;
				OrderDeliveryDetail orderDeliveryDetail = new OrderDeliveryDetail();
				orderDeliveryDetail.setOrderLineNo(createOrderLineNo(i, orderDeliveryDto.getFlowId()));
				orderDeliveryDetail.setOrderId(orderId);
				orderDeliveryDetail.setFlowId(orderDeliveryDto.getFlowId());
				orderDeliveryDetail.setDeliveryStatus(1);
				orderDeliveryDetail.setBatchNumber(rowMap.get("2"));
				orderDeliveryDetail.setOrderDetailId(detailMap.get(rowMap.get("2")));
				orderDeliveryDetail.setDeliveryProductCount(Integer.parseInt(rowMap.get("4")));
				orderDeliveryDetail.setImportFileUrl(excelPath);
				orderDeliveryDetail.setCreateTime(systemDateMapper.getSystemDate());
				orderDeliveryDetail.setUpdateTime(systemDateMapper.getSystemDate());
				orderDeliveryDetail.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetailMapper.save(orderDeliveryDetail);
				i++;
			}
		}
		return filePath;
	}



	public List<UsermanageReceiverAddress> getReceiveAddressList(UserDto user){
		UsermanageReceiverAddress receiverAddress = new UsermanageReceiverAddress();
		receiverAddress.setEnterpriseId(String.valueOf(user.getCustId()));
		List<UsermanageReceiverAddress> receiverAddressList = receiverAddressMapper.listByProperty(receiverAddress);
		return receiverAddressList;
	}

}