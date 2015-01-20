package com.feinno.appengine.database.accesslayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.sql.rowset.CachedRowSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.resource.database.SqlArgs;
import com.feinno.database.DataReader;
import com.feinno.database.Database;
import com.feinno.database.DatabaseManager;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcServiceBase;
import com.feinno.serialization.protobuf.types.ProtoByteArray;
import com.feinno.serialization.protobuf.types.ProtoInteger;
import com.feinno.serialization.protobuf.types.ProtoLong;
import com.feinno.threading.ExecutorFactory;

/**
 * DatabaseRpcServer
 * @author lichunlei
 * 
 */

/**
 * @Description: <p>
 *               增加DAL 独有数据库配置读取.
 *               </p>
 *               将原来的DatabaseManager.getDatabase(daldbName) 查分开, 先对dbName
 *               做增加后缀.DAL <br>
 *               操作, 目的是如果配有DAL 的后缀则优先使用这个, 否则按照原来的逻辑走. <br>
 *               change by: Li.Hongbo lihongbo@feinno.com
 * 
 * @author lichunlei
 * @date 2014年9月9日 上午10:43:07
 */
// public class DatabaseRpcServer implements DatabaseRpcService {
public class DatabaseRpcServer extends RpcServiceBase {

	public DatabaseRpcServer() {
		this("DatabaseRpcService");
	}

	protected DatabaseRpcServer(String name) {
		super(name);
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRpcServer.class);
	private static final String dbNameAndSql = "dbName:%s spName or sql:%s ";
	private static final Executor executor = ExecutorFactory.newFixedExecutor("DatabaseRpcServer",
	        DALConfiguration.EXECUTER_SIZE.getValue(), DALConfiguration.EXECUTER_LIMIT.getValue());

	@RpcMethod
	public ProtoInteger executeNonQuery(final RpcServerContext ctx) {
		try {
			executor.execute(new Runnable() {
				@Override
				public void run() {
					String dbName = "";
					String sql = "";
					try {
						SqlArgs args = ctx.getArgs(SqlArgs.class);
						dbName = args.getDbName();
						sql = args.getSql();
						if (LOGGER.isInfoEnabled())
							LOGGER.info("executeNonQuery dbName:{}; sql:{}", dbName, sql);
						Object[] values = {};
						if (args.getValues() != null)
							values = SqlArgs.decode(args.getValues()).toArray();

						String daldbName = dbName + ".DAL";
						Database db = DatabaseManager.getDatabase(daldbName);
						if (db == null) {
							db = DatabaseManager.getDatabase(dbName);
						}

						int row = db.executeNonQuery(sql, values);

						ProtoInteger r = new ProtoInteger();
						r.setValue(row);
						ctx.end(r);
					} catch (Exception ex) {
						LOGGER.error("executeNonQuery Exception,dbName+sql:{},ex:{}",
						        String.format(dbNameAndSql, dbName, sql), ex);
						ctx.endWithError(ex);
					} catch (Throwable t) {
						LOGGER.error("executeNonQuery Throwable,dbName+sql:{},ex:{}",
						        String.format(dbNameAndSql, dbName, sql), t);
						ctx.endWithError(t);
					}

				}

			});
		} catch (Throwable tt) {
			LOGGER.error("executor error:{}", tt);
			ctx.endWithError(tt);
		}
		return null;
	}

	@RpcMethod
	public ProtoLong executeInsertWithAutoColumn(final RpcServerContext ctx) {
		try {
			// 异步
			executor.execute(new Runnable() {

				@Override
				public void run() {
					String dbName = "";
					String sql = "";
					try {
						SqlArgs args = ctx.getArgs(SqlArgs.class);
						dbName = args.getDbName();
						sql = args.getSql();
						if (LOGGER.isInfoEnabled())
							LOGGER.info("executeInsertWithAutoColumn dbName:{}; sql:{}", dbName, sql);
						Object[] values = {};
						if (args.getValues() != null)
							values = SqlArgs.decode(args.getValues()).toArray();

						String daldbName = dbName + ".DAL";
						Database db = DatabaseManager.getDatabase(daldbName);
						if (db == null) {
							db = DatabaseManager.getDatabase(dbName);
						}

						long sequence = db.executeInsertWithAutoColumn(sql, values);
						ProtoLong r = new ProtoLong();
						r.setValue(sequence);
						ctx.end(r);
					} catch (Exception ex) {
						LOGGER.error("executeInsertWithAutoColumn Exception:,dbName+sql:{},ex:{}",
						        String.format(dbNameAndSql, dbName, sql), ex);
						ctx.endWithError(ex);
					} catch (Throwable t) {
						LOGGER.error("executeInsertWithAutoColumn Throwable,dbName+sql:{},ex:{}",
						        String.format(dbNameAndSql, dbName, sql), t);
						ctx.endWithError(t);
					}

				}

			});
		} catch (Throwable tt) {
			LOGGER.error("executor error:{}", tt);
			ctx.endWithError(tt);
		}

		return null;

	}

