package com.yyw.yhyc.order.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yao.trade.interfaces.credit.interfaces.CreditDubboServiceInterface;
import com.yao.trade.interfaces.credit.model.CreditDubboResult;
import com.yao.trade.interfaces.credit.model.CreditParams;
import com.yaoex.druggmp.dubbo.service.interfaces.IPromotionDubboManageService;
import com.yyw.yhyc.helper.DateHelper;
import com.yyw.yhyc.helper.UtilHelper;
import com.yyw.yhyc.order.bo.Order;
import com.yyw.yhyc.order.bo.OrderDelivery;
import com.yyw.yhyc.order.bo.OrderDeliveryDetail;
import com.yyw.yhyc.order.bo.OrderDetail;
import com.yyw.yhyc.order.bo.OrderException;
import com.yyw.yhyc.order.bo.OrderReturn;
import com.yyw.yhyc.order.bo.OrderSettlement;
import com.yyw.yhyc.order.bo.OrderTrace;
import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.order.dto.OrderDeliveryDto;
import com.yyw.yhyc.order.dto.OrderPartDeliveryDto;
import com.yyw.yhyc.order.dto.UserDto;
import com.yyw.yhyc.order.enmu.SystemOrderExceptionStatusEnum;
import com.yyw.yhyc.order.enmu.SystemOrderStatusEnum;
import com.yyw.yhyc.order.enmu.SystemPayTypeEnum;
import com.yyw.yhyc.order.enmu.SystemReplenishmentOrderStatusEnum;
import com.yyw.yhyc.order.mapper.OrderDeliveryDetailMapper;
import com.yyw.yhyc.order.mapper.OrderDeliveryMapper;
import com.yyw.yhyc.order.mapper.OrderDetailMapper;
import com.yyw.yhyc.order.mapper.OrderExceptionMapper;
import com.yyw.yhyc.order.mapper.OrderMapper;
import com.yyw.yhyc.order.mapper.OrderReturnMapper;
import com.yyw.yhyc.order.mapper.OrderTraceMapper;
import com.yyw.yhyc.order.mapper.SystemDateMapper;
import com.yyw.yhyc.product.manage.ProductInventoryManage;
import com.yyw.yhyc.usermanage.bo.UsermanageReceiverAddress;
import com.yyw.yhyc.usermanage.mapper.UsermanageReceiverAddressMapper;
import com.yyw.yhyc.utils.ExcelUtil;

/**
 * 部分返回确定业务逻辑处理
 * @author wangkui01
 *
 */
@Service("orderPartDeliveryConfirmService")
public class OrderPartDeliveryConfirmService { 
private Log log = LogFactory.getLog(OrderPartDeliveryConfirmService.class);
@Autowired
private OrderDeliveryMapper orderDeliveryMapper;
@Autowired
private OrderDetailMapper orderDetailMapper;
@Autowired
private OrderDeliveryDetailMapper orderDeliveryDetailMapper;
@Autowired
private OrderMapper orderMapper;
@Autowired
private SystemDateMapper systemDateMapper;
@Autowired
private UsermanageReceiverAddressMapper receiverAddressMapper;
@Autowired
private OrderExceptionMapper orderExceptionMapper;
@Autowired
private OrderReturnMapper orderReturnMapper;
@Autowired
private OrderTraceMapper orderTraceMapper;
@Autowired
private ProductInventoryManage productInventoryManage;
@Autowired
private SystemPayTypeService systemPayTypeService;
@Autowired
private OrderSettlementService orderSettlementService;
/**
 * 部分发货确定按钮处理逻辑
 * @param orderDeliveryDto
 * @return
 * @throws Exception
 */
public Map<String,String> updatePartDeliveryConfirmMethodInfo(OrderDeliveryDto orderDeliveryDto,IPromotionDubboManageService iPromotionDubboManageService,CreditDubboServiceInterface creditDubboService) throws Exception{
	 Map<String, String> map = new HashMap<String, String>();
       if (UtilHelper.isEmpty(orderDeliveryDto)) {
           map.put("code", "0");
           map.put("msg", "发货信息不能为空");
           return map;
       }
       if (UtilHelper.isEmpty(orderDeliveryDto.getFlowId())) {
           map.put("code", "0");
           map.put("msg", "订单id不能为空");
           return map;
       }
       if (UtilHelper.isEmpty(orderDeliveryDto.getReceiverAddressId())) {
           map.put("code", "0");
           map.put("msg", "发货地址不能为空");
           return map;
       }
       if (UtilHelper.isEmpty(orderDeliveryDto.getDeliveryMethod())) {
           map.put("code", "0");
           map.put("msg", "配送方式不能为空");
           return map;
       }
       
       //校验订单地址存在否
       OrderDelivery orderDelivery = orderDeliveryMapper.getByFlowId(orderDeliveryDto.getFlowId());
       if (UtilHelper.isEmpty(orderDelivery)) {
           map.put("code", "0");
           map.put("msg", "订单地址不存在");
           return map;
       }
       orderDeliveryDto.setOrderId(orderDelivery.getOrderId());

       //验证批次号并生成订单发货数据
       this.updateExcelOrderDeliveryDetail(orderDeliveryDto.getPath() + orderDeliveryDto.getFileName(), map, orderDeliveryDto,iPromotionDubboManageService,creditDubboService);
       return map;
	
}


