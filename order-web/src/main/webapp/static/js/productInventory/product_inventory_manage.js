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
    var requestUrl = ctx+"/product/productInventory/listPg";
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
                    console.info(data);
                    var nowpage = data.page;
                    $("#nowpageedit").val(nowpage);
                    fillTableJson(data.resultList);
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
        var productInventory = list[i];
        var unavailable='';                                         //是否缺货
        var minimumPacking=productInventory.minimumPacking;         //最小拆零包装
        var currentInventory=productInventory.currentInventory;   //当前库存
        if(currentInventory == null){
            currentInventory='';
        }
        var blockedInventory=productInventory.blockedInventory;     //冻结库存
        if(blockedInventory == null){
            blockedInventory='';
        }
        var frontInventory=productInventory.frontInventory;        //可售库存
        if(frontInventory == null){
            frontInventory='';
        }else{
            if(frontInventory<minimumPacking){
                unavailable='是';
            }else{
                unavailable='否'
            }
        }
        var warningInventory=productInventory.warningInventory;   //预警库存
        if(warningInventory == null){
            warningInventory='';
        }
        var updateTime=productInventory.updateTime;
        if(updateTime == null){
            updateTime='';
        }
        var tr = "<tr>";
        if (currentInventory!='' && warningInventory!='') {
            if(currentInventory<=warningInventory){
                tr += "<td ><i class='fa red fa-warning margin-r-10'></i>" + productInventory.productcodeCompany + "</td>";
            }else{
                tr += "<td >" + productInventory.productcodeCompany + "</td>";
            }
        }else{
            tr += "<td >" + productInventory.productcodeCompany + "</td>";
        }
        tr += "<td>" + productInventory.shortName + "</td>";
        tr += "<td>" + productInventory.spec + "</td>";
        tr += "<td>" + productInventory.factoryName + "</td>";
        tr += "<td>" + currentInventory + "</td>";
        tr += "<td>" + blockedInventory + "</td>";
        tr += "<td>" + frontInventory + "</td>";
        tr += "<td>" + unavailable + "</td>";
        tr += "<td>" + warningInventory + "</td>";
        tr += "<td>" + updateTime + "</td>";
        tr += "<td>"+'<a href="javascript:showModal(\''+ productInventory.productcodeCompany + '\', '+ productInventory.id+ ',\''+ productInventory.productName + '\')"  class="btn btn-info btn-sm cancel_order">编辑</a>'+"</td>";
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

function showModal(productCode,id,productName){
    $("#productCode").html(productCode);
    $("#inventoryCode").val(id);
    $("#productName").val(productName);
    $("#myModal1").modal();
}


function update(){
    var r = /^[+|-]?\d*$/;
    var r1 = /^[+]?\d*$/;
    var productCode = $('#productCode').html();
    if(productCode == null || productCode == '' || typeof  productCode == 'undefined'){
        alertModal("产品编码不能为空");
        return;
    }
    var currentInventory = $("#currentInventory").val().trim();
    if(currentInventory == null || currentInventory == '' || typeof  currentInventory == 'undefined'){
        alertModal("当前库存不能为空");
        return;
    }
    if(!r.test(currentInventory)){
        alertModal("当前库存只能输入整数");
        return;
    }
    var warningInventory = $("#warningInventory").val().trim();
    if(warningInventory == null || warningInventory == '' || typeof  warningInventory == 'undefined'){
        alertModal("预警库存不能为空");
        return;
    }
    if(!r1.test(warningInventory)){
        alertModal("预警库存只能输入正整数");
        return;
    }
    if(parseInt(currentInventory)>0){
        if(parseInt(warningInventory)>=parseInt(currentInventory)){
            alertModal("预警库存大于当前库存");
            return;
        }
    }
    var  inventoryCode=$("#inventoryCode").val().trim();
    var  productName=$("#productName").val().trim();
    var data = {productcodeCompany:productCode,currentInventory:currentInventory,warningInventory:warningInventory,id:inventoryCode,productName:productName};
        $.ajax({
            url: ctx+"/product/productInventory/update",
            data: JSON.stringify(data),
            type: 'POST',
            contentType: "application/json;charset=UTF-8",
            success: function (data) {
                if (data.statusCode || data.message) {
                    alertModal(data.message);
                    return;
                }
                $("#myModal1").modal("hide");
                $("#currentInventory").val("");
                $("#warningInventory").val("");
                pasretFormData();
                doRefreshData(params);
                alertModal("编辑成功");
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModal("编辑失败");
            }

        });
}

//显示EXCEL更新库存
function showExcel(){
    $("#myModal2").modal();
}


//选择文件
function fileType(target) {
    var flag=checkFileType(target);
    if(!flag) return ;
}

function checkFileType(this_) {
    var filepath = $(this_).val();
    var extStart = filepath.lastIndexOf(".");
    var ext = filepath.substring(extStart, filepath.length).toUpperCase();
    if (ext != ".XLS" && ext != ".XLSX") {
        alertModal("请上传正确格式的文件");
        $(this_).val("");
        return false;
    }
    return true;
}


//批量导入EXCEL更新库存
function importExcel(){
    if($("#excelFile").val()==null||$("#excelFile").val()==""){
        alertModal("请上传文件")
        return;
    }
    $("#form1").ajaxSubmit({
        url :ctx+'/product/productInventory/importExcel',
        dataType: 'text',
        type: 'POST',
        success: function(data) {
            console.info(data);
            var obj=eval("(" + data + ")");
            if(obj.code==0){
                alertModal(obj.msg);
            }else{
                $("#myModal2").modal("hide");
                $("#msgDiv").html("");
                var div = "";
                if(obj.failNumbe==0){
                    div += " <p>您成功提交了 <em>"+obj.successNumber+"</em> 条商品，失败 <em>"+obj.failNumber+"</em> 条</p>"
                }else{
                    div += " <p>您成功提交了 <em>"+obj.successNumber+"</em> 条商品，失败 <em>"+obj.failNumber+"</em> 条</p><p><a href='"+ctx+"/order/orderDetail/downLoad?filePath="+obj.filePath+"&fileName=商品库存信息导入' class='blue'>下载失败的EXCEL</a></p>"
                }
                $("#msgDiv").append(div);
                $("#myModal3").modal();
                $("#excelFile").val("");
                pasretFormData();
                doRefreshData(params);
            }
        }
    });

}