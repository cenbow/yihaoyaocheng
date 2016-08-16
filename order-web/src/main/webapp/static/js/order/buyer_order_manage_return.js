
function showSalesReturn(flowId){
    $("#myModalSalesReturn").modal("show");
    $("#curflowId").val(flowId);
    flowId = flowId.trim();
    var requestUrl = ctx+"/order/orderDeliveryDetail/listPg";
    var requestParam = {pageNo:1,pageSize:15,param:{flowId:flowId,userType:1}};
    $.ajax({
        url : requestUrl,
        data : JSON.stringify(requestParam),
        type : 'POST',
        dataType:'json',
        contentType : "application/json;charset=UTF-8",
        success : function(data) {

            //填充表格数据
            fillSaleReturnTable(data);
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
                    fillSaleReturnTable(data);
                }});
        },
        error : function(XMLHttpRequest, textStatus, errorThrown) {
            alertModal("数据获取失败",function(){
            });
        }
    });

    /**
     * 填充表格数据
     * @param data
     */
    function fillSaleReturnTable(data) {
        console.info(data)
        var indexNum = 1;
        if (!data || !data.resultList)
            return;
        var list = data.resultList;
        $("#myModalSalesReturn .table-box2 tbody").html("");
        var trs = "";
        for (var i = 0; i < list.length; i++) {
            var orderDeliveryDetail = list[i];
            var canReturnCount = orderDeliveryDetail.deliveryProductCount;
            if(canReturnCount && canReturnCount ==""){
                canReturnCount = orderDeliveryDetail.deliveryProductCount;
            }

            var tr = "<tr>";

            tr += "<td>" + orderDeliveryDetail.orderLineNo + "" +
                "<input type='hidden' name='list.orderDeliveryDetailId' value='"+orderDeliveryDetail.orderDeliveryDetailId+"' >" +
                "<input type='hidden' name='list.orderDetailId' value='"+orderDeliveryDetail.orderDetailId+"' >" +
                "<input type='hidden' name='list.flowId' value='"+orderDeliveryDetail.flowId+"' >" +
                "</td>";
            tr += "<td>" + orderDeliveryDetail.productCode + "</td>";
            tr += "<td>" + orderDeliveryDetail.batchNumber + "</td>";
            tr += "<td>" + orderDeliveryDetail.brandName + "</td>";
            tr += "<td>" + orderDeliveryDetail.productName + "</td>";
            tr += "<td>" + orderDeliveryDetail.specification + "</td>";
            tr += "<td>" + orderDeliveryDetail.formOfDrug + "</td>";
            tr += "<td>" + orderDeliveryDetail.manufactures + "</td>";
            tr += "<td><span name='list.productCount'>" + canReturnCount + "</span></td>";
            tr += "<td><input class='form-control' type='text' id='recieveCount' name='list.recieveCount'  /></td>";
            tr += "</tr>";
            trs += tr;
        }
        $("#myModalSalesReturn .table-box2 tbody").append(trs);
    }
}

function confirmSaleReturn(){
    //确认发货
    var productCount = $("[name='list.productCount']");
    var recieveCount = $("[name='list.recieveCount']");
    var orderDetailId = $("[name='list.orderDetailId']");
    var orderDeliveryDetailId = $("[name='list.orderDeliveryDetailId']");
    var flowId = $("#curflowId").val();
    var list=[];

    var returnDesc= $("#returnDesc2").val();
    var ownw = $("input[type=radio][name=ownw]:checked");
    if($("#bodyDiv:visible").size() == 0){
        for(var i=0;i<productCount.length;i++){
            if($(recieveCount[i]).val()==null){
                alertModal("请填写收货数量");
                return;
            }
            if($(recieveCount[i]).val()!=$(productCount[i]).html()){
                $("#bodyDiv").show();//display="block";
                return;
            }
            list.push({"orderDetailId":$(orderDetailId[i]).val(),"orderDeliveryDetailId":$(orderDeliveryDetailId[i]).val(),"flowId":flowId,"returnType":ownw.val(),"returnDesc":returnDesc,"recieveCount":$(recieveCount[i]).val()})
        }
    }


    console.info(list);

    $.ajax({
        url :ctx+'/order/orderDeliveryDetail/confirmReceipt',
        data: JSON.stringify(list),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            console.info(data);
            //var obj=eval("(" + data + ")");
            if(data.code==0){
                alertModal(data.msg);
            }else{
                alertModal(data.msg);
                $("#myModalSalesReturn").modal("hide");
            }
        }
    });

}
