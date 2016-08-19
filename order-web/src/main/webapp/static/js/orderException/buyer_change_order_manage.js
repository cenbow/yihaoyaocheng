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
        for (var o in orderStatusCount){
            var  $a = $("a[name='statusCount" + o +"']");
            var text = $a.text();
            var index = text.indexOf("(");
            if(index>0) text = text.substr(0, index);

            $a.html(text + '<span style="color: red;">('+orderStatusCount[o]+')</span>');
        }
    }
}

function doRefreshData(requestParam) {
    var requestUrl = ctx+"/orderException/listPgBuyerChangeGoodsOrder";
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
            setOrderCount(data.rejectOrderStatusCount);
            //填充表格数据
            fillTableJson(data.rejectOrderList);
            //设置分页组件参数
            // fillPagerUtil(data,requestParam);
            var totalpage = data.rejectOrderList.totalPage;
            var nowpage = data.rejectOrderList.pageNo;
            var totalCount = data.rejectOrderList.total;
            dataList = data.rejectOrderList.resultList;
            $("#orderTotalMoney").html("&yen" + fmoney(data.rejectOrderTotalMoney,2));
            $("#orderCount").html(data.rejectOrderCount);
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
                    fillTableJson(data.rejectOrderList);
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
        var tr = "<tr>";
        tr += "<td>" + order.exceptionOrderId + "<br/><a href='" + order.exceptionOrderId + "' class='btn btn-info btn-sm margin-r-10'>订单详情</a></td>";
        tr += "<td>" + order.orderCreateTime + "</td>";
        tr += "<td>" + order.supplyName + "</td>";
        tr += "<td>" + order.orderStatusName + "</td>";
        tr += "<td>&yen" + fmoney(order.orderMoney,2) + "<br/>" + order.payTypeName + "</td>";

        switch (order.orderStatus){
            case "1" :
                tr += "<td><a class='blue' href='javascript:void(0);' onclick='cancleOrder("+ order.exceptionId + ",2)'>取消</a></td>";
                break;
            case "4" :
                tr += "<td><a class='blue' href='javascript:void(0);' onclick='sendDelivery("+ order.exceptionId + ")'>发货</a></td>";
                break;
            case "7" :
                tr += "<td><a class='blue' href='#'>确认收货</a></td>";
                break;
            default:
                tr += "<td></td>";
                break;
        }

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

/**
 * 取消订单
 * @param orderId
 */
function cancleOrder(id, status) {
    if (window.confirm("订单取消后将无法恢复，确定取消？")) {
        $.ajax({
            url: ctx+"/orderException/updateOrderStatus/"+id+"/"+status,
            type: 'GET',
            contentType: "application/json;charset=UTF-8",
            success: function (data) {
                if(data.statusCode || data.message){
                    alertModal(data.message);
                    return;
                }

                if(data.result == "F"){
                    alertModal("取消失败");
                }else {
                    pasretFormData();
                    doRefreshData(params);
                    alertModal("取消成功");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModal("取消失败");
            }
        });
    }
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


/**
 * 发货提交
 *
 */
function sendDeliverysubmit(){

    var delivery = $("input[type=radio][name=delivery]:checked");
    var ownw = $("input[type=radio][name=ownw]:checked");

    if(delivery.val()==null||delivery.val()==""){
        alertModal("发货仓库不能为空")
        return;
    }

    var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
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
        url :ctx+'/order/orderDelivery/sendOrderDeliveryForChange',
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
                    div += " <p class='font-size-20 red'><b>发货成功</b></p>"
                    $("#myModalSendDelivery").modal("hide");
                    pasretFormData();
                    doRefreshData(params);
                }else{
                    div += "<p class='font-size-20 red'><b>发货失败</b></p>";
                }
                $("#msgDiv").append(div);
            }
        }
    });

}

