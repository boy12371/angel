package test.com.feinno.configuration;

import junit.framework.TestCase;

public class TestHAConfigurationLoader extends TestCase {

	public void testLoadTable() {
		try {
			// ServiceSettings.getCurrent().setCenterUrl("tcp://192.168.110.250:11211");
			// ServiceSettings.getCurrent().setServerName("LCL");
			/*
			 * HAConfigurationLoader loader = new
			 * HAConfigurationLoader("testApp"); ConfigTableBuffer buffer =
			 * loader.loadTable("CFG_Site");
			 * System.out.println(buffer.getVersion().toString());
			 */
			// Assert.assertTrue(buffer.rowCount()>0);
			// ServiceSettings.getCurrent().setWorkerName("SAP");
			// HAConfigurationLoader loader = new HAConfigurationLoader();
			// ConfigTextBuffer buffer = loader.loadText("appengine.properties",
			// null);
			// System.out.println(buffer.getText());
			// ConfigurationManager.setLoader(loader);
			/*
			 * Properties p =
			 * ConfigurationManager.loadProperties("appengine.properties", null,
			 * null); System.out.println(p.get("debugMode"));
			 */

			// System.in.read();

		} catch (Throwable t) {
			t.printStackTrace();
			System.out.println("error");
		}
	}

}
