/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-17
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.database;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class DataReaderGuardian
{
	public static final DataReaderGuardian INSTANCE = new DataReaderGuardian();
	private static final Logger LOGGER = LoggerFactory.getLogger(DataReaderGuardian.class);
	
	private static final int maxAvailableReaders = 200;
	private static final int maxLifeSeconds = 5;

	private Object syncRoot = new Object();
	private AtomicInteger checking = new AtomicInteger();
	private DataReader[] readers;
	
	private DataReaderGuardian()
	{
		readers = new DataReader[maxAvailableReaders];
	}
	
	/**
	 * 
	 * 干两件事
	 * 1. 检查当前所有打开的Reader，超时的关闭掉
	 * 2. 
	 * 
	 * @param newReader
	 */
	public void checkReaders()
	{
		if (checking.compareAndSet(0, 1)) {
			Date expired = new Date(System.currentTimeMillis() - maxLifeSeconds * 1000);
			
			for (int i = 0; i < readers.length; i++) {
				DataReader reader = readers[i];
				if (reader != null) {
					if (reader.closed()) {
						readers[i] = null;
					} else {
						if (reader.getOpenedTime().before(expired)) {
							LOGGER.warn("DataReader maybe leak:{}", reader.getSql());
							reader.close();
						}
					}
				}
			}
			
			checking.set(0);
		}		
	}
	
	public void addReader(DataReader reader)
	{
		for (int i = 0; i < readers.length; i++) {
			if (readers[i] == null) {
				synchronized (syncRoot) {
					if (readers[i] == null) {
						readers[i] = reader;
						return;
					}
				}
			}
		}
		LOGGER.warn("too many readers");
	}
}
