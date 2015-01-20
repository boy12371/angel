package com.feinno.diagnostic.perfmon.monitor.rpc;

public class Perfmon {
	// perfmon (tcp://)192.168.1.100:6000
	// "tcp://127.0.0.1:8081"
	public static void main(String[] args) {
		String ip = args[0];
		ClientTools clientTools = new ClientTools(ip);
		clientTools.Connect();
		if (args.length == 1) {
			clientTools.getCategoryList();
		} else if (args.length >= 2) {
			String name = args[1];
			int time = 5;
			if (args.length == 3) {
				time = Integer.parseInt(args[2]);
			}
			clientTools.subscribe(name, time);

		}
		System.exit(0);
	}
}
