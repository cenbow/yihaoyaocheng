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

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderCombined;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.OrderPayStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.mapper.OrderCombinedMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.OrderPayDto;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.pay.chinapay.httpClient.HttpRequestHandler;
import com.yyw.yhyc.pay.chinapay.utils.ChinaPayUtil;
import com.yyw.yhyc.pay.chinapay.utils.PayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.bo.Pagination;

@Service("orderPayService")
public class OrderPayService {

	private Log log = LogFactory.getLog(OrderPayService.class);

	private OrderPayMapper	orderPayMapper;
	private SystemPayTypeMapper systemPayTypeMapper;
	private AccountPayInfoMapper accountPayInfoMapper;

	private SystemDateMapper systemDateMapper;

	private OrderMapper orderMapper;

	private OrderCombinedMapper orderCombinedMapper;

	@Autowired
	public void setAccountPayInfoMapper(AccountPayInfoMapper accountPayInfoMapper) {
		this.accountPayInfoMapper = accountPayInfoMapper;
	}

	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}

	@Autowired
	public void setOrderPayMapper(OrderPayMapper orderPayMapper)
	{
		this.orderPayMapper = orderPayMapper;
	}

	@Autowired
	public void setOrderMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}
	@Autowired
	public void setOrderCombinedMapper(OrderCombinedMapper orderCombinedMapper) {
		this.orderCombinedMapper = orderCombinedMapper;
	}


	@Autowired
	public void setSystemPayTypeMapper(SystemPayTypeMapper systemPayTypeMapper) {
		this.systemPayTypeMapper = systemPayTypeMapper;
	}

	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public OrderPay getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return orderPayMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderPay> list() throws Exception
	{
		return orderPayMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<OrderPay> listByProperty(OrderPay orderPay)
			throws Exception
	{
		return orderPayMapper.listByProperty(orderPay);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<OrderPay> listPaginationByProperty(Pagination<OrderPay> pagination, OrderPay orderPay) throws Exception
	{
		List<OrderPay> list = orderPayMapper.listPaginationByProperty(pagination, orderPay);

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
		return orderPayMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		orderPayMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(OrderPay orderPay) throws Exception
	{
		return orderPayMapper.deleteByProperty(orderPay);
	}

	/**
	 * 保存记录
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public void save(OrderPay orderPay) throws Exception
	{
		orderPayMapper.save(orderPay);
	}

	/**
	 * 更新记录
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public int update(OrderPay orderPay) throws Exception
	{
		return orderPayMapper.update(orderPay);
	}

	/**
	 * 根据条件查询记录条数
	 * @param orderPay
	 * @return
	 * @throws Exception
	 */
	public int findByCount(OrderPay orderPay) throws Exception
	{
		return orderPayMapper.findByCount(orderPay);
	}

	/**
	 * 在线支付订单前，预处理订单数据
	 *
	 * @param userDto 当前登陆用户
	 * @param flowIds 订单编号
	 * @param payTypeId 支付方式id
	 * @param payFlowId 支付流水号
     * @return
     */
	public OrderPay preHandler(UserDto userDto, String flowIds, int payTypeId, String payFlowId) throws Exception {

		log.info("在线支付订单前，预处理订单数据:userDto=" + userDto + ",flowIds=" + ",payTypeId=" + payTypeId + ",payFlowId="+payFlowId);

		/* 过滤非法订单参数 */
		if(UtilHelper.isEmpty(userDto)) return null;
		if(UtilHelper.isEmpty(flowIds)) return null;
		if( OnlinePayTypeEnum.getPayName(payTypeId) == null ) return null;
		if(UtilHelper.isEmpty(payFlowId)) return null;

		/* 查询、校验、处理合法订单数据 */
		Order order = null;
		int orderCount = 0;//订单数量
		BigDecimal orderTotal = new BigDecimal(0);//订单金额(应付)
		BigDecimal freightTotal = new BigDecimal(0);//运费
		BigDecimal needToPayTotal = new BigDecimal(0);//需要支付的金额(实付)
		List<Integer> orderIdList = new ArrayList<>();

		String [] flowIdArray = flowIds.split(",");
		for(String s : flowIdArray){
			if(UtilHelper.isEmpty(s)) continue;
			order = orderMapper.getOrderbyFlowId(s.trim());
			if(UtilHelper.isEmpty(order)) continue;
			/* 过滤掉不是在线支付类型的订单 */
			if(OnlinePayTypeEnum.getPayName(order.getPayTypeId()) == null ){
				continue;
			}
			/* 过滤掉不是待付款的订单 */
			if(!SystemOrderStatusEnum.BuyerOrdered.getType().equals(order.getOrderStatus())){
				continue;
			}
			/* 过滤掉不是自己下的单 */
			if(UtilHelper.isEmpty(order.getCustId()) || !order.getCustId().equals(userDto.getCustId())){
				continue;
			}
			orderTotal = orderTotal.add(order.getOrderTotal());
			freightTotal = freightTotal.add(order.getFreight());
			needToPayTotal = needToPayTotal.add(order.getOrgTotal());
			orderCount++;
			orderIdList.add(order.getOrderId());
		}

		if(orderCount == 0 || orderIdList.size() == 0) return null;

		/* 插入 orderCombine表*/
		OrderCombined orderCombined = new OrderCombined();
		orderCombined.setPayTypeId(payTypeId);        //支付方式id
		orderCombined.setCustId(userDto.getCustId());
		orderCombined.setCustName(userDto.getCustName());
		orderCombined.setCombinedNumber(orderCount);  //合并支付的订单数量
		orderCombined.setCopeTotal(orderTotal);       //应付总额
		orderCombined.setPocketTotal(needToPayTotal); //实际支付总额
		orderCombined.setFreightPrice(freightTotal);  //运费
		orderCombined.setPayFlowId(payFlowId);        //支付流水号
		orderCombined.setCreateUser(userDto.getUserName());
		orderCombined.setCreateTime(systemDateMapper.getSystemDate());
		orderCombinedMapper.save(orderCombined);
		log.info("在线支付订单前，预处理订单数据:插入orderCombine数据=" + orderCombined);

		/* 回写order_combine_id到order表  */
		List<OrderCombined> orderCombinedList = orderCombinedMapper.listByProperty(orderCombined);
		if(UtilHelper.isEmpty(orderCombinedList) || orderCombinedList.size() != 1) throw new Exception("服务器异常!");
		orderCombined = orderCombinedList.get(0);
		for(Integer orderId : orderIdList){
			if(orderId <= 0) continue;
			order = new Order();
			order.setOrderId(orderId);
			order.setPayTypeId(payTypeId);
			order.setOrderCombinedId(orderCombined.getOrderCombinedId());
			orderMapper.update(order);
		}

		/* 插入 orderPay */
		OrderPay orderPay = new OrderPay();
		orderPay.setPayFlowId(payFlowId);
		orderPay.setPayTypeId(payTypeId);
		orderPay.setOrderMoney(needToPayTotal);//（实际上需要支付的）订单金额
		orderPay.setPayStatus(OrderPayStatusEnum.UN_PAYED.getPayStatus()); //支付状态：未支付
		orderPay.setCreateUser(userDto.getUserName());
		orderPay.setCreateTime(systemDateMapper.getSystemDate());
		log.info("在线支付订单前，预处理订单数据:插入orderPay数据=" + orderPay);
		orderPayMapper.save(orderPay);
		log.info("在线支付订单前，预处理订单数据:处理完成，返回数据=" + orderPay);
		return orderPay;
	}



	//支付调用支付接口
	public Map<String,Object> orderOnlinePay(OrderPay orderPay,UserDto userDto) throws Exception
	{
		if(UtilHelper.isEmpty(orderPay)){
			log.info("支付参数不能为空");
			throw new RuntimeException("参数不能为空");
		}

		if(UtilHelper.isEmpty(orderPay.getPayFlowId())){
			log.info("支付流水不能为空");
			throw new RuntimeException("支付流水不能为空");
		}

		if(UtilHelper.isEmpty(orderPay.getOrderPayId())){
			log.info("订单编号不能为空");
			throw new RuntimeException("参数不能为空");
		}

		SystemPayType systemPayType = systemPayTypeMapper.getByPK(orderPay.getPayTypeId());

		List<OrderPayDto> list = orderPayMapper.listOrderPayDtoByProperty(orderPay);

		if(UtilHelper.isEmpty(list)||list.size()==0||UtilHelper.isEmpty(systemPayType)){
			log.info("订单支付信息不存在");
			throw new RuntimeException("订单支付信息不存在");
		}

		return findPayMapByPayFlowId(orderPay.getPayFlowId(),systemPayType,list);
	}


	public Map<String,Object> findPayMapByPayFlowId(String payFlowId,SystemPayType systemPayType,List<OrderPayDto> list) throws Exception{
		Map<String,Object> map=new HashMap<String,Object>();

		SimpleDateFormat datefomet=new SimpleDateFormat("yyyyMMdd,HHmmss");
		Date date=new Date();
		String fDate=datefomet.format(date);
		String fromWhere="";

		//查询分账信息；
		StringBuffer MerSplitMsg=new StringBuffer();
		StringBuffer MerSpringCustomer=new StringBuffer("您的货款将在确认收货之后通过银联支付给 ");
		//如果供应商中有一个商户号有问题，就不能进行支付
		boolean isNoHaveMerId=false;

		for(int i=0;i<list.size();i++){
			OrderPayDto orderPayDto=list.get(i);
			if(!UtilHelper.isEmpty(orderPayDto)&&!UtilHelper.isEmpty(orderPayDto.getReceiveAccountNo())){
				if(i==0){
					MerSpringCustomer.append(orderPayDto.getReceiveAccountName());
					MerSplitMsg.append(orderPayDto.getReceiveAccountNo()+"^"+orderPayDto.getOrgTotal().multiply(new BigDecimal(100)).intValue());
				}else{
					MerSpringCustomer.append("、"+orderPayDto.getReceiveAccountName());
					MerSplitMsg.append(";"+orderPayDto.getReceiveAccountNo()+"^"+orderPayDto.getOrgTotal().multiply(new BigDecimal(100)).intValue());
				}
			}else{
				isNoHaveMerId=true;
			}
		}

		if(OnlinePayTypeEnum.UnionPayB2C.getPayType().intValue()==systemPayType.getPayType().intValue()){
			fromWhere=ChinaPayUtil.B2C;
		}else if(OnlinePayTypeEnum.UnionPayNoCard.getPayType().intValue()==systemPayType.getPayType().intValue()){
			fromWhere=ChinaPayUtil.NOCARD;
		}else{
			fromWhere=ChinaPayUtil.B2C;
		}

		OrderPay orderPay=orderPayMapper.getByPayFlowId(payFlowId);

		map.put("MerOrderNo", payFlowId);
		map.put("TranDate",fDate.split(",")[0]);
		map.put("TranTime", fDate.split(",")[1]);
		String OrderAmt=String.valueOf(orderPay.getOrderMoney().multiply(new BigDecimal(100)).intValue());
		map.put("OrderAmt", OrderAmt);
		map.put("MerPageUrl", PayUtil.getValue("payReturnHost") + "/buyerOrderManage.action");
		map.put("MerBgUrl", PayUtil.getValue("payReturnHost") + "/OrderCallBackPay.action");
		log.info(PayUtil.getValue("payReturnHost") + "/OrderCallBackPay.action");
		String CommodityMsg= HttpRequestHandler.bSubstring(MerSpringCustomer.toString(), 80);
		log.info("CommodityMsg="+CommodityMsg);
		map.put("CommodityMsg", CommodityMsg);

		if(isNoHaveMerId){
			map.put("MerSplitMsg","");
		}else{
			map.put("MerSplitMsg",MerSplitMsg.toString());
		}
		map.put("fromWhere", fromWhere);
		map=HttpRequestHandler.getSubmitFormMap(map);
		map=HttpRequestHandler.getSignMap(map);
		return map;
	}

}