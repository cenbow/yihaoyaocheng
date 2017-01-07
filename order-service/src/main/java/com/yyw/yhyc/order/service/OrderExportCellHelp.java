package com.yyw.yhyc.order.service;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;

public class OrderExportCellHelp {
	
	public static HSSFCellStyle createCellStyle(HSSFWorkbook wb) {
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

	public static HSSFCellStyle createContentCellStyle(HSSFWorkbook wb) {
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

}
