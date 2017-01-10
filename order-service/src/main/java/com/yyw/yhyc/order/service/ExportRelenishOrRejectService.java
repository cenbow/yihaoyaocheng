package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.dto.OrderExceptionDto;
import com.yyw.yhyc.order.dto.OrderReturnDto;
import com.yyw.yhyc.order.enmu.BillTypeEnum;
import com.yyw.yhyc.order.mapper.OrderDeliveryMapper;
import com.yyw.yhyc.order.mapper.OrderExceptionMapper;
import com.yyw.yhyc.order.mapper.OrderReturnMapper;

/**
 * 该业务方法处理买家的补货订单和拒收订单的导出功能
 * @author wangkui01
 *
 */
@Service("exportRelenishOrRejectService")
public class ExportRelenishOrRejectService {
	
	@Autowired
	private OrderExceptionMapper orderExceptionMapper;
	@Autowired
	private OrderDeliveryMapper orderDeliveryMapper;
	@Autowired
	private OrderReturnMapper orderReturnMapper;
	@Autowired
	private OrderService orderService;
	@Autowired
	private SystemPayTypeService payTypeService;
	
	
	public HSSFWorkbook exportSaleOrder(OrderExceptionDto orderExceptionDto) throws Exception{
		String returnType=orderExceptionDto.getReturnType();
		String name="明细";
		if(returnType.equals("3")){
			name="补货明细";
		}else if(returnType.equals("4")){
			name="拒收明细";
		}
		HSSFWorkbook  wb = new HSSFWorkbook(); 
		HSSFSheet sheet = wb.createSheet(name);
		//设置列宽
		sheet.setDefaultColumnWidth(30);  
		sheet.setDefaultRowHeightInPoints(20); 
		
		HSSFCellStyle cellStyle =OrderExportCellHelp.createCellStyle(wb);
		
		HSSFCellStyle contentStyle = OrderExportCellHelp.createContentCellStyle(wb);
		
		List<OrderExceptionDto> orderList=this.orderExceptionMapper.getExportExceptionByBean(orderExceptionDto);
		
		int increaseNum=3;
		
		if(!UtilHelper.isEmpty(orderList)){
			int rownum=0;
			for(int i=0;i<orderList.size();i++){
				OrderExceptionDto order=orderList.get(i);
				if(i>0){
					rownum+=increaseNum;
				}
				rownum = this.createRowData(sheet, order, rownum, cellStyle, contentStyle,returnType);
				
			}
		}
		return wb;
	}
	
	/**
	 * 退货的导出
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public HSSFWorkbook exportSaleReturnOrder(OrderExceptionDto orderExceptionDto) throws Exception{
		String returnType=orderExceptionDto.getReturnType();
		String name="明细";
		HSSFWorkbook  wb = new HSSFWorkbook(); 
		HSSFSheet sheet = wb.createSheet(name);
		//设置列宽
		sheet.setDefaultColumnWidth(30);  
		sheet.setDefaultRowHeightInPoints(20); 
		
		HSSFCellStyle cellStyle =OrderExportCellHelp.createCellStyle(wb);
		
		HSSFCellStyle contentStyle = OrderExportCellHelp.createContentCellStyle(wb);
		
		List<OrderExceptionDto> orderList=this.orderExceptionMapper.getExportExceptionByBean(orderExceptionDto);
		
		int increaseNum=3;
		
		if(!UtilHelper.isEmpty(orderList)){
			int rownum=0;
			for(int i=0;i<orderList.size();i++){
				OrderExceptionDto order=orderList.get(i);
				if(i>0){
					rownum+=increaseNum;
				}
				rownum = this.createReturnRowData(sheet, order, rownum, cellStyle, contentStyle,returnType);
				
			}
		}
		return wb;
	}
	
	/**
	 * 退货的导出
	 * @param orderExceptionDto
	 * @return
	 * @throws Exception
	 */
	public HSSFWorkbook exportSaleChangeOrder(OrderExceptionDto orderExceptionDto) throws Exception{
		String returnType=orderExceptionDto.getReturnType();
		String name="明细";
		HSSFWorkbook  wb = new HSSFWorkbook(); 
		HSSFSheet sheet = wb.createSheet(name);
		//设置列宽
		sheet.setDefaultColumnWidth(30);  
		sheet.setDefaultRowHeightInPoints(20); 
		
		HSSFCellStyle cellStyle =OrderExportCellHelp.createCellStyle(wb);
		
		HSSFCellStyle contentStyle = OrderExportCellHelp.createContentCellStyle(wb);
		
		List<OrderExceptionDto> orderList=this.orderExceptionMapper.getExportExceptionByBean(orderExceptionDto);
		
		int increaseNum=3;
		
		if(!UtilHelper.isEmpty(orderList)){
			int rownum=0;
			for(int i=0;i<orderList.size();i++){
				OrderExceptionDto order=orderList.get(i);
				if(i>0){
					rownum+=increaseNum;
				}
				rownum = this.createChangeRowData(sheet, order, rownum, cellStyle, contentStyle,returnType);
				
			}
		}
		return wb;
	}
	
