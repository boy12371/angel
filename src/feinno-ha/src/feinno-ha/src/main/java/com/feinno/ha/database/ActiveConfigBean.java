package com.feinno.ha.database;

import java.util.Date;

import com.feinno.configuration.ConfigType;

/**
 * 
 * <b>描述: </b>该类是HA_ActiveConfig表的实体Bean
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class ActiveConfigBean {

	private String serverName;

	private int workerPid;

	private ConfigType configtype;

	private String configKey;

	private String configParams;

	private Date lastReadTime;

	private Date lastVersion;

	private boolean isSubscribed;

	private String lastError;

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getWorkerPid() {
		return workerPid;
	}

	public void setWorkerPid(int workerPid) {
		this.workerPid = workerPid;
	}

	public ConfigType getConfigtype() {
		return configtype;
	}

	public void setConfigtype(ConfigType configtype) {
		this.configtype = configtype;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigParams() {
		return configParams;
	}

	public void setConfigParams(String configParams) {
		this.configParams = configParams;
	}

	public Date getLastReadTime() {
		return lastReadTime;
	}

	public void setLastReadTime(Date lastReadTime) {
		this.lastReadTime = lastReadTime;
	}

	public Date getLastVersion() {
		return lastVersion;
	}

	public void setLastVersion(Date lastVersion) {
		this.lastVersion = lastVersion;
	}

	public boolean isSubscribed() {
		return isSubscribed;
	}

	public void setSubscribed(boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
	}

	public String getLastError() {
		return lastError;
	}

	public void setLastError(String lastError) {
		this.lastError = lastError;
	}
}
