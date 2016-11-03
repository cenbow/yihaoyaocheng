package com.yyw.yhyc.order.mapper;

import java.util.Map;

import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.order.bo.CheckFileCollect;


public interface CheckFileCollectMapper  extends GenericIBatisMapper<CheckFileCollect, Integer> {
	
	public int insertCheckFileCollect(CheckFileCollect checkFileCollect);
}
