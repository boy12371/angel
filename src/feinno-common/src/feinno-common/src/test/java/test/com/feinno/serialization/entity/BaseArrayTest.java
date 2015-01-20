/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-3-11
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization.entity;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther duyu
 */
public class BaseArrayTest extends ProtoEntity {
	@ProtoMember(1)
	private GooglePropertyEntity googlePropertyEntity;
	@ProtoMember(2)
	private Integer id;

	/**
	 * @return the googlePropertyEntity
	 */
	public GooglePropertyEntity getGooglePropertyEntity() {
		return googlePropertyEntity;
	}

	/**
	 * @param googlePropertyEntity
	 *            the googlePropertyEntity to set
	 */
	public void setGooglePropertyEntity(
			GooglePropertyEntity googlePropertyEntity) {
		this.googlePropertyEntity = googlePropertyEntity;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

}
