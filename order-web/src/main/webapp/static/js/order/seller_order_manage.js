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
        tr += "<td>" + order.flowId + "<br/><a href='"+ctx+"/order/getSupplyOrderDetails?flowId=" + order.flowId + "' class='btn btn-info btn-sm margin-r-10' target='_blank'>订单详情</a></td>";
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
    //result = '<span id="order_' + order.orderId + '" ></span>';

    //<button type="button" class="btn btn-info btn-sm margin-r-10">取消订单</button>
    if (order && order.orderStatus && order.orderStatus == '1' && order.payType && order.payType == 2) {//账期支付+买家已下单
        result += '<a href="javascript:sendDelivery(' + order.orderId + ')"  class="btn btn-info btn-sm margin-r-10">发货</a>';
        result += '<a href="javascript:cancleOrder(' + order.orderId + ')" class="btn btn-info btn-sm margin-r-10">取消</a>';
    }
    if (order && order.orderStatus && order.orderStatus == '5' && order.payType && order.payType == 1) {//在线支付+买家已付款
        result += '<a href="javascript:sendDelivery(' + order.orderId + ')"  class="btn btn-info btn-sm margin-r-10">发货</a>';
        result += '<a href="javascript:cancleOrder(' + order.orderId + ')" class="btn btn-info btn-sm margin-r-10">取消</a>';
    }

    if (order && order.orderStatus && order.orderStatus == '1' && order.payType && order.payType == 3) {//线下支付+买家已下单
        result += '<a href="javascript:cancleOrder(' + order.orderId + ')" class="btn btn-info btn-sm margin-r-10">取消</a>';
        result += '<a href="'+ctx+'/order/getConfirmMoneyView?flowId='+order.flowId+'" class="btn btn-info btn-sm margin-r-10" target="_blank">收款确认</a>';
    }
    if (order && order.orderStatus && order.orderStatus == '5' && order.payType && order.payType == 3) {//线下支付+买家已付款
        result += '<a href="javascript:sendDelivery(' + order.orderId + ')"  class="btn btn-info btn-sm margin-r-10">发货</a>';
    }
    if (order && order.orderStatus && order.orderStatus == '9') {//拒收中
        result += '<a href="#" class="btn btn-info btn-sm margin-r-10">查看拒收订单</a>';
    }

    if (order && order.orderStatus && order.orderStatus == '10') {//补货中
        result += '<a href="#" class="btn btn-info btn-sm margin-r-10">查看补货订单</a>';
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
 * 发货
 * * @param orderId
 */

function sendDelivery(orderId) {
    $("#sendOrderId").val(orderId);
    $("#myModalSendDelivery").modal().hide();
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
    if (ext != ".XLSX" && ext != ".XLS") {
        alert("请上传正确格式的文件");
        $(this_).val("");
        return false;
    }
    return true;

}

function sendDeliverysubmit(){

    var delivery = $("input[type=radio][name=delivery]:checked");
    var ownw = $("input[type=radio][name=ownw]:checked");
        $("#receiverAddressId").val(delivery.val())
        $("#deliveryMethod").val(ownw.val())
    if(ownw.val()==1){
        $("#deliveryContactPerson").val($("#deliveryContactPerson1").val())
        $("#deliveryExpressNo").val($("#deliveryContactPerson1").val())
    }else{
        $("#deliveryContactPerson").val($("#deliveryContactPerson2").val())
        $("#deliveryExpressNo").val($("#deliveryContactPerson2").val())
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
                    }else{
                        div += "<p class='font-size-20 red'><b>发货失败</b></p><p>批号信息导入有误，可以直接下载导入失败原因，也可以进入订单详情下载导入失败原因！</p>";
                        div += "<p><a class='m-l-10 eyesee' href='"+ctx+"/order/orderDetail/downLoad?filePath="+obj.fileName+"&fileName=发货批号导入信息.xls'><i class='fa fa-download'></i>&nbsp;批号导入模版下载</a></p>";
                    }
                    $("#msgDiv").append(div);
                }
            }
    });

}

function totab(tab){
    var ownw= $("*[name='ownw']");
    for(var i=0;i<ownw.length;i++){
        ownw.attr("checked","false")
    }
    $("#ownw"+tab).attr("checked","true")
}

function doCancle() {
    var orderId = $("#orderId").val().trim();
    var cancelResult = $("#cancelResult").val().trim();
    var data = {orderId:orderId,cancelResult:cancelResult};
    $.ajax({
        url: ctx+"/order/sellerCancelOrder",
        data: JSON.stringify(data),
        type: 'POST',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
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


/**
 * 销售订单导出
 */
function sellerOrderExport() {
    var _data = "";
    _data += "&province=" + $.trim($('#province').val());
    _data += "&city=" + $.trim($('#city').val());
    _data += "&district=" + $.trim($('#district').val());
    _data += "&flowId=" + $.trim($('#flowId').val());
    _data += "&custName=" + $.trim($('#custName').val());
    _data += "&payType=" + $.trim($('#payType').val());
    _data += "&createBeginTime=" + $.trim($('#createBeginTime').val());
    _data += "&createEndTime=" + $.trim($('#createEndTime').val());
    _data += "&orderStatus=" + $.trim($('#orderStatus').val());
    var requestUrl = ctx+"/order/exportOrder";
    $.download(requestUrl,_data,'post' );

}

// Ajax 文件下载
jQuery.download = function(url, data, method) {    // 获得url和data
    if (url && data) {
        // data 是 string 或者 array/object
        data = typeof data == 'string' ? data : jQuery.param(data);

        // 把参数组装成 form的  input
        var inputs = '';
        jQuery.each(data.split('&'), function () {
            var pair = this.split('=');
            inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '" />';
        });        // request发送请求
        jQuery('<form action="' + url + '" method="' + (method || 'post') + '">' + inputs + '</form>')
            .appendTo('body').submit().remove();
    }
}
