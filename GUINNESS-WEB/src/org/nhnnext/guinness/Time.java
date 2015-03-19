package org.nhnnext.guinness;

import java.sql.Timestamp;

public class Time {
	public static Timestamp getTime() {
		long startTime = System.currentTimeMillis();
		Timestamp time = new Timestamp(startTime);
		return time;
	}
	
}
