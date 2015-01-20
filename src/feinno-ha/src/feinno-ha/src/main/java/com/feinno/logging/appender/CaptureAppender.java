package com.feinno.logging.appender;

import java.lang.reflect.Constructor;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.ha.WorkerAgentHA;
import com.feinno.logging.LogEvent;
import com.feinno.logging.filter.Filter;
import com.feinno.serialization.protobuf.util.ClassUtils;

public class CaptureAppender implements Appender {

	private static Logger LOGGER = LoggerFactory.getLogger(CaptureAppender.class);
	private boolean _enabled = false;
    private String _path;
    private Appender _appender;

    public  CaptureAppender(String path, boolean enabled, String className)
    {
    	_path = path;
    	_enabled = enabled;
    	try
    	{
    		LOGGER.info("CaptureAppender Initialize:" + className);
    		Class<?> classTemp = Class.forName(className);
    		
    		Constructor con = classTemp.getConstructor(String.class, boolean.class);
    		_appender = (Appender)con.newInstance(path, enabled);
    	}
    	catch (Exception ex)
    	{
    		LOGGER.error("CaptureAppender initialize error ",ex);
    	}
    	
    }
    
	@Override
	public boolean isEnabled() {
		return _enabled;
	}

	@Override
	public void doAppend(LogEvent event) {
		_appender.doAppend(event);
	}

	@Override
	public void doAppend(Queue<LogEvent> event) {
		_appender.doAppend(event);
	}

	@Override
	public void destroy() {
		_appender.destroy();
		
	}
}
