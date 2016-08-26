
function  bindTabChange() {
    $(".nav-tabs:eq(1) li").on("click",function () {
        if($(this).hasClass("active")){//包含class 就不替换内容
            return ;
        }
        if($(this).index()==0){
            $("#myModalSalesReturnForm thead th:last").html("退货数量");
            $("#myModalSalesReturnForm thead th:last").prev().html("可退数量");
            $("#bodyDiv2 label").html("退货说明:");
        }else {
            $("#myModalSalesReturnForm thead th:last").html("换货数量");
            $("#myModalSalesReturnForm thead th:last").prev().html("可换数量");
            $("#bodyDiv2 label").html("换货说明:");
        }
    })
}

function  changeReturnNum() {
    $('#myModalSalesReturnForm  tr td input[name="returnProductCount"]').on("blur",function () {
        var num = $(this).val();
        var maxNum = $(this).attr("datareturn");
        if(num && num!=""&&maxNum&&maxNum!=""){
            if(parseInt(num)>parseInt(maxNum)){
                $(this).val(maxNum);
            }
        }else{//数据不对，数量置为空
            $(this).val(0);
        }
    })

    $('#myModalSalesReturnForm  tr td input[name="returnProductCount"]').on("afterpaste",function(){
        $(this).val(this.value.replace(/\D/g,''));
        var num = $(this).val();
        var maxNum = $(this).attr("datareturn");
        if(num && num!=""&&maxNum&&maxNum!=""){
            if(parseInt(num)>parseInt(maxNum)){
                $(this).val(maxNum);
            }
        }else{//数据不对，数量置为空
            $(this).val("");
        }
    });
    $('#myModalSalesReturnForm  tr td input[name="returnProductCount"]').on("keyup",function(){
        $(this).val(this.value.replace(/\D/g,''));
        var num = $(this).val();
        var maxNum = $(this).attr("datareturn");
        if(num && num!=""&&maxNum&&maxNum!=""){
            if(parseInt(num)>parseInt(maxNum)){
                $(this).val(maxNum);
            }
        }else{//数据不对，数量置为空
            $(this).val("");
        }
    })
}

function showSalesReturn(flowId){
    $("#myModalSalesReturn").modal("show");
    bindTabChange();
    $("#curflowId").val(flowId);
    flowId = flowId.trim();
    var requestUrl = ctx+"/order/orderDeliveryDetail/listPgReturn";
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

}

    /**
     * 填充表格数据
     * @param data
     */
function fillSaleReturnTable(data) {
        var indexNum = 1;
        if (!data || !data.resultList)
            return;
        var list = data.resultList;
        $("#myModalSalesReturn .table-box2 tbody").html("");
        $("#myModalSalesReturn textarea").val("");
        var trs = "";
        for (var i = 0; i < list.length; i++) {
            var orderDeliveryDetail = list[i];
            var canReturnCount = orderDeliveryDetail.canReturnCount;
            if(canReturnCount == undefined || canReturnCount == null  || (canReturnCount+"") == ""){
                console.info(orderDeliveryDetail.recieveCount);
                canReturnCount = orderDeliveryDetail.recieveCount;
            }else{
                if(parseInt(canReturnCount) <=0){
                    canReturnCount = 0 ;
                }
            }

            var tr = "<tr>";

            tr += "<td>" + orderDeliveryDetail.orderLineNo + "" +
                "<input type='hidden' name='orderDeliveryDetailId' value='"+orderDeliveryDetail.orderDeliveryDetailId+"' >" +
                "<input type='hidden' name='orderDetailId' value='"+orderDeliveryDetail.orderDetailId+"' >" +
                "<input type='hidden' name='flowId' value='"+orderDeliveryDetail.flowId+"' >" +
                "<input type='hidden' name='batchNumber' value='"+orderDeliveryDetail.batchNumber+"' >" +
                "</td>";
            tr += "<td>" + orderDeliveryDetail.productCode + "</td>";
            tr += "<td>" + orderDeliveryDetail.batchNumber + "</td>";
            tr += "<td>" + orderDeliveryDetail.productName + "</td>";
            tr += "<td>" + orderDeliveryDetail.productName + "</td>";
            tr += "<td>" + orderDeliveryDetail.specification + "</td>";
            tr += "<td>" + orderDeliveryDetail.formOfDrug + "</td>";
            tr += "<td>" + orderDeliveryDetail.manufactures + "</td>";
            tr += "<td><span name='canReturnCount'>" + canReturnCount + "</span></td>";
            tr += "<td><input class='form-control' type='text' dataReturn='"+canReturnCount+"' name='returnProductCount'  /></td>";
            tr += "</tr>";
            trs += tr;
        }
        $("#myModalSalesReturn .table tbody").html("").append(trs);
        //绑定数量修改
        changeReturnNum();
}

function confirmSaleReturn(){
    //确认发货
    var returnProductCount = $("#myModalSalesReturn [name='returnProductCount']");
    var orderDetailId = $("#myModalSalesReturn [name='orderDetailId']");
    var orderDeliveryDetailId = $("#myModalSalesReturn [name='orderDeliveryDetailId']");
    var batchNumber = $("#myModalSalesReturn [name='batchNumber']");
    var flowId = $("#curflowId").val();
       var list=[];

    var returnDesc= $("#returnDesc2").val();
    if(!returnDesc || returnDesc == ""){
        alertModal("说明必须填写");
        return ;
    }
    var returnType = $(".nav-tabs:eq(1) .active").index()+1;

    for(var i=0;i<returnProductCount.length;i++){
        if(!$(returnProductCount[i]).val()||$(returnProductCount[i]).val()==""||$(returnProductCount[i]).val() =="0"){
            continue;
        }
       list.push({
            "orderDetailId":$(orderDetailId[i]).val(),
            "orderDeliveryDetailId":$(orderDeliveryDetailId[i]).val(),
            "flowId":flowId,
            "returnType":returnType,
            "returnDesc":returnDesc,
            "batchNumber":$(batchNumber[i]).val(),
            "returnCount":$(returnProductCount[i]).val()
        })

    }

    if(list.length==0){
        alert("没有符合退货的记录，请核实退货数量");
        return ;
    }
    $.ajax({
        url :ctx+'/order/orderReturn/confirmSaleReturn',
        data: JSON.stringify(list),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            console.info(data);
            if(data.code==0){
                alertModal("操作失败，请稍后再试...");
            }else{
                alertModal("操作成功");
                $("#myModalSalesReturn").modal("hide");
            }
        }
    });

}
