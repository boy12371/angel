/*
 * FAE, Feinno App Engine
 *  
 * Create by gaolei 2011-8-1
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package com.feinno.appengine.testing;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcService;
import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * 
 * AppEngine注入调试服务接口 TODO: 目前仅支持SipcAppBean, 距离下一步
 * 
 * @author 高磊 gaolei@feinno.com
 */
@RpcService("AppBeanInjectorService")
public interface AppBeanInjectorService {

	final String SERVICE_NAME = "AppBeanInjectorService";

	@RpcMethod("Inject")
	void inject(InjectArgs args);

	@RpcMethod("Ping")
	void ping();

	public static class InjectArgs extends ProtoEntity {
		@ProtoMember(1)
		private String annos;

		@ProtoMember(2)
		private String grayFactors;

		@ProtoMember(3)
		private String endpoint;

		public String getAnnos() {
			return annos;
		}

		public void setAnnos(String annos) {
			this.annos = annos;
		}

		public String getGrayFactors() {
			return grayFactors;
		}

		public void setGrayFactors(String grayFactors) {
			this.grayFactors = grayFactors;
		}

		public String getEndpoint() {
			return endpoint;
		}

		public void setEndpoint(String endpoint) {
			this.endpoint = endpoint;
		}
	}
}
