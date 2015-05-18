package org.nhnnext.guinness.util;

import java.util.Random;

public class RandomFactory {
	private RandomFactory() {
	}

	public static String getRandomId(int lengthOfReturnString) {
		//TODO offline 코드 리뷰 - 리팩토링 point는?
		String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < lengthOfReturnString; i++) {
			buf.append(alphabet.charAt(rnd.nextInt(alphabet.length())));
		}
		return buf.toString();
	}
}
