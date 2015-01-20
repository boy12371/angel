package com.feinno.diagnostic.perfmon.monitor.rpc;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.diagnostic.observation.ObserverReportEntity;
import com.feinno.diagnostic.observation.ObserverReportRowBean;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.client.RpcProxyFactory;
import com.feinno.serialization.protobuf.types.ProtoString;

public class ClientTools {
	private String ip;
	private String cookie;
	private MonitorService monService = null;

	public ClientTools(String ip) {
		if (ip.contains("tcp://")) {
			this.ip = ip;
		} else {
			this.ip = "tcp://" + ip;
		}
		this.cookie = UUID.randomUUID().toString();
	}

	public void Connect() {
		RpcTcpEndpoint tcpEp = RpcTcpEndpoint.parse(this.ip);
		monService = RpcProxyFactory.getService(tcpEp, MonitorService.class);
		System.out.println("Connection ip'" + this.ip + "' Service'"
				+ MonitorService.class.getSimpleName() + "' successful.");
	}

	public void getCategoryList() {
		MonitorCategoryEntity[] mCategoryEntityList = monService
				.getCategoryList();
		System.out.println("List Category:");
		for (MonitorCategoryEntity mce : mCategoryEntityList) {
			System.out.println(">>>>>>>>>>" + mce.getName());
		}
	}

	public void subscribe(String name, int time) {
		MonitorRequestArgs args = new MonitorRequestArgs(name, "all", time,
				cookie);
		int re = monService.subscribe(args);
		if (re == 200) {
			System.out.println("subscribe " + name + " successful.");
			while (true) {
				pull();
				try {
					Thread.sleep(time * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else if (re == 404) {
			System.out.println("failed. can not subscribe " + name + ".");
		}
	}

	public void pull() {
		ObserverReportEntity[] oReportEntity = monService.pull(new ProtoString(
				cookie));
		if (oReportEntity == null || oReportEntity.length == 0) {
			return;
		}
		for (ObserverReportEntity entity : oReportEntity) {
			if (entity == null || entity.getRows().size() == 0) {
				return;
			}
//			int i = 10;
//			while (i-->0) {
//				System.out.println("");
//			}
			String category = entity.getCategory();
			String time = new SimpleDateFormat("yyyy-MM-dd H:m:s")
					.format(entity.getTime());
			System.out.println(">>>>>>>>>>" + category + "<<<<<<<<<<" + time);
			System.out.println();
			List<ObserverReportRowBean> beans = entity.getRows();
			List<ObserverReportColumn> columns = entity.getColumns();
			System.out.print(formatInstance("instance"));
			for (ObserverReportColumn column : columns) {
				System.out.print(formatString(column.getName()));
			}
			System.out.println();
			for (ObserverReportRowBean bean : beans) {
				System.out.print(formatInstance(bean.getInstance()));
				for (String data : bean.getData()) {
					System.out.print(formatString(data));
				}
				System.out.println();
			}
		}
	}

	private String formatInstance(String str) {
		if (str.length() > 15) {
			return String.format("%1$-27.27S", str) + "...   ";
		} else {
			return String.format("%1$-30.30S", str) + "   ";
		}
	}

	private String formatString(String str) {
		if (str.length() > 15) {
			return String.format("%1$-17.17S", str) + "... ";
		} else {
			return String.format("%1$-20.20S", str) + " ";
		}
	}
}