 //读取验证订单批次信息excel
public Map<String, String> updateExcelOrderDeliveryDetail(String excelPath, Map<String, String> map, OrderDeliveryDto orderDeliveryDto,IPromotionDubboManageService iPromotionDubboManageService,CreditDubboServiceInterface creditDubboService) {
   String now = systemDateMapper.getSystemDate();
   List<Map<String, String>> errorList = new ArrayList<Map<String, String>>();
   Map<String, String> errorMap = null;
   Map<String, String> codeMap = new HashMap<String, String>(); //此保存的是key:商品code,value:数量
   Map<String, Integer> detailMap = new HashMap<String, Integer>();
   List<Map<String, String>> list=null;
   //原订单id
   int orderId = 0;
   String filePath = "";
   try {
       if (!UtilHelper.isEmpty(excelPath)) {
           list = ExcelUtil.readExcel(excelPath); //0:序号,1:订单编号,2:商品编码,3:通用名,4:规格,5:厂商,6:批号,7:有效期至,8:数量
           if (list.size() > 0) {
               
           	list.remove(0); //删除第一列头
               
               if(list.size()==0){ 
           	  map.put("code", "0");
                 map.put("msg", "发货的商品不能为空");
                 return map;
               }
           } else{
               map.put("code", "0");
               map.put("msg", "读取模板失败");
               return map;
           }

           for (Map<String, String> rowMap : list) {
               StringBuffer stringBuffer = new StringBuffer();
               if (UtilHelper.isEmpty(rowMap.get("1"))) {
                   stringBuffer.append("订单编码不能为空,");
               }
               if (UtilHelper.isEmpty(rowMap.get("2"))) {
                   stringBuffer.append("商品编码不能为空,");
               }
               if(!UtilHelper.isEmpty(rowMap.get("6"))){
               	String batchNum=rowMap.get("6");
               	if(batchNum.length()>100){
               		 stringBuffer.append("批次号过长,");
               	}
               }
               if(!UtilHelper.isEmpty(rowMap.get("7"))){//有效期
               	String validDateStr=rowMap.get("7");
               	SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd");
               	try{
               		Date date=formate.parse(validDateStr);
               		String lastValue=formate.format(date);
            		rowMap.put("7", lastValue);
               	}catch(Exception es){
               		log.error("上传的excel有效期格式错误");
               	    stringBuffer.append("有效期格式错误,");
               	}
               }
               if (UtilHelper.isEmpty(rowMap.get("8"))) {
                   stringBuffer.append("数量为空,");
               }
               if ((!UtilHelper.isEmpty(rowMap.get("1"))) && !rowMap.get("1").equals(orderDeliveryDto.getFlowId())) {
                   stringBuffer.append("订单编码与发货订单编码不相同,");
               }


               //如果有必填为空则记录错误返回下一次循环
               if (stringBuffer.length() > 0) {
                   errorMap = rowMap;
                   errorMap.put("9", stringBuffer.toString().replace(stringBuffer.charAt(stringBuffer.length() - 1) + "", "。"));
                   errorList.add(errorMap);
                   continue;
               } else {
                   //正常订单==1补货订单==2
                   if (orderDeliveryDto.getOrderType() == 1) {
                       //验证订单号与商品编码是否存在，都存在则根据商品编码记录批次数量
                       Order order = orderMapper.getOrderbyFlowId(rowMap.get("1"));
                       if (UtilHelper.isEmpty(order)) {
                           stringBuffer.append("订单编号不存在,");
                       } else {
                           orderId = order.getOrderId();
                           OrderDetail orderDetail = new OrderDetail();
                           orderDetail.setOrderId(orderId);
                           orderDetail.setProductCode(rowMap.get("2"));
                           orderDetail.setSupplyId(orderDeliveryDto.getUserDto().getCustId());
                           List detailList = orderDetailMapper.listByProperty(orderDetail);
                           if (UtilHelper.isEmpty(detailList) || detailList.size() == 0) {
                               stringBuffer.append("商品编码不存在,");
                           }
                           if (stringBuffer.length() > 0) {
                               errorMap = rowMap;
                               errorMap.put("9", stringBuffer.toString().replace(stringBuffer.charAt(stringBuffer.length() - 1) + "", "。"));
                               errorList.add(errorMap);
                               continue;
                           } else {
                               if (UtilHelper.isEmpty(codeMap.get(rowMap.get("2")))) {
                                   codeMap.put(rowMap.get("2"), rowMap.get("8"));
                               } else {
                                   codeMap.put(rowMap.get("2"), String.valueOf(Integer.parseInt(codeMap.get(rowMap.get("2"))) + Integer.parseInt(rowMap.get("8"))));
                               }
                           }
                       }
                   }

               }
           }

           //验证商品数量是否相同
               for (String code : codeMap.keySet()) {
                   OrderDetail orderDetail = new OrderDetail();
                   orderDetail.setOrderId(orderId);
                   orderDetail.setProductCode(code);
                   orderDetail.setSupplyId(orderDeliveryDto.getUserDto().getCustId());
                   List<OrderDetail> detailList = orderDetailMapper.listByProperty(orderDetail);
                   if (detailList.size() > 0) {
                   	
                       orderDetail = detailList.get(0);
                       detailMap.put(code, orderDetail.getOrderDetailId());
                       
                       int orderDetailProductCount=orderDetail.getProductCount().intValue(); //订单商品数量
                       int sendProductCount=Integer.parseInt(codeMap.get(code));//发货的数量
                      
                       if(sendProductCount<=0|| sendProductCount>orderDetailProductCount){
                       	
                       	 for (Map<String, String> rowMap : list) {
                                 if (rowMap.get("2").equals(code)) {
                                     errorMap = rowMap;
                                     errorMap.put("9", "商品编码为" + code + "的商品导入数量不能小于等于零或者大于采购数量");
                                     errorList.add(errorMap);
                                 }
                             }
                       }else if(sendProductCount<orderDetailProductCount){ //发货的数量小于订单数量，那么代表正常订单中的部分发货
                       	orderDeliveryDto.setSomeSend(true);
                       	orderDeliveryDto.setCodeMap(codeMap);
                       	
                       	OrderPartDeliveryDto partDeliveryDto=new OrderPartDeliveryDto();
                       	partDeliveryDto.setFlowId(orderDeliveryDto.getFlowId());
                       	partDeliveryDto.setOrderId(orderId);
                       	partDeliveryDto.setProduceCode(code);
                       	partDeliveryDto.setOrderDetailId(orderDetail.getOrderDetailId());
                       	partDeliveryDto.setNoDeliveryNum((orderDetailProductCount-sendProductCount));
                       	partDeliveryDto.setProducePrice(orderDetail.getProductPrice());
                       	partDeliveryDto.setSendDeliveryNum(sendProductCount);

                       	 if(orderDeliveryDto.getPartDeliveryDtoList()==null){
                       		 List<OrderPartDeliveryDto> partDeliveryList=new ArrayList<OrderPartDeliveryDto>();
                       		 partDeliveryList.add(partDeliveryDto);
                       		 orderDeliveryDto.setPartDeliveryDtoList(partDeliveryList);
                       	 }else{
                       		 orderDeliveryDto.getPartDeliveryDtoList().add(partDeliveryDto);
                       	 }
                       	 
                    	 if(orderDeliveryDto.getSendDeliveryDtoList()==null){
                       		 List<OrderPartDeliveryDto> sendDeliveryList=new ArrayList<OrderPartDeliveryDto>();
                       		 sendDeliveryList.add(partDeliveryDto);
                       		 orderDeliveryDto.setSendDeliveryDtoList(sendDeliveryList);
                       	 }else{
                       		 orderDeliveryDto.getSendDeliveryDtoList().add(partDeliveryDto);
                       	 }
                       	
                       }else if(sendProductCount==orderDetailProductCount){
                    	   
                    		OrderPartDeliveryDto partDeliveryDto=new OrderPartDeliveryDto();
                           	partDeliveryDto.setFlowId(orderDeliveryDto.getFlowId());
                           	partDeliveryDto.setOrderId(orderId);
                           	partDeliveryDto.setProduceCode(code);
                           	partDeliveryDto.setOrderDetailId(orderDetail.getOrderDetailId());
                           	partDeliveryDto.setNoDeliveryNum(0);
                           	partDeliveryDto.setProducePrice(orderDetail.getProductPrice());
                           	partDeliveryDto.setSendDeliveryNum(sendProductCount);
                           	
                    	 if(orderDeliveryDto.getSendDeliveryDtoList()==null){
                       		 List<OrderPartDeliveryDto> sendDeliveryList=new ArrayList<OrderPartDeliveryDto>();
                       		 sendDeliveryList.add(partDeliveryDto);
                       		 orderDeliveryDto.setSendDeliveryDtoList(sendDeliveryList);
                       	 }else{
                       		 orderDeliveryDto.getSendDeliveryDtoList().add(partDeliveryDto);
                       	 }
                       }
                       
                   }
               }
          
       }


       // lhj add 判断是否为部分发货
       orderDeliveryDto.setSomeSend(isPartSend(orderDeliveryDto,codeMap));

       //处理excel格式不正确的
       if(errorList!=null && errorList.size()>0){
       	 filePath=this.updateprocessExcelWrong(errorList, orderDeliveryDto, now);
       	    map.put("code", "2");
            map.put("msg", "发货失败。");
            map.put("fileName", filePath);
       }else{
       	
       	 if(orderDeliveryDto.isSomeSend()){ //该订单是部分发货
       		 this.saveAllRightOrderDeliverDetail(orderDeliveryDto, list, detailMap, excelPath, now);
       		 this.updateAllRightOrderDeliveryMethod(orderDeliveryDto,map, excelPath, now,iPromotionDubboManageService,creditDubboService);
       	 }
       	
       }

   } catch (Exception e) {
	   throw new RuntimeException("发货异常"+e.getMessage());
   }
   return map;
}

