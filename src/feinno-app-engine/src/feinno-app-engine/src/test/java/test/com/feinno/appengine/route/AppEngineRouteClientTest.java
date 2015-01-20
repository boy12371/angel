/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-9-7
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.appengine.route;

import com.feinno.appengine.route.AppEngineRouteClient;
import com.feinno.appengine.rpc.RemoteAppBean;
import com.feinno.appengine.runtime.AppEngineManager;
import com.feinno.ha.ServiceSettings;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class AppEngineRouteClientTest
{
	public static void main(String[] args) throws Exception
	{
		// ServiceSettings.initLocal("settings.properties");
		AppEngineManager.INSTANCE.initialize();
		AppEngineRouteClient.initialize(RemoteAppBean.class,"");
	}
}
