package com.feinno.logging.spi;

import java.util.Properties;

import com.feinno.logging.LogLevel;
import com.feinno.logging.LoggerContext;

/**
 * 为了新旧日志组件的兼容
 * 
 * @author Lv.Mingwei
 * 
 */
public class LogManager {
	/**
	 * 加载日志配置信息
	 * 
	 * @param props
	 *            Properties类型文件
	 */
	public static void loadSettings(Properties props) {
		LoggerContext.getInstance().loadSettings(props);
	}

	public static void setLevel(String name, LogLevel level) {
		LoggerContext.getInstance().getLogger(name).setLevel(level);
	}

	public static void setLevel(Class<?> clazz, LogLevel level) {
		LoggerContext.getInstance().getLogger(clazz).setLevel(level);
	}

}
