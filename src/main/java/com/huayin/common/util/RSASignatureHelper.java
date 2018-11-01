/**
 * <pre>
 * Title: 		SignatureHelper.java
 * Author:		linriqing
 * Create:	 	2010-5-18 下午03:18:51
 * Copyright: 	Copyright (c) 2010
 * Company:		Shenzhen Helper
 * <pre>
 */
package com.huayin.common.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;

/**
 * <pre>
 * RSA签名加密解密工具类
 * </pre>
 * @author linriqing
 * @version 1.0, 2010-5-18
 * @version 1.1, 2013-3-26
 */
public class RSASignatureHelper
{
	private static final String KEY_ALGORITHM = "RSA";

	private static final int KEY_ALGORITHM_BLOCK_SIZE = 1024;

	private static final String RSA_NONE_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";

	private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

	/**
	 * 解密<br>
	 * 用私钥解密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivateKey(String dec, String priKey, String encoding)
			throws GeneralSecurityException
	{
		// 对密钥解密
		byte[] keyBytes = HexStringHelper.hexStrToBytes(priKey.trim());

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(RSA_NONE_PKCS1_PADDING);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		if (encoding == null)
		{
			encoding = "ISO-8859-1";
		}
		try
		{
			return new String(cipher.doFinal(HexStringHelper.hexStrToBytes(dec)), encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new GeneralSecurityException(e);
		}
	}

	/**
	 * 解密<br>
	 * 用公钥解密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPublicKey(String dec, String pubkey, String encoding) throws GeneralSecurityException
	{
		// 对密钥解密
		byte[] keyBytes = HexStringHelper.hexStrToBytes(pubkey.trim());

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据解密
		Cipher cipher = Cipher.getInstance(RSA_NONE_PKCS1_PADDING);
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		if (encoding == null)
		{
			encoding = "ISO-8859-1";
		}
		try
		{
			return new String(cipher.doFinal(HexStringHelper.hexStrToBytes(dec)), encoding);
		}
		catch (UnsupportedEncodingException e)
		{
			throw new GeneralSecurityException(e);
		}
	}

	/**
	 * 加密<br>
	 * 用私钥加密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPrivateKey(String src, String priKey, String encoding)
			throws GeneralSecurityException
	{
		// 对密钥解密
		byte[] keyBytes = HexStringHelper.hexStrToBytes(priKey.trim());

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(RSA_NONE_PKCS1_PADDING);
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
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
	 * 加密<br>
	 * 用公钥加密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublicKey(String src, String pubkey, String encoding) throws GeneralSecurityException
	{
		// 对公钥解密
		byte[] keyBytes = HexStringHelper.hexStrToBytes(pubkey.trim());

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据加密
		Cipher cipher = Cipher.getInstance(RSA_NONE_PKCS1_PADDING);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
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
	 * <pre>
	 * 生成1024位RSA公私钥对
	 * </pre>
	 * @return 公私钥对数组，第一个元素是公钥，第二个是私钥
	 * @throws GeneralSecurityException 签名异常
	 */
	public static String[] genKeyPair() throws GeneralSecurityException
	{
		KeyPairGenerator rsaKeyGen = null;
		KeyPair rsaKeyPair = null;
		rsaKeyGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		SecureRandom random = new SecureRandom();
		String valueOf = String.valueOf(System.currentTimeMillis());
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < valueOf.length(); i++)
		{
			list.add(String.valueOf(valueOf.charAt(i)));
		}
		Collections.shuffle(list, new Random(System.currentTimeMillis()));
		StringBuffer sb = new StringBuffer();
		for (String string : list)
		{
			sb.append(string);
		}
		random.setSeed(sb.toString().getBytes());
		rsaKeyGen.initialize(KEY_ALGORITHM_BLOCK_SIZE, random);
		rsaKeyPair = rsaKeyGen.genKeyPair();
		PublicKey rsaPublic = rsaKeyPair.getPublic();
		PrivateKey rsaPrivate = rsaKeyPair.getPrivate();

		String[] keys = new String[2];
		keys[0] = HexStringHelper.bytesToHexStr(rsaPublic.getEncoded());
		keys[1] = HexStringHelper.bytesToHexStr(rsaPrivate.getEncoded());
		return keys;
	}

	/**
	 * <pre>
	 * 使用SHA1withRSA签名算法进行签名
	 * </pre>
	 * @param priKey 签名时使用的私钥(16进制编码)
	 * @param src 签名的原字符串
	 * @param encoding 字符编码
	 * @return 签名的返回结果(16进制编码)
	 * @throws GeneralSecurityException
	 */
	public static String signature(String priKey, String src, String encoding) throws GeneralSecurityException
	{
		Signature sigEng = Signature.getInstance(SIGNATURE_ALGORITHM);
		byte[] pribyte = HexStringHelper.hexStrToBytes(priKey.trim());
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pribyte);
		KeyFactory fac = KeyFactory.getInstance(KEY_ALGORITHM);

		RSAPrivateKey privateKey = (RSAPrivateKey) fac.generatePrivate(keySpec);
		sigEng.initSign(privateKey);
		if (encoding == null)
		{
			encoding = "ISO-8859-1";
		}
		try
		{
			sigEng.update(src.getBytes(encoding));
		}
		catch (UnsupportedEncodingException e)
		{
			throw new GeneralSecurityException(e);
		}

		byte[] signature = sigEng.sign();
		return HexStringHelper.bytesToHexStr(signature);
	}

	/**
	 * <pre>
	 * 使用RSA签名算法验证签名
	 * </pre>
	 * @param pubKey 验证签名时使用的公钥(16进制编码)
	 * @param sign 签名结果(16进制编码)
	 * @param src 签名的原字符串
	 * @param encoding 字符编码
	 * @return 签名的返回结果(16进制编码)
	 * @throws GeneralSecurityException 签名异常
	 */
	public static boolean verify(String pubKey, String sign, String src, String encoding)
			throws GeneralSecurityException
	{
		Signature sigEng = Signature.getInstance(SIGNATURE_ALGORITHM);
		byte[] pubbyte = HexStringHelper.hexStrToBytes(pubKey.trim());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubbyte);
		KeyFactory fac = KeyFactory.getInstance(KEY_ALGORITHM);
		RSAPublicKey rsaPubKey = (RSAPublicKey) fac.generatePublic(keySpec);

		sigEng.initVerify(rsaPubKey);
		if (encoding == null)
		{
			encoding = "ISO-8859-1";
		}
		try
		{
			sigEng.update(src.getBytes(encoding));
		}
		catch (UnsupportedEncodingException e)
		{
			throw new GeneralSecurityException(e);
		}

		byte[] sign1 = HexStringHelper.hexStrToBytes(sign);
		return sigEng.verify(sign1);
	}
}
