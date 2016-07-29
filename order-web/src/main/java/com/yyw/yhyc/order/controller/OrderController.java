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
import com.yyw.yhyc.product.dto.ProductInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
     * @param productInfoDtoList
     * @throws Exception
     */
    @RequestMapping(value = "/validateProducts", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> validateProducts(List<ProductInfoDto> productInfoDtoList) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean validateResult = false;
        try {
            validateResult = orderFacade.validateProducts(productInfoDtoList);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        map.put("result", validateResult);
        return map;
    }

    /**
     * 创建订单
     * @param orderCreateDto
     * @throws Exception
     */
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    @ResponseBody
    public List<OrderDto> createOrder(OrderCreateDto orderCreateDto) throws Exception {
        return orderFacade.createOrder(orderCreateDto);
    }

    /**
     * 采购订单查询
     * @return
     */
    @RequestMapping(value = {"", "/listPgBuyerOrder"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> listPgBuyerOrder(RequestModel<OrderDto> requestModel) throws Exception {
        System.err.println("===>" + requestModel);
        Pagination<OrderDto> pagination = new Pagination<OrderDto>();
        pagination.setPaginationFlag(requestModel.isPaginationFlag());
        pagination.setPageNo(requestModel.getPageNo());
        pagination.setPageSize(requestModel.getPageSize());
        return orderFacade.listPgBuyerOrder(pagination, requestModel.getParam());
    }
}
