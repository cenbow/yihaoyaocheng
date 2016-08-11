var params = undefined;

$(function(){
	//初始化分页插件
	fnInitPageUtil();
	//初始化查询数据
	pasretFormData();
	//获取数据
	doRefreshData(params);
	//绑定 搜索的click事件
	bindSearchBtn();
})
function fnInitPageUtil(){
	$("#J_pager").pager();
}
//初始化数据为  {"pageNo":1 ,"pageSize":22 ,"param":{"province":11。。。。}}格式
function pasretFormData(){
	params = new Object();
	var p = $("form").serializeObject()
	params.pageNo = 1;
	params.pageSize = 15;
	params.param = p;

}
//绑定搜索按钮事件
function bindSearchBtn(){
	$("form .btn-info").on("click",function () {
		params.pageNo = 1;
		pasretFormData();
		doRefreshData(params);
	})
}

function fillPagerUtil(data,requestParam) {
	var totalpage = data.totalPage;
	var nowpage = data.pageNo;
	var totalCount = data.total;
	$("#J_pager").attr("current",nowpage);
	$("#J_pager").attr("total",totalpage);
	$("#J_pager").attr("url",requestUrl);
	$("#J_pager").pager({
		data:requestParam,
		requestType:"post",
		asyn:1,
		callback:function(data,index){
			var nowpage = data.page;
			$("#nowpageedit").val(nowpage);
			fillTableJson(data);
		}});
}

function doRefreshData(requestParam){
	var requestUrl = ctx+"/order/orderSettlement/listPg/t1";
	$.ajax({
		url : requestUrl,
		data : JSON.stringify(requestParam),
		type : 'POST',
		dataType:'json',
		contentType : "application/json;charset=UTF-8",
		success : function(data) {
			//填充表格数据
			fillTableJson(data);
			//设置分页组件参数
			// fillPagerUtil(data,requestParam);
			var totalpage = data.totalPage;
			var nowpage = data.pageNo;
			var totalCount = data.total;
			$("#J_pager").attr("current",nowpage);
			$("#J_pager").attr("total",totalpage);
			$("#J_pager").attr("url",requestUrl);
			$("#J_pager").pager({
				data:requestParam,
				requestType:"post",
				asyn:1,
				contentType:'application/json;charset=UTF-8',
				callback:function(data,index){
					var nowpage = data.page;
					$("#nowpageedit").val(nowpage);
					fillTableJson(data);
				}});
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alertModal("查询结算列表错误",function(){
				closeAlert();
			});
		}
	});
}

//表单转换成 josn
$.fn.serializeObject = function() {
	var json = {};
	var arrObj = this.serializeArray();
	$.each(arrObj, function() {
		if (json[this.name]) {
			if (!json[this.name].push) {
				json[this.name] = [ json[this.name] ];
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
	var indexNum = 1 ;
	var list = data.resultList;
	$(".table-box tbody").html("");
	var trs = "";
	for (var i = 0; i < list.length; i++) {
		var orderSettlemnt = list[i];
		var operation = typeToOperate(orderSettlemnt.businessType,orderSettlemnt.confirmSettlement,orderSettlemnt.orderSettlementId);
		var tr = "<tr>";
		tr += "<td>" + orderSettlemnt.flowId + "</td>";
		tr += "<td>" + orderSettlemnt.payTypeName + "</td>";
		tr += "<td>" + orderSettlemnt.businessTypeName + "</td>";
		tr += "<td>" + orderSettlemnt.supplyName + "</td>";
		tr += "<td>" + orderSettlemnt.orderTime + "</td>";
		tr += "<td>" + orderSettlemnt.settlementTime + "</td>";
		tr += "<td>" + orderSettlemnt.settlementMoney + "</td>";
		tr += "<td>" + orderSettlemnt.confirmSettlementName + "</td>";
		tr += "<td>" +operation + "</td>";
		tr += "</tr>";
		trs += tr;		
	}
	$(".table-box tbody").append(trs);
	changeColor();
	bindOperateBtn();
}
//类型 转换成操作
function typeToOperate(businessType,confirm,settlementId) {
	var result = '';
	if(businessType==2){//只有退款有操作
		if(confirm ==1){
			result = '<button type="button" class="btn btn-info btn-sm editbtn back-detail " data-stmid = "'+settlementId+'">退款详情</button>';
		}
	}
	return result;
}

function changeColor(){
	$(".table tr:not(:first):odd").css({background:"#f7f7f7"});
	$(".table tr:not(:first):even").css({background:"#fff"});
}

function bindOperateBtn() {
	$(".back-opreate").on("click",function () {
		//$("#myModalOperate").modal();
	});
	$(".back-detail").on("click",function () {
		var settlementId = $(this).attr("data-stmid");

		var requestUrl = ctx+"/order/orderSettlement/getByPK/"+settlementId;

		$.ajax({
			url : requestUrl,
			type : 'GET',
			dataType:'json',
			success : function(data) {
				$("#myModalDetail .form-group:eq(0) div" ).html(data.settlementMoney+"元")
				$("#myModalDetail .form-group:eq(1) div" ).html(data.refunSettlementMoney+"元")
				$("#myModalDetail .form-group:eq(2) div" ).html(data.differentMoney+"元")
				$("#myModalDetail .form-group:eq(3) div" ).html(data.remark)
				$("#myModalDetail").modal();
			},
			error : function(XMLHttpRequest, textStatus, errorThrown) {
				alertModal("退款详情信息错误",function(){
					closeAlert();
				});
			}
		});
	});
}


