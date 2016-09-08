package com.yyw.yhyc.order.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * Created by shiyongxi on 2016/7/26.
 */
public interface SystemDateMapper {
    String getSystemDate();
    String getSystemDateByformatter(@Param("formatter") String formatter);
}
