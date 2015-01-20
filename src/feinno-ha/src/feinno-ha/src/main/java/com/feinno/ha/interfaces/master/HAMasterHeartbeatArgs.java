package com.feinno.ha.interfaces.master;

import java.util.List;

import com.feinno.ha.interfaces.monitor.HAMonitorValuePair;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class HAMasterHeartbeatArgs extends ProtoEntity
{
	@ProtoMember(1)
	private String masterStatus;

	@ProtoMember(2)
	private String masterStatusEx;

	@ProtoMember(3)
	private List<HAMonitorValuePair> machineValues;

	@ProtoMember(4)
	private List<HAMasterWorkerStatus> workerStatus;

	public String getMasterStatus()
	{
		return masterStatus;
	}

	public void setMasterStatus(String masterStatus)
	{
		this.masterStatus = masterStatus;
	}

	public String getMasterStatusEx()
	{
		return masterStatusEx;
	}

	public void setMasterStatusEx(String masterStatusEx)
	{
		this.masterStatusEx = masterStatusEx;
	}

	public List<HAMonitorValuePair> getMachineValues()
	{
		return machineValues;
	}

	public void setMachineValues(List<HAMonitorValuePair> machineValues)
	{
		this.machineValues = machineValues;
	}

	public List<HAMasterWorkerStatus> getWorkerStatus()
	{
		return workerStatus;
	}

	public void setWorkerStatus(List<HAMasterWorkerStatus> workerStatus)
	{
		this.workerStatus = workerStatus;
	}
}
