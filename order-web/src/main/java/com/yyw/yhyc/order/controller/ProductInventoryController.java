/**
 *
 * Created By: XI
 * Created On: 2016-8-29 11:23:12
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.product.bo.ProductInventory;
import com.yyw.yhyc.product.dto.ProductInventoryDto;
import com.yyw.yhyc.product.service.ProductInventoryService;
import com.yyw.yhyc.utils.MyConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.bo.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/product/productInventory")
public class ProductInventoryController extends BaseJsonController{
	private static final Logger logger = LoggerFactory.getLogger(ProductInventoryController.class);

	@Autowired
	private ProductInventoryService productInventoryService;


	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public ProductInventory getByPK(Integer key) throws Exception
	{
		return productInventoryService.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.POST)
	@ResponseBody
	public Pagination<ProductInventoryDto> listPgProductInventory(@RequestBody RequestModel<ProductInventoryDto> requestModel) throws Exception
	{
		ProductInventoryDto productInventoryDto = requestModel.getParam();
		UserDto userDto = super.getLoginUser();
		productInventoryDto.setSupplyId(userDto.getCustId());

		Pagination<ProductInventoryDto> pagination = new Pagination<ProductInventoryDto>();
		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return productInventoryService.listPaginationByProperty(pagination, productInventoryDto);
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@RequestBody ProductInventory productInventory) throws Exception
	{
		productInventoryService.save(productInventory);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	public void delete(@RequestBody RequestListModel<Integer> requestListModel) throws Exception
	{
		productInventoryService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void update(@RequestBody ProductInventoryDto productInventoryDto) throws Exception
	{
		UserDto userDto = super.getLoginUser();
		productInventoryDto.setSupplyId(userDto.getCustId());
		productInventoryDto.setSupplyName(userDto.getUserName());
		productInventoryDto.setSupplyType(2);
		productInventoryService.updateInventory(productInventoryDto);
	}

	/**
	 *商品库存管理页
	 * @return
	 */
	@RequestMapping("/productInventoryManage")
	public ModelAndView buyer_order_manage(){
		ModelAndView view = new ModelAndView("productInventory/product_inventory_manage");
		return view;
	}

	/**
	 *
	 * @param excelFile
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> sendOrderDelivery( MultipartFile excelFile) throws Exception
	{
		String path= MyConfigUtil.PRODUCR_FILE_PATH;
		//验证通过生成发货信息并上传文件
		if(!UtilHelper.isEmpty(excelFile)){
			String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()) + "商品库存信息导入" + ".xls";
			System.out.println(fileName);
			SaveFileFromInputStream(excelFile.getInputStream(), path, fileName);
			UserDto userDto = super.getLoginUser();
			return productInventoryService.UpdateExcelInventory(userDto.getCustId(),userDto.getUserName(),path,fileName);
		}else{
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("code", 0);
			map.put("msg", "参数异常");
			return map;
		}
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



	/**
	 * 检查购物车库存
	 * @return
	 */
	@RequestMapping(value = "/checkInventory", method = RequestMethod.POST)
	public Map<String,Object> checkInventory(@RequestBody ProductInventory productInventory) throws Exception
	{
		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId())){
			throw new Exception("用户未登录");
		}
		return  productInventoryService.findInventoryNumber(productInventory);
	}


	/**
	 * 检查购物车库存
	 * @return
	 */
	@RequestMapping(value = "/checkListInventory", method = RequestMethod.POST)
	public Map<String,Object> checkListInventory(@RequestBody ProductInventoryDto productInventoryDto) throws Exception
	{

		UserDto userDto = super.getLoginUser();
		if(UtilHelper.isEmpty(userDto) || UtilHelper.isEmpty(userDto.getCustId())){
			throw new Exception("用户未登录");
		}
		return  productInventoryService.findInventoryListNumber(productInventoryDto.getProductInventoryList());
	}

}
