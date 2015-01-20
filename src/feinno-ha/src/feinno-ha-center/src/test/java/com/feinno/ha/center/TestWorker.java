package com.feinno.ha.center;

import com.feinno.ha.Genesis;

public class TestWorker {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// testBaseCenter.properties
		int centerPort = 8111;
		args = new String[] { "-agent", "tcp://127.0.0.1:" + centerPort, "-ports",
				"rpc=8080;http=8005;monitor=8007;rpc_duplex=8111" };
		Genesis.main(args);

	}

}
