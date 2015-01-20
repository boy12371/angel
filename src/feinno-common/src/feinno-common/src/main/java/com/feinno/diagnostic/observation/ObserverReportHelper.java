package com.feinno.diagnostic.observation;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.database.DatabaseManager;
import com.feinno.database.MonDBHelper;
import com.feinno.database.Table;
import com.feinno.database.TableExistException;
import com.feinno.database.TableNotExistException;
import com.feinno.database.Transaction;
import com.feinno.util.DateUtil;

/**
 * 
 * @author Lv.Mingwei
 * 
 */
public abstract class ObserverReportHelper {

	protected MonDBHelper monDBHelper = null;

	private static final String DATE_FORMAT_PATTERN = "yyyyMMdd";

	private static final String DATE_FORMAT_FAILED_PATTERN = "HHmmss";

	private static final int DATABASE_COLUMN_VARCHAR_LENGTH = 2000;

	private static final boolean DATABASE_COLUMN_ISNULL = false;

	private static final String SQL_INSERT_TEMPLATE = "REPLACE INTO `%s` (%s) VALUES(%s);";

	private static final Logger LOGGER = LoggerFactory.getLogger(ObserverReportHelper.class);

	private static final Object SYNC_OBJECT = new Object();

	/**
	 * 客户端默认的监控表名,格式为OBR_{Category}_{YYYYMMDD}
	 */
	private static final String CLIENT_TABLE_NAME_DEFAULT = "OBR_CLIENT_%s_%s";

	/** 服务端默认的监控表名,格式为OBR_{category} */
	private static final String SERVER_TABLE_NAME_DEFAULT = "OBR_SERVER_%s";

	public ObserverReportHelper(Properties properties) {
		monDBHelper = DatabaseManager.getMonDBHelper(properties.getProperty("Database"), properties);
	}

