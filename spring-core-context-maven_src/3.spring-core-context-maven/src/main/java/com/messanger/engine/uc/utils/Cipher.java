package com.messanger.engine.uc.utils;
import java.io.UnsupportedEncodingException;


public class Cipher {

	
	public static String SHA256Encrypt(String str) throws UnsupportedEncodingException{
		return Sha256Cipher.encrypt(str);
	}
}
