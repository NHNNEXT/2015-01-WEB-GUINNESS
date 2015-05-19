package org.nhnnext.guinness.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtil {
	public static String addCurrentTime(String noteTargetDate) {
		return noteTargetDate + " " + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
	}
}
