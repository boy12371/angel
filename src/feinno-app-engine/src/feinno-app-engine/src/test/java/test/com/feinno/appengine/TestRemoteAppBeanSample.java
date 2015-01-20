/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.appengine;

import java.io.FileInputStream;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;

import com.feinno.logging.spi.LogManager;
/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class TestRemoteAppBeanSample
{
	@Test
	public void testRemoteAppBeanSample() throws Throwable
	{
		Properties props = new Properties();
		props.load(new FileInputStream("logging.properties"));
		LogManager.loadSettings(props);
		
		RemoteAppBeanSample.Entity e = new RemoteAppBeanSample.Entity();
		e.setA(100);
		e.setB(100);
		
		RemoteAppBeanSample sample = new RemoteAppBeanSample();
		sample.load();
		
		RemoteAppBeanSample.Entity e2 = sample.processTest(e, "sess:10001", null);
		Assert.assertEquals(200, e2.getA());
	}
}
