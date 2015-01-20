package com.feinno.appengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * 
 * <b>描述: </b>一个用于App的Logger，该Logger有一个特点是会自动记录该App的context.uri
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
class AppLogger implements org.slf4j.Logger {

	private Logger LOGGER;

	private AppTx tx;

	public AppLogger(AppTx tx) {
		this.tx = tx;
		this.LOGGER = LoggerFactory.getLogger(tx.getClass());
	}

	public AppLogger(AppTx tx, Class<?> clazz) {
		this.tx = tx;
		this.LOGGER = LoggerFactory.getLogger(clazz);
	}

	public void setLoggerClass(Class<?> clazz) {
		this.LOGGER = LoggerFactory.getLogger(clazz);
	}

	@Override
	public String getName() {
		return LOGGER.getName();
	}

	@Override
	public boolean isDebugEnabled() {
		return LOGGER.isDebugEnabled(tx.getMarker());
	}

	@Override
	public boolean isDebugEnabled(Marker arg0) {
		return LOGGER.isDebugEnabled(arg0);
	}

	@Override
	public boolean isErrorEnabled() {
		return LOGGER.isErrorEnabled(tx.getMarker());
	}

	@Override
	public boolean isErrorEnabled(Marker arg0) {
		return LOGGER.isErrorEnabled(arg0);
	}

	@Override
	public boolean isInfoEnabled() {
		return LOGGER.isInfoEnabled(tx.getMarker());
	}

	@Override
	public boolean isInfoEnabled(Marker arg0) {
		return LOGGER.isInfoEnabled(arg0);
	}

	@Override
	public boolean isTraceEnabled() {
		return LOGGER.isTraceEnabled(tx.getMarker());
	}

	@Override
	public boolean isTraceEnabled(Marker arg0) {
		return LOGGER.isTraceEnabled(arg0);
	}

	@Override
	public boolean isWarnEnabled() {
		return LOGGER.isWarnEnabled(tx.getMarker());
	}

	@Override
	public boolean isWarnEnabled(Marker arg0) {
		return LOGGER.isWarnEnabled(arg0);
	}

	@Override
	public void debug(String arg0) {
		LOGGER.debug(tx.getMarker(), arg0);
	}

