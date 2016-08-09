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

function doRefreshData(requestParam) {
    var requestUrl = "/order/listPgBuyerOrder";
    $.ajax({
        url: requestUrl,
        data: JSON.stringify(requestParam),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            if(data.statusCode && data.message){
                alertModal(data.message);
                return;
            }
            console.info(data);
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
                    var nowpage = data.buyerOrderList.page;
                    $("#nowpageedit").val(nowpage);
                    fillTableJson(data.buyerOrderList);
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
    var list = data.resultList;
    $(".table-box tbody").html("");
    var trs = "";
    for (var i = 0; i < list.length; i++) {
        var order = list[i];
        var operation = typeToOperate(order);
        var tr = "<tr>";
        tr += "<td>" + order.flowId + "<br/><a href='" + order.flowId + "'>订单详情</a></td>";
        tr += "<td>" + order.createTime + "</td>";
        tr += "<td>" + order.supplyName + "</td>";
        tr += "<td>" + order.orderStatusName + "</td>";
        tr += "<td>&yen" + order.orderTotal + "<br/>" + order.payTypeName + "</td>";
        tr += "<td>" + operation + "</td>";
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
window.setInterval(function () {
    doInterval();
}, 1000);
//类型 转换成操作
function typeToOperate(order) {
    var result = '';
    result = '<span id="order_' + order.orderId + '" ></span>';
    if (order && order.orderStatus && order.orderStatus == '1' && order.payType && order.payType == 1)//在线支付+买家已下单
        result = '<span id="order_' + order.orderId + '" ></span><br/>';
    if (order && order.orderStatus && order.payType && order.orderStatus == '1' && (order.payType == 1 || order.payType == 3)) {//买家已下单 + （在线支付 或 线下转账）
        result += '<a href=":;" >付款</a><br/>';
        result += '<a href="javascript:cancleOrder(' + order.orderId + ')" >取消</a><br/>';
    }
    if(order && order.orderStatus && order.orderStatus == '6'){//卖家已发货
        result += '<a href=":;" >确认收货</a><br/>';
        result += '<a href=":;" >延期收货</a><br/>';
    }
    if(order && order.orderStatus && order.orderStatus == '9'){//拒收中
        result += '<a href=":;" >查看拒收订单</a><br/>';
    }

    if(order && order.orderStatus && order.orderStatus == '10'){//补货中
        result += '<a href=":;" >查看补货订单</a><br/>';
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
            url: "/order/buyerCancelOrder/"+orderId,
            type: 'GET',
            contentType: "application/json;charset=UTF-8",
            success: function (data) {
                if(data.statusCode && data.message){
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
                $("#order_" + order.orderId).html(calNYR(order.residualTime));
            }
        }
    }
    // console.info(dataList)
    // console.info("working...")
}

/**
 * 计算年月日
 * @param seconds
 */
function calNYR(seconds) {
    if (seconds < 0) {
        return '超过支付剩余时间';
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


    console.info(day + " " + hour + " " + min);

    return str;

}