   /*
    * lhj add 判断是否为部分发货
    * 需要考虑一个订单中两个订单详情可能是同一个商品编码情况
    */
 public boolean isPartSend(OrderDeliveryDto orderDeliveryDto, Map<String, String> codeMap){
     List<OrderDetail> allDetailList=new ArrayList<OrderDetail>();// lhj add
     // lhj add  当发货品种数量小于订单品种时为部分发货
     Order orderA = orderMapper.getOrderbyFlowId(orderDeliveryDto.getFlowId());
    Integer orderId = orderA.getOrderId();
     OrderDetail orderDetailA = new OrderDetail();
     orderDetailA.setOrderId(orderId);
     allDetailList=orderDetailMapper.listByProperty(orderDetailA);
     Map<String,Integer> orderDmap=new HashMap<String,Integer> ();
     for(OrderDetail orderDetail:allDetailList){
         if(UtilHelper.isEmpty(orderDmap.get(orderDetail.getProductCode()))){
             orderDmap.put(orderDetail.getProductCode(),orderDetail.getProductCount());
         }else{
             orderDmap.put(orderDetail.getProductCode(),orderDetail.getProductCount()+orderDmap.get(orderDetail.getProductCode()));
         }
     }
     for(String code:orderDmap.keySet()){
         if(UtilHelper.isEmpty(codeMap.get(code))){
             return true;
         }else if(Integer.parseInt(codeMap.get(code))<orderDmap.get(code).intValue()) {
             orderDeliveryDto.setSomeSend(true);
             return true;
         }
     }
     return false;
 }

/**
* 处理excel错误的逻辑
* @return
*/
private String updateprocessExcelWrong(List<Map<String, String>> errorList, OrderDeliveryDto orderDeliveryDto,String now){
  String filePath = "";
  String[] headers = {"序号", "订单编码", "商品编码","通用名","规格","厂商","批号","有效期至", "数量", "失败原因"};
  List<Object[]> dataset = new ArrayList<Object[]>();
  for (Map<String, String> dataMap : errorList) {
      dataset.add(new Object[]{
              dataMap.get("0"), dataMap.get("1"), dataMap.get("2"), dataMap.get("3"), dataMap.get("4"), dataMap.get("5"),dataMap.get("6"),dataMap.get("7"),dataMap.get("8"),dataMap.get("9")
      });
  }
  filePath = orderDeliveryDto.getPath() + ExcelUtil.downloadExcel("发货批号导入信息", headers, dataset, orderDeliveryDto.getPath());

  OrderDeliveryDetail orderDeliveryDetail = new OrderDeliveryDetail();
  orderDeliveryDetail.setFlowId(orderDeliveryDto.getFlowId());
  List<OrderDeliveryDetail> orderDeliveryDetails = orderDeliveryDetailMapper.listByProperty(orderDeliveryDetail);
  if (orderDeliveryDetails.size() > 0) {
      orderDeliveryDetail = orderDeliveryDetails.get(0);
      orderDeliveryDetail.setOrderId(orderDeliveryDto.getOrderId());
      orderDeliveryDetail.setUpdateTime(now);
      orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
      orderDeliveryDetail.setDeliveryStatus(0);
      orderDeliveryDetail.setImportFileUrl(filePath);
      orderDeliveryDetailMapper.update(orderDeliveryDetail);
  } else {
      orderDeliveryDetail.setOrderId(orderDeliveryDto.getOrderId());
      orderDeliveryDetail.setCreateTime(now);
      orderDeliveryDetail.setUpdateTime(now);
      orderDeliveryDetail.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
      orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
      orderDeliveryDetail.setDeliveryStatus(0);
      orderDeliveryDetail.setImportFileUrl(filePath);
      orderDeliveryDetailMapper.save(orderDeliveryDetail);
  }
  
  return filePath;
  
}

/**
* 处理正常d的
* @param orderDeliveryDto
* @param list
* @param detailMap
* @param excelPath
* @param now
*/
private void saveAllRightOrderDeliverDetail(OrderDeliveryDto orderDeliveryDto, List<Map<String, String>> list, Map<String, Integer> detailMap, String excelPath, String now){
  
  OrderDeliveryDetail orderdel = new OrderDeliveryDetail();
  orderdel.setFlowId(orderDeliveryDto.getFlowId());
  List<OrderDeliveryDetail> orderDeliveryDetails = orderDeliveryDetailMapper.listByProperty(orderdel);
  if (orderDeliveryDetails.size() > 0) {
      List<Integer> idsList=new ArrayList<Integer>();
      for (OrderDeliveryDetail odd:orderDeliveryDetails){
          idsList.add(odd.getOrderDeliveryDetailId());
      }
      orderDeliveryDetailMapper.deleteByPKeys(idsList);
  }
  int i = 1;
  if (!UtilHelper.isEmpty(excelPath)) {//如果有批次文件
      for (Map<String, String> rowMap : list) {
      	
          OrderDeliveryDetail orderDeliveryDetail = new OrderDeliveryDetail();
          orderDeliveryDetail.setOrderLineNo(createOrderLineNo(i, orderDeliveryDto.getFlowId()));
          orderDeliveryDetail.setOrderId(orderDeliveryDto.getOrderId());
          orderDeliveryDetail.setFlowId(orderDeliveryDto.getFlowId());
          orderDeliveryDetail.setDeliveryStatus(1);
          orderDeliveryDetail.setBatchNumber(rowMap.get("6")); //批号
          orderDeliveryDetail.setOrderDetailId(detailMap.get(rowMap.get("2")));
          
          orderDeliveryDetail.setDeliveryProductCount(Integer.parseInt(rowMap.get("8")));
          orderDeliveryDetail.setValidUntil(rowMap.get("7")); //有效期至
          
          orderDeliveryDetail.setImportFileUrl(excelPath);
          orderDeliveryDetail.setCreateTime(now);
          orderDeliveryDetail.setUpdateTime(now);
          orderDeliveryDetail.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
          orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());

          
          i++;
          orderDeliveryDetailMapper.save(orderDeliveryDetail);
      }

  }else {//没有批次文件
               //正常订单发货
              List<OrderDetail> orderDetails = orderDetailMapper.listOrderDetailInfoByOrderId(orderDeliveryDto.getOrderId());
              for (OrderDetail orderDetail : orderDetails) {
                  OrderDeliveryDetail orderDeliveryDetail = new OrderDeliveryDetail();
                  orderDeliveryDetail.setOrderLineNo(createOrderLineNo(i, orderDeliveryDto.getFlowId()));
                  orderDeliveryDetail.setOrderId(orderDeliveryDto.getOrderId());
                  orderDeliveryDetail.setFlowId(orderDeliveryDto.getFlowId());
                  orderDeliveryDetail.setDeliveryStatus(1);
                  orderDeliveryDetail.setBatchNumber("");
                  orderDeliveryDetail.setOrderDetailId(orderDetail.getOrderDetailId());
                  orderDeliveryDetail.setDeliveryProductCount(orderDetail.getProductCount());
                  orderDeliveryDetail.setImportFileUrl(excelPath);
                  orderDeliveryDetail.setCreateTime(now);
                  orderDeliveryDetail.setUpdateTime(now);
                  orderDeliveryDetail.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
                  orderDeliveryDetail.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
                  orderDeliveryDetailMapper.save(orderDeliveryDetail);
                  i++;
              }
          
  }
}




