package com.feinno.ha.service.activity.node;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.center.MasterAgentService;
import com.feinno.ha.database.HADatabaseFactory;

public class MasterActivityNode extends AbstractActivityNode {

	public MasterActivityNode(String name) {
		super();
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MasterActivityNode)
			return name.equals(((MasterActivityNode) obj).name);
		else
			return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public void onExpired() {
		updateMasterStatus(name);
		MasterAgentService.INSTANCE.deleteMasterAgent(name);
		LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>The Master " + name + " is removed");
	}

	public static int updateMasterStatus(String serverName) {
		String cmd = "update HA_MasterEndpoint set Status=? where ServerName=?";
		int r = 0;
		try {
			r = HADatabaseFactory.getHADatabase().executeNonQuery(cmd, "DISCONNECTED", serverName);
			if (r == 0)
				LOGGER.warn("The Master:" + serverName + "is invalid.Can not be removed in ha_masterendpoint.");
		} catch (SQLException e) {
			throw new RuntimeException(e.getCause());
		}
		return r;
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(MasterActivityNode.class);
	private String name;
}
