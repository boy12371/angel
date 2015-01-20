/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 12, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.configuration;

import com.feinno.serialization.protobuf.ProtoEntity;

/**
 * 配置的实际基础类
 * 
 * @author 高磊 gaolei@feinno.com
 */
public interface Configurator {
	/**
	 * 
	 * 获取一个配置表 1. 当属于LOCAL模式时, 配置表将从配置根目录下的configdb.properties中读取 2. 当处于HA模式时,
	 * 配置表将会通过HACenter读取
	 * 
	 * @see ConfigTableItem
	 * @see ConfigTableKey
	 * @param <K>
	 * @param <V>
	 * @param keyType
	 *            配置表主键类型, 可以是基础类型, 或者组合类型 , 当为组合类型时必须从ConfigTableKey中派生
	 * @param valueType
	 *            配置表值类型, 必须从ConfigTableItem类派生
	 * @param path
	 *            表名
	 * @param updateCallback
	 *            更新的callback
	 * @return
	 * @throws ConfigurationFailedException
	 *             , ConfigurationNotFoundException
	 */
	<K, V extends ConfigTableItem> ConfigTable<K, V> loadConfigTable(Class<K> keyType, Class<V> valueType, String path,
			ConfigUpdateAction<ConfigTable<K, V>> updateCallback) throws ConfigurationException;

	/**
	 * 
	 * {在这里补充功能说明}
	 * 
	 * @param path
	 * @param params
	 * @param onUpdate
	 * @return
	 * @throws ConfigurationException
	 */
	String loadConfigText(String path, ConfigParams params, ConfigUpdateAction<String> updateCallback)
			throws ConfigurationException;

	/**
	 * 
	 * {在这里补充功能说明}
	 * 
	 * @param path
	 * @param params
	 * @param onUpdate
	 * @return
	 * @throws ConfigurationException
	 */
	<T extends ProtoEntity> T loadConfig(ConfigType type, String path, ConfigParams params,
			ConfigUpdateAction<T> updateCallback) throws ConfigurationException;

	/**
	 * 
	 * {在这里补充功能说明}
	 * 
	 * @param type
	 * @param path
	 */
	void subscribeConfig(ConfigType type, String path, ConfigParams params);
}
