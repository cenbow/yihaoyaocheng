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
    var p = $("#form1").serializeObject()
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
    if(!data || !data.resultList)
        return;
    var list = data.resultList;
    $(".table-box tbody").html("");
    var trs = "";
    for (var i = 0; i < list.length; i++) {
        var order = list[i];
        var operation = typeToOperate(order);
        var tr = "<tr>";
        tr += "<td>" + order.flowId + "<br/><a href='" + order.flowId + "' class='btn btn-info btn-sm margin-r-10'>订单详情</a></td>";
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
    //result = '<span id="order_' + order.orderId + '" ></span>';
    if (order && order.orderStatus && order.orderStatus == '1' && order.payType && order.payType == 1)//在线支付+买家已下单
        result += '<span id="order_' + order.orderId + '" ></span><br/>';
    if (order && order.orderStatus && order.payType && order.orderStatus == '1' && (order.payType == 1 || order.payType == 3)) {//买家已下单 + （在线支付 或 线下转账）
        result += '<a href="#" class="btn btn-info btn-sm margin-r-10">付款</a>';
        result += '<a href="javascript:cancleOrder(' + order.orderId + ')" class="btn btn-info btn-sm margin-r-10">取消</a>';
    }
    if(order && order.orderStatus && order.orderStatus == '6'){//卖家已发货
        result += '<span id="order_f_' + order.orderId + '" ></span><br/>';
        result += '<a href="#" class="btn btn-info btn-sm margin-r-10">确认收货</a>';
        result += '<a href="#" class="btn btn-info btn-sm margin-r-10">延期收货</a>';
    }
    if(order && order.orderStatus && order.orderStatus == '9'){//拒收中
        result += '<a href="#" class="btn btn-info btn-sm margin-r-10">查看拒收订单</a>';
    }

    if(order && order.orderStatus && order.orderStatus == '10'){//补货中
        result += '<a href="#" class="btn btn-info btn-sm margin-r-10">查看补货订单</a>';
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
    // console.info(dataList)
    // console.info("working...")
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


    console.info(day + " " + hour + " " + min);

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
    console.info(format(now));
    console.info(format(pre));
}

function format(date){
    var year  = date.getFullYear();
    var month  = date.getMonth()+1;
    var day  = date.getDate();
    return year+'-'+month+'-'+day;
}



