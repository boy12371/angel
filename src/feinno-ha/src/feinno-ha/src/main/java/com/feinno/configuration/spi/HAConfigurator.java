/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 12, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.configuration.spi;

import java.util.Date;

import com.feinno.configuration.ConfigParams;
import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigTableItem;
import com.feinno.configuration.ConfigType;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.Configurator;
import com.feinno.ha.ServiceSettings;
import com.feinno.ha.interfaces.configuration.HAConfigArgs;
import com.feinno.ha.interfaces.configuration.HAConfigTableBuffer;
import com.feinno.ha.interfaces.configuration.HAConfigTextBuffer;
import com.feinno.ha.interfaces.worker.HAWorkerAgentCallbackService;
import com.feinno.ha.interfaces.worker.HAWorkerAgentService;
import com.feinno.rpc.duplex.RpcDuplexClient;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.util.ServiceEnviornment;

/**
 * HA方式下的Configurator 1.
 * 通过com.feinno.ha.intrefaces.worker.HAWorkerAgentService读取配置 2. 支持PUSH更新及订阅更新
 * 3. 支持特例化
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class HAConfigurator implements Configurator {

	/** 与center交互的service */
	private HAWorkerAgentService service;

	/**
	 * 构造方法
	 * 
	 * @param client
	 */
	public HAConfigurator(RpcDuplexClient client) {
		service = client.getService(HAWorkerAgentService.class);
		client.registerCallbackService(new HAWorkerAgentCallbackService() {
			@Override
			public void notifyConfigExpired(HAConfigArgs args) {
				// 通知某一条配置过期，需要去服务器取最新数据
				try {
					ConfigurationManager.updateConfig(args.getType(), args.getPath(), args.getParams());
				} catch (ConfigurationException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.feinno.configuration.Configurator#loadConfigTable(java.lang.Class,
	 * java.lang.Class, java.lang.String)
	 */
	@Override
	public <K, V extends ConfigTableItem> ConfigTable<K, V> loadConfigTable(Class<K> keyType, Class<V> valueType,
			String path, ConfigUpdateAction<ConfigTable<K, V>> updateCallback) throws ConfigurationException {
		HAConfigArgs args = new HAConfigArgs();
		args.setType(ConfigType.TABLE);
		args.setPath(path);
		args.setParams(""); // ConfigTable 不支持特例化

		HAConfigTableBuffer tableBuffer = service.loadConfigTable(args);
		ConfigTable<K, V> table = tableBuffer.toTable(keyType, valueType);
		try {
			if (updateCallback != null) {
				updateCallback.run(table);
			}
			// 如果更新成功，那么更新版本号
			Date version = table.getVersion();
			HAConfigArgs versionArgs = new HAConfigArgs();
			versionArgs.setType(ConfigType.TABLE);
			versionArgs.setPath(path);
			versionArgs.setParams("");
			versionArgs.setVersion(version);
			service.updateConfigVersion(versionArgs);
		} catch (Exception e) {
			throw new ConfigurationException("", e);
		}
		return table;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.feinno.configuration.Configurator#loadConfigText(java.lang.String,
	 * com.feinno.configuration.ConfigParams)
	 */
	@Override
	public String loadConfigText(String path, ConfigParams params, ConfigUpdateAction<String> updateCallback)
			throws ConfigurationException {
		HAConfigArgs args = new HAConfigArgs();
		args.setType(ConfigType.TEXT);
		args.setPath(path);

		ConfigParams p2 = new ConfigParams();
		//
		// 增加本机的特定环境信息
		p2.put("service", ServiceSettings.INSTANCE.getServiceName());
		p2.put("computer", ServiceEnviornment.getComputerName());
		if (params != null) {
			p2 = p2.merge(params, true);
		}
		args.setParams(p2.toString());

		HAConfigTextBuffer configTextBuffer = service.loadConfigText(args);

		try {
			if (updateCallback != null) {
				updateCallback.run(configTextBuffer.getText());
			}
			// 如果更新成功，那么更新版本号
			Date version = configTextBuffer.getVersion();
			HAConfigArgs versionArgs = new HAConfigArgs();
			versionArgs.setType(ConfigType.TEXT);
			versionArgs.setPath(path);
			versionArgs.setParams(params != null ? params.toString() : "");
			versionArgs.setVersion(version);
			service.updateConfigVersion(versionArgs);
		} catch (Exception e) {
			throw new ConfigurationException("", e);
		}
		return configTextBuffer.getText();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.feinno.configuration.Configurator#subscribeConfig(com.feinno.
	 * configuration.ConfigType, java.lang.String)
	 */
	@Override
	public void subscribeConfig(ConfigType type, String path, ConfigParams params) {
		if (type != ConfigType.TABLE) {
			if (params == null) {
				params = new ConfigParams();
			}
			params.put("service", ServiceSettings.INSTANCE.getServiceName());
			params.put("computer", ServiceEnviornment.getComputerName());
		}
		HAConfigArgs args = new HAConfigArgs();
		args.setType(type);
		args.setPath(path);
		args.setParams(params != null ? params.toString() : "");
		service.subscribeConfig(args);
	}

	/**
	 * 这个LoadConfig方法,可以取得远程的表或文件,以PB方式返回
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T extends ProtoEntity> T loadConfig(ConfigType type, String path, ConfigParams params,
			ConfigUpdateAction<T> updateCallback) throws ConfigurationException {
		HAConfigArgs args = new HAConfigArgs();
		args.setType(type);
		args.setPath(path);
		ConfigParams p2 = new ConfigParams();
		T result = null;
		// byte[] buffer = null;
		// 如果更新成功，那么更新版本号
		Date version = null;
		try {
			if (type == ConfigType.TABLE) {
				args.setParams(""); // ConfigTable 不支持特例化
				HAConfigTableBuffer tableBuffer = null;
				tableBuffer = service.loadConfigTable(args);
				result = (T) tableBuffer;
				version = tableBuffer.getVersion();
			} else {
				// 增加本机的特定环境信息
				p2.put("service", ServiceSettings.INSTANCE.getServiceName());
				p2.put("computer", ServiceEnviornment.getComputerName());
				if (params != null) {
					p2 = p2.merge(params, true);
				}
				args.setParams(p2.toString());
				HAConfigTextBuffer configBuffer = null;
				configBuffer = service.loadConfigText(args);
				result = (T) configBuffer;
				version = configBuffer.getVersion();
			}

			if (updateCallback != null) {
				updateCallback.run(result);
			}

			HAConfigArgs versionArgs = new HAConfigArgs();
			versionArgs.setType(type);
			versionArgs.setPath(path);
			versionArgs.setParams(p2.toString());
			versionArgs.setVersion(version);
			service.updateConfigVersion(versionArgs);
		} catch (Exception e) {
			throw new ConfigurationException("LoadConfig by BaseCenter failed.", e);
		}
		return result;

	}

}
