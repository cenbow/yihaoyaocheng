package com.yyw.yhyc.order.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.stereotype.Service;

@Service("orderExportService")
public class OrderExportService {
	private Log log = LogFactory.getLog(OrderExportService.class);

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
	
	public void fillEmptyCell(HSSFRow row,HSSFCellStyle cellStyle,int start,int n){
    	for(int i=start+1;i<=n;i++){
    		HSSFCell cell = row.createCell(i);
    		cell.setCellValue("");
    		cell.setCellStyle(cellStyle);
    	}
    }
	
	
}
