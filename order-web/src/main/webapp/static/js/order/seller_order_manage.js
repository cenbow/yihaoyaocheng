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

    //绑定省市区
    bindAreaData('province','city','district');
    //绑定下载批号模板
    blindDownLoadBatchTemplate();
})
function fnInitPageUtil() {
    $("#J_pager").pager();
}
//初始化数据为  {"pageNo":1 ,"pageSize":22 ,"param":{"province":11。。。。}}格式
function pasretFormData() {
    params = new Object();
    var p = $("#form0").serializeObject()
    params.pageNo = 1;
    params.pageSize = 20;
    params.param = p;

}
//绑定搜索按钮事件
function bindSearchBtn() {
    $("form .btn-info").on("click", function () {
        $("input[name='orderStatus']").val('');
        $($("#myTab").children()[0]).addClass('active');
        $($("#myTab").children()[0]).siblings().removeClass('active');
        params.pageNo = 1;
        pasretFormData();
        doRefreshData(params);
    })
}

/**
 * 绑定省市区操作
 * @param prov
 * @param city
 * @param area
 */
function bindAreaData(prov,city,area){
    var provinceList = getProvince();
    if(provinceList && provinceList.length > 0){
        var provStr = '<option value="">省份</option>';
        for(var i=0;i<provinceList.length;i++){
            provStr += ' <option value="'+provinceList[i].infoCode+'">'+provinceList[i].infoName+'</option>';
        }
        $("#"+prov).html(provStr);
    }
    $("#"+prov).change(function () {
        var cityStr = '<option value="">城市</option>';
        var _prov = $(this).children('option:selected').val();
        $("#"+area).html('<option value="">区/县</option>');
        if(_prov == ''){
            $("#"+city).html(cityStr);
            return;
        }
        var cityList = getCity(_prov);
        if(cityList && cityList.length > 0){
            for(var i=0;i<cityList.length;i++){
                cityStr += ' <option value="'+cityList[i].infoCode+'">'+cityList[i].infoName+'</option>';
            }
            $("#"+city).html(cityStr);
        }
    });
    $("#"+city).change(function () {
        var areaStr = '<option value="">区/县</option>';
        var _city = $(this).children('option:selected').val();
        if(_city == ''){
            $("#"+area).html(areaStr);
            return;
        }
        var areaList = getArea(_city);
        if(areaList && areaList.length > 0){
            for(var i=0;i<areaList.length;i++){
                areaStr += ' <option value="'+areaList[i].infoCode+'">'+areaList[i].infoName+'</option>';
            }
            $("#"+area).html(areaStr);
        }
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
    var requestUrl = ctx+"/order/listPgSellerOrder";
    tipLoad();
    $.ajax({
        url: requestUrl,
        data: JSON.stringify(requestParam),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            tipRemove();
            if (data.statusCode || data.message) {
                alertModal(data.message);
                return;
            }
            console.info(data);
            //设置订单数量
            setOrderCount(data.orderStatusCount);
            //填充表格数据
            fillTableJson(data.sellerOrderList);
            //设置分页组件参数
            // fillPagerUtil(data,requestParam);
            var totalpage = data.sellerOrderList.totalPage;
            var nowpage = data.sellerOrderList.pageNo;
            var totalCount = data.sellerOrderList.total;
            dataList = data.sellerOrderList.resultList;
            $("#orderTotalMoney").html("&yen" + fmoney(data.orderTotalMoney,2));
            $("#orderCount").html(data.orderCount);
            $("#J_pager").attr("current", nowpage);
            $("#J_pager").attr("total", totalpage);
            $("#J_pager").attr("url", requestUrl);
            $("#J_pager").pager({
                data: requestParam,
                requestType: "post",
                asyn: 1,
                contentType: 'application/json;charset=UTF-8',
                callback: function (data, index) {
                    tipLoad();
                    console.info(data);
                    var nowpage = data.sellerOrderList.page;
                    $("#nowpageedit").val(nowpage);
                    fillTableJson(data.sellerOrderList);
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
    console.info(data)
    var indexNum = 1;
    if (!data || !data.resultList)
        return;
    var list = data.resultList;
    $(".table-box tbody").html("");
    var trs = "";
    for (var i = 0; i < list.length; i++) {
        var order = list[i];
        var operation = typeToOperate(order);
        var tr = "<tr>";
        tr += "<td><a href='"+ctx+"/order/getSupplyOrderDetails?flowId=" + order.flowId + "' class='undeline'>"+order.flowId+"</a></td>";
        tr += "<td>" + order.createTime + "</td>";
        tr += "<td>" + order.custName + "</td>";
        tr += "<td>" + order.orderStatusName + "</td>";
        tr += "<td>&yen" + fmoney(order.orgTotal,2) + "<br/>" + order.payTypeName + "</td>";
        tr += "<td>" + operation + "</td>";
        tr += "</tr>";
        trs += tr;
    }
    $(".table-box tbody").append(trs);
    changeColor();
}
function changeColor() {
    $(".table tr:not(:first):odd").css({background: "#f7f7f7"});
    $(".table tr:not(:first):even").css({background: "#fff"});
}
//类型 转换成操作
function typeToOperate(order) {
    var result = '';
    //result = '<span id="order_' + order.orderId + '" ></span>';

    //<button type="button" class="btn btn-info btn-sm margin-r-10">取消订单</button>
    // if (order && order.orderStatus && order.orderStatus == '1' && order.payType && order.payType == 2) {//账期支付+买家已下单
    //     result += '<a href="javascript:sendDelivery(\''+ order.flowId + '\')"  class="btn btn-info btn-sm margin-r-10">发货</a>';
    //     result += '<a href="javascript:cancleOrder(' + order.orderId + ')" class="btn btn-info btn-sm margin-r-10">取消</a>';
    // }
    if (order && order.orderStatus && order.orderStatus == '5' ) {// 买家已付款
        result += '<a href="javascript:sendDelivery(\'' + order.flowId + '\')"  class="btn btn-info btn-sm margin-r-10">发货</a>';
        if( order.payType && (order.payType == 1 || order.payType == 2)){ //在线支付、账期支付
            result += '<a href="javascript:cancleOrder(' + order.orderId + ')" class="btn btn-info btn-sm margin-r-10">取消</a>';    
        }else{
            //线下支付+买家已付款 没有取消按钮
        }
    }

    if (order && order.orderStatus && order.orderStatus == '1' && order.payType && order.payType == 3) {//线下支付+买家已下单
        result += '<a href="javascript:cancleOrder(' + order.orderId + ')" class="btn btn-info btn-sm margin-r-10">取消</a>';
        result += '<a href="'+ctx+'/order/getConfirmMoneyView?flowId='+order.flowId+'" class="btn btn-info btn-sm margin-r-10">收款确认</a>';
    }

    if (order && order.orderStatus && order.orderStatus == '9') {//拒收中
        result += '<a href="'+ctx+'/orderException/getDetails-2/'+order.flowId+'" class="btn btn-info btn-sm margin-r-10">查看拒收订单</a>';
    }

    if (order && order.orderStatus && order.orderStatus == '10') {//补货中
        result += '<a href="'+ctx+'/orderException/getReplenishmentDetails-2/'+order.flowId+'" class="btn btn-info btn-sm margin-r-10">查看补货订单</a>';
    }

    return result;
}

/**
 * 取消订单
 * @param orderId
 */
function cancleOrder(orderId) {
    $("#orderId").val(orderId);
    $("#cancelResult").val('');
    $("#myModalOperate").modal().hide();

}

/**
 * 绑定批号模板导入模板下载
 */
function blindDownLoadBatchTemplate(){
	 $("#batchTemplateExport").on("click", function () {
		 $("#exportTemplateForm").attr("action", ctx+"/order/exportBatchTemplate");
		 $("#exportTemplateForm").submit();
	});
	 
}


/**
 * 发货
 * * @param orderId
 */

function sendDelivery(flowId) {
    $("#sendFlowId").val(flowId);
    $("#batchTemplateFlowId").val(flowId);
    $("#orderTypeTemplate").val("1"); //正常订单发货
    $("#myModalSendDelivery").modal().hide();
    $("#excelFile").val("");
    $("#receiverAddressId").val("");
    $("#deliveryContactPerson").val("");
    $("#deliveryExpressNo").val("");
    $("#deliveryExpressNo2").val("");
    $("#deliveryContactPerson2").val("");
    $("#deliveryExpressNo1").val("");
    $("#deliveryContactPerson1").val("");
    $("#deliveryDate").val("");
    tipLoad();
    $.ajax({
        url: ctx+"/order/orderDelivery/getReceiveAddressList",
        type: 'GET',
        success: function (data) {
            tipRemove();
            console.info(data);
            if (data!=null) {
                    $("#warehouse").html("");
                    var divs = "";
                    for (var i = 0; i < data.length; i++) {
                        var delivery = data[i];
                        var div = "<label class='radio-inline no-margin'>";
                        if(delivery.defaultAddress==1){
                            div += " <input type='radio' checked='true' name='delivery' value='"+delivery.id+"'/> "
                        }else{
                            div += " <input type='radio' name='delivery' value='"+delivery.id+"' /> "
                        }
                        div +=delivery.provinceName+ delivery.cityName+delivery.districtName+delivery.address+
                        "&nbsp;&nbsp;&nbsp;"+  delivery.receiverName+"&nbsp;&nbsp;&nbsp;"+delivery.contactPhone+"</label>";
                        divs += div;
                    }
                    $("#warehouse").append(divs);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            tipRemove();
            alertModal("加载失败");
        }
    });
}


//选择文件
function closeFileInput(target) {
    var flag=checkImgType(target);
    if(!flag) return ;
}

function checkImgType(this_) {
    var filepath = $(this_).val();
    var extStart = filepath.lastIndexOf(".");
    var ext = filepath.substring(extStart, filepath.length).toUpperCase();
    if (ext != ".XLS") {
        alert("请上传正确格式的文件");
        $(this_).val("");
        return false;
    }
    return true;

}

/**
 * 部分发货的界面确定提交函数
 */
function partDeliveryConfirm(){
	var deliveryMethod=$("#hiddenDeliveryMethod").val(); //配送方式
	var fileName=$('#hiddenFileName').val(); //上传的文件名称
	var receiverAddressId= $("#hiddenReceiverAddressId").val();//发货仓库
	var flowId=$('#hiddenSendFlowId').val();//订单号
	var orderType=$('#hiddenOrderType').val();
	var selectPartDeliverty=$("input[type=radio][name=selectPartDeliverty]:checked").val();
	var deliveryContactPerson=$('#hiddenDeliveryContactPerson').val();
	var deliveryExpressNo=$('#hiddenDeliveryExpressNo').val();
	var partComent=$('#partComent').val();
	var dataParamter={
			fileName:fileName,
			receiverAddressId:receiverAddressId,
			deliveryMethod:deliveryMethod,
			orderType:orderType,
			flowId:flowId,
			selectPartDeliverty:selectPartDeliverty,
			deliveryContactPerson:deliveryContactPerson,
	        deliveryExpressNo:deliveryExpressNo,
	        partComent:partComent
	       
	};
	
	
	if(deliveryMethod==1){//自由物流
		var deliveryDate=$("#deliveryDateHidden").val();//发货时间
		dataParamter.deliveryDate=deliveryDate;
	}
	  console.info( JSON.stringify(dataParamter));
	  
	 tipLoad();
	  $.ajax({
	        url: ctx+"/order/orderDelivery/partDeliveryConfirm",
	        data: JSON.stringify(dataParamter),
	        type: 'POST',
	        dataType: 'json',
	        contentType: "application/json;charset=UTF-8",
	        success: function (data) {
	            tipRemove();
	            if (data!=null) {
	            	 console.info(data);
	                 if(data.code==0){
	                	 alertModal(data.msg);
	                 }else{
	                	 
	                	 $("#myConfirmOtherSendMessage").modal("hide");
	                     $("#myModalPrompt").modal().hide();
	                     $("#msgDiv").html("");
	                     var div = "";
	                     if(data.code==1){
	                         div += " <p class='font-size-20 red'><b>发货成功</b></p>"
	                         if(fileName){
	                             div += "<p>可在订单详情中查看批号的导入详情!</p>";
	                         }
	                         pasretFormData();
	                         doRefreshData(params);
	                     }else if(data.code==2){
	                         div += "<p class='font-size-20 red'><b>发货失败</b></p><p>批号信息导入有误，可以直接下载导入失败原因，也可以进入订单详情下载导入失败原因！</p>";
	                         div += "<p><a class='m-l-10 eyesee' href='"+ctx+"/order/orderDetail/downLoad?filePath="+data.fileName+"&fileName=发货批号导入信息'><i class='fa fa-download'></i>&nbsp;点击下载导入失败原因</a></p>";
	                     }
	                     $("#msgDiv").append(div);
	                	 
	                 }
	            }
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	            tipRemove();
	            alertModal("加载失败");
	        }
	    });
	
	
	
}

function sendDeliverysubmit(){
	
    var delivery = $("input[type=radio][name=delivery]:checked");
    var ownw = $("input[type=radio][name=ownw]:checked");

    if(delivery.val()==null||delivery.val()==""){
        alertModal("发货仓库不能为空")
        return;
    }

    var  regNo = /^[0-9a-zA-Z]{1,50}$/;

    var reg = /^0?1[3|4|5|8|7][0-9]\d{8}$/;
    $("#receiverAddressId").val(delivery.val())
    $("#deliveryMethod").val(ownw.val())

    if(ownw.val()==1){
        if($("#deliveryExpressNo1").val()!=null&&$("#deliveryExpressNo1").val()!=""){
            if (!reg.test($("#deliveryExpressNo1").val())) {
                alertModal("请填写正确的手机号")
                return;
            };
        }
        $("#deliveryContactPerson").val($("#deliveryContactPerson1").val())
        $("#deliveryExpressNo").val($("#deliveryExpressNo1").val())
       
    }else{
        if($("#deliveryExpressNo2").val()!=null&&$("#deliveryExpressNo2").val()!=""){
            if (!regNo.test($("#deliveryExpressNo2").val())) {
                alertModal("请输入正确的物流单号")
                return;
            };
        }
        $("#deliveryContactPerson").val($("#deliveryContactPerson2").val())
        $("#deliveryExpressNo").val($("#deliveryExpressNo2").val())
    }
    
    //设置确认对话框的数据
    $('#hiddenReceiverAddressId').val(delivery.val());
    $('#hiddenDeliveryContactPerson').val($("#deliveryContactPerson").val());
    $('#hiddenDeliveryExpressNo').val($("#deliveryExpressNo").val());
    $('#hiddenDeliveryMethod').val($("#deliveryMethod").val());
    $('#deliveryDateHidden').val($('#deliveryDate').val());
    $('#hiddenSendFlowId').val($('#sendFlowId').val());
    $('#hiddenOrderType').val("1");
    
    
    tipLoad();
    $("#sendform").ajaxSubmit({
        url :ctx+'/order/orderDelivery/sendOrderManagerPartDelivery',
        dataType: 'text',
        type: 'POST',
        success: function(data) {
            tipRemove();
            console.info(data);
            var obj=eval("(" + data + ")");
                if(obj.code==0){
                    alertModal(obj.msg);
                }else if(obj.code==3 && obj.isSomeSend && obj.isSomeSend==3){ //检查出是部分发货
                	var fileName=obj.fileName;
                	var partDeliveryJSon=obj.partDeliveryList;
                	 console.info(partDeliveryJSon);
                	$("#myModalSendDelivery").modal("hide");
                    $("#myConfirmOtherSendMessage").modal().hide();
                    $('#hiddenFileName').val(fileName);
                }else{
                    $("#myModalPrompt").modal().hide();
                    $("#msgDiv").html("");
                    var div = "";
                    if(obj.code==1){
                        div += " <p class='font-size-20 red'><b>发货成功</b></p>"
                        $("#myModalSendDelivery").modal("hide");
                        if($("#excelFile").val()!=null&&$("#excelFile").val()!=""){
                            div += "<p>可在订单详情中查看批号的导入详情!</p>";
                        }
                        pasretFormData();
                        doRefreshData(params);
                    }else{
                        div += "<p class='font-size-20 red'><b>发货失败</b></p><p>批号信息导入有误，可以直接下载导入失败原因，也可以进入订单详情下载导入失败原因！</p>";
                        div += "<p><a class='m-l-10 eyesee' href='"+ctx+"/order/orderDetail/downLoad?filePath="+obj.fileName+"&fileName=发货批号导入信息'><i class='fa fa-download'></i>&nbsp;点击下载导入失败原因</a></p>";
                    }
                    $("#msgDiv").append(div);
                }
            }
    });

}

function totab(tab){
    $("#ownw"+tab).prop("checked","checked");
    $("#one"+tab).addClass(" in active")
    if(tab==1){
        $("#one"+2).removeClass(" in active")
    }else{
        $("#one"+1).removeClass(" in active")
    }
}

function doCancle() {


    var orderId = $("#orderId").val().trim();
    var cancelResult = $("#cancelResult").val().trim();
    if(cancelResult==""){
        alertModal("取消原因不能为空")
        return;
    }
    var data = {orderId:orderId,cancelResult:cancelResult};
    tipLoad();
    $.ajax({
        url: ctx+"/order/sellerCancelOrder",
        data: JSON.stringify(data),
        type: 'POST',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            tipRemove();
            if (data.statusCode || data.message) {
                alertModal(data.message);
                return;
            }
            $("#closeM").trigger("click");
            pasretFormData();
            doRefreshData(params);
            alertModal("取消成功");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            tipRemove();
            alertModal("取消失败");
        }
    });

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
    console.info(format(now));
    console.info(format(pre));
}

function format(date){
    var year  = date.getFullYear();
    var month  = date.getMonth()+1;
    var day  = date.getDate();
    return year+'-'+month+'-'+day;
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
