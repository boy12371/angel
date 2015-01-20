/*
 * FAE, Feinno App Engine
 *  
 * Create by wanglihui 2011-2-15
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import com.feinno.database.DatabaseManager;
import com.feinno.database.MonDBHelper;
import com.feinno.database.Table;
import com.feinno.database.TableExistException;
import com.feinno.database.TableNotExistException;
import com.feinno.diagnostic.observation.ObserverReport;
import com.feinno.diagnostic.observation.ObserverReportColumn;
import com.feinno.diagnostic.observation.ObserverReportColumnType;
import com.feinno.diagnostic.observation.ObserverReportHelper;
import com.feinno.diagnostic.observation.ObserverReportRow;
import com.feinno.util.DateTime;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther wanglihui
 */
public class DBTableHelperTest extends TestCase {
	
	public void testSay() {
		System.out.println("注释掉测试方法，因为这些方法连接了数据库，而这个数据库并不存在!");
	}
//	private Properties properties = new Properties();
//	@SuppressWarnings("unused")
//	private MonDBHelper monDBHelper = null;
//
//	@Override
//	public void setUp() throws Exception {
//		super.setUp();
//		// p.setProperty("DriverClass", "net.sourceforge.jtds.jdbc.Driver");
//		// p.setProperty("JdbcUrl",
//		// "jdbc:jtds:sqlserver://192.168.110.210:2387/TEST");
//		properties.setProperty("DriverClass", "com.mysql.jdbc.Driver");
//		properties.setProperty("JdbcUrl", "jdbc:mysql://127.0.0.1:3306/test");
//		properties.setProperty("User", "root");
//		properties.setProperty("Password", "123456");
//		properties.setProperty("Database", "test");
//		monDBHelper = (MonDBHelper) DatabaseManager.getDatabase("FAEDB_MON", properties, MonDBHelper.class);
//	}
//
//	public void testCreateTable() throws Exception {
//
//		// Table table = new Table(" if not exists " + "work");
//		// table.addColumns(Table.Column.createAutoIncrementIntColumn("ID"));
//		// table.addColumns(Table.Column.createVarcharColumn("NAME", 256, false,
//		// null));
//		// table.addColumns(Table.Column.createIntColumn("LEVEL", false, null));
//		// table.addColumns(Table.Column.createVarcharColumn("tag", 256, true,
//		// null));
//		// table.addColumns(Table.Column.createTextColumn("ERROR", true));
//		// table.addColumns(Table.Column.createDateTimeColumn("OCCUR_TIME",
//		// false, null));
//		// table.addColumns(Table.Column.createVarcharColumn("COMPUTER", 64,
//		// true, null));
//		// table.addColumns(Table.Column.createVarcharColumn("PROCESS", 64,
//		// true, null));
//		// table.addColumns(Table.Column.createDateTimeColumn("ENROLL_TIME",
//		// false, "current_timestamp"));
//		// System.out.println(table);
//		// System.out.println(monDBHelper.createTable(table));
//
//		List<ObserverReportColumn> columns = new ArrayList<ObserverReportColumn>();
//		ObserverReportColumn column1 = new ObserverReportColumn("name", ObserverReportColumnType.TEXT);
//		ObserverReportColumn column2 = new ObserverReportColumn("age", ObserverReportColumnType.DOUBLE);
//		ObserverReportColumn column3 = new ObserverReportColumn("testRatio", ObserverReportColumnType.RATIO);
//		ObserverReportColumn column4 = new ObserverReportColumn("address", ObserverReportColumnType.TEXT);
//		columns.add(column1);
//		columns.add(column2);
//		columns.add(column3);
//		columns.add(column4);
//		ObserverReport report = new ObserverReport("OO-test", columns, new DateTime(System.currentTimeMillis()));
//		ObserverReportRow row1 = report.newRow("Jack");
//		row1.output("Jack");
//		row1.output("18");
//		row1.output("89.53");
//		row1.output("北京市通州区");
//		ObserverReportRow row2 = report.newRow("Tom");
//		row2.output("Tom");
//		row2.output("23");
//		row2.output("95.8");
//		row2.output("北京市朝阳区");
//		ObserverReportRow row3 = report.newRow("Rebort");
//		row3.output("Rebort");
//		row3.output("35");
//		row3.output("88.53");
//		row3.output("北京市天安门");
//
//		Properties properties = new Properties();
//		properties.setProperty("DriverClass", "com.mysql.jdbc.Driver");
//		properties.setProperty("JdbcUrl", "jdbc:mysql://127.0.0.1:3306/test");
//		properties.setProperty("User", "root");
//		properties.setProperty("Password", "123456");
//		properties.setProperty("Database", "test");
//		String serverName = "test1112";
//		String workerId = "workerId";
////		ObserverReportHelper observerReportHelper = ObserverReportHelper.createClientHelper(properties);
//		ObserverReportHelper observerReportHelper = ObserverReportHelper.createServerHelper(properties);
//		System.out.println(observerReportHelper.writeReport(report,serverName,workerId));
//
//		// // Step1. 创建一个表
//		// Table table = new Table("MyTest");
//		// table.addColumns(Table.Column.createAutoIncrementIntColumn("ID"));
//		// table.addColumns(Table.Column.createVarcharColumn("name", 64, true,
//		// "jack"));
//		// table.addColumns(Table.Column.createIntColumn("age", false, 18));
//		// table.addColumns(Table.Column.createDateTimeColumn("createDate",
//		// false, null));
//		// table.addColumns(Table.Column.createDateTimeColumn("operationDate",
//		// false, "current_timestamp"));
//		// table.addColumns(Table.Column.createLongColumn("longTest", false,
//		// 198710281111L));
//		// table.addColumns(Table.Column.createDoubleColumn("doubleTest", false,
//		// 19.896));
//		// table.addColumns(Table.Column.createTextColumn("textTest", false));
//		// table.addPrimaryKey("ID");
//		// table.addPrimaryKey("name");
//		// table.addPrimaryKey("age");
//		// System.out.println(table);
//		// try {
//		// // Step2. 判断是否有相同的表名
//		// if (monDBHelper.isExistTable(table.getTableName())) {
//		// // Step3. 有相同的表名，判断表结构是否相同
//		// if (!table.equals(monDBHelper.getTable(table.getTableName()))) {
//		// // Step4. 表结构不相同，则修改数据库中的表名，修改后再创建新表
//		// monDBHelper.renameTable(table.getTableName(), table.getTableName() +
//		// System.currentTimeMillis());
//		// monDBHelper.createTable(table);
//		// System.out.println("已有相同的表名,但结构不同,因此将原始表更名后重新创建");
//		// } else {
//		// System.out.println("已有相同的表,不需要创建");
//		// }
//		// } else {
//		// // Step5. 没有相同的表名,直接创建既可
//		// monDBHelper.createTable(table);
//		// System.out.println("没有相同的表名,直接创建该表:");
//		// System.out.println(table);
//		// }
//		// // Step6 最后打印已经创建好的，保存于数据库中的建表脚本
//		// System.out.print("数据库中表结构如下：");
//		// System.out.println(monDBHelper.getTable(table.getTableName()));
//		// } catch (SQLException e1) {
//		// // 数据库执行失败
//		// e1.printStackTrace();
//		// } catch (TableNotExistException e2) {
//		// // 更名时出现了源表名不存在的情况
//		// e2.printStackTrace();
//		// } catch (TableExistException e3) {
//		// // 建表时出现了表名冲突的情况
//		// e3.printStackTrace();
//		// }
//	}
}
