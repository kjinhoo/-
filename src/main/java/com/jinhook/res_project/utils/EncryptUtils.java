package com.jinhook.res_project.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {
	

	public static String encryptToMD5(String str) {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(str.getBytes());
			
			String hashedPwd = new BigInteger(1,md5.digest()).toString();
			return hashedPwd;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(EncryptUtils.encryptToMD5("a"));
		System.out.println(EncryptUtils.encryptToMD5("b"));
		
		System.out.println( EncryptUtils.encryptToMD5("c"  )  );
	}

}
