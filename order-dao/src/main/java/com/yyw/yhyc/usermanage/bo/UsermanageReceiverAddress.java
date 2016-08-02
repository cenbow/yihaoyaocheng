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
package com.yyw.yhyc.usermanage.bo;

import com.yyw.yhyc.bo.Model;

public class UsermanageReceiverAddress extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	主键
	  */
	private Integer id;

	/**
	  *	企业编号
	  */
	private String enterpriseId;

	/**
	  *	收货人
	  */
	private String receiverName;

	/**
	  *	省编码
	  */
	private String provinceCode;

	/**
	  *	市编码
	  */
	private String cityCode;

	/**
	  *	区编码
	  */
	private String districtCode;

	/**
	  *	省名称
	  */
	private String provinceName;

	/**
	  *	市名称
	  */
	private String cityName;

	/**
	  *	区名称
	  */
	private String districtName;

	/**
	  *	地址
	  */
	private String address;

	/**
	  *	联系电话
	  */
	private String contactPhone;

	/**
	  *	0:非默认地址；1:默认地址。
	  */
	private Byte defaultAddress;

	/**
	  *	创建人
	  */
	private String createUser;

	/**
	  *	创建时间
	  */
	private String createTime;

	/**
	  *	更新人
	  */
	private String updateUser;

	/**
	  *	更新时间
	  */
	private String updateTime;

	/**
	  *	主键
	  */
	public Integer getId()
	{
		return id;
	}
	
	/**
	  *	主键
	  */
	public void setId(Integer id)
	{
		this.id = id;
	}
	
	/**
	  *	企业编号
	  */
	public String getEnterpriseId()
	{
		return enterpriseId;
	}
	
	/**
	  *	企业编号
	  */
	public void setEnterpriseId(String enterpriseId)
	{
		this.enterpriseId = enterpriseId;
	}
	
	/**
	  *	收货人
	  */
	public String getReceiverName()
	{
		return receiverName;
	}
	
	/**
	  *	收货人
	  */
	public void setReceiverName(String receiverName)
	{
		this.receiverName = receiverName;
	}
	
	/**
	  *	省编码
	  */
	public String getProvinceCode()
	{
		return provinceCode;
	}
	
	/**
	  *	省编码
	  */
	public void setProvinceCode(String provinceCode)
	{
		this.provinceCode = provinceCode;
	}
	
	/**
	  *	市编码
	  */
	public String getCityCode()
	{
		return cityCode;
	}
	
	/**
	  *	市编码
	  */
	public void setCityCode(String cityCode)
	{
		this.cityCode = cityCode;
	}
	
	/**
	  *	区编码
	  */
	public String getDistrictCode()
	{
		return districtCode;
	}
	
	/**
	  *	区编码
	  */
	public void setDistrictCode(String districtCode)
	{
		this.districtCode = districtCode;
	}
	
	/**
	  *	省名称
	  */
	public String getProvinceName()
	{
		return provinceName;
	}
	
	/**
	  *	省名称
	  */
	public void setProvinceName(String provinceName)
	{
		this.provinceName = provinceName;
	}
	
	/**
	  *	市名称
	  */
	public String getCityName()
	{
		return cityName;
	}
	
	/**
	  *	市名称
	  */
	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}
	
	/**
	  *	区名称
	  */
	public String getDistrictName()
	{
		return districtName;
	}
	
	/**
	  *	区名称
	  */
	public void setDistrictName(String districtName)
	{
		this.districtName = districtName;
	}
	
	/**
	  *	地址
	  */
	public String getAddress()
	{
		return address;
	}
	
	/**
	  *	地址
	  */
	public void setAddress(String address)
	{
		this.address = address;
	}
	
	/**
	  *	联系电话
	  */
	public String getContactPhone()
	{
		return contactPhone;
	}
	
	/**
	  *	联系电话
	  */
	public void setContactPhone(String contactPhone)
	{
		this.contactPhone = contactPhone;
	}
	
	/**
	  *	0:非默认地址；1:默认地址。
	  */
	public Byte getDefaultAddress()
	{
		return defaultAddress;
	}
	
	/**
	  *	0:非默认地址；1:默认地址。
	  */
	public void setDefaultAddress(Byte defaultAddress)
	{
		this.defaultAddress = defaultAddress;
	}
	
	/**
	  *	创建人
	  */
	public String getCreateUser()
	{
		return createUser;
	}
	
	/**
	  *	创建人
	  */
	public void setCreateUser(String createUser)
	{
		this.createUser = createUser;
	}
	
	/**
	  *	创建时间
	  */
	public String getCreateTime()
	{
		return createTime;
	}
	
	/**
	  *	创建时间
	  */
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	
	/**
	  *	更新人
	  */
	public String getUpdateUser()
	{
		return updateUser;
	}
	
	/**
	  *	更新人
	  */
	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}
	
	/**
	  *	更新时间
	  */
	public String getUpdateTime()
	{
		return updateTime;
	}
	
	/**
	  *	更新时间
	  */
	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}
	
	public String toString()
	{
		return "UsermanageReceiverAddress [" + 
					"id=" + id + 
					", enterpriseId=" + enterpriseId + 
					", receiverName=" + receiverName + 
					", provinceCode=" + provinceCode + 
					", cityCode=" + cityCode + 
					", districtCode=" + districtCode + 
					", provinceName=" + provinceName + 
					", cityName=" + cityName + 
					", districtName=" + districtName + 
					", address=" + address + 
					", contactPhone=" + contactPhone + 
					", defaultAddress=" + defaultAddress + 
					", createUser=" + createUser + 
					", createTime=" + createTime + 
					", updateUser=" + updateUser + 
					", updateTime=" + updateTime + 
				"]";
	}
}

