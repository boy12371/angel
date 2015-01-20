/*
 * FAE, Feinno App Engine
 *  
 * Create by Fred 2011-1-27
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.database;

import java.sql.SQLException;
import java.util.Properties;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.feinno.database.DataColumn;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.database.DatabasePerfmon;
import com.feinno.database.MonDBHelper;
import com.feinno.database.pool.DBConnectionPoolType;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther Fred
 */
public class DataTableTest extends TestCase {
	
	private Properties p = new Properties();
	private Database database = null;
	
	private String[] params = { "@UserId" };
	private String tableName = "TestUser2";
	
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
		dbTableHelper.hashCode();

		// Step1. 创建一个表
		dbTableHelper.execute("drop table if exists `"+ tableName +"`");
		dbTableHelper.execute("create table `"+ tableName +"`(UserId INT NOT NULL,Name varchar(20) not null,Age BIGINT not null," +
				"Birthday datetime not null,Weight double not null,Height float not null,IsMan boolean not null,InputTime timestamp not null," +
				"Password varbinary(128)) engine=innodb default charset=utf8");

		
		//生成5条初始数据
		dbTableHelper.execute("insert into "+tableName +" values(1,'user1',10,'2002-1-1',66.6,177.7,true,'2012-3-5 17:32',0x0A2976C272F81049B)");
		dbTableHelper.execute("insert into "+tableName +" values(2,'user2',20,'1992-2-2',77.8,188.8,false,'2012-3-5 16:40',0x0A2976C27)");
		
		
		
		database = DatabaseManager.getDatabase("test2", p,DBConnectionPoolType.C3p0);
		
	
		DatabasePerfmon.initialize();
	}
	
	public void testSpExecuteTable() {


		try {
//			DataTable dataTable = database.executeTable("select * from "+tableName+" where UserId=1");
//			Assert.assertEquals(tableName, dataTable.getName());
//			Assert.assertEquals(1, dataTable.getRowCount());
//			Assert.assertEquals(9, dataTable.getColumnCount());
//			
//			for (int i = 0; i < dataTable.getRowCount(); i++) {
//
//				System.out.println(dataTable.getRow(i).getInt(1));
//				System.out.println(dataTable.getRow(i).getInt("UserId"));
//				
//				Assert.assertEquals(false, dataTable.getRow(i).isNull(2));
//				
//				System.out.println(dataTable.getRow(i).getString(2));
//				System.out.println(dataTable.getRow(i).getString("Name"));
//				System.out.println(dataTable.getRow(i).getObject(2));
//				System.out.println(dataTable.getRow(i).getObject("Name"));
//
//				
//				
//				
//				System.out.println(dataTable.getRow(i).getShort(3));
//				System.out.println(dataTable.getRow(i).getShort("Age"));
//				
//				System.out.println(dataTable.getRow(i).getLong(3));
//				System.out.println(dataTable.getRow(i).getLong("Age"));	
//				
//				System.out.println(dataTable.getRow(i).getBigInteger(3));
//				System.out.println(dataTable.getRow(i).getBigInteger("Age"));	
//				
//				
//				System.out.println(dataTable.getRow(i).getTimestamp(4));	
//				System.out.println(dataTable.getRow(i).getTimestamp("Birthday"));
//				
//				System.out.println(dataTable.getRow(i).getDouble(5));
//				System.out.println(dataTable.getRow(i).getDouble("Weight"));
//				System.out.println(dataTable.getRow(i).getFloat(6));
//				System.out.println(dataTable.getRow(i).getFloat("Height"));
//				
//				
//				System.out.println(dataTable.getRow(i).getByte(7));
//				System.out.println(dataTable.getRow(i).getByte("IsMan"));				
//				
//				System.out.println(dataTable.getRow(i).getBoolean(7));
//				System.out.println(dataTable.getRow(i).getBoolean("IsMan"));
//				
//				System.out.println(dataTable.getRow(i).getTimestamp(8));
//				System.out.println(dataTable.getRow(i).getTimestamp("InputTime"));
//				
//				System.out.println(dataTable.getRow(i).getDateTime(8));
//				System.out.println(dataTable.getRow(i).getDateTime("InputTime"));
//				
//				System.out.println(dataTable.getRow(i).getBytes(9));
//				System.out.println(dataTable.getRow(i).getBytes("Password"));
//				
//				//DataColumn Test
//				
//				DataColumn dc = dataTable.getColumn(1);
//				Assert.assertNotNull(dc.getDataTable());
//				Assert.assertNotNull(dc.getSqlType());
//				Assert.assertEquals("UserId", dc.getColumnName());
//				Assert.assertEquals(true, dataTable.isContainColumn(dc.getColumnName()));
//				
//				Assert.assertEquals(9, dataTable.getColumns().length);
//				Assert.assertNotNull(dataTable.getRows());
//				
//				
//			}
			DataTable dataTable = database.spExecuteTable("USP_GetUserInfo", new String[]{});
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}

	}
	
	public void testCheckColumnIndexArg()
	{
		try {
			DataTable dataTable = database.executeTable("select * from "+tableName+" where UserId=1");
			dataTable.getRow(0).getObject(88);
		}catch (SQLException e) {
			Assert.assertTrue("越界测试，正常",true);
		}
	}
}
