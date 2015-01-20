package test.com.feinno.ha.center.worker;

import java.util.Hashtable;

import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.spi.LocalConfigurator;
import com.feinno.imps.configuration.table.CFG_LogicalPool;

public class TestDatabase {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		ConfigurationManager.setConfigurator(new LocalConfigurator());
		ConfigurationManager.loadTable(Integer.class, CFG_LogicalPool.class, "CFG_LogicalPool",
				new ConfigUpdateAction<ConfigTable<Integer, CFG_LogicalPool>>() {
					@Override
					public void run(ConfigTable<Integer, CFG_LogicalPool> a) {
						Hashtable<Integer, Integer> dic = new Hashtable<Integer, Integer>();
						for (CFG_LogicalPool l : a.getValues()) {
							dic.put(l.getLogicalPoolId(), l.getPhysicalPoolId());
						}
					}
				});
		final long startTime = System.currentTimeMillis();
		ConfigurationManager.loadTable(Integer.class, CFG_LogicalPool.class, "CFG_LogicalPool",
				new ConfigUpdateAction<ConfigTable<Integer, CFG_LogicalPool>>() {
					@Override
					public void run(ConfigTable<Integer, CFG_LogicalPool> a) {
						Hashtable<Integer, Integer> dic = new Hashtable<Integer, Integer>();
						for (CFG_LogicalPool l : a.getValues()) {
							dic.put(l.getLogicalPoolId(), l.getPhysicalPoolId());
						}
						System.out.println(System.currentTimeMillis() - startTime + "ms.");
					}
				});
	}

}
