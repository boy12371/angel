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
 * LargeDataInputArgs
 * 
 * @author 李会军
 */
public class LargeDataInputArgs extends ProtoEntity {

	@ProtoMember(1)
	private int count;
	
	@ProtoMember(2)
	private List<String> data;
	
	public int getCount(){
		return count;
	}
	
	public LargeDataInputArgs setCount(int value){
		count = value;
		return this;
	}
	
	public List<String> getData(){
		return data;
	}
	
	public LargeDataInputArgs setData(List<String> value){
		data = value;
		return this;
	}
}
