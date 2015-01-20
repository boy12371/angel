package com.feinno.appengine.testing;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 周岩 zhouyanxjs@feinno.com
 */
public class FAEDebugProxyConfigTableItem extends ConfigTableItem
{
	@ConfigTableField(value = "Id", isKeyField = true)
	private String id;
	
	@ConfigTableField("BeanTypes")
	private String beanTypes;
	
	@ConfigTableField("Endpoint")
	private String endPoint;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getBeanTypes()
	{
		return beanTypes;
	}

	public void setBeanTypes(String beanTypes)
	{
		this.beanTypes = beanTypes;
	}

	public String getEndPoint()
	{
		return endPoint;
	}

	public void setEndPoint(String endPoint)
	{
		this.endPoint = endPoint;
	}
}
