/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei May 14, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.monitor;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * 监控指标
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAMonitorKey extends ProtoEntity
{
	
	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public int getScriptResultOrder() {
		return scriptResultOrder;
	}

	public void setScriptResultOrder(int scriptResultOrder) {
		this.scriptResultOrder = scriptResultOrder;
	}

	@ProtoMember(1)
	private int keyId;
	
	@ProtoMember(2)
	private String keyType;
	
	@ProtoMember(3)
	private String scriptName;
	
	@ProtoMember(4)
	private int scriptResultOrder;
}
