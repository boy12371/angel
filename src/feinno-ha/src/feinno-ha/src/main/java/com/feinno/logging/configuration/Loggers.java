package com.feinno.logging.configuration;

import java.util.HashMap;
import java.util.Map;

import com.feinno.util.ConfigBean;

public class Loggers extends ConfigBean {
	
	private Map<String, SubLogger> logger = new HashMap<String, SubLogger>();

	public Map<String, SubLogger> getLogger() {
		return logger;
	}

}
