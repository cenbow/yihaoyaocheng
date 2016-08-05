var params = undefined;

$(function(){
	//初始化分页插件
	fnInitPageUtil();
	//初始化查询数据
	pasretFormData();
	//获取数据
	doRefreshData(params);
})
function fnInitPageUtil(){
	$("#J_pager").pager();
}
function pasretFormData(){
	params = new Object();
	params.pageNo = 1;
	params.pageSize = 1;
	params.param = {"province":""};
}


function doRefreshData(requestParam){
	var requestUrl = "/order/orderSettlement/listPg/t2";
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
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alertModal("查询结算列表错误",function(){
				closeAlert();
			});
		}
	});
}


function fnInitTableData() {
	ajaxRequest(null);
	$("#nowpageedit").val("1");
}
function ajaxRequest(requestParam){
	tipLoad();
	var requestUrl = "/trade-web/trade/exchange/listRefundOrderPage";
	$.ajax({
		url : requestUrl,
		data : requestParam,	
		type : 'POST',
		dataType:'json',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		success : function(data) {
			data = eval('(' + data + ')');
			//填充表格数据
			fillTableJson(data);
			//设置分页组件参数
			var totalpage = data.paginator.totalPages;
			var nowpage = data.paginator.page;
			var totalCount = data.paginator.totalCount;
			$("#J_pager").attr("current",nowpage);
			$("#J_pager").attr("total",totalpage);
			$("#J_pager").attr("url",requestUrl);
			tipRemove();
			 $("#J_pager").pager({
				data:requestParam,
		    	requestType:"post",
		    	asyn:1,
		    	callback:function(data,index){
		    		tipLoad();
		    		data = eval('(' + data + ')');
		    		var nowpage = data.paginator.page;
		    		$("#nowpageedit").val(nowpage);
		    		fillTableJson(data);
		    		tipRemove();
			 }});
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alertModal("查询用户列表错误",function(){
				closeAlert();
			});
		}
	});
}

/**
 * 填充表格数据
 * @param data
 */
function fillTableJson(data) {
	var indexNum = 1 ;
	var list = data.resultList;
	$(".table-box tbody").html("");
	console.info(list);
	var trs = "发方法反反复复反复反复反复反复反复";
	for (var i = 0; i < list.length; i++) {
		var orderSettlemnt = list[i];
		var tr = "<tr>";
		tr += "<td>" + orderSettlemnt.flowId + "</td>";
		tr += "<td>" + orderSettlemnt.payTypeName + "</td>";
		tr += "<td>" + orderSettlemnt.businessTypeName + "</td>";
		tr += "<td>" + orderSettlemnt.supplyName + "</td>";
		tr += "<td>" + orderSettlemnt.orderTime + "</td>";
		tr += "<td>" + orderSettlemnt.settlementTime + "</td>";
		tr += "<td>" + orderSettlemnt.settlementMoney + "</td>";
		tr += "<td>" + orderSettlemnt.confirmSettlementName + "</td>";
		tr += "<td>" + '<button type="button" class="btn btn-info btn-sm editbtn">付款</button>' + "</td>";
		tr += "</tr>";
		trs += tr;		
	}
	$(".table-box tbody").append(trs);
	changeColor();
//	resetheight();
}


function changeColor(){
	$(".table tr:not(:first):odd").css({background:"#f7f7f7"});
	$(".table tr:not(:first):even").css({background:"#fff"});
}

function initButton(orderCode, refundCode) {
	var tr = '<input type="button" class="btn btn-info btn-sm" value="&nbsp;&nbsp;&nbsp;查&nbsp;看&nbsp;&nbsp;&nbsp;" onclick=showDetail("/trade-web/trade/exchange/queryRefund?orderCode=' + orderCode +'&refundCode='+ refundCode+'&from=1") />';
	return tr;
}

function showDetail(url){
	window.location.href = url;
}

