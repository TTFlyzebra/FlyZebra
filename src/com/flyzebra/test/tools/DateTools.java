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
			str = "星期日";
			break;
		case 2:
			str = "星期一";
			break;
		case 3:
			str = "星期二";
			break;
		case 4:
			str = "星期三";
			break;
		case 5:
			str = "星期四";
			break;
		case 6:
			str = "星期五";
			break;
		case 7:
			str = "星期六";
			break;		
		}
		return str;
	}

}
