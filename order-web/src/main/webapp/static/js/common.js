/**
 * 金额千分位显示
 * @param s 金额
 * @param n 小数点后几位
 * @returns {string}
 */
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



$(function(){
    /* 加载商品的图片 */
    $("img").each(function(index,element){
        console.info("index=" + index +",element=" + element + ",src=" + this.src );
        if($(this).hasClass("productImageUrl")){
            var spuCode = $(this).attr("spuCode");
            var productImageUrl = getProductImgUrl(spuCode);
            // console.info("spuCode=" + spuCode + ",productImageUrl=" + productImageUrl);
            this.src = productImageUrl;
        }
    });
});

/**
 * 异步加载每个商品的(单张)图片
 * @param _spuCode
 * @return 商品图片的url地址
 */
function getProductImgUrl(_spuCode){
    var productImageUrl = ctx +"/static/images/img_03.jpg";
    try{
        $.ajax({
            url:ctx + "/product/getProductImg?spuCode="+_spuCode,
            type:"get",
            async:false,
            success:function(data){
                // console.info("data=" + data);
                if(data != null && typeof data != "undefined"  &&  data.trim() != "" ){
                    productImageUrl = data;
                }
            },
            error:function(data){
                console.info("商品图片服务异常!");
            }
        });
    }catch (e){
        return productImageUrl;
    }
    return productImageUrl;
}