	/**
	 * 返回一个客户端需要用到的监控数据存储Helper
	 * 
	 * @return
	 */
	public static ObserverReportHelper createClientHelper(Properties properties) {

		return new ObserverReportHelper(properties) {

			private String serviceName = "";
			private String workerPid = "";

			/**
			 * 通过{@link ObserverReport}对象提取其中的表格信息，创建一个{@link table}对象
			 * 
			 * @param report
			 * @param tableName
			 * @return
			 */
			Table createTableObjec(ObserverReport report, String... args) {

				serviceName = args[0];
				workerPid = args[1];
				String category = report.getCategory();
				String date = DateUtil.formatDate(new Date(report.getTime().getTime()), DATE_FORMAT_PATTERN);

				// OBR_{Category}_{YYYYMMDD}
				String tableName = String.format(ObserverReportHelper.CLIENT_TABLE_NAME_DEFAULT, category, date);

				Table table = new Table(tableName);
				table.addColumns(Table.Column.createDateTimeColumn("time", false, null));
				table.addColumns(Table.Column.createVarcharColumn("serviceName", 128, false, null));
				table.addColumns(Table.Column.createVarcharColumn("workerPid", 128, false, null));
				table.addColumns(Table.Column.createVarcharColumn("instance", 128, false, null));

				// 因为在数据库监控时instance作为sql语句会有重复的情况发生,所以去掉主键
				table.addPrimaryKey("time");
				table.addPrimaryKey("instance");

				for (ObserverReportColumn column : report.getColumns()) {
					switch (column.getType()) {
					case LONG:
						table.addColumns(Table.Column.createLongColumn(column.getName(),
								ObserverReportHelper.DATABASE_COLUMN_ISNULL, null));
						break;
					case DOUBLE:
						table.addColumns(Table.Column.createDoubleColumn(column.getName(),
								ObserverReportHelper.DATABASE_COLUMN_ISNULL, null));
						break;
					case RATIO:
						table.addColumns(Table.Column.createDoubleColumn(column.getName(),
								ObserverReportHelper.DATABASE_COLUMN_ISNULL, null));
						break;
					case TEXT:
						table.addColumns(Table.Column.createVarcharColumn(column.getName(),
								ObserverReportHelper.DATABASE_COLUMN_VARCHAR_LENGTH,
								ObserverReportHelper.DATABASE_COLUMN_ISNULL, null));
						break;
					default:
						break;
					}
				}

				return table;
			}

			/**
			 * 此处为向指定表中写入数据
			 * 
			 * @param report
			 * @return
			 */
			boolean writeData(final ObserverReport report, final String tableName, final String... args)
					throws SQLException {
				Transaction tx = monDBHelper.beginTransaction();
				try {
					StringBuilder columnsString = new StringBuilder();// 存储列名,用于SQL拼接，例如
																		// name,age,address
					StringBuilder rowsMarkString = new StringBuilder(); // 存储某一行的占位符，用于SQL拼接，例如
																		// ?,?,?
					int columnsSize = report.getColumns().size();
					for (ObserverReportColumn column : report.getColumns()) {
						columnsString.append("`").append(column.getName()).append("`,");
						rowsMarkString.append("?").append(",");
					}
					columnsString.append("`time`,`serviceName`,`workerPid`,`instance`,");
					rowsMarkString.append("?,?,?,?,");
					columnsString.delete(columnsString.length() - 1, columnsString.length());
					rowsMarkString.delete(rowsMarkString.length() - 1, rowsMarkString.length());
					String insertSQL = String.format(ObserverReportHelper.SQL_INSERT_TEMPLATE, tableName,
							columnsString, rowsMarkString);

					// SQL拼接完成后,下面开始动态组装参数,参数因为根据类型不同,需要转换不同的类型,例如Integer、String、DATE等
					for (final ObserverReportRow row : report.getRows()) {
						if (row != null && row.getData().length == columnsSize) {
							Object[] objArrays = this.analyzeArray(row.getData(), report.getColumns(),
									new Action<Object[]>() {
										@Override
										public Object[] run(Object[] objArray) {
											objArray = new Object[objArray.length + 4];
											objArray[objArray.length - 4] = new java.sql.Timestamp(report.getTime()
													.getTime());
											objArray[objArray.length - 3] = serviceName;
											objArray[objArray.length - 2] = workerPid;
											objArray[objArray.length - 1] = row.getInstanceName().length() > 127 ? row
													.getInstanceName().substring(0, 128) : row.getInstanceName();
											return objArray;
										}

									});
							tx.executeInsertWithAutoColumn(insertSQL, objArrays);
						}
					}

					tx.commit();
				} catch (Exception e) {
					tx.rollback();
					LOGGER.error("{}", e);
					return false;
				} finally {
					tx.close();
				}
				return true;

			}
		};
	}

