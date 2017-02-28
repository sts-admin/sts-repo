package com.sts.core.util;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.eclipse.persistence.internal.security.Securable;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

public class SecurityEncryptor implements Securable {

	private static Cipher ecipher;
	private static Cipher dcipher;
	private static String key = "MyS0c1a8l3R3ward";

	static {
		try {
			byte[] raw = key.getBytes(Charset.forName("UTF-8"));
			if (raw.length != 16) {
				throw new IllegalArgumentException("Invalid key size.");
			}
			SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
			;

			ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			ecipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[16]));
			dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			dcipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(new byte[16]));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
	}

	public String encryptPassword(String pswd) {
		return encrypt(pswd);
	}

	public String decryptPassword(String encryptedPswd) {
		return decrypt(encryptedPswd);
	}

	public static String encrypt(String inputString) {

		try {
			byte[] utf8 = inputString.getBytes("UTF8");
			byte[] encryptedBytes = ecipher.doFinal(utf8);
			// encode to base64
			encryptedBytes = BASE64EncoderStream.encode(encryptedBytes);
			return new String(encryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decrypt(String encryptedString) {

		try {
			// decode with base64 to get bytes
			byte[] decryptedBytes = BASE64DecoderStream.decode(encryptedString.getBytes());
			byte[] utf8Bytes = dcipher.doFinal(decryptedBytes);
			// create new string based on the specified char set
			return new String(utf8Bytes, "UTF8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void main(String args[]){
		System.out.println(decrypt("5A6IOH4es0ByPG2umFlneA=="));
	}
}
