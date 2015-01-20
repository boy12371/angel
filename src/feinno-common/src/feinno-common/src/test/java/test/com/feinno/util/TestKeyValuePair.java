package test.com.feinno.util;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.util.KeyValuePair;

public class TestKeyValuePair {
	@Test
	public void TestKeyValuePairParameter() {
		KeyValuePair<Integer, String> keyV = new KeyValuePair<Integer, String>(
				1, "test");
		Assert.assertEquals(1, (int) keyV.getKey());
		Assert.assertEquals("test", (String) keyV.getValue());

	}
	@Test
	public void TestKeyValuePair() {
		KeyValuePair<Integer, String> keyV = new KeyValuePair<Integer, String>();
		keyV.setKey(1);
		keyV.setValue("test");
		Assert.assertEquals(1, (int) keyV.getKey());
		Assert.assertEquals("test", (String) keyV.getValue());

	}
}