	/**
	 * 返回一个服务端需要用到的监控数据存储Helper
	 * 
	 * @return
	 */
	public static ObserverReportHelper createServerHelper(Properties properties) {

		return new ObserverReportHelper(properties) {
			/**
			 * 通过{@link ObserverReport}对象提取其中的表格信息，创建一个{@link table}对象
			 * 
			 * @param report
			 * @param tableName
			 * @return
			 */
			Table createTableObjec(ObserverReport report, String... args) {
				String tableName = String.format(ObserverReportHelper.SERVER_TABLE_NAME_DEFAULT, report.getCategory());
				Table table = new Table(tableName);
				table.addColumns(Table.Column.createDateTimeColumn("Updatetime", false, null));
				table.addColumns(Table.Column.createVarcharColumn("Instance", 128, false, null));
				table.addColumns(Table.Column.createVarcharColumn("ServerName", 64, false, null));
				table.addColumns(Table.Column.createVarcharColumn("ServiceName", 128, false, null));
				// 小心在数据库监控时instance作为sql语句会有重复的情况发生
				table.addPrimaryKey("Instance");
				table.addPrimaryKey("ServerName");
				table.addPrimaryKey("ServiceName");

				for (ObserverReportColumn column : report.getColumns()) {
					switch (column.getType()) {
					case LONG:
						table.addColumns(Table.Column.createLongColumn(column.getName(),
								ObserverReportHelper.DATABASE_COLUMN_ISNULL, null));
						break;
					case DOUBLE:
						table.addColumns(Table.Column.createDoubleColumn(column.getName(),
								ObserverReportHelper.DATABASE_COLUMN_ISNULL, null));
						break;
					case RATIO:
						table.addColumns(Table.Column.createDoubleColumn(column.getName(),
								ObserverReportHelper.DATABASE_COLUMN_ISNULL, null));
						break;
					case TEXT:
						table.addColumns(Table.Column.createVarcharColumn(column.getName(),
								ObserverReportHelper.DATABASE_COLUMN_VARCHAR_LENGTH,
								ObserverReportHelper.DATABASE_COLUMN_ISNULL, null));
						break;
					default:
						break;
					}
				}

				return table;
			}

			/**
			 * 此处为向指定表中写入数据
			 * 
			 * @param report
			 * @return
			 */
			boolean writeData(final ObserverReport report, final String tableName, final String... args)
					throws SQLException {
				Transaction tx = monDBHelper.beginTransaction();
				try {
					StringBuilder columnsString = new StringBuilder();// 存储列名,用于SQL拼接，例如
																		// name,age,address
					StringBuilder rowsMarkString = new StringBuilder(); // 存储某一行的占位符，用于SQL拼接，例如
																		// ?,?,?
					int columnsSize = report.getColumns().size();
					for (ObserverReportColumn column : report.getColumns()) {
						columnsString.append("`").append(column.getName()).append("`,");
						rowsMarkString.append("?").append(",");
					}
					columnsString.append("`UpdateTime`,`Instance`,`ServerName`,`ServiceName`,");
					rowsMarkString.append("?,?,?,?,");
					columnsString.delete(columnsString.length() - 1, columnsString.length());
					rowsMarkString.delete(rowsMarkString.length() - 1, rowsMarkString.length());
					String insertSQL = String.format(ObserverReportHelper.SQL_INSERT_TEMPLATE, tableName,
							columnsString, rowsMarkString);

					// SQL拼接完成后,下面开始动态组装参数,参数因为根据类型不同,需要转换不同的类型,例如Integer、String、DATE等
					for (final ObserverReportRow row : report.getRows()) {
						if (row != null && row.getData().length == columnsSize) {
							Object[] objArrays = this.analyzeArray(row.getData(), report.getColumns(),
									new Action<Object[]>() {
										@Override
										public Object[] run(Object[] objArray) {
											objArray = new Object[objArray.length + 4];
											objArray[objArray.length - 4] = new java.sql.Timestamp(report.getTime()
													.getTime());
											objArray[objArray.length - 3] = row.getInstanceName().length() > 127 ? row
													.getInstanceName().substring(0, 128) : row.getInstanceName();
											objArray[objArray.length - 2] = args[0];
											objArray[objArray.length - 1] = args[1];
											return objArray;
										}

									});
							tx.executeInsertWithAutoColumn(insertSQL, objArrays);
						}
					}

					tx.commit();
				} catch (Exception e) {
					tx.rollback();
					LOGGER.error("{}", e);
					return false;
				} finally {
					tx.close();
				}
				return true;

			}
		};
	}

	/**
	 * 写入监控数据<br>
	 * 
	 * @param report
	 */
	public boolean writeReport(ObserverReport report, String... args) {

		/** Step1. 入参完整性验证 */
		if (report == null || report.getCategory() == null || report.getTime() == null || report.getColumns() == null
				|| report.getColumns().size() == 0) {
			return false;
		}

		if (report.getRows() == null || report.getRows().size() == 0) {
			// 此处默认为成功，代表本次没有采集到数据，也就不需要入库信息
			return true;
		}

		/** Step2. 表创建 */
		// 根据已知的ObserverReport对象，创建表格
		Table table = createTableObjec(report, args);

		/** Step3. 与数据库进行操作 */
		try {
			if (createDataBaseTable(table)) {
				String tableName = table.getTableName();
				return writeData(report, tableName, args);
			} else {
				// 如果表格创建失败了...
				return false;
			}
		} catch (SQLException e) {
			LOGGER.error("{}", e);
			return false;
		}
	}

