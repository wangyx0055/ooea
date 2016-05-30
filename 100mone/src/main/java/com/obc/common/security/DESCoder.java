package com.obc.common.security;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES安全编码组件
 * 
 * <pre>
 *  
 * 支持 DES、DESede(TripleDES,就是3DES)、AES、Blowfish、RC2、RC4(ARCFOUR) 
 * DES                  key size must be equal to 56 
 * DESede(TripleDES)    key size must be equal to 112 or 168 
 * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available 
 * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive) 
 * RC2                  key size must be between 40 and 1024 bits 
 * RC4(ARCFOUR)         key size must be between 40 and 1024 bits
 * </pre>
 * 
 * @version 1.0
 * @since 1.0
 */
public class DESCoder extends Coder {

	/**
	 * ALGORITHM 算法 <br>
	 * 可替换为以下任意一种算法，同时key值的size相应改变。
	 * 
	 * <pre>
	 *  
	 * DES                  key size must be equal to 56 
	 * DESede(TripleDES)    key size must be equal to 112 or 168 
	 * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available 
	 * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive) 
	 * RC2                  key size must be between 40 and 1024 bits 
	 * RC4(ARCFOUR)         key size must be between 40 and 1024 bits
	 * </pre>
	 * 
	 * 在Key toKey(byte[] key)方法中使用下述代码
	 * <code>SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);</code> 替换
	 * <code> 
	 * DESKeySpec dks = new DESKeySpec(key); 
	 * SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM); 
	 * SecretKey secretKey = keyFactory.generateSecret(dks); 
	 * </code>
	 */
	public static final String ALGORITHM = "DES";

	/**
	 * 
	 * <br>方法名： toKey
	 * 
	 * <br>描述：【转换密钥】 
	 * <br>创建时间： 2016年5月31日 上午2:33:36 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static Key toKey ( byte[] key ) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(dks);
		// 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
		// SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
		return secretKey;
	}

	/**
	 * 
	 * <br>方法名： decrypt
	 * 
	 * <br>描述：【解密】 
	 * <br>创建时间： 2016年5月31日 上午2:33:43 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt (	byte[] data ,
									String key ) throws Exception {
		Key k = toKey(decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	/**
	 * 
	 * <br>方法名： decrypt
	 * 
	 * <br>描述：【解密】 
	 * <br>创建时间： 2016年5月31日 上午2:33:52 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decrypt (	String data ,
									String key ) throws Exception {
		byte[] inputData = DESCoder.decryptBASE64(data);
		byte[] out = decrypt(inputData, key);
		return new String(out);
	}

	/**
	 * 
	 * <br>方法名： encrypt
	 * 
	 * <br>描述：【加密】 
	 * <br>创建时间： 2016年5月31日 上午2:33:59 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String encrypt (	String data ,
									String key ) throws Exception {
		byte[] inputData = DESCoder.encrypt(data.getBytes(), key);
		return DESCoder.encryptBASE64(inputData);
	}

	/**
	 * 
	 * <br>方法名： encrypt
	 * 
	 * <br>描述：【加密】 
	 * <br>创建时间： 2016年5月31日 上午2:34:07 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt (	byte[] data ,
									String key ) throws Exception {
		Key k = toKey(decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	/**
	 * 
	 * <br>方法名： initKey
	 * 
	 * <br>描述：【生成密钥】 
	 * <br>创建时间： 2016年5月31日 上午2:34:15 
	 * @return
	 * @throws Exception
	 */
	public static String initKey ( ) throws Exception {
		return initKey(null);
	}

	/**
	 * 
	 * <br>方法名： initKey
	 * 
	 * <br>描述：【生成密钥】 
	 * <br>创建时间： 2016年5月31日 上午2:34:23 
	 * @param seed
	 * @return
	 * @throws Exception
	 */
	public static String initKey ( String seed ) throws Exception {
		SecureRandom secureRandom = null;
		if (seed != null) {
			secureRandom = new SecureRandom(decryptBASE64(seed));
		} else {
			secureRandom = new SecureRandom();
		}
		KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
		kg.init(secureRandom);
		SecretKey secretKey = kg.generateKey();
		return encryptBASE64(secretKey.getEncoded());
	}
}