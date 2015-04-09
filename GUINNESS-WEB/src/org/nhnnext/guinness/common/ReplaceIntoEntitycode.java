package org.nhnnext.guinness.common;

public class ReplaceIntoEntitycode {
	private ReplaceIntoEntitycode() {
	}

	public static String intoEntitycode(String str) {
		return ((str.replace("<", "&lt;")).replace(">", "&gt;")).replace("\"", "&quot;");
	}
}
