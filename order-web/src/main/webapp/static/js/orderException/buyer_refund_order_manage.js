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
        tr += "<td><a href='"+ctx+"/orderException/buyerReReturnOrderDetail/" + order.exceptionId + "' class='undeline'>"+order.exceptionOrderId+"</a></td>";
        tr += "<td>" + order.orderCreateTime + "</td>";
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
    tipLoad();
    if (window.confirm("确定取消退货订单？")) {
        $.ajax({
            url: ctx+"/orderException/buyerCancelRefundOrder/"+exceptionId,
            type: 'GET',
            contentType: "application/json;charset=UTF-8",
            success: function (data) {
                tipRemove();
                if(data.statusCode || data.message){
                    alertModal(data.message);
                    return;
                }
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

function sendDelivery(exceptionId) {
    $("#sendFlowId").val(exceptionId);
    $("#myModalSendDelivery").modal().hide();
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

function totab(tab){
    $("#ownw"+tab).prop("checked","checked");
    $("#one"+tab).addClass(" in active")
    if(tab==1){
        $("#one"+2).removeClass(" in active")
    }else{
        $("#one"+1).removeClass(" in active")
    }
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

    var  regNo = /^[0-9a-zA-Z]{1,50}$/;

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
        if($("#deliveryExpressNo2").val()!=null&&$("#deliveryExpressNo2").val()!=""){
            if (!regNo.test($("#deliveryExpressNo2").val())) {
                alertModal("请输入正确的物流单号")
                return;
            };
        }
        $("#deliveryContactPerson").val($("#deliveryContactPerson2").val())
        $("#deliveryExpressNo").val($("#deliveryExpressNo2").val())
    }
    tipLoad();
    $("#sendform").ajaxSubmit({
        url :ctx+'/order/orderDelivery/sendOrderDeliveryForRefund',
        dataType: 'text',
        type: 'POST',
        success: function(data) {
            tipRemove();
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
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            tipRemove();
            alertModal("操作失败");
        }
    });

}



