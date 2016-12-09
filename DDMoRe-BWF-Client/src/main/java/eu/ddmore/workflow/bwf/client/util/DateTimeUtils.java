package eu.ddmore.workflow.bwf.client.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date and time utilities. 
 * Calendars are not lenient (which is preferable but not default).
 */
public class DateTimeUtils {

	public static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";
	public static final String DEFAULT_TIME_FORMAT = "HH:mm";
	public static final String DEFAULT_DATE_TIME_FORMAT = DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT;
	public static final String DEFAULT_DATE_TIME_MS_FORMAT = DEFAULT_DATE_FORMAT + " " + DEFAULT_TIME_FORMAT + ":ss.SSS";

	public static final TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone("GMT");

	/** Number of milliseconds in a second. */
	public static final int SECOND_MS = 1000;
	/** Number of milliseconds in a minute. */
	public static final int MINUTE_MS = 60000;
	/** Number of milliseconds in an hour. */
	public static final int HOUR_MS = 3600000;
	/** Number of milliseconds in a day. */
	public static final int DAY_MS = 86400000;
	/** Number of milliseconds in a week. */
	public static final int WEEK_MS = 604800000;

	/** Number of seconds in a minute. */
	public static final int MINUTE_S = 60;
	/** Number of seconds in an hour. */
	public static final int HOUR_S = 3600;
	/** Number of seconds in a day. */
	public static final int DAY_S = 86400;
	/** Number of seconds in a week. */
	public static final int WEEK_S = 604800;

	public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
	public static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
	public static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat(DEFAULT_DATE_TIME_FORMAT);
	public static final SimpleDateFormat DATE_TIME_MS_FORMATTER = new SimpleDateFormat(DEFAULT_DATE_TIME_MS_FORMAT);
	private static final DecimalFormat TIME_STRING_FORMATTER = new DecimalFormat("##00");

	static {
		// lenient is dangerous but unfortunately default
		DATE_FORMATTER.setLenient(false);
		TIME_FORMATTER.setLenient(false);
		DATE_TIME_FORMATTER.setLenient(false);
		DATE_TIME_MS_FORMATTER.setLenient(false);
	}

	private DateTimeUtils() {
		super();
	}

	public static String getDate(Date date) {
		return DATE_FORMATTER.format(date);
	}

	public static String getTime(Date date) {
		return TIME_FORMATTER.format(date);
	}

	public static String getDateTime(Date date) {
		return DATE_TIME_FORMATTER.format(date);
	}

	public static String getDateTimeMs(Date date) {
		return DATE_TIME_MS_FORMATTER.format(date);
	}

	public static String getDate(Date date, SimpleDateFormat formatter) {
		return formatter.format(date);
	}

	public static String getDate(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}

	public static Date getDate(String date) throws ParseException {
		return DATE_FORMATTER.parse(date);
	}

	public static Date getTime(String date) throws ParseException {
		return TIME_FORMATTER.parse(date);
	}

	public static Date getDateTime(String date) throws ParseException {
		return DATE_TIME_FORMATTER.parse(date);
	}

	public static Date getDateTimeMs(String date) throws ParseException {
		return DATE_TIME_MS_FORMATTER.parse(date);
	}
	
	public static Date getDate(String date, SimpleDateFormat formatter) throws ParseException {
		return formatter.parse(date);
	}

	public static Date getDate(String date, String format) throws ParseException {
		return new SimpleDateFormat(format).parse(date);
	}

	/**
	 * Adds an amount of unit to a date. Uses {@link Calendar}.
	 * 
	 * @param src the source {@link Date}.
	 * @param calendarUnit one of the {@link Calendar} unit constants (e.g. {@link Calendar#HOUR_OF_DAY} or {@link Calendar#DAY_OF_YEAR})
	 * @param amount the unit
	 * @return a date with the amount of units added (or subtracted, if negative).
	 */
	public static Date addToDate(Date src, int calendarUnit, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.setTime(src);
		cal.add(calendarUnit, amount);
		return cal.getTime();
	}

