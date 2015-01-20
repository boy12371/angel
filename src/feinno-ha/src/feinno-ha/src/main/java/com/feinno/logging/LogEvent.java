/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-11-29
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.logging;

import java.io.Serializable;
import java.util.Date;

import org.slf4j.Marker;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

import com.feinno.logging.common.LogCommon;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;
import com.feinno.threading.ThreadContext;
import com.feinno.threading.ThreadContextName;
import com.feinno.util.ServiceEnviornment;
import com.feinno.util.TraceContext;

/**
 * 日志事件的实体类, 记录日志的具体信�?
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class LogEvent extends ProtoEntity implements Serializable {

	private static final long serialVersionUID = 9132999088662753326L;
//test
	/** 日志产生时间，此处存储的是long，没有用date是因为date占内存 */
	@ProtoMember(1)
	private long timeStamp;

	/** 日志名称 */
	@ProtoMember(2)
	private String loggerName;

	/** 日志级别 */
	@ProtoMember(3)
	private LogLevel level;

	/** 参数内容 */
	private transient Object[] argumentArray;

	/** 用以存储格式化之前的日志信息 */
	private String message;

	/** 用以存储格式化之后的日志信息 */
	@ProtoMember(4)
	private transient String formattedMessage;

	/** 异常信息 */
	private Throwable throwable;

	/** 异常的序列化 */
	@ProtoMember(5)
	private String throwableString;

	/** 标记 */
	private Marker marker;

	/** 用于序列化时使用的Marker文字描述 */
	@ProtoMember(6)
	private String markerString;

	/** 线程ID */
	@ProtoMember(7)
	private long threadId;

	/** 线程名称 */
	@ProtoMember(8)
	private String threadName;

	/** 进程ID */
	@ProtoMember(9)
	private int pid;

	/** 服务名称 */
	@ProtoMember(10)
	public static String serviceName = ServiceEnviornment.getServiceName();

	/** 扩展字段 */
	@ProtoMember(11)
	private String computer = ServiceEnviornment.getComputerName();
	
	/** 用户标识 */
	@ProtoMember(12)
	private String userIdentify;
	
	/** 请求id */
	@ProtoMember(13)
	private String requestId;

	/** 当被监控的HA_LoggingMarker表中数量超过1个后，开始使用默认的线程上下文中的Marker */
	public static boolean isEnableDefaultMarker = false;

	/**
	 * 用于序列化的默认构造器
	 */
	public LogEvent() {
	}

	/**
	 * 参数构造器
	 * 
	 * @param message
	 *            日志信息
	 * @param level
	 *            日志级别
	 */
	public LogEvent(Logger logger, LogLevel level, String message, Object[] params, Throwable throwable) {

		this.loggerName = logger.getName();
		this.level = level;
		this.message = message;
		FormattingTuple ft = MessageFormatter.arrayFormat(message, params);
		formattedMessage = ft.getMessage();
		if (throwable == null) {
			argumentArray = ft.getArgArray();
			throwable = ft.getThrowable();
		} else {
			this.argumentArray = params;
		}
		this.throwable = throwable;
		timeStamp = System.currentTimeMillis();
		threadName = Thread.currentThread().getName();
		threadId = Thread.currentThread().getId();
		TraceContext tc = TraceContext.GetContext();
		if (tc != null)
		{
			requestId = tc.RequestId;
			userIdentify = tc.UserIdentity;
		}
		pid = ServiceEnviornment.getPid();
		if (isEnableDefaultMarker && ThreadContext.getCurrent() != null
				&& ThreadContext.getCurrent().getNamedContext(ThreadContextName.LOGGING_MARKER) != null) {
			this.marker = (Marker) ThreadContext.getCurrent().getNamedContext(ThreadContextName.LOGGING_MARKER);
		}
	}

	/**
	 * 参数构造器
	 * 
	 * @param message
	 *            日志信息
	 * @param level
	 *            日志级别
	 */
	public LogEvent(Logger logger, LogLevel level, String message, Object[] params, Throwable throwable, Marker marker) {

		this.loggerName = logger.getName();
		this.level = level;
		this.message = message;
		FormattingTuple ft = MessageFormatter.arrayFormat(message, params);
		formattedMessage = ft.getMessage();
		if (throwable == null) {
			argumentArray = ft.getArgArray();
			throwable = ft.getThrowable();
		} else {
			this.argumentArray = params;
		}
		this.throwable = throwable;
		TraceContext tc = TraceContext.GetContext();
		if (tc != null)
		{
			requestId = tc.RequestId;
			userIdentify = tc.UserIdentity;
		}
		timeStamp = System.currentTimeMillis();
		threadName = Thread.currentThread().getName();
		threadId = Thread.currentThread().getId();
		pid = ServiceEnviornment.getPid();

		this.marker = marker;
		if (marker != null && isEnableDefaultMarker && ThreadContext.getCurrent() != null
				&& ThreadContext.getCurrent().getNamedContext(ThreadContextName.LOGGING_MARKER) != null) {
			this.marker.add((Marker) ThreadContext.getCurrent().getNamedContext(ThreadContextName.LOGGING_MARKER));
		} else if (isEnableDefaultMarker && ThreadContext.getCurrent() != null
				&& ThreadContext.getCurrent().getNamedContext(ThreadContextName.LOGGING_MARKER) != null) {
			this.marker = (Marker) ThreadContext.getCurrent().getNamedContext(ThreadContextName.LOGGING_MARKER);
		}

	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getLoggerName() {
		return loggerName;
	}

	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	public LogLevel getLevel() {
		return level;
	}

	public void setLevel(LogLevel level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getThrowableString() {
		if (throwable != null) {
			throwableString = LogCommon.formaError(throwable);
		} else {
			throwableString = "";
		}
		return throwableString;
	}

	public void setThrowableString(String throwableString) {
		this.throwableString = throwableString;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public Marker getMarker() {
		return marker;
	}

	public String getMarkerString() {
		markerString = marker != null ? marker.toString() : "";
		return markerString;
	}

	public void setMarkerString(String markerString) {
		this.markerString = markerString;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		LogEvent.serviceName = serviceName;
	}

	public String getComputer() {
		return computer;
	}

	public void setComputer(String computer) {
		this.computer = computer;
	}

	public void setFormattedMessage(String formattedMessage) {
		this.formattedMessage = formattedMessage;
	}
	
	public String getUserIdentify()
	{
		return userIdentify;
	}
	
	public void setUserIdentify(String value)
	{
		userIdentify = value;
	}
	
	public String getRequestId()
	{
		return requestId;
	}
	
	public void setRequestId(String value)
	{
		requestId = value;
	}

	public String getFormattedMessage() {
		if (formattedMessage != null) {
			return formattedMessage;
		}
		if (argumentArray != null) {
			formattedMessage = MessageFormatter.arrayFormat(message, argumentArray).getMessage();
		} else {
			formattedMessage = message;
		}

		return formattedMessage;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(LogCommon.formatDate(new Date(this.getTimeStamp()))).append("  ");
		buffer.append('[');
		buffer.append(level).append("] ");
		
		if (this.getUserIdentify() != null)
		{
			buffer.append(this.getUserIdentify()).append("#");
		}
		
		if (this.getRequestId() != null)
		{
			buffer.append(this.getRequestId()).append("  ");
		}

		buffer.append(this.getThreadId()).append("  ");

		if (this.getThreadName() != null) {
			buffer.append(this.getThreadName()).append("  ");
		}

		if (this.getComputer() != null) {
			buffer.append(this.getComputer()).append("  ");
		}

		if (this.getServiceName() != null) {
			buffer.append(this.getServiceName()).append("  ");
		}

		if (this.getLoggerName() != null) {
			buffer.append(this.getLoggerName()).append("  ");
		}

		// if (this.getTag() != null)
		// buffer.append(this.getTag()).append("  ");
		buffer.append(getFormattedMessage()).append("  ");
		buffer.append(getThrowableString());

		return buffer.toString().trim();
	}
}
