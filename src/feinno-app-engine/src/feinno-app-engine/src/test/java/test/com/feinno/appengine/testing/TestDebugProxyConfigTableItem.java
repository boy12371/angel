package test.com.feinno.appengine.testing;

import java.io.IOException;

import com.feinno.appengine.testing.FAEDebugProxyConfigTableItem;
import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigurationException;
import com.feinno.configuration.ConfigurationManager;

public class TestDebugProxyConfigTableItem
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		try {
			getURL();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static ConfigTable<String, FAEDebugProxyConfigTableItem> getURL() throws ConfigurationException, IOException
	{
		// LocalConfigurationLoader loader = new LocalConfigurationLoader();
		// ConfigurationManager.setLoader(loader);
		ConfigTable<String, FAEDebugProxyConfigTableItem> ct = ConfigurationManager.loadTable(String.class,
				FAEDebugProxyConfigTableItem.class, "FAE_DebugProxy", null);
		return ct;
	}

}
