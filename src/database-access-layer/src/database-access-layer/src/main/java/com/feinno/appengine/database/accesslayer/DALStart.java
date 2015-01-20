package com.feinno.appengine.database.accesslayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.ServiceSettings;

/**
 * Hello world!
 *
 */
public class DALStart 
{
	public static final Logger logger = LoggerFactory.getLogger(DALHost.class);

	private static DALHost host;
	

	public static void main(String[] args)
	{
		try {
			ServiceSettings.INSTANCE.init();
			host = new DALHost();
			host.start();
			//channel.stop导致CPU 100%
			Thread.sleep(60*1000);
		    stop();
		} catch (Exception e) {
			logger.error("local server startup failed. DAL is shutting down", e);
			stop();
		}

	}

	public static void stop()
	{
		try {
			if (host != null)
				host.stop();
		} catch (Exception e1) {
			logger.error("local server stop failed. DAL is shutting down", e1);
		}
	}

	private static void printUsage()
	{
		System.out.println("Usage:");
		System.out.println("\t\t xxx -ha:<port> -workername:xxx");
		System.out.println("\tor\t xxx -local:settings.properties ");
	}
}
