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

    seleckAllCheckBox();

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
        params.pageNo = 1;
        pasretFormData();
        doRefreshData(params);
    })
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

function doRefreshData(requestParam) {
    var requestUrl = ctx + "/orderIssuedException/listPg";
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
            //填充表格数据
            fillTableJson(data);
            //设置分页组件参数
            // fillPagerUtil(data,requestParam);
            var totalpage = data.totalPage;
            var nowpage = data.pageNo;
            var totalCount = data.total;
            dataList = data.resultList;
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
                    var nowpage = data.page;
                    $("#nowpageedit").val(nowpage);
                    fillTableJson(data);
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

        if (order.isRelationship == 1 && order.dealStatus == 1 && order.orderStatus == 5) {
            tr += "<td><input type='checkbox' id ='checkboxlist_" + order.flowId + "' value='" + order.flowId + "'></td>";
        } else {
            tr += "<td><input type='checkbox' id ='checkboxlist_" + order.flowId + "' value='" + order.flowId + "' disabled=true></td>";
        }

        tr += "<td id='_flowId'><a href='" + ctx + "/order/getSupplyOrderDetails?flowId=" + order.flowId + "' class='undeline'>" + order.flowId + "</a></td>";
        tr += "<td>" + order.orderCreateTime + "</td>";
        tr += "<td>" + order.orderStatusName + "</td>";
        tr += "<td>" + order.payTypeName + "</td>";
        tr += "<td>" + order.custName + "</td>";
        tr += "<td>" + order.receivePerson + "</td>";
        tr += "<td>" + order.receiveContactPhone + "</td>";
        tr += "<td>" + order.receiveAddress + "</td>";
        tr += "<td>" + order.exceptionTypeName + "</td>";
        tr += "<td>" + operation + "</td>";
        tr += "</tr>";
        trs += tr;
    }
    console.info(trs);
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
    if (order.isRelationship == 0) {//是否有客户关联关系，0否1是
        result += '<a href="javascript:showModal(\'' + order.custId + '\',\'' + order.custName + '\')"  class="btn btn-info btn-sm cancel_order margin-r-5">关联客户</a>';
    }
    if (order.isRelationship == 1 && order.dealStatus == 1 && order.orderStatus == 5) {//是否有客户关联关系，0否1是
        result += '<a href="javascript:issued(\'' + order.flowId + '\')"  class="btn btn-info btn-sm cancel_order margin-r-5">下发</a>';
        result += '<a href="javascript:orderMark(\'' + order.flowId + '\')"  class="btn btn-info btn-sm cancel_order margin-r-5">标记成功</a>';
    }
    result += '<a href="javascript:listOrderIssuedLog(\'' + order.flowId + '\')"  class="btn btn-info btn-sm cancel_order">日志</a>';
    return result;
}

//显示关联客户层
function showModal(custId, custName) {
    $("#custCodeYc").html(custId);
    $("#custNameYc").html(custName);
    $("#myModal2").modal();
}