	@Override
	public void debug(String arg0, Object arg1) {
		LOGGER.debug(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void debug(String arg0, Object[] arg1) {
		LOGGER.debug(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void debug(String arg0, Throwable arg1) {
		LOGGER.debug(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void debug(String arg0, Object arg1, Object arg2) {
		LOGGER.debug(tx.getMarker(), arg0, arg1, arg2);
	}

	@Override
	public void debug(Marker arg0, String arg1) {
		LOGGER.debug(arg0, arg1);
	}

	@Override
	public void debug(Marker arg0, String arg1, Object arg2) {
		LOGGER.debug(arg0, arg1, arg2);
	}

	@Override
	public void debug(Marker arg0, String arg1, Object[] arg2) {
		LOGGER.debug(arg0, arg1, arg2);
	}

	@Override
	public void debug(Marker arg0, String arg1, Throwable arg2) {
		LOGGER.debug(arg0, arg1, arg2);
	}

	@Override
	public void debug(Marker arg0, String arg1, Object arg2, Object arg3) {
		LOGGER.debug(arg0, arg1, arg2, arg3);
	}

	@Override
	public void error(String arg0) {
		LOGGER.error(tx.getMarker(), arg0);
	}

	@Override
	public void error(String arg0, Object arg1) {
		LOGGER.error(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void error(String arg0, Object[] arg1) {
		LOGGER.error(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void error(String arg0, Throwable arg1) {
		LOGGER.error(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void error(String arg0, Object arg1, Object arg2) {
		LOGGER.error(tx.getMarker(), arg0, arg1, arg2);
	}

	@Override
	public void error(Marker arg0, String arg1) {
		LOGGER.error(arg0, arg1);
	}

	@Override
	public void error(Marker arg0, String arg1, Object arg2) {
		LOGGER.error(arg0, arg1, arg2);
	}

	@Override
	public void error(Marker arg0, String arg1, Object[] arg2) {
		LOGGER.error(arg0, arg1, arg2);
	}

	@Override
	public void error(Marker arg0, String arg1, Throwable arg2) {
		LOGGER.error(arg0, arg1, arg2);
	}

	@Override
	public void error(Marker arg0, String arg1, Object arg2, Object arg3) {
		LOGGER.error(arg0, arg1, arg2, arg3);
	}

	@Override
	public void info(String arg0) {
		LOGGER.info(tx.getMarker(), arg0);
	}

	@Override
	public void info(String arg0, Object arg1) {
		LOGGER.info(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void info(String arg0, Object[] arg1) {
		LOGGER.info(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void info(String arg0, Throwable arg1) {
		LOGGER.info(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void info(String arg0, Object arg1, Object arg2) {
		LOGGER.info(tx.getMarker(), arg0, arg1, arg2);
	}

	@Override
	public void info(Marker arg0, String arg1) {
		LOGGER.info(arg0, arg1);
	}

	@Override
	public void info(Marker arg0, String arg1, Object arg2) {
		LOGGER.info(arg0, arg1, arg2);
	}

	@Override
	public void info(Marker arg0, String arg1, Object[] arg2) {
		LOGGER.info(arg0, arg1, arg2);
	}

	@Override
	public void info(Marker arg0, String arg1, Throwable arg2) {
		LOGGER.info(arg0, arg1, arg2);
	}

	@Override
	public void info(Marker arg0, String arg1, Object arg2, Object arg3) {
		LOGGER.info(arg0, arg1, arg2, arg3);
	}

	@Override
	public void trace(String arg0) {
		LOGGER.trace(tx.getMarker(), arg0);
	}

	@Override
	public void trace(String arg0, Object arg1) {
		LOGGER.trace(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void trace(String arg0, Object[] arg1) {
		LOGGER.trace(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void trace(String arg0, Throwable arg1) {
		LOGGER.trace(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void trace(String arg0, Object arg1, Object arg2) {
		LOGGER.trace(tx.getMarker(), arg0, arg1, arg2);
	}

	@Override
	public void trace(Marker arg0, String arg1) {
		LOGGER.trace(arg0, arg1);
	}

	@Override
	public void trace(Marker arg0, String arg1, Object arg2) {
		LOGGER.trace(arg0, arg1, arg2);
	}

	@Override
	public void trace(Marker arg0, String arg1, Object[] arg2) {
		LOGGER.trace(arg0, arg1, arg2);
	}

	@Override
	public void trace(Marker arg0, String arg1, Throwable arg2) {
		LOGGER.trace(arg0, arg1, arg2);
	}

	@Override
	public void trace(Marker arg0, String arg1, Object arg2, Object arg3) {
		LOGGER.trace(arg0, arg1, arg2, arg3);
	}

	@Override
	public void warn(String arg0) {
		LOGGER.warn(tx.getMarker(), arg0);
	}

	@Override
	public void warn(String arg0, Object arg1) {
		LOGGER.warn(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void warn(String arg0, Object[] arg1) {
		LOGGER.warn(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void warn(String arg0, Throwable arg1) {
		LOGGER.warn(tx.getMarker(), arg0, arg1);
	}

	@Override
	public void warn(String arg0, Object arg1, Object arg2) {
		LOGGER.warn(tx.getMarker(), arg0, arg1, arg2);
	}

	@Override
	public void warn(Marker arg0, String arg1) {
		LOGGER.warn(arg0, arg1);
	}

	@Override
	public void warn(Marker arg0, String arg1, Object arg2) {
		LOGGER.warn(arg0, arg1, arg2);
	}

	@Override
	public void warn(Marker arg0, String arg1, Object[] arg2) {
		LOGGER.warn(arg0, arg1, arg2);
	}

	@Override
	public void warn(Marker arg0, String arg1, Throwable arg2) {
		LOGGER.warn(arg0, arg1, arg2);
	}

	@Override
	public void warn(Marker arg0, String arg1, Object arg2, Object arg3) {
		LOGGER.warn(arg0, arg1, arg2, arg3);
	}

}