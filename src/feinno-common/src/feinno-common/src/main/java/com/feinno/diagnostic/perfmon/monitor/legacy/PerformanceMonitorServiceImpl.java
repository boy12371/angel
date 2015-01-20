/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2012-2-10
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor.legacy;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObserverInspector;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReport;
import com.feinno.diagnostic.observation.ObserverReportMode;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.serialization.protobuf.types.ProtoByteArray;
import com.feinno.serialization.protobuf.types.ProtoString;
import com.feinno.util.DateTime;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class PerformanceMonitorServiceImpl implements PerformanceMonitorService
{
	private Map<String, ObserverInspector> inspectors = new Hashtable<String, ObserverInspector>();

	@Override
	public ProtoByteArray readObserverReport(ProtoString namePro,RpcServerContext context) throws IOException
	{
		String name = namePro.getValue();
		ObserverInspector inspector = inspectors.get(name);
		if (inspector == null) {
			Observable ob = ObserverManager.getObserverItem(name);
			if (ob == null) {
				throw new IllegalArgumentException("Invaild name:" + name);
			}
			inspector = new ObserverInspector(ob, ObserverReportMode.ALL, null, null);
			inspectors.put(name, inspector);
		}
		ObserverReport report = inspector.getReport(DateTime.now());
		
		byte[] buffer = report.encodeToProtobuf();
		return new ProtoByteArray(buffer);
	}
}
