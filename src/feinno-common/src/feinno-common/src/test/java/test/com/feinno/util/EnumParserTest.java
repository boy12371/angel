package test.com.feinno.util;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import com.feinno.util.EnumParser;
import com.feinno.util.Flags;

public class EnumParserTest {
	private ArrayList valueOf;

	@Test
	public void testParseString() {
		String s = "A";
		try {
			TestEnum en = (TestEnum) EnumParser.parse(TestEnum.class, s, false);
			System.out.println(en.toString());
			System.out.println(en.intValue());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testParseString2() {
		String s = "1";
		try {
			TestEnum en = (TestEnum) EnumParser.parse(TestEnum.class, s, false);
			System.out.println(en.toString());
			System.out.println(en.intValue());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testNotParseString() {
		String s = "1";
		try {
			TestEnum en = (TestEnum) EnumParser.parse(EnumParserTest.class, s,
					false);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testParseInt() {
		int i = 1;
		try {
			TestEnum en = (TestEnum) EnumParser.parseInt(TestEnum.class, 1);
			System.out.println(en.toString());
			System.out.println(en.intValue());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testParseStringIgnoreCase() {
		String s = "a";
		try {
			TestEnum en = (TestEnum) EnumParser.parse(TestEnum.class, s, true);
			System.out.println(en.toString());
			System.out.println(en.intValue());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testParseHex() {
		String s = "0x0001";
		try {
			TestEnum en = (TestEnum) EnumParser.parse(TestEnum.class, s, true);
			System.out.println(en.toString());
			System.out.println(en.intValue());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testParseHex2() {
		String s = "0X0000000001";
		try {
			TestEnum en = (TestEnum) EnumParser.parse(TestEnum.class, s, true);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			e.getMessage();
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testParseHex3() {
		String s = "D";
		try {
			TestEnum en = (TestEnum) EnumParser.parse(TestEnum.class, s, true);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			e.getMessage();
			Assert.assertTrue(true);
		}
	}

	@Test
	public void testParseFlags() {
		String s = "A,B,C";
		try {
			Flags<TestEnum> fs = EnumParser.parseFlags(TestEnum.class, s, true);
			System.out.println(fs.toString());
			System.out.println(fs.value());
			Assert.assertEquals(true, fs.has(TestEnum.A));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testFlags() {
		String s = "3";
		try {
			Flags fsx = new Flags(3);
			Flags<TestEnum> fs = EnumParser.parseFlags(fsx.getClass(), s, true);
			System.out.println(fs.toString());
			System.out.println(fs.value());
			Assert.assertEquals(true, fs.has(TestEnum.A));
			Assert.assertEquals(true, fs.has(TestEnum.B));
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void TestValueOf() {
		try {
			EnumParser.valueOf(TestEnum.class, 1);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void TestParseFlagsString() {
		try {
			EnumParser.parseFlagsString(TestEnum.class, "0x0001", true);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void TestParseFlagsString2() {
		try {
			EnumParser.parseFlagsString(TestEnum.class, "0X0001", true);
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void TestParseFlagsStringException() {
		try {
			EnumParser.parseFlagsString(TestEnum.class, "0X00000000001", true);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

}
