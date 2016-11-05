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
import java.util.ArrayList;
import java.util.List;

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

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.dto.OrderSettlementDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.OrderSettlementService;
import com.yyw.yhyc.order.utils.MyExcelUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/export")
public class ExportController {
	private static final Logger logger = LoggerFactory.getLogger(ExportController.class);

	@Autowired
	private OrderSettlementService orderSettlementService;
	
	private <T> T getLoginUser(HttpServletRequest request ){
        T t = (T) request.getSession().getAttribute("loginUserDto");
        if(UtilHelper.isEmpty(t))
            t = (T) request.getAttribute("loginUserDto");

        return t;
    }

	/**
	* 导出记录
	 * type 1 应收 2 应付
	* @return
	*/
	@ResponseBody
	@RequestMapping(value = {"/exportOrderSettlement/t{type}"}, method = RequestMethod.POST)
	public void exportOrderSettlement( @PathVariable("type") Integer type , HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		OutputStream out = null;
		try {
			OrderSettlementDto orderSettlementDto = parseObject(request);
			orderSettlementDto.setType(type);
			//TODO custId
			UserDto dto = getLoginUser(request);
			String fileName;
			String[] colNames;
			String[] colParams;
			//应收应付差异化设置
			if(type==1){
				orderSettlementDto.setSupplyId(dto.getCustId());
				fileName = "结算应收管理";
				colNames = new String[]{"原订单号","异常流程订单号","结算流水号","业务类型","支付方式","支付平台","采购商","下单时间","结算时间","应结算金额(元)","实际结算金额(元)","结算状态"};
				colParams= new String[]{"orgFlowId","flowId","payFlowId","businessTypeName","payTypeName","payName","custName","orderTime",
						"settlementTime","settlementMoney","refunSettlementMoney","confirmSettlementName"};
			}else{
				orderSettlementDto.setCustId(dto.getCustId());
				fileName = "结算应付管理";
				colNames = new String[]{"原订单号","异常流程订单号","结算流水号","业务类型","支付方式","支付平台","供购商","下单时间","结算时间","应结算金额(元)","实际结算金额(元)","结算状态"};
				colParams= new String[]{"orgFlowId","flowId","payFlowId","businessTypeName","payTypeName","payName","supplyName","orderTime",
						"settlementTime","settlementMoney","refunSettlementMoney","confirmSettlementName"};
	        }
			
			/* 分页查询 */
			List<OrderSettlementDto> modelList = new ArrayList<OrderSettlementDto>();
			int page = 1;
			int totalPages;
			Pagination<OrderSettlementDto> pagination = new Pagination<OrderSettlementDto>();
			pagination.setPaginationFlag(true);
			pagination.setPageSize(20);
			do{
				pagination.setPageNo(page);
				Pagination<OrderSettlementDto> pageInfo = orderSettlementService.listPaginationByProperty(pagination, orderSettlementDto);
				modelList.addAll(pageInfo.getResultList());
				totalPages = pageInfo.getTotalPage();
				page++;
			}while(page<=totalPages);
			
			/* 设置输出格式 */
			response.setContentType("text/json");
			response.setCharacterEncoding("UTF-8");
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","attachment;filename=" + new String((fileName+".xls").getBytes("GBK"), "iso8859-1")); 
			out = response.getOutputStream();
			//调用工具类导出Excel
			MyExcelUtil.setExcel(modelList, out, colNames, colParams,fileName);
			
		} catch (Exception e) {
			logger.error("exception when export",e);
		}finally{
			try {
				if(out != null){
					out.close();
				}
			}catch (IOException e) {
				logger.error("exception when close OutputStream",e);
			}
		}
	}


	private OrderSettlementDto parseObject(HttpServletRequest request) {
//		JSONObject jsonObject = JSONObject.fromObject(request.getParameter("param"));
//		OrderSettlementDto orderSettlementDto = (OrderSettlementDto)JSONObject.toBean(jsonObject, OrderSettlementDto.class);
		OrderSettlementDto orderSettlementDto = new OrderSettlementDto();
		String province = request.getParameter("province");
		if( !UtilHelper.isEmpty(province) ){
			orderSettlementDto.setProvince(province);
		}
		String city = request.getParameter("city");
		if( !UtilHelper.isEmpty(city) ){
			orderSettlementDto.setCity(city);
		}
		String area = request.getParameter("area");
		if( !UtilHelper.isEmpty(area) ){
			orderSettlementDto.setArea(area);
		}
		String custName = request.getParameter("custName");
		if( !UtilHelper.isEmpty(custName) ){
			orderSettlementDto.setCustName(custName);
		}
		String orgFlowId = request.getParameter("orgFlowId");
		if( !UtilHelper.isEmpty(orgFlowId) ){
			orderSettlementDto.setOrgFlowId(orgFlowId);
		}
		String businessType = request.getParameter("businessType");
		if( !UtilHelper.isEmpty(businessType) && !"-1".equals(businessType) ){
			orderSettlementDto.setBusinessType( Integer.parseInt(businessType));
		}
		String confirmSettlement = request.getParameter("confirmSettlement");
		if( !UtilHelper.isEmpty(confirmSettlement)  && !"-1".equals(confirmSettlement) ){
			orderSettlementDto.setConfirmSettlement(confirmSettlement);
		}
		String payType = request.getParameter("payType");
		if( !UtilHelper.isEmpty(payType) && !"-1".equals(payType) ){
			orderSettlementDto.setPayType( payType );
		}
		String payTypeId = request.getParameter("payTypeId");
		if( !UtilHelper.isEmpty(payTypeId) && !"-1".equals(payTypeId) ){
			orderSettlementDto.setPayTypeId( Integer.parseInt(payTypeId) );
		}
		String startTime = request.getParameter("startTime");
		if( !UtilHelper.isEmpty(startTime) ){
			orderSettlementDto.setStartTime(startTime);
		}
		String endTime = request.getParameter("endTime");
		if( !UtilHelper.isEmpty(endTime) ){
			orderSettlementDto.setEndTime(endTime);
		}
		return orderSettlementDto;
	}





}