	/**
	 * 报表转表格的具体业务逻辑(例如客户端需要额外在表格中增加time和instance,而服务端需要增加servername)
	 * 
	 * @param report
	 * @param args
	 * @return
	 */
	abstract Table createTableObjec(ObserverReport report, String... args);

	/**
	 * 写数据的具体业务逻辑(例如客户端需要额外在多写time和instance,而增加servername)
	 * 
	 * @param report
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	abstract boolean writeData(final ObserverReport report, final String tableName, final String... args)
			throws SQLException;

	Object[] analyzeArray(String[] data, List<ObserverReportColumn> columns, Action<Object[]> action) {
		if (data == null || data.length == 0 || columns == null || columns.size() == 0) {
			return new Object[0];
		}
		Object[] objArray = new Object[data.length];

		if (action != null) {
			objArray = action.run(objArray);
		}

		for (int i = 0; i < columns.size(); i++) {
			try {
				switch (columns.get(i).getType()) {
				case LONG:
					objArray[i] = Long.valueOf(data[i]);
					break;
				case DOUBLE:
					objArray[i] = Double.valueOf(data[i]);
					break;
				case RATIO:
					objArray[i] = Double.valueOf(data[i]);
					break;
				case TEXT:
					objArray[i] = data[i];
					break;
				default:
					break;
				}
			} catch (Exception e) {
				LOGGER.error("object type covert failed,use default value 0{}", e);
				switch (columns.get(i).getType()) {
				case LONG:
					objArray[i] = Long.valueOf(0);
					break;
				case DOUBLE:
					objArray[i] = Double.valueOf(0);
					break;
				case RATIO:
					objArray[i] = Double.valueOf(0);
					break;
				case TEXT:
					objArray[i] = "0";
					break;
				default:
					break;
				}
			}
		}
		return objArray;
	}

	/**
	 * 根据处理好的{@link Table}
	 * 对象，来创建词表格，如果表格存在，那么判断表格的格式与将要创建的表格是否相同，相同则不创建，不相同则修改原始表的表名后重新创建
	 * 
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	boolean createDataBaseTable(Table table) throws SQLException {
		synchronized (SYNC_OBJECT) {
			// 如果表格不符合规格，则返回创建表格失败,个人感觉其实应该抛出一个异常...
			if (table == null || !table.check()) {
				return false;
			}

			try {
				// 首先判断以此命名的表是否存在
				if (monDBHelper.isExistTable(table.getTableName())) {
					// 如果存在此表,那么判断表结构是否相同，如果表结构不同，则修改原始表名，再创建新表
					if (!table.equals(monDBHelper.getTable(table.getTableName()))) {
						// 如果表存在了，并且表结构也不同，那么修改表名，在原始表名后面追加精确时间，变成完整的(yyyyMMddHHmmss)
						monDBHelper.renameTable(table.getTableName(),
								table.getTableName() + DateUtil.formatDate(new Date(), DATE_FORMAT_FAILED_PATTERN));
						return monDBHelper.createTable(table);
					}
				} else {
					// 如果不存在此表，那么创建此表
					return monDBHelper.createTable(table);
				}

			} catch (TableExistException e) {
				// 如果在创建表时，表已存在
				throw new SQLException(e);
			} catch (TableNotExistException e) {
				// 如果在修改源表名时，表不存在
				throw new SQLException(e);
			}

		}
		return true;
	}

	interface Action<T> {
		T run(T a);
	}
}
