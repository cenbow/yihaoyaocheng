package com.yyw.yhyc.order.bo;

/**
 * Created by lizhou on 2016/7/29
 */
public class CommonType {

    /* 在线支付订单的订单编号前缀 */
    public static final String ORDER_ONLINE_PAY_PREFIX = "ZXD";

    /* 线下支付订单的订单编号前缀 */
    public static final String ORDER_OFFLINE_PAY_PREFIX = "XXD";

    /* 账期支付订单的订单编号前缀 */
    public static final String ORDER_PERIOD_TERM_PAY_PREFIX = "ZQD";

    /* 订单支付剩余时间 (小时)*/
    public static final int PAY_TIME = 24;

    /* 买家发货系统自动确认收货时间 (天)*/
    public static final int AUTO_RECEIVE_TIME = 7;

    /* 订单支付流水编号前缀( t_order_pay表中的pay_flow_id字段 ) */
    public static final String ORDER_PAY_FLOW_ID_PREFIX = "PF";

    /*每次延期的天数*/
    public static final int POSTPONE_TIME = 3 ;

    /*能延期次数*/
    public static final int CAN_DELAY_TIME = 2;

}
