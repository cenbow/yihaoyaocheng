package com.yyw.yhyc.pay.impl;

import com.yyw.yhyc.pay.interfaces.PayService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


@Service("chinaPayB2cService")
public class ChinaPayServiceImpl implements PayService {

    @Override
    public Map<String, Object> postToBankForDoneOrder(Map<String, Object> orderInfo, int Action) {
        return null;
    }
}
