package com.feinno.logging.counter;

import com.feinno.logging.LogLevel;

import com.feinno.logging.common.LogCommon;

/**
 * 用于给日志计数的具体业务逻辑，达到warn或error时才增加计数信息，另发现异常了，则将最新异常写入
 * 
 * @author Lv.Mingwei
 * 
 */
public class LoggerCountListener
{
	public static void doCounter(LoggerCounter counter, final LogLevel level, final Throwable t)
	{
		// 发现异常了，则将最新异常写入		
		// 达到warn或error时才增加计数信息
		switch (level) {
		case WARN:
			break;
		case ERROR:
			counter.increaseError();
			if (t != null) {
				counter.setLastException(LogCommon.formaErrorNotLn(t));
			}
			break;
		default:
			break;
		}
	}
}