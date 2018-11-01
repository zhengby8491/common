/**
 * <pre>
 * Title: 		Passport.java
 * Author:		linriqing
 * Create:	 	2007-6-6 下午05:59:07
 * Copyright: 	Copyright (c) 2007
 * Company:
 * <pre>
 */
package com.huayin.common.acl.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * <pre>
 * 通行证
 * </pre>
 * @author linriqing
 * @version 1.0, 2007-6-6
 */
public class Passport implements Serializable
{
	/**
	 * <pre>
	 * 通行证所持有的令牌
	 * </pre>
	 * @author linriqing
	 * @version 1.0, 2010-7-29
	 */
	public enum Token
	{
		/**
		 * 管理
		 */
		MANAGE("管理"),
		/**
		 * 生产
		 */
		PRODUCT("生产"),
		/**
		 * 只读
		 */
		SLAVE("只读");

		private String text;

		Token(String text)
		{
			this.text = text;
		}

		public String getText()
		{
			return text;
		}

		public void setText(String text)
		{
			this.text = text;
		}
	}

	private static final long serialVersionUID = -8044332067926907817L;

	/**
	 * 组名
	 */
	private String groupName;

	/**
	 * 用户组对应值
	 */
	private Object groupValue;

	/**
	 * 最后一次登录时间
	 */
	private Date lastLogin;

	/**
	 * 昵称
	 */
	private String nickName;

	/**
	 * 权限集合
	 */
	private Set<Permission> permissions = new HashSet<Permission>(0);

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 组名
	 */
	private String roleName;

	/**
	 * 密钥
	 */
	private String secretKey;

	/**
	 * 编号
	 */
	private String serialNo;

	/**
	 * 客户端令牌
	 */
	private Token token = Token.PRODUCT;

	/**
	 * 用户名
	 */
	private String userId;
	
	/**
	 * 所属代理商编号
	 */
	private String counterId;
	
	/**
	 * 所属会员店编号(会员店用)
	 */
	private String storeId;
	
	/**
	 * 306所属商户编号（306专用）
	 */
	private String merchantId;
	
	/**
	 * 构造函数
	 */
	public Passport()
	{
		super();
	}

	public String getGroupName()
	{
		return groupName;
	}

	public Object getGroupValue()
	{
		return groupValue;
	}

	/**
	 * @return 最后一次登录时间
	 */
	public Date getLastLogin()
	{
		return lastLogin;
	}

	/**
	 * @return 昵称
	 */
	public String getNickName()
	{
		return nickName;
	}

	/**
	 * @return 权限集合
	 */
	public Set<Permission> getPermissions()
	{
		return permissions;
	}

	/**
	 * @return 真实姓名
	 */
	public String getRealName()
	{
		return realName;
	}

	public String getRoleName()
	{
		return roleName;
	}

	/**
	 * @return 密钥
	 */
	public String getSecretKey()
	{
		return secretKey;
	}

	/**
	 * @return 编号
	 */
	public String getSerialNo()
	{
		return serialNo;
	}

	/**
	 * @return 客户端令牌
	 */
	public Token getToken()
	{
		return token;
	}

	/**
	 * @return 用户名
	 */
	public String getUserId()
	{
		return userId;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}

	public void setGroupValue(Object groupValue)
	{
		this.groupValue = groupValue;
	}

	/**
	 * @param lastLogin 最后一次登录时间
	 */
	public void setLastLogin(Date lastLogin)
	{
		this.lastLogin = lastLogin;
	}

	/**
	 * @param nickName 昵称
	 */
	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	/**
	 * @param permissions 权限集合
	 */
	public void setPermissions(Set<Permission> permissions)
	{
		this.permissions = permissions;
	}

	/**
	 * @param realName 真实姓名
	 */
	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	/**
	 * @param secretKey 密钥
	 */
	public void setSecretKey(String secretKey)
	{
		this.secretKey = secretKey;
	}

	/**
	 * @param serialNo 编号
	 */
	public void setSerialNo(String serialNo)
	{
		this.serialNo = serialNo;
	}

	/**
	 * @param token 客户端令牌
	 */
	public void setToken(Token token)
	{
		this.token = token;
	}

	/**
	 * @param userId 用户名
	 */
	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getCounterId()
	{
		return counterId;
	}

	public void setCounterId(String counterId)
	{
		this.counterId = counterId;
	}

	public String getStoreId()
	{
		return storeId;
	}

	public void setStoreId(String storeId)
	{
		this.storeId = storeId;
	}

	public String getMerchantId()
	{
		return merchantId;
	}

	public void setMerchantId(String merchantId)
	{
		this.merchantId = merchantId;
	}
	
}
