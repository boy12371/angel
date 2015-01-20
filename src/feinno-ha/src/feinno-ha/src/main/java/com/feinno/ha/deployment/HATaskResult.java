package com.feinno.ha.deployment;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <b>描述: </b>该类用于包装一个HA任务的执行结果
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class HATaskResult {

	private String serviceName;

	private String action;

	private boolean isSucess;

	private List<Exception> exceptions = new ArrayList<Exception>();

	public HATaskResult() {
	}

	public HATaskResult(String serviceName, String action) {
		this.serviceName = serviceName;
		this.action = action;
	}

	/**
	 * 提供了几个建议的静态工厂方法，用于快速的创造一个任务结果对象
	 * 
	 * @param serviceName
	 * @param action
	 * @return
	 */
	public static HATaskResult newSucessResult(String serviceName, String action) {
		HATaskResult result = new HATaskResult();
		result.setServiceName(serviceName);
		result.setAction(action);
		result.setSucess(true);
		return result;
	}

	/**
	 * 提供了几个建议的静态工厂方法，用于快速的创造一个任务结果对象
	 * 
	 * @param serviceName
	 * @param action
	 * @param exceptions
	 * @return
	 */
	public static HATaskResult newFailedResult(String serviceName, String action, Exception... exceptions) {
		HATaskResult result = new HATaskResult();
		result.setServiceName(serviceName);
		result.setAction(action);
		result.setSucess(false);
		if (exceptions != null) {
			for (Exception exception : exceptions) {
				result.exceptions.add(exception);
			}
		}
		return result;
	}

	/**
	 * 提供了几个建议的静态工厂方法，用于快速的创造一个任务结果对象
	 * 
	 * @param serviceName
	 * @param action
	 * @param exceptions
	 * @return
	 */
	public static HATaskResult newFailedResult(String serviceName, String action, List<Exception> exceptions) {
		HATaskResult result = new HATaskResult();
		result.setServiceName(serviceName);
		result.setAction(action);
		result.setSucess(false);
		if (exceptions != null) {
			for (Exception exception : exceptions) {
				result.exceptions.add(exception);
			}
		}
		return result;
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

	public boolean isSucess() {
		return isSucess;
	}

	public void setSucess(boolean isSucess) {
		this.isSucess = isSucess;
	}

	public List<Exception> getExceptions() {
		return exceptions;
	}

	public void setExceptions(List<Exception> exceptions) {
		this.exceptions = exceptions;
	}

	public void addException(Exception exception) {
		if (this.exceptions == null) {
			this.exceptions = new ArrayList<Exception>();
		}
		this.exceptions.add(exception);
	}

}
