/**
 * <pre>
 * Title: 		DESHelper.java
 * Project: 	HappyLottery
 * Author:		linriqing
 * Create:	 	2013-3-28 下午12:41:16
 * Copyright: 	Copyright (c) 2013
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * <pre>
 * DES加密解密帮助类
 * </pre>
 * @author linriqing
 * @version 1.0, 2013-3-28
 */
public class DESHelper
{
	/**
	 * ALGORITHM 算法 <br>
	 * 可替换为以下任意一种算法，同时key值的size相应改变。
	 * 
	 * <pre> 
     * DES                  key size must be equal to 56 
     * DESede(TripleDES)    key size must be equal to 112 or 168 
     * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available 
     * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive) 
     * RC2                  key size must be between 40 and 1024 bits 
     * RC4(ARCFOUR)         key size must be between 40 and 1024 bits 
     * </pre>
	 * 
	 * 在Key toKey(byte[] key)方法中使用下述代码 <code>SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);</code> 替换 <code> 
	 * DESKeySpec dks = new DESKeySpec(key); 
	 * SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM); 
	 * SecretKey secretKey = keyFactory.generateSecret(dks); 
	 * </code>
	 */
	public static final String ALGORITHM = "DES";

	/**
	 * 解密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String data, String keySource, String encoding) throws GeneralSecurityException
	{
		Key k = getSecretKey(keySource, encoding);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		if (encoding == null)
		{
			encoding = "ISO-8859-1";
		}
		try
		{
			return new String(cipher.doFinal(HexStringHelper.hexStrToBytes(data)), encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new GeneralSecurityException(e);
		}
	}

	/**
	 * 加密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String src, String keySource, String encoding) throws GeneralSecurityException
	{
		Key k = getSecretKey(keySource, encoding);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		if (encoding == null)
		{
			encoding = "ISO-8859-1";
		}
		try
		{
			return HexStringHelper.bytesToHexStr(cipher.doFinal(src.getBytes(encoding)));
		}
		catch (UnsupportedEncodingException e)
		{
			throw new GeneralSecurityException(e);
		}
	}

	/**
	 * 生成随机密钥
	 * @param seed
	 * @return
	 * @throws Exception
	 */
	public static String genRandomKey() throws GeneralSecurityException
	{
		SecureRandom secureRandom = new SecureRandom();

		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		kg.init(secureRandom);

		SecretKey secretKey = kg.generateKey();

		return HexStringHelper.bytesToHexStr(secretKey.getEncoded());
	}

	/**
	 * 转换密钥<br>
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getKey(String keySource, String encoding) throws GeneralSecurityException
	{
		return HexStringHelper.bytesToHexStr(getSecretKey(keySource, encoding).getEncoded());
	}

	/**
	 * 转换密钥<br>
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Key getSecretKey(String keySource, String encoding) throws GeneralSecurityException
	{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] data;
		if (encoding == null)
		{
			encoding = "ISO-8859-1";
		}
		try
		{
			data = keySource.getBytes(encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new GeneralSecurityException(e);
		}
		byte[] digest = md5.digest(data);
		byte[] hexStrToBytes = new byte[8];
		System.arraycopy(digest, 4, hexStrToBytes, 0, 8);
		DESKeySpec dks = new DESKeySpec(hexStrToBytes);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(dks);
		// 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
		// SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
		return secretKey;
	}
}
