/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2010-11-23
 * 
 * Copyright (c) 2010 北京新媒传信科技有限公司
 */
package com.feinno.logging;

import java.util.ArrayList;

import java.util.List;

import org.slf4j.Marker;

import com.feinno.logging.configuration.CoreConstants;
import com.feinno.logging.counter.LoggerCountListener;
import com.feinno.logging.counter.LoggerCounter;
import com.feinno.logging.counter.LoggerCounterCategory;
import com.feinno.logging.filter.CaptureFilter;
import com.feinno.logging.filter.Filter;
import com.feinno.logging.filter.FilterManager;
import com.feinno.logging.filter.FilterReply;

/**
 * 记录日志的核心类,实现了slf4j的Logger接口，日志的操作方式与slf4j系列的log4j和logback一�?br>
 * 部分结构采用了logback的思路
 * 
 * @author Lv.Mingwei
 */
public class Logger implements org.slf4j.Logger {

	// 当前节点下的子节点日�?
	private List<Logger> childrenList;

	// 当前日志节点的父节点
	private Logger parent;

	// 当前日志名称
	private String name;

	// 日志的建造工�?保存它的引用的目的是它会拥有比较丰富的资�?
	private LoggerContext loggerContext;

	// 日志等级，可为空null
	private LogLevel level;

	// 日志的生效等级
	public LogLevel effectiveLevel;

	// 日志Filter，可为空null
	private Filter filter;

	// 日志的生效等级
	public Filter effectiveFilter;
	
	// 日志的生效等级
	public Filter captureFilter;

	// 日志计数
	private LoggerCounter counter;

	// 日志子节点默认的初始化list集合大小
	private static final int DEFAULT_CHILD_ARRAY_SIZE = 5;

	public Logger(String name, Logger parent, LoggerContext loggerContext) {
		this.name = name;
		this.parent = parent;
		this.loggerContext = loggerContext;
		this.counter = LoggerCounterCategory.getLoggerCounter(name);
		//
		captureFilter = new CaptureFilter();
		//FilterManager.addCapture("250934071");
	}

