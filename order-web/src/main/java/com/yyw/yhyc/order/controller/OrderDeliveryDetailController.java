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
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.JsonHelper;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.OrderDeliveryDetailDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.service.OrderDeliveryDetailService;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.order.service.SystemPayTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/order/orderDeliveryDetail")
public class OrderDeliveryDetailController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderDeliveryDetailController.class);

	@Autowired
	private OrderDeliveryDetailService orderDeliveryDetailService;

	@Reference(timeout = 50000)
	private CreditDubboServiceInterface creditDubboService;

	@Autowired
	private SystemPayTypeService systemPayTypeService;

	@Autowired
	private OrderService orderService;


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
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.POST)
	@ResponseBody
	public Pagination<OrderDeliveryDetailDto> listPgOrderDeliveryDetail(@RequestBody RequestModel<OrderDeliveryDetailDto> requestModel) throws Exception
	{
		//用户登录信息
		UserDto userDto=super.getLoginUser();
		Pagination<OrderDeliveryDetailDto> pagination = new Pagination<OrderDeliveryDetailDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderDeliveryDetailDto orderDeliveryDetailDto=requestModel.getParam();
        if("1".equals(orderDeliveryDetailDto.getUserType())){
			orderDeliveryDetailDto.setCustId(userDto.getCustId());
		}else {
			orderDeliveryDetailDto.setSupplyId(userDto.getCustId());
		}
		return orderDeliveryDetailService.listPaginationByProperty(pagination, orderDeliveryDetailDto);
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
	 * 确认收货
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/confirmReceipt", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> confirmReceipt(@RequestBody  String listStr) throws Exception
	{

		String flowId="";
		String returnType="";
		List<OrderDeliveryDetailDto> list = JsonHelper.fromList(listStr, OrderDeliveryDetailDto.class);
		if(list.size()>0){
			flowId=list.get(0).getFlowId();
			returnType=list.get(0).getReturnType();
		}
		UserDto user = super.getLoginUser();
		Map<String,String> map = orderDeliveryDetailService.updateConfirmReceipt(list, user);

		// TODO: 2016/8/23  待联调
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
		return map;
	}

    /**
     * 分页查询记录
     * @return
     */
    @RequestMapping(value = { "/listPgReturn"}, method = RequestMethod.POST)
    @ResponseBody
    public Pagination<OrderDeliveryDetailDto> listPgOrderDeliveryDetailReturn(@RequestBody RequestModel<OrderDeliveryDetailDto> requestModel) throws Exception
    {
        //获取用户信息
        UserDto userDto=super.getLoginUser();
        Pagination<OrderDeliveryDetailDto> pagination = new Pagination<OrderDeliveryDetailDto>();
        pagination.setPaginationFlag(requestModel.isPaginationFlag());
        pagination.setPageNo(requestModel.getPageNo());
        pagination.setPageSize(requestModel.getPageSize());
        OrderDeliveryDetailDto orderDeliveryDetailDto=requestModel.getParam();
        if("1".equals(orderDeliveryDetailDto.getUserType())){
            orderDeliveryDetailDto.setCustId(userDto.getCustId());
        }else {
            orderDeliveryDetailDto.setSupplyId(userDto.getCustId());
        }
        return orderDeliveryDetailService.listPaginationReturnByProperty(pagination, orderDeliveryDetailDto);
    }


	/**
	 * 补货订单确认收货商品列表
	 * @return
	 */
	@RequestMapping(value = {"", "/listReplenishment"}, method = RequestMethod.POST)
	@ResponseBody
	public Pagination<OrderDeliveryDetailDto> listPgReplenishment(@RequestBody RequestModel<OrderDeliveryDetailDto> requestModel) throws Exception
	{
		//用户登录信息
		UserDto userDto=super.getLoginUser();
		Pagination<OrderDeliveryDetailDto> pagination = new Pagination<OrderDeliveryDetailDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderDeliveryDetailDto orderDeliveryDetailDto=requestModel.getParam();
		orderDeliveryDetailDto.setCustId(userDto.getCustId());
		return orderDeliveryDetailService.listPaginationReplenishment(pagination, orderDeliveryDetailDto);
	}

	/**
	 * 补货订单确认收货商品列表
	 * @return
	 */
	@RequestMapping(value = "/sellerChangeOrderDeliveryDetailDto", method = RequestMethod.POST)
	@ResponseBody
	public Pagination<OrderDeliveryDetailDto> sellerChangeOrderDeliveryDetailDto(@RequestBody RequestModel<OrderDeliveryDetailDto> requestModel) throws Exception
	{
		//用户登录信息
		UserDto userDto=super.getLoginUser();
		Pagination<OrderDeliveryDetailDto> pagination = new Pagination<OrderDeliveryDetailDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderDeliveryDetailDto orderDeliveryDetailDto=requestModel.getParam();
		orderDeliveryDetailDto.setSupplyId(userDto.getCustId());
		return orderDeliveryDetailService.listPaginationOrderDeliveryDetailDto(pagination, orderDeliveryDetailDto);
	}
}
