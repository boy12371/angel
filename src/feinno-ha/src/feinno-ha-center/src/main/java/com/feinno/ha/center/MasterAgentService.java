/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei Jun 23, 2012
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.ha.center;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigType;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.ha.database.HADBMonitorHelper;
import com.feinno.ha.database.HADBServerHelper;
import com.feinno.ha.database.HADatabaseFactory;
import com.feinno.ha.interfaces.master.HAMasterAgentService;
import com.feinno.ha.interfaces.master.HAMasterDeployment;
import com.feinno.ha.interfaces.master.HAMasterHeartbeatArgs;
import com.feinno.ha.interfaces.master.HAMasterRegisterArgs;
import com.feinno.ha.interfaces.monitor.HAMonitorDeployment;
import com.feinno.ha.interfaces.monitor.HAMonitorKey;
import com.feinno.ha.interfaces.monitor.HAMonitorScript;
import com.feinno.ha.interfaces.monitor.HAObserverDeployment;
import com.feinno.ha.interfaces.worker.HAWorkerMonitorConfig;
import com.feinno.ha.service.activity.node.ActivityFinalNodeQueue;
import com.feinno.ha.service.activity.node.MasterActivityNode;
import com.feinno.rpc.channel.tcp.RpcTcpConnection;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcServiceBase;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
import com.feinno.util.EventHandler;
import com.feinno.util.ObjectHelper;

