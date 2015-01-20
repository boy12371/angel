package com.feinno.diagnostic.perfmon.monitor.rpc;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * 订阅信息包.
 * 
 * @author jingmiao
 * 
 */
public class MonitorRequestArgs extends ProtoEntity {
	@ProtoMember(1)
	private String category;
	@ProtoMember(2)
	private String instance;
	@ProtoMember(3)
	private int invterval;
	@ProtoMember(4)
	private String cookie;

	public MonitorRequestArgs() {

	}

	public MonitorRequestArgs(String category, String instance,
			int invterval, String cookie) {
		this.setCategory(category);
		this.setInstance(instance);
		this.setInvterval(invterval);
		this.setCookie(cookie);
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public int getInvterval() {
		return invterval;
	}

	public void setInvterval(int invterval) {
		this.invterval = invterval;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
}
