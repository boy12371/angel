/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-4-14
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feinno.appengine.AppBean;
import com.feinno.appengine.configuration.AppBeanAnnotations;
import com.feinno.appengine.configuration.AppBeanAnnotationsLoader;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.appengine.runtime.AppHost;
import com.feinno.appengine.testing.AppBeanInjectorService.InjectArgs;
import com.feinno.configuration.ConfigTable;
import com.feinno.configuration.ConfigurationManager;
import com.feinno.ha.ServiceSettings;
import com.feinno.rpc.channel.tcp.RpcTcpEndpoint;
import com.feinno.rpc.duplex.RpcDuplexClient;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppBeanTestProxy
{
	/**
	 * 
	 * xxx com.feinno.imps.message.sipc.SendSmsSipcAppBean sid.in(888888888)
	 * 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args)
	{
		LOGGER.info("AppBeanTestProxy debug running start.");
		// 得到所有特性类型AppBean的proxy地址
		try {
			// ServiceSettings.initDebug("settings.properties"); TODO
			ServiceSettings.init("settings.properties");
			Class<? extends AppBean> beanClazz = (Class<? extends AppBean>) Class.forName(args[0]);
			AppEngineManager.INSTANCE.initialize();
			AppEngineManager.INSTANCE.loadAppBean(beanClazz);

			// AppEngineManager.INSTANCE.bringupHosts();

			//
			// TODO:　通过FAE_DebugProxy得到proxy的地址（多个）
			ConfigTable<Integer, FAEDebugProxyConfigTableItem> table = ConfigurationManager.loadTable(Integer.class, FAEDebugProxyConfigTableItem.class, "FAE_DebugProxy", null);

			for (FAEDebugProxyConfigTableItem item : table.getValues()) {

				RpcTcpEndpoint ep = RpcTcpEndpoint.parse(item.getEndPoint());
				RpcDuplexClient client = new RpcDuplexClient(ep);
				AppBeanInjectorService injector = client.getService(AppBeanInjectorService.class);
				AppBeanAnnotations annos = AppBeanAnnotationsLoader.getAppBeanAnnotaions(beanClazz);
				AppHost host = AppEngineManager.INSTANCE.getHosts().get(annos.getClassInfo().getBaseClass().getType());
				if (host != null) {
					host.registerInjectorService(client);
				}
				client.connectSync();

				InjectArgs ia = new InjectArgs();
				ia.setAnnos(annos.toJsonString());
				ia.setGrayFactors(args[1]); // TODO 灰度GrayFactors校验输入合法性
				injector.inject(ia); // 调用服务器开始注入
			}

			LOGGER.info("=======================================================================================================");
			LOGGER.info("=====================================The Debug Processor Has Started==============================================");
			LOGGER.info("=======================================================================================================");

			//
			// wait
			while (true) {
				Thread.sleep(1000);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(AppBeanTestProxy.class);
}

// [ProtoContract]
// public class InjectArgs
// {
// [ProtoMember(1)]
// public String CategoryMinusName;

//
// [ProtoMember(2)]
// public String Annotations;
// }