private void updateAllRightOrderDeliveryMethod(OrderDeliveryDto orderDeliveryDto, Map<String, String> map, String excelPath, String now,IPromotionDubboManageService iPromotionDubboManageService,CreditDubboServiceInterface creditDubboService) throws Exception {
	 //发货成功更新订单状态
   if (orderDeliveryDto.getOrderType() == 1) {
       Order order = orderMapper.getOrderbyFlowId(orderDeliveryDto.getFlowId());
       order.setOrderStatus(SystemOrderStatusEnum.SellerDelivered.getType());
       order.setDeliverTime(now);
       order.setUpdateTime(now);
       order.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
       order.setIsDartDelivery("1");
       orderMapper.update(order);

       //插入日志表
       OrderTrace orderTrace = new OrderTrace();
       orderTrace.setOrderId(order.getOrderId());
       orderTrace.setNodeName("卖家已发货");
       orderTrace.setDealStaff(orderDeliveryDto.getUserDto().getUserName());
       orderTrace.setRecordDate(now);
       orderTrace.setRecordStaff(orderDeliveryDto.getUserDto().getUserName());
       orderTrace.setOrderStatus(order.getOrderStatus());
       orderTrace.setCreateTime(now);
       orderTrace.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
       orderTraceMapper.save(orderTrace);

       //生成发货信息
       UsermanageReceiverAddress receiverAddress = receiverAddressMapper.getByPK(orderDeliveryDto.getReceiverAddressId());
       OrderDelivery orderDelivery = orderDeliveryMapper.getByFlowId(orderDeliveryDto.getFlowId());
       orderDelivery.setDeliveryMethod(orderDeliveryDto.getDeliveryMethod());
       orderDelivery.setDeliveryContactPerson(orderDeliveryDto.getDeliveryContactPerson());
       orderDelivery.setDeliveryExpressNo(orderDeliveryDto.getDeliveryExpressNo());
       orderDelivery.setDeliveryDate(orderDeliveryDto.getDeliveryDate());
       orderDelivery.setUpdateDate(now);
       orderDelivery.setDeliveryAddress(receiverAddress.getProvinceName() + receiverAddress.getCityName() + receiverAddress.getDistrictName() + receiverAddress.getAddress());
       orderDelivery.setDeliveryPerson(receiverAddress.getReceiverName());
       orderDelivery.setDeliveryContactPhone(receiverAddress.getContactPhone());
       orderDelivery.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
       orderDelivery.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
       orderDelivery.setUpdateTime(now);
       orderDelivery.setCreateTime(now);
       orderDeliveryMapper.update(orderDelivery);
       
       //发货调用扣减冻结库存
       this.updateDeductionInventory(orderDeliveryDto, order,iPromotionDubboManageService,creditDubboService);
       
       //处理剩余的货物
       this.updateAllDeliverYesAndNo(orderDeliveryDto,now);
       
       
   } 
   map.put("code", "1");
   map.put("msg", "发货成功。");
   map.put("fileName", excelPath);
}

