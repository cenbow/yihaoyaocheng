/**
 *
 * Created By: XI
 * Created On: 2016-7-28 17:34:55
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.order.service.OrderExceptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.dto.OrderDeliveryDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.OrderDeliveryService;
import com.yyw.yhyc.order.service.OrderLogService;
import com.yyw.yhyc.order.service.OrderService;
import com.yyw.yhyc.usermanage.bo.UsermanageReceiverAddress;
import com.yyw.yhyc.utils.MyConfigUtil;

@Controller
@RequestMapping(value = "/order/orderDelivery")
public class OrderDeliveryController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(OrderDeliveryController.class);

	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private OrderDeliveryService orderDeliveryService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderExceptionService orderExceptionService;


	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public OrderDelivery getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return orderDeliveryService.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<OrderDelivery> listPgOrderDelivery(RequestModel<OrderDelivery> requestModel) throws Exception
	{
		Pagination<OrderDelivery> pagination = new Pagination<OrderDelivery>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return orderDeliveryService.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(OrderDelivery orderDelivery) throws Exception
	{
		orderDeliveryService.save(orderDelivery);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(RequestListModel<Integer> requestListModel) throws Exception
	{
		orderDeliveryService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(OrderDelivery orderDelivery) throws Exception
	{
		orderDeliveryService.update(orderDelivery);
	}


	/**
	 * 确认发货
	 * @return
	 */
	@RequestMapping(value = "/sendOrderDelivery", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> sendOrderDelivery(OrderDeliveryDto orderDeliveryDto,HttpServletRequest request,MultipartFile excelFile) throws Exception
	{
		UserDto user = super.getLoginUser();
		orderDeliveryDto.setUserDto(user);
		//验证通过生成发货信息并上传文件
		if(!UtilHelper.isEmpty(excelFile)){
			orderDeliveryDto.setPath(MyConfigUtil.FILE_PATH);
			String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + "发货批号导入信息" + ".xls";
			SaveFileFromInputStream(excelFile.getInputStream(), orderDeliveryDto.getPath(), fileName);
			orderDeliveryDto.setFileName(fileName);
		}else {
			orderDeliveryDto.setFileName("");
			orderDeliveryDto.setPath("");
		}
		orderLogService.insertOrderLog(this.request,"2",user.getCustId(),orderDeliveryDto.getFlowId(),orderService.getOrderbyFlowId(orderDeliveryDto.getFlowId()).getSource() );
		return orderDeliveryService.updateSendOrderDelivery(orderDeliveryDto);
	}
	/**
	 * 退货，买家确认发货
	 * @return
	 */
	@RequestMapping(value = "/sendOrderDeliveryForRefund", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> sendOrderDeliveryForRefund(OrderDeliveryDto orderDeliveryDto) throws Exception
	{
		UserDto user = super.getLoginUser();
		orderDeliveryDto.setUserDto(user);

		//不需要刷单记录日志
		//OrderException oe=orderExceptionService.getByPK(Integer.parseInt(orderDeliveryDto.getFlowId()));
		//orderLogService.insertOrderLog(this.request,"2",user.getCustId(),orderDeliveryDto.getFlowId(),orderService.getOrderbyFlowId(oe.getFlowId()).getSource()  );
		return orderDeliveryService.updateOrderDeliveryForRefund(orderDeliveryDto);
	}

	/**
	 * 换货，买家发货
	 * @return
	 */
	@RequestMapping(value = "/sendOrderDeliveryForChange", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,String> sendOrderDeliveryForChange(OrderDeliveryDto orderDeliveryDto) throws Exception
	{
		UserDto user = super.getLoginUser();
		orderDeliveryDto.setUserDto(user);
		//不需要刷单记录日志
		//OrderException oe=orderExceptionService.getByPK(Integer.parseInt(orderDeliveryDto.getFlowId()));
		//orderLogService.insertOrderLog(this.request,"2",user.getCustId(),orderDeliveryDto.getFlowId(),orderService.getOrderbyFlowId(oe.getFlowId()).getSource());
		return orderDeliveryService.updateOrderDeliveryForChange(orderDeliveryDto);
	}

	/**
	 * 获取发货列表
	 * @return
	 */
	@RequestMapping(value = "/getReceiveAddressList", method = RequestMethod.GET)
	@ResponseBody
	public List<UsermanageReceiverAddress> getReceiveAddressList() throws Exception{
		UserDto user = super.getLoginUser();
		if(UtilHelper.isEmpty(user)){
			String url = "/passport-web/jsp/login/sso_login.jsp";
			response.sendRedirect(url);
		}
		return orderDeliveryService.getReceiveAddressList(user);
	}

    /**
     *换货 确认发货
     * @return
     */
    @RequestMapping(value = "/sendOrderDeliveryReturn", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,String> sendOrderDeliveryReturn(OrderDeliveryDto orderDeliveryDto,HttpServletRequest request,MultipartFile excelFile) throws Exception
    {
        UserDto user = super.getLoginUser();
        if(user==null){
            user =  new UserDto();
            user.setCustId(123456);
        }
        orderDeliveryDto.setUserDto(user);
        //验证通过生成发货信息并上传文件
        if(!UtilHelper.isEmpty(excelFile)){
			orderDeliveryDto.setPath(MyConfigUtil.FILE_PATH);
            String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + "发货批号导入信息" + ".xls";
            SaveFileFromInputStream(excelFile.getInputStream(), orderDeliveryDto.getPath(), fileName);
            orderDeliveryDto.setFileName(fileName);
        }else{
			orderDeliveryDto.setPath("");
			orderDeliveryDto.setFileName("");
		}
		//不需要刷单记录日志
		//OrderException oe=orderExceptionService.getByPK(Integer.parseInt(orderDeliveryDto.getFlowId()));
        //orderLogService.insertOrderLog(this.request,"2",user.getCustId(),orderDeliveryDto.getFlowId(),orderService.getOrderbyFlowId(oe.getFlowId()).getSource()  );
        return orderDeliveryService.updateSendOrderDeliveryReturn(orderDeliveryDto);
    }


	public void SaveFileFromInputStream(InputStream stream,String path,String filename) throws IOException
	{
		File tempFile = new File(path);
		if(!tempFile.exists()){
			tempFile.mkdirs();
		}
		FileOutputStream fs=new FileOutputStream( path + "/"+ filename);
		byte[] buffer =new byte[1024*1024];
		int bytesum = 0;
		int byteread = 0;
		while ((byteread=stream.read(buffer))!=-1)
		{
			bytesum+=byteread;
			fs.write(buffer,0,byteread);
			fs.flush();
		}
		fs.close();
		stream.close();
	}

}
