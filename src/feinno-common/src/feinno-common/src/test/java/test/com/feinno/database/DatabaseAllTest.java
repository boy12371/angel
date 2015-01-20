package test.com.feinno.database;

import java.sql.SQLException;

import com.feinno.database.DBSyncResult;
import com.feinno.database.DataColumn;
import com.feinno.database.DatabaseHelper;

import junit.framework.Assert;
import junit.framework.TestCase;

public class DatabaseAllTest extends TestCase {
	
	public void testDataColumn()
	{
		DataColumn dc = new DataColumn();
		dc.setColumnName("Test");
		dc.setSqlType(0);
		dc.setDataTable(null);
		DataColumn dc2 = new DataColumn(null);
	}
	
	public void testDBSyncResult()
	{
		DBSyncResult sbsr = new DBSyncResult();
		sbsr.setException(new Exception("test"));
	}

	public void testDatabaseHelper()
	{
		try {
			DatabaseHelper.getCachedRowSet(null);
			DatabaseHelper.getSqlTrace("select * from test", new String[]{"p1","ps"}, new Object[]{1,2});
			
		} catch (SQLException e) {
			Assert.fail(e.toString());
		}
		
	}
}
