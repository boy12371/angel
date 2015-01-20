package com.feinno.database;

/**
 * 数据库中表已存在的异常
 * 
 * @author Lv.Mingwei
 * 
 */
public class TableExistException extends Exception {

	private static final long serialVersionUID = 1410972059479026371L;

	public TableExistException(String msg) {
		super(msg);
	}

}
