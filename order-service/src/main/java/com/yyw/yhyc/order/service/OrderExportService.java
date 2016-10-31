package com.yyw.yhyc.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.dto.OrderExceptionDto;
import com.yyw.yhyc.order.enmu.BillTypeEnum;

@Service("orderExportService")
public class OrderExportService {
	private Log log = LogFactory.getLog(OrderExportService.class);
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderExceptionService orderExceptionService;
	
	private final static Map<String, String> orderTypeMap = new HashMap<String, String>(); 
	
	static{
		orderTypeMap.put("1", "退货");
		orderTypeMap.put("2", "换货");
		orderTypeMap.put("3", "补货");
		orderTypeMap.put("4", "拒收");
	}

	public HSSFCellStyle createCellStyle(HSSFWorkbook wb) {
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		// 设置标题字体格式
		Font font = wb.createFont();
		// 设置字体样式
		font.setFontHeightInPoints((short) 10);
		font.setFontName("微软雅黑");
		style.setFont(font);

		HSSFPalette palette = wb.getCustomPalette();
		palette.setColorAtIndex(HSSFColor.LAVENDER.index, (byte) (184), (byte) (204), (byte) (228));
		style.setFillForegroundColor(HSSFColor.LAVENDER.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// style.setWrapText(true);
		style.setBorderBottom((short) 1);
		style.setBorderLeft((short) 1);
		style.setBorderRight((short) 1);
		style.setBorderTop((short) 1);
		return style;
	}

	public HSSFCellStyle createContentCellStyle(HSSFWorkbook wb) {
		HSSFCellStyle style = wb.createCellStyle(); // 样式对象
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
		// 设置标题字体格式
		HSSFFont font = wb.createFont();
		// 设置字体样式
		font.setFontHeightInPoints((short) 10);
		font.setFontName("微软雅黑");
		style.setFont(font);
		// style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
		// style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		// style.setWrapText(true);
		style.setBorderBottom((short) 1);
		style.setBorderLeft((short) 1);
		style.setBorderRight((short) 1);
		style.setBorderTop((short) 1);
		return style;

	}

	public void fillEmptyCell(HSSFRow row, HSSFCellStyle cellStyle, int start, int n) {
		for (int i = start + 1; i <= n; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue("");
			cell.setCellStyle(cellStyle);
		}
	}

	private int createExceptionInitRow(HSSFSheet sheet, HSSFCellStyle cellStyle, int rownum,String type) {
		HSSFRow row1 = sheet.createRow(rownum);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellValue("下单时间");
		cell11.setCellStyle(cellStyle);

		HSSFCell cell12 = row1.createCell(2);
		cell12.setCellValue(orderTypeMap.get(type)+"订单号");
		cell12.setCellStyle(cellStyle);

		HSSFCell cell13 = row1.createCell(4);
		cell13.setCellValue("原订单号");
		cell13.setCellStyle(cellStyle);

		HSSFCell cell14 = row1.createCell(6);
		cell14.setCellValue("发票类型");
		cell14.setCellStyle(cellStyle);

		rownum++;

		HSSFRow row2 = sheet.createRow(rownum);
		HSSFCell cell21 = row2.createCell(0);
		cell21.setCellValue("供应商");
		cell21.setCellStyle(cellStyle);

		HSSFCell cell22 = row2.createCell(2);
		cell22.setCellValue("发货地址");
		cell22.setCellStyle(cellStyle);

		HSSFCell cell23 = row2.createCell(4);
		cell23.setCellValue("发货联系人");
		cell23.setCellStyle(cellStyle);

		HSSFCell cell24 = row2.createCell(6);
		cell24.setCellValue("发货人联系方式");
		cell24.setCellStyle(cellStyle);

		rownum++;
		HSSFRow row3 = sheet.createRow(rownum);
		HSSFCell cell31 = row3.createCell(0);
		cell31.setCellValue("");
		cell31.setCellStyle(cellStyle);

		HSSFCell cell32 = row3.createCell(2);
		cell32.setCellValue("收货地址");
		cell32.setCellStyle(cellStyle);

		HSSFCell cell33 = row3.createCell(4);
		cell33.setCellValue("收货联系人");
		cell33.setCellStyle(cellStyle);

		HSSFCell cell34 = row3.createCell(6);
		cell34.setCellValue("收货人联系方式");
		cell34.setCellStyle(cellStyle);

		rownum++;
		HSSFRow row4 = sheet.createRow(rownum);

		HSSFCell cell41 = row4.createCell(0);
		cell41.setCellValue("商品编码");
		cell41.setCellStyle(cellStyle);

		HSSFCell cell42 = row4.createCell(1);
		cell42.setCellValue("通用名");
		cell42.setCellStyle(cellStyle);

		HSSFCell cell43 = row4.createCell(2);
		cell43.setCellValue("规格");
		cell43.setCellStyle(cellStyle);

		HSSFCell cell44 = row4.createCell(3);
		cell44.setCellValue("厂商");
		cell44.setCellStyle(cellStyle);

		HSSFCell cell54 = row4.createCell(4);
		cell54.setCellValue("单价（元）");
		cell54.setCellStyle(cellStyle);

		HSSFCell cell64 = row4.createCell(5);
		cell64.setCellValue("数量");
		cell64.setCellStyle(cellStyle);

		HSSFCell cell74 = row4.createCell(6);
		cell74.setCellValue("金额（元）");
		cell74.setCellStyle(cellStyle);

		HSSFCell cell84 = row4.createCell(7);
		cell84.setCellValue("促销信息");
		cell84.setCellStyle(cellStyle);

		return rownum;
	}
	
	private int createReturnInitRow(HSSFSheet sheet, HSSFCellStyle cellStyle, int rownum) {
		HSSFRow row1 = sheet.createRow(rownum);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellValue("下单时间");
		cell11.setCellStyle(cellStyle);

		HSSFCell cell12 = row1.createCell(2);
		cell12.setCellValue("退货订单号");
		cell12.setCellStyle(cellStyle);

		HSSFCell cell13 = row1.createCell(4);
		cell13.setCellValue("原订单号");
		cell13.setCellStyle(cellStyle);

		HSSFCell cell14 = row1.createCell(6);
		cell14.setCellValue("发票类型");
		cell14.setCellStyle(cellStyle);

		rownum++;

		HSSFRow row2 = sheet.createRow(rownum);
		HSSFCell cell21 = row2.createCell(0);
		cell21.setCellValue("供应商");
		cell21.setCellStyle(cellStyle);

		HSSFCell cell22 = row2.createCell(2);
		cell22.setCellValue("供应商收货地址");
		cell22.setCellStyle(cellStyle);

		HSSFCell cell23 = row2.createCell(4);
		cell23.setCellValue("供应商收货联系人");
		cell23.setCellStyle(cellStyle);

		HSSFCell cell24 = row2.createCell(6);
		cell24.setCellValue("供应商收货人联系方式");
		cell24.setCellStyle(cellStyle);

		rownum++;
		HSSFRow row3 = sheet.createRow(rownum);
		HSSFCell cell31 = row3.createCell(0);
		cell31.setCellValue("");
		cell31.setCellStyle(cellStyle);

		HSSFCell cell32 = row3.createCell(2);
		cell32.setCellValue("采购商退货发货地址");
		cell32.setCellStyle(cellStyle);

		HSSFCell cell33 = row3.createCell(4);
		cell33.setCellValue("采购商退货发货联系人");
		cell33.setCellStyle(cellStyle);

		HSSFCell cell34 = row3.createCell(6);
		cell34.setCellValue("采购商退货发货人联系方式");
		cell34.setCellStyle(cellStyle);

		rownum++;
		HSSFRow row4 = sheet.createRow(rownum);

		HSSFCell cell41 = row4.createCell(0);
		cell41.setCellValue("商品编码");
		cell41.setCellStyle(cellStyle);

		HSSFCell cell42 = row4.createCell(1);
		cell42.setCellValue("通用名");
		cell42.setCellStyle(cellStyle);

		HSSFCell cell43 = row4.createCell(2);
		cell43.setCellValue("规格");
		cell43.setCellStyle(cellStyle);

		HSSFCell cell44 = row4.createCell(3);
		cell44.setCellValue("厂商");
		cell44.setCellStyle(cellStyle);

		HSSFCell cell54 = row4.createCell(4);
		cell54.setCellValue("单价（元）");
		cell54.setCellStyle(cellStyle);

		HSSFCell cell64 = row4.createCell(5);
		cell64.setCellValue("数量");
		cell64.setCellStyle(cellStyle);

		HSSFCell cell74 = row4.createCell(6);
		cell74.setCellValue("金额（元）");
		cell74.setCellStyle(cellStyle);

		HSSFCell cell84 = row4.createCell(7);
		cell84.setCellValue("促销信息");
		cell84.setCellStyle(cellStyle);

		return rownum;
	}
	
	private int createChangeInitRow(HSSFSheet sheet, HSSFCellStyle cellStyle, int rownum) {
		HSSFRow row1 = sheet.createRow(rownum);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellValue("下单时间");
		cell11.setCellStyle(cellStyle);

		HSSFCell cell12 = row1.createCell(2);
		cell12.setCellValue("换货订单号");
		cell12.setCellStyle(cellStyle);

		HSSFCell cell13 = row1.createCell(4);
		cell13.setCellValue("原订单号");
		cell13.setCellStyle(cellStyle);

		HSSFCell cell14 = row1.createCell(6);
		cell14.setCellValue("发票类型");
		cell14.setCellStyle(cellStyle);

		rownum++;

		HSSFRow row2 = sheet.createRow(rownum);
		HSSFCell cell21 = row2.createCell(0);
		cell21.setCellValue("供应商");
		cell21.setCellStyle(cellStyle);

		HSSFCell cell22 = row2.createCell(2);
		cell22.setCellValue("供应商换货收货地址");
		cell22.setCellStyle(cellStyle);

		HSSFCell cell23 = row2.createCell(4);
		cell23.setCellValue("收货联系人");
		cell23.setCellStyle(cellStyle);

		HSSFCell cell24 = row2.createCell(6);
		cell24.setCellValue("收货人联系方式");
		cell24.setCellStyle(cellStyle);
		
		rownum++;
		HSSFRow row3 = sheet.createRow(rownum);
		HSSFCell cell31 = row3.createCell(0);
		cell31.setCellValue("");
		cell31.setCellStyle(cellStyle);

		HSSFCell cell32 = row3.createCell(2);
		cell32.setCellValue("供应商换货发货地址");
		cell32.setCellStyle(cellStyle);

		HSSFCell cell33 = row3.createCell(4);
		cell33.setCellValue("发货联系人");
		cell33.setCellStyle(cellStyle);

		HSSFCell cell34 = row3.createCell(6);
		cell34.setCellValue("发货人联系方式");
		cell34.setCellStyle(cellStyle);

		rownum++;
		HSSFRow row4 = sheet.createRow(rownum);
		HSSFCell cell41 = row4.createCell(0);
		cell41.setCellValue("");
		cell41.setCellStyle(cellStyle);

		HSSFCell cell42 = row4.createCell(2);
		cell42.setCellValue("采购商换货发货地址");
		cell42.setCellStyle(cellStyle);

		HSSFCell cell43 = row4.createCell(4);
		cell43.setCellValue("发货联系人");
		cell43.setCellStyle(cellStyle);

		HSSFCell cell44 = row4.createCell(6);
		cell44.setCellValue("发货人联系方式");
		cell44.setCellStyle(cellStyle);
		
		rownum++;
		HSSFRow row5 = sheet.createRow(rownum);
		HSSFCell cell51 = row5.createCell(0);
		cell51.setCellValue("");
		cell51.setCellStyle(cellStyle);

		HSSFCell cell52 = row5.createCell(2);
		cell52.setCellValue("采购商换货收货地址");
		cell52.setCellStyle(cellStyle);

		HSSFCell cell53 = row5.createCell(4);
		cell53.setCellValue("收货联系人");
		cell53.setCellStyle(cellStyle);

		HSSFCell cell54 = row5.createCell(6);
		cell54.setCellValue("收货人联系方式");
		cell54.setCellStyle(cellStyle);

		rownum++;
		HSSFRow row6 = sheet.createRow(rownum);

		HSSFCell cell61 = row6.createCell(0);
		cell61.setCellValue("商品编码");
		cell61.setCellStyle(cellStyle);

		HSSFCell cell62 = row6.createCell(1);
		cell62.setCellValue("通用名");
		cell62.setCellStyle(cellStyle);

		HSSFCell cell63 = row6.createCell(2);
		cell63.setCellValue("规格");
		cell63.setCellStyle(cellStyle);

		HSSFCell cell64 = row6.createCell(3);
		cell64.setCellValue("厂商");
		cell64.setCellStyle(cellStyle);

		HSSFCell cell65 = row6.createCell(4);
		cell65.setCellValue("单价（元）");
		cell65.setCellStyle(cellStyle);

		HSSFCell cell66 = row6.createCell(5);
		cell66.setCellValue("数量");
		cell66.setCellStyle(cellStyle);

		HSSFCell cell67 = row6.createCell(6);
		cell67.setCellValue("金额（元）");
		cell67.setCellStyle(cellStyle);

		HSSFCell cell68 = row6.createCell(7);
		cell68.setCellValue("促销信息");
		cell68.setCellStyle(cellStyle);

		return rownum;
	}

	private int createExtraRow(HSSFSheet sheet, HSSFCellStyle cellStyle, HSSFCellStyle contentStyle, int rownum,
			String type) {
		rownum++;
		HSSFRow row1 = sheet.createRow(rownum);
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellValue("商品金额（元）");
		cell11.setCellStyle(cellStyle);

		HSSFCell cell12 = row1.createCell(2);
		cell12.setCellValue("优惠券（元）");
		cell12.setCellStyle(cellStyle);

		HSSFCell cell13 = row1.createCell(4);
		cell13.setCellValue("订单金额（元）");
		cell13.setCellStyle(cellStyle);

		this.fillEmptyCell(row1, contentStyle, 4, 7);

		rownum++;
		HSSFRow row2 = sheet.createRow(rownum);
		HSSFCell cell21 = row2.createCell(0);
		cell21.setCellValue(orderTypeMap.get(type)+"说明");
		cell21.setCellStyle(cellStyle);
		this.fillEmptyCell(row2, contentStyle, 0, 7);
		return rownum;
	}

	private int createInitRow(HSSFSheet sheet,HSSFCellStyle cellStyle,int rownum){
    	HSSFRow row1 = sheet.createRow(rownum);  
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellValue("下单时间");
		cell11.setCellStyle(cellStyle);
		
		HSSFCell cell12 = row1.createCell(2);
		cell12.setCellValue("订单号");
		cell12.setCellStyle(cellStyle);
		
		HSSFCell cell13 = row1.createCell(4);
		cell13.setCellValue("订单状态");
		cell13.setCellStyle(cellStyle);
		
		HSSFCell cell14 = row1.createCell(6);
		cell14.setCellValue("发票类型");
		cell14.setCellStyle(cellStyle);
		
		rownum++;
		
		HSSFRow row2 = sheet.createRow(rownum);  
		HSSFCell cell21 = row2.createCell(0);
		cell21.setCellValue("供应商");
		cell21.setCellStyle(cellStyle);
		
		HSSFCell cell22 = row2.createCell(2);
		cell22.setCellValue("发货地址");
		cell22.setCellStyle(cellStyle);
		
		HSSFCell cell23 = row2.createCell(4);
		cell23.setCellValue("发货联系人");
		cell23.setCellStyle(cellStyle);
		
		HSSFCell cell24 = row2.createCell(6);
		cell24.setCellValue("发货人联系方式");
		cell24.setCellStyle(cellStyle);
		
		rownum++;
		HSSFRow row3 = sheet.createRow(rownum);  
		HSSFCell cell31 = row3.createCell(0);
		cell31.setCellValue("");
		cell31.setCellStyle(cellStyle);
		
		HSSFCell cell32 = row3.createCell(2);
		cell32.setCellValue("收货地址");
		cell32.setCellStyle(cellStyle);
		
		HSSFCell cell33 = row3.createCell(4);
		cell33.setCellValue("收货联系人");
		cell33.setCellStyle(cellStyle);
		
		HSSFCell cell34 = row3.createCell(6);
		cell34.setCellValue("收货人联系方式");
		cell34.setCellStyle(cellStyle);
		
		rownum++;
		HSSFRow row4 = sheet.createRow(rownum);  
		
		HSSFCell cell41 = row4.createCell(0);
		cell41.setCellValue("商品编码");
		cell41.setCellStyle(cellStyle);
		 
		HSSFCell cell42 = row4.createCell(1);
		cell42.setCellValue("通用名");
		cell42.setCellStyle(cellStyle);
		
		HSSFCell cell43 = row4.createCell(2);
		cell43.setCellValue("规格");
		cell43.setCellStyle(cellStyle);
		
		HSSFCell cell44 = row4.createCell(3);
		cell44.setCellValue("厂商");
		cell44.setCellStyle(cellStyle);
		
		HSSFCell cell54 = row4.createCell(4);
		cell54.setCellValue("单价（元）");
		cell54.setCellStyle(cellStyle);
		
		HSSFCell cell64 = row4.createCell(5);
		cell64.setCellValue("数量");
		cell64.setCellStyle(cellStyle);
		
		HSSFCell cell74 = row4.createCell(6);
		cell74.setCellValue("金额（元）");
		cell74.setCellStyle(cellStyle);
		
		HSSFCell cell84 = row4.createCell(7);
		cell84.setCellValue("促销信息");
		cell84.setCellStyle(cellStyle);
		
		return rownum;
    }
    
    private int createExtraRow(HSSFSheet sheet,HSSFCellStyle cellStyle,HSSFCellStyle contentStyle,int rownum){
    	rownum++;
    	HSSFRow row1 = sheet.createRow(rownum);  
		HSSFCell cell11 = row1.createCell(0);
		cell11.setCellValue("商品金额（元）");
		cell11.setCellStyle(cellStyle);
		
		HSSFCell cell12 = row1.createCell(2);
		cell12.setCellValue("优惠券（元）");
		cell12.setCellStyle(cellStyle);
		
		HSSFCell cell13 = row1.createCell(4);
		cell13.setCellValue("订单金额（元）");
		cell13.setCellStyle(cellStyle);
		
		this.fillEmptyCell(row1,contentStyle,4,7);
		
		rownum++;
		HSSFRow row2 = sheet.createRow(rownum);  
		HSSFCell cell21 = row2.createCell(0);
		cell21.setCellValue("买家留言");
		cell21.setCellStyle(cellStyle);
		this.fillEmptyCell(row2,contentStyle,0,7);
		return rownum;
    }
    
    /**
     * 销售订单导出
     * @param orderDto
     * @return
     */
    public HSSFWorkbook exportSaleOrder(OrderDto orderDto){
    	HSSFWorkbook  wb = new HSSFWorkbook(); 
		HSSFSheet sheet = wb.createSheet("明细");
		List<Map<String,Object>> list =  orderService.getOrderDetailForExport(orderDto);
		if(list != null && list.size()>0){
			int rownum=0;
			int listSize = list.size();
			Map<String,Object> tempMap = null;
			//设置列宽
			sheet.setDefaultColumnWidth(30);  
			sheet.setDefaultRowHeightInPoints(20);  
			HSSFCellStyle cellStyle = this.createCellStyle(wb);
			
			HSSFCellStyle contentStyle = this.createContentCellStyle(wb);
			for(int i=0;i<listSize;i++){
				tempMap = list.get(i);
				String flow_id = (String)tempMap.get("flow_id");
				
				if(i>0){
					rownum +=3;
				}
				rownum = this.createInitRow(sheet, cellStyle, rownum);
				//订单的第一行
				HSSFRow row1 = sheet.getRow(rownum-3);  
				HSSFCell cell1 = row1.createCell(1);
				cell1.setCellValue((String)tempMap.get("create_time"));
				cell1.setCellStyle(contentStyle);
				
				HSSFCell cell2 = row1.createCell(3);
				cell2.setCellValue(flow_id);
				cell2.setCellStyle(contentStyle);
				
				
				HSSFCell cell3 = row1.createCell(5);
				cell3.setCellValue(orderService.getBuyerOrderStatus((String)tempMap.get("order_status"),
						(Integer)tempMap.get("pay_type")).getValue());
				cell3.setCellStyle(contentStyle);
				
				HSSFCell cell4 = row1.createCell(7);
				cell4.setCellValue(BillTypeEnum.getBillTypeName((Integer)tempMap.get("bill_type")));
				cell4.setCellStyle(contentStyle);
				
				//订单的第二行
				HSSFRow row2 = sheet.getRow(rownum-2);  
				HSSFCell cell5 = row2.createCell(1);
				cell5.setCellValue((String)tempMap.get("supply_name"));
				cell5.setCellStyle(contentStyle);
				
				HSSFCell cell6 = row2.createCell(3);
				cell6.setCellValue((String)tempMap.get("delivery_address"));
				cell6.setCellStyle(contentStyle);
				
				HSSFCell cell7 = row2.createCell(5);
				cell7.setCellValue((String)tempMap.get("delivery_person"));
				cell7.setCellStyle(contentStyle);
				
				HSSFCell cell8 = row2.createCell(7);
				cell8.setCellValue((String)tempMap.get("delivery_contact_phone"));
				cell8.setCellStyle(contentStyle);
				
				//订单的第三行
				HSSFRow row3 = sheet.getRow(rownum-1);  
				HSSFCell cell9 = row3.createCell(3);
				cell9.setCellValue((String)tempMap.get("receive_address"));
				cell9.setCellStyle(contentStyle);
				
				HSSFCell cell10 = row3.createCell(5);
				cell10.setCellValue((String)tempMap.get("receive_person"));
				cell10.setCellStyle(contentStyle);
				
				HSSFCell cell11 = row3.createCell(7);
				cell11.setCellValue((String)tempMap.get("receive_contact_phone"));
				cell11.setCellStyle(contentStyle);
				
				//明细
				String orderDetail = (String)tempMap.get("detail");
				//商品金额
				double productAmount = 0.00;
				if(StringUtils.isNotBlank(orderDetail)){
					String[] orderDetailArray = orderDetail.split(";");
					for(int j=0;j<orderDetailArray.length;j++){
						rownum++;
						String[] detail = orderDetailArray[j].split(",");
					 
						HSSFRow detailRow = sheet.createRow(rownum);  
						HSSFCell newcell1 = detailRow.createCell(0);
						newcell1.setCellValue(detail[0]);
						newcell1.setCellStyle(contentStyle);
						
						HSSFCell newcell2 = detailRow.createCell(1);
						newcell2.setCellValue(detail[1]);
						newcell2.setCellStyle(contentStyle);
						
						HSSFCell newcell3 = detailRow.createCell(2);
						newcell3.setCellValue(detail[2]);
						newcell3.setCellStyle(contentStyle);
						
						HSSFCell newcell4 = detailRow.createCell(3);
						newcell4.setCellValue(detail[3]);
						newcell4.setCellStyle(contentStyle);
						
						HSSFCell newcell5 = detailRow.createCell(4);
						newcell5.setCellValue(detail[4]);
						newcell5.setCellStyle(contentStyle);
						
						HSSFCell newcell6 = detailRow.createCell(5);
						newcell6.setCellValue(detail[5]);
						newcell6.setCellStyle(contentStyle);
						
						HSSFCell newcell7 = detailRow.createCell(6);
						newcell7.setCellValue(detail[6]);
						newcell7.setCellStyle(contentStyle);
						productAmount += Double.parseDouble(detail[6]);
						
						HSSFCell newcell8 = detailRow.createCell(7);
						newcell8.setCellValue("");
						newcell8.setCellStyle(contentStyle);
					}
				}
				rownum = this.createExtraRow(sheet, cellStyle,contentStyle, rownum);
				HSSFRow detailRow = sheet.getRow(rownum-1);  
				HSSFCell newcell1 = detailRow.createCell(1);
				newcell1.setCellValue(productAmount);
				newcell1.setCellStyle(contentStyle);
				
				HSSFCell newcell2 = detailRow.createCell(3);
				newcell2.setCellValue(String.valueOf(tempMap.get("preferential_money")));
				newcell2.setCellStyle(contentStyle);
				
				HSSFCell newcell3 = detailRow.createCell(5);
				newcell3.setCellValue(String.valueOf(tempMap.get("order_total")));
				newcell3.setCellStyle(contentStyle);
				
				HSSFRow lastRow = sheet.getRow(rownum);  
				HSSFCell lastcell1 = lastRow.createCell(1);
				lastcell1.setCellValue(String.valueOf(tempMap.get("leave_message")));
				lastcell1.setCellStyle(contentStyle);
				
			}
		}
		return wb;
    }
    
	/**
	 * 补货或者拒收导出
	 * @param orderDto
	 * @return
	 */
	public HSSFWorkbook exportRelenishOrRejectOrder(OrderExceptionDto orderDto) {
		List<Map<String, Object>> list = orderExceptionService.getExportExceptionOrder(orderDto);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("明细");

		if (list != null && list.size() > 0) {
			int rownum = 0;
			int listSize = list.size();
			Map<String, Object> tempMap = null;
			// 设置列宽
			sheet.setDefaultColumnWidth(30);
			sheet.setDefaultRowHeightInPoints(20);
			HSSFCellStyle cellStyle = this.createCellStyle(wb);

			HSSFCellStyle contentStyle = this.createContentCellStyle(wb);
			for (int i = 0; i < listSize; i++) {
				tempMap = list.get(i);
				String flow_id = (String) tempMap.get("flow_id");

				if (i > 0) {
					rownum += 3;
				}
				rownum = this.createExceptionInitRow(sheet, cellStyle, rownum,orderDto.getReturnType());
				// 订单的第一行
				HSSFRow row1 = sheet.getRow(rownum - 3);
				HSSFCell cell1 = row1.createCell(1);
				cell1.setCellValue((String) tempMap.get("create_time"));
				cell1.setCellStyle(contentStyle);

				HSSFCell cell2 = row1.createCell(3);
				cell2.setCellValue((String) tempMap.get("exception_order_id"));
				cell2.setCellStyle(contentStyle);

				HSSFCell cell3 = row1.createCell(5);
				cell3.setCellValue(flow_id);
				cell3.setCellStyle(contentStyle);

				HSSFCell cell4 = row1.createCell(7);
				cell4.setCellValue(BillTypeEnum.getBillTypeName((Integer) tempMap.get("bill_type")));
				cell4.setCellStyle(contentStyle);

				// 订单的第二行
				HSSFRow row2 = sheet.getRow(rownum - 2);
				HSSFCell cell5 = row2.createCell(1);
				cell5.setCellValue((String) tempMap.get("supply_name"));
				cell5.setCellStyle(contentStyle);

				String returnType = orderDto.getReturnType();
				HSSFCell cell6 = row2.createCell(3);
				cell6.setCellStyle(contentStyle);
				
				HSSFCell cell7 = row2.createCell(5);
				cell7.setCellStyle(contentStyle);
				
				HSSFCell cell8 = row2.createCell(7);
				cell8.setCellStyle(contentStyle);
				//补货的发货地址
				if("3".equals(returnType)){
					cell6.setCellValue((String) tempMap.get("exception_delivery_address"));
					cell7.setCellValue((String) tempMap.get("exception_delivery_person"));
					cell8.setCellValue((String) tempMap.get("exception_delivery_contact_phone"));
				}else{
					cell6.setCellValue((String) tempMap.get("former_delivery_address"));
					cell7.setCellValue((String) tempMap.get("former_delivery_person"));
					cell8.setCellValue((String) tempMap.get("former_delivery_contact_phone"));
				}

				// 订单的第三行
				HSSFRow row3 = sheet.getRow(rownum - 1);
				HSSFCell cell9 = row3.createCell(3);
				cell9.setCellValue((String) tempMap.get("former_receive_address"));
				cell9.setCellStyle(contentStyle);

				HSSFCell cell10 = row3.createCell(5);
				cell10.setCellValue((String) tempMap.get("former_receive_person"));
				cell10.setCellStyle(contentStyle);

				HSSFCell cell11 = row3.createCell(7);
				cell11.setCellValue((String) tempMap.get("former_receive_contact_phone"));
				cell11.setCellStyle(contentStyle);

				// 明细
				String orderDetail = (String) tempMap.get("detail");
				// 商品金额
				double productAmount = 0.00;
				if (StringUtils.isNotBlank(orderDetail)) {
					String[] orderDetailArray = orderDetail.split(";");
					for (int j = 0; j < orderDetailArray.length; j++) {
						rownum++;
						String[] detail = orderDetailArray[j].split(",");

						HSSFRow detailRow = sheet.createRow(rownum);
						HSSFCell newcell1 = detailRow.createCell(0);
						newcell1.setCellValue(detail[0]);
						newcell1.setCellStyle(contentStyle);

						HSSFCell newcell2 = detailRow.createCell(1);
						newcell2.setCellValue(detail[1]);
						newcell2.setCellStyle(contentStyle);

						HSSFCell newcell3 = detailRow.createCell(2);
						newcell3.setCellValue(detail[2]);
						newcell3.setCellStyle(contentStyle);

						HSSFCell newcell4 = detailRow.createCell(3);
						newcell4.setCellValue(detail[3]);
						newcell4.setCellStyle(contentStyle);

						HSSFCell newcell5 = detailRow.createCell(4);
						newcell5.setCellValue(detail[4]);
						newcell5.setCellStyle(contentStyle);

						HSSFCell newcell6 = detailRow.createCell(5);
						newcell6.setCellValue(detail[5]);
						newcell6.setCellStyle(contentStyle);

						HSSFCell newcell7 = detailRow.createCell(6);
						newcell7.setCellValue(detail[6]);
						newcell7.setCellStyle(contentStyle);
						productAmount += Double.parseDouble(detail[6]);

						HSSFCell newcell8 = detailRow.createCell(7);
						newcell8.setCellValue("");
						newcell8.setCellStyle(contentStyle);
					}
				}
				rownum = this.createExtraRow(sheet, cellStyle, contentStyle, rownum,orderDto.getReturnType());
				HSSFRow detailRow = sheet.getRow(rownum - 1);
				HSSFCell newcell1 = detailRow.createCell(1);
				newcell1.setCellValue(productAmount);
				newcell1.setCellStyle(contentStyle);

				HSSFCell newcell2 = detailRow.createCell(3);
				newcell2.setCellValue(String.valueOf(tempMap.get("preferential_money")));
				newcell2.setCellStyle(contentStyle);

				HSSFCell newcell3 = detailRow.createCell(5);
				newcell3.setCellValue(String.valueOf(tempMap.get("order_money")));
				newcell3.setCellStyle(contentStyle);

				HSSFRow lastRow = sheet.getRow(rownum);
				HSSFCell lastcell1 = lastRow.createCell(1);
				lastcell1.setCellValue(String.valueOf(tempMap.get("return_desc")));
				lastcell1.setCellStyle(contentStyle);

			}

		}
		return wb;
	}
	
	/**
	 * 退货导出
	 * @param orderDto
	 * @return
	 */
	public HSSFWorkbook exportReturnOrder(OrderExceptionDto orderDto) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("明细");
		
		List<Map<String, Object>> list = orderExceptionService.getExportExceptionOrder(orderDto);
		
		if (list != null && list.size() > 0) {
			int rownum = 0;
			int listSize = list.size();
			Map<String, Object> tempMap = null;
			// 设置列宽
			sheet.setDefaultColumnWidth(30);
			sheet.setDefaultRowHeightInPoints(20);
			HSSFCellStyle cellStyle = this.createCellStyle(wb);

			HSSFCellStyle contentStyle = this.createContentCellStyle(wb);
			for (int i = 0; i < listSize; i++) {
				tempMap = list.get(i);
				String flow_id = (String) tempMap.get("flow_id");

				if (i > 0) {
					rownum += 3;
				}
				rownum = this.createReturnInitRow(sheet, cellStyle, rownum);
				// 订单的第一行
				HSSFRow row1 = sheet.getRow(rownum - 3);
				HSSFCell cell1 = row1.createCell(1);
				cell1.setCellValue((String) tempMap.get("create_time"));
				cell1.setCellStyle(contentStyle);

				HSSFCell cell2 = row1.createCell(3);
				cell2.setCellValue((String) tempMap.get("exception_order_id"));
				cell2.setCellStyle(contentStyle);

				HSSFCell cell3 = row1.createCell(5);
				cell3.setCellValue(flow_id);
				cell3.setCellStyle(contentStyle);

				HSSFCell cell4 = row1.createCell(7);
				cell4.setCellValue(BillTypeEnum.getBillTypeName((Integer) tempMap.get("bill_type")));
				cell4.setCellStyle(contentStyle);

				// 订单的第二行
				HSSFRow row2 = sheet.getRow(rownum - 2);
				HSSFCell cell5 = row2.createCell(1);
				cell5.setCellValue((String) tempMap.get("supply_name"));
				cell5.setCellStyle(contentStyle);

				HSSFCell cell6 = row2.createCell(3);
				cell6.setCellValue((String) tempMap.get("exception_receive_address"));
				cell6.setCellStyle(contentStyle);

				HSSFCell cell7 = row2.createCell(5);
				cell7.setCellValue((String) tempMap.get("exception_receive_person"));
				cell7.setCellStyle(contentStyle);

				HSSFCell cell8 = row2.createCell(7);
				cell8.setCellValue((String) tempMap.get("exception_receive_contact_phone"));
				cell8.setCellStyle(contentStyle);

				// 订单的第三行
				HSSFRow row3 = sheet.getRow(rownum - 1);
				HSSFCell cell9 = row3.createCell(3);
				cell9.setCellValue((String) tempMap.get("exception_delivery_address"));
				cell9.setCellStyle(contentStyle);

				HSSFCell cell10 = row3.createCell(5);
				cell10.setCellValue((String) tempMap.get("exception_delivery_person"));
				cell10.setCellStyle(contentStyle);

				HSSFCell cell11 = row3.createCell(7);
				cell11.setCellValue((String) tempMap.get("exception_delivery_contact_phone"));
				cell11.setCellStyle(contentStyle);

				// 明细
				String orderDetail = (String) tempMap.get("detail");
				// 商品金额
				double productAmount = 0.00;
				if (StringUtils.isNotBlank(orderDetail)) {
					String[] orderDetailArray = orderDetail.split(";");
					for (int j = 0; j < orderDetailArray.length; j++) {
						rownum++;
						String[] detail = orderDetailArray[j].split(",");

						HSSFRow detailRow = sheet.createRow(rownum);
						HSSFCell newcell1 = detailRow.createCell(0);
						newcell1.setCellValue(detail[0]);
						newcell1.setCellStyle(contentStyle);

						HSSFCell newcell2 = detailRow.createCell(1);
						newcell2.setCellValue(detail[1]);
						newcell2.setCellStyle(contentStyle);

						HSSFCell newcell3 = detailRow.createCell(2);
						newcell3.setCellValue(detail[2]);
						newcell3.setCellStyle(contentStyle);

						HSSFCell newcell4 = detailRow.createCell(3);
						newcell4.setCellValue(detail[3]);
						newcell4.setCellStyle(contentStyle);

						HSSFCell newcell5 = detailRow.createCell(4);
						newcell5.setCellValue(detail[4]);
						newcell5.setCellStyle(contentStyle);

						HSSFCell newcell6 = detailRow.createCell(5);
						newcell6.setCellValue(detail[5]);
						newcell6.setCellStyle(contentStyle);

						HSSFCell newcell7 = detailRow.createCell(6);
						newcell7.setCellValue(detail[6]);
						newcell7.setCellStyle(contentStyle);
						productAmount += Double.parseDouble(detail[6]);

						HSSFCell newcell8 = detailRow.createCell(7);
						newcell8.setCellValue("");
						newcell8.setCellStyle(contentStyle);
					}
				}
				rownum = this.createExtraRow(sheet, cellStyle, contentStyle, rownum);
				HSSFRow detailRow = sheet.getRow(rownum - 1);
				HSSFCell newcell1 = detailRow.createCell(1);
				newcell1.setCellValue(productAmount);
				newcell1.setCellStyle(contentStyle);

				HSSFCell newcell2 = detailRow.createCell(3);
				newcell2.setCellValue(String.valueOf(tempMap.get("preferential_money")));
				newcell2.setCellStyle(contentStyle);

				HSSFCell newcell3 = detailRow.createCell(5);
				newcell3.setCellValue(String.valueOf(tempMap.get("order_money")));
				newcell3.setCellStyle(contentStyle);

				HSSFRow lastRow = sheet.getRow(rownum);
				HSSFCell lastcell1 = lastRow.createCell(1);
				lastcell1.setCellValue(String.valueOf(tempMap.get("return_desc")));
				lastcell1.setCellStyle(contentStyle);

			}

		}
		return wb;
	}
	
