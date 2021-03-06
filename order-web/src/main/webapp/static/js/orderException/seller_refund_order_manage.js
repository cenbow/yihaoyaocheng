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

function setOrderCount(orderStatusCount) {
    if (orderStatusCount) {
        if (orderStatusCount['1'])
            $($("a[name='statusCount']")[0]).html('待确认('+orderStatusCount['1']+')');
        else
            $($("a[name='statusCount']")[0]).html('待确认');
        if (orderStatusCount['3'])
            $($("a[name='statusCount']")[1]).html('待买家发货('+orderStatusCount['3']+')');
        else
            $($("a[name='statusCount']")[1]).html('待买家发货');
        if (orderStatusCount['5'])
            $($("a[name='statusCount']")[2]).html('待卖家收货('+orderStatusCount['5']+')');
        else
            $($("a[name='statusCount']")[2]).html('待卖家收货');
        if (orderStatusCount['6'])
            $($("a[name='statusCount']")[3]).html('退款中('+orderStatusCount['6']+')');
        else
            $($("a[name='statusCount']")[3]).html('退款中');
    }
}

function doRefreshData(requestParam) {
    var requestUrl = ctx+"/orderException/listPgSellerRefundOrder";
    tipLoad();
    $.ajax({
        url: requestUrl,
        data: JSON.stringify(requestParam),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            tipRemove();
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
                    tipLoad();
                    var nowpage = data.orderList.page;
                    $("#nowpageedit").val(nowpage);
                    fillTableJson(data.orderList);
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
        var op = createOperation(order);
        var tr = "<tr>";
        tr += "<td><a href='"+ctx+"/orderException/sellerReReturnOrderDetail/" + order.exceptionId + "' class='undeline'>"+order.exceptionOrderId+"</a></td>";
        tr += "<td>" + order.orderCreateTime + "</td>";
        tr += "<td>" + order.custName + "</td>";
        tr += "<td>" + order.orderStatusName + "</td>";
        tr += "<td>&yen" + fmoney(order.orderMoney,2) + "<br/>" + order.payTypeName + "</td>";
        tr += "<td>" + op + "</td>";
        tr += "</tr>";
        trs += tr;
    }
    $(".table-box tbody").append(trs);
    changeColor();
}
function changeColor(){
    $(".table tr:not(:first):odd").css({background:"#f7f7f7"});
    $(".table tr:not(:first):even").css({background:"#fff"});
}

function createOperation(order){
    var str = '';
    if(order.orderStatus == '1')
        str += '<a href="'+ctx+'/orderException/getReturnOrderDetails/'+order.exceptionId+'" class="btn btn-info btn-sm margin-r-10">审核</a>';
    if(order.orderStatus == '5')
        str += '<a href="javascript:showReturnList(\''+order.exceptionOrderId+'\');" class="btn btn-info btn-sm margin-r-10">确认收货</a>';
    return str;
}

function showReturnList (exceptionOrderId){
    $("#myConfirmReturn").modal("show");
    //TODO  请求数据
    var requestUrl = ctx+"/order/orderReturn/listOrderReturn/"+exceptionOrderId;
    $("#curExceptionOrderId").val(exceptionOrderId);
    $.ajax({
        url: requestUrl,
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            if(data&&data.length>0){
                $("#curExceptionOrderId").val(exceptionOrderId);
                fillReturnTable(data)
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModal("数据获取失败");
        }
    });
}

function fillReturnTable(list){

    $("#myConfirmReturn tbody").html("");
    var trs = "";
    for (var i = 0; i < list.length; i++) {
        var orderReturn = list[i];
        var tr = "<tr>";
        tr += "<td>" + orderReturn.orderLineNo + "</td>";
        tr += "<td>" + orderReturn.productCode + "</td>";
        tr += "<td>" + orderReturn.batchNumber + "</td>";
        tr += "<td>" + orderReturn.productName + "</td>";
        tr += "<td>" + (orderReturn.shortName == null?"":orderReturn.shortName) + "</td>";
        tr += "<td>" + orderReturn.specification + "</td>";
        tr += "<td>" + orderReturn.formOfDrug + "</td>";
        tr += "<td>" + orderReturn.manufactures + "</td>";
        tr += "<td>" + orderReturn.returnCount + "</td>";
        tr += "</tr>";
        trs += tr;
    }
    $("#myConfirmReturn tbody").append(trs);
}

function  confirmSaleReturn() {

    var requestUrl = ctx+"/orderException/editConfirmReceiptReturn";
    var data = {"exceptionOrderId":$("#curExceptionOrderId").val()};
    tipLoad();
   $.ajax({
        url: requestUrl,
        type: 'POST',
        data: JSON.stringify(data),
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            tipRemove();
           if(data&&data.msg== true){
               alertModal("操作成功");
               $("#myConfirmReturn").modal("hide");
               doRefreshData(params);
           }else{
               alertModal("操作失败");
           }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            tipRemove();
            alertModal("数据获取失败");
        }
    })
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




