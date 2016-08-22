<%--
  Created by IntelliJ IDEA.
  User: lizhou
  Date: 2016/8/20
  Time: 7:01
  To change this template use File | Settings | Editor | File and Code Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>进货单页-1号药城</title>
    <meta name="Keywords" content="">
    <meta name="description" content="">
    <link rel="stylesheet" href="${STATIC_URL}/static/css/cart.css">
<body>
<div class="top">
    <div class="wapper">
        <div class="top-side">
            <ul>
                <li>
                    您好，欢迎您来到1号药城药品信息网
                    <!-- <a href="#" class="pl25">我的名字啦</a>
                  <a href="#">退出</a>
                  -->
                    <a href="#" class="red pl25">请登录</a>
                </li>
                <li>
                    <a href="#">我的订单</a>
                </li>
                <li>
                    <a href="#">我的1号药城</a>
                </li>
                <li>
                    <a href="#">帮助</a>
                </li>
                <li class="last-li"><i class="common-icon top-phone"></i>
                    <span class="red">4009215767</span>
                </li>
            </ul>
        </div>
    </div>
</div>
<div class="wapper header">
    <h1 class="common-icon">
        <a href="#">1号药城 — 方便.快捷.第一</a>
    </h1>
    <div class="stepflex">
        <dl class="done">
            <dt class="common-icon">1</dt>
            <dd>我的进货单</dd>
        </dl>
        <dl>
            <dt class="common-icon">2</dt>
            <dd>确认订单信息</dd>
        </dl>
        <dl>
            <dt class="common-icon">3</dt>
            <dd>成功提交订单</dd>
        </dl>
    </div>
