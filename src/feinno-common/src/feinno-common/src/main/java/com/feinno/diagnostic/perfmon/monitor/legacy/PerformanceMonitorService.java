/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-8
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor.legacy;

import java.io.IOException;

import com.feinno.diagnostic.observation.ObserverReport;
import com.feinno.rpc.RpcEndpointFactory;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.rpc.client.RpcProxyFactory;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcService;
import com.feinno.serialization.protobuf.types.ProtoByteArray;
import com.feinno.serialization.protobuf.types.ProtoString;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
@RpcService("PerformonMonitor")
public interface PerformanceMonitorService
{
	@RpcMethod("ReadObserverReport")
	ProtoByteArray readObserverReport(ProtoString name,RpcServerContext countext) throws IOException;
	
	class Sample 
	{
		@SuppressWarnings("unused")
		private static void sample()
		{
			RpcEndpoint ep = RpcEndpointFactory.parse("tcp://127.0.0.1:6203");
			try {
				PerformanceMonitorService service = RpcProxyFactory.getService(ep, PerformanceMonitorService.class);
				
				ProtoByteArray ret = service.readObserverReport(new ProtoString("rpc-client"),null);	// 名字靠配置文件
				ObserverReport report = ObserverReport.decodeFromProtobuf(ret.getValue());
				report.getColumns();	// 列
				report.getRows();		// 行
			} catch (Exception ex) {
				
			}
		}
	}
}