	@RpcMethod
	public ProtoByteArray executeTable(final RpcServerContext ctx) {
		try {
			// 异步
			executor.execute(new Runnable() {

				@Override
				public void run() {
					String dbName = "";
					String sql = "";
					try {
						SqlArgs args = ctx.getArgs(SqlArgs.class);
						dbName = args.getDbName();
						sql = args.getSql();
						if (LOGGER.isInfoEnabled())
							LOGGER.info("executeTable dbName:{}; sql:{}", dbName, sql);

						Object[] values = {};
						if (args.getValues() != null) {
							values = SqlArgs.decode(args.getValues()).toArray();
						}
						String daldbName = dbName + ".DAL";
						Database db = DatabaseManager.getDatabase(daldbName);
						if (db == null) {
							db = DatabaseManager.getDatabase(dbName);
						}

						CachedRowSet rowSet = db.excuteRowSet(sql, values);
						byte[] bytes = null;
						if (rowSet != null) {
							List<CachedRowSet> list = new ArrayList<CachedRowSet>();
							list.add(rowSet);
							bytes = SqlArgs.encodeRowSet(list);
						}
						ProtoByteArray r = new ProtoByteArray();
						r.setValue(bytes);
						ctx.end(r);
					} catch (Exception ex) {
						LOGGER.error("executeTable Exception,dbName+sql:{},ex:{}",
						        String.format(dbNameAndSql, dbName, sql), ex);
						ctx.endWithError(ex);
					} catch (Throwable t) {
						LOGGER.error("executeTable Throwable,dbName+sql:{},ex:{}",
						        String.format(dbNameAndSql, dbName, sql), t);
						ctx.endWithError(t);
					}
				}
			});
		} catch (Exception tt) {
			LOGGER.error("executor error:{}", tt);
			ctx.endWithError(tt);
		}

		return null;
	}

	@RpcMethod
	public ProtoByteArray executeReader(final RpcServerContext ctx) {
		try {
			// 异步
			executor.execute(new Runnable() {

				@Override
				public void run() {
					DataReader reader = null;
					String dbName = "";
					String sql = "";
					try {
						SqlArgs args = ctx.getArgs(SqlArgs.class);
						dbName = args.getDbName();
						sql = args.getSql();
						if (LOGGER.isInfoEnabled())
							LOGGER.info("executeReader dbName:{}; sql:{}", dbName, sql);

						Object[] values = {};
						if (args.getValues() != null) {
							values = SqlArgs.decode(args.getValues()).toArray();
						}
						String daldbName = dbName + ".DAL";
						Database db = DatabaseManager.getDatabase(daldbName);
						if (db == null) {
							db = DatabaseManager.getDatabase(dbName);
						}

						reader = db.executeReader(sql, values);
						byte[] bytes = null;
						if (reader != null) {
							List<CachedRowSet> list = reader.getAllResultSet();
							bytes = SqlArgs.encodeRowSet(list);
						}
						ProtoByteArray r = new ProtoByteArray();
						r.setValue(bytes);
						ctx.end(r);
					} catch (Exception ex) {
						LOGGER.error("executeReader Exception,dbName+sql:{},ex:{}",
						        String.format(dbNameAndSql, dbName, sql), ex);
						ctx.endWithError(ex);
					} catch (Throwable t) {
						LOGGER.error("executeReader Throwable,dbName+sql:{},ex:{}",
						        String.format(dbNameAndSql, dbName, sql), t);
						ctx.endWithError(t);
					} finally {
						if (reader != null) {
							reader.close();
						}
					}
				}
			});
		} catch (Throwable tt) {
			LOGGER.error("executor error:{}", tt);
			ctx.endWithError(tt);
		}
		return null;
	}

	@RpcMethod
	public ProtoInteger spExecuteNonQuery(final RpcServerContext ctx) {
		try {
			// 异步
			executor.execute(new Runnable() {
				@Override
				public void run() {
					String dbName = "";
					String spName = "";
					try {
						SqlArgs args = ctx.getArgs(SqlArgs.class);
						dbName = args.getDbName();
						spName = args.getSpName();
						String[] params = args.getParams();
						Object[] values = {};
						if (args.getValues() != null) {
							values = SqlArgs.decode(args.getValues()).toArray();
						}
						if (LOGGER.isInfoEnabled())
							LOGGER.info("spExecuteNonQuery dbName:{}; spName:{}", dbName, spName);

						String daldbName = dbName + ".DAL";
						Database db = DatabaseManager.getDatabase(daldbName);
						if (db == null) {
							db = DatabaseManager.getDatabase(dbName);
						}

						int row = db.spExecuteNonQuery(spName, params, values);
						ProtoInteger r = new ProtoInteger();
						r.setValue(row);
						ctx.end(r);
					} catch (Exception ex) {
						LOGGER.error("spExecuteNonQuery Exception,dbName+spName:{},ex:{}",
						        String.format(dbNameAndSql, dbName, spName), ex);
						ctx.endWithError(ex);
					} catch (Throwable t) {
						LOGGER.error("spExecuteNonQuery Throwable,dbName+spName:{},ex:{}",
						        String.format(dbNameAndSql, dbName, spName), t);
						ctx.endWithError(t);
					}
				}
			});
		} catch (Throwable tt) {
			LOGGER.error("executor error:{}", tt);
			ctx.endWithError(tt);
		}
		return null;
	}

