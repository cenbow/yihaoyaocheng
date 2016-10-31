package com.yyw.yhyc.pay.facade.impl;

import com.yyw.yhyc.pay.facade.AlipayFacade;
import com.yyw.yhyc.pay.impl.AlipayServiceImpl;
import com.yyw.yhyc.pay.interfaces.PayService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bigwing on 2016/10/26.
 */
public class AlipayFacadeImpl implements AlipayFacade {

    private static final Logger logger = LoggerFactory.getLogger(AlipayFacadeImpl.class);

    @Autowired
    private AlipayServiceImpl alipayService;

    @Override
    public String alipayCommit(String out_trade_no, String subject, String total_fee, String body) {
      return alipayService.alipayCommit(out_trade_no,subject,total_fee,body);

    }

    @Override
    public String alipayrefundFastpay(int batch_num, String trade_no, String total_fee, String reason) {
        Map<Integer, String> refundMap = new HashMap<>();
        refundMap.put(1, trade_no + "^" + total_fee + "^" + reason);
        return alipayrefundFastpayByMap(batch_num, refundMap);
    }

    @Override
    public String alipayrefundFastpayByMap(int batch_num, Map<Integer, String> refundMap) {
        return alipayService.alipayrefundFastpayByMap(batch_num,refundMap);
    }

    public static void main(String[] args) {
        AlipayFacadeImpl af = new AlipayFacadeImpl();
        af.alipayCommit("test20161029174446","test商品123","0.01","即时到账测试");
        System.out.println(StringUtils.repeat("=", 40));
        af.alipayrefundFastpay(1,"2011011201037066","0.01","协商退款");
        Map<Integer, String> refundMap = new HashMap<>();
        refundMap.put(1, "2011011201037066^0.01^协商退款");
        refundMap.put(2, "2011011201037068^0.01^协商退款");
        af.alipayrefundFastpayByMap(2,refundMap);
    }
}
