package com.yyw.yhyc.order.service;

import com.yyw.yhyc.order.mapper.SystemDateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by shiyongxi on 2016/7/26.
 */
@Service
public class SystemDateService {
    @Autowired
    private SystemDateMapper systemDateMapper;

    public String getSystemDate(){
        return systemDateMapper.getSystemDate();
    }
}
