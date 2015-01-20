package test.com.feinno.util;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.util.ConfigBean;

public class TestConfigBean {

	@Test
	public void TestConfigBean() {
		Properties props = new Properties();
		props.setProperty("driver", "com.mysql.jdbc.Driver");

		ConfigBean config = ConfigBean.valueOf(props, ConfigBean.class);
		Assert.assertEquals("[driver]", config.fieldKeySet().toString());
		Assert.assertEquals("[]", config.childKeySet().toString());
		Assert.assertNull(config.getFieldValue("test"));
		Assert.assertNull(config.getProperty("test"));
		Assert.assertEquals("{driver=com.mysql.jdbc.Driver}", config
				.getProperties().toString());
		Assert.assertEquals("[]", config.childValues().toString());
		Assert.assertEquals("[]", config.childValues().toString());
		Assert.assertEquals("1", config.getProperty("test", "1"));
		Assert.assertEquals("[com.mysql.jdbc.Driver]", config.fieldValues().toString());
		Assert.assertEquals("field:{driver=com.mysql.jdbc.Driver} child:{}", config.toString());
	}
}
