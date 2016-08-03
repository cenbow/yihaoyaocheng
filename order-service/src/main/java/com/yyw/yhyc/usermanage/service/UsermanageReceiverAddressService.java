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
package com.yyw.yhyc.usermanage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyw.yhyc.usermanage.bo.UsermanageReceiverAddress;
import com.yyw.yhyc.bo.Pagination;
import com.yyw.yhyc.usermanage.mapper.UsermanageReceiverAddressMapper;

@Service("usermanageReceiverAddressService")
public class UsermanageReceiverAddressService {

	private UsermanageReceiverAddressMapper	usermanageReceiverAddressMapper;

	@Autowired
	public void setUsermanageReceiverAddressMapper(UsermanageReceiverAddressMapper usermanageReceiverAddressMapper)
	{
		this.usermanageReceiverAddressMapper = usermanageReceiverAddressMapper;
	}
	
	/**
	 * 通过主键查询实体对象
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public UsermanageReceiverAddress getByPK(Integer primaryKey) throws Exception
	{
		return usermanageReceiverAddressMapper.getByPK(primaryKey);
	}

	/**
	 * 查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<UsermanageReceiverAddress> list() throws Exception
	{
		return usermanageReceiverAddressMapper.list();
	}

	/**
	 * 根据查询条件查询所有记录
	 * @return
	 * @throws Exception
	 */
	public List<UsermanageReceiverAddress> listByProperty(UsermanageReceiverAddress usermanageReceiverAddress)
			throws Exception
	{
		return usermanageReceiverAddressMapper.listByProperty(usermanageReceiverAddress);
	}

	/**
	 * 根据查询条件查询分页记录
	 * @return
	 * @throws Exception
	 */
	public Pagination<UsermanageReceiverAddress> listPaginationByProperty(Pagination<UsermanageReceiverAddress> pagination, UsermanageReceiverAddress usermanageReceiverAddress) throws Exception
	{
		List<UsermanageReceiverAddress> list = usermanageReceiverAddressMapper.listPaginationByProperty(pagination, usermanageReceiverAddress);

		pagination.setResultList(list);

		return pagination;
	}

	/**
	 * 根据主键删除记录
	 * @param primaryKey
	 * @return
	 * @throws Exception
	 */
	public int deleteByPK(Integer primaryKey) throws Exception
	{
		return usermanageReceiverAddressMapper.deleteByPK(primaryKey);
	}

	/**
	 * 根据多个主键删除记录
	 * @param primaryKeys
	 * @throws Exception
	 */
	public void deleteByPKeys(List<Integer> primaryKeys) throws Exception
	{
		usermanageReceiverAddressMapper.deleteByPKeys(primaryKeys);
	}

	/**
	 * 根据传入参数删除记录
	 * @param usermanageReceiverAddress
	 * @return
	 * @throws Exception
	 */
	public int deleteByProperty(UsermanageReceiverAddress usermanageReceiverAddress) throws Exception
	{
		return usermanageReceiverAddressMapper.deleteByProperty(usermanageReceiverAddress);
	}

	/**
	 * 保存记录
	 * @param usermanageReceiverAddress
	 * @return
	 * @throws Exception
	 */
	public void save(UsermanageReceiverAddress usermanageReceiverAddress) throws Exception
	{
		usermanageReceiverAddressMapper.save(usermanageReceiverAddress);
	}

	/**
	 * 更新记录
	 * @param usermanageReceiverAddress
	 * @return
	 * @throws Exception
	 */
	public int update(UsermanageReceiverAddress usermanageReceiverAddress) throws Exception
	{
		return usermanageReceiverAddressMapper.update(usermanageReceiverAddress);
	}

	/**
	 * 根据条件查询记录条数
	 * @param usermanageReceiverAddress
	 * @return
	 * @throws Exception
	 */
	public int findByCount(UsermanageReceiverAddress usermanageReceiverAddress) throws Exception
	{
		return usermanageReceiverAddressMapper.findByCount(usermanageReceiverAddress);
	}
}