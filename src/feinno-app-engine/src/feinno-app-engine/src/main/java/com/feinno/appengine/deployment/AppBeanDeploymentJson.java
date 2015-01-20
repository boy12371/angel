/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-8-30
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.appengine.deployment;

import com.google.gson.JsonObject;

/**
 * 
 * 需要传给客户端的Json格式数据
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppBeanDeploymentJson {
	private int beanId;
	private String categoryMinusName;
	private int packageId;
	private String packageName;
	private String grayFactors;
	private String serviceDeployName;
	private String serverGroups;
	private String localSites;
	private boolean enabled;
	private String hotServerName;
	private String statusText; // 部署中... 3->2/3(Core)
	private String warnColor;
	private String status; // RUNNING, MODIFIED, DEPLOYING
	private String runningInfos; // 失败
	private String beanType; // AppBean类型，例如Sipc、Remote
	private String version; // 版本号

	public int getBeanId() {
		return beanId;
	}

	public void setBeanId(int beanId) {
		this.beanId = beanId;
	}

	public String getCategoryMinusName() {
		return categoryMinusName;
	}

	public void setCategoryMinusName(String categoryMinusName) {
		this.categoryMinusName = categoryMinusName;
	}

	public int getPackageId() {
		return packageId;
	}

	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getGrayFactors() {
		return grayFactors;
	}

	public void setGrayFactors(String grayFactors) {
		this.grayFactors = grayFactors;
	}

	public String getServiceDeployName() {
		return serviceDeployName;
	}

	public void setServiceDeployName(String serviceDeployName) {
		this.serviceDeployName = serviceDeployName;
	}

	public String getServerGroups() {
		return serverGroups;
	}

	public void setServerGroups(String serverGroups) {
		this.serverGroups = serverGroups;
	}

	public String getLocalSites() {
		return localSites;
	}

	public void setLocalSites(String localSites) {
		this.localSites = localSites;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getHotServerName() {
		return hotServerName;
	}

	public void setHotServerName(String hotServerName) {
		this.hotServerName = hotServerName;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRunningInfos() {
		return runningInfos;
	}

	public void setRunningInfos(String runningInfos) {
		this.runningInfos = runningInfos;
	}

	public JsonObject toJsonObject() {

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("BeanId", beanId);
		jsonObject.addProperty("CategoryName", categoryMinusName);
		jsonObject.addProperty("PackageId", packageId);
		jsonObject.addProperty("PackageName", packageName);
		jsonObject.addProperty("ServerGroup", serverGroups);
		jsonObject.addProperty("GrayFactors", grayFactors);
		jsonObject.addProperty("Enabled", enabled);
		jsonObject.addProperty("LocalSites", localSites);
		jsonObject.addProperty("DynamicServer", hotServerName);
		jsonObject.addProperty("ServiceName", serviceDeployName);
		jsonObject.addProperty("Status", status);
		jsonObject.addProperty("warnColor", warnColor == null ? "" : warnColor);
		jsonObject.addProperty("StatusText", statusText);
		jsonObject.addProperty("RunningInfos", runningInfos);
		jsonObject.addProperty("BeanType", beanType != null ? beanType : "UNKNOW");
		jsonObject.addProperty("Version", version != null ? version : "UNKNOW");

		return jsonObject;
	}

	public String toString() {
		return toJsonObject().toString();
	}

	public String getWarnColor() {
		return warnColor;
	}

	public void setWarnColor(String warnColor) {
		this.warnColor = warnColor;
	}

	public String getBeanType() {
		return beanType;
	}

	public void setBeanType(String beanType) {
		if (beanType == null) {
			return;
		} else if (beanType.equals("com.feinno.appengine.rpc.RemoteAppBean")) {
			this.beanType = "Remote";
		} else if (beanType.equals("com.feinno.appengine.sipc.SipcAppBean")) {
			this.beanType = "Sipc";
		} else if (beanType.equals("com.feinno.appengine.http.HttpAppBean")) {
			this.beanType = "Http";
		} else if (beanType.equals("com.feinno.appengine.job.JobAppBean")) {
			this.beanType = "Job";
		} else if (beanType.equals("com.feinno.appengine.mcp.McpAppBean")) {
			this.beanType = "Mcp";
		} else if (beanType.equals("com.feinno.appengine.sms.SmsAppBean")) {
			this.beanType = "Sms";
		} else {
			this.beanType = "UNKNOW";
		}

	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
