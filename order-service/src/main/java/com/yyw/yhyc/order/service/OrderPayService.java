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
import java.util.List;
import java.util.Map;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.AccountPayInfo;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.OrderPayDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.OnlinePayTypeEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.mapper.*;
import com.yyw.yhyc.pay.chinapay.httpClient.HttpRequestHandler;
import com.yyw.yhyc.pay.chinapay.utils.ChinaPayUtil;
import com.yyw.yhyc.pay.chinapay.utils.PayUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.OrderPay;
import com.yyw.yhyc.bo.Pagination;

@Service("orderPayService")
public class OrderPayService {

	private OrderPayMapper	orderPayMapper;
	private OrderMapper orderMapper;
	private SystemPayTypeMapper systemPayTypeMapper;
	private SystemDateMapper systemDateMapper;
	private AccountPayInfoMapper accountPayInfoMapper;

	@Autowired
	public void setAccountPayInfoMapper(AccountPayInfoMapper accountPayInfoMapper) {
		this.accountPayInfoMapper = accountPayInfoMapper;
	}

	@Autowired
	public void setSystemDateMapper(SystemDateMapper systemDateMapper) {
		this.systemDateMapper = systemDateMapper;
	}

	private Log log = LogFactory.getLog(OrderExceptionService.class);

	@Autowired
	public void setOrderMapper(OrderMapper orderMapper) {
		this.orderMapper = orderMapper;
	}

	@Autowired
	public void setOrderPayMapper(OrderPayMapper orderPayMapper)
	{
		this.orderPayMapper = orderPayMapper;
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