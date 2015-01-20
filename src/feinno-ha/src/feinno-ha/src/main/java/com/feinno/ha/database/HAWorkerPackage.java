package com.feinno.ha.database;

import java.util.Date;

/**
 * 
 * HA_WorkerPackage实体类
 * 
 * @author lvmingwei
 * 
 */
public class HAWorkerPackage {
	private int packageId;
	private String packageFile;
	private Date uploadTimedatetime;
	private String uploadUser;
	private String comment;
	private String serviceName;
	private String serviceVersion;
	private boolean controllable;
	private String servicePorts;
	private String metastext;
	private String startParamsvarchar;
	private int instances;
	private int maxMemory;
	private int minMemory;

	public int getPackageId() {
		return packageId;
	}

	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	public String getPackageFile() {
		return packageFile;
	}

	public void setPackageFile(String packageFile) {
		this.packageFile = packageFile;
	}

	public Date getUploadTimedatetime() {
		return uploadTimedatetime;
	}

	public void setUploadTimedatetime(Date uploadTimedatetime) {
		this.uploadTimedatetime = uploadTimedatetime;
	}

	public String getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceVersion() {
		return serviceVersion;
	}

	public void setServiceVersion(String serviceVersion) {
		this.serviceVersion = serviceVersion;
	}

	public boolean isControllable() {
		return controllable;
	}

	public void setControllable(boolean controllable) {
		this.controllable = controllable;
	}

	public String getServicePorts() {
		return servicePorts;
	}

	public void setServicePorts(String servicePorts) {
		this.servicePorts = servicePorts;
	}

	public String getMetastext() {
		return metastext;
	}

	public void setMetastext(String metastext) {
		this.metastext = metastext;
	}

	public String getStartParamsvarchar() {
		return startParamsvarchar;
	}

	public void setStartParamsvarchar(String startParamsvarchar) {
		this.startParamsvarchar = startParamsvarchar;
	}

	public int getInstances() {
		return instances;
	}

	public void setInstances(int instances) {
		this.instances = instances;
	}

	public int getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(int maxMemory) {
		this.maxMemory = maxMemory;
	}

	public int getMinMemory() {
		return minMemory;
	}

	public void setMinMemory(int minMemory) {
		this.minMemory = minMemory;
	}

}
