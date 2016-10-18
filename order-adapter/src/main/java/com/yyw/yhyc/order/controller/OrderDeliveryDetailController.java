/**
 *
 * Created By: XI
 * Created On: 2016-7-28 17:34:55
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yao.trade.interfaces.credit.model.CreditDubboResult;
import com.yao.trade.interfaces.credit.model.CreditParams;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.DateHelper;
import com.yyw.yhyc.helper.JsonHelper;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.OrderDeliveryDetailDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.service.OrderDeliveryDetailService;
import com.yyw.yhyc.order.service.OrderExceptionService;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.order.service.SystemPayTypeService;
import com.yyw.yhyc.usermanage.bo.UsermanageEnterprise;
import com.yyw.yhyc.usermanage.service.UsermanageEnterpriseService;
import com.yyw.yhyc.utils.MyConfigUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/api/order/orderDeliveryDetail")
public class OrderDeliveryDetailController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(OrderDeliveryDetailController.class);

	@Autowired
	private OrderDeliveryDetailService orderDeliveryDetailService;

	@Autowired
	private UsermanageEnterpriseService usermanageEnterpriseService;

	@Reference(timeout = 50000)
	private CreditDubboServiceInterface creditDubboService;

	@Autowired
	private SystemPayTypeService systemPayTypeService;

	@Reference
	private IProductDubboManageService iProductDubboManageService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderExceptionService orderExceptionService;
	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public OrderDeliveryDetail getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return orderDeliveryDetailService.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<OrderDeliveryDetailDto> listPgOrderDeliveryDetail(RequestModel<OrderDeliveryDetailDto> requestModel) throws Exception
	{
		Pagination<OrderDeliveryDetailDto> pagination = new Pagination<OrderDeliveryDetailDto>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return orderDeliveryDetailService.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		orderDeliveryDetailService.save(orderDeliveryDetail);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderDeliveryDetailService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(OrderDeliveryDetail orderDeliveryDetail) throws Exception
	{
		orderDeliveryDetailService.update(orderDeliveryDetail);
	}

	/**
	 * 确认收货列表
	 * @return
	 */
	@RequestMapping(value = {"", "/confirmOrderDetail"}, method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> listPgOrderDeliveryDetail(String orderId) throws Exception
	{
		String supplyName="";
		UserDto userDto = super.getLoginUser();
		Map<String,Object> map=new HashMap<String,Object>();
		List<Map<String,Object>> productList= new ArrayList<Map<String,Object>>();
		Pagination<OrderDeliveryDetailDto> pagination = new Pagination<OrderDeliveryDetailDto>();
		pagination.setPaginationFlag(false);
		OrderDeliveryDetailDto orderDeliveryDetailDto=new OrderDeliveryDetailDto();
		orderDeliveryDetailDto.setFlowId(orderId);
		orderDeliveryDetailDto.setCustId(userDto.getCustId());
		Pagination<OrderDeliveryDetailDto>  paginationList = orderDeliveryDetailService.listPaginationByProperty(pagination, orderDeliveryDetailDto);
		if(!UtilHelper.isEmpty(paginationList)&&paginationList.getResultList().size()>0){
			UsermanageEnterprise usermanageEnterprise = usermanageEnterpriseService.getByEnterpriseId(paginationList.getResultList().get(0).getSupplyId().toString());
			supplyName = usermanageEnterprise.getEnterpriseName();
		}else {
			throw new RuntimeException("信息不存在");
		}
		//转换为APP命名数据。
		for(OrderDeliveryDetailDto dto : paginationList.getResultList()){
			String productPicUrl="";
			try{
				List picUrlList = iProductDubboManageService.selectByTypeIdAndSPUCode(map);
				if(!UtilHelper.isEmpty(picUrlList)) {
					JSONObject productJson = JSONObject.fromObject(picUrlList.get(0));
					productPicUrl = (String) productJson.get("file_path");
					if (UtilHelper.isEmpty(productPicUrl))
						productPicUrl = "http://oms.yaoex.com/static/images/product_default_img.jpg";
					else
						try {
							productPicUrl =  URLEncoder.encode(MyConfigUtil.IMG_DOMAIN + productPicUrl, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							logger.error("查询图片接口:URLEncoder编码(UTF-8)异常:"+e.getMessage(),e);
						}
				}
			}catch (Exception e){
				logger.error("查询图片接口:调用异常," + e.getMessage(),e);
			}

			Map<String,Object> productMap=new HashMap<String,Object>();
			productMap.put("productId",dto.getProductCode());
			productMap.put("productPicUrl",productPicUrl);
			productMap.put("productName",dto.getShortName());
			productMap.put("spec",dto.getSpecification());
			productMap.put("factoryName",dto.getManufactures());
			productMap.put("buyNumber",dto.getProductCount());
			productMap.put("batchNumber",dto.getBatchNumber());
			productMap.put("batchId",dto.getOrderDeliveryDetailId());
			productMap.put("orderDetailId",dto.getOrderDetailId());
			productList.add(productMap);
		}
		map.put("supplyName",supplyName);
		map.put("qq","");
		map.put("productList",productList);
		return ok(map);
	}

	/**
	 * 确认收货
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/refusedReplenishOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> confirmReceipt(@RequestBody String listStr) throws Exception
	{
		String flowId="";
		String returnType="";
		List<OrderDeliveryDetailDto> list = new ArrayList<OrderDeliveryDetailDto>();
		//解析json字符串转换成dto
		JSONObject jsonObject = JSONObject.fromObject(listStr);
				JSONArray  jsonArray = JSONArray.fromObject(jsonObject.opt("productList"));
				List<Map> jsonList=jsonArray.toList(jsonArray, Map.class);
				for(Map<String, Object> map: jsonList){
					OrderDeliveryDetailDto dto=new OrderDeliveryDetailDto();
					dto.setFlowId(jsonObject.optString("orderId"));
					dto.setReturnType(jsonObject.optString("applyType"));
					dto.setReturnDesc(jsonObject.optString("applyCause"));
					dto.setRecieveCount(Integer.parseInt(map.get("buyNumber").toString()));
					dto.setOrderDetailId(Integer.parseInt(map.get("orderDetailId").toString()));
					dto.setOrderDeliveryDetailId(Integer.parseInt(map.get("batchId").toString()));
					list.add(dto);
				}
		if(list.size()>0){
			flowId=list.get(0).getFlowId();
			returnType=list.get(0).getReturnType();
		}
		UserDto user = super.getLoginUser();
		Map<String,String> map = orderDeliveryDetailService.updateConfirmReceipt(list, user);
		if(map.get("code").equals("0")){
			return error(map.get("msg"));
		}
		//当没有异常流程订单结束的时候调用账期结算接口
		if(null==returnType||"".equals(returnType)){
			try {
				if(UtilHelper.isEmpty(creditDubboService)){
					logger.error("CreditDubboServiceInterface creditDubboService is null");
				}else{
					Order od =  orderService.getOrderbyFlowId(flowId);
					SystemPayType systemPayType= systemPayTypeService.getByPK(od.getPayTypeId());
					if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())){
						CreditParams creditParams = new CreditParams();
						creditParams.setSourceFlowId(od.getFlowId());//订单编码
						creditParams.setBuyerCode(od.getCustId() + "");
						creditParams.setSellerCode(od.getSupplyId() + "");
						creditParams.setBuyerName(od.getCustName());
						creditParams.setSellerName(od.getSupplyName());
						creditParams.setOrderTotal(od.getOrgTotal());//订单金额  扣减后的
						creditParams.setFlowId(od.getFlowId());//订单编码
						creditParams.setStatus("2");//创建订单设置为1，收货时设置2，已还款设置4，（取消订单）已退款设置为5，创建退货订单设置为6
						creditParams.setReceiveTime(DateHelper.parseTime(od.getReceiveTime()));
						CreditDubboResult creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
						if(UtilHelper.isEmpty(creditDubboResult) || "0".equals(creditDubboResult.getIsSuccessful())){
							throw new RuntimeException(creditDubboResult !=null?creditDubboResult.getMessage():"接口调用失败！");
						}
					}
				}
			} catch (Exception e) {
				logger.error("orderService.getByPK error, flowId: "+flowId+",errorMsg:"+e.getMessage());
				e.printStackTrace();
			}
		}
		return ok(flowId);
	}

	/**
	 * 确认收货
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/refusedExceptionReplenishOrder", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> updateRepConfirmReceipt(String exceptionOrderId) throws Exception {
		UserDto user = super.getLoginUser();
		orderExceptionService.updateRepConfirmReceipt(exceptionOrderId, user);
		if (UtilHelper.isEmpty(creditDubboService))
			logger.error("CreditDubboServiceInterface creditDubboService is null");
		else {
			OrderException oe = orderExceptionService.getByExceptionOrderId(exceptionOrderId);
			Order order = orderService.getByPK(oe.getOrderId());
			SystemPayType systemPayType = systemPayTypeService.getByPK(order.getPayTypeId());
			if (SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())) {
				logger.info("补货确认收货调用资讯接口");
				CreditParams creditParams = new CreditParams();
				//creditParams.setSourceFlowId(oe.getFlowId());源订单单号
				creditParams.setBuyerCode(oe.getCustId() + "");
				creditParams.setSellerCode(oe.getSupplyId() + "");
				creditParams.setBuyerName(oe.getCustName());
				creditParams.setSellerName(oe.getSupplyName());
				creditParams.setOrderTotal(order.getOrgTotal());//订单金额
				creditParams.setFlowId(order.getFlowId());//订单编码
				creditParams.setStatus("2");
				creditParams.setReceiveTime(DateHelper.parseTime(oe.getReceiveTime()));
				CreditDubboResult creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
				if (UtilHelper.isEmpty(creditDubboResult) || "0".equals(creditDubboResult.getIsSuccessful())) {
					throw new RuntimeException(creditDubboResult != null ? creditDubboResult.getMessage() : "接口调用失败！");
				}
			}
		}
		return ok(exceptionOrderId);
	}

}
