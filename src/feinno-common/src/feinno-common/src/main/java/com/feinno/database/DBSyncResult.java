package com.feinno.database;

/**
 * 数据库异步调用返回类
 * 
 * @author lichunlei
 *
 * @param <E>
 */

public class DBSyncResult<E> {
	
	
	private Exception exception;
	
	private E result;

	public E getResult() {
		return result;
	}

	public void setResult(E result) {
		this.result = result;
	}
	
	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}


}
