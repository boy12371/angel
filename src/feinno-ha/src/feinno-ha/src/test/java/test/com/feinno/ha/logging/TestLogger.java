/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-10-25
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.ha.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.feinno.logging.spi.MDC;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class TestLogger
{
	private static final Logger logger = LoggerFactory.getLogger(TestLogger.class);
	
	public static void main(String[] args)
	{
//		MDC.put("123", "223");
		logger.info("Save");
	}
}
