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
    var requestUrl = ctx+"/orderException/listPgBuyerRefundOrder";
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
        var op = createOperation(order);
        var tr = "<tr>";
        tr += "<td>" + order.exceptionOrderId + "<br/><a href='"+ctx+"/orderException/getDetails-1/" + order.flowId + "' class='btn btn-info btn-sm margin-r-10'>订单详情</a></td>";
        tr += "<td>" + order.createTime + "</td>";
        tr += "<td>" + order.supplyName + "</td>";
        tr += "<td>" + order.orderStatusName + "</td>";
        tr += "<td>&yen" + fmoney(order.orderMoney,2) + "<br/>" + order.payTypeName + "</td>";
        tr += "<td>" + op + "</td>";
        tr += "</tr>";
        trs += tr;
    }
    console.info(trs);
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
        str += '<a href="javascript:cancleOrder(' + order.exceptionId + ')" class="btn btn-info btn-sm margin-r-10">取消</a>';
    if(order.orderStatus == '3')
        str += '<a href="javascript:sendDelivery(' + order.exceptionId + ')" class="btn btn-info btn-sm margin-r-10">发货</a>';
    return str;
}

/**
 * 取消订单
 * @param orderId
 */
function cancleOrder(exceptionId) {
    if (window.confirm("确定取消退货订单？")) {
        $.ajax({
            url: ctx+"/orderException/buyerCancelRefundOrder/"+exceptionId,
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

function sendDelivery(flowId) {
    $("#sendFlowId").val(flowId);
    $("#myModalSendDelivery").modal().hide();
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

function totab(tab){
    var ownw= $("*[name='ownw']");
    $("#ownw"+tab).get(0).checked = "checked"
}



