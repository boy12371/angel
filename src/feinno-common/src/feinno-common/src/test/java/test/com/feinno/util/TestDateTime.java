package test.com.feinno.util;

import java.text.ParseException;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.feinno.util.DateTime;
import com.feinno.util.TimeSpan;

public class TestDateTime {
	private DateTime dt ;
	@Before
	public void setUp() {
		dt = new DateTime(new Date());
	}

	@Test
	public void TestDateTime() {
		DateTime dtTest = new DateTime(10);
	}
	@Test
	public void TestNow() {
		System.out.println("DateTime:"+ DateTime.now());
	}
	@Test
	public void TestGetDate() {
		System.out.println("DateTime(getDate):"+dt.getDate());
	}
	@Test
	public void TestGetTime() {
		System.out.println("DateTime(getTime):"+dt.getTime());
	}
	@Test
	public void getTimeOfDay() {
		System.out.println("DateTime(getTimeOfDay().getDays()):"+dt.getTimeOfDay().getDays());
	}
	@Test
	public void getSubstract() {
		System.out.println("DateTime(substract()):"+dt.substract(new DateTime()));
	}
	@Test
	public void getAdd() {
		System.out.println("DateTime(add()):"+dt.add(new TimeSpan((long)1000)));
	}
	@Test
	public void getCompareTo() {
		dt.compareTo(dt);
		try{
		dt.compareTo(new Object());
		}catch(Exception e)
		{
			
		}
		//System.out.println("DateTime(add()):"+dt.add(new TimeSpan((long)1000)));
	}
}

