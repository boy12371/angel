package test.com.feinno.configuration;

import org.junit.Test;

import junit.framework.Assert;

import com.feinno.configuration.ConfigParams;

public class TestConfigArgs {

	@Test
	public void testConfigArgs() {
		ConfigParams args = new ConfigParams(
				"computer=IIC-IGS-01;service=IBS;app=core:GetUserInfo;");
		Assert.assertEquals(3, args.size());
		String actual = args.toString();
		Assert.assertEquals(
				"computer=IIC-IGS-01;service=IBS;app=core:GetUserInfo", actual);

		ConfigParams argss = new ConfigParams(args);
		ConfigParams rt = argss.merge(args, false);
		org.junit.Assert.assertTrue(rt != null);
	}

}
