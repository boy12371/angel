package com.feinno.appengine.resource.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.database.DBSyncResult;
import com.feinno.rpc.channel.RpcException;
import com.feinno.rpc.channel.RpcFuture;
import com.feinno.rpc.channel.RpcResults;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.client.RpcMethodStub;
import com.feinno.rpc.client.RpcProxyFactory;
import com.feinno.serialization.protobuf.types.ProtoByteArray;
import com.feinno.serialization.protobuf.types.ProtoInteger;
import com.feinno.serialization.protobuf.types.ProtoLong;
import com.feinno.util.Action;
import com.feinno.util.EventHandler;
import com.feinno.util.StringUtils;

/**
 * 通过Rpc方式访问DAL再访问Database
 * 1.存在多个DAL的情况下，随机方式访问其中一台服务器
 * 2.TODO：事务支持
 * 
 * @author lichunlei
 *
 */

public class DatabaseRpcClient {
	
	//DAL的地址列表
	private List<RpcTcpEndpoint> endpoints; 
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseRpcClient.class);
	
	private static DatabaseRpcClient instance;
	
	private static Random rand = new Random();
	
	private static String serviceName = "DatabaseRpcService";
	//RpcClient的超时时间
	public static final int TIMEOUT = 10 * 1000;
	
	private DatabaseRpcClient()
	{
		
	}
	public DatabaseRpcClient(List<RpcTcpEndpoint> endpoints)
	{
		this.endpoints = endpoints;
	}
	
    public synchronized static DatabaseRpcClient getInstance()
	{
		if(instance == null)
		{
			String hosts = AppEngineManager.INSTANCE.getSettings().getDalHosts();
			if(StringUtils.isNullOrEmpty(hosts))
				throw new IllegalArgumentException("appegine.properties dalHosts can not be null");
			String[] hostStr = hosts.split(";");
			List<RpcTcpEndpoint> ps = new ArrayList<RpcTcpEndpoint>();
			for(int i=0;i<hostStr.length;i++)
			{
				RpcTcpEndpoint point = RpcTcpEndpoint.parse("tcp://"+hostStr[i]);
				ps.add(point);
			}
			instance = new DatabaseRpcClient(ps);
		}
		return instance;
	}


	/**
	 * 执行不返回结果集的sql语句
	 * @param SqlArgs
	 * @return (1)SQL 数据操作语言 (DML) 语句的行数 (2)对于无返回内容的SQL语句，返回 0
	 */
	ProtoInteger executeNonQuery(SqlArgs args)
	{
		RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "executeNonQuery";
		
		try {
			RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
			RpcFuture future = stub.invoke(args, TIMEOUT);
			return future.syncGet(ProtoInteger.class);
		} catch (RpcException ex) {
			throw new RuntimeException(ex);
		}
	}
	
		
	/**
	 * 
	 * 执行一个存储过程, 返回一个DataTable, 结果序列化为byte[]
	 * @param SqlArgs
	 * @return 包含该查询生成的数据的CachedRowSet序列化后的byte[]
	 */
	ProtoByteArray executeTable(SqlArgs args)
	{
		RpcTcpEndpoint ep = getRandomEndpoint();
		
		String method = "executeTable";
		
		try {
			RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
			RpcFuture future = stub.invoke(args, TIMEOUT);
			return future.syncGet(ProtoByteArray.class);
		} catch (RpcException ex) {
			throw new RuntimeException(ex);
		}
		
	}
	
	/**
	 * 
	 * 执行一个存储过程, 返回一个DataReader, 结果序列化为byte[]
	 * @param SqlArgs
	 * @return 包含该查询生成的数据的CachedRowSet序列化后的byte[]
	 */
	ProtoByteArray executeReader(SqlArgs args)
	{
		RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "executeReader";		
		try {
			RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
			RpcFuture future = stub.invoke(args, TIMEOUT);
			return future.syncGet(ProtoByteArray.class);
		} catch (RpcException ex) {
			throw new RuntimeException(ex);
		}
		
	}
	
    /**
     * 对一个带有自增长字段的表，执行一条insert语句，并返回自增长的值。
     * @param SqlArgs
     * @return 返回自增长字段的值。如果该表不带自增长字段，则返回-1。

     */
    ProtoLong executeInsertWithAutoColumn(SqlArgs args)
    {

		RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "executeInsertWithAutoColumn";		
		try {
			RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
			RpcFuture future = stub.invoke(args, TIMEOUT);
			return future.syncGet(ProtoLong.class);
		} catch (RpcException ex) {
			throw new RuntimeException(ex);
		}
    	
    }
    

	ProtoInteger spExecuteNonQuery(SqlArgs args)
	{
		RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "spExecuteNonQuery";		
		try {
			RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
			RpcFuture future = stub.invoke(args, TIMEOUT);
			return future.syncGet(ProtoInteger.class);
		} catch (RpcException ex) {
			throw new RuntimeException(ex);
		}
	}
	
    
    ProtoByteArray spExecuteTable(SqlArgs args){
    	RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "spExecuteTable";		
		try {
			RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
			RpcFuture future = stub.invoke(args, TIMEOUT);
			return future.syncGet(ProtoByteArray.class);
		} catch (RpcException ex) {
			throw new RuntimeException(ex);
		}
    }
    
    public ProtoByteArray spExecuteReader(SqlArgs args){
    	RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "spExecuteReader";		
		try {
			RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
			RpcFuture future = stub.invoke(args, TIMEOUT);
			return future.syncGet(ProtoByteArray.class);
		} catch (RpcException ex) {
			throw new RuntimeException(ex);
		}
	
    }

    private RpcTcpEndpoint getRandomEndpoint()
    {
    	int r = rand.nextInt(endpoints.size());
		return endpoints.get(r);    	
    }
	
	/***sync***/
    
    /**
	 * 执行不返回结果集的sql语句
	 * @param SqlArgs
	 * @return (1)SQL 数据操作语言 (DML) 语句的行数 (2)对于无返回内容的SQL语句，返回 0
	 */
	void executeNonQuerySync(SqlArgs args,final Action<DBSyncResult<ProtoInteger>> callback)
	{
		RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "executeNonQuery";
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
		RpcFuture future = stub.invoke(args, TIMEOUT);
		future.addListener(new EventHandler<RpcResults>() {
			@Override
			public void run(Object sender, RpcResults a)
			{
				DBSyncResult<ProtoInteger> dbResult = new DBSyncResult<ProtoInteger>();
				
				if(a.getError()!=null)
					dbResult.setException(a.getError());
				else
				{
					ProtoInteger r = (ProtoInteger) a.getValue(ProtoInteger.class);
					dbResult.setResult(r);
				}
				callback.run(dbResult);
			}
		});
		
	}
	
	/**
	 * 
	 * 执行一个存储过程, 返回一个CachedRowSet, 结果序列化为byte[]
	 * @param SqlArgs
	 * @return 包含该查询生成的数据的CachedRowSet序列化后的byte[]
	 */
	void executeTableSync(SqlArgs args,final Action<DBSyncResult<ProtoByteArray>> callback)
	{
		RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "executeTable";
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
		RpcFuture future = stub.invoke(args, TIMEOUT);
		future.addListener(new EventHandler<RpcResults>() {
			@Override
			public void run(Object sender, RpcResults a)
			{
				DBSyncResult<ProtoByteArray> dbResult = new DBSyncResult<ProtoByteArray>();
				
				if(a.getError()!=null)
					dbResult.setException(a.getError());
				else
				{
					ProtoByteArray bytes = (ProtoByteArray)a.getValue(ProtoByteArray.class);
					dbResult.setResult(bytes);					
				}
				callback.run(dbResult);
			}
		});
		
	}
	
	/**
	 * 
	 * 执行一个存储过程, 返回一个DataRader, 结果序列化为byte[]
	 * @param SqlArgs
	 * @return 包含该查询生成的数据的CachedRowSet序列化后的byte[]
	 */
	void executeReaderSync(SqlArgs args,final Action<DBSyncResult<ProtoByteArray>> callback)
	{
		RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "executeReader";
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
		RpcFuture future = stub.invoke(args, TIMEOUT);
		future.addListener(new EventHandler<RpcResults>() {
			@Override
			public void run(Object sender, RpcResults a)
			{
				DBSyncResult<ProtoByteArray> dbResult = new DBSyncResult<ProtoByteArray>();
				
				if(a.getError()!=null)
					dbResult.setException(a.getError());
				else
				{
					ProtoByteArray bytes = (ProtoByteArray)a.getValue(ProtoByteArray.class);
					dbResult.setResult(bytes);					
				}
				callback.run(dbResult);
			}
		});

	}
	
    /**
     * 对一个带有自增长字段的表，执行一条insert语句，并返回自增长的值。
     * @param SqlArgs
     * @return 返回自增长字段的值。如果该表不带自增长字段，则返回-1。

     */
    void executeInsertWithAutoColumnSync(SqlArgs args,final Action<DBSyncResult<ProtoLong>> callback)
    {
    	
    	RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "executeInsertWithAutoColumn";
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
		RpcFuture future = stub.invoke(args, TIMEOUT);
		future.addListener(new EventHandler<RpcResults>() {
			@Override
			public void run(Object sender, RpcResults a)
			{
				DBSyncResult<ProtoLong> dbResult = new DBSyncResult<ProtoLong>();
				
				if(a.getError()!=null)
					dbResult.setException(a.getError());
				else
				{
					ProtoLong r = (ProtoLong)a.getValue(ProtoLong.class);
					dbResult.setResult(r);
				}
				callback.run(dbResult);
			}
		});
    }
    

	void spExecuteNonQuerySync(SqlArgs args,final Action<DBSyncResult<ProtoInteger>> callback)
	{
		RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "spExecuteNonQuery";
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
		RpcFuture future = stub.invoke(args, TIMEOUT);
		future.addListener(new EventHandler<RpcResults>() {
			@Override
			public void run(Object sender, RpcResults a)
			{
				DBSyncResult<ProtoInteger> dbResult = new DBSyncResult<ProtoInteger>();
				
				if(a.getError()!=null)
					dbResult.setException(a.getError());
				else
				{
					ProtoInteger r = (ProtoInteger) a.getValue(ProtoInteger.class);
					dbResult.setResult(r);
				}
				callback.run(dbResult);
			}
		});

	}
	
    
    void spExecuteTableSync(SqlArgs args,final Action<DBSyncResult<ProtoByteArray>> callback){
    	
    	RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "spExecuteTable";
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
		RpcFuture future = stub.invoke(args, TIMEOUT);
		future.addListener(new EventHandler<RpcResults>() {
			@Override
			public void run(Object sender, RpcResults a)
			{
				DBSyncResult<ProtoByteArray> dbResult = new DBSyncResult<ProtoByteArray>();
				
				if(a.getError()!=null)
					dbResult.setException(a.getError());
				else
				{
					ProtoByteArray bytes = (ProtoByteArray)a.getValue(ProtoByteArray.class);
					dbResult.setResult(bytes);
					
				}
				callback.run(dbResult);
			}
		});
    	
    }
    
    void spExecuteReaderSync(SqlArgs args,final Action<DBSyncResult<ProtoByteArray>> callback)
    {
    	RpcTcpEndpoint ep = getRandomEndpoint();
		String method = "spExecuteReader";
		RpcMethodStub stub = RpcProxyFactory.getMethodStub(ep, serviceName, method);
		RpcFuture future = stub.invoke(args, TIMEOUT);
		future.addListener(new EventHandler<RpcResults>() {
			@Override
			public void run(Object sender, RpcResults a)
			{
				DBSyncResult<ProtoByteArray> dbResult = new DBSyncResult<ProtoByteArray>();
				
				if(a.getError()!=null)
					dbResult.setException(a.getError());
				else
				{
					ProtoByteArray bytes = (ProtoByteArray)a.getValue(ProtoByteArray.class);
					dbResult.setResult(bytes);
					
				}
				callback.run(dbResult);
			}
		});

    }

}
