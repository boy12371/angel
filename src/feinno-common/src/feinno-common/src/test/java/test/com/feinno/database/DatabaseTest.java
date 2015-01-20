/*
 * FAE, Feinno App Engine
 *  
 * Create by wanglihui 2011-2-15
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.database;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.sql.rowset.CachedRowSet;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.feinno.database.DBSyncResult;
import com.feinno.database.DataReader;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.database.DatabasePerfmon;
import com.feinno.database.MonDBHelper;
import com.feinno.database.Table;
import com.feinno.database.pool.DBConnectionPoolType;
import com.feinno.util.Action;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther wanglihui
 */
public class DatabaseTest extends TestCase {
	
	

//	public void testSay() {
//		System.out.println("注释掉测试方法，因为这些方法连接了数据库，而这个数据库并不存在!");
//	}

	private Properties p = new Properties();
	private Database database = null;
	
	private String[] params = { "@UserId" };
	private String tableName = "TestUser";
	//Tomcat-pool
	//private Properties p2 = new Properties();
	private Database database2 = null;
	//BoneCP
	private Database database3 = null;

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
		dbTableHelper.execute("DROP PROCEDURE IF EXISTS `SP_UpdateUserInfo`; ");
		dbTableHelper.execute("CREATE  PROCEDURE `SP_UpdateUserInfo`(v_UserId	INT,v_Name	VARCHAR(20)) " +
						"BEGIN	" +
						"UPDATE TestUser SET NAME=v_Name WHERE UserId = v_UserId;    " 
						 +" END ");
		//生成5条初始数据
		dbTableHelper.execute("insert into TestUser values(1,'user1',10,'2002-1-1')");
		dbTableHelper.execute("insert into TestUser values(2,'user2',20,'1992-2-2')");
		dbTableHelper.execute("insert into TestUser values(3,'user3',30,'1982-3-3')");
		dbTableHelper.execute("insert into TestUser values(4,'user4',40,'1972-4-4')");
		dbTableHelper.execute("insert into TestUser values(5,'user5',50,'1970-5-5')");
		
		database = DatabaseManager.getDatabase("test2", p,DBConnectionPoolType.C3p0);
		
		//Tomcat-pool
//		p2.setProperty("driverClassName", "com.mysql.jdbc.Driver");
//		p2.setProperty(
//				"url",
//				"jdbc:mysql://192.168.110.234/test?autoReconnect=true&cacheCallableStmts=true&callableStmtCacheSize=512&tinyInt1isBit=false&useDynamicCharsetInfo=false&zeroDateTimeBehavior=round");
//		p2.setProperty("username", "admin");
//		p2.setProperty("password", "admin");
		database2 = DatabaseManager.getDatabase("test3", p,DBConnectionPoolType.TomcatPool);
		
		database3 = DatabaseManager.getDatabase("test4", p,DBConnectionPoolType.BoneCP);
		
