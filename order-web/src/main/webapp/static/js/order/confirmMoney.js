function submitForm(){
    var refunSettlementMoney=$("#refunSettlementMoney").val().trim();
    var remark=$("#remark").val().trim();
    var reg = /^[0-9]+(.[0-9]{2})?$/;
    if(!refunSettlementMoney.match(reg) || null==refunSettlementMoney || ""==refunSettlementMoney){
        return false;
    }
    var p = $("#confirmForm").serializeObject();
    if (window.confirm("确认收款金额为"+settlementMoney+"元？")) {
        $.ajax({
            url: ctx+"/order/addForConfirmMoney",
            type: 'POST',
            data: JSON.stringify(p),
            contentType: "application/json;charset=UTF-8",
            success: function (data) {
                if(data.statusCode || data.message){
                    alertModal(data.message);
                    return;
                }
                alert("确认收款成功");
                window.location.href=ctx+"/order/sellerOrderManage";
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alertModal("确认收款失败");
            }
        });
    }

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