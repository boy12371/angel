/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-18
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.configuration.dynamic;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;

/**
 * 当访问Rpc的表时, 获取到的Rpc地址使用其他端口代替
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class UserTable extends ConfigTableItem {
	@ConfigTableField(value = "name", isKeyField = true)
	private String name;

	@ConfigTableField(value = "address", isKeyField = false)
	private String address;

	@ConfigTableField("age")
	private int age;

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getAddress() {
		return address;
	}

	public final void setAddress(String address) {
		this.address = address;
	}

	public final int getAge() {
		return age;
	}

	public final void setAge(int age) {
		this.age = age;
	}

	@Override
	public void afterLoad() throws Exception {
	}
}
