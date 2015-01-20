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
 * 
 */
public class TestDynamicTable extends ConfigTableItem {
	@ConfigTableField(value = "WorkerId", isKeyField = true)
	private String WorkerId;

	@ConfigTableField(value = "ServerName", isKeyField = true)
	private String ServerName;

}
