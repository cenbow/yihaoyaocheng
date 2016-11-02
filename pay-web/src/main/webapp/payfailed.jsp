<%--
  Created by IntelliJ IDEA.
  User: bigwing
  Date: 2016/10/26
  Time: 10:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>店铺首页-1号药城</title>
    <meta name="Keywords" content="">
    <meta name="description" content="">
    <link rel="stylesheet" href="styles/store.css">
    <link rel="stylesheet" href="styles/lib/swiper.css">
    <link rel="stylesheet" href="styles/lib/fancybox.css">
    <style>
        .pay_error{width: 500px;margin: 0 auto;text-align: center;}
        button{width:168px;height: 48px;font-size: 18px;color: #fff;background: #fe5050;border: none;cursor: pointer;}
        .text{padding: 72px 0 48px 0;font-size: 16px;color: #333;}
    </style>
</head>
<body>
<div class="top">
    <div class="wapper">
        <div class="top-side">
            <ul>
                <li>
                    您好，欢迎您来到1号药城药品信息网
                    <!-- <a href="#" class="pl25">我的名字啦</a>
                    <a href="#">退出</a> -->
                    <a href="#" class="red pl25">请登录</a>
                </li>
                <li><a href="#">我的订单</a></li>
                <li><a href="#">我的1号药城</a></li>
                <li><a href="#">帮助</a></li>
                <li class="last-li">
                    <i class="common-icon top-phone"></i>
                    <span class="red">4009215767</span>
                </li>
            </ul>
        </div>
    </div>
</div>
<div class="wapper header">
    <h1 class="common-icon"><a href="javascript:;">1号药城 — 方便.快捷.第一</a></h1>
</div>
<div class="wapper store-page clearfix">
    <div class="pay_error">
        <p><img src="images/pay_error.jpg" /></p>
        <p class="text">支付服务异常，请您稍后重试！</p>
        <p><button>返回</button></p>
    </div>
</div>

<div class="footer main-footer">
    <div class="wapper">
        <div class="pb30 clearfix">
            <div class="slogan fl">
                <ul>
                    <li>
                        <i class="common-icon fi01"></i>
                        <div class="pl70">
                            <h3>入驻指南</h3>
                            <p>如何注册</p>
                            <p>如何完善资料</p>
                        </div>
                    </li>
                    <li>
                        <i class="common-icon fi02"></i>
                        <div class="pl70">
                            <h3>购买指南</h3>
                            <p>如何加入渠道</p>
                            <p>购物流程</p>
                        </div>
                    </li>
                    <li>
                        <i class="common-icon fi03"></i>
                        <div class="pl70">
                            <h3>支付流程</h3>
                            <p>在线支付</p>
                            <p>账期支付</p>
                            <p>线下转账</p>
                        </div>
                    </li>
                    <li>
                        <i class="common-icon fi04"></i>
                        <div class="pl70">
                            <h3>订单流程</h3>
                            <p>补货流程</p>
                            <p>如何拒收</p>
                            <p>退换货流程</p>
                        </div>
                    </li>
                </ul>
            </div>
            <div class="fr">
                <!-- <i class="common-icon fi-phone"></i> -->
                <span class="red f18">客服热线：4009215767</span>
                <p class="pt10">服务时间：9:00-18:00</p>
            </div>
        </div>
    </div>
</div>
<div class="copyright">
    <div class="wapper tc">
        <div class="footer-nav">
            <p>
                <a href="javascript:;" class="bl0">关于我们</a>
                <a href="javascript:;">联系我们</a>
                <a href="javascript:;">客服中心</a>
                <a href="javascript:;">意见反馈</a>
                <a href="javascript:;">广告服务</a>
                <a href="javascript:;">免责声明</a>
                <a href="javascript:;">诚聘英才</a>
                <a href="javascript:;">网站地图</a>
            </p>
            <p>
                <a href="javascript:;" class="bl0">增值电信信息服务业务经营许可证</a>
                <a href="javascript:;">互联网药品信息服务资格证</a>
                <a href="javascript:;">互联网药品交易服务资格证书</a>
            </p>
        </div>
        <p>监管机构：国家食品药品监督管理局　湖北省食品药品监督管理局　武汉市食品药品监督管理局</p>
        <p>Copyright(C) 2015-2016　华中药品交易有限公司版权所有　网站备案号:鄂ICP备15017367号-1</p>
    </div>
</div>
<!-- 右侧浮动框 -->
<div class="side-bar tc">
    <div class="side-bar-height">
        <div class="side-bar-ie8">
            <div class="side-bar-ie8hack">
                <a href="javascript:;">
                    <p><i class="common-icon purchase-icon"></i></p>
                    <p>进</p>
                    <p>货</p>
                    <p>单</p>
                    <p><i class="common-icon su-sum sb-sum">3</i></p>
                </a>
                <!--
                          <div class="pt40">
                            <a href="javascript:;" >
                              <p><i class="common-icon sb-info"></i></p>
                              <p>消</p>
                              <p>息</p>
                              <p><i class="common-icon su-sum sb-sum">3</i></p>
                            </a>
                          </div>
                 -->
                <div class="side-bar-con">
                    <div class="common-icon sbc-qq">
                        <div class="common-icon sbc-qq-details none">
                            <p>客服热线：4009215767</p>
                            <ul>
                                <li>
                                    <a href="javascript:;">
                                        <i class="common-icon qq-icon"></i>
                                        客服春晓
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:;">
                                        <i class="common-icon qq-icon"></i>
                                        客服春晓
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:;">
                                        <i class="common-icon qq-icon"></i>
                                        客服春晓
                                    </a>
                                </li>
                                <li>
                                    <a href="javascript:;">
                                        <i class="common-icon qq-icon"></i>
                                        客服春晓
                                    </a>
                                </li>
                            </ul>
                            <p>服务时间：周一至周五  09:00 - 18:00</p>
                        </div>
                    </div>
                    <div class="common-icon sbc-contact">
                        <div class="common-icon sbc-c-details none">
                            <p>用户反馈</p>
                            <form>
                                <textarea></textarea>
                                <p>
                                    <button class="sbt-c-btn">提交</button>
                                    <button class="sbt-c-btn gray">取消</button>
                                </p>
                            </form>
                        </div>
                    </div>
                    <div class="common-icon sbc-barcode">
                        <div class="common-icon sbc-b-details none">
                            <p><img src="images/main_barcode.gif"></p>
                            <p>1号药城APP</p>
                            <p><img src="images/main_barcode_wx.gif"></p>
                            <p>1号药城 微信公众号</p>
                        </div>
                    </div>
                </div>
                <a href="javascript:;" class="common-icon top-btn"></a>
            </div>
        </div>
    </div>
</div>
</body>
</html>
