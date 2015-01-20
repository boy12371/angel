package com.feinno.appengine.database.accesslayer;

import com.feinno.imps.configuration.ConfigFile;

public class DALConfiguration extends ConfigFile {

	static DALConfiguration instance = new DALConfiguration();
	
	protected DALConfiguration() {
		super("DAL.properties");
	}
	
	public static IntConfigKey EXECUTER_SIZE = new IntConfigKey(instance,"size", 512);
	
	public static IntConfigKey EXECUTER_LIMIT = new IntConfigKey(instance, "limit",2048);

}
