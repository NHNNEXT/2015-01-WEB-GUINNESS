package org.nhnnext.guinness.util;

import java.util.Random;

public class GetRandom {
	private GetRandom() {
	}

	public static String getRandomId(int lengthOfReturnString) {
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < lengthOfReturnString; i++) {
			buf.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
		}
		return buf.toString();
	}
}
