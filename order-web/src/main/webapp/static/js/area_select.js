/**
 * Created by shiyongxi on 2016/8/18.
 */

$(function(){
    $.fn.loadArea = function($province, $city, $area){
        if(!_areaMap) {
            alert("缺少area_data.js");
            return;
        }

        if($province.size()>0){
            var htmls = ['<option value="">--省份--</option>'];
            for (var o in _areaMap){
                htmls.push('<option value="'+ o.infoCode +'">'+ o.infoName +'</option>')
            }
            $province.html(htmls.join(""));
            $province.change(function(){
                var value = $(this).val();
                htmls = ['<option value="">--市/城--</option>'];
                for (var o in _areaMap[value]){
                    htmls.push('<option value="'+ o.infoCode +'">'+ o.infoName +'</option>')
                }
                $city.html(htmls.join(""));
            });
            $city.change(function(){
                var value = $(this).val();
                htmls = ['<option value="">--县/区--</option>'];
                for (var o in _areaMap[value]){
                    htmls.push('<option value="'+ o.infoCode +'">'+ o.infoName +'</option>')
                }
                $area.html(htmls.join(""));
            });
        }
    };
});
