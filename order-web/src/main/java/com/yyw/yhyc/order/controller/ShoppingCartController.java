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

import com.alibaba.dubbo.config.annotation.Reference;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.bo.RequestListModel;
import com.yyw.yhyc.bo.RequestModel;
import com.yyw.yhyc.controller.BaseJsonController;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.service.ShoppingCartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/shoppingCart")
public class ShoppingCartController extends BaseJsonController {
	private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

	@Autowired
	private ShoppingCartService shoppingCartService;

//	@Reference
//	private IProductDubboManageService iProductDubboManageService;

	/**
	* 通过主键查询实体对象
	* @return
	*/
	@RequestMapping(value = "/getByPK/{key}", method = RequestMethod.GET)
	@ResponseBody
	public ShoppingCart getByPK(@PathVariable("key") Integer key) throws Exception
	{
		return shoppingCartService.getByPK(key);
	}

	/**
	* 分页查询记录
	* @return
	*/
	@RequestMapping(value = {"", "/listPg"}, method = RequestMethod.GET)
	@ResponseBody
	public Pagination<ShoppingCart> listPgShoppingCart(RequestModel<ShoppingCart> requestModel) throws Exception
	{
		Pagination<ShoppingCart> pagination = new Pagination<ShoppingCart>();

		pagination.setPaginationFlag(requestModel.isPaginationFlag());
		pagination.setPageNo(requestModel.getPageNo());
		pagination.setPageSize(requestModel.getPageSize());

		return shoppingCartService.listPaginationByProperty(pagination, requestModel.getParam());
	}

	/**
	* 新增记录
	* @return
	*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@RequestBody ShoppingCart shoppingCart) throws Exception
	{
		shoppingCartService.save(shoppingCart);
	}

	/**
	* 根据多条主键值删除记录
	* @return
	*/
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public void delete(@RequestBody RequestListModel<Integer> requestListModel) throws Exception
	{
		shoppingCartService.deleteByPKeys(requestListModel.getList());
	}

	/**
	* 修改记录
	* @return
	*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public void update(@RequestBody ShoppingCart shoppingCart) throws Exception
	{
		shoppingCartService.update(shoppingCart);
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView model = new ModelAndView();
		UserDto userDto = super.getLoginUser();

		/* 获取购物车中的商品信息 */
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(userDto.getCustId());
		List<ShoppingCartListDto> allShoppingCart = shoppingCartService.listAllShoppingCart(shoppingCart);

		/* 获取商品图片 */
//		logger.info("购物车页面-获取商品图片：iProductDubboManageService = " + iProductDubboManageService);
//		if(!UtilHelper.isEmpty(allShoppingCart) && !UtilHelper.isEmpty(iProductDubboManageService)){
//			for(ShoppingCartListDto shoppingCartListDto : allShoppingCart){
//				if(UtilHelper.isEmpty(shoppingCartListDto) || UtilHelper.isEmpty(shoppingCartListDto.getShoppingCartDtoList())){
//					continue;
//				}
//				for(ShoppingCartDto shoppingCartDto : shoppingCartListDto.getShoppingCartDtoList()){
//					if(UtilHelper.isEmpty(shoppingCartDto)) continue;
//
//					Map map = new HashMap();
//					map.put("spu_code", shoppingCartDto.getSpuCode());
//					map.put("type_id", "1");
//					List list = iProductDubboManageService.selectByTypeIdAndSPUCode(map);
//					//TODO
//
//				}
//			}
//		}


		model.addObject("allShoppingCart",allShoppingCart);
		model.setViewName("shoppingCart/index");
		return model;
	}

	/**
	 * 更新购物车中数量
	 * @param shoppingCart
	 * @throws Exception
     */
	@RequestMapping(value = "/updateNum", method = RequestMethod.POST)
	public void updateNum(@RequestBody ShoppingCart shoppingCart) throws Exception {
		UserDto userDto = super.getLoginUser();
		int resultCount = shoppingCartService.updateNum(shoppingCart,userDto);
	}

}
