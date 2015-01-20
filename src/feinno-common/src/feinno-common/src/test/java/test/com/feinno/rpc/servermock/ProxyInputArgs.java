/*
 * FAE, Feinno App Engine
 *  
 * Create by 李会军 2011-1-20
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.rpc.servermock;

import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * ProxyInputArgs
 * 
 * @author 李会军
 */
public class ProxyInputArgs extends ProtoEntity {

	@ProtoMember(1)
	private String uri;
	
	@ProtoMember(2)
	private int count;
	
	@ProtoMember(3)
	private List<String> data;
	
	public String getUri(){
		return uri;
	}
	
	public ProxyInputArgs setUri(String value){
		uri = value;
		return this;
	}
	
	public int getCount(){
		return count;
	}
	
	public ProxyInputArgs setCount(int value){
		count = value;
		return this;
	}
	
	public List<String> getData(){
		return data;
	}
	
	public ProxyInputArgs setData(List<String> value){
		data = value;
		return this;
	}
}
