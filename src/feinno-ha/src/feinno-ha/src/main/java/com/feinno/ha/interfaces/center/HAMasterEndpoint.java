/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Aug 3, 2012
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
public class HAMasterEndpoint extends ProtoEntity {
	@ProtoMember(1)
	private String centerUrl;

	public String getCenterUrl() {
		return centerUrl;
	}

	public void setCenterUrl(String centerUrl) {
		this.centerUrl = centerUrl;
	}

}
