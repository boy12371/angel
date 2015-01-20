/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2012-2-20
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.diagnostic.perfmon.monitor.legacy;

import java.io.IOException;
import java.util.List;

import com.feinno.diagnostic.observation.Observable;
import com.feinno.diagnostic.observation.ObserverManager;
import com.feinno.rpc.server.RpcServerContext;
import com.feinno.serialization.Serializer;
import com.feinno.serialization.protobuf.types.ProtoByteArray;
import com.feinno.serialization.protobuf.types.ProtoString;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 */
public class CategoryListServiceImpl implements CategoryListService {

	private StringBuffer sb = new StringBuffer();
	
	@Override
	public ProtoByteArray getCategoryList(ProtoString str,RpcServerContext countext) throws IOException {
		List<Observable> obList = ObserverManager.getAllObserverItems();
		if(obList == null){
			throw new IllegalArgumentException("Category List is Null");
		}
		
		for(Observable ob : obList){
			sb.append(ob.getObserverName());
			sb.append(",");
		}
		return new ProtoByteArray(Serializer.encode(new ProtoString(sb.substring(0, sb.length() - 1).toString())));
	}

}
