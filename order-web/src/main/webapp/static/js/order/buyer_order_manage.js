var params = undefined;
var dataList = [];

$(function () {
    //初始化分页插件
    fnInitPageUtil();
    //初始化查询数据
    pasretFormData();
    //获取数据
    doRefreshData(params);
    //绑定 搜索的click事件
    bindSearchBtn();
    //导出
    bindExportBtn();
    
    monitionRadionClick();
})
function fnInitPageUtil() {
    $("#J_pager").pager();
}
//初始化数据为  {"pageNo":1 ,"pageSize":22 ,"param":{"province":11。。。。}}格式
function pasretFormData() {
    params = new Object();
    var p = $("#form1").serializeObject()
    params.pageNo = 1;
    params.pageSize = 20;
    params.param = p;

}
//绑定搜索按钮事件
function bindSearchBtn() {
    $("#search").on("click", function () {
        $("input[name='orderStatus']").val('');
        $($("#myTab").children()[0]).addClass('active');
        $($("#myTab").children()[0]).siblings().removeClass('active');
        params.pageNo = 1;
        pasretFormData();
        doRefreshData(params);
    })
}
//导出
function bindExportBtn(){
	$("#export").on("click", function () {
		 pasretFormData();
		 $("#exportForm").attr("action", ctx+"/order/exportSaleOrder");
		 $("#condition").val(JSON.stringify(params));
		 $("#exportForm").submit();
	});
}

/**
 * 切换订单状态
 * @param status
 */
function changeStatus(status) {
    $("input[name='orderStatus']").val(status);
    params.pageNo = 1;
    pasretFormData();
    doRefreshData(params);
}

function fillPagerUtil(data, requestParam) {
    var totalpage = data.totalPage;
    var nowpage = data.pageNo;
    var totalCount = data.total;
    $("#J_pager").attr("current", nowpage);
    $("#J_pager").attr("total", totalpage);
    $("#J_pager").attr("url", requestUrl);
    $("#J_pager").pager({
        data: requestParam,
        requestType: "post",
        asyn: 1,
        callback: function (data, index) {
            var nowpage = data.page;
            $("#nowpageedit").val(nowpage);
            fillTableJson(data);
        }
    });
}

function setOrderCount(orderStatusCount) {
    if (orderStatusCount) {
        if (orderStatusCount['1'])
            $($("a[name='statusCount']")[0]).html('待付款('+orderStatusCount['1']+')');
        else
            $($("a[name='statusCount']")[0]).html('待付款');
        if (orderStatusCount['2'])
            $($("a[name='statusCount']")[1]).html('待发货('+orderStatusCount['2']+')');
        else
            $($("a[name='statusCount']")[1]).html('待发货');
        if (orderStatusCount['3'])
            $($("a[name='statusCount']")[2]).html('待收货('+orderStatusCount['3']+')');
        else
            $($("a[name='statusCount']")[2]).html('待收货');
        if (orderStatusCount['4'])
            $($("a[name='statusCount']")[3]).html('拒收中('+orderStatusCount['4']+')');
        else
            $($("a[name='statusCount']")[3]).html('拒收中');
        if (orderStatusCount['5'])
            $($("a[name='statusCount']")[4]).html('补货中('+orderStatusCount['5']+')');
        else
            $($("a[name='statusCount']")[4]).html('补货中');
    }
}

