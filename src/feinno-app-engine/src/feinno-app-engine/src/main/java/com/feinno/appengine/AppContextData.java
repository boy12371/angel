/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine;

/**
 * AppContext数据实体类, 所有的AppContextData都包含一个uri和一个byte[]的buffer
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppContextData
{
	private String uri;
	private byte[] data;
	
	public String getUri()
	{
		return uri;
	}

	public byte[] getData()
	{
		return data;
	}

	public void setUri(String uri)
	{
		this.uri = uri;
	}
	
	public void setData(byte[] data)
	{
		this.data = data;
	}
}
