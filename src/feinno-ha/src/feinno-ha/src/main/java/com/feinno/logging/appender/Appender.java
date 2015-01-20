package com.feinno.logging.appender;

import java.util.Queue;

import com.feinno.logging.LogEvent;

/**
 * LogFeinno的通用添加接口
 * 
 * @author Lv.Mingwei
 * 
 * @param <E>
 */
public interface Appender {

	public boolean isEnabled();
	
	public void doAppend(LogEvent event);

	public void doAppend(Queue<LogEvent> event);
	
	public void destroy();
}
