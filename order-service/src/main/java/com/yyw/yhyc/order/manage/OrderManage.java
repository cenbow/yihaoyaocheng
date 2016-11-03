/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:48
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.manage;

import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yaoex.usermanage.model.custgroup.CustGroupDubboRet;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.product.dto.ProductPromotionDto;
import org.search.remote.yhyc.ProductSearchInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("orderManage")
public class OrderManage {

	private static final Logger logger = LoggerFactory.getLogger(OrderManage.class);

	private OrderMapper	orderMapper;

	@Autowired
	public void setOrderMapper(OrderMapper orderMapper)
	{
		this.orderMapper = orderMapper;
	}

	/**
	 * 查询商品参加活动的信息
	 * @param iPromotionDubboManageService
	 * @param spuCode                    商品的spu编码
	 * @param sellerEnterpriseId       供应商企业id
	 * @param promotionId               活动id
	 * @param buyerEnterpriseId        采购商企业id
	 * @return
	 */
	public ProductPromotionDto queryProductWithPromotion(IPromotionDubboManageService iPromotionDubboManageService,
														  String spuCode, String sellerEnterpriseId, Integer promotionId, String buyerEnterpriseId) {
		if(UtilHelper.isEmpty(iPromotionDubboManageService) || UtilHelper.isEmpty(spuCode) || UtilHelper.isEmpty(sellerEnterpriseId) || UtilHelper.isEmpty(promotionId)){
			return null;
		}

		Map map = null;
		try{
			Map params=new HashMap();
			params.put("spuCode",spuCode);
			params.put("promotionId", promotionId);
			params.put("buyerCode", buyerEnterpriseId);
			params.put("sellerCode", sellerEnterpriseId);
//			params.put("spuCode", "1000500BBBH240001");
//			params.put("promotionId", 3);
//			params.put("buyerCode", "8859");
//			params.put("sellerCode", "11905");
			logger.info("查询商品参加活动的信息,请求参数params：" + params);
			long startTime = System.currentTimeMillis();
			map = iPromotionDubboManageService.getProductGroupBySpuCodeAndSellerCode(params);
			long endTime = System.currentTimeMillis();
			logger.info("查询商品参加活动的信息,耗时" + (endTime - startTime) + "毫秒，响应参数：map=" + map);
		}catch (Exception e){
			logger.error("查询商品参加活动的信息,dubbo服务查询异常：errorMesssage = " + e.getMessage(),e);
			return null;
		}
		if(UtilHelper.isEmpty(map) ){
			return null;
		}
		ProductPromotionDto productPromotionDto = new ProductPromotionDto();
		productPromotionDto.setPromotionId(UtilHelper.isEmpty(map.get("promotionId")+"") ? 0 : Integer.parseInt(map.get("promotionId")+""));
		productPromotionDto.setSpuCode(UtilHelper.isEmpty(map.get("spuCode")+"") ? "" : map.get("spuCode")+"");
		productPromotionDto.setPromotionPrice(UtilHelper.isEmpty(map.get("promotionPrice")+"") ? new BigDecimal("0") : new BigDecimal(map.get("promotionPrice")+""));
		productPromotionDto.setMinimumPacking(UtilHelper.isEmpty(map.get("minimumPacking")+"") ? 0 : Integer.parseInt(map.get("minimumPacking")+""));
		productPromotionDto.setLimitNum(UtilHelper.isEmpty(map.get("limitNum")+"") ? 0 : Integer.parseInt(map.get("limitNum")+""));
		productPromotionDto.setSumInventory(UtilHelper.isEmpty(map.get("sumInventory")+"") ? 0 : Integer.parseInt(map.get("sumInventory")+""));
		productPromotionDto.setCurrentInventory(UtilHelper.isEmpty(map.get("currentInventory")+"") ? 0 : Integer.parseInt(map.get("currentInventory")+""));
		productPromotionDto.setSort(UtilHelper.isEmpty(map.get("sumInventory")+"") ? 0 : Short.parseShort(map.get("sumInventory")+""));
		productPromotionDto.setPromotionType(UtilHelper.isEmpty(map.get("promotionType")+"") ? 0 : Integer.parseInt(map.get("promotionType")+""));
		productPromotionDto.setPromotionName(UtilHelper.isEmpty(map.get("promotionName")+"") ? "" : map.get("promotionName")+"");
		logger.info("查询商品参加活动的信息,productPromotionDto=" + productPromotionDto);
		return productPromotionDto;
	}


