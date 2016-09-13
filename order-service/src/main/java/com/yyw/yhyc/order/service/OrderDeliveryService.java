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
import java.util.*;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.*;
import com.yyw.yhyc.order.dto.OrderDeliveryDto;

import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.SystemChangeGoodsOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderExceptionStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemRefundOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemReplenishmentOrderStatusEnum;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.product.manage.ProductInventoryManage;
import com.yyw.yhyc.usermanage.bo.UsermanageReceiverAddress;
import com.yyw.yhyc.usermanage.mapper.UsermanageReceiverAddressMapper;
import com.yyw.yhyc.utils.ExcelUtil;
import com.yyw.yhyc.utils.FileUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	private OrderExceptionMapper orderExceptionMapper;

	private OrderReturnMapper orderReturnMapper;

	private OrderTraceMapper orderTraceMapper;

	private ProductInventoryManage productInventoryManage;

	@Autowired
	public void setProductInventoryManage(ProductInventoryManage productInventoryManage) {
		this.productInventoryManage = productInventoryManage;
	}

	private Log log = LogFactory.getLog(OrderDeliveryService.class);

	@Autowired
	public void setOrderTraceMapper(OrderTraceMapper orderTraceMapper) {
		this.orderTraceMapper = orderTraceMapper;
	}

	@Autowired
	public void setOrderReturnMapper(OrderReturnMapper orderReturnMapper) {
		this.orderReturnMapper = orderReturnMapper;
	}


	@Autowired
	public void setOrderExceptionMapper(OrderExceptionMapper orderExceptionMapper) {
		this.orderExceptionMapper = orderExceptionMapper;
	}

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

	public Map updateSendOrderDelivery(OrderDeliveryDto orderDeliveryDto) throws Exception{
		Map<String,String> map=new HashMap<String, String>();
		if(UtilHelper.isEmpty(orderDeliveryDto)){
			map.put("code", "0");
			map.put("msg", "发货信息不能为空");
			return map;
		}
		if(UtilHelper.isEmpty(orderDeliveryDto.getFlowId())){
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

		//正常下单根据orderId查询订单收发货信息是否存在,更新发货信息
		if(orderDeliveryDto.getOrderType()==1){
				OrderDelivery orderDelivery = orderDeliveryMapper.getByFlowId(orderDeliveryDto.getFlowId());
				if(UtilHelper.isEmpty(orderDelivery)){
					map.put("code", "0");
					map.put("msg", "订单地址不存在");
					return map;
				}
			orderDeliveryDto.setOrderId(orderDelivery.getOrderId());
		}else {
			//更具异常订单编码获补货订单id
			OrderException orderException = orderExceptionMapper.getByExceptionOrderId(orderDeliveryDto.getFlowId());
			orderDeliveryDto.setOrderId(orderException.getExceptionId());
		}

		//验证批次号并生成订单发货数据
		readExcelOrderDeliveryDetail(orderDeliveryDto.getPath()+orderDeliveryDto.getFileName(),map,orderDeliveryDto);

		return map;
	}

	//读取验证订单批次信息excel
	public Map<String,String> readExcelOrderDeliveryDetail(String excelPath,Map<String,String> map,OrderDeliveryDto orderDeliveryDto){
		String now = systemDateMapper.getSystemDate();
		List<Map<String,String>> errorList=new ArrayList<Map<String, String>>();
		Map<String,String> errorMap=null;
		Map<String,String> codeMap=new HashMap<String, String>();
		Map <String,Integer> detailMap=new HashMap<String, Integer>();
		//原订单id
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

			if((!UtilHelper.isEmpty(rowMap.get("1")))&&!rowMap.get("1").equals(orderDeliveryDto.getFlowId())){
				stringBuffer.append("订单编码与发货订单编码不相同,");
			}

			//如果有必填为空则记录错误返回下一次循环
			if(stringBuffer.length()>0){
				errorMap=rowMap;
				errorMap.put("5",stringBuffer.toString().replace(stringBuffer.charAt(stringBuffer.length() - 1) + "", "。"));
				errorList.add(errorMap);
				continue;
			}else {
				//正常订单==1补货订单==2
				if(orderDeliveryDto.getOrderType()==1){
					//验证订单号与商品编码是否存在，都存在则根据商品编码记录批次数量
					Order order=orderMapper.getOrderbyFlowId(rowMap.get("1"));
					if(UtilHelper.isEmpty(order)){
						stringBuffer.append("订单编号不存在,");
					}else {
						orderId=order.getOrderId();
						OrderDetail orderDetail=new OrderDetail();
						orderDetail.setOrderId(orderId);
						orderDetail.setProductCode(rowMap.get("2"));
						orderDetail.setSupplyId(orderDeliveryDto.getUserDto().getCustId());
						List detailList = orderDetailMapper.listByProperty(orderDetail);
						if(UtilHelper.isEmpty(detailList)||detailList.size()==0){
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
				}else{
					//验证订单号与商品编码是否存在，都存在则根据商品编码记录批次数量
					OrderException  orderException=orderExceptionMapper.getByExceptionOrderId(rowMap.get("1"));

					if(UtilHelper.isEmpty(orderException)){
						stringBuffer.append("订单编号不存在,");
					}else {
						orderId=orderException.getOrderId();
						OrderDetail orderDetail=new OrderDetail();
						orderDetail.setOrderId(orderId);
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
		}

		//验证商品数量是否相同
			if(orderDeliveryDto.getOrderType()==1){
				for (String code : codeMap.keySet()){
					OrderDetail orderDetail=new OrderDetail();
					orderDetail.setOrderId(orderId);
					orderDetail.setProductCode(code);
					orderDetail.setSupplyId(orderDeliveryDto.getUserDto().getCustId());
					List<OrderDetail> detailList = orderDetailMapper.listByProperty(orderDetail);
					if(detailList.size()>0){
						orderDetail=detailList.get(0);
						detailMap.put(code,orderDetail.getOrderDetailId());
						if(orderDetail.getProductCount().intValue()!=Integer.parseInt(codeMap.get(code))){
							for(Map<String,String> rowMap:list){
								if(rowMap.get("2").equals(code)){
									errorMap=rowMap;
									errorMap.put("5","商品编码为"+code+"的商品导入数量不等于采购数量");
									errorList.add(errorMap);
								}
							}
						}
					}
				}
			}else {
				for (String code : codeMap.keySet()){
					Map<String,Integer> returnMap=new HashMap<String, Integer>();
					OrderReturn orderReturn=new OrderReturn();
					orderReturn.setProductCode(code);
					orderReturn.setExceptionOrderId(orderDeliveryDto.getFlowId());
					orderReturn.setReturnType("3");
					List<OrderReturn> returnList = orderReturnMapper.listByProperty(orderReturn);
					if(returnList.size()>0) {
						detailMap.put(code, returnList.get(0).getOrderDetailId());
						for (OrderReturn or : returnList) {

							if (UtilHelper.isEmpty(returnMap.get(or.getProductCode()))) {
								returnMap.put(or.getProductCode(), or.getReturnCount());
							} else {
								returnMap.put(or.getProductCode(), or.getReturnCount() + returnMap.get(or.getProductCode()));
							}
						}
						if (returnMap.get(code).intValue() != Integer.parseInt(codeMap.get(code))) {
							for(Map<String,String> rowMap:list){
								if(rowMap.get("2").equals(code)){
									errorMap=rowMap;
									errorMap.put("5","商品编码为"+code+"的商品导入数量不等于采购数量");
									errorList.add(errorMap);
								}
							}
						}
					}
				}
			}

			//生成excel和订单发货信息
			filePath=createOrderdeliverDetail(errorList,orderDeliveryDto,list,detailMap,excelPath,now);

			//更新发货信息 更新日志表
			updateOrderDelivery(errorList,orderDeliveryDto,map,excelPath,now,filePath);

		}catch (Exception e){
			map.put("code", "0");
			map.put("msg", "Excel读取出错");
			log.info(e.getMessage());
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
	public String createOrderdeliverDetail(List<Map<String,String>> errorList,OrderDeliveryDto orderDeliveryDto,List<Map<String,String>> list,Map<String,Integer> detailMap,String excelPath,String now) {
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
			orderDeliveryDetail.setFlowId(orderDeliveryDto.getFlowId());
			List<OrderDeliveryDetail> orderDeliveryDetails = orderDeliveryDetailMapper.listByProperty(orderDeliveryDetail);
			if (orderDeliveryDetails.size() > 0) {
				orderDeliveryDetail = orderDeliveryDetails.get(0);
				orderDeliveryDetail.setOrderId(orderDeliveryDto.getOrderId());
				orderDeliveryDetail.setUpdateTime(now);
				orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetail.setDeliveryStatus(0);
				orderDeliveryDetail.setImportFileUrl(filePath);
				orderDeliveryDetailMapper.update(orderDeliveryDetail);
			} else {
				orderDeliveryDetail.setOrderId(orderDeliveryDto.getOrderId());
				orderDeliveryDetail.setCreateTime(now);
				orderDeliveryDetail.setUpdateTime(now);
				orderDeliveryDetail.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetail.setDeliveryStatus(0);
				orderDeliveryDetail.setImportFileUrl(filePath);
				orderDeliveryDetailMapper.save(orderDeliveryDetail);
			}
			return filePath;
		} else {
			OrderDeliveryDetail orderdel = new OrderDeliveryDetail();
			orderdel.setFlowId(orderDeliveryDto.getFlowId());
			List<OrderDeliveryDetail> orderDeliveryDetails = orderDeliveryDetailMapper.listByProperty(orderdel);
			if (orderDeliveryDetails.size() > 0) {
				orderDeliveryDetailMapper.deleteByPK(orderDeliveryDetails.get(0).getOrderDeliveryDetailId());
			}
			int i = 1;
			for (Map<String, String> rowMap : list) {
				OrderDeliveryDetail orderDeliveryDetail = new OrderDeliveryDetail();
				orderDeliveryDetail.setOrderLineNo(createOrderLineNo(i, orderDeliveryDto.getFlowId()));
				orderDeliveryDetail.setOrderId(orderDeliveryDto.getOrderId());
				orderDeliveryDetail.setFlowId(orderDeliveryDto.getFlowId());
				orderDeliveryDetail.setDeliveryStatus(1);
				orderDeliveryDetail.setBatchNumber(rowMap.get("3"));
				orderDeliveryDetail.setOrderDetailId(detailMap.get(rowMap.get("2")));
				orderDeliveryDetail.setDeliveryProductCount(Integer.parseInt(rowMap.get("4")));
				orderDeliveryDetail.setImportFileUrl(excelPath);
				orderDeliveryDetail.setCreateTime(now);
				orderDeliveryDetail.setUpdateTime(now);
				orderDeliveryDetail.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				if(orderDeliveryDto.getOrderType()==2)
					orderDeliveryDetail.setRecieveCount(orderDeliveryDetail.getDeliveryProductCount());
				orderDeliveryDetailMapper.save(orderDeliveryDetail);
				i++;
			}
		}
		return filePath;
	}

	/*生产excel和订单发货信息 */
	public String createOrderdeliverDetailReturn(List<Map<String,String>> errorList,OrderDeliveryDto orderDeliveryDto,List<Map<String,String>> list,Map<String,OrderReturn> detailMap,String excelPath,String now) {
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
			orderDeliveryDetail.setFlowId(orderDeliveryDto.getFlowId());
			List<OrderDeliveryDetail> orderDeliveryDetails = orderDeliveryDetailMapper.listByProperty(orderDeliveryDetail);
			if (orderDeliveryDetails.size() > 0) {
				orderDeliveryDetail = orderDeliveryDetails.get(0);
				orderDeliveryDetail.setOrderId(orderDeliveryDto.getOrderId());
				orderDeliveryDetail.setUpdateTime(now);
				orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetail.setDeliveryStatus(0);
				orderDeliveryDetail.setImportFileUrl(filePath);
				orderDeliveryDetailMapper.update(orderDeliveryDetail);
			} else {
				orderDeliveryDetail.setOrderId(orderDeliveryDto.getOrderId());
				orderDeliveryDetail.setCreateTime(now);
				orderDeliveryDetail.setUpdateTime(now);
				orderDeliveryDetail.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetail.setDeliveryStatus(0);
				orderDeliveryDetail.setImportFileUrl(filePath);
				orderDeliveryDetailMapper.save(orderDeliveryDetail);
			}
			return filePath;
		} else {
			OrderDeliveryDetail orderdel = new OrderDeliveryDetail();
			orderdel.setFlowId(orderDeliveryDto.getFlowId());
			List<OrderDeliveryDetail> orderDeliveryDetails = orderDeliveryDetailMapper.listByProperty(orderdel);
			if (orderDeliveryDetails.size() > 0) {
				orderDeliveryDetailMapper.deleteByPK(orderDeliveryDetails.get(0).getOrderDeliveryDetailId());
			}
			for (Map<String, String> rowMap : list) {
				int i = 1;
				OrderDeliveryDetail orderDeliveryDetail = new OrderDeliveryDetail();
				OrderReturn orderReturn = detailMap.get(rowMap.get("2"));
				orderDeliveryDetail.setOrderLineNo(createOrderLineNo(i, orderDeliveryDto.getFlowId()));
				orderDeliveryDetail.setOrderId(orderReturn.getOrderId());
				orderDeliveryDetail.setFlowId(orderDeliveryDto.getFlowId());
				orderDeliveryDetail.setDeliveryStatus(1);
				orderDeliveryDetail.setBatchNumber(rowMap.get("3"));
				orderDeliveryDetail.setOrderDetailId(orderReturn.getOrderDetailId());
				orderDeliveryDetail.setDeliveryProductCount(Integer.parseInt(rowMap.get("4")));
				orderDeliveryDetail.setImportFileUrl(excelPath);
				orderDeliveryDetail.setCreateTime(now);
				orderDeliveryDetail.setUpdateTime(now);
				orderDeliveryDetail.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				if(orderDeliveryDto.getOrderType()==2){
					orderDeliveryDetail.setRecieveCount(orderDeliveryDetail.getDeliveryProductCount());
					orderDeliveryDetail.setCanReturnCount(orderDeliveryDetail.getDeliveryProductCount());
				}
				orderDeliveryDetailMapper.save(orderDeliveryDetail);
				i++;
			}
		}
		return filePath;
	}

	//更新发货信息 更新日志表
	public void updateOrderDelivery(List<Map<String,String>> errorList,OrderDeliveryDto orderDeliveryDto,Map<String,String> map,String excelPath,String now,String filePath) throws Exception{

		if(errorList.size()>0){
			map.put("code", "2");
			map.put("msg","发货失败。");
			map.put("fileName",filePath);
		}else {
			//发货成功更新订单状态
			if(orderDeliveryDto.getOrderType()==1){
				Order order = orderMapper.getOrderbyFlowId(orderDeliveryDto.getFlowId());
				order.setOrderStatus(SystemOrderStatusEnum.SellerDelivered.getType());
				order.setDeliverTime(now);
				order.setUpdateTime(now);
				order.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				orderMapper.update(order);

				//插入日志表
				OrderTrace orderTrace = new OrderTrace();
				orderTrace.setOrderId(order.getOrderId());
				orderTrace.setNodeName("卖家已发货");
				orderTrace.setDealStaff(orderDeliveryDto.getUserDto().getUserName());
				orderTrace.setRecordDate(now);
				orderTrace.setRecordStaff(orderDeliveryDto.getUserDto().getUserName());
				orderTrace.setOrderStatus(order.getOrderStatus());
				orderTrace.setCreateTime(now);
				orderTrace.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
				orderTraceMapper.save(orderTrace);

				//生成发货信息
				UsermanageReceiverAddress receiverAddress=receiverAddressMapper.getByPK(orderDeliveryDto.getReceiverAddressId());
				OrderDelivery orderDelivery = orderDeliveryMapper.getByFlowId(orderDeliveryDto.getFlowId());
				orderDelivery.setDeliveryMethod(orderDeliveryDto.getDeliveryMethod());
				orderDelivery.setDeliveryContactPerson(orderDeliveryDto.getDeliveryContactPerson());
				orderDelivery.setDeliveryExpressNo(orderDeliveryDto.getDeliveryExpressNo());
				orderDelivery.setDeliveryDate(orderDeliveryDto.getDeliveryDate());
				orderDelivery.setUpdateDate(now);
				orderDelivery.setDeliveryAddress(receiverAddress.getProvinceName() + receiverAddress.getCityName() + receiverAddress.getDistrictName() + receiverAddress.getAddress());
				orderDelivery.setDeliveryPerson(receiverAddress.getReceiverName());
				orderDelivery.setDeliveryContactPhone(receiverAddress.getContactPhone());
				orderDelivery.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDelivery.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDelivery.setUpdateTime(now);
				orderDelivery.setCreateTime(now);
				orderDeliveryMapper.update(orderDelivery);
				//发货调用扣减冻结库存
				OrderDetail orderDetail=new OrderDetail();
				orderDetail.setOrderId(order.getOrderId());
				orderDetail.setSupplyId(orderDeliveryDto.getUserDto().getCustId());
				List<OrderDetail> detailList=orderDetailMapper.listByProperty(orderDetail);
				productInventoryManage.deductionInventory(detailList,orderDeliveryDto.getUserDto().getUserName());
			}else {
				//更新异常订单
				OrderException  orderException=orderExceptionMapper.getByExceptionOrderId(orderDeliveryDto.getFlowId());
				orderException.setOrderStatus(SystemReplenishmentOrderStatusEnum.SellerDelivered.getType());
				orderException.setDeliverTime(now);
				orderException.setUpdateTime(now);
				orderException.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				orderExceptionMapper.update(orderException);
				//插入日志表
				OrderTrace orderTrace = new OrderTrace();
				orderTrace.setOrderId(orderException.getOrderId());
				orderTrace.setNodeName("补货卖家已发货");
				orderTrace.setDealStaff(orderDeliveryDto.getUserDto().getUserName());
				orderTrace.setRecordDate(now);
				orderTrace.setRecordStaff(orderDeliveryDto.getUserDto().getUserName());
				orderTrace.setOrderStatus(orderException.getOrderStatus());
				orderTrace.setCreateTime(now);
				orderTrace.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
				orderTraceMapper.save(orderTrace);
				//生成发货信息
				UsermanageReceiverAddress receiverAddress=receiverAddressMapper.getByPK(orderDeliveryDto.getReceiverAddressId());
				//更具原订单发货信息生成新的异常订单发货信息
				OrderDelivery orderDelivery = orderDeliveryMapper.getByFlowId(orderException.getFlowId());
				orderDelivery.setOrderId(orderException.getExceptionId());
				orderDelivery.setFlowId(orderException.getExceptionOrderId());
				orderDelivery.setDeliveryMethod(orderDeliveryDto.getDeliveryMethod());
				orderDelivery.setDeliveryContactPerson(orderDeliveryDto.getDeliveryContactPerson());
				orderDelivery.setDeliveryExpressNo(orderDeliveryDto.getDeliveryExpressNo());
				orderDelivery.setDeliveryDate(orderDeliveryDto.getDeliveryDate());
				orderDelivery.setUpdateDate(now);
				orderDelivery.setDeliveryAddress(receiverAddress.getProvinceName() + receiverAddress.getCityName() + receiverAddress.getDistrictName() + receiverAddress.getAddress());
				orderDelivery.setDeliveryPerson(receiverAddress.getReceiverName());
				orderDelivery.setDeliveryContactPhone(receiverAddress.getContactPhone());
				orderDelivery.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDelivery.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
				orderDelivery.setCreateTime(now);
				orderDelivery.setDeliveryId(null);
				orderDelivery.setUpdateTime(now);
				orderDeliveryMapper.save(orderDelivery);

			}


			map.put("code","1");
			map.put("msg","发货成功。");
			map.put("fileName",excelPath);
		}
	}

    //更新发货信息 更新日志表
    public void updateOrderDeliveryReturn(List<Map<String,String>> errorList,OrderDeliveryDto orderDeliveryDto,Map<String,String> map,String excelPath,String now,String filePath){

        if(errorList.size()>0){
            map.put("code", "2");
            map.put("msg","发货失败。");
            map.put("fileName",filePath);
        }else {
            //更新异常订单
            OrderException  orderException=orderExceptionMapper.getByExceptionOrderId(orderDeliveryDto.getFlowId());
            orderException.setOrderStatus(SystemChangeGoodsOrderStatusEnum.WaitingBuyerReceived.getType());
            orderException.setDeliverTime(now);
            orderException.setUpdateTime(now);
            orderException.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
            orderExceptionMapper.update(orderException);
            //插入日志表
            OrderTrace orderTrace = new OrderTrace();
            orderTrace.setOrderId(orderException.getOrderId());
            orderTrace.setNodeName("补货卖家已发货");
            orderTrace.setDealStaff(orderDeliveryDto.getUserDto().getUserName());
            orderTrace.setRecordDate(now);
            orderTrace.setRecordStaff(orderDeliveryDto.getUserDto().getUserName());
            orderTrace.setOrderStatus(orderException.getOrderStatus());
            orderTrace.setCreateTime(now);
            orderTrace.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
            orderTraceMapper.save(orderTrace);
            //生成发货信息
            UsermanageReceiverAddress receiverAddress=receiverAddressMapper.getByPK(orderDeliveryDto.getReceiverAddressId());
            //更具原订单发货信息生成新的异常订单发货信息
            OrderDelivery orderDelivery = orderDeliveryMapper.getByFlowId(orderException.getFlowId());
            orderDelivery.setOrderId(orderException.getExceptionId());
            orderDelivery.setFlowId(orderException.getExceptionOrderId());
            orderDelivery.setDeliveryMethod(orderDeliveryDto.getDeliveryMethod());
            orderDelivery.setDeliveryContactPerson(orderDeliveryDto.getDeliveryContactPerson());
            orderDelivery.setDeliveryExpressNo(orderDeliveryDto.getDeliveryExpressNo());
            orderDelivery.setDeliveryDate(orderDeliveryDto.getDeliveryDate());
            orderDelivery.setUpdateDate(now);
            orderDelivery.setDeliveryAddress(receiverAddress.getProvinceName() + receiverAddress.getCityName() + receiverAddress.getDistrictName() + receiverAddress.getAddress());
            orderDelivery.setDeliveryPerson(receiverAddress.getReceiverName());
            orderDelivery.setDeliveryContactPhone(receiverAddress.getContactPhone());
            orderDelivery.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
            orderDelivery.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
            orderDelivery.setCreateTime(now);
            orderDelivery.setDeliveryId(null);
            orderDelivery.setUpdateTime(now);
            orderDeliveryMapper.save(orderDelivery);

            map.put("code","1");
            map.put("msg","发货成功。");
            map.put("fileName",excelPath);
        }
    }

	public List<UsermanageReceiverAddress> getReceiveAddressList(UserDto user){
		UsermanageReceiverAddress receiverAddress = new UsermanageReceiverAddress();
		receiverAddress.setEnterpriseId(String.valueOf(user.getCustId()));
		List<UsermanageReceiverAddress> receiverAddressList = receiverAddressMapper.listByProperty(receiverAddress);
		return receiverAddressList;
	}
	/**
	 * 退货，买家确认发货
	 * @param orderDeliveryDto
	 * @return
	 * @throws Exception
	 */

	public Map updateOrderDeliveryForRefund(OrderDeliveryDto orderDeliveryDto) throws Exception{
		Map<String,String> map=new HashMap<String, String>();
		if(UtilHelper.isEmpty(orderDeliveryDto)){
			map.put("code", "0");
			map.put("msg", "发货信息不能为空");
			return map;
		}
		if(UtilHelper.isEmpty(orderDeliveryDto.getFlowId())){
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

		OrderException orderException = orderExceptionMapper.getByPK(Integer.parseInt(orderDeliveryDto.getFlowId()));
		if(UtilHelper.isEmpty(orderException)){
			map.put("code", "0");
			map.put("msg", "参数有误");
			return map;
		}
		Order order =  orderMapper.getByPK(orderException.getOrderId());
		if(UtilHelper.isEmpty(order)){
			map.put("code", "0");
			map.put("msg", "订单不存在");
			return map;
		}
		OrderDelivery od = orderDeliveryMapper.getByFlowId(order.getFlowId());
		if(UtilHelper.isEmpty(order)){
			map.put("code", "0");
			map.put("msg", "订单发货信息不存在");
			return map;
		}

		orderDeliveryDto.setOrderId(orderException.getExceptionId());
		String now = systemDateMapper.getSystemDate();
		//生成发货信息
		UsermanageReceiverAddress receiverAddress=receiverAddressMapper.getByPK(orderDeliveryDto.getReceiverAddressId());
		OrderDelivery  orderDelivery = new OrderDelivery();
		orderDelivery.setOrderId(orderException.getExceptionId());
		orderDelivery.setFlowId(orderException.getExceptionOrderId());
		orderDelivery.setDeliveryMethod(orderDeliveryDto.getDeliveryMethod());//配送方式
		orderDelivery.setDeliveryContactPerson(orderDeliveryDto.getDeliveryContactPerson());//发货联系人或第三方物流公司名称
		orderDelivery.setDeliveryExpressNo(orderDeliveryDto.getDeliveryExpressNo());//第三该物流单号或发货联系人电话
		orderDelivery.setDeliveryDate(orderDeliveryDto.getDeliveryDate());//预计送达时间
		orderDelivery.setDeliveryAddress(receiverAddress.getProvinceName() + receiverAddress.getCityName() + receiverAddress.getDistrictName() + receiverAddress.getAddress());
		orderDelivery.setDeliveryPerson(receiverAddress.getReceiverName());
		orderDelivery.setDeliveryContactPhone(receiverAddress.getContactPhone());
		orderDelivery.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
		orderDelivery.setCreateTime(now);
		orderDelivery.setReceivePerson(od.getDeliveryPerson());
		orderDelivery.setReceiveAddress(od.getDeliveryAddress());
		orderDelivery.setReceiveContactPhone(od.getDeliveryContactPhone());
		orderDeliveryMapper.save(orderDelivery);
		orderException.setUpdateTime(now);
		orderException.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
		orderException.setOrderStatus(SystemRefundOrderStatusEnum.BuyerDelivered.getType());
		orderExceptionMapper.update(orderException);
		map.put("code","1");
		map.put("msg", "发货成功。");
		return map;
	}


	/**
	 * 换货，买家确认发货
	 * @param orderDeliveryDto
	 * @return
	 * @throws Exception
	 */

	public Map updateOrderDeliveryForChange(OrderDeliveryDto orderDeliveryDto) throws Exception{
		Map<String,String> map=new HashMap<String, String>();
		if(UtilHelper.isEmpty(orderDeliveryDto)){
			map.put("code", "0");
			map.put("msg", "发货信息不能为空");
			return map;
		}
		if(UtilHelper.isEmpty(orderDeliveryDto.getFlowId())){
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

		OrderException orderException = orderExceptionMapper.getByPK(Integer.parseInt(orderDeliveryDto.getFlowId()));
		if(UtilHelper.isEmpty(orderException)){
			map.put("code", "0");
			map.put("msg", "参数有误");
			return map;
		}
		Order order =  orderMapper.getByPK(orderException.getOrderId());
		if(UtilHelper.isEmpty(order)){
			map.put("code", "0");
			map.put("msg", "订单不存在");
			return map;
		}
		OrderDelivery od = orderDeliveryMapper.getByFlowId(order.getFlowId());
		if(UtilHelper.isEmpty(order)){
			map.put("code", "0");
			map.put("msg", "订单发货信息不存在");
			return map;
		}

		orderDeliveryDto.setOrderId(orderException.getExceptionId());
		String now = systemDateMapper.getSystemDate();
		//生成发货信息
		UsermanageReceiverAddress receiverAddress=receiverAddressMapper.getByPK(orderDeliveryDto.getReceiverAddressId());
		OrderDelivery  orderDelivery = new OrderDelivery();
		orderDelivery.setOrderId(orderException.getExceptionId());
		orderDelivery.setFlowId(orderException.getExceptionOrderId());
		orderDelivery.setDeliveryMethod(orderDeliveryDto.getDeliveryMethod());//配送方式
		orderDelivery.setDeliveryContactPerson(orderDeliveryDto.getDeliveryContactPerson());//发货联系人或第三方物流公司名称
		orderDelivery.setDeliveryExpressNo(orderDeliveryDto.getDeliveryExpressNo());//第三该物流单号或发货联系人电话
		orderDelivery.setDeliveryDate(orderDeliveryDto.getDeliveryDate());//预计送达时间
		orderDelivery.setDeliveryAddress(receiverAddress.getProvinceName() + receiverAddress.getCityName() + receiverAddress.getDistrictName() + receiverAddress.getAddress());
		orderDelivery.setDeliveryPerson(receiverAddress.getReceiverName());
		orderDelivery.setDeliveryContactPhone(receiverAddress.getContactPhone());
		orderDelivery.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
		orderDelivery.setCreateTime(now);
		orderDelivery.setReceivePerson(od.getDeliveryPerson());
		orderDelivery.setReceiveAddress(od.getDeliveryAddress());
		orderDelivery.setReceiveContactPhone(od.getDeliveryContactPhone());
		orderDeliveryMapper.save(orderDelivery);
		orderException.setUpdateTime(now);
		orderException.setBuyerDeliverTime(now);
		orderException.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
		orderException.setOrderStatus(SystemChangeGoodsOrderStatusEnum.WaitingSellerReceived.getType());
		orderExceptionMapper.update(orderException);
		map.put("code","1");
		map.put("msg", "发货成功。");
		return map;
	}

	public Map updateSendOrderDeliveryReturn(OrderDeliveryDto orderDeliveryDto) throws Exception{
		Map<String,String> map=new HashMap<String, String>();
		if(UtilHelper.isEmpty(orderDeliveryDto)){
			map.put("code", "0");
			map.put("msg", "发货信息不能为空");
			return map;
		}
		if(UtilHelper.isEmpty(orderDeliveryDto.getFlowId())){
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

		//正常下单根据orderId查询订单收发货信息是否存在,更新发货信息

		OrderException orderException = orderExceptionMapper.getByPK(Integer.parseInt(orderDeliveryDto.getFlowId()));
		if(UtilHelper.isEmpty(orderException)){
			map.put("code", "0");
			map.put("msg", "订单不存在");
			return map;
		}
		//orderDeliveryDto.setOrderId(orderDelivery.getOrderId());
        orderDeliveryDto.setFlowId(orderException.getExceptionOrderId());

		//验证批次号并生成订单发货数据
		readExcelOrderDeliveryDetailReturn(orderDeliveryDto.getPath()+orderDeliveryDto.getFileName(),map,orderDeliveryDto);

		return map;
	}

	//读取验证订单批次信息excel
	public Map<String,String> readExcelOrderDeliveryDetailReturn(String excelPath,Map<String,String> map,OrderDeliveryDto orderDeliveryDto){
		String now = systemDateMapper.getSystemDate();
		List<Map<String,String>> errorList=new ArrayList<Map<String, String>>();
		Map<String,String> errorMap=null;
		Map<String,String> codeMap=new HashMap<String, String>();
		Map <String,OrderReturn> detailMap=new HashMap<String, OrderReturn>();
		//原订单id
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
					//正常订单==1补货订单==2
					if(orderDeliveryDto.getOrderType()==2){
						//验证订单号与商品编码是否存在，都存在则根据商品编码记录批次数量
						OrderException  orderException=orderExceptionMapper.getByExceptionOrderId(rowMap.get("1"));

						if(UtilHelper.isEmpty(orderException)){
							stringBuffer.append("订单编号不存在,");
						}else {
							orderId=orderException.getOrderId();
							OrderDetail orderDetail=new OrderDetail();
							orderDetail.setOrderId(orderId);
							orderDetail.setProductCode(rowMap.get("2"));
							orderDetail.setSupplyId(orderDeliveryDto.getUserDto().getCustId());
							List detailList = orderDetailMapper.listByProperty(orderDetail);
							if(detailList.size()==0){
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
			}

			//验证商品数量是否相同

			for (String code : codeMap.keySet()){
				Map<String,Integer> returnMap=new HashMap<String, Integer>();
				OrderReturn orderReturn=new OrderReturn();
				orderReturn.setProductCode(code);
				orderReturn.setExceptionOrderId(orderDeliveryDto.getFlowId());
				orderReturn.setReturnType("2");
				List<OrderReturn> returnList = orderReturnMapper.listByProperty(orderReturn);
				if(returnList.size()>0) {
					detailMap.put(code, returnList.get(0));
					for (OrderReturn or : returnList) {

						if (UtilHelper.isEmpty(returnMap.get(or.getProductCode()))) {
							returnMap.put(or.getProductCode(), or.getReturnCount());
						} else {
							returnMap.put(or.getProductCode(), or.getReturnCount() + returnMap.get(or.getProductCode()));
						}
					}
					if (returnMap.get(code) != Integer.parseInt(codeMap.get(code))) {
						errorMap = new HashMap<String, String>();
						errorMap.put("5", "商品编码为" + code + "的商品导入数量不等于换货数量");
						errorList.add(errorMap);
					}
				}
			}

			//生成excel和订单发货信息
			filePath=createOrderdeliverDetailReturn(errorList,orderDeliveryDto,list,detailMap,excelPath,now);

			//更新发货信息 更新日志表
            updateOrderDeliveryReturn(errorList,orderDeliveryDto,map,excelPath,now,filePath);

		}catch (Exception e){
			map.put("code", "0");
			map.put("msg", "Excel读取出错");
			log.info(e.getMessage());
		}
		return map;
	}

	/**
	 * 供应商发货
	 *
	 * @param manufacturerOrderList
	 * @return
	 */
	public List<ManufacturerOrder> updateOrderDeliver(List<ManufacturerOrder> manufacturerOrderList) {
		if (!UtilHelper.isEmpty(manufacturerOrderList)) {
			String now = systemDateMapper.getSystemDate();
			List<ManufacturerOrder> list = new ArrayList<ManufacturerOrder>();
			for (ManufacturerOrder manufacturerOrder : manufacturerOrderList) {
				if (!UtilHelper.isEmpty(manufacturerOrder.getSupplyId()) && !UtilHelper.isEmpty(manufacturerOrder.getDeliverTime()) && !UtilHelper.isEmpty(manufacturerOrder.getOrderStatus()) && !UtilHelper.isEmpty(manufacturerOrder.getDeliveryMethod())) {
					Order order = orderMapper.getOrderbyFlowId(manufacturerOrder.getFlowId());
					if (UtilHelper.isEmpty(order)) {
						list.add(manufacturerOrder);
						log.info("该订单" + manufacturerOrder.getFlowId() + "为空");
						continue;
					}
					if (!manufacturerOrder.getSupplyId().equals(order.getSupplyId())) {
						list.add(manufacturerOrder);
						log.info("该订单" + manufacturerOrder.getFlowId() + "不是该" + manufacturerOrder.getSupplyId() + "供应商");
						continue;
					}
					if (!SystemOrderStatusEnum.BuyerAlreadyPaid.getType().equals(order.getOrderStatus())) {
						list.add(manufacturerOrder);
						log.info("该订单" + manufacturerOrder.getFlowId() + "不是买家已付款状态");
						continue;
					}
					List<OrderDetail> orderDetailList = orderDetailMapper.listOrderDetailInfoByOrderId(order.getOrderId());
					if (UtilHelper.isEmpty(orderDetailList)) {
						list.add(manufacturerOrder);
						log.info("该订单" + manufacturerOrder.getFlowId() + "的商品信息为空");
						continue;
					}
					String path = writeExcel(orderDetailList, order.getFlowId(), "/opt/deploy/order-web/include/excel/");
					int i = 1;
					for (OrderDetail orderDetail : orderDetailList) {
						OrderDeliveryDetail orderDeliveryDetail = new OrderDeliveryDetail();
						orderDeliveryDetail.setOrderLineNo(createOrderLineNo(i, order.getFlowId()));
						orderDeliveryDetail.setOrderId(order.getOrderId());
						orderDeliveryDetail.setFlowId(order.getFlowId());
						orderDeliveryDetail.setDeliveryStatus(1);
						orderDeliveryDetail.setBatchNumber("1001");
						orderDeliveryDetail.setOrderDetailId(orderDetail.getOrderDetailId());
						orderDeliveryDetail.setDeliveryProductCount(orderDetail.getProductCount());
						orderDeliveryDetail.setImportFileUrl(path);
						orderDeliveryDetail.setCreateTime(now);
						orderDeliveryDetail.setUpdateTime(now);
						orderDeliveryDetail.setCreateUser(manufacturerOrder.getSupplyName());
						orderDeliveryDetail.setUpdateUser(manufacturerOrder.getSupplyName());
						orderDeliveryDetailMapper.save(orderDeliveryDetail);
						i++;
					}
					//修改发货人地址
					UsermanageReceiverAddress receiverAddress = receiverAddressMapper.findByEnterpriseId(manufacturerOrder.getSupplyId().toString());  //根据供应商编码查询最新的地址
					OrderDelivery orderDelivery = orderDeliveryMapper.getByFlowId(order.getFlowId());
					orderDelivery.setDeliveryMethod(manufacturerOrder.getDeliveryMethod());
					if (manufacturerOrder.getDeliveryMethod() == 1) {
						orderDelivery.setDeliveryContactPerson(receiverAddress.getReceiverName());
						orderDelivery.setDeliveryExpressNo(receiverAddress.getContactPhone());
					}
					orderDelivery.setDeliveryDate(manufacturerOrder.getDeliveryDate());
					orderDelivery.setUpdateDate(now);
					orderDelivery.setDeliveryAddress(receiverAddress.getProvinceName() + receiverAddress.getCityName() + receiverAddress.getDistrictName() + receiverAddress.getAddress());
					orderDelivery.setDeliveryPerson(receiverAddress.getReceiverName());
					orderDelivery.setDeliveryContactPhone(receiverAddress.getContactPhone());
					orderDelivery.setUpdateUser(manufacturerOrder.getSupplyName());
					orderDelivery.setUpdateTime(now);
					orderDeliveryMapper.update(orderDelivery);
					//修改订单状态
					order.setOrderStatus(SystemOrderStatusEnum.SellerDelivered.getType());
					order.setDeliverTime(manufacturerOrder.getDeliverTime());
					order.setUpdateTime(now);
					order.setUpdateUser(manufacturerOrder.getSupplyName());
					orderMapper.update(order);
					//插入日志表
					OrderTrace orderTrace = new OrderTrace();
					orderTrace.setOrderId(order.getOrderId());
					orderTrace.setNodeName("卖家已发货");
					orderTrace.setDealStaff(manufacturerOrder.getSupplyName());
					orderTrace.setRecordDate(now);
					orderTrace.setRecordStaff(manufacturerOrder.getSupplyName());
					orderTrace.setOrderStatus(order.getOrderStatus());
					orderTrace.setCreateTime(now);
					orderTrace.setCreateUser(manufacturerOrder.getSupplyName());
					orderTraceMapper.save(orderTrace);
					//扣减冻结库存(发货)
					productInventoryManage.deductionInventory(orderDetailList, manufacturerOrder.getSupplyName());
				} else {
					list.add(manufacturerOrder);
				}
			}
			return list;
		}
		return null;
	}

	/**
	 * 供应商发货商品信息写入Excel
	 *
	 * @param orderDetailList
	 * @param flowId
	 * @param path
	 * @return
	 */
	public static String writeExcel(List<OrderDetail> orderDetailList, String flowId, String path) {
		//创建路径
		File fileUrl = new File(path);
		if (!fileUrl.exists()) {
			fileUrl.mkdirs();
		}
		String[] headers = new String[]{"序号", "订单编号", "商品编码", "批号", "数量"};
		List<Object[]> dataset = new ArrayList<Object[]>();
		int i = 1;
		for (OrderDetail orderDetail : orderDetailList) {
			dataset.add(new Object[]{i, flowId, orderDetail.getProductCode(), orderDetail.getSpuCode(), orderDetail.getProductCount()});
			i++;
		}
		String fileName = ExcelUtil.downloadExcel("发货批号导入信息", headers, dataset, path);
		return path + fileName;
	}
}