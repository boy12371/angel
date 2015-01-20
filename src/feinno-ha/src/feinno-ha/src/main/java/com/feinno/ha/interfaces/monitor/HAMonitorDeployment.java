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
public class HAMonitorDeployment extends ProtoEntity {

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public boolean isUpload() {
		return upload;
	}

	public void setUpload(boolean upload) {
		this.upload = upload;
	}

	public float getThresholdMin() {
		return thresholdMin;
	}

	public void setThresholdMin(float thresholdMin) {
		this.thresholdMin = thresholdMin;
	}

	public float getThresholdMax() {
		return thresholdMax;
	}

	public void setThresholdMax(float thresholdMax) {
		this.thresholdMax = thresholdMax;
	}

	@ProtoMember(1)
	private int keyId;

	@ProtoMember(2)
	private int interval;

	@ProtoMember(3)
	private boolean upload;

	@ProtoMember(4)
	private float thresholdMin;

	@ProtoMember(5)
	private float thresholdMax;
}