	/**
	 * 调用接口查询商品价格
	 * @param spuCode               商品spu编码
	 * @param buyerEnterprizeId    买家企业id
	 * @param sellerEnterprizeId   商家企业id
	 * @param productSearchInterface
	 * @return
	 */
	public BigDecimal getProductPrice(String spuCode, Integer buyerEnterprizeId, Integer sellerEnterprizeId,
									   ICustgroupmanageDubbo iCustgroupmanageDubbo, UserDto userDto, ProductSearchInterface productSearchInterface){
		if(UtilHelper.isEmpty(iCustgroupmanageDubbo)){
			logger.error("统一校验订单商品接口,查询商品价格前先获取客户组信息，iCustgroupmanageDubbo = " + iCustgroupmanageDubbo);
			return null;
		}
		long startTime,endTime;
		CustGroupDubboRet custGroupDubboRet = null;
		try{
			logger.info("统一校验订单商品接口,查询商品价格前先获取客户组信息，请求参数 = " + userDto.getCustId());
			startTime = System.currentTimeMillis();
			custGroupDubboRet = iCustgroupmanageDubbo.queryGroupBycustId(userDto.getCustId()+"");
			endTime = System.currentTimeMillis();
			logger.info("统一校验订单商品接口,查询商品价格前先获取客户组信息，耗时:"+(endTime - startTime)+"毫秒，响应参数= " + custGroupDubboRet + ",data=" + custGroupDubboRet.getData());
		}catch (Exception e){
			logger.error("统一校验订单商品接口,查询商品价格前先获取客户组信息异常：" + e.getMessage(),e);
			return null;
		}

		String custGroupCode = null;//客户组编码
		if(UtilHelper.isEmpty(custGroupDubboRet) ||  custGroupDubboRet.getIsSuccess() != 1){
			logger.error("统一校验订单商品接口,查询商品价格前先获取客户组信息异常：" + (custGroupDubboRet == null ? "custGroupDubboRet is null " :custGroupDubboRet.getMessage()));
			return null;
		}else{
			custGroupCode = getCustGroupCode(custGroupDubboRet.getData());
		}

		logger.info("统一校验订单商品接口,查询商品价格(调用搜索接口 productSearchInterface= "+ productSearchInterface +")");
		if(UtilHelper.isEmpty(productSearchInterface)){
			return null;
		}

		//调用搜索的接口
		Double productPrice = null;
		try{
			logger.info("统一校验订单商品接口,查询商品价格，请求参数:\n buyerEnterprizeId=" + buyerEnterprizeId +
					",sellerEnterprizeId=" + sellerEnterprizeId +",custGroupName="+custGroupCode +",spuCode="+spuCode);
			startTime = System.currentTimeMillis();
			productPrice = productSearchInterface.findProductShowPrice(buyerEnterprizeId+"",sellerEnterprizeId+"",spuCode,custGroupCode);
			endTime = System.currentTimeMillis();
			logger.info("统一校验订单商品接口,查询商品价格，耗时:"+(endTime - startTime)+"毫秒，响应参数：" + productPrice );
		}catch (Exception e){
			logger.error("统一校验订单商品接口,查询商品价格前先获取客户组信息异常：" + e.getMessage(),e);
			return null;
		}
		if(UtilHelper.isEmpty(productPrice)){
			return null;
		}
		return new BigDecimal(productPrice + "");
	}

	/**
	 *
	 * @param data  "[{group_code=61650851012264}, {group_code=61671525425650}]"
	 * @return
	 */
	private String getCustGroupCode(List<Map<String, Object>> data) {
		if(UtilHelper.isEmpty(data)) return "";
		String result = "";
		for(Map map : data){
			if(UtilHelper.isEmpty(map)) continue;
			if(!UtilHelper.isEmpty(map.get("group_code")+"")){
				if(UtilHelper.isEmpty(result)){
					result += map.get("group_code")+"";
				}else{
					result += "," + map.get("group_code")+"";
				}
			}
		}
		return result;
	}

}