	public static Date setStartOfDay(final Date date) {
		Calendar cal = getCalendar(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static Date setEndOfDay(final Date date) {
		Calendar cal = getCalendar(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		// cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	public static Calendar setStartOfDay(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	public static Calendar setEndOfDay(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal;
	}

	/** Sets minutes, seconds and milliseconds to 0 */
	public static final Calendar setTimeToGMTHourBegin(final Date date) {
		Calendar hourBegin = getGMTCalendar(date);
		hourBegin.set(Calendar.MINUTE, 0);
		hourBegin.set(Calendar.SECOND, 0);
		hourBegin.set(Calendar.MILLISECOND, 0);
		return hourBegin;
	}

	/** Sets the minutes and seconds to 59 and milliseconds to 999 */
	public static final Calendar setTimeToGMTHourEnd(final Date date) {
		Calendar hourEnd = getGMTCalendar(date);
		hourEnd.set(Calendar.MINUTE, 59);
		hourEnd.set(Calendar.SECOND, 59);
		hourEnd.set(Calendar.MILLISECOND, 999);
		return hourEnd;
	}

	public static final int getHour24(final Date date) {
		Calendar cal = getGMTCalendar(date);
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	/** @return if the date is a weekday (no weekend) */
	public static final boolean isWeekDay(final Date date) {
		return !isWeekEnd(date);
	}

	/** @return if the date is on a weekend (respecting Friday, 23:00) */
	public static final boolean isWeekEnd(final Date date) {
		Calendar day = getCalendar(date);
		int dow = day.get(Calendar.DAY_OF_WEEK);
		if (dow == Calendar.SUNDAY || dow == Calendar.SATURDAY) {
			return true;
		}
		if (dow == Calendar.FRIDAY && day.get(Calendar.HOUR_OF_DAY) == 23) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the date for a floating day. Example: 3rd Monday in January 2011.
	 * 
	 * @param nTh the nTh (1st, 2nd, 3rd, etc.) day of week in a month.
	 * @param dayOfWeek {@link Calendar#MONDAY} to {@link Calendar#SUNDAY}
	 * @param month {@link Calendar#JANUARY} to {@link Calendar#DECEMBER}
	 * @param year the year.
	 * @return the {@link Date} of the floating date.
	 */
	public static final Date getFloatingDay(int nTh, int dayOfWeek, int month, int year) {
		int offset;
		int earliestDate = 1 + 7 * (nTh - 1);
		Calendar cal = getCalendar(earliestDate, month, year);
		int weekday = cal.get(Calendar.DAY_OF_WEEK);

		if (dayOfWeek == weekday)
			offset = 0;
		else {
			if (dayOfWeek < weekday)
				offset = dayOfWeek + (7 - weekday);
			else
				offset = (dayOfWeek + (7 - weekday)) - 7;
		}

		return getCalendar(earliestDate + offset, month, year).getTime();
	}

	public static final Date addTime(Date date, Integer seconds) {
		return addTime(date, seconds, 0);
	}

	public static final Date addTime(Date date, Integer seconds, Integer milliseconds) {
		Calendar cal = getCalendar(date);

		// first: add days
		int dayCount = seconds / 86400;
		if (dayCount != 0) {
			cal.add(Calendar.DAY_OF_YEAR, dayCount);
			seconds = seconds % 86400;
		}
		// second: add remaining seconds
		if (seconds != 0) {
			cal.add(Calendar.SECOND, seconds);
		}
		// milliseconds
		if (milliseconds != 0) {
			cal.add(Calendar.MILLISECOND, milliseconds);
		}
		return cal.getTime();
	}

	public static final Date subTime(Date date, Integer seconds) {
		return addTime(date, (seconds * -1), 0);
	}

	public static final Date subTime(Date date, Integer seconds, Integer milliseconds) {
		return addTime(date, (seconds * -1), (milliseconds * -1));
	}

	/**
	 * @return if the two dates have the same date ignoring the year.
	 */
	public static final boolean equalsDayMonth(Date date1, Date date2) {
		Calendar cal1 = getCalendar(date1);
		Calendar cal2 = getCalendar(date2);
		return (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH));
	}

	/**
	 * @return if the date has the day and month given.
	 */
	public static final boolean equalsDayMonth(Date date1, int day, int month) {
		Calendar cal1 = getCalendar(date1);
		return (cal1.get(Calendar.DAY_OF_MONTH) == day && cal1.get(Calendar.MONTH) == month);
	}

	/**
	 * @return the day of the week for the given date.
	 * @see {@link Calendar#MONDAY} to {@link Calendar#SUNDAY}
	 */
	public static final int getDayOfWeek(final Date date) {
		Calendar day = getCalendar(date);
		return day.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Instantiates a {@code Calendar} using the local time zone and sets the time.
	 * 
	 * @param date the {@code Date} which holds the time
	 */
	public static Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.setTime(date);
		return cal;
	}

	/**
	 * Returns a {@link Date} with the offset given.
	 * 
	 * @param date the reference date.
	 * @param field any valid {@link Calendar} constant e.g. {@link Calendar#HOUR}.
	 * @param amount the number of periods given in field.
	 * @return the date with the offset given.
	 */
	public static Date getDateWithOffset(Date date, int field, int amount) {
		Calendar cal = getCalendar(date);
		cal.add(field, amount);
		return cal.getTime();
	}

	/**
	 * Instantiates a {@code Calendar} using the GMT time zone and sets the time.
	 * 
	 * @param date the {@code Date} which holds the time
	 */
	public static Calendar getGMTCalendar(Date date) {
		Calendar cal = Calendar.getInstance(TIME_ZONE_GMT);
		cal.setLenient(false);
		cal.setTime(date);
		return cal;
	}

	/**
	 * Instantiates a {@code Calendar} using the local time zone and sets the time.
	 * 
	 * @param time the Time in milliseconds
	 */
	public static Calendar getCalendar(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.setTimeInMillis(time);
		return cal;
	}

	/**
	 * Instantiates a {@code Calendar} using the local time zone and sets the date.
	 * 
	 * @param day the day of the month.
	 * @param month {@link Calendar#JANUARY} to {@link Calendar#DECEMBER} (zero-based!)
	 * @param year the year.
	 * @return the date with day, month and year set.
	 */
	public static Calendar getCalendar(int day, int month, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		return cal;
	}

	/**
	 * Instantiates a {@code Calendar} using the local time zone and sets the date and time.
	 * 
	 * @param day the day of the month.
	 * @param month {@link Calendar#JANUARY} to {@link Calendar#DECEMBER}
	 * @param year the year.
	 * @param hour the hour.
	 * @param minute the minute.
	 * @param second the second.
	 * @param milliseconds the millisecond.
	 * @return the resulting date with time.
	 */
	public static Calendar getCalendar(int day, int month, int year, int hour, int minute, int second, int milliseconds) {
		Calendar cal = getCalendar(day, month, year);
		cal.set(Calendar.HOUR_OF_DAY, hour); // DO NOT USE Calendar.HOUR (0 = 0pm = noon)
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, second);
		cal.set(Calendar.MILLISECOND, milliseconds);
		return cal;
	}

	/**
	 * Instantiates a {@code Calendar} using the GMT time zone and sets the time.
	 * 
	 * @param day the day of the month.
	 * @param month {@link Calendar#JANUARY} to {@link Calendar#DECEMBER}
	 * @param year the year.
	 * @return the date with day, month and year set.
	 */
	public static Calendar getGMTCalendar(int day, int month, int year) {
		Calendar cal = Calendar.getInstance(TIME_ZONE_GMT);
		cal.setLenient(false);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.YEAR, year);
		return cal;
	}

	public static String getTimeString_HH_MM_SS(Date date) {
		return getTimeString_HH_MM_SS(date.getTime());
	}

	public static String getTimeString_HH_MM_SS(Date date, String delim) {
		return getTimeString_HH_MM_SS(date.getTime(), delim);
	}

	public static String getTimeString_HH_MM_SS(long date) {
		return getTimeString_HH_MM_SS(date, ":");
	}

	public static String getTimeString_HH_MM_SS(long date, String delim) {
		int hours = (int) (date / 3600000);
		int minutes = (int) ((date % 3600000) / 60000);
		int seconds = (int) ((date % 60000) / 1000);

		return TIME_STRING_FORMATTER.format(hours) + delim + TIME_STRING_FORMATTER.format(minutes) + delim + TIME_STRING_FORMATTER.format(seconds);
	}

	/**
	 * Get date string from file time long value.
	 * 
	 * @param fileTime long vaule file time.
	 * 
	 * @return date in string format
	 */
	public static Date getDateFromFileTimeGMT(long fileTime) {
		// this is the nano time for January 1, 1970, 00:00:00 GMT.
		long nanosatJavaEpoch = 116444736000000000L;
		// the time in binary message is nano time.
		long time = fileTime - nanosatJavaEpoch;
		return new Date(time / 10000);
	}

	/**
	 * get the first day of month (time set to 00:00:00.000)
	 * @param date date with month of interest
	 * @return first day of month
	 */
	public static Date getStartOfMonthDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	/**
	 * get last day of month (time set to 23:59:59.999)
	 * @param date date with month of interest
	 * @return last day of month
	 */
	public static Date getEndOfMonthDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	public static int monthsBetween(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setLenient(false);
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setLenient(false);
		cal2.setTime(date2);
		
		int m1 = cal1.get(Calendar.YEAR) * 12 + cal1.get(Calendar.MONTH);
		int m2 = cal2.get(Calendar.YEAR) * 12 + cal2.get(Calendar.MONTH);
		return m2 - m1;
	}
	
	public static int getWeekOfYear(Date date) {
		/** Evaluate week-of-year */
		Calendar cal = DateTimeUtils.getCalendar(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
}
