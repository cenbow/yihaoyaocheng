package com.yyw.yhyc.pay.facade;

/**
 * Created by bigwing on 2016/10/25.
 */
public interface AlipayFacade {

    //支付
    public void alipayCommit();

    //退款
    public void alipayTranspay();
}
