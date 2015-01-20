/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-9
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

import java.io.FileInputStream;
import java.util.Hashtable;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.util.ConfigBean;
import com.feinno.util.PropertiesUtil;

/**
 * AppEngine的配置文件
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineSettings
{
	private static Logger LOGGER = LoggerFactory.getLogger(AppEngineSettings.class);
	public static final String Xenon = "Xenon";
	
	private String extension;
	private boolean debugMode;
	private String zkHosts;
	private String currentSite;

	
	// 其他Site ZK地址信息
	private String otherZkHosts; // 格式，如果siteB机器zk1,zk2,zk3；siteC机器zk4,zk5,zk6; 数据格式为zk1,zk2,zk3|zk4,zk5,zk6中间用'|'做site分割
	
	// 远程调用是否强制走本Site
	private boolean isGlobal = false;

	// 是否添加FAE测试标示
	private String faeTestMark;

	// 是否通过Rpc方式访问DAL再访问数据库
	private String dbOverRpc = "0";
	// 格式：192.169.1.1：9999;192.168.1.2:9889 ...
	private String dalHosts;
	private boolean dalClientNew = false;
	
	/*useXenon=IICUPDB.1;IICUPDB.2;
	useXenon=IICUPDB;
	useXenon=IICUPDB.*;GRPDB.*;
	useXenon=ALL; or NONE */
	private String useXenon;
	private Hashtable<String,String> useXenonMap = new Hashtable<String,String>();
	
	private int workerThreads;
	private int requestQueueSize;
	private int maxSessions;
	
	
	public AppEngineSettings()
	{
		// TODO: 待调整
		workerThreads = 8 * 4;					// 默认按照8核CPU计算，每核CPU*8线程
		requestQueueSize = workerThreads * 32; 	// 每个线程允许32个等待
		maxSessions = workerThreads * 8; 		// 同时8任务
	}
	public int getWorkerThreads()
	{
		return workerThreads;
	}

	public void setWorkerThreads(int workerThreads)
	{
		this.workerThreads = workerThreads;
	}

	public int getRequestQueueSize()
	{
		return requestQueueSize;
	}

	public void setRequestQueueSize(int requestQueueSize)
	{
		this.requestQueueSize = requestQueueSize;
	}

	public int getMaxSessions()
	{
		return maxSessions;
	}

	public void setMaxSessions(int maxSessions)
	{
		this.maxSessions = maxSessions;
	}

	public String getDalHosts()
	{
		return dalHosts;
	}

	public void setDalHosts(String dalHosts)
	{
		this.dalHosts = dalHosts;
	}

	public String getDbOverRpc()
	{
		return dbOverRpc;
	}

	public void setDbOverRpc(String dbOverRpc)
	{
		this.dbOverRpc = dbOverRpc;
		LOGGER.warn("setDbOverRpc:"+dbOverRpc);
	}

	public String getFaeTestMark()
	{
		return faeTestMark;
	}

	public void setFaeTestMark(String faeTestMark)
	{
		this.faeTestMark = faeTestMark;
	}

	public String getCurrentSite()
	{
		return currentSite;
	}

	public void setCurrentSite(String currentSite)
	{
		this.currentSite = currentSite;
	}

	public String getExtension()
	{
		return extension;
	}

	public void setExtension(String extension)
	{
		this.extension = extension;
	}

	public boolean isDebugMode()
	{
		return debugMode;
	}

	public void setDebugMode(boolean debugMode)
	{
		this.debugMode = debugMode;
	}

	public String getZkHosts()
	{
		return zkHosts;
	}

	public void setZkHosts(String zkHosts)
	{
		this.zkHosts = zkHosts;
	}
	
	public String getOtherZkHosts() {
		return otherZkHosts;
	}
	public void setOtherZkHosts(String otherZkHosts) {
		this.otherZkHosts = otherZkHosts;
	}
	
	public boolean isGlobal() {
		return isGlobal;
	}
	public void setGlobal(boolean isGlobal) {
		this.isGlobal = isGlobal;
	}
	public String getUseXenon() {
		return useXenon;
	}
	public void setUseXenon(String useXenon) {
		this.useXenon = useXenon;
		useXenonMap = new Hashtable<String,String>();
		if(!com.feinno.util.StringUtils.isNullOrEmpty(useXenon))
		{
			String[] dbs = useXenon.split(";");
			for(String db : dbs)
			{
				if(!com.feinno.util.StringUtils.isNullOrEmpty(db))
					useXenonMap.put(db, db);
			}
		}
	}
	
		public boolean userXenon(String dbName)
	{
		if(useXenonMap==null || useXenonMap.size() == 0)
			return false;
		//NONE
		if(useXenonMap.containsKey("NONE"))
			return false;
		//ALL
		if(useXenonMap.containsKey("ALL"))
			return true;
		//UDBD.1
		if(useXenonMap.containsKey(dbName))
			return true;
		//UPDB.* 或者 UPDB
		//String[] temp = dbName.split("\\.");
		String name = dbName;
		int index = dbName.lastIndexOf('.');
		if(index > 0)
			name = dbName.substring(0, index);		
		//兼容IICUPDB.Password.1
		
		String dbAll = name + ".*";
		if(useXenonMap.containsKey(dbAll) || useXenonMap.containsKey(name))
		{
			LOGGER.info(name + " use Xenon-proxy");
			return true;
		}
		else
			LOGGER.info(name + " not use Xenon-proxy");

		return false;
	}
	
	public static void main(String[] args) throws Exception
	{
		
		//String useXenon = "IICUPDB.*";
		String useXenon = "IICUPDB.7;GRPDB.7";
		
		AppEngineSettings settings = new AppEngineSettings();
		settings.setUseXenon(useXenon);
		settings.setUseXenon(settings.getUseXenon());
		
		System.out.println("IICUPDB.7 :" + settings.userXenon("IICUPDB.7"));
		
		System.out.println("GRPDB.7 :" + settings.userXenon("GRPDB.7"));
		
		System.out.println("IICUPDB.Password.3 :" + settings.userXenon("IICUPDB.Password.3"));

		System.exit(0);
	}
	public boolean isDalClientNew() {
		return dalClientNew;
	}
	public void setDalClientNew(boolean dalClientNew) {
		this.dalClientNew = dalClientNew;
	}
	
}
