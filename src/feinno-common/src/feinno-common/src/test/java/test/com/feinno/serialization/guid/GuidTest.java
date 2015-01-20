/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-4-19
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization.guid;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther duyu
 */
public class GuidTest extends ProtoEntity {
	@ProtoMember(1)
	private GuidInner inner;

	public GuidInner getInner() {
		return inner;
	}

	public void setInner(GuidInner inner) {
		this.inner = inner;
	}
}
