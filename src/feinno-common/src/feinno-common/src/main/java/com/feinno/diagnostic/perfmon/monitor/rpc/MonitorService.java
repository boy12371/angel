package com.feinno.diagnostic.perfmon.monitor.rpc;

import com.feinno.diagnostic.observation.ObserverReportEntity;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;
import com.feinno.serialization.protobuf.types.ProtoString;

/**
 * Rpc服务接口<br>
 * 包括:获取列表,轮训Counter值等;
 * 
 * @author jingmiao
 * 
 */
@RpcService("Monitor")
public interface MonitorService {
	/**
	 * 获取Category列表<br>
	 * 
	 * @return List<MonitorCategoryEntity>,结构如下:<br>
	 *         ----name;<br>
	 *         ----instance;<br>
	 *         ----columns[ObserverReportColumn(List)]:<br>
	 *         --------name;<br>
	 *         --------type[ObserverReportColumnType];<br>
	 */
	@RpcMethod(value = "GetCategoryList")
	MonitorCategoryEntity[] getCategoryList();

	@RpcMethod(value = "Pull")
	ObserverReportEntity[] pull(ProtoString cookie);

	@RpcMethod(value = "Subscribe")
	int subscribe(MonitorRequestArgs args);

	@RpcMethod(value = "Unsubscribe")
	int unsubscribe(MonitorRequestArgs args);
}
