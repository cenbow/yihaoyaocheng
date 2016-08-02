/**
 * Created By: XI
 * Created On: 2016-8-2 15:49:20
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.usermanage.mapper;

import java.util.List;

import com.yyw.yhyc.usermanage.bo.UsermanageReceiverAddress;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.bo.Pagination;

public interface UsermanageReceiverAddressMapper extends GenericIBatisMapper<UsermanageReceiverAddress, Integer> {

	public List<UsermanageReceiverAddress> listPaginationByProperty(Pagination<UsermanageReceiverAddress> pagination, UsermanageReceiverAddress usermanageReceiverAddress);
}
