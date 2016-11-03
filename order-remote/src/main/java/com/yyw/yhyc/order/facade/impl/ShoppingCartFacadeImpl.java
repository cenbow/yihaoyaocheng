/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:50
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.facade.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yaoex.druggmp.dubbo.service.interfaces.IProductDubboManageService;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.enmu.ShoppingCartFromWhereEnum;
import org.search.remote.yhyc.ProductSearchInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.facade.ShoppingCartFacade;
import com.yyw.yhyc.order.service.ShoppingCartService;

@Service("shoppingCartFacade")
public class ShoppingCartFacadeImpl implements ShoppingCartFacade {
	private static final Logger logger = LoggerFactory.getLogger(ShoppingCartFacadeImpl.class);

	private ShoppingCartService shoppingCartService;
	
	@Autowired
	public void setShoppingCartService(ShoppingCartService shoppingCartService)
	{
		this.shoppingCartService = shoppingCartService;
	}

	@Reference
	private IPromotionDubboManageService iPromotionDubboManageService;

	@Reference
	private IProductDubboManageService iProductDubboManageService;

	@Reference
	private ICustgroupmanageDubbo iCustgroupmanageDubbo;

	@Reference
	private ProductSearchInterface productSearchInterface;
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public ShoppingCart getByPK(java.lang.Integer primaryKey) throws Exception
	{
		return shoppingCartService.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ShoppingCart> list() throws Exception
	{
		return shoppingCartService.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<ShoppingCart> listByProperty(ShoppingCart shoppingCart)
			throws Exception
	{
		return shoppingCartService.listByProperty(shoppingCart);
	}
	
	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<ShoppingCart> listPaginationByProperty(Pagination<ShoppingCart> pagination, ShoppingCart shoppingCart)
			throws Exception
	{
		return shoppingCartService.listPaginationByProperty(pagination, shoppingCart);
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(java.lang.Integer primaryKey) throws Exception
	{
		return shoppingCartService.deleteByPK(primaryKey);
	}
	
	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<java.lang.Integer> primaryKeys) throws Exception
	{
		shoppingCartService.deleteByPKeys(primaryKeys);
	}
	
	/**
	 * 根据传入参数删除记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(ShoppingCart shoppingCart) throws Exception
	{
		return shoppingCartService.deleteByProperty(shoppingCart);
	}

	/**
	 * 保存记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public void save(ShoppingCart shoppingCart) throws Exception
	{
		shoppingCartService.save(shoppingCart);
	}

	/**
	 * 更新记录
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public int update(ShoppingCart shoppingCart) throws Exception
	{
		return shoppingCartService.update(shoppingCart);
	}

	/**
	 * 根据条件查询记录条数
	 * @param shoppingCart
	 * @return
	 * @throws Exception
	 */
	public int findByCount(ShoppingCart shoppingCart) throws Exception
	{
		return shoppingCartService.findByCount(shoppingCart);
	}

	/**
	 * 查询进货单商品数量
	 * @param custId 企业ID
	 * @return 进货单商品数量
	 * @throws Exception
	 */
	public int findShoppingCartByCount(Integer custId){
		if(UtilHelper.isEmpty(custId)) return 0;
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setCustId(custId);
		shoppingCart.setFromWhere(ShoppingCartFromWhereEnum.SHOPPING_CART.getFromWhere());
		logger.info("查询进货单商品数量,查询条件：shoppingCart=" + shoppingCart);
		try {
			return shoppingCartService.findByCount(shoppingCart);
		}catch (Exception e){
			logger.error(e.getMessage(), e);

			return 0;
		}
	}

	/**
	 * 加入进货单
	 * @param shoppingCart 进货单对象
	 * @return 成功失败标识（state：[S-->成功, F-->失败]），进货单商品数量，进货单订单金额
	 * @throws Exception
	 */
	public Map<String, Object> addShoppingCart(ShoppingCart shoppingCart){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("state", "F");

		if(UtilHelper.isEmpty(shoppingCart)) return map;
		if(UtilHelper.isEmpty(shoppingCart.getCustId())) {
			map.put("message", "采购商企业ID不能为空！");
			return map;
		}else if (UtilHelper.isEmpty(shoppingCart.getSupplyId())){
			map.put("message", "供应商企业ID不能为空！");
			return map;
		}else if (UtilHelper.isEmpty(shoppingCart.getSpuCode())){
			map.put("message", "SPU编码不能为空！");
			return map;
		}else if (UtilHelper.isEmpty(shoppingCart.getProductId())){
			map.put("message", "商品ID不能为空！");
			return map;
		}else if (UtilHelper.isEmpty(shoppingCart.getProductCount())){
			map.put("message", "商品数量不能为空！");
			return map;
		}

		try {
			//TODO Facade层 怎么获取当前登陆用户的信息呢？
			return shoppingCartService.addShoppingCart(shoppingCart,null,iPromotionDubboManageService,iProductDubboManageService,iCustgroupmanageDubbo,productSearchInterface);
		}catch (Exception e){
			logger.error(e.getMessage(), e);

			map.put("message", e.getMessage());
			return map;
		}
	}
}