	/**
	 * 创建换货的excelData
	 * @param sheet
	 * @param orderException
	 * @param rownum
	 * @param cellStyle
	 * @param contentStyle
	 * @param returnType
	 * @return
	 * @throws Exception
	 */
	private int createChangeRowData(HSSFSheet sheet,OrderExceptionDto orderException,int rownum,HSSFCellStyle cellStyle,HSSFCellStyle contentStyle,String returnType) throws Exception{
		List<OrderReturnDto> orderReturnList=orderException.getOrderReturnList();
		OrderDelivery orderDelivery=orderException.getOrderDelivery();
		HSSFRow row=null;
		HSSFCell cell=null;
		
		 row = sheet.createRow(rownum);  
		
		HSSFCell cell00 = row.createCell(0);
		cell00.setCellValue("下单时间");
		cell00.setCellStyle(cellStyle);
		
		HSSFCell cell01 = row.createCell(1);
		cell01.setCellValue(orderException.getCreateTime());
		cell01.setCellStyle(contentStyle);
		
		
		
		
		HSSFCell cell02 = row.createCell(2);
		cell02.setCellValue("换货订单号");
		cell02.setCellStyle(cellStyle);
		
		
		HSSFCell cell03 = row.createCell(3);
		cell03.setCellValue(orderException.getExceptionOrderId());
		cell03.setCellStyle(contentStyle);
		
		HSSFCell cell04 = row.createCell(4);
		cell04.setCellValue("原订单号");
		cell04.setCellStyle(cellStyle);
		
		
		
		
		HSSFCell cell05 = row.createCell(5);
		cell05.setCellValue(orderException.getFlowId());
		cell05.setCellStyle(contentStyle);
		
		HSSFCell cell06 = row.createCell(6);
		cell06.setCellValue("发票类型");
		cell06.setCellStyle(cellStyle);
		
		HSSFCell cell07 = row.createCell(7);
		cell07.setCellValue(BillTypeEnum.getBillTypeName(orderException.getBillType()));
		cell07.setCellStyle(contentStyle);
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("供应商");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(orderException.getSupplyName());
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(2);
		cell.setCellValue("供应商换货收货地址");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(3);
		cell.setCellValue(orderDelivery.getReceiveAddress());
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(4);
		cell.setCellValue("收货联系人");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(5);
		cell.setCellValue(orderDelivery.getReceivePerson());
		cell.setCellStyle(contentStyle);
		
		
		cell= row.createCell(6);
		cell.setCellValue("收货人联系方式");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(7);
		cell.setCellValue(orderDelivery.getReceiveContactPhone());
		cell.setCellStyle(contentStyle);
		
		
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("采购商");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(orderException.getCustName());
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(2);
		cell.setCellValue("采购商换货发货地址");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(3);
		cell.setCellValue(orderDelivery.getDeliveryAddress());
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(4);
		cell.setCellValue("发货联系人");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(5);
		cell.setCellValue(orderDelivery.getDeliveryPerson());
		cell.setCellStyle(contentStyle);
		
		
		cell= row.createCell(6);
		cell.setCellValue("发货联系方式");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(7);
		cell.setCellValue(orderDelivery.getDeliveryContactPerson());
		cell.setCellStyle(contentStyle);
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		
		cell= row.createCell(0);
		cell.setCellValue("商品编码");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue("通用名");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(2);
		cell.setCellValue("规格");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(3);
		cell.setCellValue("厂商");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(4);
		cell.setCellValue("单价(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(5);
		cell.setCellValue("数量");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(6);
		cell.setCellValue("金额(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(7);
		cell.setCellValue("促销信息");
		cell.setCellStyle(cellStyle);
		
		BigDecimal exceptionOrderProductPrice=new BigDecimal(0);
		
		if(!UtilHelper.isEmpty(orderReturnList)){
			for(OrderReturnDto orderDetail : orderReturnList){
				rownum++;
				row = sheet.createRow(rownum); 
				
				cell= row.createCell(0);
				cell.setCellValue(orderDetail.getProductCode());
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(1);
				cell.setCellValue(orderDetail.getShortName());
				cell.setCellStyle(contentStyle);
				
				
				cell= row.createCell(2);
				cell.setCellValue(orderDetail.getSpecification());
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(3);
				cell.setCellValue(orderDetail.getManufactures());
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(4);
				cell.setCellValue(orderDetail.getProductPrice()+"");
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(5);
				cell.setCellValue(orderDetail.getReturnCount()+"");
				cell.setCellStyle(contentStyle);
				
				BigDecimal produceMoney=orderDetail.getProductPrice().multiply(new BigDecimal(orderDetail.getReturnCount()));
				exceptionOrderProductPrice=exceptionOrderProductPrice.add(produceMoney);
				cell= row.createCell(6);
				cell.setCellValue(produceMoney+"");
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(7);
				cell.setCellValue(orderDetail.getPromotionName());
				cell.setCellStyle(contentStyle);
				
				
			}
			
		}
		
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("商品金额(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(exceptionOrderProductPrice+"");
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(2);
		cell.setCellValue("优惠金额");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(3);
		cell.setCellValue(0);
		cell.setCellStyle(contentStyle);
		
		
		
		cell= row.createCell(4);
		cell.setCellValue("满减金额");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(5);
		cell.setCellValue(exceptionOrderProductPrice.subtract(orderException.getOrderMoney())+"");
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(6);
		cell.setCellValue("订单金额(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(7);
		cell.setCellValue(orderException.getOrderMoney()+"");
		cell.setCellStyle(contentStyle);
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("换货说明");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(orderException.getReturnDesc());
		cell.setCellStyle(contentStyle);
		
		return rownum;
	}
	
	
	

	/**
	 * 创建退货的exceldata
	 * @param sheet
	 * @param orderException
	 * @param rownum
	 * @param cellStyle
	 * @param contentStyle
	 * @param returnType
	 * @return
	 * @throws Exception
	 */
	private int createReturnRowData(HSSFSheet sheet,OrderExceptionDto orderException,int rownum,HSSFCellStyle cellStyle,HSSFCellStyle contentStyle,String returnType) throws Exception{
		List<OrderReturnDto> orderReturnList=orderException.getOrderReturnList();
		OrderDelivery orderDelivery=orderException.getOrderDelivery();
		HSSFRow row=null;
		HSSFCell cell=null;
		
		 row = sheet.createRow(rownum);  
		
		HSSFCell cell00 = row.createCell(0);
		cell00.setCellValue("下单时间");
		cell00.setCellStyle(cellStyle);
		
		HSSFCell cell01 = row.createCell(1);
		cell01.setCellValue(orderException.getCreateTime());
		cell01.setCellStyle(contentStyle);
		
		
		
		
		HSSFCell cell02 = row.createCell(2);
		cell02.setCellValue("退货订单号");
		cell02.setCellStyle(cellStyle);
		
		
		HSSFCell cell03 = row.createCell(3);
		cell03.setCellValue(orderException.getExceptionOrderId());
		cell03.setCellStyle(contentStyle);
		
		HSSFCell cell04 = row.createCell(4);
		cell04.setCellValue("原订单号");
		cell04.setCellStyle(cellStyle);
		
		
		
		
		HSSFCell cell05 = row.createCell(5);
		cell05.setCellValue(orderException.getFlowId());
		cell05.setCellStyle(contentStyle);
		
		HSSFCell cell06 = row.createCell(6);
		cell06.setCellValue("发票类型");
		cell06.setCellStyle(cellStyle);
		
		HSSFCell cell07 = row.createCell(7);
		cell07.setCellValue(BillTypeEnum.getBillTypeName(orderException.getBillType()));
		cell07.setCellStyle(contentStyle);
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("供应商");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(orderException.getSupplyName());
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(2);
		cell.setCellValue("供应商收货地址");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(3);
		cell.setCellValue(orderDelivery.getReceiveAddress());
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(4);
		cell.setCellValue("供应商收货联系人");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(5);
		cell.setCellValue(orderDelivery.getReceivePerson());
		cell.setCellStyle(contentStyle);
		
		
		cell= row.createCell(6);
		cell.setCellValue("供应商收货人联系方式");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(7);
		cell.setCellValue(orderDelivery.getReceiveContactPhone());
		cell.setCellStyle(contentStyle);
		
		
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("采购商");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(orderException.getCustName());
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(2);
		cell.setCellValue("采购商退货发货地址");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(3);
		cell.setCellValue(orderDelivery.getDeliveryAddress());
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(4);
		cell.setCellValue("采购商退货发货联系人");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(5);
		cell.setCellValue(orderDelivery.getDeliveryPerson());
		cell.setCellStyle(contentStyle);
		
		
		cell= row.createCell(6);
		cell.setCellValue("采购商退货发货联系方式");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(7);
		cell.setCellValue(orderDelivery.getDeliveryContactPerson());
		cell.setCellStyle(contentStyle);
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		
		cell= row.createCell(0);
		cell.setCellValue("商品编码");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue("通用名");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(2);
		cell.setCellValue("规格");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(3);
		cell.setCellValue("厂商");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(4);
		cell.setCellValue("单价(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(5);
		cell.setCellValue("数量");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(6);
		cell.setCellValue("金额(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(7);
		cell.setCellValue("促销信息");
		cell.setCellStyle(cellStyle);
		
		BigDecimal exceptionOrderProductPrice=new BigDecimal(0);
		
		if(!UtilHelper.isEmpty(orderReturnList)){
			for(OrderReturnDto orderDetail : orderReturnList){
				rownum++;
				row = sheet.createRow(rownum); 
				
				cell= row.createCell(0);
				cell.setCellValue(orderDetail.getProductCode());
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(1);
				cell.setCellValue(orderDetail.getShortName());
				cell.setCellStyle(contentStyle);
				
				
				cell= row.createCell(2);
				cell.setCellValue(orderDetail.getSpecification());
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(3);
				cell.setCellValue(orderDetail.getManufactures());
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(4);
				cell.setCellValue(orderDetail.getProductPrice()+"");
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(5);
				cell.setCellValue(orderDetail.getReturnCount()+"");
				cell.setCellStyle(contentStyle);
				
				BigDecimal produceMoney=orderDetail.getProductPrice().multiply(new BigDecimal(orderDetail.getReturnCount()));
				exceptionOrderProductPrice=exceptionOrderProductPrice.add(produceMoney);
				cell= row.createCell(6);
				cell.setCellValue(produceMoney+"");
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(7);
				cell.setCellValue(orderDetail.getPromotionName());
				cell.setCellStyle(contentStyle);
				
				
			}
			
		}
		
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("商品金额(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(exceptionOrderProductPrice+"");
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(2);
		cell.setCellValue("优惠金额");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(3);
		cell.setCellValue(0);
		cell.setCellStyle(contentStyle);
		
		
		
		cell= row.createCell(4);
		cell.setCellValue("满减金额");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(5);
		cell.setCellValue(exceptionOrderProductPrice.subtract(orderException.getOrderMoney())+"");
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(6);
		cell.setCellValue("订单金额(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(7);
		cell.setCellValue(orderException.getOrderMoney()+"");
		cell.setCellStyle(contentStyle);
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("退货说明");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(orderException.getReturnDesc());
		cell.setCellStyle(contentStyle);
		
		return rownum;
	}
	
	
/**	
 * 创建拒收合和补货的excel
 * @param sheet
 * @param orderException
 * @param rownum
 * @param cellStyle
 * @param contentStyle
 * @param returnType
 * @return
 * @throws Exception
 */
private int createRowData(HSSFSheet sheet,OrderExceptionDto orderException,int rownum,HSSFCellStyle cellStyle,HSSFCellStyle contentStyle,String returnType) throws Exception{
		//returnType:3补货，4拒收
		List<OrderReturnDto> orderReturnList=orderException.getOrderReturnList();
		OrderDelivery orderDelivery=orderException.getOrderDelivery();
		HSSFRow row=null;
		HSSFCell cell=null;
		
		 row = sheet.createRow(rownum);  
		
		HSSFCell cell00 = row.createCell(0);
		cell00.setCellValue("下单时间");
		cell00.setCellStyle(cellStyle);
		
		HSSFCell cell01 = row.createCell(1);
		cell01.setCellValue(orderException.getCreateTime());
		cell01.setCellStyle(contentStyle);
		
		
		
		
		HSSFCell cell02 = row.createCell(2);
		if(returnType.equals("3")){
			cell02.setCellValue("补货订单号");
		}else if(returnType.equals("4")){
			cell02.setCellValue("拒收订单号");
		}else{
			cell02.setCellValue("订单号");
		}
		
		cell02.setCellStyle(cellStyle);
		
		
		HSSFCell cell03 = row.createCell(3);
		cell03.setCellValue(orderException.getExceptionOrderId());
		cell03.setCellStyle(contentStyle);
		
		HSSFCell cell04 = row.createCell(4);
		cell04.setCellValue("原订单号");
		cell04.setCellStyle(cellStyle);
		
		
		
		
		HSSFCell cell05 = row.createCell(5);
		cell05.setCellValue(orderException.getFlowId());
		cell05.setCellStyle(contentStyle);
		
		HSSFCell cell06 = row.createCell(6);
		cell06.setCellValue("发票类型");
		cell06.setCellStyle(cellStyle);
		
		HSSFCell cell07 = row.createCell(7);
		cell07.setCellValue(BillTypeEnum.getBillTypeName(orderException.getBillType()));
		cell07.setCellStyle(contentStyle);
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("供应商");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(orderException.getSupplyName());
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(2);
		cell.setCellValue("发货地址");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(3);
		cell.setCellValue(orderDelivery.getDeliveryAddress());
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(4);
		cell.setCellValue("发货联系人");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(5);
		cell.setCellValue(orderDelivery.getDeliveryPerson());
		cell.setCellStyle(contentStyle);
		
		
		cell= row.createCell(6);
		cell.setCellValue("发货人联系方式");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(7);
		cell.setCellValue(orderDelivery.getDeliveryContactPhone());
		cell.setCellStyle(contentStyle);
		
		
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("采购商");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(orderException.getCustName());
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(2);
		cell.setCellValue("收货地址");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(3);
		cell.setCellValue(orderDelivery.getReceiveAddress());
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(4);
		cell.setCellValue("收货联系人");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(5);
		cell.setCellValue(orderDelivery.getReceivePerson());
		cell.setCellStyle(contentStyle);
		
		
		cell= row.createCell(6);
		cell.setCellValue("收货人联系方式");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(7);
		cell.setCellValue(orderDelivery.getReceiveContactPhone());
		cell.setCellStyle(contentStyle);
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		
		cell= row.createCell(0);
		cell.setCellValue("商品编码");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue("通用名");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(2);
		cell.setCellValue("规格");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(3);
		cell.setCellValue("厂商");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(4);
		cell.setCellValue("单价(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(5);
		cell.setCellValue("数量");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(6);
		cell.setCellValue("金额(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(7);
		cell.setCellValue("促销信息");
		cell.setCellStyle(cellStyle);
		
		BigDecimal exceptionOrderProductPrice=new BigDecimal(0);
		
		if(!UtilHelper.isEmpty(orderReturnList)){
			for(OrderReturnDto orderDetail : orderReturnList){
				rownum++;
				row = sheet.createRow(rownum); 
				
				cell= row.createCell(0);
				cell.setCellValue(orderDetail.getProductCode());
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(1);
				cell.setCellValue(orderDetail.getShortName());
				cell.setCellStyle(contentStyle);
				
				
				cell= row.createCell(2);
				cell.setCellValue(orderDetail.getSpecification());
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(3);
				cell.setCellValue(orderDetail.getManufactures());
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(4);
				cell.setCellValue(orderDetail.getProductPrice()+"");
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(5);
				cell.setCellValue(orderDetail.getReturnCount()+"");
				cell.setCellStyle(contentStyle);
				
				BigDecimal produceMoney=orderDetail.getProductPrice().multiply(new BigDecimal(orderDetail.getReturnCount()));
				exceptionOrderProductPrice=exceptionOrderProductPrice.add(produceMoney);
				cell= row.createCell(6);
				cell.setCellValue(produceMoney+"");
				cell.setCellStyle(contentStyle);
				
				cell= row.createCell(7);
				cell.setCellValue(orderDetail.getPromotionName());
				cell.setCellStyle(contentStyle);
				
				
			}
			
		}
		
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("商品金额(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(exceptionOrderProductPrice+"");
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(2);
		cell.setCellValue("优惠金额");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(3);
		cell.setCellValue(0);
		cell.setCellStyle(contentStyle);
		
		
		
		cell= row.createCell(4);
		cell.setCellValue("满减金额");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(5);
		cell.setCellValue(exceptionOrderProductPrice.subtract(orderException.getOrderMoney())+"");
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(6);
		cell.setCellValue("订单金额(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(7);
		cell.setCellValue(orderException.getOrderMoney()+"");
		cell.setCellStyle(contentStyle);
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		if(returnType.equals("3")){
			cell.setCellValue("补货说明");
		}else if(returnType.equals("4")){
			cell.setCellValue("拒收说明");
		}
		
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(orderException.getReturnDesc());
		cell.setCellStyle(contentStyle);
		
		return rownum;
		
	}
	

}
