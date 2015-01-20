package com.feinno.ha.deployment;

import java.sql.SQLException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.database.HADBDeploymentHelper;

/**
 * 用于分配HAServer服务器的类
 * 
 * @author lvmingwei
 * 
 */
public class HAServerDispatcher {

	/** 为系统运行预留1G的内存 */
	private static final Integer SYSTEM_RUN_MEMORY = 1024;

	/** 日志引用 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HAServerDispatcher.class);

	/** 这是一个单例 */
	private static HAServerDispatcher INSTANCE = null;

	/**
	 * 这是一个单例
	 * 
	 * @return
	 */
	public synchronized static HAServerDispatcher getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HAServerDispatcher();
		}
		return INSTANCE;
	}

	/**
	 * 分配服务器并且部署到这些服务器中
	 * 
	 * @param serverGroup
	 * @param serviceName
	 * @param serviceDeployName
	 * @param originalServiceDeployName
	 * @param instances
	 * @param needMemory
	 * @throws Exception
	 */
	public synchronized void dispatchAndDeploy(String serverGroup, String serviceDeployName,
			String originalServiceDeployName, int instances, int needMemory) throws Exception {
		// 分配服务器
		Set<String> serverSet = dispatch(serverGroup, serviceDeployName, originalServiceDeployName, instances,
				needMemory);
		// 向分配得服务器中添加部署信息
		HADBDeploymentHelper.saveWorkerDeploymentServer(serviceDeployName, serverSet, needMemory);
	}

	/**
	 * 通过提供的原始信息，返回服务器得分配位置
	 * 
	 * @param serviceName
	 *            服务名称
	 * @param originalServiceDeployName
	 *            以前的服务部署名称，用于提供历史列表，可以为空
	 * @param serverGroup
	 * @param instances
	 * @param needMemory
	 * @return
	 * @throws Exception
	 */
	public synchronized Set<String> dispatch(String serverGroup, String serviceDeployName,
			String originalServiceDeployName, int instances, int needMemory) throws Exception {

		LOGGER.info(String.format(
				"Invoke dispatch() serverGroup=%s, originalServiceDeployName=%s, instances=%s, needMemory=%s ",
				serverGroup, originalServiceDeployName, instances, needMemory));

		// 清理过期的部署信息，保证本次分配得准确性
		LOGGER.info("Clean expired HA_WorkerDeploymentServer");
		HADBDeploymentHelper.cleanExpiredWorkerDeploymentServer();

		// 获取此服务名称是否被部署过,如果部署过并且数量相同，则直接返回此服务列表
		LOGGER.info("Get history server list by originalServiceDeployName. originalServiceDeployName={}",
				originalServiceDeployName);
		Set<String> serverSet = HADBDeploymentHelper.getServerByServiceDeployName(originalServiceDeployName,
				serverGroup);
		// TODO 需要判断内存是否有变化，如果无变化或变小，则不理会，如果增加了，需要判断这些服务器是否有足够空间运行
		LOGGER.debug("Server list is {}", serverSet);
		LOGGER.info("Get server by ServiceDeployName. server count is {}, need instances is {}", serverSet.size(),
				instances);

		if (serverSet.size() == instances) {
			// 如果部署数量相同，则直接完成
			return serverSet;
		} else if (serverSet.size() > instances) {
			// 如果服务器多了，则移除多余的服务器
			int removeServerCount = serverSet.size() - instances;
			LOGGER.info("Need remove server count is {}", removeServerCount);
			serverSet = removeServer(serverSet, removeServerCount);
		} else if (serverSet.size() < instances) {
			// 如果服务器不够，需要添加新的服务器进来，那么从历史列表中获取
			int newServerCount = instances - serverSet.size();
			LOGGER.info(
					"Get servers list by ServiceDeployName not enough. So add server by history. Need new server count is {}",
					newServerCount);
			// 对于服务曾经部署的机器有优惠，有一半的空间，就允许，因爲一般新的启开老的会被关闭
			String serviceName = HADBDeploymentHelper.getServiceNameByServerDeployName(serviceDeployName);
			addServerByHitory(serverSet, serviceName, serverGroup, newServerCount, needMemory / 2);
		}

		if (serverSet.size() < instances) {
			// 还不够，只能从当前组的其它服务器获取刘
			int newServerCount = instances - serverSet.size();
			LOGGER.info("Add server by other server list. Need server count is {}", newServerCount);
			addServerByOtherServer(serverSet, serverGroup, newServerCount, needMemory);
		}

		if (serverSet.size() == instances) {
			return serverSet;
		} else {
			// 还不够？只能抛错了
			throw new RuntimeException("Server does not have sufficient resources. Missing "
					+ (instances - serverSet.size()) + " units");
		}
	}

	/**
	 * 从该服务名称所使用得历史服务器中选出服务器，加入到本次服务器列表中
	 * 
	 * @param serverSet
	 * @param serviceName
	 * @param serverGroup
	 * @param newServerCount
	 * @throws Exception
	 */
	private void addServerByHitory(Set<String> serverSet, String serviceName, String serverGroup, int newServerCount,
			int needMemory) throws Exception {
		LOGGER.info("Step 3. Get service history deploy info. ServiceName={}, ServerGroup={}", serviceName, serverGroup);
		Set<String> historyServerSet = HADBDeploymentHelper.getServiceHistoryServer(serviceName, serverGroup);
		LOGGER.info("Service [{}] history deploy set is {}", serviceName, serverSet);
		int count = 0;
		for (String server : historyServerSet) {
			// 如果该服务未位于已被选中得服务器列表中，并且有足够的资源，那么选择此台服务器
			if (!serverSet.contains(server) && checkServerResource(server, needMemory)) {
				serverSet.add(server);
				if (++count == newServerCount) {
					break;
				}
			}
		}
		LOGGER.info("Add server by history. New server count is {}", count);
	}

	/**
	 * 验证该台服务器是否有足够资源运行当前程序
	 * 
	 * @param serverName
	 * @param needMemory
	 * @return
	 */
	public boolean checkServerResource(String serverName, int needMemory) {
		try {
			// 总内存数 - 已使用内存 - 系统保留内存得数量是否满足needMemory的需要
			int totalMemory = HADBDeploymentHelper.getServerMemory(serverName);
			int usedMemory = HADBDeploymentHelper.getFreeMemoryByServer(serverName);
			int freeMemeory = totalMemory - usedMemory - SYSTEM_RUN_MEMORY;
			LOGGER.debug(String.format(
					"Server[%s] total memory is %sM,used memory is %sM, run system memory is %sM, free memory is %sM",
					serverName, totalMemory, usedMemory, SYSTEM_RUN_MEMORY, freeMemeory));
			if (freeMemeory >= needMemory) {
				return true;
			}
		} catch (SQLException e) {
			LOGGER.error("Parse server memory error. Ignore server[" + serverName + "]", e);
		}
		return false;
	}

	/**
	 * 添加符合要求得机器加入到服务器列表中
	 * 
	 * @param serverSet
	 * @param serverGroup
	 * @param newServerCount
	 * @return
	 */
	private Set<String> addServerByOtherServer(Set<String> serverSet, String serverGroup, int newServerCount,
			int needMemroy) {

		try {
			LOGGER.info("Get server by serverGroup");
			Set<String> allServer = HADBDeploymentHelper.getServerByServerGroup(serverGroup);
			if (serverSet.size() + newServerCount > allServer.size()) {
				throw new RuntimeException(String.format(
						"Instances count is greater than ServerGroup server.instances is %s, but server count is %s.",
						(serverSet.size() + newServerCount), allServer.size()));
			}
			int count = 0;
			for (String server : allServer) {
				// 如果该server不在服务器列表中，并且资源足够
				if (!serverSet.contains(server) && checkServerResource(server, needMemroy)) {
					serverSet.add(server);
					// 够了就结束
					if (++count == newServerCount) {
						break;
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.error(String.format("Add server failed.Params:serverSet:%s | serverGroup:%s | newServerCount:%s",
					serverSet, serverGroup, newServerCount), e);
		}

		return serverSet;
	}

	/**
	 * 删除制定数量的Server
	 * 
	 * @param serverSet
	 * @param serverGroup
	 * @param newServerCount
	 * @return
	 */
	private Set<String> removeServer(Set<String> serverSet, int removeServerCount) {

		if (removeServerCount > serverSet.size()) {
			throw new RuntimeException("Remove the number of servers exceeds the total number.");
		}

		for (int i = 0; i < removeServerCount; i++) {
			// 每次都移除最后一个
			serverSet.remove(serverSet.size() - 1);
		}

		return serverSet;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		try {
			int instances = 6;
			int needMemory = 1280;
			String serviceName = "test";
			String serverGroup = "BigGroup";
			String originalServiceDeployName = "meetingschedule-681";

			Set<String> serverSet = HAServerDispatcher.getInstance().dispatch(serverGroup, serviceName,
					originalServiceDeployName, instances, needMemory);
			System.out.println(serverSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
}
