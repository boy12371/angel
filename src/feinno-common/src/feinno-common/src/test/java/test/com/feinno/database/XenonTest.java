package test.com.feinno.database;

import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

import com.feinno.database.DataReader;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.database.pool.DBConnectionPoolType;

public class XenonTest {
	
	public static void main(String[] args) 	{
		try
		{
		Properties p = new Properties();
		p.setProperty("DriverClass", "com.mysql.jdbc.Driver");
		p.setProperty(
				"JdbcUrl",
				"jdbc:mysql://192.168.251.35/test?noAccessToProcedureBodies=true&autoReconnect=true&cacheCallableStmts=true&callableStmtCacheSize=512&tinyInt1isBit=false&useDynamicCharsetInfo=false&zeroDateTimeBehavior=round");
		p.setProperty("user", "admin");
		p.setProperty("password", "admin");
		p.setProperty("acquireIncrement", "5");
		p.setProperty("acquireRetryDelay", "500");
		p.setProperty("checkoutTimeout", "15000");
		p.setProperty("initialPoolSize", "5");
		p.setProperty("maxPoolSize", "10");
		p.setProperty("numHelperThreads", "3");
		p.setProperty("maxIdleTime", "30");
		p.setProperty("automaticTestTable", "c3p0Test");
		
		/*acquireIncrement=5
				acquireRetryDelay=500
				checkoutTimeout=15000
				initialPoolSize=5
				minPoolSize=5
				maxPoolSize=10
				numHelperThreads=3
				maxIdleTime=30
				automaticTestTable=c3p0Test*/
		
		Database database = DatabaseManager.getDatabase("test", p,DBConnectionPoolType.C3p0);
		
		//database.spExecuteNonQuery("USP_DG_GetOfflineNotify", new String[] {
		//		"@UserId", "@Name" }, 1, "changeName2");
		
			while(true)
			{
				Thread.sleep(1000);
				DataReader reader = database.executeReader("select 1");
				Date d = new Date();
				reader.read();
				System.out.println(d.toString() + ":"+reader.getInt(1));
				reader.close();
			}
		}catch(Exception ex)
		{
			System.out.println(ex.toString());
		}
	}

}