		DatabasePerfmon.initialize();
	}

	public void testSpExecuteNonQuery() {
		int i;
		try {
			i = database.spExecuteNonQuery("SP_UpdateUserInfo", new String[] {
					"@UserId", "@Name" }, 1, "changeName");
			Assert.assertEquals(i, 1);
		} catch (SQLException e) {
			Assert.assertEquals(1, 2);
		}
	}
	
	public void testSpExecuteNonQueryByTomcatPool() {
		int i;
		try {
			i = database2.spExecuteNonQuery("SP_UpdateUserInfo", new String[] {
					"@UserId", "@Name" }, 1, "changeName2");
			Assert.assertEquals(i, 1);
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	public void testSpExecuteNonQueryByBoneCP() {
		int i;
		try {
			i = database3.spExecuteNonQuery("SP_UpdateUserInfo", new String[] {
					"@UserId", "@Name" }, 1, "changeName3");
			Assert.assertEquals(i, 1);
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
	}

//	public void testSpExecuteReader() {
//		DataReader reader = null;
//		try {
//			reader = database.spExecuteReader("SP_GetUserInfo", params, "1");
//			if (reader.read()) {
//				// System.out.println(reader.getString(1));
//				Assert.assertEquals(reader.getString(1), "1");
//			}
//		} catch (SQLException e) {
//			Assert.assertEquals(1, 2);
//		} finally {
//			reader.close();
//		}
//	}

	public void testSpExecuteTable() {
		DataTable dataTable;
		try {
			dataTable =  database.spExecuteTable("SP_GetUserInfo", params, "1");
			if (dataTable.getRowCount() > 0)
				// System.out.println(dataTable.getRow(0).getObject(1));
				Assert.assertEquals(
						dataTable.getRow(0).getObject(1).toString(), "1");
		} catch (SQLException e) {
			Assert.assertEquals(1, 2);
		}
	}
	
	public void testSpExecuteTabless() {
		DataTable dataTable;
		try {
			List<DataTable> tables =  database.spExecuteTables("SP_GetUserInfo", params, "1");
			dataTable = tables.get(0);
			if (dataTable.getRowCount() > 0)
				// System.out.println(dataTable.getRow(0).getObject(1));
				Assert.assertEquals(
						dataTable.getRow(0).getObject(1).toString(), "1");
		} catch (SQLException e) {
			Assert.assertEquals(1, 2);
		}
	}

	public void testExecuteNonQuery() {
		try {
			database.executeNonQuery(
					"update TestUser set Name=? where UserId=?", "testest", 2);
		} catch (SQLException e) {
			Assert.assertEquals(1, 2);
		}
	}

//	public void testExecuteReader() {
//		DataReader reader = null;
//		try {
//			reader = database.executeReader(
//					"select * from TestUser where UserId =?", 1);
//			if (reader.read()) {
//				// System.out.println(reader.getString(1) + "---" +
//				// reader.getString(2));
//				Assert.assertEquals(reader.getString(1), "1");
//			}
//		} catch (SQLException e) {
//			Assert.assertEquals(1, 2);
//		} finally {
//			reader.close();
//		}
//	}
	
//	public void testExecuteReader2() {
//		DataReader reader = null;
//		try {
//			reader = database.executeReader(
//					"select * from TestUser where UserId =1");
//			if (reader.read()) {
//				// System.out.println(reader.getString(1) + "---" +
//				// reader.getString(2));
//				Assert.assertEquals(reader.getString(1), "1");
//			}
//		} catch (SQLException e) {
//			Assert.assertEquals(1, 2);
//		} finally {
//			reader.close();
//		}
//	}

	public void testExecuteTable() {
		try {
			DataTable dataTable = database.executeTable(
					"select * from TestUser where UserId =?", 1);
			if (dataTable.getRowCount() > 0)
				System.out.println(dataTable.getRow(0).getObject(1) + "---"
						+ dataTable.getRow(0).getObject(2));
			Assert.assertEquals(dataTable.getRow(0).getObject(1).toString(),
					"1");
		} catch (SQLException e) {
			Assert.assertEquals(1, 2);
		}
	}
	
	public void testExecuteTables() {
		try {
			
			List<DataTable> tables = database.executeTables(
					"select * from TestUser where UserId =?", 1);
			DataTable dataTable = tables.get(0);
			if (dataTable.getRowCount() > 0)
				System.out.println(dataTable.getRow(0).getObject(1) + "---"
						+ dataTable.getRow(0).getObject(2));
			Assert.assertEquals(dataTable.getRow(0).getObject(1).toString(),
					"1");
		} catch (SQLException e) {
			Assert.assertEquals(1, 2);
		}
	}
	
	//executeInsertWithAutoColumn
	public void testExecuteInsertWithAutoColumn()
	{
		try {
			long id = database.executeInsertWithAutoColumn("insert into TestUser(Name,Age,Birthday) values(?,?,?)", "insert",16,"1996-10-10");
			Assert.assertTrue(id>0);
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	public void testExcuteRowSet() {
		try {
			CachedRowSet rowSet = database.excuteRowSet(
					"select * from TestUser where UserId =?", 1);
			if (rowSet.size() > 0)
			{
				rowSet.next();
					System.out.println(rowSet.getObject(1)+ "---"
							+ rowSet.getObject(2));
				Assert.assertEquals(rowSet.getObject(1).toString(),
						"1");
			}
			else
				Assert.fail("get no row");
		} catch (SQLException e) {
			Assert.assertEquals(1, 2);
		}
	}
	
	//spExecuteRowSet
	public void testSpExecuteRowSet() {
		CachedRowSet rowSet;
		try {
			rowSet =  database.spExecuteRowSet("SP_GetUserInfo", params, "1");
			if (rowSet.size() > 0)
			{
				rowSet.next();
					System.out.println(rowSet.getObject(1)+ "---"
							+ rowSet.getObject(2));
				Assert.assertEquals(rowSet.getObject(1).toString(),
						"1");
			}
			else
				Assert.fail("get no row");
		} catch (SQLException e) {
			Assert.assertEquals(1, 2);
		}
	}

	

	public void testGetPrimary() {
		DataReader reader = null;
		try {
			reader = database.getPrimaryKeys(null, null, "TestUser");
			while (reader.read()) {
				System.out.print("TABLE_CAT:" + reader.getString(1));
				System.out.print(" TABLE_SCHEM:" + reader.getString(2));
				System.out.print(" TABLE_NAME:" + reader.getString(3));
				System.out.print(" COLUMN_NAME:" + reader.getString(4));
				System.out.print(" KEY_SEQ:" + reader.getString(5));
				System.out.print(" PK_NAME:" + reader.getString(6));
				System.out.println();
			}
		} catch (SQLException e) {
			Assert.assertEquals(1, 2);
		} finally {
			reader.close();
		}
	}

	public void testGetTables() {
		DataReader reader = null;
		try {
			reader = database.getTables(null, null, null,
					null);
			while (reader.read()) {
				System.out.print("TABLE_CAT:" + reader.getString(1));
				System.out.print(" TABLE_SCHEM:" + reader.getString(2));
				System.out.print(" TABLE_NAME:" + reader.getString(3));
				System.out.print(" TABLE_TYPE:" + reader.getString(4));
				System.out.print(" REMARKS:" + reader.getString(5));
//				System.out.print(" TYPE_CAT:" + reader.getString(6));
//				System.out.print(" TYPE_SCHEM:" + reader.getString(7));
//				System.out.print(" TYPE_NAME:" + reader.getString(8));
//				System.out.print(" SELF_REFERENCING_COL_NAME:"
//						+ reader.getString(9));
//				System.out.print(" REF_GENERATION:" + reader.getString(10));
				System.out.println();
			}
		} catch (SQLException e) {
			Assert.assertEquals(1, 2);
		} finally {
			reader.close();
		}
	}

	public void testGetColumns() {
		DataReader reader = null;
		try {
			reader = database.getColumns("TEST", "dbo", "TestUser", "name");
			while (reader.read()) {
				System.out.print("TABLE_CAT:" + reader.getString(1));
				System.out.print(" TABLE_SCHEM:" + reader.getString(2));
				System.out.print(" TABLE_NAME:" + reader.getString(3));
				System.out.print(" COLUMN_NAME:" + reader.getString(4));
				System.out.print(" DATA_TYPE:" + reader.getString(5));
				System.out.print(" TYPE_NAME:" + reader.getString(6));
				System.out.print(" COLUMN_SIZE:" + reader.getString(7));
				System.out.print(" BUFFER_LENGTH:" + reader.getString(8));
				System.out.print(" DECIMAL_DIGITS:" + reader.getString(9));
				System.out.print(" NUM_PREC_RADIX:" + reader.getString(10));
				System.out.print(" NULLABLE:" + reader.getString(11));
				System.out.print(" REMARKS:" + reader.getString(12));
				System.out.print(" COLUMN_DEF:" + reader.getString(13));
				System.out.print(" SQL_DATA_TYPE:" + reader.getString(14));
				System.out.print(" SQL_DATETIME_SUB:" + reader.getString(15));
				System.out.print(" CHAR_OCTET_LENGTH:" + reader.getString(16));
				System.out.print(" ORDINAL_POSITION:" + reader.getString(17));
				System.out.print(" IS_NULLABLE:" + reader.getString(18));
				System.out.print(" SCOPE_CATLOG:" + reader.getString(19));
				System.out.print(" SCOPE_SCHEMA:" + reader.getString(20));
				System.out.print(" SCOPE_TABLE:" + reader.getString(21));
				System.out.print(" SOURCE_DATA_TYPE:"
						+ reader.getString("SOURCE_DATA_TYPE"));
				System.out.print(" IS_AUTOINCREMENT:"
						+ reader.getString("IS_AUTOINCREMENT"));
				// 没有此属性
				// System.out.println();
			}
		} catch (SQLException e) {
			Assert.assertEquals(1, 2);
		} finally {
			reader.close();
		}
	}

	//spExecuteNonQueryAsync
	
//
//	public void testSpExecuteNonQueryAsync() {
//		database.spExecuteNonQueryAsync("SP_UpdateUserInfo", new String[] {
//				"@UserId", "@Name" }, new Action<DBSyncResult<Integer>>(){
//
//					@Override
//					public void run(DBSyncResult<Integer> a) {
//						if(a.getException()==null)
//						{
//							int i = a.getResult().intValue();
//							Assert.assertEquals(i, 1);
//						}
//						else
//							Assert.fail(a.getException().getMessage());
//					}
//
//						
//				} ,3, "xxxAsync");
//		
//		try {
//			Thread.sleep(5*1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
//
//	
//	//spExecuteReaderAsync
//	
////	public void testSpExecuteReaderAsync() {
////
////		database.spExecuteReaderAsync("SP_GetUserInfo", params, new Action<DBSyncResult<DataReader>>(){
////
////					@Override
////					public void run(DBSyncResult<DataReader> a) {
////						if(a.getException()==null)
////						{
////							DataReader reader = a.getResult();
////							try {
////								if (reader.read()) {
////									Assert.assertEquals(reader.getString(1), "3");
////								}
////								else 
////									Assert.fail("have no user");
////							} catch (SQLException e) {
////								Assert.fail(e.getMessage());
////							}
////						}
////						else
////							Assert.fail(a.getException().getMessage());
////					}
////
////						
////				} ,3);
////		
////		try {
////			Thread.sleep(5*1000);
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////	
////}
//
//	
//	//spExecuteTableAsync
//	public void testSpExecuteTableAsync() {
//
//		database.spExecuteTableAsync("SP_GetUserInfo", params, new Action<DBSyncResult<DataTable>>(){
//
//					@Override
//					public void run(DBSyncResult<DataTable> a) {
//						if(a.getException()==null)
//						{
//							DataTable table = a.getResult();
//							try {
//								if(table.getRowCount()>0)
//									Assert.assertEquals(table.getRow(0).getString(1), "3");			
//								else 
//									Assert.fail("have no user");
//							} catch (SQLException e) {
//								Assert.fail(e.getMessage());
//							}
//						}
//						else
//							Assert.fail(a.getException().getMessage());
//					}
//
//						
//				} ,3);
//		
//		try {
//			Thread.sleep(5*1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	
//}
//	//spExecuteTableAsync
//		public void testSpExecuteTablesAsync() {
//
//			database.spExecuteTablesAsync("SP_GetUserInfo", params, new Action<DBSyncResult<List<DataTable>>>(){
//
//						@Override
//						public void run(DBSyncResult<List<DataTable>> a) {
//							if(a.getException()==null)
//							{
//								DataTable table = a.getResult().get(0);
//								try {
//									if(table.getRowCount()>0)
//										Assert.assertEquals(table.getRow(0).getString(1), "3");			
//									else 
//										Assert.fail("have no user");
//								} catch (SQLException e) {
//									Assert.fail(e.getMessage());
//								}
//							}
//							else
//								Assert.fail(a.getException().getMessage());
//						}
//
//							
//					} ,3);
//			
//			try {
//				Thread.sleep(5*1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		
//	}
//	
//	//executeNonQueryAsync	
//	public void testExecuteNonQueryAsync()
//	{
//		database.executeNonQueryAsync("update TestUser set name='NonQueryAsync'  where UserId=5", new Action<DBSyncResult<Integer>>()
//				{
//					@Override
//					public void run(DBSyncResult<Integer> a) {
//						if(a.getException() == null)
//						{
//							int i = a.getResult().intValue();
//							Assert.assertEquals(1, i);
//						}
//						else
//							Assert.fail("have not userId=5");
//						
//					}
//			
//				});
//		
//		try {
//			Thread.sleep(5*1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	//executeInsertWithAutoColumnAsync
//	public void testExecuteInsertWithAutoColumnAsync()
//	{
//		database.executeInsertWithAutoColumnAsync("insert into TestUser set name='insertAsync',Age=29,Birthday='1981-12-12'", new Action<DBSyncResult<Long>>()
//				{
//					public void run(DBSyncResult<Long> a) {
//						if(a.getException() == null)
//						{
//							int i = a.getResult().intValue();
//							Assert.assertTrue(i>0);
//						}
//						else
//							Assert.fail("fail");
//						
//					}
//		
//				});
//		
//		try {
//			Thread.sleep(5*1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	//executeReaderAsync
////	public void testExecuteReaderAsync()
////	{
////		database.executeReaderAsync("select * from TestUser", new Action<DBSyncResult<DataReader>>()
////				{
////
////					@Override
////					public void run(DBSyncResult<DataReader> a) {
////						if(a.getException() == null)
////						{
////							DataReader reader = a.getResult();
////							try {
////								Assert.assertTrue(reader.read());
////							} catch (SQLException e) {
////								Assert.fail(e.getMessage());
////							}
////						}
////						else
////							Assert.fail(a.getException().getMessage());
////						
////					}
////			
////				});
////		
////		try {
////			Thread.sleep(5*1000);
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////	}
//	
//	//executeTableAsync
//	public void testExecuteTableAsync()
//	{
//		database.executeTableAsync("select * from TestUser", new Action<DBSyncResult<DataTable>>()
//				{
//
//					@Override
//					public void run(DBSyncResult<DataTable> a) {
//						if(a.getException() == null)
//						{
//							DataTable table = a.getResult();
//							Assert.assertTrue(table.getRowCount()>0);
//						}
//						else
//							Assert.fail(a.getException().getMessage());
//						
//					}
//			
//				});
//		
//		try {
//			Thread.sleep(5*1000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	//executeTableAsync
//		public void testExecuteTablesAsync()
//		{
//			database.executeTablesAsync("select * from TestUser", new Action<DBSyncResult<List<DataTable>>>()
//					{
//
//						@Override
//						public void run(DBSyncResult<List<DataTable>> a) {
//							if(a.getException() == null)
//							{
//								DataTable table = a.getResult().get(0);
//								Assert.assertTrue(table.getRowCount()>0);
//							}
//							else
//								Assert.fail(a.getException().getMessage());
//							
//						}
//				
//					});
//			
//			try {
//				Thread.sleep(5*1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	
	//Exception
	public void testSpExecuteNonQueryException() {
		int i;
		try {
			i = database.spExecuteNonQuery("SP_UpdateUserInfo", new String[] {
					"@UserId", "@Name" }, 1, "changeName");
		} catch (SQLException e) {
			Assert.assertEquals(1, 1);
		}
	}

//	public void testSpExecuteReaderException() {
//		DataReader reader = null;
//		try {
//			reader = database.spExecuteReader("SP_GetUserInfo1111", params, "1");
//			
//		} catch (Exception e) {
//			Assert.assertEquals(1, 1);
//		} finally {
//			if(reader!=null)
//				reader.close();
//		}
//	}

	public void testSpExecuteTableException() {
		DataTable dataTable;
		try {
			dataTable =  database.spExecuteTable("SP_GetUserInfo111", params, "1");
		
		} catch (Exception e) {
			Assert.assertEquals(1, 1);
		}
	}

	public void testExecuteNonQueryException() {
		try {
			database.executeNonQuery(
					"update update TestUser set Name=? where UserId=?", "testest", 2);
		} catch (SQLException e) {
			Assert.assertEquals(1, 1);
		}
	}

//	public void testExecuteReaderException() {
//		DataReader reader = null;
//		try {
//			reader = database.executeReader(
//					"select *@@@ from TestUser where UserId =?", 1);
//			
//		} catch (SQLException e) {
//			Assert.assertEquals(1, 1);
//		} finally {
//			if(reader!=null)
//				reader.close();
//		}
//	}
	
	

	public void testExecuteTableException() {
		try {
			DataTable dataTable = database.executeTable(
					"select @@@@@* from TestUser where UserId =?", 1);
			
		} catch (SQLException e) {
			Assert.assertEquals(1, 1);
		}
	}
	
	//executeInsertWithAutoColumn
	public void testExecuteInsertWithAutoColumnException()
	{
		try {
			long id = database.executeInsertWithAutoColumn("insert into TestUser1111(Name,Age,Birthday) values(?,?,?)", "insert",16,"1996-10-10");

		} catch (SQLException e) {
			Assert.assertEquals(1, 1);
		}
	}
	
	public void testExcuteRowSetException() {
		try {
			CachedRowSet rowSet = database.excuteRowSet(
					"select * ### from TestUser where UserId =?", 1);

		} catch (SQLException e) {
			Assert.assertEquals(1, 1);
		}
	}
	
	//spExecuteRowSet
	public void testSpExecuteRowSetException() {
		CachedRowSet rowSet;
		try {
			rowSet =  database.spExecuteRowSet("SP_GetUserInfo1111", params, "1");
			
		} catch (Exception e) {
			Assert.assertEquals(1, 1);
		}
	}

	

	public void testGetPrimaryException() {
		DataReader reader = null;
		try {
			reader = database.getPrimaryKeys(null, null, "TestUser1111");
			
		} catch (SQLException e) {
			Assert.assertEquals(1, 1);
		} finally {
			if(reader!=null)
				reader.close();
		}
	}

	

	public void testGetColumnsException() {
		DataReader reader = null;
		try {
			reader = database.getColumns("TEST", "dbo", "TestUse11111r", "name");
			
		} catch (SQLException e) {
			Assert.assertEquals(1, 1);
		} finally {
			if(reader!=null)
				reader.close();
		}
	}

}
