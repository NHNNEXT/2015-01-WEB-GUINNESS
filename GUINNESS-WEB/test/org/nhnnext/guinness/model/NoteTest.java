package org.nhnnext.guinness.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoteTest {
	private static final Logger logger = LoggerFactory.getLogger(NoteTest.class);
	@Test
	public void noteButtonClick() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		String targetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		logger.debug(targetDate);
	}

}