/**
 * 发货的时候处理，掉释放冻结的库存，发货调用扣减冻结库存
 * @param orderDeliveryDto
 */
private void updateDeductionInventory(OrderDeliveryDto orderDeliveryDto,Order order,IPromotionDubboManageService iPromotionDubboManageService,CreditDubboServiceInterface creditDubboService){
	
	 //发货调用扣减冻结库存
    OrderDetail orderDetail = new OrderDetail();
    orderDetail.setOrderId(order.getOrderId());
    orderDetail.setSupplyId(orderDeliveryDto.getUserDto().getCustId());
    
    List<OrderDetail> detailList = orderDetailMapper.listByProperty(orderDetail);
    
    String selectIsPartDelivery=orderDeliveryDto.getSelectPartDeliverty();
   
    if(StringUtils.hasText(selectIsPartDelivery) && selectIsPartDelivery.equals("1")){ //剩余商品补发货物
    	
    	 productInventoryManage.deductionInventory(detailList, orderDeliveryDto.getUserDto().getUserName());
 	   
    }else if(StringUtils.hasText(selectIsPartDelivery) && selectIsPartDelivery.equals("0")){ //剩余商品不补发了
    	
    	  //如果剩余的商品不补货物,那么需要将发货的部分扣减掉库存，同时如果不发货的部分包含了活动商品，
    	  //那么需要将该部分扣减掉，如果该订单是账期支付类型的，那么还需要向账期模块发送解冻未发货商品金额的指令
    	  
    	  //处理发货的部分
    	  List<OrderPartDeliveryDto> sendDeliveryList=orderDeliveryDto.getSendDeliveryDtoList();
    	  List<OrderDetail> currentOrderDetailList=new ArrayList<OrderDetail>();
    	  
    	  if(sendDeliveryList!=null && sendDeliveryList.size()>0){
    		  
    		  for(OrderPartDeliveryDto sendOrderBean : sendDeliveryList){
    			    Integer orderDetailId=sendOrderBean.getOrderDetailId();
    			    String productCode=sendOrderBean.getProduceCode();
    			    int sendDeliveryNum=sendOrderBean.getSendDeliveryNum();
    			    
    			    for(OrderDetail innerOrderDetail : detailList){
        			    String innerProduceCode=innerOrderDetail.getProductCode();
        			    Integer innerOrderDetailId=innerOrderDetail.getOrderDetailId();
        			     if(innerProduceCode.equals(productCode) && innerOrderDetailId.intValue()==orderDetailId.intValue()){
        			    	 innerOrderDetail.setProductCount(sendDeliveryNum);
        			    	 currentOrderDetailList.add(innerOrderDetail);
        			     }
        			   
        		   }
    		  }
    		  productInventoryManage.deductionInventory(currentOrderDetailList, orderDeliveryDto.getUserDto().getUserName());
    	  }
    	  
    	  //处理剩余没有发货的部分商品
    	  List<OrderPartDeliveryDto> noSendDeliveryList=orderDeliveryDto.getPartDeliveryDtoList();
    	  if(noSendDeliveryList!=null && noSendDeliveryList.size()>0){
    		  
    		  List<OrderDetail> currentOrderDetailNOSendList=new ArrayList<OrderDetail>();
    		  
    		  for(OrderPartDeliveryDto noSendBean : noSendDeliveryList){
    			  
    			  Integer orderDetailId=noSendBean.getOrderDetailId();
  			     String productCode=noSendBean.getProduceCode();
  			     int noSendDeliveryNum=noSendBean.getNoDeliveryNum();
  			     
  			     
  			   for(OrderDetail innerOrderDetail : detailList){
  				   
   			    String innerProduceCode=innerOrderDetail.getProductCode();
   			    Integer innerOrderDetailId=innerOrderDetail.getOrderDetailId();
   			    
   			     if(innerProduceCode.equals(productCode) && innerOrderDetailId.intValue()==orderDetailId.intValue()){
   			    	 innerOrderDetail.setProductCount(noSendDeliveryNum);
   			    	 currentOrderDetailNOSendList.add(innerOrderDetail);
   			     }
   			   
   		      }
  			     
    			  
    		  }
    		  //释放没发货的库存
    		  this.productInventoryManage.releaseInventoryByOrderDetail(currentOrderDetailNOSendList,order.getOrderId(),orderDeliveryDto.getUserDto().getUserName(), orderDeliveryDto.getUserDto().getUserName(), iPromotionDubboManageService);
    		  
    	  }
    	  
    	  //如果该订单的支付类型是账期支付那么需要将不发货的部分解冻
		try {
			SystemPayType systemPayType = systemPayTypeService.getByPK(order.getPayTypeId());
			
			if(SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType()) && !UtilHelper.isEmpty(creditDubboService)){
				CreditParams creditParams = new CreditParams();
				
				creditParams.setBuyerCode(order.getCustId()+"");
				creditParams.setSellerCode(order.getSupplyId()+"");
				creditParams.setBuyerName(order.getCustName());
				creditParams.setSellerName(order.getSupplyName());
				BigDecimal noSendMoney=this.computerNoDeliveryPreferentialMoney(orderDeliveryDto);
				creditParams.setOrderTotal(noSendMoney);//订单金额
				creditParams.setFlowId(order.getFlowId());//订单编码
				creditParams.setStatus("5"); //退货
				creditParams.setReceiveTime(DateHelper.parseTime(order.getReceiveTime()));
				CreditDubboResult creditDubboResult = creditDubboService.updateCreditRecord(creditParams);
				if(UtilHelper.isEmpty(creditDubboResult) || "0".equals(creditDubboResult.getIsSuccessful())){
					log.error("creditDubboResult error:"+(creditDubboResult !=null?creditDubboResult.getMessage():"接口调用失败！"));
					throw new RuntimeException(creditDubboResult !=null?creditDubboResult.getMessage():"接口调用失败！");
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException("该订单的支付类型不存在");
		}
    	
    }else{
    	throw new RuntimeException("没有选择剩余商品是否补发货");
    }
	
}

/**
 * 当用户发货是部分发货，同时剩余货物选择了部分发货或者不发货的逻辑处理
 * @param orderDeliveryDto
 * @throws Exception 
 */

public void updateAllDeliverYesAndNo(OrderDeliveryDto orderDeliveryDto,String now) throws Exception{

	String selectIsPartDelivery=orderDeliveryDto.getSelectPartDeliverty();
	
	Order currentOrder=this.orderMapper.getOrderbyFlowId(orderDeliveryDto.getFlowId());
	
	if(StringUtils.hasText(selectIsPartDelivery) && selectIsPartDelivery.equals("1")){ //剩余商品补发货物
		
		  currentOrder.setIsDartDelivery("1");
		  BigDecimal sendAllMoney=this.computerSendDeliveryMoney(orderDeliveryDto);
		  BigDecimal preferentialSendAllMoney=this.computeSendPreferentialDeliveryMoney(orderDeliveryDto);
		  currentOrder.setDeliveryMoney(sendAllMoney);
		  currentOrder.setCancelMoney(new BigDecimal(0));
		  
		  currentOrder.setPreferentialCancelMoney(new BigDecimal(0));
		  currentOrder.setPreferentialDeliveryMoney(preferentialSendAllMoney);
		  
		  this.orderMapper.update(currentOrder);
		  //补发货物剩余货物的异常订单生成
		  this.savePartDeliveryException(orderDeliveryDto,now,currentOrder);
		  
	}else{ //剩余商品不补发货物
		
		    OrderDetail orderDetail = new OrderDetail();
	       orderDetail.setOrderId(orderDeliveryDto.getOrderId());
	       orderDetail.setSupplyId(orderDeliveryDto.getUserDto().getCustId());
	       List<OrderDetail> detailList = orderDetailMapper.listByProperty(orderDetail);
	      
	       List<OrderPartDeliveryDto> noDeliveryList=orderDeliveryDto.getPartDeliveryDtoList(); //剩下的商品
	      
	       for(OrderPartDeliveryDto partDeliveryDto: noDeliveryList){
	    	   
	    	    Integer orderDetailId= partDeliveryDto.getOrderDetailId();
	    	    
	    	    for(OrderDetail currentDetail :detailList){
	    	    	
	    	    	 if(currentDetail.getOrderDetailId().equals(orderDetailId)){
	    	    		 currentDetail.setCancelProduceNum(partDeliveryDto.getNoDeliveryNum());
	    	    		 
	    	    		 this.orderDetailMapper.update(currentDetail);
	    	    	 }
	    	    }
	       }
	       
		  currentOrder.setIsDartDelivery("1");
		  BigDecimal noSendAllMoney=this.computerNoDeliveryMoney(orderDeliveryDto);
		  BigDecimal noSendPreferentialMoney=this.computerNoDeliveryPreferentialMoney(orderDeliveryDto);
		  currentOrder.setCancelMoney(noSendAllMoney);
		  currentOrder.setPreferentialCancelMoney(noSendPreferentialMoney);
		  
		  BigDecimal sendAllMoney=this.computerSendDeliveryMoney(orderDeliveryDto);
		  BigDecimal preferentialSendAllMoney=this.computeSendPreferentialDeliveryMoney(orderDeliveryDto);
		  
		  currentOrder.setDeliveryMoney(sendAllMoney);
		  currentOrder.setPreferentialDeliveryMoney(preferentialSendAllMoney);
		  this.orderMapper.update(currentOrder);
		  
		    SystemPayType systemPayType = this.systemPayTypeService.getByPK(currentOrder.getPayTypeId());
			if(!SystemPayTypeEnum.PayPeriodTerm.getPayType().equals(systemPayType.getPayType())){
			         //剩余商品生产结算信息
				OrderSettlement orderSettlement=new OrderSettlement();
				orderSettlement.setBusinessType(5);
				orderSettlement.setOrderId(currentOrder.getOrderId());
				orderSettlement.setFlowId(currentOrder.getFlowId());
				orderSettlement.setCustId(currentOrder.getCustId());
				orderSettlement.setCustName(currentOrder.getCustName());
				orderSettlement.setConfirmSettlement("0");
				orderSettlement.setSettlementMoney(noSendPreferentialMoney);
				orderSettlement.setRefunSettlementMoney(noSendPreferentialMoney);
				orderSettlement.setSettlementTime(now);
				orderSettlement.setOrderTime(currentOrder.getCreateTime());
				orderSettlement.setCreateTime(now);
				orderSettlement.setCreateUser(orderDeliveryDto.getUserDto().getUserName());
				orderSettlement.setUpdateTime(now);
				orderSettlement.setUpdateUser(orderDeliveryDto.getUserDto().getUserName());
				this.orderSettlementService.save(orderSettlement);
			}
		  
	}
	
}

/**
 * 计算发货的金额
 * @param orderDeliveryDto
 * @return
 */
private BigDecimal computerSendDeliveryMoney(OrderDeliveryDto orderDeliveryDto){
	             BigDecimal sendAllMoney=new BigDecimal(0);
	             //计算所发货物的金额
			  List<OrderPartDeliveryDto> sendDeliveryList=orderDeliveryDto.getSendDeliveryDtoList();
			  if(sendDeliveryList!=null && sendDeliveryList.size()>0){
			
				  for(OrderPartDeliveryDto bean : sendDeliveryList){
					   int sendDeliveryNum=bean.getSendDeliveryNum();
					   BigDecimal currentAllMoney=bean.getProducePrice().multiply(new BigDecimal(sendDeliveryNum));
					   sendAllMoney=sendAllMoney.add(currentAllMoney);
				  }
				  
			  }
			  return sendAllMoney;
}

/**
 * 计算发货的商品的优惠后的金额
 * @param orderDeliveryDto
 * @return
 */
private BigDecimal computeSendPreferentialDeliveryMoney(OrderDeliveryDto orderDeliveryDto){
		  BigDecimal sendAllMoney=new BigDecimal(0);
		    //计算所发货物的金额
		 List<OrderPartDeliveryDto> sendDeliveryList=orderDeliveryDto.getSendDeliveryDtoList();
		 if(sendDeliveryList!=null && sendDeliveryList.size()>0){
			 
			  for(OrderPartDeliveryDto bean : sendDeliveryList){
				  
				   int sendDeliveryNum=bean.getSendDeliveryNum();
				   Integer orderDetailId=bean.getOrderDetailId();
				   
				   OrderDetail orderDetail=this.orderDetailMapper.getByPK(orderDetailId);
				   
				   if(!UtilHelper.isEmpty(orderDetail.getPreferentialCollectionMoney())){
					   
					    String[] moneyList=orderDetail.getPreferentialCollectionMoney().split(",");
						BigDecimal shareMoney=new BigDecimal(0);
						for(String currentMoney : moneyList){
							BigDecimal value=new BigDecimal(currentMoney);
							shareMoney=shareMoney.add(value);
						}
						BigDecimal orderDetailMoney=orderDetail.getProductSettlementPrice(); //该笔商品的结算金额
						BigDecimal lastOrderDetailShareMoney=orderDetailMoney.subtract(shareMoney); //减去优惠后的钱
						BigDecimal bigDecimal = new BigDecimal(sendDeliveryNum);
						BigDecimal allRecord=new BigDecimal(orderDetail.getProductCount());
						
						double currentReturnMoneyTotal=(bigDecimal.doubleValue()/allRecord.doubleValue())*(lastOrderDetailShareMoney.doubleValue());
						BigDecimal currentReturnMoneyValue=new BigDecimal(currentReturnMoneyTotal);
						sendAllMoney=sendAllMoney.add(currentReturnMoneyValue);
						
					
					   
				   }else{
					   BigDecimal currentAllMoney=bean.getProducePrice().multiply(new BigDecimal(sendDeliveryNum));
					   sendAllMoney=sendAllMoney.add(currentAllMoney);
				   }
				   
			  }
			  
		 }
	     sendAllMoney=sendAllMoney.setScale(2,BigDecimal.ROUND_HALF_UP);
		 return sendAllMoney;
	
}

/**
 * 计算未发货的优惠后的金额
 * @param orderDeliveryDto
 * @return
 */
private BigDecimal computerNoDeliveryPreferentialMoney(OrderDeliveryDto orderDeliveryDto){
	  
	List<OrderPartDeliveryDto> noDeliveryList=orderDeliveryDto.getPartDeliveryDtoList(); //剩下的商品
      
      BigDecimal noSendAllMoney=new BigDecimal(0);
      for(OrderPartDeliveryDto partDeliveryDto: noDeliveryList){
   	            //计算没发的货物金额
   	              int noDeliveryNum=partDeliveryDto.getNoDeliveryNum();
   	           
   	              Integer orderDetailId=partDeliveryDto.getOrderDetailId();
			   
			   OrderDetail orderDetail=this.orderDetailMapper.getByPK(orderDetailId);
			   
			   if(!UtilHelper.isEmpty(orderDetail.getPreferentialCollectionMoney())){
				   
				    String[] moneyList=orderDetail.getPreferentialCollectionMoney().split(",");
					BigDecimal shareMoney=new BigDecimal(0);
					for(String currentMoney : moneyList){
						BigDecimal value=new BigDecimal(currentMoney);
						shareMoney=shareMoney.add(value);
					}
					BigDecimal orderDetailMoney=orderDetail.getProductSettlementPrice(); //该笔商品的结算金额
					BigDecimal lastOrderDetailShareMoney=orderDetailMoney.subtract(shareMoney); //减去优惠后的钱
					BigDecimal bigDecimal = new BigDecimal(noDeliveryNum);
					BigDecimal allRecord=new BigDecimal(orderDetail.getProductCount());
					
					double currentReturnMoneyTotal=(bigDecimal.doubleValue()/allRecord.doubleValue())*(lastOrderDetailShareMoney.doubleValue());
					BigDecimal currentReturnMoneyValue=new BigDecimal(currentReturnMoneyTotal);
					noSendAllMoney=noSendAllMoney.add(currentReturnMoneyValue);
				   
			   }else{
				   BigDecimal currentAllMoney=partDeliveryDto.getProducePrice().multiply(new BigDecimal(noDeliveryNum));
				   noSendAllMoney=noSendAllMoney.add(currentAllMoney);
			   }
   	           
   	           
			  
      }
  	    noSendAllMoney=noSendAllMoney.setScale(2,BigDecimal.ROUND_HALF_UP);
      return noSendAllMoney;
	
}

/**
 * 计算未发货的金额
 * @param orderDeliveryDto
 * @return
 */
private BigDecimal computerNoDeliveryMoney(OrderDeliveryDto orderDeliveryDto){
	  
	List<OrderPartDeliveryDto> noDeliveryList=orderDeliveryDto.getPartDeliveryDtoList(); //剩下的商品
      
      BigDecimal noSendAllMoney=new BigDecimal(0);
      for(OrderPartDeliveryDto partDeliveryDto: noDeliveryList){
   	            //计算没发的货物金额
   	           int noDeliveryNum=partDeliveryDto.getNoDeliveryNum();
			   BigDecimal currentAllMoney=partDeliveryDto.getProducePrice().multiply(new BigDecimal(noDeliveryNum));
			   noSendAllMoney=noSendAllMoney.add(currentAllMoney);
      }
      
      return noSendAllMoney;
	
}

/**
 * 卖家发货是部分发货，切剩余货物选择的是补发货物，此时生成补发货物订单
 * @param orderDeliveryDto
 */
private void savePartDeliveryException(OrderDeliveryDto orderDeliveryDto,String now,Order order){
	    //根据类型生产异常订单号
		String exceptionOrderId="BH"+orderDeliveryDto.getFlowId()+"01";
		
		UserDto userDto=orderDeliveryDto.getUserDto();
		
		//插入日志表
		OrderTrace orderTrace = new OrderTrace();
		orderTrace.setOrderId(orderDeliveryDto.getOrderId());
		orderTrace.setNodeName("卖家部分发货,且剩余部分补发货物");
		orderTrace.setDealStaff(orderDeliveryDto.getUserDto().getUserName());
		orderTrace.setRecordDate(now);
		orderTrace.setRecordStaff(userDto.getUserName());
		orderTrace.setOrderStatus(order.getOrderStatus());
		orderTrace.setCreateTime(now);
		orderTrace.setCreateUser(userDto.getUserName());
		orderTraceMapper.save(orderTrace);

		    //生成异常订单
			OrderException orderException=new OrderException();
			orderException.setOrderId(order.getOrderId());
			orderException.setFlowId(orderDeliveryDto.getFlowId());
			orderException.setCreateTime(now);
			orderException.setCreateUser(userDto.getUserName());
			orderException.setReturnType("3");//补货
			orderException.setReturnDesc("");
			orderException.setOrderStatus(SystemReplenishmentOrderStatusEnum.SellerConfirmed.getType());//待确认
			orderException.setReviewTime(now);
			
			orderException.setCustId(order.getCustId());
			orderException.setCustName(order.getCustName());
			orderException.setSupplyId(order.getSupplyId());
			orderException.setSupplyName(order.getSupplyName());
			orderException.setOrderCreateTime(now);
			BigDecimal moneyTotal=this.computerNoDeliveryPreferentialMoney(orderDeliveryDto); //未发货的优惠均摊金额
			orderException.setOrderMoney(moneyTotal);
			orderException.setOrderMoneyTotal(order.getOrderTotal());
			orderException.setExceptionOrderId(exceptionOrderId);
			orderException.setUpdateTime(now);
			orderException.setUpdateUser(userDto.getUserName());
			orderExceptionMapper.save(orderException);
			
			
			//保存未发货的商品
			this.saveOrderReturn(orderDeliveryDto, now, order, exceptionOrderId);

			//插入异常订单日志
			OrderTrace orderTrace1 = new OrderTrace();
			orderTrace1.setOrderId(orderException.getExceptionId());
			orderTrace1.setNodeName("卖家部分发货,生成补货订单:" + SystemReplenishmentOrderStatusEnum.SellerConfirmed.getValue());
			
			orderTrace1.setDealStaff(userDto.getUserName());
			orderTrace1.setRecordDate(now);
			orderTrace1.setRecordStaff(userDto.getUserName());
			orderTrace1.setOrderStatus(orderException.getOrderStatus());
			orderTrace1.setCreateTime(now);
			orderTrace1.setCreateUser(userDto.getUserName());
			orderTraceMapper.save(orderTrace1);

	
}

/**
 * 将未发出去的货物保存起来
 * @param orderDeliveryDto
 * @param now
 * @param order
 * @param exceptionOrderId
 */
private void  saveOrderReturn(OrderDeliveryDto orderDeliveryDto,String now,Order order,String exceptionOrderId){
	
	//将没有发出去的货物保存插入t_order_return表
	List<OrderPartDeliveryDto> noDeliveryList=orderDeliveryDto.getPartDeliveryDtoList();
	UserDto userDto=orderDeliveryDto.getUserDto();
	if(noDeliveryList!=null && noDeliveryList.size()>0){
		for(OrderPartDeliveryDto partBean : noDeliveryList){
			
			OrderReturn orderReturn=new OrderReturn();
			orderReturn.setOrderDetailId(partBean.getOrderDetailId());
			orderReturn.setOrderId(order.getOrderId());
			orderReturn.setCustId(userDto.getCustId());
			orderReturn.setReturnCount(partBean.getNoDeliveryNum());
			
			       BigDecimal moneyTotal = new BigDecimal(0);
                   Integer orderDetailId=partBean.getOrderDetailId();
			   
			       OrderDetail orderDetail=this.orderDetailMapper.getByPK(orderDetailId);
			   
			   if(!UtilHelper.isEmpty(orderDetail.getPreferentialCollectionMoney())){
				   
				    String[] moneyList=orderDetail.getPreferentialCollectionMoney().split(",");
					BigDecimal shareMoney=new BigDecimal(0);
					for(String currentMoney : moneyList){
						BigDecimal value=new BigDecimal(currentMoney);
						shareMoney=shareMoney.add(value);
					}
					BigDecimal orderDetailMoney=orderDetail.getProductSettlementPrice(); //该笔商品的结算金额
					BigDecimal lastOrderDetailShareMoney=orderDetailMoney.subtract(shareMoney); //减去优惠后的钱
					BigDecimal bigDecimal = new BigDecimal(orderReturn.getReturnCount());
					BigDecimal allRecord=new BigDecimal(orderDetail.getProductCount());
					
					double currentReturnMoneyTotal=(bigDecimal.doubleValue()/allRecord.doubleValue())*(lastOrderDetailShareMoney.doubleValue());
					BigDecimal currentReturnMoneyValue=new BigDecimal(currentReturnMoneyTotal);
					moneyTotal=moneyTotal.add(currentReturnMoneyValue);
				   
			   }else{
				   BigDecimal currentAllMoney=partBean.getProducePrice().multiply(new BigDecimal(orderReturn.getReturnCount()));
				   moneyTotal=moneyTotal.add(currentAllMoney);
			   }
			   
		     moneyTotal=moneyTotal.setScale(2,BigDecimal.ROUND_HALF_UP);
			orderReturn.setReturnPay(moneyTotal);
			orderReturn.setReturnType("3");
			orderReturn.setReturnDesc("");
			orderReturn.setFlowId(order.getFlowId());
			orderReturn.setReturnStatus("1");//未处理
			orderReturn.setCreateTime(now);
			orderReturn.setUpdateTime(now);
			orderReturn.setCreateUser(userDto.getUserName());
			orderReturn.setUpdateUser(userDto.getUserName());
			orderReturn.setExceptionOrderId(exceptionOrderId);
			
			
			OrderDeliveryDetail parameterDelivery=new OrderDeliveryDetail();
			parameterDelivery.setOrderId(order.getOrderId());
			parameterDelivery.setFlowId(order.getFlowId());
			parameterDelivery.setOrderDetailId(partBean.getOrderDetailId());
			
			List<OrderDeliveryDetail> deliverDetailList=this.orderDeliveryDetailMapper.listByProperty(parameterDelivery);
			OrderDeliveryDetail currenDetailDelivery=deliverDetailList.get(0); 
			
			orderReturn.setOrderDeliveryDetailId(currenDetailDelivery.getOrderDeliveryDetailId());
			orderReturn.setBatchNumber(currenDetailDelivery.getBatchNumber());
			orderReturn.setProductCode(partBean.getProduceCode());
			orderReturnMapper.save(orderReturn);
			
		}
		
	}
	
	
}


/*生成订单行号 订单号加6位数字码 */
public String createOrderLineNo(int i, String flowId) {
   int count = 6 - String.valueOf(i).length();
   StringBuffer stringBuffer = new StringBuffer();
   stringBuffer.append(flowId);
   for (int j = 0; j < count; j++) {
       stringBuffer.append("0");
   }
   return stringBuffer.append(i).toString();
}


	

}
