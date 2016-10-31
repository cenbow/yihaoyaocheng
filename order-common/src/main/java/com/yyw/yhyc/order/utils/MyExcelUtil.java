package com.yyw.yhyc.order.utils;

import java.beans.PropertyDescriptor;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.poi.util.StringUtil;

import com.yyw.yhyc.helper.UtilHelper;

import jxl.Workbook;
import jxl.biff.DisplayFormat;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;



/**
 * Excel的工具类
 * @author zhaolingzhi
 * 2015年12月9日
 * @since
 * @version 1.0
 */
public class MyExcelUtil {
	
	//控制Excel里面每个sheet单元最多有多少行
	private static int MAX_ROWS = 50000;
	
	/**
	 * 填充Excel各单元格的值
	 * @param <T>
	 * @param list
	 * @param os
	 * @param colNames第一行列字段的中文名称
	 * @param colParams各列对应对象的属性
	 * @param sheetName sheet的名称
	 */
	public static <T> void setExcel(List<T> list,OutputStream os,String[] colNames,String[] colParams,String sheetName){
	
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			//设定一个sheet里面只能放50000条数据
			int num = list.size()/MAX_ROWS + 1;
			
			for(int i=0;i<num;i++){
				
				WritableSheet sheet = null;
				if(UtilHelper.isEmpty(sheetName)){
					sheet = workbook.createSheet("sheet"+i, 0);
				}else{
				    sheet = workbook.createSheet(sheetName+i, 0);
				}
				
				//设置第一行标题的样式
				jxl.write.WritableFont wfc = new jxl.write.WritableFont(
						WritableFont.ARIAL, 10, WritableFont.BOLD, false,
						jxl.format.UnderlineStyle.NO_UNDERLINE,
						jxl.format.Colour.RED);
				jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(
						wfc);
				wcfFC.setBackground(jxl.format.Colour.YELLOW);
				wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
				wcfFC.setAlignment(Alignment.CENTRE); // 文字水平对齐
				
				//设置单元格值的样式
				jxl.write.WritableCellFormat wcf_cell = new jxl.write.WritableCellFormat();
				wcf_cell.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
				wcf_cell.setAlignment(Alignment.CENTRE); // 文字水平对齐

				//给Excel第一行的列名赋予中文名称
				for(int j=0;j<colNames.length;j++){
					jxl.write.Label labelCFC = new jxl.write.Label(j, 0, colNames[j],
							wcfFC);
					sheet.addCell(labelCFC);
					
				}
				
				//参数list集合遍历循环的起始值和结束值
				int num_start = i*MAX_ROWS;
				int num_end = (i+1)*MAX_ROWS;
				int rowNum = 0;
				
				if(num_end > list.size()){
					num_end = list.size();
				}
				for(int k=num_start;k<num_end;k++){
					Map obj = beanToMap(list.get(k));
					Label label = null;
					int colNum = 0;
					
					//循环遍历各列对应对象的属性，并取出值，填充单元格
					for(String temp:colParams){
						String value = String.valueOf(obj.get(temp));
						if(UtilHelper.isEmpty(value) || "null".equals(value)){
							label = new jxl.write.Label(colNum++,rowNum+1,"",wcf_cell);
						}else{
							label = new jxl.write.Label(colNum++,rowNum+1,value,wcf_cell);
						}
						sheet.addCell(label);
					}
					
					rowNum++;
				}
				
			}
			
			workbook.write();
			workbook.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		
		}
		
	}
	
	/**
	 * 实体类转换为Map集合
	 * @param obj
	 * @return
	 */
	 public static Map<String, Object> beanToMap(Object obj) {
		 
		 if(obj instanceof Map || obj instanceof LinkedMap){
			 return (Map)obj;
		 }
		 
         Map<String, Object> params = new HashMap<String, Object>(0); 
         try { 
             PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean(); 
             PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj); 
             for (int i = 0; i < descriptors.length; i++) { 
                 String name = descriptors[i].getName(); 
                 if (!"class".equals(name)) { 
                     params.put(name, propertyUtilsBean.getNestedProperty(obj, name)); 
                 } 
             } 
         } catch (Exception e) { 
             e.printStackTrace(); 
         } 
         return params; 
	 }
	 
	 /**
		 * 填充Excel，并对某一列设置其格式样式和宽度
		 * @param list
		 * @param os
		 * @param colNames
		 * @param colParams
		 * @param sheetName
		 * @param colStyle
		 */
		public static <T> void setExcelByCol(List<T> list,OutputStream os,String[] colNames,String[] colParams,String sheetName,Object[] colStyle){
		
			try {
				WritableWorkbook workbook = Workbook.createWorkbook(os);
				//设定一个sheet里面只能放50000条数据
				int num = list.size()/MAX_ROWS + 1;
				
				for(int i=0;i<num;i++){
					
					WritableSheet sheet = null;
					if(UtilHelper.isEmpty(sheetName)){
						sheet = workbook.createSheet("sheet"+i, 0);
					}else{
					    sheet = workbook.createSheet(sheetName+i, 0);
					}
					
					jxl.write.WritableFont wfc = new jxl.write.WritableFont(
							WritableFont.ARIAL, 10, WritableFont.BOLD, false,
							jxl.format.UnderlineStyle.NO_UNDERLINE,
							jxl.format.Colour.RED);
					jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(
							wfc);
					wcfFC.setBackground(jxl.format.Colour.YELLOW);
					wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
					wcfFC.setAlignment(Alignment.CENTRE); // 文字水平对齐
					
					//设置单元格值的样式
					jxl.write.WritableCellFormat wcf_cell = new jxl.write.WritableCellFormat();
					wcf_cell.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
					wcf_cell.setAlignment(Alignment.CENTRE); // 文字水平对齐
					
					//设置某一列宽度
					if(colStyle.length == 4){
						sheet.setColumnView(Integer.valueOf(colStyle[2].toString()),Integer.valueOf(colStyle[3].toString()));
					}
					//给单独某一列设置样式
					jxl.write.WritableCellFormat wcfCol = new jxl.write.WritableCellFormat(
							(DisplayFormat)colStyle[1]); //定义一个单元格样式  NumberFormats.FORMAT7
					wcfCol.setAlignment(Alignment.CENTRE);
					wcfCol.setVerticalAlignment(VerticalAlignment.CENTRE);
					jxl.write.Number number = null;
					
					 //CellView cv = new CellView(); //定义一个列显示样式 
				     //cv.setFormat(wcfCol);//把定义的单元格格式初始化进去
				     //cv.setSize(20);//设置列宽度（不设置的话是0，不会显示）
					//sheet.setColumnView(Integer.valueOf(colStyle[0].toString()), cv);
					
					//给Excel第一行的列名赋予中文名称
					for(int j=0;j<colNames.length;j++){
						jxl.write.Label labelCFC = new jxl.write.Label(j, 0, colNames[j],
								wcfFC);
						sheet.addCell(labelCFC);
					}
					
					//参数list集合遍历循环的起始值和结束值
					int num_start = i*MAX_ROWS;
					int num_end = (i+1)*MAX_ROWS;
					int rowNum = 0;
					
					if(num_end > list.size()){
						num_end = list.size();
					}
					for(int k=num_start;k<num_end;k++){
						Map obj = beanToMap(list.get(k));
						Label label = null;
						int colNum = 0;
						
						//循环遍历各列对应对象的属性，并取出值，填充单元格
						for(String temp:colParams){
							String value = String.valueOf(obj.get(temp));
							if(temp.equals(colStyle[0].toString()) && isNum(value)){
								number = new jxl.write.Number(colNum++,rowNum+1,Double.parseDouble(value),wcfCol);
								sheet.addCell(number);
							}else{
								if(UtilHelper.isEmpty(value) || "null".equals(value)){
									label = new jxl.write.Label(colNum++,rowNum+1,"",wcf_cell);
								}else{
									label = new jxl.write.Label(colNum++,rowNum+1,value,wcf_cell);
								}
								sheet.addCell(label);
							}
						}
						
						rowNum++;
					}
					
				}
				
				workbook.write();
				workbook.close();
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			
			}
			
		}
		
		/**
		 * 判断其是否为数字
		 * @param str
		 * @return
		 */
		public static boolean isNum(String str){
			return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
		}
}