	/**
	 * 换货导出
	 * @param orderDto
	 * @return
	 */
	public HSSFWorkbook exportChangeOrder(OrderExceptionDto orderDto) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("明细");
		
		List<Map<String, Object>> list = orderExceptionService.getExportChangeOrder(orderDto);
		
		if (list != null && list.size() > 0) {
			int rownum = 0;
			int listSize = list.size();
			Map<String, Object> tempMap = null;
			// 设置列宽
			sheet.setDefaultColumnWidth(30);
			sheet.setDefaultRowHeightInPoints(20);
			HSSFCellStyle cellStyle = this.createCellStyle(wb);

			HSSFCellStyle contentStyle = this.createContentCellStyle(wb);
			for (int i = 0; i < listSize; i++) {
				tempMap = list.get(i);
				String flow_id = (String) tempMap.get("flow_id");

				if (i > 0) {
					rownum += 3;
				}
				rownum = this.createChangeInitRow(sheet, cellStyle, rownum);
				// 订单的第一行
				HSSFRow row1 = sheet.getRow(rownum - 5);
				HSSFCell cell1 = row1.createCell(1);
				cell1.setCellValue((String) tempMap.get("create_time"));
				cell1.setCellStyle(contentStyle);

				HSSFCell cell2 = row1.createCell(3);
				cell2.setCellValue((String) tempMap.get("exception_order_id"));
				cell2.setCellStyle(contentStyle);

				HSSFCell cell3 = row1.createCell(5);
				cell3.setCellValue(flow_id);
				cell3.setCellStyle(contentStyle);

				HSSFCell cell4 = row1.createCell(7);
				cell4.setCellValue(BillTypeEnum.getBillTypeName((Integer) tempMap.get("bill_type")));
				cell4.setCellStyle(contentStyle);

				// 订单的第二行
				HSSFRow row2 = sheet.getRow(rownum - 4);
				HSSFCell cell5 = row2.createCell(1);
				cell5.setCellValue((String) tempMap.get("supply_name"));
				cell5.setCellStyle(contentStyle);
				
				//收发货信息
				String delivery = (String) tempMap.get("delivery");
				//供应商换货收货
				String sellerReceiveAddress = "";
				String sellerReceivePerson = "";
				String sellerReceiveContact = "";
				String sellerDeliveryAddress = "";
				String sellerDeliveryPerson = "";
				String sellerDeliveryContact = "";
				
				//采购商换货信息
				String buyerReceiveAddress = "";
				String buyerReceivePerson = "";
				String buyerReceiveContact = "";
				String buyerDeliveryAddress = "";
				String buyerDeliveryPerson = "";
				String buyerDeliveryContact = "";
				if (StringUtils.isNotBlank(delivery)) {
					String[] tempArray = delivery.split(";");
					String[] firstArray = tempArray[0].split("split");
					String[] secondArray = tempArray[1].split("split");
					sellerReceivePerson= firstArray[0].split(",")[0];
					sellerReceiveAddress = firstArray[0].split(",")[1];
					sellerReceiveContact =  firstArray[0].split(",")[2];
					
					buyerDeliveryPerson = firstArray[1].split(",")[0];
					buyerDeliveryAddress = firstArray[1].split(",")[1];
					buyerDeliveryContact = firstArray[1].split(",")[2];
					
					buyerReceivePerson= secondArray[0].split(",")[0];
					buyerReceiveAddress = secondArray[0].split(",")[1];
					buyerReceiveContact =  secondArray[0].split(",")[2];
					
					sellerDeliveryPerson = secondArray[1].split(",")[0];
					sellerDeliveryAddress = secondArray[1].split(",")[1];
					sellerDeliveryContact = secondArray[1].split(",")[2];
					
					
				}
				
				HSSFCell cell6 = row2.createCell(3);
				cell6.setCellValue(sellerReceiveAddress);
				cell6.setCellStyle(contentStyle);

				HSSFCell cell7 = row2.createCell(5);
				cell7.setCellValue(sellerReceivePerson);
				cell7.setCellStyle(contentStyle);

				HSSFCell cell8 = row2.createCell(7);
				cell8.setCellValue(sellerReceiveContact);
				cell8.setCellStyle(contentStyle);

				// 订单的第三行
				HSSFRow row3 = sheet.getRow(rownum - 3);
				HSSFCell cell9 = row3.createCell(3);
				cell9.setCellValue(sellerDeliveryAddress);
				cell9.setCellStyle(contentStyle);

				HSSFCell cell10 = row3.createCell(5);
				cell10.setCellValue(sellerDeliveryPerson);
				cell10.setCellStyle(contentStyle);

				HSSFCell cell11 = row3.createCell(7);
				cell11.setCellValue(sellerDeliveryContact);
				cell11.setCellStyle(contentStyle);
				
				// 订单的第四行
				HSSFRow row4 = sheet.getRow(rownum - 2);
				HSSFCell cell12 = row4.createCell(3);
				cell12.setCellValue(buyerReceiveAddress);
				cell12.setCellStyle(contentStyle);

				HSSFCell cell13 = row4.createCell(5);
				cell13.setCellValue(buyerReceivePerson);
				cell13.setCellStyle(contentStyle);

				HSSFCell cell14 = row4.createCell(7);
				cell14.setCellValue(buyerReceiveContact);
				cell14.setCellStyle(contentStyle);
				
				// 订单的第五行
				HSSFRow row5 = sheet.getRow(rownum - 1);
				HSSFCell cell15 = row5.createCell(3);
				cell15.setCellValue(buyerDeliveryAddress);
				cell15.setCellStyle(contentStyle);

				HSSFCell cell16 = row5.createCell(5);
				cell16.setCellValue(buyerDeliveryPerson);
				cell16.setCellStyle(contentStyle);

				HSSFCell cell17 = row5.createCell(7);
				cell17.setCellValue(buyerDeliveryContact);
				cell17.setCellStyle(contentStyle);


				// 明细
				String orderDetail = (String) tempMap.get("detail");
				// 商品金额
				double productAmount = 0.00;
				if (StringUtils.isNotBlank(orderDetail)) {
					String[] orderDetailArray = orderDetail.split(";");
					for (int j = 0; j < orderDetailArray.length; j++) {
						rownum++;
						String[] detail = orderDetailArray[j].split(",");

						HSSFRow detailRow = sheet.createRow(rownum);
						HSSFCell newcell1 = detailRow.createCell(0);
						newcell1.setCellValue(detail[0]);
						newcell1.setCellStyle(contentStyle);

						HSSFCell newcell2 = detailRow.createCell(1);
						newcell2.setCellValue(detail[1]);
						newcell2.setCellStyle(contentStyle);

						HSSFCell newcell3 = detailRow.createCell(2);
						newcell3.setCellValue(detail[2]);
						newcell3.setCellStyle(contentStyle);

						HSSFCell newcell4 = detailRow.createCell(3);
						newcell4.setCellValue(detail[3]);
						newcell4.setCellStyle(contentStyle);

						HSSFCell newcell5 = detailRow.createCell(4);
						newcell5.setCellValue(detail[4]);
						newcell5.setCellStyle(contentStyle);

						HSSFCell newcell6 = detailRow.createCell(5);
						newcell6.setCellValue(detail[5]);
						newcell6.setCellStyle(contentStyle);

						HSSFCell newcell7 = detailRow.createCell(6);
						newcell7.setCellValue(detail[6]);
						newcell7.setCellStyle(contentStyle);
						productAmount += Double.parseDouble(detail[6]);

						HSSFCell newcell8 = detailRow.createCell(7);
						newcell8.setCellValue("");
						newcell8.setCellStyle(contentStyle);
					}
				}
				rownum = this.createExtraRow(sheet, cellStyle, contentStyle, rownum);
				HSSFRow detailRow = sheet.getRow(rownum - 1);
				HSSFCell newcell1 = detailRow.createCell(1);
				newcell1.setCellValue(productAmount);
				newcell1.setCellStyle(contentStyle);

				HSSFCell newcell2 = detailRow.createCell(3);
				newcell2.setCellValue(String.valueOf(tempMap.get("preferential_money")));
				newcell2.setCellStyle(contentStyle);

				HSSFCell newcell3 = detailRow.createCell(5);
				newcell3.setCellValue(String.valueOf(tempMap.get("order_money")));
				newcell3.setCellStyle(contentStyle);

				HSSFRow lastRow = sheet.getRow(rownum);
				HSSFCell lastcell1 = lastRow.createCell(1);
				lastcell1.setCellValue(String.valueOf(tempMap.get("return_desc")));
				lastcell1.setCellStyle(contentStyle);

			}

		}
		return wb;
	}

}
