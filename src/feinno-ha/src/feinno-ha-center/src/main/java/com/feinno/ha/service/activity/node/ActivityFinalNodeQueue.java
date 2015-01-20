package com.feinno.ha.service.activity.node;

import java.sql.SQLException;

import com.feinno.database.DataRow;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.ha.ServiceSettings;
import com.feinno.ha.database.HADatabaseFactory;
import com.feinno.logging.common.FireEventQueue;
import com.feinno.util.Action;

public class ActivityFinalNodeQueue {

	public static String centerURL = null;

	public ActivityFinalNodeQueue() {
		if (centerURL == null) {
			centerURL = "tcp://" + ServiceSettings.INSTANCE.getServerAddress() + ":"
					+ ServiceSettings.INSTANCE.getServicePort("rpc_duplex");
		}
	}

	public static void putMasterNode(MasterActivityNode node) {
		masterEvent.add(node);
	}

	private static FireEventQueue<AbstractActivityNode> masterEvent = FireEventQueue.newFreshBoxFireEventQueue(
			1000 * 60 * 10,
			// .newFreshBoxFireEventQueue(1000 * 8,
			new Action<AbstractActivityNode>() {

				public void run(AbstractActivityNode node) {
					node.onExpired();
				}
			});

	public static void initEndpointQueue() {
		try {
			Database db = HADatabaseFactory.getHADatabase();

			DataTable dt = db.executeTable("select ServerName from HA_MasterEndpoint where CenterUrl=?", centerURL);
			for (DataRow dr : dt.getRows()) {
				String computerName = dr.getString("ServerName");
				ActivityFinalNodeQueue.putMasterNode(new MasterActivityNode(computerName));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Has a problem when initialize endpoint.");
		}
	}
}
