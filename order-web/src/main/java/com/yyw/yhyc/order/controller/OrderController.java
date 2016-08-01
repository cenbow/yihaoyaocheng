/**
 * Created By: XI
 * Created On: 2016-7-28 17:34:55
 * <p/>
 * Amendment History:
 * <p/>
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 **/
package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.Pagination;
import com.yyw.yhyc.order.bo.RequestListModel;
import com.yyw.yhyc.order.bo.RequestModel;
import com.yyw.yhyc.order.dto.OrderCreateDto;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.facade.OrderFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/order")
public class OrderController extends BaseJsonController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Reference
    private OrderFacade orderFacade;

    /**
     * 通过主键查询实体对象
     * @return
     */
    @RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Order getByPK(@PathVariable("key") Integer key) throws Exception {
        return orderFacade.getByPK(key);
    }

    /**
     * 分页查询记录
     * @return
     */
    @RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
    @ResponseBody
    public Pagination<Order> listPgOrder(RequestModel<Order> requestModel) throws Exception {
        Pagination<Order> pagination = new Pagination<Order>();

        pagination.setPaginationFlag(requestModel.isPaginationFlag());
        pagination.setPageNo(requestModel.getPageNo());
        pagination.setPageSize(requestModel.getPageSize());

        return orderFacade.listPaginationByProperty(pagination, requestModel.getParam());
    }

    /**
     * 新增记录
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add(Order order) throws Exception {
        orderFacade.save(order);
    }

    /**
     * 根据多条主键值删除记录
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(RequestListModel<Integer> requestListModel) throws Exception {
        orderFacade.deleteByPKeys(requestListModel.getList());
    }

    /**
     * 修改记录
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void update(Order order) throws Exception {
        orderFacade.update(order);
    }


	/**
	 * 校验要购买的商品(通用方法)
	 * @param orderDto
	 * @throws Exception
	 */
	@RequestMapping(value = "/validateProducts", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> validateProducts(OrderDto orderDto) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		boolean validateResult = false;
		try{
			validateResult = orderFacade.validateProducts(orderDto);
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		map.put("result",validateResult);
		return map;
	}

	/**
	 * 创建订单
	 * 请求数据格式：
	 *


	 {

		 "orderDeliveryDto":{
			 "receivePerson":"收货人",
			 "receiveProvince":"收货省码",
			 "receiveCity":"收货市码",
			 "receiveRegion":"收货区县码",
			 "receiveProvinceName":"收货省名称",
			 "receiveCityName":"收货市名称",
			 "receiveRegionName":"收货区县名称",
			 "receiveAddress":"省名称+市名称+区县名称+具体地址",
			 "receiveContactPhone":"收货人联系电话",
			 "zipCode":"邮政编码"
		 },

		 "orderDtoList": [
				 {
					 "custId": "123",
					 "supplyId": "321",
					 "productInfoDtoList": [
						 {
							 "id": "111",
							 "productCount": "1"
						 },
						 {
							 "id": "112",
							 "productCount": "2"
						 }
					 ],
					 "billType": "1",
					 "payTypeId": "1",
					 "leaveMessage": "买家留言啦！！"
				 },
				 {
					 "custId": "123",
					 "supplyId": "124",
					 "productInfoDtoList": [
							 {
								 "id": "222",
								 "productCount": "1"
							 },
							 {
								 "id": "223",
								 "productCount": "2"
							 }
					 ],
					 "billType": "2",
					 "payTypeId": "2",
					 "leaveMessage": "买家留言咯！！"
				 }
		 ]
	 }




	 * @param orderCreateDto
	 * @throws Exception
	 */
	@RequestMapping(value = "/createOrder", method = RequestMethod.POST)
	@ResponseBody
	public OrderCreateDto createOrder(@RequestBody OrderCreateDto orderCreateDto) throws Exception {
		return orderFacade.createOrder(orderCreateDto);
	}

    /**
     * 采购订单查询
     * @return
     */
    @RequestMapping(value = {"", "/listPgBuyerOrder"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listPgBuyerOrder(@RequestBody RequestModel<OrderDto> requestModel){
		// TODO: 2016/8/1 需要从usercontex获取登录用户id
		System.err.println("===>" + requestModel);
        /**
		 * http://localhost:8088/order/listPgBuyerOrder
         * {"param":{"custId":1,"flowId":"1","payType":1,"supplyName":"上","createBeginTime":"2016-01-02","createEndTime":"2016-8-20","orderStatus":"1"}}
         */
        Pagination<OrderDto> pagination = new Pagination<OrderDto>();
        pagination.setPaginationFlag(requestModel.isPaginationFlag());
        pagination.setPageNo(requestModel.getPageNo());
        pagination.setPageSize(requestModel.getPageSize());
        return orderFacade.listPgBuyerOrder(pagination, requestModel.getParam());
    }

	/**
	 * 采购商取消订单
	 * @return
	 */
	@RequestMapping(value = "/cancleOrder/{orderId}", method = RequestMethod.GET)
	@ResponseBody
	public void cancleOrder(@PathVariable("orderId") Integer orderId){
		// TODO: 2016/8/1 需要从usercontex获取登录用户id
		/**
		 *  http://localhost:8088/order/cancleOrder/2
		 */
		int custId = 1;
		orderFacade.cancleOrder(custId,orderId);
	}
}
