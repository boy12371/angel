package com.feinno.ha.interfaces.master;

import java.util.Date;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class HAMasterRegisterArgs extends ProtoEntity {
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getMasterVersion() {
		return masterVersion;
	}

	public void setMasterVersion(String masterVersion) {
		this.masterVersion = masterVersion;
	}

	public Date getMasterStartTime() {
		return masterStartTime;
	}

	public void setMasterStartTime(Date masterStartTime) {
		this.masterStartTime = masterStartTime;
	}

	public int getMasterPid() {
		return masterPid;
	}

	public void setMasterPid(int masterPid) {
		this.masterPid = masterPid;
	}

	@ProtoMember(1)
	private String serverName;

	@ProtoMember(2)
	private String masterVersion;

	@ProtoMember(value = 3, timezone = "UTC")
	private Date masterStartTime;

	@ProtoMember(4)
	private int masterPid;
}
