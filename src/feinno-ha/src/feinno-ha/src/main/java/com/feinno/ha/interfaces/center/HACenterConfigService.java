/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-10-26
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.ha.interfaces.center;

import com.feinno.ha.interfaces.configuration.HAConfigArgs;
import com.feinno.ha.interfaces.configuration.HAConfigTableBuffer;
import com.feinno.ha.interfaces.configuration.HAConfigTextBuffer;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;

/**
 * 为非Worker管理服务提供的HACenterConfigService, 不支持特例化
 * 
 * @author 高磊 gaolei@feinno.com
 */
@RpcService("HACenterConfigService")
public interface HACenterConfigService
{
	/**
	 * 获取配置表，非主动的push更新
	 */
	@RpcMethod("LoadConfigTable")
	HAConfigTableBuffer loadConfigTable(HAConfigArgs input);

	/**
	 * 获取配置文本
	 */
	@RpcMethod("LoadConfigText")
	HAConfigTextBuffer loadConfigText(HAConfigArgs input);
}
