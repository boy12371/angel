package test.com.feinno.database;

import java.sql.SQLException;
import java.util.Properties;

import com.feinno.database.DataReader;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.database.pool.DBConnectionPoolType;

import junit.framework.TestCase;

public class DatabaseTestSqlServer extends TestCase {
	/**
	 * DriverClass=net.sourceforge.jtds.jdbc.Driver
JdbcUrl=jdbc:jtds:sqlserver://192.168.110.119:2387/IICHADB
User=dbreader
Password=Password01!
	 */
	public void testConnect()
	{
		Properties p = new Properties();
		p.setProperty("DriverClass", "net.sourceforge.jtds.jdbc.Driver");
		p.setProperty("JdbcUrl","jdbc:jtds:sqlserver://192.168.110.119;instance=global01;integratedSecurity=true;databasename=IICHADB");
		//p.setProperty("JdbcUrl","jdbc:jtds:sqlserver://192.168.110.119:2387/IICHADB");
		p.setProperty("user", "dbreader");
		p.setProperty("password", "Password01!");
		
		Database database = DatabaseManager.getDatabase("IICHADB", p,DBConnectionPoolType.C3p0);
		String spName = "USP_LoadConfigTable";
		String[] params = {"@TableName"};
		Object[] values = {"CFG_Site"};
		
		try {
			DataTable table = database.spExecuteTable(spName, params, values);
			System.out.println(table.getRowCount());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
