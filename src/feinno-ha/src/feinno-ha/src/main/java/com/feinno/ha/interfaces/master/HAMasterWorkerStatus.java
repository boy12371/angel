package com.feinno.ha.interfaces.master;

import java.util.List;
import com.feinno.ha.interfaces.monitor.HAMonitorValuePair;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAMasterWorkerStatus extends ProtoEntity {

	@ProtoMember(1)
	private int pid;
	@ProtoMember(2)
	private String serviceName;
	@ProtoMember(3)
	private String workerMode;
	@ProtoMember(4)
	private String workerPorts;
	@ProtoMember(5)
	private String workerStatus;
	@ProtoMember(6)
	private String workerAction;
	@ProtoMember(7)
	private int packageId;
	@ProtoMember(8)
	private String packageVersion;
	@ProtoMember(9)
	private List<HAMonitorValuePair> workerValues;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getWorkerMode() {
		return workerMode;
	}

	public void setWorkerMode(String workerMode) {
		this.workerMode = workerMode;
	}

	public String getWorkerPorts() {
		return workerPorts;
	}

	public void setWorkerPorts(String workerPorts) {
		this.workerPorts = workerPorts;
	}

	public String getWorkerStatus() {
		return workerStatus;
	}

	public void setWorkerStatus(String workerStatus) {
		this.workerStatus = workerStatus;
	}

	public List<HAMonitorValuePair> getWorkerValues() {
		return workerValues;
	}

	public void setWorkerValues(List<HAMonitorValuePair> workerValues) {
		this.workerValues = workerValues;
	}

	/**
	 * @return the workerAction
	 */
	public String getWorkerAction() {
		return workerAction;
	}

	/**
	 * @param workerAction
	 *            the workerAction to set
	 */
	public void setWorkerAction(String workerAction) {
		this.workerAction = workerAction;
	}

	/**
	 * @return the packageId
	 */
	public int getPackageId() {
		return packageId;
	}

	/**
	 * @param packageId
	 *            the packageId to set
	 */
	public void setPackageId(int packageId) {
		this.packageId = packageId;
	}

	/**
	 * @return the packageVersion
	 */
	public String getPackageVersion() {
		return packageVersion;
	}

	/**
	 * @param packageVersion
	 *            the packageVersion to set
	 */
	public void setPackageVersion(String packageVersion) {
		this.packageVersion = packageVersion;
	}
}
