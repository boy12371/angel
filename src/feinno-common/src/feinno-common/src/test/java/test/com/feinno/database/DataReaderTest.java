package test.com.feinno.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import com.feinno.database.DataReader;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.database.DatabasePerfmon;
import com.feinno.database.MonDBHelper;
import com.feinno.database.pool.DBConnectionPoolType;

import junit.framework.Assert;
import junit.framework.TestCase;

public class DataReaderTest extends TestCase {
	
	private Properties p = new Properties();
	private Database database = null;
	
	private String[] params = { "@UserId" };
	private String tableName = "TestUser3";
	
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
		dbTableHelper.execute("create table `"+ tableName +"`(UserId INT NOT NULL,Name varchar(20) not null,Age BIGINT not null," +
				"Birthday datetime not null,Weight double not null,Height float not null,IsMan boolean not null,InputTime timestamp not null," +
				"Password varbinary(128), Content blob ,  Image blob ) engine=innodb default charset=utf8");

		
		//生成5条初始数据
		dbTableHelper.execute("insert into "+tableName +" values(1,'user1',10,'2002-1-1',66.6,177.7,true,'2012-3-5 17:32',0x0A2976C272F81049B,'abcdefg',0x000000000100011111)");
		dbTableHelper.execute("insert into "+tableName +" values(2,'user2',20,'1992-2-2',77.8,188.8,false,'2012-3-5 16:40',0x0A2976C27,'aaaaaaaa',0x111111111111111111111100000000001111111111111111)");
		
		
		database = DatabaseManager.getDatabase("test2", p,DBConnectionPoolType.C3p0);
		
	
		DatabasePerfmon.initialize();

		
		
	}

	public void testExecuteReader() {


		try {
			database = DatabaseManager.getDatabase("test2");
			DataReader reader = database.executeReader("select * from "+tableName+" where UserId=1");
			
			Assert.assertNotNull(reader.getAllResultSet());
			if (reader.read()) {
				System.out.println(reader.getOpenedTime());
				Assert.assertEquals(false, reader.wasNull());
				
				ResultSet rs = reader.getCurResultSet();
				Assert.assertNotNull(rs);
				
				System.out.println(reader.getInt(1));
				System.out.println(reader.getInt("UserId"));
				
				System.out.println(reader.getString(2));
				System.out.println(reader.getString("Name"));
				System.out.println(reader.getObject(2));
				System.out.println(reader.getObject("Name"));				
				Assert.assertNotNull(reader.getAsciiStream(2));
				Assert.assertNotNull(reader.getAsciiStream("Name"));
			
				
				System.out.println(reader.getShort(3));
				System.out.println(reader.getShort("Age"));				
				System.out.println(reader.getLong(3));
				System.out.println(reader.getLong("Age"));	
				
				
				System.out.println(reader.getTimestamp(4));	
				System.out.println(reader.getTimestamp("Birthday"));
				
				System.out.println(reader.getDouble(5));
				System.out.println(reader.getDouble("Weight"));
				System.out.println(reader.getFloat(6));
				System.out.println(reader.getFloat("Height"));
				
				
				System.out.println(reader.getByte(7));
				System.out.println(reader.getByte("IsMan"));		
				System.out.println(reader.getBoolean(7));
				System.out.println(reader.getBoolean("IsMan"));
				
				System.out.println(reader.getTimestamp(8));
				System.out.println(reader.getTimestamp("InputTime"));				
				System.out.println(reader.getDateTime(8));
				System.out.println(reader.getDateTime("InputTime"));
				
				System.out.println(reader.getBytes(9));
				System.out.println(reader.getBytes("Password"));
				Assert.assertNotNull(reader.getBinaryStream(9));
				Assert.assertNotNull(reader.getBinaryStream("Password"));
//				Assert.assertNotNull(reader.getClob(10));
//				Assert.assertNotNull(reader.getClob("Content"));
				//Assert.assertNotNull(reader.getBlob(11));
				//Assert.assertNotNull(reader.getBlob("Image"));
				
		
				reader.close();
				boolean isClose = reader.closed();
				Assert.assertEquals(true, isClose);
				
			}

		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}

	}
	
	public void testDataReader() {


		try {
			DataReader reader = database.executeReader("select * from "+tableName );
			
			Assert.assertNotNull(reader.getAllResultSet());
			if (reader.read()) {
				System.out.println(reader.getWarnings());
				reader.clearWarnings();
				//System.out.println(reader.getCursorName());
				Assert.assertNotNull(reader.getMetaData());
				Assert.assertEquals(1, reader.findColumn("UserId"));
				Assert.assertNotNull(reader.getCharacterStream(2));
				Assert.assertNotNull(reader.getCharacterStream("Name"));
				
				Assert.assertNotNull(reader.getBigDecimal(3));
				Assert.assertNotNull(reader.getBigDecimal("Age"));
				Assert.assertEquals(1, reader.getRow());
				Assert.assertEquals(true, reader.absolute(1));
				Assert.assertEquals(true, reader.relative(0));
				Assert.assertEquals(false, reader.previous());
					
				reader.setFetchDirection(ResultSet.FETCH_FORWARD);
				Assert.assertEquals(ResultSet.FETCH_FORWARD,reader.getFetchDirection());
				reader.setFetchSize(0);
				Assert.assertEquals(0,reader.getFetchSize());
				
				System.out.println("Type:"+reader.getType());
				System.out.println("Concurrency:"+reader.getConcurrency());
			
				
				reader.moveToCurrentRow();
				//Assert.assertEquals(false,reader.rowDeleted());
//				Assert.assertEquals(false,reader.rowInserted());
//				Assert.assertEquals(false,reader.rowUpdated());
				
				reader.getStatement();
				//reader.refreshRow();
				
				reader.moveToInsertRow();			
				reader.nextResult();
				
			}

		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}

	}

}
