package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yaoex.usermanage.interfaces.erp.ICustErpRelationServiceDubbo;
import com.yaoex.usermanage.model.erp.CustErpRelation;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.controller.BaseJsonController;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.dto.OrderIssuedExceptionDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.OrderIssuedExceptionService;
import com.yyw.yhyc.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liqiang on 2016/10/26.
 */
@Controller
@RequestMapping(value = "/orderIssuedException")
public class OrderIssuedExceptionController extends BaseJsonController {
    private static final Logger logger = LoggerFactory.getLogger(OrderIssuedExceptionController.class);

    @Autowired
    private OrderIssuedExceptionService orderIssuedExceptionService;

    @Reference(timeout = 50000)
    private ICustErpRelationServiceDubbo iCustErpRelationServiceDubbo;


    /**
     * 异常订单管理页
     *
     * @return
     */
    @RequestMapping("/sellerOrderIssuedExceptionManage")
    public ModelAndView orderIssuedExceptionManage() {
        ModelAndView view = new ModelAndView("orderIssuedException/order_issued_exception_manage");
        return view;
    }


    /**
     * 分页查询记录
     *
     * @return
     */
    @RequestMapping(value = {"", "/listPg"}, method = RequestMethod.POST)
    @ResponseBody
    public Pagination<OrderIssuedExceptionDto> listPgOrderIssuedLog(@RequestBody RequestModel<OrderIssuedExceptionDto> requestModel) throws Exception {
        OrderIssuedExceptionDto orderIssuedExceptionDto = requestModel.getParam();
        UserDto userDto = super.getLoginUser();
        orderIssuedExceptionDto.setSupplyId(userDto.getCustId());

        Pagination<OrderIssuedExceptionDto> pagination = new Pagination<OrderIssuedExceptionDto>();

        pagination.setPaginationFlag(requestModel.isPaginationFlag());
        pagination.setPageNo(requestModel.getPageNo());
        pagination.setPageSize(requestModel.getPageSize());

        return orderIssuedExceptionService.listPaginationByPropertyEx(pagination, orderIssuedExceptionDto);
    }

    /**
     * 异常订单下发
     *
     * @return
     */
    @RequestMapping(value = "/issued", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> issued(@RequestBody OrderIssuedExceptionDto orderIssuedExceptionDto) throws Exception {
        UserDto userDto = super.getLoginUser();
        if (!UtilHelper.isEmpty(orderIssuedExceptionDto)) {
            return orderIssuedExceptionService.updateOrderIssued(orderIssuedExceptionDto.getFlowId(), userDto.getCustName());
        } else {
            Map<String, String> result = new HashMap<String, String>();
            result.put("statusCode", "0");
            result.put("message", "订单编码不能为空");
            return result;
        }
    }

    /**
     * 订单标记接口，会出现实际下发成功，超时返回或相关问题，或顾客通过其他方式将订单录入到其ERP系统。此处增加一个手工标记成功的机制，标记后则状态变为已完成。仅针对非关联问题的异常订单。
     *
     * @return
     */
    @RequestMapping(value = "/orderMark", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> orderMark(@RequestBody OrderIssuedExceptionDto orderIssuedExceptionDto) throws Exception {
        UserDto userDto = super.getLoginUser();
        if (!UtilHelper.isEmpty(orderIssuedExceptionDto)) {
            return orderIssuedExceptionService.updateOrderMark(orderIssuedExceptionDto.getFlowId(), userDto.getCustName());
        } else {
            Map<String, String> result = new HashMap<String, String>();
            result.put("statusCode", "0");
            result.put("message", "订单编码不能为空");
            return result;
        }
    }


    /**
     * 设置关联客户
     *
     * @return
     */
    @RequestMapping(value = "/relatedCustomers", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> relatedCustomers(@RequestBody CustErpRelation custErpRelation) throws Exception {
        UserDto userDto = super.getLoginUser();
        custErpRelation.setSeller_code(userDto.getCustId()+"");
        if (!UtilHelper.isEmpty(iCustErpRelationServiceDubbo)){
            return   iCustErpRelationServiceDubbo.createOrUpdateCustRelation(custErpRelation,userDto.getCustName());
        }else {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("status", "error");
            result.put("msg", "调用DubboService服务失败");
            return result;
        }

    }


    /**
     * 导出下发异常订单
     */
    @RequestMapping(value = {"/export"}, method = RequestMethod.POST)
    @ResponseBody
    public void export(OrderIssuedExceptionDto orderIssuedExceptionDto) {
        UserDto userDto = super.getLoginUser();
        orderIssuedExceptionDto.setSupplyId(userDto.getCustId());

        Pagination<OrderIssuedExceptionDto> pagination = new Pagination<OrderIssuedExceptionDto>();
        pagination.setPaginationFlag(false);
        pagination.setPageNo(1);
        pagination.setPageSize(1);

        List<Object[]> dataset = new ArrayList<Object[]>();
        try {
            pagination = orderIssuedExceptionService.listPaginationByPropertyEx(pagination, orderIssuedExceptionDto);
            for (OrderIssuedExceptionDto order : pagination.getResultList()) {
                dataset.add(new Object[]
                        {
                                order.getFlowId(), order.getOrderCreateTime(), order.getSupplyName(), order.getSupplyId(), order.getCustName(), order.getCustId(), order.getReceivePerson(), order.getReceiveContactPhone(), order.getReceiveAddress(), order.getExceptionTypeName(), order.getOrderStatusName(), order.getPayTypeName(),
                        });
            }
        } catch (Exception e) {
            logger.info("导出失败");
            e.printStackTrace();
        }
        String[] headers = {"订单编码", "下单时间", "供应商", "供应商编码", "采购商", "采购商编码", "收货人", "联系方式", "收货地址", "异常类型", "订单状态", "支付类型"};
        byte[] bytes = ExcelUtil.exportExcel("异常订单信息", headers, dataset);
        String fileName = null;
        try {
            fileName = new String(("异常订单报表" + new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + ".xls").getBytes("gbk"), "iso-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        ServletOutputStream stream = null;
        try {
            stream = response.getOutputStream();
        } catch (IOException e) {
            logger.info("导出失败");
            e.printStackTrace();
        }
        try {
            stream.write(bytes);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            logger.info("导出失败");
            e.printStackTrace();
        }
    }

}
