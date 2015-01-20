package test.com.feinno.util;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.util.NumberUtils;

public class TestNumberUtils {
	@Test
	public void TestNextPower2() {
		Assert.assertEquals(32, NumberUtils.NextPower2(20));
		try {
			NumberUtils.NextPower2(Integer.MAX_VALUE);
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
	}

	@Test
	public void TestByteArrayToInt() {
		Assert.assertEquals(100,
				NumberUtils.byteArrayToInt(NumberUtils.intToByteArray(100)));
	}

	@Test
	public void TestByteArrayToShort() {
		Assert.assertEquals(100, NumberUtils.byteArrayToShort(NumberUtils
				.shortToByteArray((short) 100)));
	}

	@Test
	public void TestTraceHexString() {
		NumberUtils.traceHexString(NumberUtils.intToByteArray(100));
	}

	@Test
	public void TestBooleanToLong() {
		Assert.assertEquals(1L, NumberUtils.booleanToLong(true));
		Assert.assertEquals(0L, NumberUtils.booleanToLong(false));
	}

	@Test
	public void TestBooleanToInt() {
		Assert.assertEquals(1, NumberUtils.booleanToInt(true));
		Assert.assertEquals(0, NumberUtils.booleanToInt(false));
	}

	@Test
	public void TestByteArrayToInt32() {
		Assert.assertEquals(100, NumberUtils.byteArrayToInt32(
				NumberUtils.intToByteArray(100), 0));
	}

	@Test
	public void TestByteArrayToInt16() {
		Assert.assertEquals(0, NumberUtils.byteArrayToInt16(
				NumberUtils.intToByteArray(100), 0));
	}
	@Test
	public void TestFillByteBufferWithInt() {
		NumberUtils.fillByteBufferWithInt32(1, NumberUtils.intToByteArray(100), 0);
		NumberUtils.fillByteBufferWithInt16((short)1, NumberUtils.intToByteArray(100), 0);
	}
}
