package com.flyzebra.test.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTools {
	public static String getCurrentDate(String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,
				Locale.getDefault());
		Date date = new Date(System.currentTimeMillis());
		return sdf.format(date);
	}

	public static String getCurrentWeek() {
		String str = "";
		Date date = new Date(System.currentTimeMillis());
		final Calendar mCalendar = Calendar.getInstance();
		mCalendar.setTime(date);
		switch (mCalendar.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			str = "������";
			break;
		case 2:
			str = "����һ";
			break;
		case 3:
			str = "���ڶ�";
			break;
		case 4:
			str = "������";
			break;
		case 5:
			str = "������";
			break;
		case 6:
			str = "������";
			break;
		case 7:
			str = "������";
			break;		
		}
		return str;
	}

}
