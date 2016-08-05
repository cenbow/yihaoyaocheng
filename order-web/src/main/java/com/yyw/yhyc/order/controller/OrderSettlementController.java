/**
 *
 * Created By: XI
 * Created On: 2016-7-28 17:34:56
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.dto.OrderSettlementDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.facade.OrderSettlementFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/order/orderSettlement")
public class OrderSettlementController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderSettlementController.class);

	@Reference
	private OrderSettlementFacade orderSettlementFacade;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public OrderSettlement getByPK(@PathVariable("key") Integer key) throws Exception
	{
		OrderSettlement orderSettlement= orderSettlementFacade.getByPK(key);
		if(orderSettlement!=null && orderSettlement.getSettlementMoney()!=null && orderSettlement.getRefunSettlementMoney()!=null){
			if(orderSettlement.getRefunSettlementMoney().intValue()!=0 && orderSettlement.getRefunSettlementMoney().intValue()!=0){
				orderSettlement.setDifferentMoney(orderSettlement.getRefunSettlementMoney().subtract(orderSettlement.getSettlementMoney()));
			}
		}
		return orderSettlement;
	}

	/**
	* 分页查询记录
	 * type 1 应收 2 应付
	* @return
	*/
	@RequestMapping(value = {"", "/listPg/t{type}"}, method = RequestMethod.POST)
	@ResponseBody
	public Pagination<OrderSettlementDto> listPgOrderSettlement(@RequestBody RequestModel<OrderSettlementDto> requestModel, @PathVariable("type") Integer type) throws Exception
	{
		Pagination<OrderSettlementDto> pagination = new Pagination<OrderSettlementDto>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());
		OrderSettlementDto orderSettlementDto = requestModel.getParam()==null?new OrderSettlementDto():requestModel.getParam();
		orderSettlementDto.setType(type);
		//TODO custId
		UserDto dto = super.getLoginUser();
		if(type==1){
			//orderSettlementDto.setCustId(dto.getCustId());
		}else if(type==2){
			//orderSettlementDto.setSupplyId(dto.getCustId());
        }
		return orderSettlementFacade.listPaginationByProperty(pagination, orderSettlementDto);
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(OrderSettlement orderSettlement) throws Exception
	{
		orderSettlementFacade.save(orderSettlement);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderSettlementFacade.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(OrderSettlement orderSettlement) throws Exception
	{
		orderSettlementFacade.update(orderSettlement);
	}

	/**
	 * 退款订单结算
	 * @return
	 */
	@RequestMapping(value = "/refundSettlement", method = RequestMethod.POST)
	@ResponseBody
	public OrderSettlement refundSettlement(@RequestBody OrderSettlement orderSettlement) throws Exception
	{

		// TODO: 2016/8/3  需获取登录用户信息
		/**
		 * {"orderSettlementId":1,"supplyId":512,"updateUser":"zhangba","remark":"苟利国家生死以","refunSettlementMoney":998.222}
		 */
		UserDto userDto = super.getLoginUser();
		orderSettlement.setSupplyId(userDto.getCustId());
		orderSettlement.setUpdateUser(userDto.getUserName());
		orderSettlementFacade.refundSettlement(orderSettlement);
		return  orderSettlement;
	}
	/**
	 * 分页查询记录
	 * type 1 应收 2 应付
	 * @return
	 */
	@RequestMapping(value = {"/lhk{type}"}, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView showOrderSettlementList(@PathVariable("type") Integer type){
        ModelAndView model = new ModelAndView();
        try {

            model.setViewName("lhk");
        }catch (Exception e){
            viewExceptionHandler(e,model);
        }
		return model;
	}

	/**
	 * 分页查询记录
	 * type 1 应收 2 应付
	 * @return
	 */
	@RequestMapping(value = {"/list{type}"}, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView showOrderSettlementList2(OrderSettlement settlement, @PathVariable("type") Integer type, HttpServletRequest request, HttpServletResponse response)throws Exception{

		OrderSettlement orderSettlement = settlement;
		ModelAndView model = new ModelAndView();
        model.addObject("type",type);
		if(type==1){ //卖家
			model.setViewName("order/order_settlement_seller");
		}else{
			model.setViewName("order/order_settlement_buyer");
		}
		return model;
	}
}
