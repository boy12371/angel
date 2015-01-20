package com.feinno.ha.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.serialization.protobuf.util.FileUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * <b>描述: </b>获取HA配置文件的工具类,该工具类在寻找一个配置文件的时候会执行以下步骤<br>
 * 1. 首先根据操作系统类型从对应的目录中取<br>
 * 2. 如果上一步没有取到，则从启动目录中取<br>
 * 3. 如果还是没有找到，从当前环境变量中取
 * <p>
 * <b>功能: </b>
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public class HAConfigUtils {

	/** Windows系统下HA配置文件所在的根路径 */
	private static final String CONFIG_ROOT_PATH_WINDOWS = "C:\\ha\\";

	/** Linux系统下HA配置文件所在的根路径 */
	private static final String CONFIG_ROOT_PATH_LINUX = "/etc/ha/";

	/** 以json表示的环境变量 */
	private static JsonObject ENV_JSON = null;

	private static final Logger LOGGER = LoggerFactory.getLogger(HAConfigUtils.class);
	static {
		try {
			LOGGER.info("load env by {}", getConfig("env.conf").getAbsolutePath());
			String content = FileUtil.read(getConfig("env.conf"));
			JsonParser jsonParser = new JsonParser();
			JsonElement elements = jsonParser.parse(content);
			ENV_JSON = elements.getAsJsonObject();
		} catch (Exception e) {
			LOGGER.error(String.format("load env.conf failed ."), e);
		}
	}

	/**
	 * 获得当前操作系统下的配置文件所在的根目录
	 * 
	 * @return
	 */
	public static String getRootPath() {
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Win") || osName.startsWith("win")) {
			LOGGER.info("OS Name : {} , Root Path : {}", osName, CONFIG_ROOT_PATH_WINDOWS);
			return CONFIG_ROOT_PATH_WINDOWS;
		} else {
			LOGGER.info("OS Name : {} , Root Path : {}", osName, CONFIG_ROOT_PATH_LINUX);
			return CONFIG_ROOT_PATH_LINUX;
		}
	}

	/**
	 * 获取一个指定的配置文件<br>
	 * 1. 首先根据操作系统类型从对应的目录中取<br>
	 * 2. 如果上一步没有取到，则从启动目录中取<br>
	 * 3. 如果还是没有找到，从当前环境变量的位置中取
	 * 
	 * @param fileName
	 * @return
	 */
	public static File getConfig(String fileName) {
		// 1. 首先根据操作系统类型从对应的目录中取
		File configFile = new File(HAConfigUtils.getRootPath() + fileName);
		if (!configFile.exists()) {
			// 2. 如果上一步没有取到，则从启动目录中取
			configFile = new File(fileName);
		}
		// 3. 如果还是没有找到，从当前环境变量的位置中取
		if (!configFile.exists()) {
			fileName = "/META-INF/" + fileName;
			URL url = HAConfigUtils.class.getResource(fileName);
			if (url != null) {
				configFile = new File(HAConfigUtils.class.getResource(fileName).getFile());
			} else {
				throw new RuntimeException("Not Found " + fileName);
			}

		}
		return configFile;
	}

	/**
	 * 获取一个指定的配置文件<br>
	 * 1. 首先根据操作系统类型从对应的目录中取<br>
	 * 2. 如果上一步没有取到，则从启动目录中取<br>
	 * 3. 如果还是没有找到，从当前环境变量的位置中取
	 * 
	 * @param fileName
	 * @return
	 */
	public static InputStream getConfigAsStream(String fileName) throws FileNotFoundException {
		// 1. 首先根据操作系统类型从对应的目录中取
		File configFile = new File(HAConfigUtils.getRootPath() + fileName);
		if (!configFile.exists()) {
			// 2. 如果上一步没有取到，则从启动目录中取
			configFile = new File(fileName);
		}
		// 3. 如果还是没有找到，从当前环境变量的位置中取
		if (!configFile.exists()) {
			fileName = "/META-INF/" + fileName;
			URL url = HAConfigUtils.class.getResource(fileName);
			if (url != null) {
				// 如果在包中，可以通过这种方法取到
				return HAConfigUtils.class.getResourceAsStream(fileName);
			} else {
				throw new FileNotFoundException("Not Found " + fileName);
			}
		}
		return new FileInputStream(configFile);
	}

	/**
	 * 获得HA的环境变量，该环境变量从env.con中取得
	 * 
	 * @param key
	 * @return
	 */
	public static String getHAEnv(String key) {
		return ENV_JSON.get(key).getAsString();
	}

}
