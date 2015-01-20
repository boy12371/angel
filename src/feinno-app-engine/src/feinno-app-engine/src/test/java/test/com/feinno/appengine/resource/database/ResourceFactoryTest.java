package test.com.feinno.appengine.resource.database;

//import com.feinno.appengine.imps.GlobalLocator;
//import com.feinno.appengine.imps.PoolLocator;
//import com.feinno.appengine.job.JobResourceLocator;
import com.feinno.appengine.resource.ResourceFactory;
import com.feinno.appengine.resource.database.DatabaseProxy;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.configuration.spi.LocalConfigurator;
import com.feinno.database.DataTable;
//import com.feinno.imps.user.context.UserContext;

import junit.framework.Assert;
import junit.framework.TestCase;

public class ResourceFactoryTest extends TestCase{

	/**
	 * 需要引用feinno-imps，需要AppEnginManager
	 * registerLocatorType("PoolLocator", PoolLocator.class);
		registerLocatorType("GlobalLocator", GlobalLocator.class);
		registerLocatorType("JobResourceLocator", JobResourceLocator.class);
	 */
	public void testDatabaseProxy()
	{
		try {
			ConfigurationManager.setConfigurator(new LocalConfigurator());
			AppEngineManager.INSTANCE.initialize();

			DatabaseProxy proxy = ResourceFactory.getDatabaseProxy("IICUPDB");
	//		UserContext context = new UserContext(1000565664,201718198,21323);
			
			Assert.assertNotNull(proxy);
	//		DataTable table = proxy.executeTable(context, "select * from UP_User where UserId=?", 201718198);
	//		System.out.println(table.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
