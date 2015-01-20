package test.com.feinno.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.util.DateUtil;

public class TestDateUtil {
	@Test
	public void TestGetDefaultYearMonthDayTime() {
		System.out.println(DateUtil.getDefaultYearMonthDayTime());
		System.out.println(DateUtil.getDefaultYearMonthDayTime(new Date()));
	}

	@Test
	public void TestGetClientDateTime() {
		System.out.println(DateUtil.getClientDateTime(new Date()));
	}

	@Test
	public void TestGetDefaultHourTime() {
		System.out.println(DateUtil.getDefaultHourTime());
	}

	@Test
	public void TestGetDefaultDate() {
		System.out.println(DateUtil.getDefaultDate());

		try {
			System.out.println(DateUtil.getDefaultDate("2012-02-21 17:03:29"));
			System.out.println(DateUtil.getDefaultDate("2012-02-21 17:03:29",
					"yyyy-MM-dd"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void TestGetDefaultYearMonth() {
		System.out.println(DateUtil.getDefaultYearMonth());
	}

	@Test
	public void TestGetDefaultMonthDay() {
		System.out.println(DateUtil.getDefaultMonthDay());
	}

	@Test
	public void TestGetDefaultTime() {
		System.out.println(DateUtil.getDefaultTime());
		System.out.println(DateUtil.getDefaultTime("HHmmss"));
	}

	@Test
	public void TestGetSqlDate() {
		System.out.println(DateUtil.getSqlDate(new Date()));
	}

	@Test
	public void TestGetUtilDate() {
		try {
			System.out.println(DateUtil.getUtilDate(new java.sql.Date(0)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			System.out.println(DateUtil.getUtilDate(new Date()));
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertTrue(true);
		}
	}

	@Test
	public void TestAddDatefield() {
		Date date = new Date(2012, 1, 1, 11, 11, 11);
		Assert.assertEquals("Mon Feb 12 11:11:11 CST 3912", DateUtil
				.addDatefield(date, 5, 11).toString());
		Assert.assertEquals("Sat Feb 01 11:11:11 CST 3913", DateUtil
				.addDatefield(date, 1, 1).toString());
		Assert.assertEquals("Fri Mar 01 11:11:11 CST 3912", DateUtil
				.addDatefield(date, 2, 1).toString());
		Assert.assertEquals("Thu Feb 01 12:11:11 CST 3912", DateUtil
				.addDatefield(date, 10, 1).toString());
		Assert.assertEquals("Thu Feb 01 11:12:11 CST 3912", DateUtil
				.addDatefield(date, 12, 1).toString());
		Assert.assertEquals("Thu Feb 01 11:11:12 CST 3912", DateUtil
				.addDatefield(date, 13, 1).toString());
		try {
			DateUtil.addDatefield(date, 16, 1).toString();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.assertTrue(true);
		}
	}

	@Test
	public void TestAddDay() {
		Date date = new Date(2012, 1, 1, 11, 11, 11);
		Assert.assertEquals("Mon Feb 12 11:11:11 CST 3912",
				DateUtil.addDay(date, 11).toString());

	}

	@Test
	public void TestAddMonth() {
		Date date = new Date(2012, 1, 1, 11, 11, 11);
		Assert.assertEquals("Fri Mar 01 11:11:11 CST 3912",
				DateUtil.addMonth(date, 1).toString());
	}

	@Test
	public void TestGetSystemCurrentDate() {
		System.out.println(DateUtil.getSystemCurrentDate("yyyy.MM.dd"));
	}

	@Test
	public void TestMonth() {
		Assert.assertEquals("2012-02-01", DateUtil.month("2012", "2"));
	}

	@Test
	public void TestMonthAdd() {
		Assert.assertEquals("2012-3-01", DateUtil.monthAdd("2012", "02"));
		Assert.assertEquals("2013-01-01", DateUtil.monthAdd("2012", "12"));
	}

	@Test
	public void TestGetSystemCurrentTime() {
		System.out.println(DateUtil.getSystemCurrentTime());
	}

	@Test
	public void TestGetTimestamp() {
		Assert.assertEquals("3900-02-01 00:00:00.0",
				DateUtil.getTimestamp(new Date(2000, 1, 1)).toString());
		try {
			Assert.assertEquals("2000-01-01 00:00:00.0",
					DateUtil.getTimestamp("2000-1-1").toString());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void TestGetTimestampString() {
		Assert.assertEquals("1970年01月01日 08时00分01秒", DateUtil
				.getTimestampString(new Timestamp(1000)).toString());
	}

	@Test
	public void TestGetDateDiffDay() {
		Assert.assertEquals(10, DateUtil.getDateDiffDay(new Date(2000, 11, 1),
				new Date(2000, 11, 11)));

	}

	@Test
	public void TestGetDateFiled() {
		Assert.assertEquals(0,
				DateUtil.getDateFiled(null, new Date(2000, 11, 11), 1));
		Assert.assertEquals(0,
				DateUtil.getDateFiled(new Date(2000, 11, 1), null, 1));

	}

	@Test
	public void TestCompareDate() {
		Date date1 = new Date(2000, 1, 1);
		Date date2 = new Date(2000, 2, 2);
		Assert.assertEquals(0, DateUtil.compareDate(date1, date1));
		Assert.assertEquals(-1, DateUtil.compareDate(date1, date2));
	}

	@Test
	public void TestFormatDateStr() {
		System.out.println(DateUtil.formatDateStr(Calendar.getInstance(),
				"yyyy-MM-dd HH:mm:ss E"));
		System.out.println(DateUtil.formatDateStr(new Date(),
				"yyyy-MM-dd HH:mm:ss E"));

	}

	@Test
	public void TestGetPreviousWeek() {
		for (String str : DateUtil.getPreviousWeek()) {
			System.out.println("getPreviousWeek: " + str);
		}
	}

	@Test
	public void TestGetPreviousMonth() {
		for (String str : DateUtil.getPreviousMonth()) {
			System.out.println("getPreviousMonth: " + str);
		}
	}

	@Test
	public void TestIsRightDateFormat() {
		Assert.assertEquals(false, DateUtil.isRightDateFormat(null));
		Assert.assertEquals(true, DateUtil.isRightDateFormat("2000-1-1"));
	}

	@Test
	public void TestGetAttachmentTime() {
		Assert.assertEquals("19700101080002000_",
				DateUtil.getAttachmentTime(new Timestamp(2000)));
	}

	@Test
	public void TestFormatDateString() {

		Assert.assertEquals("2000.10.01",
				DateUtil.formatDateString("2000.10.1", "yyyy.MM.dd"));
	}

	@Test
	public void TestParseComplexGMTStringDate() {

		System.out.println("DateUtil.TestParseComplexGMTStringDate():"
				+ DateUtil.parseComplexGMTStringDate("2000.10.10"));
		;
	}

	@Test
	public void TestGetGMTFormater() {
		System.out.println("DateUtil.TestGetGMTFormater():"
				+ DateUtil.getGMTFormater().getCalendar().getTime());
	}

	@Test
	public void TestToGMTString() {
		System.out.println("DateUtil.toGMTString():"
				+ DateUtil.toGMTString(new Date()));
	}

	@Test
	public void TestToComplexGMTString() {
		System.out.println("DateUtil.toComplexGMTString():"
				+ DateUtil.toComplexGMTString(new Date()));
	}

	@Test
	public void TestGetTodayDate() {
		System.out.println("DateUtil.formatDate():"
				+ DateUtil.getTodayDate("yyyy-MM-dd HH:mm:ss E"));
	}

	@Test
	public void TestGetUTCDate() {
		System.out.println("DateUtil.getUTCDate():"
				+ DateUtil.getUTCDate(new Date()));
	}

	@Test
	public void TestGetUTCNow() {
		System.out.println("DateUtil.getUTCNow():" + DateUtil.getUTCNow());
	}

	@Test
	public void TestVerfiySmallDateTime() {
		Assert.assertEquals(true, DateUtil.verfiySmallDateTime(new Date()));
		Assert.assertEquals(false,
				DateUtil.verfiySmallDateTime(new Date(1899, 1, 1)));
	}

	@Test
	public void TestGetDayOfWeak() {
		System.out.println("DateUtil.getDayOfWeak():"
				+ DateUtil.getDayOfWeak(new Date()));
	}

	@Test
	public void TestGetDayOfMonth() {
		System.out.println("DateUtil.getDayOfMonth():"
				+ DateUtil.getDayOfMonth(new Date()));
	}

	@Test
	public void TestGetMonthFirst() {
		System.out.println("DateUtil.getMonthFirst():"
				+ DateUtil.getMonthFirst(new Date()));
	}

	@Test
	public void TestGetMonthEnd() {
		System.out.println("DateUtil.getMonthEnd():"
				+ DateUtil.getMonthEnd(new Date()));
	}
	
	@Test
	public void TestMysqlTimeStamp() {
		System.out.println("DateUtil.getMysqlTimeStamp():"
				+ DateUtil.getMysqlTimeStamp(new Date()));
	}
	@Test
	public void TestBirthDateFor229() {
		System.out.println("DateUtil.getBirthDateFor229():"
				+ DateUtil.getBirthDateFor229(new Date()));
	}
	@Test
	public void TestGetGMTDate() {
		System.out.println("DateUtil.getGMTDate():"
				+ DateUtil.getGMTDate(new Date()));
		System.out.println("DateUtil.getGMTDate()-Sql:"
				+ DateUtil.getGMTDate(new java.sql.Date(100)));
	}
}
