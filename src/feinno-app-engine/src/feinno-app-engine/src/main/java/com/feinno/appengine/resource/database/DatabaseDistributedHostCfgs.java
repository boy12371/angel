package com.feinno.appengine.resource.database;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;

import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.google.gson.Gson;

/**
 * 
 * 
 * @author Li.Hongbo <lihongbo@feinno.com>
 * @date 2014年11月17日 下午5:37:38
 */
public class DatabaseDistributedHostCfgs {
	private Map<String, RpcTcpEndpoint> endpoints = new ConcurrentHashMap<>();
	private Map<String, Integer> dbTimeOut = new ConcurrentHashMap<>();
	private RpcTcpEndpoint defaultEp = null;
	public static final int DEFAULT_TIMEOUT_SECOND = 10;
	private int defalutTimeout = 10;

	public Map<String, RpcTcpEndpoint> getEndpoints() {
		return endpoints;
	}

	public RpcTcpEndpoint getRpcTcpEndpoint(String dbname) {
		RpcTcpEndpoint tmp = endpoints.get(dbname);
		if (tmp == null)
			tmp = getDefaultEp();
		return tmp;
	}

	public void setEndpoints(Map<String, RpcTcpEndpoint> endpoints) {
		this.endpoints = endpoints;
	}

	public RpcTcpEndpoint getDefaultEp() {
		return defaultEp;
	}

	public void setDefaultEp(RpcTcpEndpoint defaultEp) {
		this.defaultEp = defaultEp;
	}

	public void setEndpoints(Logger LOGGER, RpcTcpEndpoint endpoint,
			String... dbnames) {
		for (String db : dbnames) {
			if (db != null && !db.trim().equals(""))
				this.endpoints.put(db, endpoint);
			else {
				LOGGER.error(String.format("dbname is empty dbname =%s ep=%s",
						new Gson().toJson(dbnames), endpoint.toString()));
			}

		}
	}

	public void setTimeOuts(Logger LOGGER, int timeoutSecond, String... dbnames) {
		for (String db : dbnames) {
			if (db != null && !db.trim().equals(""))
				this.dbTimeOut.put(db, timeoutSecond);

		}
	}

	public int getTimeOut(String dbname) {
		
		Integer tmp = dbTimeOut.get(dbname);
		if(tmp ==null) tmp =0;
		if ( tmp <= 0)
			tmp = defalutTimeout > 0 ? defalutTimeout : DEFAULT_TIMEOUT_SECOND;

		return tmp;
	}

	public Map<String, Integer> getDbTimeOut() {
		return dbTimeOut;
	}

	public void setDbTimeOut(Map<String, Integer> dbTimeOut) {
		this.dbTimeOut = dbTimeOut;
	}

	public int getDefalutTimeout() {
		return defalutTimeout;
	}

	public void setDefalutTimeout(int defalutTimeout) {
		this.defalutTimeout = defalutTimeout;
	}

}
