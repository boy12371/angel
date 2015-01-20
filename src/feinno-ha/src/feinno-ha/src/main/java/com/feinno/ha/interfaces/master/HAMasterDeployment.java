package com.feinno.ha.interfaces.master;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class HAMasterDeployment extends ProtoEntity {
	@ProtoMember(1)
	private String serviceDeployName;

	@ProtoMember(2)
	private int packageId;

	@ProtoMember(3)
	private String packageUrl;

	@ProtoMember(4)
	private String serviceVersion;

	@ProtoMember(5)
	private boolean controllable;

	@ProtoMember(6)
	private String workerUpdateMode;

	@ProtoMember(7)
	private String workerStartMode;

	@ProtoMember(8)
	private String servicePorts;

	@ProtoMember(9)
	private String portsSettings;

	@ProtoMember(10)
	private String startParams;

	@ProtoMember(11)
	private String settings;

	@ProtoMember(12)
	private String serverGroup;

	public String getServiceDeployName() {
		return serviceDeployName;
	}

	public void setServiceDeployName(String serviceDeployName) {
		this.serviceDeployName = serviceDeployName;
	}

	public int getPackageId() {
		return packageId;
	}

	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	public String getPackageUrl() {
		return packageUrl;
	}

	public void setPackageUrl(String packageUrl) {
		this.packageUrl = packageUrl;
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

	public String getWorkerUpdateMode() {
		return workerUpdateMode;
	}

	public void setWorkerUpdateMode(String workerUpdateMode) {
		this.workerUpdateMode = workerUpdateMode;
	}

	public String getWorkerStartMode() {
		return workerStartMode;
	}

	public void setWorkerStartMode(String workerStartMode) {
		this.workerStartMode = workerStartMode;
	}

	public String getServicePorts() {
		return servicePorts;
	}

	public void setServicePorts(String servicePorts) {
		this.servicePorts = servicePorts;
	}

	public String getPortsSettings() {
		return portsSettings;
	}

	public void setPortsSettings(String portsSettings) {
		this.portsSettings = portsSettings;
	}

	public String getStartParams() {
		return startParams;
	}

	public void setStartParams(String startParams) {
		this.startParams = startParams;
	}

	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

	public String getServerGroup() {
		return serverGroup;
	}

	public void setServerGroup(String serverGroup) {
		this.serverGroup = serverGroup;
	}

}
