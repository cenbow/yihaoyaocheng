package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.enmu.BillTypeEnum;
import com.yyw.yhyc.order.enmu.BuyerOrderStatusEnum;
import com.yyw.yhyc.order.mapper.OrderDeliveryMapper;
import com.yyw.yhyc.order.mapper.OrderDetailMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;

/**
 * 买家采购订单导出服务
 * @author wangkui01
 *
 */
@Service("orderBuyerExportService")
public class OrderBuyerExportService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderDeliveryMapper orderDeliveryMapper;
	@Autowired
	private OrderDetailMapper orderDetailMapper;
	@Autowired
	private OrderService orderService;
	@Autowired
	private SystemPayTypeService payTypeService;
	
	private int createRowData(HSSFSheet sheet,Order order,int rownum,HSSFCellStyle cellStyle,HSSFCellStyle contentStyle ) throws Exception{
		
		List<OrderDetail> orderDetailList=this.orderDetailMapper.listOrderDetailInfoByOrderFlowId(order.getFlowId()); //订单详情
		OrderDelivery orderDelivery=this.orderDeliveryMapper.getByFlowId(order.getFlowId()); //订单发货信息
		HSSFRow row=null;
		HSSFCell cell=null;
		
		 row = sheet.createRow(rownum);  
		
		HSSFCell cell00 = row.createCell(0);
		cell00.setCellValue("下单时间");
		cell00.setCellStyle(cellStyle);
		
		HSSFCell cell01 = row.createCell(1);
		cell01.setCellValue(order.getCreateTime());
		cell01.setCellStyle(contentStyle);
		
		
		
		
		HSSFCell cell02 = row.createCell(2);
		cell02.setCellValue("订单号");
		cell02.setCellStyle(cellStyle);
		
		
		HSSFCell cell03 = row.createCell(3);
		cell03.setCellValue(order.getFlowId());
		cell03.setCellStyle(contentStyle);
		
		HSSFCell cell04 = row.createCell(4);
		cell04.setCellValue("订单状态");
		cell04.setCellStyle(cellStyle);
		
		
		
		
		HSSFCell cell05 = row.createCell(5);
		SystemPayType systemPayType=this.payTypeService.getByPK(order.getPayTypeId());
		BuyerOrderStatusEnum orderStatusName=this.orderService.getBuyerOrderStatus(order.getOrderStatus(),systemPayType.getPayType());
		cell05.setCellValue(orderStatusName.getValue());
		cell05.setCellStyle(contentStyle);
		
		HSSFCell cell06 = row.createCell(6);
		cell06.setCellValue("发票类型");
		cell06.setCellStyle(cellStyle);
		
		HSSFCell cell07 = row.createCell(7);
		cell07.setCellValue(BillTypeEnum.getBillTypeName(order.getBillType()));
		cell07.setCellStyle(contentStyle);
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("供应商");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(order.getSupplyName());
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
		cell.setCellValue(orderDelivery.getDeliveryContactPerson());
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
		cell.setCellValue(order.getCustName());
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
		
		if(!UtilHelper.isEmpty(orderDetailList)){
			for(OrderDetail orderDetail : orderDetailList){
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
				cell.setCellValue(orderDetail.getProductCount()+"");
				cell.setCellStyle(contentStyle);
				
				BigDecimal produceMoney=orderDetail.getProductPrice().multiply(new BigDecimal(orderDetail.getProductCount()));
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
		cell.setCellValue(order.getOrderTotal()+"");
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(2);
		cell.setCellValue("优惠金额");
		cell.setCellStyle(cellStyle);
		
		
		cell= row.createCell(3);
		cell.setCellValue(order.getPreferentialMoney()+"");
		cell.setCellStyle(contentStyle);
		
		cell= row.createCell(4);
		cell.setCellValue("订单金额(元)");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(5);
		cell.setCellValue(order.getOrgTotal()+"");
		cell.setCellStyle(contentStyle);
		
		rownum++;
		row = sheet.createRow(rownum); 
		
		cell= row.createCell(0);
		cell.setCellValue("买家留言");
		cell.setCellStyle(cellStyle);
		
		cell= row.createCell(1);
		cell.setCellValue(order.getLeaveMessage());
		cell.setCellStyle(contentStyle);
		
		return rownum;
		
	}
	
	public HSSFWorkbook exportSaleOrder(OrderDto orderDto) throws Exception{
		
		HSSFWorkbook  wb = new HSSFWorkbook(); 
		HSSFSheet sheet = wb.createSheet("明细");
		//设置列宽
		sheet.setDefaultColumnWidth(30);  
		sheet.setDefaultRowHeightInPoints(20); 
		
		HSSFCellStyle cellStyle =OrderExportCellHelp.createCellStyle(wb);
		
		HSSFCellStyle contentStyle = OrderExportCellHelp.createContentCellStyle(wb);
		
		List<Order> orderList=this.orderMapper.queryOrderForExportExcel(orderDto);
		
		int increaseNum=3;
		
		if(!UtilHelper.isEmpty(orderList)){
			int rownum=0;
			for(int i=0;i<orderList.size();i++){
				Order order=orderList.get(i);
				if(i>0){
					rownum+=increaseNum;
				}
				rownum = this.createRowData(sheet, order, rownum, cellStyle, contentStyle);
				
			}
		}
		return wb;
	}

}
