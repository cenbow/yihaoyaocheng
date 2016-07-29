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

    /**
     * 获取数据时间
     * @param formatter 按照指定格式获取时间
     * @return
     */
    public String getSystemDateByformatter(String formatter){
        return systemDateMapper.getSystemDateByformatter(formatter);
    }
}
