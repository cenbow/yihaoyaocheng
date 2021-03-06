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

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.OrderSettlementDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.OrderSettlementService;
import com.yyw.yhyc.order.service.SystemPayTypeService;
import com.yyw.yhyc.order.utils.MyExcelUtil;

@Controller
@RequestMapping(value = "/order/orderSettlement")
public class OrderSettlementController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderSettlementController.class);

	@Autowired
	private OrderSettlementService orderSettlementService;
	@Autowired
	private SystemPayTypeService systemPayTypeService;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public OrderSettlement getByPK(@PathVariable("key") Integer key) throws Exception
	{
		OrderSettlement orderSettlement= orderSettlementService.getByPK(key);
		if(orderSettlement!=null && orderSettlement.getSettlementMoney()!=null && orderSettlement.getRefunSettlementMoney()!=null){
			orderSettlement.setDifferentMoney(orderSettlement.getRefunSettlementMoney().subtract(orderSettlement.getSettlementMoney()));
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
			orderSettlementDto.setSupplyId(dto.getCustId());
		}else if(type==2){
			orderSettlementDto.setCustId(dto.getCustId());
        }
		return orderSettlementService.listPaginationByProperty(pagination, orderSettlementDto);
	}
	
	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(OrderSettlement orderSettlement) throws Exception
	{
		orderSettlementService.save(orderSettlement);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderSettlementService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(OrderSettlement orderSettlement) throws Exception
	{
		orderSettlementService.update(orderSettlement);
	}

	/**
	 * 退款订单结算
	 * @return
	 */
	@RequestMapping(value = "/refundSettlement", method = RequestMethod.POST)
	@ResponseBody
	public OrderSettlement refundSettlement(@RequestBody OrderSettlement orderSettlement) throws Exception
	{

		/**
		 * {"orderSettlementId":1,"supplyId":512,"updateUser":"zhangba","remark":"苟利国家生死以","refunSettlementMoney":998.222}
		 */
		UserDto userDto = super.getLoginUser();
		orderSettlement.setSupplyId(userDto.getCustId());
		orderSettlement.setUpdateUser(userDto.getUserName());
		orderSettlementService.updateRefundSettlement(orderSettlement);
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
	@RequestMapping(value = {"/list/{type}"}, method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView showOrderSettlementList2(OrderSettlement settlement, @PathVariable("type") Integer type, HttpServletRequest request, HttpServletResponse response)throws Exception{

		OrderSettlement orderSettlement = settlement;
		ModelAndView model = new ModelAndView();
        model.addObject("type",type);
        
        //加载支付平台、支付方式枚举值
        SystemPayType systemPayType = new SystemPayType();
        systemPayType.setPayStates(1);
        List<SystemPayType> systemPayTypeList = systemPayTypeService.listByProperty(systemPayType);
        //支付方式
        SortedSet<SystemPayType> payTypeName = new TreeSet<SystemPayType>(  
                // 函数对象：既匿名内部类的实例，这个瞬时的对象永远不会被别的对象引用  
                        new Comparator<SystemPayType>() {  
                            public int compare(SystemPayType type1, SystemPayType type2) {
                            	String key1 = type1.getPayType().toString();
                            	String key2 = type2.getPayType().toString();
                                return key1.compareTo(key2);  
                            }  
                        });
        //支付平台
        SortedSet<SystemPayType> payName = new TreeSet<SystemPayType>(  
                // 函数对象：既匿名内部类的实例，这个瞬时的对象永远不会被别的对象引用  
                        new Comparator<SystemPayType>() {  
                            public int compare(SystemPayType type1, SystemPayType type2) {
                            	String key1 = type1.getPayTypeId().toString();
                            	String key2 = type2.getPayTypeId().toString();
                                return key1.compareTo(key2);  
                            }  
                        });
        
        for(SystemPayType record:systemPayTypeList){
        	payTypeName.add(record);
        	payName.add(record);
        }
        model.addObject("payTypeName",payTypeName);	
        model.addObject("payName",payName);			
		if(type==1){ //卖家
			model.setViewName("order/order_settlement_seller");
		}else if(type==2){//卖家应付
			model.setViewName("order/order_settlement_buyer");
		}
		return model;
	}

}
