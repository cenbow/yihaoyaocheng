/**
 * Created By: XI
 * Created On: 2016-7-27 20:21:50
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.mapper;

import java.util.List;

import com.yyw.yhyc.order.bo.SystemPayType;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface SystemPayTypeMapper extends GenericIBatisMapper<SystemPayType, Integer> {

	public List<SystemPayType> listPaginationByProperty(Pagination<SystemPayType> pagination, SystemPayType systemPayType);
}