/**
 * 用于hamasterd注册连接，获取部署相关信息和上报监控指标的相关接口
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class MasterAgentService extends RpcServiceBase {

	private Map<String, MasterAgent> agents;

	public static final MasterAgentService INSTANCE = new MasterAgentService();

	private static final Logger LOGGER = LoggerFactory.getLogger(MasterAgentService.class);

	private static String HA_DESKTOP_HOST = null;

	private static Object HA_DESKTOP_HOST_SYN = new Object();

	private MasterAgentService() {
		super(HAMasterAgentService.SERVICE_NAME);
		agents = new Hashtable<String, MasterAgent>();
	}

	private MasterAgent getCurrentAgent(RpcServerContext ctx) {
		return (MasterAgent) ctx.getConnection().getAttachment();
	}

	public MasterAgent getMasterAgent(String serverName) {
		MasterAgent agent = agents.get(serverName);
		if (agent == null) {
			return null;
		}
		return agent;
	}

	public void deleteMasterAgent(String serverName) {
		agents.remove(serverName);
	}

	@RpcMethod("Register")
	public void register(RpcServerContext ctx) {
		try {
			RpcTcpEndpoint remoteEndpoint = (RpcTcpEndpoint) ctx.getConnection().getRemoteEndpoint();
			HAMasterRegisterArgs args = ctx.getArgs(HAMasterRegisterArgs.class);
			String serverName = args.getServerName();

			MasterAgent agent = getMasterAgent(args.getServerName());
			if (agent != null) {
				MasterAgentService.INSTANCE.deleteMasterAgent(agent.getServerName());
			}
			agent = new MasterAgent(ctx, remoteEndpoint);
			ctx.getConnection().setAttachment(agent);
			ctx.getConnection().getDisconnected().addListener(new EventHandler<Throwable>() {

				public void run(Object sender, Throwable e) {
					RpcTcpConnection conn = (RpcTcpConnection) sender;
					MasterAgent agent = (MasterAgent) conn.getAttachment();
					LOGGER.error("MasterAgent Disconnected. masterName:{} Endpoint:{}.", agent.getServerName(),agent.getEndpoint());
					MasterAgent cacheAgent = getMasterAgent(agent.getServerName());
					if(conn.getRemoteEndpoint().toString().equals(cacheAgent.getEndpoint().toString())){
						LOGGER.error(
								"MasterAgent Disconnected. Update HA_MasterEndpoint status to DISCONNECTED.RemoteEndpoint : {}",
								conn.getRemoteEndpoint().toString());
						MasterAgentService.INSTANCE.deleteMasterAgent(agent.getServerName());
						MasterActivityNode.updateMasterStatus(agent.getServerName());
					}
				}

			});
			agents.put(serverName, agent);

			if (agent.processRegister(args)) {
				LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Registered " + serverName);
				ActivityFinalNodeQueue.putMasterNode(new MasterActivityNode(serverName));
			} else {
				RuntimeException exception = new RuntimeException("MasterD " + serverName + " register failed!");
				LOGGER.debug("Register Exception ", exception);
				ctx.endWithError(exception);
				return;
			}
			ctx.end();
		} catch (SQLException e) {
			LOGGER.debug("Register Exception ", e);
			ctx.endWithError(e);
		}

	}

	/**
	 * 保持心跳并上报状态, 在第一次连接或出现报警及worker状态变更时，应立刻进行Keepalive
	 */
	@RpcMethod("Heartbeat")
	public void heartbeat(RpcServerContext ctx) {
		MasterAgent agent = getCurrentAgent(ctx);
		HAMasterHeartbeatArgs args = ctx.getArgs(HAMasterHeartbeatArgs.class);
		String serverName = agent.getServerName();
		LOGGER.info("MasterAgentService receive heartbeat by " + serverName + " server.");

		if (serverName != null && !serverName.equals("")) {
			LOGGER.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Now try to get " + serverName + " from MasterManager");
			if (this.getMasterAgent(serverName) == null) {
				RuntimeException exception = new RuntimeException(String.format("Not found %s master server.",
						serverName));
				LOGGER.error("", exception);
				ctx.endWithError(exception);
				return;
			} else if (agent.processKeepAlive(args)) {
				ActivityFinalNodeQueue.putMasterNode(new MasterActivityNode(serverName));
			} else {
				LOGGER.error("Update Master alive info failed.");
				ctx.endWithError(new RuntimeException("Update alive info failed."));
				return;
			}
		} else {
			LOGGER.error("Master can not get name of computer from server context.");
			ctx.endWithError(new RuntimeException("Master  can not get name of computer from server context."));
			return;
		}
		ctx.end();
	}

	/**
	 * 获取监控相关配置
	 * 
	 * @return
	 */
	@RpcMethod("GetMonitorConfig")
	public void getMonitorConfig(RpcServerContext ctx) {
		try {
			// Step 1.从连接中取得服务器名称
			MasterAgent agent = getCurrentAgent(ctx);
			String serverName = agent.getServerName();

			// Step 2.通过HADBMonitorHelper取得监控配置
			List<String> serverGroupList = HADBServerHelper.getServerGroupList(serverName);
			List<HAMonitorDeployment> deployments = HADBMonitorHelper.getHAMonitorDeployments(serverGroupList);
			List<HAObserverDeployment> observers = HADBMonitorHelper.getHAObserverReportDeployments(serverGroupList);
			List<HAMonitorKey> haMonitorKeys = HADBMonitorHelper.getHAMonitorKeys(deployments);
			List<HAMonitorScript> haMonitorScripts = HADBMonitorHelper.getHAMonitorScript(haMonitorKeys);

			// Step 3.组装监控数据
			HAWorkerMonitorConfig monitorConfig = new HAWorkerMonitorConfig();
			monitorConfig.setDeployments(deployments);
			monitorConfig.setKeys(haMonitorKeys);
			monitorConfig.setObservers(observers);
			monitorConfig.setScripts(haMonitorScripts);

			// Step 4.结束,回执
			ctx.end(monitorConfig);
		} catch (SQLException e) {
			LOGGER.error("Master getMonitorConfig failed.", e);
			ctx.endWithError(e);
		}
	}

	/**
	 * 获取部署相关配置
	 * 
	 * @return
	 */
	@RpcMethod("GetDeployments")
	public void getDeployments(RpcServerContext ctx) {
		MasterAgent agent = getCurrentAgent(ctx);
		String serverName = agent.getServerName();
		try {
			// 该接口由于表结构变化，如果发生字段不匹配的话，看到的同学直接修改
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT HA_WorkerDeployment.ServiceDeployName,  ");
			sb.append(" HA_WorkerDeployment.PackageId,  ");
			sb.append("	HA_WorkerDeployment.WorkerUpdateMode,  ");
			sb.append("	HA_WorkerDeployment.PackageId,  ");
			sb.append("	HA_WorkerDeployment.WorkerStartMode,  ");
			sb.append("	HA_WorkerDeployment.PortsSettings,  ");
			sb.append("	HA_WorkerDeployment.StartParams,  ");
			sb.append("	HA_WorkerDeployment.Settings,  ");
			sb.append("	HA_WorkerPackage.PackageFile,  ");
			sb.append("	HA_WorkerPackage.ServiceVersion,  ");
			sb.append("	HA_WorkerPackage.Controllable,  ");
			sb.append("	HA_WorkerPackage.ServicePorts ");
			sb.append("FROM HA_WorkerDeployment INNER JOIN HA_WorkerPackage ON HA_WorkerDeployment.PackageId = HA_WorkerPackage.PackageId ");
			//sb.append("	 INNER JOIN HA_ServerGroupMember ON HA_WorkerDeployment.ServerGroup = HA_ServerGroupMember.ServerGroup ");
			sb.append("	 INNER JOIN HA_WorkerDeploymentServer ON HA_WorkerDeployment.ServiceDeployName = HA_WorkerDeploymentServer.ServiceDeployName ");
			sb.append("WHERE HA_ServerGroupMember.ServerName=? ");
			Database db = HADatabaseFactory.getHADatabase();
			ArrayList<HAMasterDeployment> lst = new ArrayList<HAMasterDeployment>();
			DataTable dt = db.executeTable(sb.toString(), serverName);
			List<DataRow> rows = dt.getRows();
			for (DataRow dr : rows) {
				HAMasterDeployment deployment = new HAMasterDeployment();
				lst.add(deployment);
				boolean controllable = (Boolean) dr.getObject("Controllable");
				deployment.setControllable(controllable);
				deployment.setPackageId(dr.getInt("PackageId"));
				deployment.setPackageUrl(getHADesktopHost() + dr.getString("PackageFile"));
				deployment.setPortsSettings(dr.getString("PortsSettings"));
				deployment.setServicePorts(dr.getString("ServicePorts"));
				deployment.setServiceVersion(dr.getString("ServiceVersion"));
				deployment.setStartParams(dr.getString("StartParams"));
				deployment.setServiceDeployName(dr.getString("ServiceDeployName"));
				deployment.setWorkerStartMode(dr.getString("WorkerStartMode"));
				deployment.setWorkerUpdateMode(dr.getString("WorkerUpdateMode"));
				deployment.setSettings(dr.getString("Settings"));
			}
			LOGGER.debug(ObjectHelper.dumpObject(lst));
			HAMasterDeployments masterDeployments = new HAMasterDeployments();
			masterDeployments.setDeployments(lst);
			String availablePortsSql = "SELECT AvailablePorts FROM HA_Server WHERE ServerName = ?";
			dt = db.executeTable(availablePortsSql, serverName);
			rows = dt.getRows();
			if (rows.size() > 0) {
				String availablePorts = rows.get(0).getString("AvailablePorts");
				masterDeployments.setAvailablePorts(availablePorts != null ? availablePorts : "");
			} else {
				masterDeployments.setAvailablePorts("");
			}
			ctx.end(masterDeployments);
		} catch (SQLException e) {
			LOGGER.error(String.format("GetDeployments failed. serverName:%s", serverName), e);
			ctx.endWithError(e);
		}
	}

	/**
	 * 获取HA_Desktop的Host URL
	 * 
	 * @return
	 */
	public String getHADesktopHost() {
		if (HA_DESKTOP_HOST != null) {
			return HA_DESKTOP_HOST;
		}
		// 如果未初始化，则开始初始化
		synchronized (HA_DESKTOP_HOST_SYN) {
			if (HA_DESKTOP_HOST != null) {
				return HA_DESKTOP_HOST;
			}
			try {
				// 获取并订阅HADesktop.properties的配置更新
				Properties properties = ConfigurationManager.loadProperties("HADesktop.properties", null,
						new ConfigUpdateAction<Properties>() {
							public void run(Properties e) throws Exception {
								HA_DESKTOP_HOST = e.getProperty("host");
							}
						});
				ConfigurationManager.subscribeConfigUpdate(ConfigType.TEXT, "HADesktop.properties", null);
				HA_DESKTOP_HOST = properties.getProperty("host");
			} catch (Exception e) {
				LOGGER.error("Init HA_DESKTOP_HOST failed.", e);
			}
		}
		return HA_DESKTOP_HOST;
	}

	public static class HAMasterDeployments extends ProtoEntity {
		@ProtoMember(1)
		private List<HAMasterDeployment> Deployments;
		@ProtoMember(2)
		private String AvailablePorts;

		public List<HAMasterDeployment> getDeployments() {
			return Deployments;
		}

		public void setDeployments(List<HAMasterDeployment> deployments) {
			Deployments = deployments;
		}

		public String getAvailablePorts() {
			return AvailablePorts;
		}

		public void setAvailablePorts(String availablePorts) {
			AvailablePorts = availablePorts;
		}
	}
}
