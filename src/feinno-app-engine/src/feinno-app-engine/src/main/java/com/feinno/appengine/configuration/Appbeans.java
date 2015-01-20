/**
 * 
 */
package com.feinno.appengine.configuration;

import java.util.List;

import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.serialization.json.JsonContract;

/**
 * <b>描述: json数据包装类，保证生成的json字符串中最外层是appbeans</b>
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Zhou.yan
 * 
 */
@JsonContract
public class Appbeans {

	public List<AppBeanAnnotations> getAppBeans() {
		return appBeans;
	}

	public void setAppBeans(List<AppBeanAnnotations> appBeans) {
		this.appBeans = appBeans;
	}

	private List<AppBeanAnnotations> appBeans;
}
