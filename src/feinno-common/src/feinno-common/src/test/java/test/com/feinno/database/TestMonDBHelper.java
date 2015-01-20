package test.com.feinno.database;

import java.sql.SQLException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import test.com.feinno.diagnostic.CounterTest;

import com.feinno.database.DatabaseManager;
import com.feinno.database.MonDBHelper;
import com.feinno.database.Table;
import com.feinno.database.TableExistException;
import com.feinno.database.TableNotExistException;
import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.diagnostic.observation.ObserverReport;
import com.feinno.diagnostic.observation.ObserverReportHelper;
import com.feinno.diagnostic.observation.ObserverReportMode;
import com.feinno.diagnostic.observation.ObserverInspector.ReportCallback;
import com.feinno.util.TimeSpan;

public class TestMonDBHelper {

	private Properties p = new Properties();

	@Before
	public void before() {
		p.setProperty("DriverClass", "com.mysql.jdbc.Driver");
		p.setProperty(
				"JdbcUrl",
				"jdbc:mysql://192.168.110.234/test?autoReconnect=true&cacheCallableStmts=true&callableStmtCacheSize=512&tinyInt1isBit=false&useDynamicCharsetInfo=false&zeroDateTimeBehavior=round");
		p.setProperty("user", "admin");
		p.setProperty("password", "admin");
	}

	@Test
	public void test() throws SQLException {
		// 生成表
		MonDBHelper dbTableHelper = DatabaseManager.getMonDBHelper("test", p);
		// Step1. 创建一个表
		Table table = new Table("MyTest1");
		table.addColumns(Table.Column.createVarcharColumn("name", 100, true, null));
		table.addColumns(Table.Column.createLongColumn("age", false, null));
		table.addColumns(Table.Column.createDateTimeColumn("createDate", false, null));
		table.addPrimaryKey("name");
		table.addPrimaryKey("age");
		try {
			dbTableHelper.createTable(table);
			dbTableHelper.createTable(table);
		} catch (TableExistException e) {
			// 表已经存在是正常现象
		}

		// 测试使用
		// Table table1 = new Table("MyTest");
		// table1.addColumns(Column.createVarcharColumn("name", 100, true));
		// table1.addColumns(Column.createIntColumn("age", 11, false));
		// table1.addColumns(Column.createDateColumn("createDate", false));
		// table1.addPrimaryKey("name");
		// table1.addPrimaryKey("age");
		// System.out.println(table.hashCode());
		// System.out.println(table1.hashCode());
		// System.out.println(table.equals(table1));
		table.addColumns(Table.Column.createVarcharColumn("name123", 100, true, null));
		try {
			// Step2. 判断是否有相同的表名
			if (dbTableHelper.isExistTable(table.getTableName())) {
				// Step3. 有相同的表名，判断表结构是否相同
				if (!table.equals(dbTableHelper.getTable(table.getTableName()))) {
					// Step4. 表结构不相同，则修改数据库中的表名，修改后再创建新表
					dbTableHelper.renameTable(table.getTableName(), table.getTableName() + System.currentTimeMillis());
					dbTableHelper.createTable(table);
					System.out.println("已有相同的表名,但结构不同,因此将原始表更名后重新创建");
				} else {
					System.out.println("已有相同的表,不需要创建");
				}
			} else {
				// Step5. 没有相同的表名,直接创建既可
				dbTableHelper.createTable(table);
				System.out.println("没有相同的表名,直接创建该表");
			}
			// Step6 最后打印已经创建好的，保存于数据库中的建表脚本
			System.out.print("数据库中表结构如下：");
			System.out.println(dbTableHelper.getTable(table.getTableName()));
		} catch (SQLException e1) {
			// 数据库执行失败
			e1.printStackTrace();
		} catch (TableNotExistException e2) {
			// 更名时出现了源表名不存在的情况
			e2.printStackTrace();
		} catch (TableExistException e3) {
			// 建表时出现了表名冲突的情况
			e3.printStackTrace();
		}
	}

	@Test
	public void testObserverReportHelper() throws Exception {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				CounterTest counter = new CounterTest();
				counter.test();
			}
		});
		thread.start();
		final ObserverReportHelper observerReportHelperClient = ObserverReportHelper.createClientHelper(p);
		final ObserverReportHelper observerReportHelperServer = ObserverReportHelper.createServerHelper(p);
		Thread.sleep(1000);
		Observable ob = ObserverManager.getObserverItem("sample");
		ObserverManager.addInspector(ob, ObserverReportMode.ALL, new TimeSpan(5000), new ReportCallback() {
			@Override
			public boolean handle(ObserverReport report) {
				observerReportHelperClient.writeReport(report);
				observerReportHelperServer.writeReport(report,"workName","workId");
				return true;
			}
		});
		Thread.sleep(3 * 1000);
	}

	@Test
	public void testOther() {
		Table table = new Table("MyTest1");
		table.addColumns(Table.Column.createVarcharColumn("name", 100, true, null));
		table.addColumns(Table.Column.createLongColumn("age", false, null));
		table.addColumns(Table.Column.createDateTimeColumn("createDate", false, null));
		table.addColumns(Table.Column.createDoubleColumn("double", false, null));
		table.addColumns(Table.Column.createTextColumn("text", true));
		table.addPrimaryKey("name");
		table.addPrimaryKey("age");
		table.hashCode();
		Table.Column.createVarcharColumn("name", 100, true, null).hashCode();
		Table.Column.createVarcharColumn("name", 100, true, null).equals(
				Table.Column.createVarcharColumn("name", 100, true, null));
		Table table2 = new Table("MyTest1");
		table2.addColumns(Table.Column.createVarcharColumn("name", 100, true, null));
		table2.addColumns(Table.Column.createLongColumn("age", false, null));
		table2.addColumns(Table.Column.createDateTimeColumn("createDate", false, null));
		table2.addColumns(Table.Column.createDoubleColumn("double", false, null));
		table2.addColumns(Table.Column.createTextColumn("text", true));
		table.equals(table2);
	}

}
