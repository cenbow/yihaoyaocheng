package com.yyw.yhyc.pay.impl;

import com.yyw.yhyc.pay.interfaces.PayService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service("chinaPayService")
public class ChinaPayServiceImpl implements PayService {

    @Override
    public Map<String, Object> postToBankForDoneOrder(Map<String, Object> orderInfo, String Action) {
        return null;
    }
}
