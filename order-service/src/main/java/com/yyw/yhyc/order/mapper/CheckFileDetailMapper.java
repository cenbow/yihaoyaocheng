package com.yyw.yhyc.order.mapper;

import com.yyw.yhyc.mapper.GenericIBatisMapper;

import com.yyw.yhyc.order.bo.CheckFileDetail;


public interface CheckFileDetailMapper  extends GenericIBatisMapper<CheckFileDetail, Integer>  {
	
	public void insertCheckFileDetail(CheckFileDetail fileDetailInfo);
}
