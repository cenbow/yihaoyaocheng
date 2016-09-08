package com.yyw.yhyc.order.facade.impl;

import com.yyw.yhyc.order.facade.SystemDateFacade;
import com.yyw.yhyc.order.service.SystemDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shiyongxi on 2016/7/26.
 */
@Service("systemDateFacade")
public class SystemDateFacadeImpl implements SystemDateFacade {
    @Autowired
    SystemDateService systemDateService;

    public String getSystemDate() {
        return systemDateService.getSystemDate();
    }
}
