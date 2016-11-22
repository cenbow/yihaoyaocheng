/**
 * Created by shiyongxi on 2016/8/19.
 */
//补货订单确认收货商品清单
function changeOrderDeliveryDetail(exceptionOrderId) {
    var requestUrl = ctx+"/order/orderDeliveryDetail/sellerChangeOrderDeliveryDetailDto";
    var requestParam = {pageNo:1,pageSize:15,param:{flowId:exceptionOrderId}};
    tipLoad();
    $.ajax({
        url : requestUrl,
        data : JSON.stringify(requestParam),
        type : 'POST',
        dataType:'json',
        contentType : "application/json;charset=UTF-8",
        success : function(data) {
            tipRemove();
            //填充表格数据
            fillTableJson(data);
            var totalpage = data.totalPage;
            var nowpage = data.pageNo;
            var totalCount = data.total;
            $("#J_pager2").attr("current",nowpage);
            $("#J_pager2").attr("total",totalpage);
            $("#J_pager2").attr("url",requestUrl);
            $("#J_pager2").pager({
                data:requestParam,
                requestType:"post",
                asyn:1,
                contentType:'application/json;charset=UTF-8',
                callback:function(data,index){
                    var nowpage = data.page;
                    $("#nowpageedit").val(nowpage);
                    fillTableJson(data);
                }});
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            tipRemove();
            alertModal("数据获取失败",function(){
            });
        }
    });
}

/**
 * 填充表格数据
 * @param data
 */
function fillTableJson(data) {
    console.info(data)
    var indexNum = 1;
    if (!data || !data.resultList)
        return;
    var list = data.resultList;
    $(".table-box2 tbody").html("");
    var trs = "";
    for (var i = 0; i < list.length; i++) {
        var orderDeliveryDetail = list[i];
        var recieveCount=orderDeliveryDetail.recieveCount;
        if(recieveCount == null ){
            recieveCount='';
        }
        var tr = "<tr>";
        tr += "<td>" + orderDeliveryDetail.orderLineNo + "</td>";
        tr += "<td>" + orderDeliveryDetail.productCode + "</td>";
        tr += "<td>" + orderDeliveryDetail.batchNumber + "</td>";
           if(orderDeliveryDetail.validUntil){
        	   tr += "<td>" + orderDeliveryDetail.validUntil + "</td>";
           }else{
        	   tr += "<td></td>";
           }
        tr += "<td>" + orderDeliveryDetail.productName + "</td>";
        tr += "<td>" + orderDeliveryDetail.shortName + "</td>";
        tr += "<td>" + orderDeliveryDetail.specification + "</td>";
        tr += "<td>" + orderDeliveryDetail.formOfDrug + "</td>";
        tr += "<td>" + orderDeliveryDetail.manufactures + "</td>";
        tr += "<td>" + orderDeliveryDetail.productCount + "</td>";
        tr += "<td>" + orderDeliveryDetail.deliveryProductCount + "</td>";
        tr += "<td>" + recieveCount + "</td>";
        tr += "</tr>";
        trs += tr;
    }
    $(".table-box2 tbody").append(trs);
    $("#myModal2").modal();
}
