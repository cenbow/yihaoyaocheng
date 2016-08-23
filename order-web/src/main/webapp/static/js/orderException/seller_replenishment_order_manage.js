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
    bindAreaData('province','city','area');
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
        if(_prov == ''){
            $("#"+city).html(cityStr);
            $("#"+area).html('<option value="">区/县</option>');
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
            $($("a[name='statusCount']")[0]).html('待确认('+orderStatusCount['1']+')');
        else
            $($("a[name='statusCount']")[0]).html('待确认');
        if (orderStatusCount['2'])
            $($("a[name='statusCount']")[1]).html('待发货('+orderStatusCount['2']+')');
        else
            $($("a[name='statusCount']")[1]).html('待发货');
        if (orderStatusCount['3'])
            $($("a[name='statusCount']")[2]).html('待收货('+orderStatusCount['3']+')');
        else
            $($("a[name='statusCount']")[2]).html('待收货');
    }
}

function doRefreshData(requestParam) {
    var requestUrl = ctx+"/orderException/listPgSellerReplenishmentOrder";
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
            console.info(data);
            //设置订单数量
            setOrderCount(data.orderStatusCount);
            //填充表格数据
            fillTableJson(data.orderList);
            //设置分页组件参数
            // fillPagerUtil(data,requestParam);
            var totalpage = data.orderList.totalPage;
            var nowpage = data.orderList.pageNo;
            var totalCount = data.orderList.total;
            dataList = data.orderList.resultList;
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
                    console.info(data);
                    var nowpage = data.orderList.page;
                    $("#nowpageedit").val(nowpage);
                    fillTableJson(data.orderList);
                }
            });
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
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
    if(!data || !data.resultList)
        return;
    var list = data.resultList;
    $(".table-box tbody").html("");
    var trs = "";
    for (var i = 0; i < list.length; i++) {
        var order = list[i];
        var operate = getOperateHtml(order.flowId,order.orderStatus,order.exceptionId,order.exceptionOrderId);
        var tr = "<tr>";
        tr += "<td>" + order.exceptionOrderId + "<br/><a href='"+ctx+"/orderException/getReplenishmentDetails-2/" + order.flowId + "' class='btn btn-info btn-sm margin-r-10'>订单详情</a></td>";
        tr += "<td>" + order.orderCreateTime + "</td>";
        tr += "<td>" + order.supplyName + "</td>";
        tr += "<td>" + order.orderStatusName + "</td>";
        tr += "<td>&yen" + fmoney(order.orderMoney,2) + "<br/>" + order.payTypeName + "</td>";
        tr += "<td>" + operate + "</td>";
        tr += "</tr>";
        trs += tr;
    }
    console.info(trs);
    $(".table-box tbody").append(trs);
    changeColor();
}

function getOperateHtml(_flowId,_orderStatus,_exceptionId,_exceptionOrderId){
    var result = "";
    if ( _orderStatus == "1") {
        result = "<a href='"+ctx+"/orderException/getReviewReplenishmentDetails/"+_flowId+"' class='btn btn-info btn-sm margin-r-10'>审核</a>";
    } else if( _orderStatus == "2") {
        result = "<a href='javascript:sendDelivery(\""+_exceptionOrderId+"\");' class='btn btn-info btn-sm margin-r-10')'>发货</a>";
    }
    return result;
}

function changeColor(){
    $(".table tr:not(:first):odd").css({background:"#f7f7f7"});
    $(".table tr:not(:first):even").css({background:"#fff"});
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

    $("input[name='startTime']").val(format(pre));
    $("input[name='endTime']").val(format(now));
    //console.info(format(now));
    //console.info(format(pre));
}

function format(date){
    var year  = date.getFullYear();
    var month  = date.getMonth()+1;
    var day  = date.getDate();
    return year+'-'+month+'-'+day;
}


/**
 * 发货
 * * @param orderId
 */

function sendDelivery(exceptionOrderId) {
    $("#sendFlowId").val(exceptionOrderId);
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

    $.ajax({
        url: ctx+"/order/orderDelivery/getReceiveAddressList",
        type: 'GET',
        success: function (data) {
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

function sendDeliverysubmit(){

    var delivery = $("input[type=radio][name=delivery]:checked");
    var ownw = $("input[type=radio][name=ownw]:checked");

    if($("#excelFile").val()==null||$("#excelFile").val()==""){
        alertModal("请上传文件")
        return;
    }

    if(delivery.val()==null||delivery.val()==""){
        alertModal("发货仓库不能为空")
        return;
    }

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
        $("#deliveryContactPerson").val($("#deliveryContactPerson2").val())
        $("#deliveryExpressNo").val($("#deliveryExpressNo2").val())
    }
    $("#sendform").ajaxSubmit({
        url :ctx+'/order/orderDelivery/sendOrderDelivery',
        dataType: 'text',
        type: 'POST',
        success: function(data) {
            console.info(data);
            var obj=eval("(" + data + ")");
            if(obj.code==0){
                alertModal(obj.msg);
            }else{
                $("#myModalPrompt").modal().hide();
                $("#msgDiv").html("");
                var div = "";
                if(obj.code==1){
                    div += " <p class='font-size-20 red'><b>发货成功</b></p><p>可在订单详情中查看批号的导入详情!</p> "
                    $("#myModalSendDelivery").modal("hide");
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
    var ownw= $("*[name='ownw']");
    $("#ownw"+tab).get(0).checked = "checked"
}

