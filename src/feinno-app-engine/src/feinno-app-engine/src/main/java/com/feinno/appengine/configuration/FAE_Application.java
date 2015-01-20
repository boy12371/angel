/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-3-9
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.configuration;

import java.nio.charset.Charset;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.rpc.RemoteAppBeanProxy;
import com.feinno.appengine.runtime.configuration.ApplicationEntity;
import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;
import com.feinno.database.DataRow;
import com.feinno.ha.ServiceSettings;
import com.feinno.util.StringUtils;

/**
 * FAE_Application 全局应用配置表
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class FAE_Application extends ConfigTableItem {
	@ConfigTableField(value = "BeanId", isKeyField = true)
	private int beanId;

	@ConfigTableField(value = "PackageId")
	private int packageId;

	@ConfigTableField("AppCategory")
	private String appCategory;

	@ConfigTableField("AppName")
	private String appName;

	@ConfigTableField("ServerGroup")
	private String serverGroup;

	@ConfigTableField("WorkerInstance")
	private String workerInstance;

	@ConfigTableField("Enabled")
	private boolean enabled;

	@ConfigTableField("Annotations")
	private String annotationsText;

	@ConfigTableField("GrayFactors")
	private String grayFactors;

	@ConfigTableField("LocalSites")
	private String localSites;

	@ConfigTableField("HotServerName")
	private String hotServerName;

	@ConfigTableField("DeployStatus")
	private AppBeanDeployStatus deployStatus;

	@ConfigTableField("ServiceDeployName")
	private String serviceDeployName;

	@ConfigTableField("ZookeeperAppId")
	private int zookeeperAppId;

	private AppBeanAnnotations annotations;

	/**
	 * 
	 * 默认构造
	 */
	public FAE_Application() {
	}

	/**
	 * 
	 * 从数据库记录中创建
	 * 
	 * @param row
	 * @throws SQLException
	 */
	public FAE_Application(DataRow row) throws SQLException {
		beanId = row.getInt("BeanId");
		packageId = row.getInt("PackageId");
		appCategory = row.getString("AppCategory");
		appName = row.getString("AppName");
		serverGroup = row.getString("ServerGroup");
		workerInstance = row.getString("WorkerInstance");
		enabled = (Boolean) row.getObject("Enabled");
		annotationsText = row.getString("Annotations");
		grayFactors = row.getString("GrayFactors");
		localSites = row.getString("LocalSites");
		hotServerName = row.getString("HotServerName");
		String deployStatusStr = row.getString("DeployStatus");
		deployStatus = !StringUtils.isNullOrEmpty(deployStatusStr) ? AppBeanDeployStatus.valueOf(deployStatusStr)
				: AppBeanDeployStatus.UNDEPLOYED;
		serviceDeployName = row.getString("ServiceDeployName");
		zookeeperAppId = row.getInt("ZookeeperAppId");
		afterLoad();
	}

	public int getBeanId() {
		return beanId;
	}

	public int getPackageId() {
		return packageId;
	}

	public String getAppCategory() {
		return appCategory;
	}

	public String getAppName() {
		return appName;
	}

	public String getServerGroup() {
		return serverGroup;
	}

	public String getWorkerInstance() {
		return workerInstance;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public AppBeanAnnotations getAnnotations() {
		return annotations;
	}

	public String getGrayFactorys() {
		return grayFactors;
	}

	public String getLocalSites() {
		return localSites;
	}

	public String getHotServerName() {
		return hotServerName;
	}

	public AppBeanDeployStatus getDeployStatus() {
		return deployStatus;
	}

	public String getServiceDeployName() {
		return serviceDeployName;
	}

	public int getZookeeperAppId() {
		return zookeeperAppId;
	}

	@Override
	public void afterLoad() {
		byte[] buf = annotationsText.getBytes(Charset.forName("UTF-8"));
		// annotations = Serializer.decode(AppBeanAnnotations.class, buf);
		// //泛型解析异常
		try {
			annotations = FAEAppAnnotationDecoder.decode(new String(buf));
		} catch (RuntimeException e) {
			LOGGER.error("FAEAppAnnotationDecoder failed, appName {}, id {} \n annotationsText : \n {}", new Object[] {
					appName, beanId, annotationsText });
			throw e;
		}
	}

	public ApplicationEntity toApplicationEntity(){
		ApplicationEntity entity = new ApplicationEntity();
		entity.setAppId(beanId);
		entity.setCategory(appCategory);
		entity.setName(appName);
		entity.setAppWorkerId(serviceDeployName);
		entity.setAnnotations(annotationsText);
		entity.setGrayFactors(grayFactors);
		entity.setSite(localSites);
		entity.setWorkerInstance(workerInstance);
		entity.setServerGroup(serverGroup);
		entity.setAppVersion(annotations.getClassInfo().getVersion());
		entity.setSite(ServiceSettings.INSTANCE.getSiteName());
		return entity;
	}
	
	private static final transient Logger LOGGER = LoggerFactory.getLogger(RemoteAppBeanProxy.class);
}
