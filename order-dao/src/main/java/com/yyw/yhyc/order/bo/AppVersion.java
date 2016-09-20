/**
 * Created By: XI
 * Created On: 2016-9-20 17:02:42
 *
 * Amendment History:
 * 
 * Amended By       Amended On      Amendment Description
 * ------------     -----------     ---------------------------------------------
 *
 **/
package com.yyw.yhyc.order.bo;

import com.yyw.yhyc.bo.Model;

public class AppVersion extends Model{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	/**
	  *	主键
	  */
	private Integer versionId;

	/**
	  *	编码
	  */
	private String versionCode;

	/**
	  *	类型 1-Android;2-IOS;
	  */
	private String versionType;

	/**
	  *	路径
	  */
	private String versionPath;

	/**
	  *	状态 1-启用;0-关闭; 默认1
	  */
	private String versionStatus;

	/**
	  *	更新 1->强制更新; 0->不强制
	  */
	private String updateFag;

	/**
	  *	创建人
	  */
	private String creator;

	/**
	  *	创建时间
	  */
	private String createTime;

	/**
	  *	修改人
	  */
	private String updateUser;

	/**
	  *	修改时间
	  */
	private String updateTime;

	/**
	  *	备注
	  */
	private String remark;

	/**
	  *	主键
	  */
	public Integer getVersionId()
	{
		return versionId;
	}
	
	/**
	  *	主键
	  */
	public void setVersionId(Integer versionId)
	{
		this.versionId = versionId;
	}
	
	/**
	  *	编码
	  */
	public String getVersionCode()
	{
		return versionCode;
	}
	
	/**
	  *	编码
	  */
	public void setVersionCode(String versionCode)
	{
		this.versionCode = versionCode;
	}
	
	/**
	  *	类型 1-Android;2-IOS;
	  */
	public String getVersionType()
	{
		return versionType;
	}
	
	/**
	  *	类型 1-Android;2-IOS;
	  */
	public void setVersionType(String versionType)
	{
		this.versionType = versionType;
	}
	
	/**
	  *	路径
	  */
	public String getVersionPath()
	{
		return versionPath;
	}
	
	/**
	  *	路径
	  */
	public void setVersionPath(String versionPath)
	{
		this.versionPath = versionPath;
	}
	
	/**
	  *	状态 1-启用;0-关闭; 默认1
	  */
	public String getVersionStatus()
	{
		return versionStatus;
	}
	
	/**
	  *	状态 1-启用;0-关闭; 默认1
	  */
	public void setVersionStatus(String versionStatus)
	{
		this.versionStatus = versionStatus;
	}
	
	/**
	  *	更新 1->强制更新; 0->不强制
	  */
	public String getUpdateFag()
	{
		return updateFag;
	}
	
	/**
	  *	更新 1->强制更新; 0->不强制
	  */
	public void setUpdateFag(String updateFag)
	{
		this.updateFag = updateFag;
	}
	
	/**
	  *	创建人
	  */
	public String getCreator()
	{
		return creator;
	}
	
	/**
	  *	创建人
	  */
	public void setCreator(String creator)
	{
		this.creator = creator;
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
	  *	修改人
	  */
	public String getUpdateUser()
	{
		return updateUser;
	}
	
	/**
	  *	修改人
	  */
	public void setUpdateUser(String updateUser)
	{
		this.updateUser = updateUser;
	}
	
	/**
	  *	修改时间
	  */
	public String getUpdateTime()
	{
		return updateTime;
	}
	
	/**
	  *	修改时间
	  */
	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}
	
	/**
	  *	备注
	  */
	public String getRemark()
	{
		return remark;
	}
	
	/**
	  *	备注
	  */
	public void setRemark(String remark)
	{
		this.remark = remark;
	}
	
	public String toString()
	{
		return "AppVersion [" + 
					"versionId=" + versionId + 
					", versionCode=" + versionCode + 
					", versionType=" + versionType + 
					", versionPath=" + versionPath + 
					", versionStatus=" + versionStatus + 
					", updateFag=" + updateFag + 
					", creator=" + creator + 
					", createTime=" + createTime + 
					", updateUser=" + updateUser + 
					", updateTime=" + updateTime + 
					", remark=" + remark + 
				"]";
	}
}

