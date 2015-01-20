/*
 * FAE, Feinno App Engine
 *  
 * Create by lichunlei 2010-11-26
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.configuration.spi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.feinno.configuration.ConfigParams;
import com.feinno.ha.interfaces.configuration.HAConfigTableBuffer;
import com.feinno.ha.interfaces.configuration.HAConfigTextBuffer;

/**
 * 配置获取代理类的接口
 * 针对LOCAL和HA模式有不同的配置获取方式
 * 
 * @author lichunlei
 */
public interface ConfigurationLoader
{
	/**
	 * 读取一个配置表
	 * 
	 * @param path
	 * @return ConfigTableBuffer
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	HAConfigTableBuffer loadTable(String path) throws SQLException, FileNotFoundException, IOException;

	/**
	 * 
	 * 读取一个配置文本
	 * 
	 * @param path
	 * @return List<ConfigItemBuffer>
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 */
	HAConfigTextBuffer loadText(String path,ConfigParams args) throws FileNotFoundException, IOException;
}
