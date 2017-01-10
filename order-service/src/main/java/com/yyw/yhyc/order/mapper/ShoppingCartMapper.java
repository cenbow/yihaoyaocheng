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
package com.yyw.yhyc.order.mapper;

import java.util.List;
import java.util.Map;

import com.yyw.yhyc.order.bo.ShoppingCart;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.order.dto.ShoppingCartDto;
import com.yyw.yhyc.order.dto.ShoppingCartListDto;
import org.apache.ibatis.annotations.Param;

public interface ShoppingCartMapper extends GenericIBatisMapper<ShoppingCart, Integer> {

	public List<ShoppingCart> listPaginationByProperty(Pagination<ShoppingCart> pagination, ShoppingCart shoppingCart);

	List<ShoppingCartListDto> listAllShoppingCart(ShoppingCart shoppingCart);
	List<ShoppingCartDto> listShoppingCartDtoByProperty(ShoppingCart shoppingCart);

	/**
	 * 查询商品数量和进化单金额
	 * @param ShoppingCart
	 * @return
     */
	Map<String, java.math.BigDecimal> queryShoppingCartStatistics(ShoppingCart shoppingCart);

	/**
	 * 更新商品数量 （product_count= product_count + #{productCount}）
	 * @param shoppingCart
	 * @return
     */
	int updateProductCount(ShoppingCart shoppingCart);

	List<ShoppingCart> listDistinctCustIdAndSupplyId(ShoppingCart shoppingCart);

	/**
	 * 查询历史购买量
	 * @param shoppingCart
	 * @return
     */
	int countBuyedNumInHistory(ShoppingCart shoppingCart);
	
	public void updateShoppingCartPromotionCollectionId(Map<String,Object> paramterMap);
}
