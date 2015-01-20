package test.com.feinno.util;

import org.junit.Test;

import com.feinno.util.ServiceEnviornment;

public class TestServiceEnviornment
{
	@Test
	public void Test()
	{
		System.out.println("getServiceName:" + ServiceEnviornment.getServiceName());
		System.out.println("getComputerName:" + ServiceEnviornment.getComputerName());
		ServiceEnviornment.setComputerName(ServiceEnviornment.getComputerName());
		ServiceEnviornment.setServiceName(ServiceEnviornment.getServiceName());
	}
}
