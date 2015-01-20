/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2012-2-20
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor.legacy;

import java.io.IOException;

import com.feinno.rpc.server.RpcMethod;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.rpc.server.RpcService;
import com.feinno.serialization.protobuf.types.ProtoByteArray;
import com.feinno.serialization.protobuf.types.ProtoString;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 */
@RpcService("CategoryList")
public interface CategoryListService {
	
	@RpcMethod("GetCategoryList")
	ProtoByteArray getCategoryList(ProtoString str,RpcServerContext countext) throws IOException;
}
