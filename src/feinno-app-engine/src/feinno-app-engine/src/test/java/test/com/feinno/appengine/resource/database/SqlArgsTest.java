package test.com.feinno.appengine.resource.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.rowset.CachedRowSet;

import org.junit.Assert;
import com.feinno.appengine.resource.database.SqlArgs;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.database.MonDBHelper;
import com.feinno.database.Table;
import com.feinno.database.pool.DBConnectionPoolType;

import junit.framework.TestCase;

public class SqlArgsTest extends TestCase {
	
	private Properties p = new Properties();
	private Database database = null;
	
	private String[] params = { "@UserId" };
	private String tableName = "TestUser";
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		//C3p0
		p.setProperty("DriverClass", "com.mysql.jdbc.Driver");
		p.setProperty(
				"JdbcUrl",
				"jdbc:mysql://192.168.110.234/test?autoReconnect=true&cacheCallableStmts=true&callableStmtCacheSize=512&tinyInt1isBit=false&useDynamicCharsetInfo=false&zeroDateTimeBehavior=round");
		p.setProperty("user", "admin");
		p.setProperty("password", "admin");
		//生成表
		MonDBHelper dbTableHelper = DatabaseManager.getMonDBHelper("test", p);

		// Step1. 创建一个表
		dbTableHelper.execute("drop table if exists `"+ tableName +"`");
		Table table = new Table(tableName);
		table.addColumns(Table.Column.createAutoIncrementIntColumn("UserId"));
		table.addColumns(Table.Column.createVarcharColumn("Name", 20, true, null));
		table.addColumns(Table.Column.createIntColumn("Age", false, 20));
		table.addColumns(Table.Column.createDateTimeColumn("Birthday", false, "'2000-1-1'"));
		table.addPrimaryKey("UserId");
		dbTableHelper.createTable(table);
		//生成存储过程
		dbTableHelper.execute("DROP PROCEDURE IF EXISTS `SP_GetUserInfo`;");
		dbTableHelper.execute("CREATE  PROCEDURE `SP_GetUserInfo`(v_UserId	INT) " +
								"BEGIN	" +
								"SELECT * FROM TestUser WHERE UserId=V_UserId;    " 
								 +" END ");
				
		//生成5条初始数据
		dbTableHelper.execute("insert into TestUser values(1,'user1',10,'2002-1-1')");
		dbTableHelper.execute("insert into TestUser values(2,'user2',20,'1992-2-2')");
		dbTableHelper.execute("insert into TestUser values(3,'user3',30,'1982-3-3')");
		dbTableHelper.execute("insert into TestUser values(4,'user4',40,'1972-4-4')");
		dbTableHelper.execute("insert into TestUser values(5,'user5',50,'1970-5-5')");
				
		database = DatabaseManager.getDatabase("test2", p,DBConnectionPoolType.C3p0);
		
	}
//	
//	public void testSerialize()
//	{
//		CachedRowSet rowSet;
//		try {
//			rowSet =  database.spExecuteRowSet("SP_GetUserInfo", params, "1");
//			List<CachedRowSet> list = new ArrayList<CachedRowSet>();
//			list.add(rowSet);
//			
//			byte[] bs = SqlArgs.encodeRowSet(list);
//			
//			List<CachedRowSet> list2 = (List<CachedRowSet>) SqlArgs.decodeRowSet(bs);
//			
//			CachedRowSet rowSet2 = list2.get(0);
//			
//			if (rowSet2.size() > 0)
//			{
//				rowSet2.next();
//					System.out.println(rowSet2.getObject(1)+ "---"
//							+ rowSet2.getObject(2));
//				Assert.assertEquals(rowSet2.getObject(1).toString(),
//						"1");
//			}
//			else
//				Assert.fail("get no row");
//			
//		}catch(Exception ex)
//		{
//			Assert.fail(ex.getMessage());
//		}
//	}
	
	public void testSerializeMillion(){
		CachedRowSet rowSet;
		try {
			rowSet =  database.spExecuteRowSet("SP_GetUserInfo", params, "1");
			List<CachedRowSet> list = new ArrayList<CachedRowSet>();
			list.add(rowSet);
			List<CachedRowSet> list2 = null;
			long start = System.nanoTime();
			byte[] bs = null;
			for(int i=0;i<100000;i++)
			{
				 bs = SqlArgs.encodeRowSet(list);
				
			    list2 = (List<CachedRowSet>) SqlArgs.decodeRowSet(bs);
				
			}
			long cost = System.nanoTime() - start;
			
			System.out.println("100000 cost time(ms):"+cost/1E6);
			System.out.println("bytes size is:" + bs.length);
			
			CachedRowSet rowSet2 = list2.get(0);
			if (rowSet2.size() > 0)
			{
				rowSet2.next();
					System.out.println(rowSet2.getObject(1)+ "---"
							+ rowSet2.getObject(2));
				Assert.assertEquals(rowSet2.getObject(1).toString(),
						"1");
			}
			else
				Assert.fail("get no row");
			
		}catch(Exception ex)
		{
			Assert.fail(ex.getMessage());
		}
	}
	
	

}
