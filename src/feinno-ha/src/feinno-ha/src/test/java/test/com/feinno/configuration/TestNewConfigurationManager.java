package test.com.feinno.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigType;
import com.feinno.configuration.ConfigUpdateAction;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.spi.LocalConfigurator;

public class TestNewConfigurationManager {

	@Before
	public void testSetLoader() throws IOException {
		ConfigurationManager.setConfigurator(new LocalConfigurator());

		//
		// ConfigurationManaager.setConfigurator(HAWorkerAgent.getDuplexClient());
	}

	@org.junit.Test
	public void testLoadTable() throws ConfigurationException {
		ConfigTable<String, CFGSiteConfigTableItem> ct = ConfigurationManager.loadTable(String.class,
				CFGSiteConfigTableItem.class, "CFG_Site",
				new ConfigUpdateAction<ConfigTable<String, CFGSiteConfigTableItem>>() {
					@Override
					public void run(ConfigTable<String, CFGSiteConfigTableItem> a) throws Exception {
						LOGGER.info(a.getCount() + "");
					}

				});
		ct.getTableName();
		ct.getCount();
		ct.getHashtable();
		ct.getKeys();
		ct.getSet();
		ct.getValues();
		ct.getVersion();
		try {
			ct.get("1");
		} catch (IllegalArgumentException e) {
		}
		ct.get("SiteC");
	}

	@org.junit.Test
	public void testLoadText() throws ConfigurationException {
		ConfigurationManager.loadText("HA.properties", null, new ConfigUpdateAction<String>() {
			@Override
			public void run(String e) throws Exception {
				System.out.println(e);
			}
		});
	}

	@org.junit.Test
	public void testUpdateConfig() throws ConfigurationException {
		ConfigurationManager.updateConfig(ConfigType.TABLE, "CFG_Site", "");
		ConfigurationManager.updateConfig(ConfigType.TEXT, "HA.properties", "");
		ConfigurationManager.updateConfig(ConfigType.UNKOWN, "HA.properties", "");
	}

	@org.junit.Test
	public void testLoadTextStream() throws ConfigurationException {
		ConfigurationManager.loadTextStream("HA.properties", null, new ConfigUpdateAction<InputStream>() {

			@Override
			public void run(InputStream e) throws Exception {
				LOGGER.info(e.available() + "");
			}

		});
	}

	@org.junit.Test
	public void testloadProperties() throws ConfigurationException {
		Properties prop = ConfigurationManager.loadProperties("HA.properties", null,
				new ConfigUpdateAction<Properties>() {

					@Override
					public void run(Properties e) throws Exception {
						LOGGER.info(e.isEmpty() + " "+ e);
					}

				});
		System.out.println(prop.isEmpty());
	}

	private static Logger LOGGER = LoggerFactory.getLogger(TestNewConfigurationManager.class);
}
