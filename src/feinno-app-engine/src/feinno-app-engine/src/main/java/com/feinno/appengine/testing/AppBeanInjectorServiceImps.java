package com.feinno.appengine.testing;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.annotation.AppName;
import com.feinno.appengine.configuration.AppBeanAnnotation;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.testing.AppBeanInjectorService.InjectArgs;
import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcServiceBase;
import com.feinno.serialization.Serializer;
import com.feinno.util.EnumInteger;

/**
 * AppEngine注入调试服务
 * 
 * @author lvmingwei
 * 
 */
public class AppBeanInjectorServiceImps extends RpcServiceBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppBeanInjectorServiceImps.class);

	public static final AppBeanInjectorServiceImps INSTANCE = new AppBeanInjectorServiceImps();

	public AppBeanInjectorServiceImps() {
		super(AppBeanInjectorService.SERVICE_NAME);
	}

	@RpcMethod("Inject")
	public void inject(RpcServerContext ctx) {
		// 检验与获取参数
		DebugProxy debugProxy = null;
		InjectArgs args = ctx.getArgs(InjectArgs.class);
		AppBeanAnnotations annos = checkArgs(args);
		AppType type = AppType.valueOf(annos);
		String categoryMinusName = annos.getAppCategory() + "-" + annos.getAppName();
		String grayFactors = args.getGrayFactors();

		// 创建对应的DebugProxy
		switch (type) {
		case REMOTE:
			debugProxy = new RemoteDebugProxy(categoryMinusName, grayFactors, ctx);
			break;
		case SIPC:
			debugProxy = new SipcDebugProxy(categoryMinusName, grayFactors, ctx);
			break;
		case MCP:
			// TODO 等待添加McpDebugProxy
		case HTTP:
			debugProxy = new HttpDebugProxy(categoryMinusName, grayFactors, ctx);
			break;
		case SMS:
			// TODO 等待添加SmsDebugProxy
		default:
			LOGGER.error("Inject failed.Does not support {} app.", type);
			ctx.endWithError(new RuntimeException(String.format("Inject failed.Does not support %s app.", type)));
			return;
		}

		// 添加新创建的DebugProxy
		DebugProxyManager.addDebugProxy(debugProxy);
		ctx.end();
	}

	@RpcMethod("Ping")
	public void ping(RpcServerContext ctx) {
		ctx.end();
	}

	/**
	 * 验证传入参数是否合法
	 * 
	 * @param args
	 * @return
	 */
	private AppBeanAnnotations checkArgs(InjectArgs args) {
		if (args == null) {
			throw new RuntimeException("InjectArgs is null.");
		}

		if (args.getAnnos() == null) {
			throw new RuntimeException("Annos is empty.");
		}

		try {
			AppBeanAnnotations annos = Serializer.decode(AppBeanAnnotations.class, args.getAnnos().getBytes());
			AppBeanAnnotation appName = annos.getAppBeanAnnotation(AppName.class);
			annos.setAppCategory(appName.getFieldValue("category"));
			annos.setAppName(appName.getFieldValue("name"));
			if (annos.getAppCategory() == null || annos.getAppCategory().length() == 0) {
				throw new RuntimeException("Category is empty.");
			}
			if (annos.getAppName() == null || annos.getAppName().length() == 0) {
				throw new RuntimeException("AppName is empty.");
			}
			String type = annos.getClassInfo().getType();
			if (type == null || type.length() == 0) {
				throw new RuntimeException("AppType is empty.");
			}
			return annos;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 应用类型
	 * 
	 * @author lvmingwei
	 * 
	 */
	public static enum AppType implements EnumInteger {
		REMOTE(1), SIPC(2), HTTP(3), MCP(4), SMS(5), JOB(6), UNKNOW(404);

		private int value;

		private AppType(int value) {
			this.value = value;
		}

		@Override
		public int intValue() {
			return value;
		}

		public static AppType valueOf(AppBeanAnnotations annos) {
			String type = annos.getClassInfo().getBaseClass().getType();
			if (type == null) {
				return UNKNOW;
			} else if (type.equals("com.feinno.appengine.rpc.RemoteAppBean")) {
				return REMOTE;
			} else if (type.equals("com.feinno.appengine.sipc.SipcAppBean")) {
				return SIPC;
			} else if (type.equals("com.feinno.appengine.http.HttpAppBean")) {
				return HTTP;
			} else if (type.equals("com.feinno.appengine.mcp.McpAppBean")) {
				return MCP;
			} else if (type.equals("com.feinno.appengine.sms.SmsAppBean")) {
				return SMS;
			} else if (type.equals("com.feinno.appengine.job.JobAppBean")) {
				return JOB;
			} else {
				return UNKNOW;
			}
		}

	}

}
