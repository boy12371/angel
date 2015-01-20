package com.feinno.logging.configuration;

import java.util.HashMap;
import java.util.Map;

import com.feinno.util.ConfigBean;

public class SubLogger extends ConfigBean {

	private String key;
	private String level;
	private Map<String, FilterSetting> filter = new HashMap<String, FilterSetting>();

	public final String getKey() {
		return key;
	}

	public final String getLevel() {
		return level;
	}

	public final Map<String, FilterSetting> getFilter() {
		return filter;
	}

}