function doRefreshData(requestParam) {
    var requestUrl = ctx+"/order/listPgBuyerOrder";
    tipLoad();
    $.ajax({
        url: requestUrl,
        data: JSON.stringify(requestParam),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            if(data.statusCode || data.message){
                alertModal(data.message);
                return;
            }
            //设置订单数量
            setOrderCount(data.orderStatusCount);
            //填充表格数据
            fillTableJson(data.buyerOrderList);
            //设置分页组件参数
            // fillPagerUtil(data,requestParam);
            var totalpage = data.buyerOrderList.totalPage;
            var nowpage = data.buyerOrderList.pageNo;
            var totalCount = data.buyerOrderList.total;
            dataList = data.buyerOrderList.resultList;
            $("#orderTotalMoney").html("&yen" + fmoney(data.orderTotalMoney,2));
            $("#orderCount").html(data.orderCount);
            $("#J_pager").attr("current", nowpage);
            $("#J_pager").attr("total", totalpage);
            $("#J_pager").attr("url", requestUrl);
            tipRemove();
            $("#J_pager").pager({
                data: requestParam,
                requestType: "post",
                asyn: 1,
                contentType: 'application/json;charset=UTF-8',
                callback: function (data, index) {
                    tipLoad();
                    var nowpage = data.buyerOrderList.page;
                    $("#nowpageedit").val(nowpage);
                    fillTableJson(data.buyerOrderList);
                    tipRemove();
                }
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            tipRemove();
            alertModal("数据获取失败");
        }
    });
}

//表单转换成 josn
$.fn.serializeObject = function () {
    var json = {};
    var arrObj = this.serializeArray();
    $.each(arrObj, function () {
        if (json[this.name]) {
            if (!json[this.name].push) {
                json[this.name] = [json[this.name]];
            }
            json[this.name].push(this.value || '');
        } else {
            json[this.name] = this.value || '';
        }
    });
    return json;
};


/**
 * 填充表格数据
 * @param data
 */
function fillTableJson(data) {
    var indexNum = 1;
    if(!data || !data.resultList)
        return;
    var list = data.resultList;
    $(".table-box tbody").html("");
    var trs = "";
    for (var i = 0; i < list.length; i++) {
        var order = list[i];
        var operation = typeToOperate(order);
        var tr = "<tr>";
        
        if(order.isDartDelivery && order.isDartDelivery=='1'){
            tr += "<td style='text-align:right'><a onclick=openSendProductInfo(\""+order.flowId+"\")><span style='color:red;border-style:solid;border-width:1px;border-color:red;'>部分发货</span></a>&nbsp;&nbsp;<a href='"+ctx+"/order/getBuyOrderDetails?flowId=" + order.flowId + "' class='undeline'>"+order.flowId+"</a></td>";
         }else{
         	tr += "<td style='text-align:right'><a href='"+ctx+"/order/getBuyOrderDetails?flowId=" + order.flowId + "' class='undeline'>"+order.flowId+"</td>";
         }
        
        tr += "<td>" + order.createTime + "</td>";
        tr += "<td>" + order.supplyName + "</td>";
        tr += "<td>" + order.orderStatusName + "</td>";
        tr += "<td>&yen" + fmoney(order.orgTotal,2) + "<br/>" + order.payTypeName + "</td>";
        tr += "<td>" + operation + "</td>";
        tr += "</tr>";
        trs += tr;
    }
    $(".table-box tbody").append(trs);
    changeColor();
}


/**
 * 查看部分发货清单
 * @param flowId
 */
function openSendProductInfo(flowId){
	  var requestUrl = ctx+"/order/orderDeliveryDetail/listPg";
	    var flowId=flowId
	    var userType="1";
	    var requestParam = {pageNo:1,pageSize:15,param:{flowId:flowId,userType:userType}};
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
	            fillSendDataTableJson(data);
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
	                    tipLoad();
	                    var nowpage = data.page;
	                    $("#nowpageedit").val(nowpage);
	                    fillSendDataTableJson(data);
	                    tipRemove();
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
 * 填充收发货物清单
 * @param data
 */
function fillSendDataTableJson(data) {
    console.info(data)
    var indexNum = 1;
    if (!data || !data.resultList)
        return;
    var list = data.resultList;
    $(".table-box2 tbody").html("");
    var trs = "";
    //保存部分发货未发货的金额
    var orderSendObj={};
    for (var i = 0; i < list.length; i++) {
        var orderDeliveryDetail = list[i];
        var recieveCount=orderDeliveryDetail.recieveCount;
        if(recieveCount == null){
            recieveCount='';
        }
        var tr = "<tr>";
        tr += "<td>" + orderDeliveryDetail.orderLineNo + "</td>";
        tr += "<td>" + orderDeliveryDetail.productCode + "</td>";
        tr += "<td>" + orderDeliveryDetail.batchNumber + "</td>";
        tr += "<td>" + orderDeliveryDetail.validUntil + "</td>";
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
        
        //处理部分发货的未发货的金额
        if(i==0){
         var currentObject=list[0];
         orderSendObj.partDelivery=currentObject['partDelivery'];
         orderSendObj.cancelmMoney=currentObject['cancelmMoney'];
        }
        
        
    }
    $(".table-box2 tbody").append(trs);
    
    if(orderSendObj.partDelivery){
    	  if(orderSendObj.cancelmMoney){
    		  $('#showProductMoneyDiv').show();
    		  $('#showMoney').html(orderSendObj.cancelmMoney);
    	  }
    	
    }
    $("#myModal2").modal();
}

function changeColor(){
    $(".table tr:not(:first):odd").css({background:"#f7f7f7"});
    $(".table tr:not(:first):even").css({background:"#fff"});
}
window.setInterval(function () {
    doInterval();
}, 1000);
//类型 转换成操作
function typeToOperate(order) {
    var result = '';
    //result = '<span id="order_' + order.orderId + '" ></span>';
    if (order && order.orderStatus && order.orderStatus == '1' && order.payType && order.payType == 1)//在线支付+买家已下单
        result += '<span id="order_' + order.orderId + '" ></span><br/>';
    if (order && order.orderStatus && order.payType && order.orderStatus == '1' && (order.payType == 1 )) {//买家已下单 + （在线支付）
        result += '<a href=' + PAY_DOMAIN + '/orderPay/confirmPay?orderId='+order.orderId+' class="btn btn-info btn-sm margin-r-10">付款</a>';
        result += '<a href="javascript:cancleOrder(' + order.orderId + ')" class="btn btn-info btn-sm margin-r-10">取消</a>';
    }

    if (order && order.orderStatus && order.payType && order.orderStatus == '1' && ( order.payType == 3)) {//买家已下单 + （线下转账）
        result += '<a href=' + domainPath + '/order/accountPayInfo/getByCustId/' + order.supplyId + ' class="btn btn-info btn-sm margin-r-10">付款</a>';
        result += '<a href="javascript:cancleOrder(' + order.orderId + ')" class="btn btn-info btn-sm margin-r-10">取消</a>';
    }

    if(order && order.orderStatus && order.orderStatus == '6'){//卖家已发货
        result += '<span id="order_f_' + order.orderId + '" ></span><br/>';
        result += '<a href="javascript:listPg(\''+order.flowId+'\')" class="btn btn-info btn-sm margin-r-10">确认收货</a>';
        result += '<a href="javascript:showPostponeModal('+order.orderId+')" class="btn btn-info btn-sm margin-r-10">延期收货</a>';
    }
    if(order && order.orderStatus && (order.orderStatus == '9' || order.orderStatus == '15')){//拒收中
        result += '<a href="'+ctx+'/orderException/getDetails-1/'+order.flowId+'" class="btn btn-info btn-sm margin-r-10">查看拒收订单</a>';
    }

    if(order && order.orderStatus && (order.orderStatus == '10' || order.orderStatus == '15')){//补货中
        //result += '<a href="'+ctx+'/orderException/getReplenishmentDetails-1/'+order.flowId+'" class="btn btn-info btn-sm margin-r-10">查看补货订单</a>';
        result += '<a href="'+ctx+'/orderException/buyerReplenishmentOrderManage?flowId='+order.flowId+'" class="btn btn-info btn-sm margin-r-10">查看补货订单</a>';
    }

    if(order && order.orderStatus && (order.orderStatus == '8'||order.orderStatus == '11'||order.orderStatus == '14')){//补货中
        result += '<a href="javascript:void(0);" class="btn btn-info btn-sm margin-r-10" onClick="showSalesReturn(\''+order.flowId+'\')">申请退货/换货</a>';
    }
    return result;
}

/**
 * 取消订单
 * @param orderId
 */
function cancleOrder(orderId) {
    if (window.confirm("订单取消后将无法恢复，确定取消？")) {
        $.ajax({
            url: ctx+"/order/buyerCancelOrder/"+orderId,
            type: 'GET',
            contentType: "application/json;charset=UTF-8",
            success: function (data) {
                if(data.statusCode || data.message){
                    alertModal(data.message);
                    return;
                }
                pasretFormData();
                doRefreshData(params);
                alertModal("取消成功");
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModal("取消失败");
            }
        });
    }
}

/**
 * 页面倒计时定时器
 */
function doInterval() {
    if (dataList && dataList.length > 0) {
        var order;
        for (var i = 0; i < dataList.length; i++) {
            order = dataList[i];
            if (order && order.orderStatus && order.orderStatus == '1' && order.payType && order.payType == 1) {//在线支付+买家已付款
                order.residualTime = order.residualTime - 1;
                dataList[i] = order;
                $("#order_" + order.orderId).html(calNYR("超过支付剩余时间",order.residualTime));
            }
            if(order && order.orderStatus && order.orderStatus == '6'){//卖家已发货
                order.receivedTime = order.receivedTime - 1;
                dataList[i] = order;
                $("#order_f_" + order.orderId).html(calNYR("超过确认收货时间",order.receivedTime));
            }
        }
    }
}

/**
 * 计算年月日
 * @param seconds
 */
function calNYR(msg,seconds) {
    if (seconds <= 0) {
        return msg;
    }
    var str = '剩余';
    var day = parseInt(seconds / (60 * 60 * 24));
    var dayB = parseInt(seconds % (60 * 60 * 24));

    var hour, hourB, min, minB;

    if (dayB != 0) {
        hour = parseInt(dayB / (60 * 60));
        hourB = parseInt(dayB % (60 * 60));
        if (hourB != 0) {
            min = parseInt(hourB / 60);
            minB = parseInt(hourB % 60);
        }
    }

    if (typeof(day) != "undefined" && day != 0)
        str += day + '天';
    if (typeof(hour) != "undefined") {
        if (!(day == 0 && hour == 0))
            str += hour + '时';
    }
    if (typeof(min) != "undefined") {
        if (!(day == 0 && hour == 0 && min == 0))
            str += min + '分';
    }
    if (minB)
        str += minB + '秒';


    return str;

}

/**
 * 获取最近n天日期
 * @param day
 */
function selectDate(day){
    var now = new Date();
    var pre;
    pre = now.valueOf()
    pre = pre + day * 24 * 60 * 60 * 1000
    pre = new Date(pre);

    $("input[name='createBeginTime']").val(format(pre));
    $("input[name='createEndTime']").val(format(now));
}

function format(date){
    var year  = date.getFullYear();
    var month  = date.getMonth()+1;
    var day  = date.getDate();
    return year+'-'+month+'-'+day;
}


function listPg(flowId) {
    $("#crflowId").val(flowId);
    var requestUrl = ctx+"/order/orderDeliveryDetail/listPg";
    var requestParam = {pageNo:1,pageSize:15,param:{flowId:flowId,userType:1}};
    tipLoad();
    $.ajax({
        url : requestUrl,
        data : JSON.stringify(requestParam),
        type : 'POST',
        dataType:'json',
        contentType : "application/json;charset=UTF-8",
        success : function(data) {
        	console.info(data);
            //填充表格数据
            fillTable(data);
            var totalpage = data.totalPage;
            var nowpage = data.pageNo;
            var totalCount = data.total;
            $("#J_pager2").attr("current",nowpage);
            $("#J_pager2").attr("total",totalpage);
            $("#J_pager2").attr("url",requestUrl);
            tipRemove();
            $("#J_pager2").pager({
                data:requestParam,
                requestType:"post",
                asyn:1,
                contentType:'application/json;charset=UTF-8',
                callback:function(data,index){
                    tipLoad();
                    var nowpage = data.page;
                    $("#nowpageedit").val(nowpage);
                    fillTable(data);
                    tipRemove();
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
function fillTable(data) {
    var indexNum = 1;
    if (!data || !data.resultList)
        return;
    var list = data.resultList;
    $(".table-box2 tbody").html("");
    var trs = "";
    for (var i = 0; i < list.length; i++) {
        var orderDeliveryDetail = list[i];
        var tr = "<tr>";

        tr += "<td>" + orderDeliveryDetail.orderLineNo + "" +
        "<input type='hidden' name='list.orderDeliveryDetailId' value='"+orderDeliveryDetail.orderDeliveryDetailId+"' >" +
        "<input type='hidden' name='list.orderDetailId' value='"+orderDeliveryDetail.orderDetailId+"' >" +
        "<input type='hidden' name='list.flowId' value='"+orderDeliveryDetail.flowId+"' >" +
        "</td>";
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
        tr += "<td><input type='hidden' name='list.productCount' value='"+orderDeliveryDetail.deliveryProductCount+"' >" + orderDeliveryDetail.deliveryProductCount + "</td>";
        tr += "<td><input class='form-control' type='text' name='list.recieveCount' value='"+orderDeliveryDetail.deliveryProductCount+"' /></td>";
        tr += "</tr>";
        trs += tr;
    }
    $(".table-box2 tbody").append(trs);
    $("#myModalConfirmReceipt").modal().hide();
    $("#bodyDiv").hide();
    $("#js").prop("checked",false);
    $("#bh").prop("checked",false);
}


function monitionRadionClick(){
	$("input[type=radio][name=ownw]").change(function(){
		var selectedvalue = $("input[type=radio][name=ownw]:checked").val();
		 if(selectedvalue && selectedvalue==3){
	            	$('#buhuoAddressShow').show();
	                $.ajax({
	                    url: ctx+"/order/orderDelivery/getReceiveAddressList",
	                    type: 'GET',
	                    success: function (data) {
	                        tipRemove();
	                        if (data!=null) {
	                                $("#buhuowarehouse").html("");
	                                var divs = "";
	                                for (var i = 0; i < data.length; i++) {
	                                    var delivery = data[i];
	                                    var div = "<label class='radio-inline no-margin' style='margin-left:0px' >";
	                                    if(delivery.defaultAddress==1){
	                                        div += " <input type='radio' checked='true' name='buhuodelivery' value='"+delivery.id+"'/> "
	                                    }else{
	                                        div += " <input type='radio' name='buhuodelivery' value='"+delivery.id+"' /> "
	                                    }
	                                    div +=delivery.provinceName+ delivery.cityName+delivery.districtName+delivery.address+
	                                    "&nbsp;&nbsp;&nbsp;"+  delivery.receiverName+"&nbsp;&nbsp;&nbsp;"+delivery.contactPhone+"</label>";
	                                    divs += div;
	                                }
	                                $("#buhuowarehouse").append(divs);
	                        }
	                    },
	                    error: function (XMLHttpRequest, textStatus, errorThrown) {
	                        tipRemove();
	                        alertModal("加载失败");
	                    }
	                });
		 }else{
			 $('#buhuoAddressShow').hide();
		 }
	});
}

function confirmReceipt(){
    //确认收货
    var productCount = $("[name='list.productCount']");
    var recieveCount = $("[name='list.recieveCount']");
    var orderDetailId = $("[name='list.orderDetailId']");
    var orderDeliveryDetailId = $("[name='list.orderDeliveryDetailId']");
    var flowId = $("#crflowId").val();
    var list=[];
    var flag=true;
    var returnDesc= $("#returnDesc").val();
    var ownw = $("input[type=radio][name=ownw]:checked");

    var type = /^[0-9]*[0-9][0-9]*$/;
    var re = new RegExp(type);

    if($("#bodyDiv:visible").size() == 0){
        for(var i=0;i<productCount.length;i++){
            if($(recieveCount[i]).val()==""){
                alertModal("请填写收货数量");
                return;
            }

            if ($(recieveCount[i]).val().match(re) == null) {
                alertModal("请输入大于等于零的整数!");
                return;
            }

            if($(recieveCount[i]).val()!=$(productCount[i]).val()){
                $("#bodyDiv").show();//display="block";
                $("#js").prop("checked","checked");
                return;
            }
            list.push({"orderDetailId":$(orderDetailId[i]).val(),"orderDeliveryDetailId":$(orderDeliveryDetailId[i]).val(),"flowId":flowId,"returnType":ownw.val(),"returnDesc":returnDesc,"recieveCount":$(recieveCount[i]).val()})
        }
    }else{
        //当补货框出来再次验证收采购与收货量是否相同如果相同则清除处理类型和备注

        var ownw = $("input[type=radio][name=ownw]:checked");
        for(var i=0;i<recieveCount.length;i++){
            if($(recieveCount[i]).val()==""){
                alertModal("请填写收货数量");
                return;
            }

            if ($(recieveCount[i]).val().match(re) == null) {
                alertModal("请输入大于零的整数!");
                return;
            }

            if($(recieveCount[i]).val()!=$(productCount[i]).val()){
                flag=false;
            }

            if(parseInt($(recieveCount[i]).val())>parseInt($(productCount[i]).val())){
                alertModal("收货数量不能大于发货数量!");
                return;
            }

        }
        if(!flag){
            if(ownw.val()==null||ownw.val()==""){
                alertModal("请选择处理类型");
                return;
            }
            for(var i=0;i<recieveCount.length;i++){
                list.push({"orderDetailId":$(orderDetailId[i]).val(),"orderDeliveryDetailId":$(orderDeliveryDetailId[i]).val(),"flowId":flowId,"returnType":ownw.val(),"returnDesc":returnDesc,"recieveCount":$(recieveCount[i]).val()})
            }
        }else{
            for(var i=0;i<recieveCount.length;i++){
                list.push({"orderDetailId":$(orderDetailId[i]).val(),"orderDeliveryDetailId":$(orderDeliveryDetailId[i]).val(),"flowId":flowId,"returnType":"","returnDesc":"","recieveCount":$(recieveCount[i]).val()})
            }
        }
    }
    //处理选择的补货发货地址
    if(ownw.val()==3){//补货
    	 var delivery = $("input[type=radio][name=buhuodelivery]:checked").val();
    	  if(delivery){
    		 $.each(list,function(index,item){
    			 item.selectDeliveryAddressId=delivery;
    		 });
    	  }else{
    		  alertModal("请选择补货的收货地址");
              return;
    	  }
    }
    tipLoad();
    $.ajax({
        url :ctx+'/order/orderDeliveryDetail/confirmReceipt',
        data: JSON.stringify(list),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            tipRemove();
            //var obj=eval("(" + data + ")");
            if(data.code==0){
                alertModal(data.msg);
            }else{
                alertModal(data.msg);
                $("#myModalConfirmReceipt").modal("hide");
                pasretFormData();
                doRefreshData(params);
            }
        }
    });

}

function  showPostponeModal(orderId) {
    $("#postponeOrderId").val(orderId);
    var orderId = $("#postponeOrderId").val();
    tipLoad();
    $.ajax({
        url :ctx+'/order/showPostponeOrder',
        data: "orderId="+orderId,
        type: 'GET',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            //var obj=eval("(" + data + ")");
            tipRemove();
            if(data.delayTimes>=2){
                $("#postponeOrder .modal-body").html("当前订单已延期两次，不可延期")
                $("#postponeOrder").modal("show");
                $("#postponeOrder .modal-footer button:first").hide();
                $("#postponeOrder .modal-footer button:eq(1)").html("确定");
            }else {
                if(data.day && data.day<=3){
                    $("#postponeOrder .modal-body").html("每笔订单最多延期两次，确定延期收货吗?")
                    $("#postponeOrder").modal("show");
                    $("#postponeOrder .modal-footer button").show();
                    $("#postponeOrder .modal-footer button:eq(1)").html("取消");
                }else{
                    $("#postponeOrder .modal-body").html("您好！距离确认收货截止日期前3天内才可以延期!")
                    $("#postponeOrder").modal("show");
                    $("#postponeOrder .modal-footer button:first").hide();
                    $("#postponeOrder .modal-footer button:eq(1)").html("确定");
                }
            }
        }
    });
}


function postponeOrder() {
    var orderId = $("#postponeOrderId").val();
    var jsonData = {"orderId":orderId};
    tipLoad();
    $.ajax({
        url :ctx+'/order/postponeOrder',
        data: JSON.stringify(jsonData),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            //var obj=eval("(" + data + ")");
            tipRemove();
            $("#postponeOrder").modal("hide");
            if(data.code==0){
                alertModal("操作失败了思密达，稍后再试吧");
            }else{
                alertModal("操作成功");
                pasretFormData();
                doRefreshData(params);
            }
        }
    });

}

function fmoney(s, n)
{
    n = n > 0 && n <= 20 ? n : 2;
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(),
        r = s.split(".")[1];
    t = "";
    for(i = 0; i < l.length; i ++ )
    {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    return t.split("").reverse().join("") + "." + r;
}

