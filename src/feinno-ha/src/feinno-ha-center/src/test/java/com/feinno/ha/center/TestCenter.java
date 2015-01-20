package com.feinno.ha.center;

import com.feinno.ha.ServiceSettings;

public class TestCenter {

	public static void startCenter(String[] args) throws Exception {
		ServiceSettings.INSTANCE.loadFromXml();
		ServiceSettings.INSTANCE.assignOpts(args);
		ServiceSettings.INSTANCE.verifyConfig();
		new com.feinno.ha.center.CenterServiceComponent().start();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		args = new String[] { "-ports", "rpc_duplex=8111;rpc=8112;monitor=8113" };
		startCenter(args);

	}

}