//异常订单下发
function issued(flowId) {
    var jsonData = {"flowId": flowId};
    tipLoad();
    $.ajax({
        url: ctx + "/orderIssuedException/issued",
        data: JSON.stringify(jsonData),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            tipRemove();
            if (data.statusCode == '1') {
                alertModalb("下发成功！");
                pasretFormData();
                doRefreshData(params);
            } else {
                alertModalb(data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            tipRemove();
            alertModalb("下发失败！");
        }
    });
}


//订单标记
function orderMark(flowId) {
    var jsonData = {"flowId": flowId};
    tipLoad();
    $.ajax({
        url: ctx + "/orderIssuedException/orderMark",
        data: JSON.stringify(jsonData),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            tipRemove();
            if (data.statusCode == '1') {
                alertModalb("标记完成成功！");
                pasretFormData();
                doRefreshData(params);
            } else {
                alertModalb(data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            tipRemove();
            alertModalb("标记完成失败！");
        }
    });
}


//关联客户
function relatedCustomers() {
    var custCodeYc = $('#custCodeYc').html();
    var custNameYc = $('#custNameYc').html();
    var custCodeErp = $("#custCodeErp").val().trim();
    var custNameErp = $("#custNameErp").val().trim();

    var chk_value = [];
    $('input[name="payTypeErp"]:checked').each(function () {
        chk_value.push($(this).val());
    });

    if (custCodeYc == null || custCodeYc == '' || typeof  custCodeYc == 'undefined') {
        alertModal("一号药城客户编码不能为空");
        return;
    }
    if (custNameYc == null || custNameYc == '' || typeof  custNameYc == 'undefined') {
        alertModal("一号药城客户名称不能为空");
        return;
    }
    if (custCodeErp == null || custCodeErp == '' || typeof  custCodeErp == 'undefined') {
        alertModal("本公司客户编码不能为空");
        return;
    }
    if (!validateInput(custCodeErp, /^[0-9a-zA-Z]{1,20}$/, "本公司客户编码", "本公司客户编码不符合规范（请输入字母数字的组合）！")) {
        return;
    }
    if (custNameErp == null || custNameErp == '' || typeof  custNameErp == 'undefined') {
        alertModal("本公司客户名称不能为空");
        return;
    }
    if (chk_value.length == 0) {
        alertModal("订单下发不能为空");
        return;
    }
    var payTypeErp = "";
    $.each(chk_value, function (n, value) {
        payTypeErp += value + ",";
    });
    var payType = payTypeErp.substring(0, payTypeErp.length - 1);

    var jsonData = {"cust_code_yc": custCodeYc,"cust_name_yc":custNameYc,"cust_code_erp":custCodeErp,"cust_name_erp":custNameErp,"pay_type":payType};
    tipLoad();
    $.ajax({
        url: ctx + "/orderIssuedException/relatedCustomers",
        data: JSON.stringify(jsonData),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            tipRemove();
            if (data.status == 'success') {
                $("#myModal2").modal("hide");
                alertModalb("关联客户成功！");
                $("#custCodeErp").val("");
                $("#custNameErp").val("");
                $('input[name="payTypeErp"]:checked').attr("checked",false)
                pasretFormData();
                doRefreshData(params);
            } else {
                alertModalb(data.msg);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            tipRemove();
            alertModalb("关联客户失败！");
        }
    });
}


//日志列表
function listOrderIssuedLog(flowId) {
    var requestUrl = ctx + "/orderIssuedLog/listPg";
    var requestParam = {pageNo: 1, pageSize: 100, param: {flowId: flowId}};
    tipLoad();
    $.ajax({
        url: requestUrl,
        data: JSON.stringify(requestParam),
        type: 'POST',
        dataType: 'json',
        contentType: "application/json;charset=UTF-8",
        success: function (data) {
            tipRemove();
            //填充表格数据
            fillTableJson1(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            tipRemove();
            alertModal("数据获取失败", function () {
            });
        }
    });
}


/**
 * 填充表格数据
 * @param data
 */
function fillTableJson1(data) {
    console.info(data)
    var indexNum = 1;
    if (!data || !data.resultList)
        return;
    var list = data.resultList;
    $(".table-box1 tbody").html("");
    var trs = "";
    for (var i = 0; i < list.length; i++) {
        var orderIssuedLog = list[i];
        var tr = "<tr>";
        tr += "<td>" + orderIssuedLog.operateName + "</td>";
        tr += "<td>" + orderIssuedLog.operator + "</td>";
        tr += "<td>" + orderIssuedLog.operateTime + "</td>";
        tr += "</tr>";
        trs += tr;
    }
    $(".table-box1 tbody").append(trs);
    $("#myModal1").modal();
}

//checkbox全选/全不选
function seleckAllCheckBox() {
    $("#checkboxTitle").unbind("click").bind("click", function () {
        var checked = $(this).prop('checked');
        if (checked) {
            $("#showList input[type='checkbox']").each(function () {
                if (!$(this).prop("disabled")) {
                    $(this).prop("checked", true)
                }
            });
        } else {
            $("input[type='checkbox']").removeProp("checked");
        }
    });
}

//获取选择的值
function getChecked() {
    var flowIds = "";
    $("#showList tr").find(":checkbox:checked").each(function (k) {
        var flowId, isRelationship, dealStatus, orderStatus = "";
        var obj = $(this).parent().parent();
        obj.children("td").each(function (i) {
            var objTd = $(this).attr("id");
            if (objTd == "_flowId") {
                flowId = $(this).text();
            }
        });
        flowIds = flowIds + "," + flowId;
    });
    var str = flowIds.substring(1);
    return str;
}

//1、批量下发、2、批量标记订单
function batchModify(type) {
    var msg = "请勾选一个下发订单！";
    if (type == 2) {
        msg = "请勾选一个标记订单！";
    }
    if (getChecked() != '') {
        var flowIdArr = getChecked().split(',');
        if (flowIdArr.length > 1) {
            alertModalb(msg);
            return;
        }
        if (type == 1) {
            issued(flowIdArr[0]);
        }
        if (type == 2) {
            orderMark(flowIdArr[0]);
        }
    } else {
        alertModalb(msg);
    }
}


function validateInput(data, regStr, col, errorStr){
    var reg = {
        num:regStr
    };
    if($.trim(data) == ""){
        alertModalb(col + "不能为空！",function(){});
        return false;
    }else if($.trim(data).match(reg.num) == null){
        alertModalb(errorStr, function(){});
        return false;
    }

    return true;
}

