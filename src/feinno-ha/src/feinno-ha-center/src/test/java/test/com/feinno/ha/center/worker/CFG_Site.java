/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-5-15
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.ha.center.worker;

import java.util.Map;

import com.feinno.configuration.ConfigTableField;
import com.feinno.configuration.ConfigTableItem;
import com.feinno.rpc.RpcEndpointFactory;
import com.feinno.rpc.channel.RpcEndpoint;
import com.feinno.util.StringUtils;

/**
 * 描述一个FAE当中的分机房配置
 * 
 * @author 高磊 gaolei@feinno.com
 */
public class CFG_Site extends ConfigTableItem
{
	public static final CFG_Site DEFAULT = new CFG_Site();

	@ConfigTableField(value="SiteName", isKeyField=true)
	private String name = "";

	@ConfigTableField("SiteType")
	private String type = "";

	@ConfigTableField("Gateway")
	private String gateway = "";
	
	@ConfigTableField("AppEngineGateway")
	private String appengineGateway = "";
	
	private RpcEndpoint aegEp;
	private Map<String, String> gatewayAddresses;

	public String getName()
	{
		return name;
	}

	public boolean isPeerSite()
	{
		return "peer".equalsIgnoreCase(type);
	}

	public String getType()
	{
		return this.type;
	}

	public String getGateway()
	{
		return gateway;
	}
	
	public String getGatewayAddress(String protocol)
	{
		return gatewayAddresses.get(protocol);
	}
	
	public String getAppEngineGateway()
	{
		return appengineGateway;
	}
	
	public RpcEndpoint getAppEngineGatewayEp()
	{
		return aegEp;
	}
	
	@Override
	public void afterLoad()
	{
		if (!StringUtils.isNullOrEmpty(gateway)) {
			gatewayAddresses = StringUtils.splitValuePairs(gateway, ";", "=");
		}
		
		if (!StringUtils.isNullOrEmpty(appengineGateway)) {
			Map<String, String> gatewayAddresses = StringUtils.splitValuePairs(appengineGateway, ";", "=");
		
			String ep = gatewayAddresses.get("rpc_tcp"); 
			if (ep != null) {
				aegEp = RpcEndpointFactory.parse(ep);
			}
		}
	}
}
