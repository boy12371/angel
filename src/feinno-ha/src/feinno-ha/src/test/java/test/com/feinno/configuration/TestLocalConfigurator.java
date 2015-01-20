package test.com.feinno.configuration;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLocalConfigurator extends TestCase {

//	public void testLoadTable() throws IOException, ConfigurationException {
//		LocalConfigurationLoader loader = new LocalConfigurationLoader();
//		ConfiguratorObsoleted configurator = new ConfiguratorObsoleted();
//		configurator
//				.loadTable(
//						String.class,
//						CFGSiteConfigTableItem.class,
//						loader,
//						"CFG_Site",
//						new ConfigUpdateAction<ConfigTable<String, CFGSiteConfigTableItem>>() {
//							@Override
//							public void run(
//									ConfigTable<String, CFGSiteConfigTableItem> a)
//									throws Exception {
//								LOGGER.info(a.getCount() + "");
//							}
//
//						});
//
//	}
//
//	public void testLoadText() throws Exception {
//		LocalConfigurationLoader loader = new LocalConfigurationLoader();
//		ConfiguratorObsoleted configurator = new ConfiguratorObsoleted();
//		configurator.loadText(loader, "FAEDB.properties", null,
//				new ConfigUpdateAction<String>() {
//
//					@Override
//					public void run(String e) throws Exception {
//
//					}
//				});
//	}

	/*
	 * public void testLoadTable() { try { LocalConfigurationLoader loader = new
	 * LocalConfigurationLoader(); Configurator configurator = new
	 * Configurator(); configurator.loadTable(String.class,
	 * CFGSiteConfigTableItem.class, loader, "CFG_Site", new
	 * Action<ConfigTable<String,CFGSiteConfigTableItem>>() {
	 * 
	 * @Override public void run(ConfigTable<String, CFGSiteConfigTableItem> a)
	 * {
	 * 
	 * }
	 * 
	 * } );
	 * 
	 * }catch(Exception e) { Assert.fail(e.toString()); } }
	 * 
	 * 
	 * public void testLoadTable_ConfigKey() { try { LocalConfigurationLoader
	 * loader = new LocalConfigurationLoader(); Configurator configurator = new
	 * Configurator(); configurator.loadTable(CFGSiteConfigTableKey.class,
	 * CFGSiteConfigTableItem.class, loader, "CFG_Site", new
	 * Action<ConfigTable<CFGSiteConfigTableKey,CFGSiteConfigTableItem>>() {
	 * 
	 * @Override public void run(ConfigTable<CFGSiteConfigTableKey,
	 * CFGSiteConfigTableItem> a) {
	 * 
	 * }
	 * 
	 * } );
	 * 
	 * }catch(Exception e) { Assert.fail(e.toString()); } }
	 * 
	 * public void testUpdateConfig() { try { LocalConfigurationLoader loader =
	 * new LocalConfigurationLoader(); Configurator configurator = new
	 * Configurator(); configurator.loadTable(String.class,
	 * CFGSiteConfigTableItem.class, loader, "CFG_Site", new
	 * Action<ConfigTable<String,CFGSiteConfigTableItem>>() {
	 * 
	 * @Override public void run(ConfigTable<String, CFGSiteConfigTableItem> a)
	 * { Assert.assertEquals("CFG_Site", a.getTableName()); }
	 * 
	 * } ); configurator.updateConfig("CFG_Site", ConfigType.TABLE);
	 * 
	 * }catch(Exception e) { Assert.fail(e.toString()); } }
	 * 
	 * public void testLoadFile() { try { LocalConfigurationLoader loader = new
	 * LocalConfigurationLoader(); Configurator configurator = new
	 * Configurator(); configurator.loadText(loader,
	 * "/IICHADBConfig.properties", null, new Action<String>() {
	 * 
	 * @Override public void run(String a) {
	 * 
	 * }
	 * 
	 * });
	 * 
	 * }catch(Exception e) { Assert.fail(e.toString()); } }
	 */

	private static Logger LOGGER = LoggerFactory
			.getLogger(TestLocalConfigurator.class);
}
