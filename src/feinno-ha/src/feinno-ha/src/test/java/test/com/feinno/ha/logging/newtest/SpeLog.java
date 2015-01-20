package test.com.feinno.ha.logging.newtest;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.logging.spi.LogManager;

public class SpeLog {

	private static final Logger LOGGER ;
	
	static{

        Properties configs = new Properties();
        configs.setProperty("level","info");
        configs.setProperty("cache.enabled","false");
        configs.setProperty("appenders.console.enabled","true");
        configs.setProperty("appenders.text.enabled","false");
        configs.setProperty("appenders.database.enabled","false");
        LogManager.loadSettings(configs);
        LOGGER = LoggerFactory.getLogger(SpeLog.class);
	}
	
	public static void main(String args[]){
		LOGGER.debug("debug");
		LOGGER.info("info");
		LOGGER.warn("warn");
		LOGGER.error("error");
	}

}
