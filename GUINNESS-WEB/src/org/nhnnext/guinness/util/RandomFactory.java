package org.nhnnext.guinness.util;

import java.util.Random;

public class RandomFactory {
	private RandomFactory() {
	}

	public static String getRandomId(int lengthOfReturnString) {
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < lengthOfReturnString; i++) {
			buf.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
		}
		return buf.toString();
	}
	
	public static String getRandomKeyAddress(int lengthOfReturnAddress) {
		return "http://localhost:8080/user/confirm/" + getRandomId(lengthOfReturnAddress);
	}
}