	@RpcMethod
	public ProtoByteArray spExecuteTable(final RpcServerContext ctx) {
		try {
			// 异步
			executor.execute(new Runnable() {
				@Override
				public void run() {
					String dbName = "";
					String spName = "";
					try {
						SqlArgs args = ctx.getArgs(SqlArgs.class);
						dbName = args.getDbName();
						spName = args.getSpName();
						String[] params = args.getParams();
						Object[] values = {};
						if (args.getValues() != null) {
							values = SqlArgs.decode(args.getValues()).toArray();
						}
						if (LOGGER.isInfoEnabled())
							LOGGER.info("spExecuteTable dbName:{}; spName:{}", dbName, spName);
						String daldbName = dbName + ".DAL";
						Database db = DatabaseManager.getDatabase(daldbName);
						if (db == null) {
							db = DatabaseManager.getDatabase(dbName);
						}

						CachedRowSet rowSet = db.spExecuteRowSet(spName, params, values);
						byte[] bytes = null;
						if (rowSet != null) {
							List<CachedRowSet> list = new ArrayList<CachedRowSet>();
							list.add(rowSet);
							bytes = SqlArgs.encodeRowSet(list);
						}
						ProtoByteArray r = new ProtoByteArray();
						r.setValue(bytes);
						ctx.end(r);
					} catch (Exception ex) {
						LOGGER.error("spExecuteTable Exception,dbName+spName:{},ex:{}",
						        String.format(dbNameAndSql, dbName, spName), ex);
						ctx.endWithError(ex);
					} catch (Throwable t) {
						LOGGER.error("spExecuteTable Throwable,dbName+spName:{},ex:{}",
						        String.format(dbNameAndSql, dbName, spName), t);
						ctx.endWithError(t);
					}
				}
			});
		} catch (Throwable tt) {
			LOGGER.error("executor error:{}", tt);
			ctx.endWithError(tt);
		}
		return null;
	}

	@RpcMethod
	public ProtoByteArray spExecuteReader(final RpcServerContext ctx) {
		try {
			// 异步
			executor.execute(new Runnable() {

				@Override
				public void run() {
					DataReader reader = null;
					String dbName = "";
					String spName = "";
					try {
						SqlArgs args = ctx.getArgs(SqlArgs.class);
						dbName = args.getDbName();
						spName = args.getSpName();
						String[] params = args.getParams();
						Object[] values = {};
						if (args.getValues() != null) {
							values = SqlArgs.decode(args.getValues()).toArray();
						}
						if (LOGGER.isInfoEnabled())
							LOGGER.info("spExecuteReader dbName:{}; spName:{}", dbName, spName);
						String daldbName = dbName + ".DAL";
						Database db = DatabaseManager.getDatabase(daldbName);
						if (db == null) {
							db = DatabaseManager.getDatabase(dbName);
						}

						reader = db.spExecuteReader(spName, params, values);
						byte[] bytes = null;
						if (reader != null) {
							List<CachedRowSet> list = reader.getAllResultSet();
							bytes = SqlArgs.encodeRowSet(list);
						}
						ProtoByteArray r = new ProtoByteArray();
						r.setValue(bytes);
						ctx.end(r);
					} catch (Exception ex) {
						LOGGER.error("spExecuteReader Exception,dbName+spName:{},ex:{}",
						        String.format(dbNameAndSql, dbName, spName), ex);
						ctx.endWithError(ex);
					} catch (Throwable t) {
						LOGGER.error("spExecuteReader Throwable,dbName+spName:{},ex:{}",
						        String.format(dbNameAndSql, dbName, spName), t);
						ctx.endWithError(t);
					} finally {
						if (reader != null) {
							reader.close();
						}
					}
				}
			});
		} catch (Exception tt) {
			LOGGER.error("executor error:{}", tt);
			ctx.endWithError(tt);
		}
		return null;
	}

	/**
	 * history: 2011-10-20 first version 2011-10-27 DataReader.nextResult 支持
	 * 2011-11-24 close reader first 2012-07-25 follow feinno-1.5.0
	 */
}
