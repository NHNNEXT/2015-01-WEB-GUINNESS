package org.nhnnext.guinness.common;

//TODO http://www.slipp.net/questions/360 글 참고해 수정
public class ReplaceIntoEntitycode {
	public static String intoEntitycode(String str) {
		return ((str.replace("<", "&lt;")).replace(">", "&gt;")).replace("\"", "&quot;");
	}
}
