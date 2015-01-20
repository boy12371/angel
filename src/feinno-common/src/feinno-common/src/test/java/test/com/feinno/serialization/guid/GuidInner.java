/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-4-19
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization.guid;

import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther duyu
 */
public class GuidInner extends ProtoEntity {
	@ProtoMember(1)
	private List<Byte> data1;

	@ProtoMember(2)
	private List<Byte> data2;

	/**
	 * @return the data1
	 */
	public List<Byte> getData1() {
		return data1;
	}

	/**
	 * @param data1
	 *            the data1 to set
	 */
	public void setData1(List<Byte> data1) {
		this.data1 = data1;
	}

	/**
	 * @return the data2
	 */
	public List<Byte> getData2() {
		return data2;
	}

	/**
	 * @param data2
	 *            the data2 to set
	 */
	public void setData2(List<Byte> data2) {
		this.data2 = data2;
	}

}
