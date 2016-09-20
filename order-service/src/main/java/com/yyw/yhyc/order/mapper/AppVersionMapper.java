/**
 *
 * Amendment History:
 *
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.mapper;


import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.mapper.GenericIBatisMapper;
import com.yyw.yhyc.order.bo.AppVersion;

import java.util.List;

public interface AppVersionMapper extends GenericIBatisMapper<AppVersion, Long> {

	public List<AppVersion> listPaginationByProperty(Pagination<AppVersion> pagination, AppVersion appVersion);
}
