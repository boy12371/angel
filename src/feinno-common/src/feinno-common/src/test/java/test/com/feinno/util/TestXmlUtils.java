package test.com.feinno.util;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.util.XmlUtils;

public class TestXmlUtils {
	@Test
	public void TestDecode() {
		Assert.assertEquals("<>\"&'\r\n",
				XmlUtils.decode("&lt;&gt;&quot;&amp;&apos;&#xd;&#xa;"));
	}

	@Test
	public void TestEncode() {
		Assert.assertEquals(null, XmlUtils.encode(null));
		Assert.assertEquals("  ", XmlUtils.encode("  "));
		Assert.assertEquals("Test&lt;&gt;&quot;&amp;&apos;&#xd;&#xa;",
				XmlUtils.encode("Test<>\"&'\r\n"));
		Assert.assertEquals("&#x8;", XmlUtils.encode("\b", false));
		Assert.assertEquals("", XmlUtils.encode("\b"));
		Assert.assertEquals("Test", XmlUtils.encode("\bTest"));
	}
}
