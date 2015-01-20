package com.feinno.ha;

import java.util.ArrayList;
import java.util.List;

import com.feinno.ha.database.HADatabaseFactory;
import com.feinno.ha.util.HAConfigUtils;
import com.feinno.util.EnumInteger;

/**
 * 
 * <b>描述: </b>这是一个启动选项的枚举类型，它标识出了以下内容<br>
 * 1. HA中所有启动的可选项<br>
 * 2. 每一个选项对应的运行时参数<br>
 * 3. 每一个选项对应的参数准确性
 * <p>
 * <b>功能: </b>标识出了HA中所有启动的可选项，以及选项对应的运行时参数
 * <p>
 * <b>用法: </b>
 * <p>
 * 
 * @author Lv.Mingwei
 * 
 */
public enum StartupOptionEnum implements EnumInteger {

	HA(1) {
		@Override
		public void verifyArgs() throws StartupOptionException {
			if (isEnable()) {
				// -ha ControllerPort
				if (getArgs().size() == 0
						&& (HAConfigUtils.getHAEnv("HA_CONTROLLER_PORT") == null || HAConfigUtils.getHAEnv(
								"HA_CONTROLLER_PORT").length() == 0)) {
					throw new StartupOptionException("Startup options -ha error, Must be provided Controller URL.");
				}
			}
		}
	},
	AGENT(2) {
		@Override
		public void verifyArgs() throws StartupOptionException {
			if (isEnable()) {
				if (getArgs().size() == 0
						&& (HAConfigUtils.getHAEnv("HA_CENTER_URL") == null || HAConfigUtils.getHAEnv("HA_CENTER_URL")
								.length() == 0)) {
					throw new StartupOptionException("Startup options -agent error, Must be provided center URL");
				}
			}
		}
	},
	PORTS(3) {
		@Override
		public void verifyArgs() throws StartupOptionException {
			if (isEnable()) {
				// -ports rpc=8088;http=7022;monitor=8088;
				if (StartupOptionEnum.PORTS.getArgs().size() > 0) {
					String params = StartupOptionEnum.PORTS.getArgs().get(0);
					String[] ports = params.split(";");
					for (String port : ports) {
						String[] args = port.split("=");
						try {
							// 验证端口字段是不是一个正确的数字
							Integer.valueOf(args[1]);
						} catch (Exception e) {
							throw new StartupOptionException(
									"Startup options -ports error,  For example '-ports rpc=8088;http=7022;monitor=8088;'.",e);
						}
					}
				}
			}
		}
	},
	CONFIG(4) {
		@Override
		public void verifyArgs() throws StartupOptionException {
			if (isEnable()) {
				// -config HADB.properties
				if (StartupOptionEnum.CONFIG.getArgs().size() > 0) {
					String properties = StartupOptionEnum.CONFIG.getArgs().get(0);
					try {
						HADatabaseFactory.getProperties(properties);
					} catch (Exception e) {
						throw new StartupOptionException(String.format(
								"Startup options -config config, %s not exists.", properties));
					}
				} else {
					throw new StartupOptionException(
							"Startup options -config config, Must be provided config properties, For example '-config HADB.properties'.");
				}
			}
		}
	},
	LOG(5) {
		@Override
		public void verifyArgs() throws StartupOptionException {
			if (isEnable()) {
				// -log LEVEL=INFO LOGDB=127.0.0.1:3007/LOGDB USER=admin
				// PASSWORD=admin
				if (StartupOptionEnum.LOG.getArgs().size() > 0) {
					for (String params : StartupOptionEnum.LOG.getArgs()) {
						String[] args = params.split("=");
						// 验证协议字段
						if (args == null || args.length != 2) {
							throw new StartupOptionException(
									"Startup options -log error, For example '-log LEVEL=INFO LOGDB=127.0.0.1:3007/LOGDB USER=admin PASSWORD=admin'.");
						} else if (!"LEVEL".equals(args[0]) && !"LOGDB".equals(args[0]) && !"USER".equals(args[0])
								&& !"PASSWORD".equals(args[0])) {
							throw new StartupOptionException(
									"Startup options -log error, For example '-log LEVEL=INFO LOGDB=127.0.0.1:3007/LOGDB USER=admin PASSWORD=admin'.");
						}
						// 验证端口字段是不是一个正确的数字
						try {
							Integer.valueOf(args[1]);
						} catch (Exception e) {
							throw new StartupOptionException(
									"Startup options -log error, For example '-log LEVEL=INFO LOGDB=127.0.0.1:3007/LOGDB USER=admin PASSWORD=admin'.");
						}
					}
				}
			}
		}
	},
	NOZK(6){

		@Override
		public void verifyArgs() throws StartupOptionException {
			//-nozk 带有此参数时，不注册zookeeper，同时不启动AppBeanRouteManager，（无法进行RemoteAppBean的路由），
			//在此种情况下，需要指定端口，不再使用端口池逻辑
			if (isEnable()) {
				
			}
			
		}
		
	},
	BEANS(7){

		@Override
		public void verifyArgs() throws StartupOptionException {
			//带有此参数时，不读取FAE_Application配置，仅启动自身所包含的bean，从ha.xml的meta中读取需要启动的beans
			if (isEnable()) {
				//-beans core-RegisterSipcApp core-GetContactInfo
				if (getArgs().size() == 0) 
					throw new StartupOptionException("Startup options -beans error, Must be provided beans categroy-name");

			}
			
			
		}
		
	}
	;


	private int value;

	/** 是否开启当前操作 */
	private boolean isEnable = false;

	/** 当前操作参数 */
	private List<String> args = new ArrayList<String>();

	/**
	 * 默认的构造方法
	 * 
	 * @param value
	 */
	StartupOptionEnum(int value) {
		this.value = value;
	}

	@Override
	public int intValue() {
		return value;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void addArgs(String args) {
		this.args.add(args);
	}

	public List<String> getArgs() {
		return args;
	}

	/**
	 * 启动项对应的参数正确性验证，如果验证失败，会抛出异常，告知失败原因
	 * 
	 * @return
	 */
	public abstract void verifyArgs() throws StartupOptionException;

	/**
	 * <b>描述: </b>与启动参数项相关的异常
	 * <p>
	 * <b>功能: </b>
	 * <p>
	 * <b>用法: </b>
	 * <p>
	 * 
	 * @author Lv.Mingwei
	 * 
	 */
	public static class StartupOptionException extends Exception {

		private static final long serialVersionUID = 6774859301461202318L;

		public StartupOptionException(String msg) {
			super(msg);
		}

		public StartupOptionException(String msg, Exception exception) {
			super(msg, exception);
		}

		public StartupOptionException(Exception exception) {
			super(exception);
		}

	}
}
