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

import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.order.service.OrderDetailService;
import com.yyw.yhyc.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;

@Controller
@RequestMapping(value = "/order/orderDetail")
public class OrderDetailController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderDetailController.class);

	@Autowired
	private OrderDetailService orderDetailService;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public OrderDetail getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return orderDetailService.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<OrderDetail> listPgOrderDetail(RequestModel<OrderDetail> requestModel) throws Exception
	{
		Pagination<OrderDetail> pagination = new Pagination<OrderDetail>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return orderDetailService.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(OrderDetail orderDetail) throws Exception
	{
		orderDetailService.save(orderDetail);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderDetailService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(OrderDetail orderDetail) throws Exception
	{
		orderDetailService.update(orderDetail);
	}

	/**
	 * 通过主键查询实体对象
	 * @return
	 */
	@RequestMapping(value = {"/downLoad"}, method = RequestMethod.GET)
	@ResponseBody
	public void downLoad(@RequestParam("filePath") String filePath,@RequestParam("fileName")  String fileName ) throws Exception {
		//下载已有的文件
		String zhPath = new String(filePath.getBytes("iso8859-1"),"UTF-8");
		String zhName = new String(fileName.getBytes("iso8859-1"),"UTF-8");
		downLoad(zhPath, zhName, response, false);
	}



	public static void downLoad(String filePath,String fileName , HttpServletResponse response, boolean isOnLine)
			throws Exception {
		String excelName = new String((fileName+new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())+".xls").getBytes("gbk"),"iso-8859-1");


		File f = new File(filePath);
		if (!f.exists()) {
			response.sendError(404, "File not found!");
			return;
		}
		BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
		byte[] buf = new byte[1024];
		int len = 0;
		response.reset(); //非常重要
		if (isOnLine) { //在线打开方式
			URL u = new URL("file:///" + filePath);
			response.setContentType(u.openConnection().getContentType());
			response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
			//文件名应该编码成UTF-8
		} else { //纯下载方式
			response.setContentType("application/x-msdownload;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment;filename="+excelName);
		}
		OutputStream out = response.getOutputStream();
		while ((len = br.read(buf)) > 0)
			out.write(buf, 0, len);
		br.close();
		out.close();
	}

}
