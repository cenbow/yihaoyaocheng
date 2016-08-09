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
    var p = $("form").serializeObject()
    params.pageNo = 1;
    params.pageSize = 20;
    params.param = p;

}
//绑定搜索按钮事件
function bindSearchBtn() {
    $("form .btn-info").on("click", function () {
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
            $($("span[name='statusCount']")[0]).html(orderStatusCount['1']);
        else
            $($("span[name='statusCount']")[0]).html('');
        if (orderStatusCount['2'])
            $($("span[name='statusCount']")[1]).html(orderStatusCount['2']);
        else
            $($("span[name='statusCount']")[1]).html('');
        if (orderStatusCount['3'])
            $($("span[name='statusCount']")[2]).html(orderStatusCount['3']);
        else
            $($("span[name='statusCount']")[2]).html('');
        if (orderStatusCount['4'])
            $($("span[name='statusCount']")[3]).html(orderStatusCount['4']);
        else
            $($("span[name='statusCount']")[3]).html('');
        if (orderStatusCount['5'])
            $($("span[name='statusCount']")[4]).html(orderStatusCount['5']);
        else
            $($("span[name='statusCount']")[4]).html('');
    }
}

function doRefreshData(requestParam) {
    var requestUrl = "/order/listPgSellerOrder";
    $.ajax({
        url: requestUrl,
        data: JSON.stringify(requestParam),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
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
            $("#orderTotalMoney").html("&yen" + data.orderTotalMoney);
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
                    var nowpage = data.sellerOrderList.page;
                    $("#nowpageedit").val(nowpage);
                    fillTableJson(data.sellerOrderList);
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
    if (!data || !data.resultList)
        return;
    var list = data.resultList;
    $(".table-box tbody").html("");
    var trs = "";
    for (var i = 0; i < list.length; i++) {
        var order = list[i];
        var operation = typeToOperate(order);
        var tr = "<tr>";
        tr += "<td>" + order.flowId + "<br/><a href='" + order.flowId + "'>订单详情</a></td>";
        tr += "<td>" + order.createTime + "</td>";
        tr += "<td>" + order.custName + "</td>";
        tr += "<td>" + order.orderStatusName + "</td>";
        tr += "<td>&yen" + order.orderTotal + "<br/>" + order.payTypeName + "</td>";
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
    result = '<span id="order_' + order.orderId + '" ></span>';

    if (order && order.orderStatus && order.orderStatus == '1' && order.payType && order.payType == 2) {//账期支付+买家已下单
        result = '<a href="#" >发货</a><br/>';
        result += '<a href="javascript:cancleOrder(' + order.orderId + ')" >取消</a><br/>';
    }
    if (order && order.orderStatus && order.orderStatus == '5' && order.payType && order.payType == 1) {//在线支付+买家已付款
        result = '<a href="#" >发货</a><br/>';
        result += '<a href="javascript:cancleOrder(' + order.orderId + ')" >取消</a><br/>';
    }

    if (order && order.orderStatus && order.orderStatus == '1' && order.payType && order.payType == 3) {//线下支付+买家已下单
        result += '<a href="javascript:cancleOrder(' + order.orderId + ')" >取消</a><br/>';
    }
    if (order && order.orderStatus && order.orderStatus == '5' && order.payType && order.payType == 3) {//线下支付+买家已付款
        result = '<a href="#" >发货</a><br/>';
    }
    if (order && order.orderStatus && order.orderStatus == '9') {//拒收中
        result += '<a href="#" >查看拒收订单</a><br/>';
    }

    if (order && order.orderStatus && order.orderStatus == '10') {//补货中
        result += '<a href="#" >查看补货订单</a><br/>';
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

function doCancle() {
    var orderId = $("#orderId").val().trim();
    var cancelResult = $("#cancelResult").val().trim();
    var data = {orderId:orderId,cancelResult:cancelResult};
    $.ajax({
        url: "/order/sellerCancelOrder",
        data: JSON.stringify(data),
        type: 'POST',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            if (data.statusCode || data.message) {
                alertModal(data.message);
                return;
            }
            $("#myModalOperate").modal().hide();
            pasretFormData();
            doRefreshData(params);
            alertModal("取消成功");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alertModal("取消失败");
        }
    });

}



