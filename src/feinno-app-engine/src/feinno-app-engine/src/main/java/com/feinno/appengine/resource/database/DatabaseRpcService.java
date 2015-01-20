package com.feinno.appengine.resource.database;

import java.sql.SQLException;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;
import com.feinno.serialization.protobuf.types.ProtoByteArray;
import com.feinno.serialization.protobuf.types.ProtoInteger;
import com.feinno.serialization.protobuf.types.ProtoLong;


/**
 * DatabaseRpcService
 * 
 * @author lichunlei
 * 
 */

@RpcService("DatabaseRpcService")
public interface DatabaseRpcService
{
	/**
	 * 执行不返回结果集的存储过程
	 * 
	 * @param spName
	 *            存储过程名
	 * @param params
	 *            参数名,如果参数为空，需要传递String类型的空数组，如定义：String[] params =
	 *            {};然后传递params。
	 * @param values
	 *            参数值
	 * @return (1)SQL 数据操作语言 (DML) 语句的行数 (2)对于无返回内容的SQL语句，返回 0
	 * @throws SQLException
	 */

	ProtoInteger spExecuteNonQuery(SqlArgs args);

	/**
	 * 
	 * 执行一个存储过程, 返回一个DataTable, 序列化到byte[]
	 * 
	 * @param spName
	 *            存储过程名
	 * @param params
	 *            参数名,如果参数为空，需要传递String类型的空数组，如定义：String[] params =
	 *            {};然后传递params。
	 * @param values
	 *            参数值
	 * @return 包含该查询生成的数据的DataTable对象
	 * @throws SQLException
	 */

	ProtoByteArray spExecuteTable(SqlArgs args);

	/**
	 * 
	 * 执行一个存储过程, 返回一个DataReader, 序列化到byte[]
	 * 
	 * @param spName
	 *            存储过程名
	 * @param params
	 *            参数名,如果参数为空，需要传递String类型的空数组，如定义：String[] params =
	 *            {};然后传递params。
	 * @param values
	 *            参数值
	 * @return 包含该查询生成的数据的DataTable对象
	 * @throws SQLException
	 */

	ProtoByteArray spExecuteReader(SqlArgs args);

	/**
	 * 执行一个不返回结果的SQL语句
	 * 
	 * @param sql
	 *            可以包含?参数的sql语句
	 * @param values
	 *            ?对应的参数值
	 * @return (1)SQL 数据操作语言 (DML) 语句的行数 (2)对于无返回内容的SQL语句，返回 0
	 * @throws SQLException
	 */

	ProtoInteger executeNonQuery(SqlArgs args);

	/**
	 * 对一个带有自增长字段的表，执行一条insert语句，并返回自增长的值。
	 * 
	 * @param insertSql
	 *            可以包含?参数的insert语句
	 * @param values
	 *            ?对应的参数值
	 * @return 返回自增长字段的值。如果该表不带自增长字段，则返回-1。
	 * @throws SQLException
	 */

	ProtoLong executeInsertWithAutoColumn(SqlArgs args);

	/**
	 * 执行一个SQL语句, 返回一个DataTable, 结果缓存在Table中
	 * 
	 * @param sql
	 *            可以包含?参数的sql语句
	 * @param values
	 *            ?对应的参数值
	 * @return 包含该查询生成的数据的DataTable对象
	 * @throws SQLException
	 */

	ProtoByteArray executeTable(SqlArgs args);

	/**
	 * 执行一个SQL语句, 返回一个DataReader, 结果缓存在byte[]中
	 * 
	 * @param sql
	 *            可以包含?参数的sql语句
	 * @param values
	 *            ?对应的参数值
	 * @return 包含该查询生成的数据的DataTable对象
	 * @throws SQLException
	 */

	ProtoByteArray executeReader(SqlArgs args);

}
