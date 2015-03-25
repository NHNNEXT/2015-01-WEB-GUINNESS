package org.nhnnext.guinness.controller;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class CreateNoteServletTest {

	@Test
	public void addTimeIntoTargetDate() {
		String targetDate = "2015-03-25";

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		targetDate += " " + dateFormat.format(calendar.getTime());;
		
		System.out.println(targetDate);
	}

}
