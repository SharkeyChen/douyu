package com.lei2j.douyu.util;

import java.nio.charset.Charset;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES128 对称加密解密(替代DES)
 * @author leijinjun
 *
 */
public class AESUtil {
	
	private AESUtil() {}
	
	private static String getKey() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(128,new SecureRandom());
			SecretKey generateKey = keyGenerator.generateKey();
			return Base64Util.encodeToString(generateKey.getEncoded());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static byte[] encrypt(String key,String input) {
		try {
			Cipher aes = Cipher.getInstance("AES");
			SecretKeySpec secretKeySpec = new SecretKeySpec(Base64Util.decode(key), "AES");
			aes.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			return aes.doFinal(input.getBytes(Charset.forName("utf-8")));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static byte[] decrypt(String key,byte[] input){
		try {
			Cipher aes = Cipher.getInstance("AES");
			SecretKeySpec secretKeySpec = new SecretKeySpec(Base64Util.decode(key), "AES");
			aes.init(Cipher.DECRYPT_MODE,secretKeySpec);
			return aes.doFinal(input);
		} catch (NoSuchAlgorithmException|NoSuchPaddingException
				|InvalidKeyException|BadPaddingException|IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		return null;
	}

}
