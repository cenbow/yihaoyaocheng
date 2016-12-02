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

import com.search.model.dto.ProductSearchParamDto;
import com.search.model.yhyc.ProductDrug;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yaoex.usermanage.interfaces.custgroup.ICustgroupmanageDubbo;
import com.yaoex.usermanage.model.custgroup.CustGroupDubboRet;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.dto.OrderDto;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.product.dto.ProductInfoDto;
import com.yyw.yhyc.product.dto.ProductPromotionDto;
import org.search.remote.yhyc.ProductSearchInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
	 * 查询单个商品的详细信息(含活动信息)(调用批量搜索商品的接口)
	 * @param productSearchInterface
	 * @param iCustgroupmanageDubbo
	 *@param spuCode                    商品的spu编码
	 * @param sellerEnterpriseId       供应商企业id
	 * @param promotionId               活动id
	 * @param buyerEnterpriseId        采购商企业id     @return
	 */
	public ProductPromotionDto queryProductWithPromotion(ProductSearchInterface productSearchInterface,
														 ICustgroupmanageDubbo iCustgroupmanageDubbo, IPromotionDubboManageService iPromotionDubboManageService,
														 String spuCode, int sellerEnterpriseId, Integer promotionId, int buyerEnterpriseId) {
		if(UtilHelper.isEmpty(productSearchInterface) || UtilHelper.isEmpty(iCustgroupmanageDubbo) || UtilHelper.isEmpty(iPromotionDubboManageService)|| UtilHelper.isEmpty(spuCode)
				|| sellerEnterpriseId <= 0 || sellerEnterpriseId <= 0 || UtilHelper.isEmpty(promotionId)) {
			return null;
		}

		String custGroupCode = this.getCustGroupCode(buyerEnterpriseId,iCustgroupmanageDubbo);
		Set<String> productCodeSet = new HashSet<>();
		String key = spuCode + "-" + sellerEnterpriseId;
		productCodeSet.add( key );
		Map<String,ProductDrug> productDrugMap = this.searchBatchProduct(productSearchInterface,buyerEnterpriseId+"",custGroupCode,productCodeSet);
		if(UtilHelper.isEmpty(productDrugMap) ||  !productDrugMap.containsKey(key)){
			return null;
		}
		ProductDrug productDrug = productDrugMap.get(key);
		com.search.model.yhyc.ProductPromotion productPromotion = UtilHelper.isEmpty(productDrug) ? null : productDrug.getProductPromotion();

		ProductPromotionDto productPromotionDto = null;
		if(!UtilHelper.isEmpty(productPromotion)){
			productPromotionDto = new ProductPromotionDto();
			productPromotionDto.setPromotionId(UtilHelper.isEmpty(productPromotion.getPromotion_id()) ? 0 : Integer.parseInt(productPromotion.getPromotion_id()));
			productPromotionDto.setSpuCode(UtilHelper.isEmpty(productPromotion.getSpu_code()) ? "" : productPromotion.getSpu_code());
			productPromotionDto.setPromotionPrice(UtilHelper.isEmpty(productPromotion.getPromotion_price()) ? new BigDecimal("0") : productPromotion.getPromotion_price());
			productPromotionDto.setMinimumPacking(UtilHelper.isEmpty(productPromotion.getMinimum_packing()) ? 0 : productPromotion.getMinimum_packing());
			productPromotionDto.setLimitNum(UtilHelper.isEmpty(productPromotion.getLimit_num()) ? 0 : productPromotion.getLimit_num());
			productPromotionDto.setSumInventory(UtilHelper.isEmpty(productPromotion.getSum_inventory()) ? 0 : productPromotion.getSum_inventory());

//			productPromotionDto.setCurrentInventory(UtilHelper.isEmpty(productPromotion.getCurrent_inventory()) ? 0 : productPromotion.getCurrent_inventory());
			/* 活动实时库存字段，如果走搜索接口，可能会有问题(比如该字段同步失败)。所以改用活动的dubbo接口去获取该字段的值 */
			ProductPromotionDto temp = queryProductWithPromotion(iPromotionDubboManageService,spuCode,sellerEnterpriseId+"",promotionId,buyerEnterpriseId+"");
			productPromotionDto.setCurrentInventory(getPromotionCurrentInventory(temp));

			productPromotionDto.setSort(UtilHelper.isEmpty(productPromotion.getSort()) ? 0 : Short.parseShort(productPromotion.getSort()+""));
			productPromotionDto.setPromotionType(UtilHelper.isEmpty(productPromotion.getPromotion_type()) ? 0 : Integer.parseInt(productPromotion.getPromotion_type()+""));
			productPromotionDto.setPromotionName(UtilHelper.isEmpty(productPromotion.getPromotion_name()) ? "" : productPromotion.getPromotion_name());
		}
		logger.info("查询单个商品的详细信息(含活动信息)(调用批量搜索商品的接口),productPromotionDto=" + productPromotionDto);
		return productPromotionDto;
	}


	/**
	 *
	 * @param promotionDto
	 * @return
     */
	public int getPromotionCurrentInventory(ProductPromotionDto promotionDto){
		if(UtilHelper.isEmpty(promotionDto) || UtilHelper.isEmpty(promotionDto.getCurrentInventory())){
			return 0;
		}
		return promotionDto.getCurrentInventory();
	}

	/**
	 * 查询单个商品参加活动的信息(调用活动的dubbo接口)
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
			logger.info("查询单个商品参加活动的信息(调用活动的dubbo接口),请求参数params：" + params);
			long startTime = System.currentTimeMillis();
			map = iPromotionDubboManageService.getProductGroupBySpuCodeAndSellerCode(params);
			long endTime = System.currentTimeMillis();
			logger.info("查询单个商品参加活动的信息(调用活动的dubbo接口),耗时" + (endTime - startTime) + "毫秒，响应参数：map=" + map);
		}catch (Exception e){
			logger.error("查询单个商品参加活动的信息(调用活动的dubbo接口),dubbo服务查询异常：errorMesssage = " + e.getMessage(),e);
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
		logger.info("查询单个商品参加活动的信息(调用活动的dubbo接口),productPromotionDto=" + productPromotionDto);
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
									   ICustgroupmanageDubbo iCustgroupmanageDubbo, ProductSearchInterface productSearchInterface){
		if(UtilHelper.isEmpty(iCustgroupmanageDubbo)){
			logger.error("统一校验订单商品接口,查询商品价格前先获取客户组信息，iCustgroupmanageDubbo = " + iCustgroupmanageDubbo);
			return null;
		}
		long startTime,endTime;
		CustGroupDubboRet custGroupDubboRet = null;
		try{
			logger.info("统一校验订单商品接口,查询商品价格前先获取客户组信息，请求参数 buyerEnterprizeId = " + buyerEnterprizeId);
			startTime = System.currentTimeMillis();
			custGroupDubboRet = iCustgroupmanageDubbo.queryGroupBycustId(buyerEnterprizeId+"");
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

	/**
	 * 若是活动商品，则减掉活动商品库存
	 * @param orderDto
	 * @param iPromotionDubboManageService
	 */
	public void reducePromotionInventory(OrderDto orderDto, IPromotionDubboManageService iPromotionDubboManageService) {
		if(UtilHelper.isEmpty(orderDto) || UtilHelper.isEmpty(orderDto.getProductInfoDtoList()) || UtilHelper.isEmpty(iPromotionDubboManageService)){

			return ;
		}
		long startTime,endTime;
		Map params = new HashMap();
		for(ProductInfoDto productInfoDto : orderDto.getProductInfoDtoList()){
			if(UtilHelper.isEmpty(productInfoDto) || UtilHelper.isEmpty(productInfoDto.getPromotionId()) || productInfoDto.getPromotionId() <= 0
					|| UtilHelper.isEmpty(productInfoDto.getProductCount()) || productInfoDto.getProductCount() <= 0){
				continue;
			}
			params.put("spuCode",productInfoDto.getSpuCode() );
			params.put("promotionId", productInfoDto.getPromotionId());
			params.put("buyerCode", orderDto.getCustId());
			params.put("sellerCode", orderDto.getSupplyId());
			params.put("productCount", ( 0 - productInfoDto.getProductCount()));//调用此接口时，负数表示减库存

//			params.put("spuCode", "1000500BBBH240001");
//			params.put("promotionId", 3);
//			params.put("buyerCode", "8859");
//			params.put("sellerCode", "11905");
//			params.put("productCount", -10);
			startTime = System.currentTimeMillis();
			logger.info("活动商品创建完订单后，减库存操作，请求参数params= " + params);
			Map map = iPromotionDubboManageService.updateProductGroupInventroy(params);
			endTime = System.currentTimeMillis();
			logger.info("活动商品创建完订单后，减库存操作完成，耗时(" + (endTime - startTime) + ")毫秒，响应参数map= " + map);
		}

	}

	/**
	 * 根据买家企业id 获取客户组信息
	 * @param buyerEnterprizeId
	 * @return
     */
	public String getCustGroupCode(int buyerEnterprizeId,ICustgroupmanageDubbo iCustgroupmanageDubbo){
		if(UtilHelper.isEmpty(iCustgroupmanageDubbo)){
			logger.error("获取客户组信息异常，iCustgroupmanageDubbo = " + iCustgroupmanageDubbo);
			return null;
		}
		long startTime,endTime;
		CustGroupDubboRet custGroupDubboRet = null;
		try{
			logger.info("调用接口获取客户组信息，请求参数 buyerEnterprizeId = " + buyerEnterprizeId);
			startTime = System.currentTimeMillis();
			custGroupDubboRet = iCustgroupmanageDubbo.queryGroupBycustId(buyerEnterprizeId+"");
			endTime = System.currentTimeMillis();
			logger.info("调用接口获取客户组信息，耗时:"+(endTime - startTime)+"毫秒，响应参数= " + custGroupDubboRet + ",data=" + custGroupDubboRet.getData());
		}catch (Exception e){
			logger.error("调用接口获取客户组信息：" + e.getMessage(),e);
			return null;
		}

		String custGroupCode = null;//客户组编码
		if(UtilHelper.isEmpty(custGroupDubboRet) ||  custGroupDubboRet.getIsSuccess() != 1){
			logger.error("调用接口获取客户组信息异常：" + (custGroupDubboRet == null ? "custGroupDubboRet is null " :custGroupDubboRet.getMessage()));
			return null;
		}else{
			custGroupCode = getCustGroupCode(custGroupDubboRet.getData());
		}
		return custGroupCode;
	}

	/**
	 * 批量搜索商品详情信息接口
	 * @param buyerEnterpriseId  买家企业id
	 * @param custGroupCode      根据买家查询的客户组编码
	 * @param productCodeSet     买家企业id和spuCode ,格式： "sellerEnterpriseId-spuCode"
     * @return
     */
	public Map<String,ProductDrug> searchBatchProduct(ProductSearchInterface productSearchInterface,String buyerEnterpriseId,String custGroupCode , Set<String> productCodeSet){

		if(UtilHelper.isEmpty(productSearchInterface)){
			logger.error("批量搜索商品详情信息接口,服务异常 productSearchInterface = " + productSearchInterface);
			return null;
		}

		long startTime ,endTime;
		ProductSearchParamDto productSearchParamDto = new ProductSearchParamDto();
		productSearchParamDto.setBuyerCode(buyerEnterpriseId);
		productSearchParamDto.setGroupCodes(custGroupCode);
		productSearchParamDto.setProductCodes(productCodeSet);
		List<ProductDrug> productDrugList = null;
		try{
			logger.info("调用批量搜索商品详情接口，获取所有商品详细信息，请求参数 productSearchParamDto = " + productSearchParamDto);
			startTime = System.currentTimeMillis();
			productDrugList = productSearchInterface.searchBatchProduct(productSearchParamDto);
			endTime = System.currentTimeMillis();
			logger.info("调用批量搜索商品详情接口，获取所有商品详细信息，耗时：" + (endTime - startTime) + "毫秒,响应参数 productDrugList = " + productDrugList);
		}catch (Exception e){
			logger.error("用批量搜索商品详情接口，获取所有商品详细信息,发生异常!!!,msg = "+ e.getMessage(),e);
		}

		if(UtilHelper.isEmpty(productDrugList)){
			return null;
		}

		startTime = System.currentTimeMillis();
		Map<String,ProductDrug> map = new HashMap<>();
		for(ProductDrug productDrug : productDrugList){
			if(UtilHelper.isEmpty(productDrug) || UtilHelper.isEmpty(productDrug.getId())){
				continue;
			}
			map.put(productDrug.getId(),productDrug);
		}
		endTime = System.currentTimeMillis();
		logger.info("调用批量搜索商品详情接口，获取所有商品详细信息，list格式转成Map格式，耗时：" + (endTime - startTime) + "毫秒，返回数据map=" + map);

		return map;
	}


}