	@Override
	public void debug(String message) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.DEBUG, message, null, null);
	}

	@Override
	public void debug(String message, Object param1) {
		this.filterAndLog_1(null, LogLevel.DEBUG, message, param1, null);
	}

	@Override
	public void debug(String message, Object[] params) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.DEBUG, message, params, null);
	}

	@Override
	public void debug(String message, Throwable t) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.DEBUG, message, null, t);
	}

	@Override
	public void debug(Marker marker, String message) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.DEBUG, message, null, null);
	}

	@Override
	public void debug(String message, Object param1, Object param2) {
		this.filterAndLog_2(null, LogLevel.DEBUG, message, param1, param2, null);
	}

	@Override
	public void debug(Marker marker, String message, Object param1) {
		this.filterAndLog_1(marker, LogLevel.DEBUG, message, param1, null);
	}

	@Override
	public void debug(Marker marker, String message, Object[] params) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.DEBUG, message, params, null);
	}

	@Override
	public void debug(Marker marker, String message, Throwable t) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.DEBUG, message, null, t);
	}

	@Override
	public void debug(Marker marker, String message, Object param1, Object param2) {
		this.filterAndLog_2(marker, LogLevel.DEBUG, message, param1, param2, null);
	}

	@Override
	public void error(String message) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.ERROR, message, null, null);
	}

	@Override
	public void error(String message, Object param1) {
		this.filterAndLog_1(null, LogLevel.ERROR, message, param1, null);
	}

	@Override
	public void error(String message, Object[] params) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.ERROR, message, params, null);
	}

	@Override
	public void error(String message, Throwable t) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.ERROR, message, null, t);
	}

	@Override
	public void error(Marker marker, String message) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.ERROR, message, null, null);
	}

	@Override
	public void error(String message, Object param1, Object param2) {
		this.filterAndLog_2(null, LogLevel.ERROR, message, param1, param2, null);
	}

	@Override
	public void error(Marker marker, String message, Object param1) {
		this.filterAndLog_1(marker, LogLevel.ERROR, message, param1, null);
	}

	@Override
	public void error(Marker marker, String message, Object[] params) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.ERROR, message, params, null);
	}

	@Override
	public void error(Marker marker, String message, Throwable t) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.ERROR, message, null, t);
	}

	@Override
	public void error(Marker marker, String message, Object param1, Object param2) {
		this.filterAndLog_2(marker, LogLevel.ERROR, message, param1, param2, null);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void info(String message) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.INFO, message, null, null);
	}

	@Override
	public void info(String message, Object param1) {
		this.filterAndLog_1(null, LogLevel.INFO, message, param1, null);
	}

	@Override
	public void info(String message, Object[] params) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.INFO, message, params, null);
	}

	@Override
	public void info(String message, Throwable t) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.INFO, message, null, t);
	}

	@Override
	public void info(Marker marker, String message) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.INFO, message, null, null);
	}

	@Override
	public void info(String message, Object param1, Object param2) {
		this.filterAndLog_2(null, LogLevel.INFO, message, param1, param2, null);
	}

	@Override
	public void info(Marker marker, String message, Object param1) {
		this.filterAndLog_1(marker, LogLevel.INFO, message, param1, null);
	}

	@Override
	public void info(Marker marker, String message, Object[] params) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.INFO, message, params, null);
	}

	@Override
	public void info(Marker marker, String message, Throwable t) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.INFO, message, null, t);
	}

	@Override
	public void info(Marker marker, String message, Object param1, Object param2) {
		this.filterAndLog_2(marker, LogLevel.INFO, message, param1, param2, null);
	}

	@Override
	public boolean isDebugEnabled() {
		return isDebugEnabled(null);
	}

	@Override
	public boolean isDebugEnabled(Marker arg0) {
		LogLevel level = LogLevel.DEBUG;
		LogEvent event = new LogEvent(this, level, null, null, null, arg0);
		if (this.captureFilter != null) {
			if (captureFilter.action(event) == FilterReply.ACCEPT){
				return true;
			}
		}
		FilterReply reply = FilterReply.NEUTRAL;
		if (this.effectiveFilter != null) {
			reply = effectiveFilter.action(event);
		}

		if (reply == FilterReply.DENY) {// 如果是拒�?无论如何都不�?
			return false;
		} else if (reply == FilterReply.ACCEPT) {// 如果是接受，无论如何都写
			return true;
		} else if (effectiveLevel.intValue() <= level.intValue()) {// 否则需要大于日志指定级别，才写
			return true;
		}
		return false;
	}

	@Override
	public boolean isErrorEnabled() {
		return isErrorEnabled(null);
	}

	@Override
	public boolean isErrorEnabled(Marker arg0) {
		LogLevel level = LogLevel.ERROR;
		LogEvent event = new LogEvent(this, level, null, null, null, arg0);
		if (this.captureFilter != null) {
			if (captureFilter.action(event) == FilterReply.ACCEPT){
				return true;
			}
		}
		FilterReply reply = FilterReply.NEUTRAL;
		if (this.effectiveFilter != null) {
			reply = effectiveFilter.action(event);
		}

		if (reply == FilterReply.DENY) {// 如果是拒�?无论如何都不�?
			return false;
		} else if (reply == FilterReply.ACCEPT) {// 如果是接受，无论如何都写
			return true;
		} else if (effectiveLevel.intValue() <= level.intValue()) {// 否则需要大于日志指定级别，才写
			return true;
		}
		return false;
	}

	@Override
	public boolean isInfoEnabled() {
		return isInfoEnabled(null);
	}

	@Override
	public boolean isInfoEnabled(Marker arg0) {
		LogLevel level = LogLevel.INFO;
		LogEvent event = new LogEvent(this, level, null, null, null, arg0);
		if (this.captureFilter != null) {
			if (captureFilter.action(event) == FilterReply.ACCEPT){
				return true;
			}
		}
		FilterReply reply = FilterReply.NEUTRAL;
		if (this.effectiveFilter != null) {
			reply = effectiveFilter.action(event);
		}

		if (reply == FilterReply.DENY) {// 如果是拒�?无论如何都不�?
			return false;
		} else if (reply == FilterReply.ACCEPT) {// 如果是接受，无论如何都写
			return true;
		} else if (effectiveLevel.intValue() <= level.intValue()) {// 否则需要大于日志指定级别，才写
			return true;
		}
		return false;
	}

	@Override
	public boolean isTraceEnabled() {
		return isTraceEnabled(null);
	}

	@Override
	public boolean isTraceEnabled(Marker arg0) {
		LogLevel level = LogLevel.TRACE;
		LogEvent event = new LogEvent(this, level, null, null, null, arg0);
		if (this.captureFilter != null) {
			if (captureFilter.action(event) == FilterReply.ACCEPT){
				return true;
			}
		}
		FilterReply reply = FilterReply.NEUTRAL;
		if (this.effectiveFilter != null) {
			reply = effectiveFilter.action(event);
		}

		if (reply == FilterReply.DENY) {// 如果是拒�?无论如何都不�?
			return false;
		} else if (reply == FilterReply.ACCEPT) {// 如果是接受，无论如何都写
			return true;
		} else if (effectiveLevel.intValue() <= level.intValue()) {// 否则需要大于日志指定级别，才写
			return true;
		}
		return false;
	}

	@Override
	public boolean isWarnEnabled() {
		return isWarnEnabled(null);
	}

	@Override
	public boolean isWarnEnabled(Marker arg0) {
		LogLevel level = LogLevel.WARN;
		LogEvent event = new LogEvent(this, level, null, null, null, arg0);
		FilterReply reply = FilterReply.NEUTRAL;
		if (this.effectiveFilter != null) {
			reply = effectiveFilter.action(event);
		}

		if (reply == FilterReply.DENY) {// 如果是拒�?无论如何都不�?
			return false;
		} else if (reply == FilterReply.ACCEPT) {// 如果是接受，无论如何都写
			return true;
		} else if (effectiveLevel.intValue() <= level.intValue()) {// 否则需要大于日志指定级别，才写
			return true;
		}
		return false;
	}

	@Override
	public void trace(String message) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.TRACE, message, null, null);
	}

	@Override
	public void trace(String message, Object param1) {
		this.filterAndLog_1(null, LogLevel.TRACE, message, param1, null);
	}

	@Override
	public void trace(String message, Object[] params) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.TRACE, message, params, null);
	}

	@Override
	public void trace(String message, Throwable t) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.TRACE, message, null, t);
	}

	@Override
	public void trace(Marker marker, String message) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.TRACE, message, null, null);
	}

	@Override
	public void trace(String message, Object param1, Object param2) {
		this.filterAndLog_2(null, LogLevel.TRACE, message, param1, param2, null);
	}

	@Override
	public void trace(Marker marker, String message, Object param1) {
		this.filterAndLog_1(marker, LogLevel.TRACE, message, param1, null);
	}

	@Override
	public void trace(Marker marker, String message, Object[] params) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.TRACE, message, params, null);
	}

	@Override
	public void trace(Marker marker, String message, Throwable t) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.TRACE, message, null, t);
	}

	@Override
	public void trace(Marker marker, String message, Object param1, Object param2) {
		this.filterAndLog_2(marker, LogLevel.TRACE, message, param1, param2, null);
	}

	@Override
	public void warn(String message) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.WARN, message, null, null);
	}

	@Override
	public void warn(String message, Object param1) {
		this.filterAndLog_1(null, LogLevel.WARN, message, param1, null);
	}

	@Override
	public void warn(String message, Object[] params) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.WARN, message, params, null);
	}

	@Override
	public void warn(String message, Throwable t) {
		this.filterAndLog_0_Or3Plus(null, LogLevel.WARN, message, null, t);
	}

	@Override
	public void warn(Marker marker, String message) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.WARN, message, null, null);
	}

	@Override
	public void warn(String message, Object param1, Object param2) {
		this.filterAndLog_2(null, LogLevel.WARN, message, param1, param2, null);
	}

	@Override
	public void warn(Marker marker, String message, Object param1) {
		this.filterAndLog_1(marker, LogLevel.WARN, message, param1, null);
	}

	@Override
	public void warn(Marker marker, String message, Object[] params) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.WARN, message, params, null);
	}

	@Override
	public void warn(Marker marker, String message, Throwable t) {
		this.filterAndLog_0_Or3Plus(marker, LogLevel.WARN, message, null, t);
	}

	@Override
	public void warn(Marker marker, String message, Object param1, Object param2) {
		this.filterAndLog_2(marker, LogLevel.WARN, message, param1, param2, null);
	}

	/**
	 * 过滤日志内容以及写日�?br> 特殊的方法签名相对于同名重载要快一些，而重载相对于可变参数来说又要快一些，尤其当方法为大量被调用的公共方法时，
	 * 因此这里使用了不同的方法名字来应付一参和两参的情况，当参数超过三个时，才会调用到可变参数方法�?br> 以上参数是指
	 * <code>final Object param1</code>参数 <br>
	 * 方法分别�?<br>
	 * {@link Logger#filterAndLog_1(Marker, LogLevel, String, Object, Throwable)}
	 * <br>
	 * {@link Logger#filterAndLog_2(Marker, LogLevel, String, Object, Object, Throwable)}
	 * <br>
	 * {@link Logger#filterAndLog_0_Or3Plus(Marker, LogLevel, String, Object[], Throwable)}
	 * 
	 * @param marker
	 * @param level
	 * @param message
	 * @param param1
	 * @param param2
	 * @param t
	 */
	private final void filterAndLog_1(final Marker marker, final LogLevel level, final String message,
			final Object param1, final Throwable t) {
		// 为计数器提供数据
		LoggerCountListener.doCounter(counter, level, t);
		LogEvent event = new LogEvent(this, level, message, new Object[] { param1 }, t, marker);

		if (captureFilter != null && captureFilter.action(event) == FilterReply.ACCEPT) {//如果被capture，添加到
			callCaptureAppenders(event);
		}
		
		FilterReply reply = FilterReply.NEUTRAL;
		if (this.effectiveFilter != null) {
			reply = effectiveFilter.action(event);
		}
		if (reply == FilterReply.DENY) {// 如果是拒�?无论如何都不�?
			return;
		} else if (reply == FilterReply.ACCEPT) {// 如果是接受，无论如何都写
			callAppenders(event);
		} else if (effectiveLevel.intValue() <= level.intValue()) {// 否则需要大于日志指定级别，才写
			callAppenders(event);
		}
	}

	/**
	 * 过滤日志内容以及写日�?br> 特殊的方法签名相对于同名重载要快一些，而重载相对于可变参数来说又要快一些，尤其当方法为大量被调用的公共方法时，
	 * 因此这里使用了不同的方法名字来应付一参和两参的情况，当参数超过三个时，才会调用到可变参数方法�?br> 以上参数是指
	 * <code>final Object param1</code>参数 <br>
	 * 方法分别�?<br>
	 * {@link Logger#filterAndLog_1(Marker, LogLevel, String, Object, Throwable)}
	 * <br>
	 * {@link Logger#filterAndLog_2(Marker, LogLevel, String, Object, Object, Throwable)}
	 * <br>
	 * {@link Logger#filterAndLog_0_Or3Plus(Marker, LogLevel, String, Object[], Throwable)}
	 * 
	 * 
	 * @param marker
	 * @param level
	 * @param message
	 * @param param1
	 * @param param2
	 * @param t
	 */
	private final void filterAndLog_2(final Marker marker, final LogLevel level, final String message,
			final Object param1, final Object param2, final Throwable t) {
		// 为计数器提供数据
		LoggerCountListener.doCounter(counter, level, t);
		LogEvent event = new LogEvent(this, level, message, new Object[] { param1, param2 }, t, marker);

		if (captureFilter != null &&captureFilter.action(event) == FilterReply.ACCEPT) {//如果被capture，添加到CaptureAppender
			callCaptureAppenders(event);
		}
		FilterReply reply = FilterReply.NEUTRAL;
		if (this.effectiveFilter != null) {
			reply = effectiveFilter.action(event);
		}
		if (reply == FilterReply.DENY) {// 如果是拒�?无论如何都不�?
			return;
		} else if (reply == FilterReply.ACCEPT) {// 如果是接受，无论如何都写
			callAppenders(event);
		} else if (effectiveLevel.intValue() <= level.intValue()) {// 否则需要大于日志指定级别，才写
			callAppenders(event);
		}
	}

	/**
	 * 过滤日志内容以及写日�?br> 特殊的方法签名相对于同名重载要快一些，而重载相对于可变参数来说又要快一些，尤其当方法为大量被调用的公共方法时，
	 * 因此这里使用了不同的方法名字来应付一参和两参的情况，当参数超过三个时，才会调用到可变参数方法�?br> 以上参数是指
	 * <code>final Object param1</code>参数 <br>
	 * 方法分别�?<br>
	 * {@link Logger#filterAndLog_1(Marker, LogLevel, String, Object, Throwable)}
	 * <br>
	 * {@link Logger#filterAndLog_2(Marker, LogLevel, String, Object, Object, Throwable)}
	 * <br>
	 * {@link Logger#filterAndLog_0_Or3Plus(Marker, LogLevel, String, Object[], Throwable)}
	 * 
	 * 
	 * @param marker
	 * @param level
	 * @param message
	 * @param param1
	 * @param param2
	 * @param t
	 */
	private final void filterAndLog_0_Or3Plus(final Marker marker, final LogLevel level, final String message,
			final Object[] params, final Throwable t) {
		// 为计数器提供数据
		LoggerCountListener.doCounter(counter, level, t);

		LogEvent event = new LogEvent(this, level, message, params, t, marker);

		if (captureFilter != null && captureFilter.action(event) == FilterReply.ACCEPT) {//如果被capture，添加到
			callCaptureAppenders(event);
		}
		FilterReply reply = FilterReply.NEUTRAL;
		if (this.effectiveFilter != null) {
			reply = effectiveFilter.action(event);
		}
		if (reply == FilterReply.DENY) {// 如果是拒�?无论如何都不�?
			return;
		} else if (reply == FilterReply.ACCEPT) {// 如果是接受，无论如何都写
			callAppenders(event);
		} else if (effectiveLevel.intValue() <= level.intValue()) {// 否则需要大于日志指定级别，才写
			callAppenders(event);
		}
	}

	/**
	 * 根据目录结构中的"."符号，找到对应的位置，如果点符号找不到，则使�?$'符号找位�?因为可能为内部类�?
	 * 此方法主要是为了提供日志的树形结构方法做支持
	 * 
	 * @param name
	 * @param fromIndex
	 * @return
	 */
	static int getSeparatorIndexOf(String name, int fromIndex) {
		int i = name.indexOf(CoreConstants.DOT, fromIndex);
		if (i != -1) {
			return i;
		} else {
			return name.indexOf(CoreConstants.DOLLAR, fromIndex);
		}
	}

	/**
	 * 在当前节点下，查找是否有符合指定名称的子节点<br>
	 * <code>logback</code>使用的是树形结构来存储日志对象，那么我们也采用了此结构来存储日志对象�?
	 * 用于日后当有热更新配置功能时能够通过树形结构找到相应的节点及子节点进行更�?
	 * 
	 * @param childName
	 * @return
	 */
	Logger getChildByName(final String childName) {
		if (childrenList == null) {
			return null;
		} else {
			int len = this.childrenList.size();
			for (int i = 0; i < len; i++) {
				final Logger childLogger_i = (Logger) childrenList.get(i);
				final String childName_i = childLogger_i.getName();

				if (childName.equals(childName_i)) {
					return childLogger_i;
				}
			}
			// no child found
			return null;
		}
	}

	/**
	 * 创在当前节点下创建一个子节点 采用<code>logback</code>的思路采用了树形结�?
	 * 
	 * @param childName
	 * @return
	 */
	Logger createChildByName(final String childName) {
		int i_index = getSeparatorIndexOf(childName, this.name.length() + 1);
		if (i_index != -1) {
			throw new IllegalArgumentException("For logger [" + this.name + "] child name [" + childName
					+ " passed as parameter, may not include '.' after index" + (this.name.length() + 1));
		}

		if (childrenList == null) {
			childrenList = new ArrayList<Logger>(DEFAULT_CHILD_ARRAY_SIZE);
		}
		Logger childLogger;
		childLogger = new Logger(childName, this, this.loggerContext);
		childrenList.add(childLogger);
		childLogger.effectiveLevel = this.effectiveLevel;
		childLogger.effectiveFilter = this.effectiveFilter;
		return childLogger;
	}

	/**
	 * 采用<code>logback</code>
	 * 的思路采用了树形结构，因此在设置一个节点的等级后，会遍历它的子节点，将子节点的生效等级也相应的修改为此等级，当发现子节点有自己的配置等级时�?
	 * 才停�?
	 * 
	 * @param newLevel
	 */
	public synchronized void setLevel(LogLevel newLevel) {
		if (level == newLevel) {
			// nothing to do;
			return;
		}
		if (newLevel == null && isRootLogger()) {
			throw new IllegalArgumentException("The level of the root logger cannot be set to null");
		}

		level = newLevel;
		if (newLevel == null) {
			effectiveLevel = parent.effectiveLevel;
		} else {
			effectiveLevel = newLevel;
		}

		if (childrenList != null) {
			int len = childrenList.size();
			for (int i = 0; i < len; i++) {
				Logger child = (Logger) childrenList.get(i);
				// tell child to handle parent levelInt change
				child.handleParentLevelChange(effectiveLevel);
			}
		}
	}

	/**
	 * 如果父节点的日志等级有变更，会递归的变更其子节点的日志等级，直到找到子节点的真实等级不为空的情�?br>
	 * 此方法与setLevel方法相对应，是由setLevel方法调入进来
	 * 
	 * @param newParentLevelInt
	 */
	private synchronized void handleParentLevelChange(LogLevel newParentLevelInt) {
		// changes in the parent levelInt affect children only if their levelInt
		// is
		// null
		if (level == null) {
			effectiveLevel = newParentLevelInt;

			// propagate the parent levelInt change to this logger's children
			if (childrenList != null) {
				int len = childrenList.size();
				for (int i = 0; i < len; i++) {
					Logger child = (Logger) childrenList.get(i);
					child.handleParentLevelChange(newParentLevelInt);
				}
			}
		}
	}

	/**
	 * 与等级的设置很类似，此处是设置一个Filter
	 * 
	 * @param newFilter
	 */
	public synchronized void setFilter(Filter newFilter) {
		if (filter == newFilter) {
			// nothing to do;
			return;
		}

		filter = newFilter;
		if (newFilter == null) {
			effectiveFilter = parent.effectiveFilter;
		} else {
			effectiveFilter = newFilter;
		}

		if (childrenList != null) {
			int len = childrenList.size();
			for (int i = 0; i < len; i++) {
				Logger child = (Logger) childrenList.get(i);
				// tell child to handle parent filter change
				child.handleParentFilterChange(effectiveFilter);
			}
		}
	}

	/**
	 * 如果父节点的日志Filter有变更，会递归的变更其子节点的Filter，直到找到子节点的真实Filter不为空的情况<br>
	 * 此方法与setFilter方法相对应，是由setFilter方法调入进来
	 * 
	 * @param newParentFilterInt
	 */
	private synchronized void handleParentFilterChange(Filter newParentFilterInt) {
		// changes in the parent filter affect children only if their levelInt
		// is
		// null
		if (level == null) {
			effectiveFilter = newParentFilterInt;

			// propagate the parent filter change to this logger's children
			if (childrenList != null) {
				int len = childrenList.size();
				for (int i = 0; i < len; i++) {
					Logger child = (Logger) childrenList.get(i);
					child.handleParentFilterChange(newParentFilterInt);
				}
			}
		}
	}

	/**
	 * 判断是否是根节点
	 * 
	 * @return
	 */
	private final boolean isRootLogger() {
		return parent == null;
	}

	/**
	 * 通知appender可以开始工作了
	 * 
	 * @param event
	 *            The event to log
	 */
	public void callCaptureAppenders(LogEvent event) {
		// 不同于logback一样遍历所有节点的appender，我们只需要调用根节点的一个既�?
		loggerContext.root.appendCaptureAppenders(event);
	}


	/**
	 * 通知appender可以开始工作了
	 * 
	 * @param event
	 *            The event to log
	 */
	public void callAppenders(LogEvent event) {
		// 不同于logback一样遍历所有节点的appender，我们只需要调用根节点的一个既�?
		loggerContext.root.appendLoopOnAppenders(event);
	}
	

	/**
	 * 发消息给capture
	 * 
	 * @param event
	 * @return
	 */
	private void appendCaptureAppenders(LogEvent event) {
		if (loggerContext.getCaptureAttachable() != null) {
			loggerContext.getCaptureAttachable().appendLoopOnAppenders(event);
		}
	}

	/**
	 * 发消息给appender，写日志
	 * 
	 * @param event
	 * @return
	 */
	private void appendLoopOnAppenders(LogEvent event) {
		if (loggerContext.getAppenderAttachable() != null) {
			loggerContext.getAppenderAttachable().appendLoopOnAppenders(event);
		}
	}

	public final Filter getFilter() {
		return effectiveFilter;
	}

}
