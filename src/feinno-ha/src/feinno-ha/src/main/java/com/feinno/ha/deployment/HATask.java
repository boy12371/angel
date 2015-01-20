package com.feinno.ha.deployment;

import java.util.UUID;

import com.feinno.logging.common.LogCommon;
import com.feinno.threading.Future;
import com.google.gson.JsonObject;

/**
 * 
 * <b>描述: </b>一个继承自Future的类，该类增加了任务运行状态的几个字段，分别是服务器名字，服务名字以及正在执行的动作
 * <p>
 * <b>功能: </b>增加了任务运行状态的几个字段，分别是服务器名字，服务名字以及正在执行的动作
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 * @param <V>
 */
public class HATask extends Future<Exception> {

	private String serverName;

	private String serviceName;

	private String action;

	// 此任务是否为一个Rpc的任务，如果是，则按照Rpc的任务进行说明
	private boolean isRpcTask;

	public HATask(String serverName, String serviceName, String action) {
		this.serverName = serverName;
		this.serviceName = serviceName;
		this.action = action;
	}

	public boolean isRpcTask() {
		return isRpcTask;
	}

	public void setRpcTask(boolean isRpcTask) {
		this.isRpcTask = isRpcTask;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public JsonObject toJsonObject() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("serverName", serverName != null ? serverName : "");
		jsonObject.addProperty("serviceName", serviceName != null ? serviceName : "");
		jsonObject.addProperty("action", action != null ? action : "");
		jsonObject.addProperty("isDone", isDone());
		jsonObject.addProperty("exception", isDone() ? getValue() != null ? LogCommon.formaError(getValue()) : "" : "");
		return jsonObject;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof HATask)) {
			return false;
		}
		HATask task = (HATask) obj;
		if (serverName == task.getServerName() && serviceName == task.getServiceName() && action == task.getAction()) {
			return true;
		}
		if (serverName != null) {
			if (task.getServerName() == null || !serverName.equals(task.getServerName())) {
				return false;
			}
		} else {
			if (task.getServerName() != null) {
				return false;
			}
		}
		if (serviceName != null) {
			if (task.getServiceName() == null || !serviceName.equals(task.getServiceName())) {
				return false;
			}
		} else {
			if (task.getServiceName() != null) {
				return false;
			}
		}
		if (action != null) {
			if (task.getAction() == null || !action.equals(task.getAction())) {
				return false;
			}
		} else {
			if (task.getAction() != null) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result += (serverName != null ? serverName.hashCode() * 17 : 0);
		result += (serviceName != null ? serviceName.hashCode() * 17 : 0);
		result += (action != null ? action.hashCode() * 17 : 0);
		return result;
	}

	public String toString() {
		return toJsonObject().toString();
	}

	public String getIdentifier() {
		return UUID.randomUUID().toString();
	}
}
