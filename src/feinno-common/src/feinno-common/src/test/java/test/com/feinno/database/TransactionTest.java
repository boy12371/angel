/*
 * FAE, Feinno App Engine
 *  
 * Create by wanglihui 2011-2-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.feinno.database.DataReader;
import com.feinno.database.DataTable;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.database.Transaction;
import com.feinno.database.pool.DBConnectionPoolType;
/**
 * {在这里补充类的功能说明}
 * 
 * @auther wanglihui
 */
public class TransactionTest extends TestCase{
	
	
	private Properties p = new Properties();
	private String[] params = { "@UserId" };
	Database database;
	@Override
	public void setUp() throws Exception{
		super.setUp();		
		p.setProperty("DriverClass", "com.mysql.jdbc.Driver");
		p.setProperty(
				"JdbcUrl",
				"jdbc:mysql://192.168.110.234/test?autoReconnect=true&cacheCallableStmts=true&callableStmtCacheSize=512&tinyInt1isBit=false&useDynamicCharsetInfo=false&zeroDateTimeBehavior=round");
		p.setProperty("user", "admin");
		p.setProperty("password", "admin");		
		database = DatabaseManager.getDatabase("test", p,DBConnectionPoolType.C3p0);			
	
	}
	
	public void testspExecuteNonQuery(){
		int i;
		Transaction tx = null;
		try {
			tx = database.beginTransaction();
			i = tx.spExecuteNonQuery("SP_UpdateUserInfo", new String[] {
					"@UserId", "@Name" }, 1, "NameByTx");
			tx.commit();
			Assert.assertEquals(i, 1);
			
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
			tx.rollback();
		} finally {
			if(tx!=null)
				tx.close();
		}
	}
	
	public void testspExecuteNonQueryRollback(){
		int i;
		Transaction tx = null;
		try {
			tx = database.beginTransaction();
			i = tx.spExecuteNonQuery("SP_UpdateUserInfo", new String[] {
					"@UserId", "@Name" }, 1, "NameRollBack");
			tx.rollback();
			Assert.assertEquals(1, 1);
			
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
			tx.rollback();
		} finally {
			if(tx!=null)
				tx.close();
		}
	}
	
//	public void testspExecuteReader(){
//		DataReader reader;
//		Transaction tx = null;
//		try {
//			tx = database.beginTransaction();
//			reader = tx.spExecuteReader("SP_GetUserInfo",params, 1);
//			tx.commit();
//			while (reader.read()) {
////				System.out.println(reader.getString(1));
//				Assert.assertEquals(reader.getString(1), "1");
//			}
//			reader.close();
//		} catch (SQLException e) {
//			Assert.fail(e.getMessage());
//			tx.rollback();
//		} finally {
//			if(tx!=null)
//				tx.close();
//		}		
//	}
	
	public void testspExecuteTable(){
		DataTable dataTable;
		Transaction tx = null;
		try {
			tx = database.beginTransaction();
			dataTable = tx.spExecuteTable("SP_GetUserInfo",params, "1");
			tx.commit();
			if(dataTable.getRowCount()>0)
				Assert.assertEquals(dataTable.getRow(0).getObject(1).toString(), "1");
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
			tx.rollback();
			
		} finally {
			if(tx!=null)
				tx.close();
		}	
	}
	
	public void testexecuteNonQuery(){
		Transaction tx = null;
		try {
			tx = database.beginTransaction();
			tx.executeNonQuery("update TestUser set Age=? where UserId=?", new Integer(199), new Integer(1));
			tx.executeNonQuery("update TestUser set Age=? where UserId=?", new Integer(199), new Integer(2));
			tx.commit();
			Assert.assertEquals(1, 1);
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
			tx.rollback();
		} finally {			
			if(tx!=null)
				tx.close();
		}
	}
	
//	public void testexecuteReader(){
//		Transaction tx = null;
//		try{
//			tx = database.beginTransaction();
//			DataReader reader = tx.executeReader("select * from TestUser where UserId =?", 1);
//			tx.commit();
//			if (reader.read()) {
////				System.out.println(reader.getString(1) + "---" + reader.getString(2));
//				Assert.assertEquals(reader.getString(1), "1");
//			}
//			reader.close();
//		} catch (SQLException e) {
//			Assert.fail(e.getMessage());
//			tx.rollback();
//		} finally {
//			if(tx!=null)
//				tx.close();
//		}	
//	}
	
	public void testexecuteTable(){
		Transaction tx = null;
		try{
			tx = database.beginTransaction();
			DataTable dataTable = tx.executeTable("select * from TestUser where UserId =?", 1);
			tx.commit();
			if(dataTable.getRowCount()>0)
//				System.out.println(dataTable.getRow(0).getObject(1) + "---" + dataTable.getRow(0).getObject(2));
				Assert.assertEquals(dataTable.getRow(0).getObject(1).toString(), "1");
		} catch (SQLException e) {
			Assert.fail(e.getMessage());
			tx.rollback();
		} finally {
			if(tx!=null)
			tx.close();
		}	
	}
	/**
	 * Trans 对SqlServer好使
	 * 对Mysql不好使 需要设置engine=InnoDB才好用，默认engine=MyISAM
	 */
//	public void testTrans()
//	{
//		try {
//			Properties p2 = new Properties();
//			p2.setProperty("DriverClass", "net.sourceforge.jtds.jdbc.Driver"); 
//			p2.setProperty("JdbcUrl", "jdbc:jtds:sqlserver://192.168.110.210:2387/FAEDB");
//			p2.setProperty("User", "dbwriter");
//			p2.setProperty("Password", "Password2012");		
//			Database db = DatabaseManager.getDatabase("FAEDB", p2);	
//			
//			Connection conn = db.getConnection();
//			conn.setAutoCommit(false);
//			System.out.println(conn.getTransactionIsolation());
//						
////			PreparedStatement stmt = conn.prepareStatement("insert into FAE_ConfigText values('test3.properties','*','test=test',getdate())");
////			stmt.executeUpdate();
////			PreparedStatement stmt2 = conn.prepareStatement("insert into FAE_ConfigText values('test4.properties','*','test=test2',getdate())");
////			stmt2.executeUpdate();
//			PreparedStatement stmt = conn.prepareStatement("delete from  FAE_ConfigText where  ConfigKey='test.properties'");
//			stmt.executeUpdate();
//			PreparedStatement stmt2 = conn.prepareStatement("delete from  FAE_ConfigText where  ConfigKey='test2.properties'");
//			stmt2.executeUpdate();
//			conn.rollback();
//			conn.close();
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