</div>
<div class="wapper shopping-cart">
    <div class="cart-table-th">
        <div class="wp">
            <div class="th th-chk">
                <div class="cart-checkbox">
                    <span class="inside-icon">全选所有商品</span>
                    全选
                </div>
            </div>
            <div class="th th-item">商品</div>
            <div class="th th-price">单价</div>
            <div class="th th-amount">数量</div>
            <div class="th th-sum">小计</div>
            <div class="th th-op">操作</div>
        </div>
    </div>
    <div class="order-holder">
        <div class="holder-top">
            <div class="cart-checkbox select-all">
                <span class="inside-icon">全选所有商品</span>
            </div>
            <div class="mark-supplier">供应商：扬州中润医药有限公司</div>
            <a class="lts-shop-icon f12" href="javascript:;">进入店铺</a>
            <p>
                此供应商订单起售价为
                <span class="from-price">100</span>
                元，您目前已购买
                <span class="buy-price">80</span>
                元，还差
                <span class="need-price">20</span>
                元 !
            </p>
        </div>
        <div class="holder-list">
            <ul>
                <li class="fl td-chk">
                    <div class="cart-checkbox select-all">
                        <span class="inside-icon">全选所有商品</span>
                    </div>
                </li>
                <li class="fl td-pic">
                    <img src="images/pro_img.jpg" alt="汤臣倍健鱼油软胶囊 100g"></li>
                <li class="fl td-item">
                    <p class="item-title">汤臣倍健鱼油软胶囊 100g</p>
                    <p>汤臣倍健医药保健有限公司</p>
                </li>
                <li class="fl td-price">
                    ¥
                    <span>2.50</span>
                </li>
                <li class="fl td-amount">
                    <div class="it-sort-col5 clearfix pr">
                        <div class="clearfix">
                            <div class="its-choose-amount fl">
                                <div class="its-input">
                                    <a href="javascript:;" class="its-btn-reduce">-</a>
                                    <a href="javascript:;" class="its-btn-add">+</a>
                                    <input value="10" class="its-buy-num"></div>
                            </div>
                            <div class="pt13 pl20 fl">瓶</div>
                        </div>
                        <p class="color-gray9">2盒起售 限购5盒</p>
                        <div class="pr-list-tips-frame tc">
                            <i class="common-icon pl-frame-icon"></i>
                            <p>最小拆零包装：4瓶</p>
                        </div>
                    </div>
                </li>
                <li class="fl td-sum">
                    ¥
                    <span>25.00</span>
                </li>
                <li class="fl td-op"><a href="javascript:;" class="btn-delete">删除</a></li>
            </ul>
        </div>
        <div class="holder-list">
            <ul>
                <li class="fl td-chk">
                    <div class="cart-checkbox select-all">
                        <span class="inside-icon">全选所有商品</span>
                    </div>
                </li>
                <li class="fl td-pic">
                    <img src="images/pro_img.jpg" alt="汤臣倍健鱼油软胶囊 100g"></li>
                <li class="fl td-item">
                    <p class="item-title">汤臣倍健鱼油软胶囊 100g</p>
                    <p>汤臣倍健医药保健有限公司</p>
                </li>
                <li class="fl td-price">
                    ¥
                    <span>2.50</span>
                </li>
                <li class="fl td-amount">
                    <div class="it-sort-col5 clearfix pr">
                        <div class="clearfix">
                            <div class="its-choose-amount fl">
                                <div class="its-input">
                                    <a href="javascript:;" class="its-btn-reduce">-</a>
                                    <a href="javascript:;" class="its-btn-add">+</a>
                                    <input value="10" class="its-buy-num"></div>
                            </div>
                            <div class="pt13 pl20 fl">瓶</div>
                        </div>
                        <p class="color-gray9">2盒起售 限购5盒</p>
                        <div class="pr-list-tips-frame tc">
                            <i class="common-icon pl-frame-icon"></i>
                            <p>库存不足，这次少买点吧！</p>
                        </div>
                    </div>
                </li>
                <li class="fl td-sum">
                    ¥
                    <span>25.00</span>
                </li>
                <li class="fl td-op"><a href="javascript:;" class="btn-delete">删除</a></li>
            </ul>
        </div>
    </div>
    <div class="order-holder">
        <div class="holder-top">
            <div class="cart-checkbox">
                <span class="inside-icon">全选所有商品</span>
            </div>
            <div class="mark-supplier">供应商：扬州中润医药有限公司</div>
            <a class="lts-shop-icon f12" href="javascript:;">进入店铺</a>
        </div>
        <div class="holder-list">
            <ul>
                <li class="fl td-chk">
                    <div class="cart-checkbox">
                        <span class="inside-icon">全选所有商品</span>
                    </div>
                </li>
                <li class="fl td-pic">
                    <img src="images/pro_img.jpg" alt="汤臣倍健鱼油软胶囊 100g"></li>
                <li class="fl td-item">
                    <p class="item-title">汤臣倍健鱼油软胶囊 100g</p>
                    <p>汤臣倍健医药保健有限公司</p>
                </li>
                <li class="fl td-price">
                    ¥
                    <span>2.50</span>
                </li>
                <li class="fl td-amount">
                    <div class="it-sort-col5  clearfix pr">
                        <div class="clearfix">
                            <div class="its-choose-amount fl">
                                <div class="its-input">
                                    <a href="javascript:;" class="its-btn-reduce">-</a>
                                    <a href="javascript:;" class="its-btn-add">+</a>
                                    <input value="10" class="its-buy-num"></div>
                            </div>
                            <div class="pt13 pl20 fl">瓶</div>
                        </div>
                        <p class="color-gray9">2盒起售 限购5盒</p>
                    </div>
                </li>
                <li class="fl td-sum">
                    ¥
                    <span>25.00</span>
                </li>
                <li class="fl td-op"><a href="javascript:;" class="btn-delete">删除</a></li>
            </ul>
        </div>
        <div class="holder-list">
            <ul>
                <li class="fl td-chk">
                    <div class="cart-checkbox">
                        <span class="inside-icon">全选所有商品</span>
                    </div>
                </li>
                <li class="fl td-pic">
                    <img src="images/pro_img.jpg" alt="汤臣倍健鱼油软胶囊 100g"></li>
                <li class="fl td-item">
                    <p class="item-title">汤臣倍健鱼油软胶囊 100g</p>
                    <p>汤臣倍健医药保健有限公司</p>
                </li>
                <li class="fl td-price">
                    ¥
                    <span>2.50</span>
                </li>
                <li class="fl td-amount">
                    <div class="it-sort-col5 clearfix pr">
                        <div class="clearfix">
                            <div class="its-choose-amount fl">
                                <div class="its-input">
                                    <a href="javascript:;" class="its-btn-reduce">-</a>
                                    <a href="javascript:;" class="its-btn-add">+</a>
                                    <input value="10" class="its-buy-num"></div>
                            </div>
                            <div class="pt13 pl20 fl">瓶</div>
                        </div>
                        <p class="color-gray9">2盒起售 限购5盒</p>
                    </div>
                </li>
                <li class="fl td-sum">
                    ¥
                    <span>25.00</span>
                </li>
                <li class="fl td-op"><a href="javascript:;" class="btn-delete">删除</a></li>
            </ul>
        </div>
        <div class="holder-list no-stock">
            <ul>
                <li class="fl td-chk">
                    <div class="cart-checkbox checkbox-disable">
                        <span class="inside-icon">全选所有商品</span>
                    </div>
                </li>
                <li class="fl td-pic">
                    <span class="inside-icon">缺货</span>
                    <img src="images/pro_img.jpg" alt="汤臣倍健鱼油软胶囊 100g"></li>
                <li class="fl td-item">
                    <p class="item-title">汤臣倍健鱼油软胶囊 100g</p>
                    <p>汤臣倍健医药保健有限公司</p>
                </li>
                <li class="fl td-price">
                    ¥
                    <span>2.50</span>
                </li>
                <li class="fl td-amount">
                    <div class="it-sort-col5 clearfix pr">
                        <div class="clearfix">
                            <div class="its-choose-amount fl">
                                <div class="its-input">
                                    <a href="javascript:;" class="its-btn-reduce">-</a>
                                    <a href="javascript:;" class="its-btn-add">+</a>
                                    <input value="10" class="its-buy-num"></div>
                            </div>
                            <div class="pt13 pl20 fl">瓶</div>
                        </div>
                        <p class="color-gray9">2盒起售 限购5盒</p>
                    </div>
                </li>
                <li class="fl td-sum">
                    ¥
                    <span>25.00</span>
                </li>
                <li class="fl td-op"><a href="javascript:;" class="btn-delete">删除</a></li>
            </ul>
        </div>
        <div class="holder-list no-stock">
            <ul>
                <li class="fl td-chk">
                    <div class="cart-checkbox checkbox-disable">
                        <span class="inside-icon">全选所有商品</span>
                    </div>
                </li>
                <li class="fl td-pic">
                    <span class="inside-icon">下架</span>
                    <img src="images/pro_img.jpg" alt="汤臣倍健鱼油软胶囊 100g"></li>
                <li class="fl td-item">
                    <p class="item-title">汤臣倍健鱼油软胶囊 100g</p>
                    <p>汤臣倍健医药保健有限公司</p>
                </li>
                <li class="fl td-price">
                    ¥
                    <span>2.50</span>
                </li>
                <li class="fl td-amount">
                    <div class="it-sort-col5 clearfix pr">
                        <div class="clearfix">
                            <div class="its-choose-amount fl">
                                <div class="its-input">
                                    <a href="javascript:;" class="its-btn-reduce">-</a>
                                    <a href="javascript:;" class="its-btn-add">+</a>
                                    <input value="10" class="its-buy-num"></div>
                            </div>
                            <div class="pt13 pl20 fl">瓶</div>
                        </div>
                        <p class="color-gray9">2盒起售 限购5盒</p>
                    </div>
                </li>
                <li class="fl td-sum">
                    ¥
                    <span>25.00</span>
                </li>
                <li class="fl td-op"><a href="javascript:;" class="btn-delete btn-notice">删除</a></li>
            </ul>
        </div>
    </div>
    <div class="order-bottom">
        <div class="operation">
            <div class="th-chk">
                <div class="cart-checkbox">
                    <span class="inside-icon">全选所有商品</span>
                    全选
                </div>
            </div>
            <a class="delete-items">删除选中商品</a>
            <a class="clear-items">清除失效商品</a>
        </div>
        <div class="oper-info">
            <div class="total-price">
                商品总额： <em>¥</em>
                <span>59.00</span>
            </div>
            <div class="total-item">
                品种总计：
                <span>6</span>
            </div>
        </div>
        <div class="btn">
            <a class="os-btn-pay tc" href="javascript:;">立即结算</a>
            <a class="os-btn-order tc" href="index.html">继续采购</a>
        </div>
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
<script type="text/javascript" src="${STATIC_URL}/static/js/jquery-1.12.1.min.js"></script>
<script src="${STATIC_URL}/static/js/shoppingCart/common.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/inside.js"></script>
<script type="text/javascript" src="${STATIC_URL}/static/js/shoppingCart/dialog.js"></script>
</body>
</html>