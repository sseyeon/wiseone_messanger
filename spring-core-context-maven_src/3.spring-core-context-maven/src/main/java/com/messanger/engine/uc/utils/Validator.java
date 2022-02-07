package com.messanger.engine.uc.utils;

public class Validator {

	public static boolean isValidPhoneNumber(String number) {
		if (number == null || number.trim().length() == 0 || number.equals("null")) {
			return false;
		}
		
		return true;
	}
}
