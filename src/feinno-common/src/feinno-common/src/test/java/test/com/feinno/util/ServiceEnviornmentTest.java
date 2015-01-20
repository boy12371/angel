package test.com.feinno.util;

import org.junit.Assert;
import org.junit.Test;

import com.feinno.util.ServiceEnviornment;

public class ServiceEnviornmentTest {

	@Test
	public void basicFunctionalTest(){
		Assert.assertNotNull("serviceName assert", ServiceEnviornment.getServiceName());
	}
}
