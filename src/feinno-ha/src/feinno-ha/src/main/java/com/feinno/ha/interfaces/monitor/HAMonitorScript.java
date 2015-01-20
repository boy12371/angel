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
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAMonitorScript extends ProtoEntity {

	public String getScriptName() {
		return scriptName;
	}

	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}

	public String getScriptMode() {
		return scriptMode;
	}

	public void setScriptMode(String scriptMode) {
		this.scriptMode = scriptMode;
	}

	public String getScriptText() {
		return scriptText;
	}

	public void setScriptText(String scriptText) {
		this.scriptText = scriptText;
	}

	@ProtoMember(1)
	private String scriptName;

	@ProtoMember(2)
	private String scriptMode;

	@ProtoMember(3)
	private String scriptText;
}
