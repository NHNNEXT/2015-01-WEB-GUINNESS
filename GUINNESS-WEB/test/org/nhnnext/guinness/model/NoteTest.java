package org.nhnnext.guinness.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class NoteTest {

	@Test
	public void noteButtonClick() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		String targetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		System.out.println(targetDate);
	}

}
