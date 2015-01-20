package test.com.feinno.util;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.feinno.util.Outter;
import com.feinno.util.StringUtils;

public class TestStringUtiles {
	private Outter<String> out1;
	private Outter<String> out2;

	@Before
	public void setUp() {
		out1 = new Outter<String>();
		out2 = new Outter<String>();
	}

	@Test
	public void TestSplitWithFirst() {
		String strTest = "Test";
		Assert.assertEquals(true,
				StringUtils.splitWithFirst(strTest, 'e', out1, out2));
		System.out.println("SplitWithFirst Out first:" + out1.value());
		System.out.println("SplitWithFirst Out left:" + out2.value());
		Assert.assertEquals(false,
				StringUtils.splitWithFirst(strTest, 'g', out1, out2));

		Assert.assertEquals(true,
				StringUtils.splitWithFirst(strTest, "es", out1, out2));
		System.out.println("SplitWithFirst Out first:" + out1.value());
		System.out.println("SplitWithFirst Out left:" + out2.value());
		Assert.assertEquals(false,
				StringUtils.splitWithFirst(strTest, "ts", out1, out2));
	}

	@Test
	public void TestSplitWithLast() {
		String strTest = "Test";
		Assert.assertEquals(true,
				StringUtils.splitWithLast(strTest, 'e', out1, out2));
		System.out.println("SplitWithLast Out first:" + out1.value());
		System.out.println("SplitWithLast Out left:" + out2.value());
		Assert.assertEquals(false,
				StringUtils.splitWithLast(strTest, 'g', out1, out2));

		Assert.assertEquals(true,
				StringUtils.splitWithLast(strTest, "es", out1, out2));
		System.out.println("SplitWithLast Out first:" + out1.value());
		System.out.println("SplitWithLast Out left:" + out2.value());
		Assert.assertEquals(false,
				StringUtils.splitWithLast(strTest, "ts", out1, out2));
	}

	@Test
	public void TestExtractQuoted() {
		try {
			Outter<String> outT = new Outter<String>();
			String strTest = "Test";
			StringUtils.extractQuoted(strTest, 't', 'e', outT, out1, out2);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void TestFromHexString() {
		Assert.assertEquals(0, StringUtils.fromHexString(null).length);
		Assert.assertEquals(0, StringUtils.fromHexString("").length);
		Assert.assertEquals(2, StringUtils.fromHexString("12345").length);
	}

	@Test
	public void TestIsNullOrEmpty() {
		Assert.assertEquals(true, StringUtils.isNullOrEmpty(null));
		Assert.assertEquals(false, StringUtils.isNullOrEmpty("Test"));
	}

	@Test
	public void TestTrimEnd() {
		Assert.assertEquals("123", StringUtils.trimEnd("1234", '4'));
	}

	@Test
	public void TestSplitValuePairs() {
		try {
			StringUtils.splitValuePairs("Test for SplitValuePairs", " ", "for");
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail();
		}
		System.out.println("SplitValuePairs:"
				+ StringUtils.splitValuePairs("Testr for SplitValuePairs", " ",
						"r"));

	}

	@Test
	public void TestFormatBuffer() {
		Assert.assertEquals("<NULL>", StringUtils.formatBuffer(null));
		byte[] byteTest = new byte[] { 'T', 'e', 's', 't' };
		Assert.assertEquals("<4: 54 65 73 74>",
				StringUtils.formatBuffer(byteTest));
	}

	@Test
	public void TestIsAllDBC() {
		Assert.assertEquals(true, StringUtils.isAllDBC("Test", 10));
		Assert.assertEquals(false, StringUtils.isAllDBC("Test！", 10));

		Assert.assertEquals(true, StringUtils.isAllDBC("Test"));
		Assert.assertEquals(false, StringUtils.isAllDBC("Test！"));
	}

	@Test
	public void TestToDBC() {
		Assert.assertEquals('t', StringUtils.toDBC('t'));
		Assert.assertEquals('!', StringUtils.toDBC('！'));
		Assert.assertEquals('\n', StringUtils.toDBC('\n'));

		Assert.assertEquals("test", StringUtils.toDBC("test"));
		Assert.assertEquals("test!", StringUtils.toDBC("test！"));
	}

	@Test
	public void TestToSBC() {
		Assert.assertEquals('ｔ', StringUtils.toSBC('t'));
		Assert.assertEquals('！', StringUtils.toSBC('！'));
		Assert.assertEquals('\n', StringUtils.toSBC('\n'));

		Assert.assertEquals("ｔｅｓｔ", StringUtils.toSBC("test"));
		Assert.assertEquals("ｔｅｓｔ！", StringUtils.toSBC("ｔｅｓｔ！"));
	}

	@Test
	public void TestTruncateStringWithDBC() {
		Assert.assertEquals("test ｔｅｓ",
				StringUtils.truncateStringWithDBC("test ｔｅｓｔ", 10));
		Assert.assertEquals("test ｔｅｓｔ",
				StringUtils.truncateStringWithDBC("test ｔｅｓｔ", 100));

	}

	@Test
	public void TestGetNewStrArray() {
		int i = 10;
		String[] str = StringUtils.getNewStrArray(i);
		for (int j = 0; j < i; j++) {
			Assert.assertEquals("", str[j]);
		}
	}

	@Test
	public void TestIsNum() {
		Assert.assertEquals(true, StringUtils.isNum("1234"));
		Assert.assertEquals(false, StringUtils.isNum("test"));
	}

	@Test
	public void TestStr2int() {
		Assert.assertEquals(10, StringUtils.str2int("", 10));
		Assert.assertEquals(1234, StringUtils.str2int("1234", 10));
		Assert.assertEquals(10, StringUtils.str2int("test", 10));

		Assert.assertEquals(10, StringUtils.str2int("", (long) 10));
		Assert.assertEquals(1234, StringUtils.str2int("1234", (long) 10));
		Assert.assertEquals(10, StringUtils.str2int("test", (long) 10));
	}

	@Test
	public void TestInt2str() {
		Assert.assertEquals("1234", StringUtils.int2str(1234, 2, "Test"));
		Assert.assertEquals("{Test:D20}", StringUtils.int2str(1234, 20, "Test"));
		Assert.assertEquals("1234", StringUtils.int2str(1234, 10, ""));

	}

	@Test
	public void TestSubstring() {
		Assert.assertEquals("te", StringUtils.substring("test", 2));
		Assert.assertEquals("test", StringUtils.substring("test", 10));
	}

	@Test
	public void TestStrEquals() {
		Assert.assertEquals(false, StringUtils.strEquals(null, "test"));
		Assert.assertEquals(true, StringUtils.strEquals(null, null));
		Assert.assertEquals(true, StringUtils.strEquals("test", "test"));
	}

	@Test
	public void TestSafeTruncate() {
		Assert.assertEquals("te", StringUtils.safeTruncate("test", 2));
		Assert.assertEquals("test", StringUtils.safeTruncate("test", 10));
	}
	@Test
	public void TestEqual() {
		Assert.assertEquals(false, StringUtils.equal(null, "test"));
		Assert.assertEquals(true, StringUtils.equal(null, null));
		Assert.assertEquals(true, StringUtils.equal("test", "test"));

	}
	@Test
	public void TestNormalize() {
		Assert.assertEquals(StringUtils.EMPTY, StringUtils.normalize(null));
		Assert.assertEquals(StringUtils.EMPTY, StringUtils.normalize(""));
		Assert.assertEquals(StringUtils.EMPTY, StringUtils.normalize("   "));
		Assert.assertEquals("test", StringUtils.normalize("  test "));


	}
}
