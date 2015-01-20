/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-7-26
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.center;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAPackageInfo extends ProtoEntity {
	@ProtoMember(1)
	private int packageId;

	@ProtoMember(2)
	private String packageUrl;

	@ProtoMember(3)
	private String serviceVersion;

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

}
