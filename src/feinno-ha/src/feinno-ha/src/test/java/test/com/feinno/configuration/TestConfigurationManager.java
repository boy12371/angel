package test.com.feinno.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;

public class TestConfigurationManager {

	@Before
	public void testSetLoader() throws IOException {
//		LocalConfigurationLoader loader = new LocalConfigurationLoader();
//		ConfigurationManager.setConfigurator(new LocalConfigurationLoader());
//		
//		//
//		// ConfigurationManaager.setConfigurator(HAWorkerAgent.getDuplexClient());
	}

	@org.junit.Test
	public void testLoadTable() throws ConfigurationException {
//		ConfigTable<String, CFGSiteConfigTableItem> ct = ConfigurationManager
//				.loadTable(
//						String.class,
//						CFGSiteConfigTableItem.class,
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
//		ct.getTableName();
//		ct.getCount();
//		ct.getHashtable();
//		ct.getKeys();
//		ct.getSet();
//		ct.getValues();
//		ct.getVersion();
//		try {
//			ct.get("1");
//		} catch (IllegalArgumentException e) {
//		}
//		ct.get("SiteC");
	}

	@org.junit.Test
	public void testLoadText() throws ConfigurationException {
		ConfigurationManager.loadText("FAEDB.properties", null,
				new ConfigUpdateAction<String>() {

					@Override
					public void run(String e) throws Exception {

					}
				});
	}

	@org.junit.Test
	public void testUpdateConfig() throws ConfigurationException {
//		ConfigurationManager.updateConfig("CFG_Site", ConfigType.TABLE);
//		ConfigurationManager.updateConfig("FAEDB.properties", ConfigType.TEXT);
//		ConfigurationManager
//				.updateConfig("FAEDB.properties", ConfigType.UNKOWN);
	}

	@org.junit.Test
	public void testLoadTextStream() throws ConfigurationException {
		ConfigurationManager.loadTextStream("FAEDB.properties", null,
				new ConfigUpdateAction<InputStream>() {

					@Override
					public void run(InputStream e) throws Exception {
						LOGGER.info(e.available() + "");

					}

				});
	}

	@org.junit.Test
	public void testloadProperties() throws ConfigurationException {
		Properties prop = ConfigurationManager.loadProperties(
				"FAEDB.properties", null, new ConfigUpdateAction<Properties>() {

					@Override
					public void run(Properties e) throws Exception {
						LOGGER.info(e.isEmpty() + "");

					}

				});
		System.out.println(prop.isEmpty());
	}

	private static Logger LOGGER = LoggerFactory
			.getLogger(TestConfigurationManager.class);
}
