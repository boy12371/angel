/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-11-23
 * 
 * Copyright (c) 2010 ������ý���ſƼ����޹�˾
 */
package test.com.feinno.configuration;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;

/**
 * SampleConfigTable
 * 
 * @auther gaolei
 */
public class SampleConfigTableItem extends ConfigTableItem
{
	@ConfigTableField("Name")
	private String name;
	
	@ConfigTableField("Value")
	private int value;
	
	@ConfigTableField("Desc")
	private String desc;
}
