package com.feinno.appengine.rpc;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class GetRouterArgs extends ProtoEntity {
	
	@ProtoMember(1)
	private String categoryMinusName;
	
	@ProtoMember(2)
	private String contextUri;
	
	@ProtoMember(3)
	private String fromVersion;
	

	public String getCategoryMinusName() {
		return categoryMinusName;
	}

	public void setCategoryMinusName(String categoryMinusName) {
		this.categoryMinusName = categoryMinusName;
	}

	public String getContextUri() {
		return contextUri;
	}

	public void setContextUri(String contextUri) {
		this.contextUri = contextUri;
	}

	public String getFromVersion() {
		return fromVersion;
	}

	public void setFromVersion(String fromVersion) {
		this.fromVersion = fromVersion;
	}

	
}

