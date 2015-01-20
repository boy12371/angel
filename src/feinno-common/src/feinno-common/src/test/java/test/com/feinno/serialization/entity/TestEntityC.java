/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-4-6
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization.entity;

import java.util.List;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @auther duyu
 */
public class TestEntityC extends ProtoEntity {

	@ProtoMember(value = 1, required = true)
	private String path;

	@ProtoMember(2)
	private ConfigTypeEnum type = ConfigTypeEnum.UNKOWN;
	@ProtoMember(3)
	private List<Integer> i1;
	@ProtoMember(4)
	private List<Integer> i2;
	@ProtoMember(5)
	private Integer[] i3;
	@ProtoMember(6)
	private Integer[] i4;

	/**
	 * @return the i1
	 */
	public List<Integer> getI1() {
		return i1;
	}

	/**
	 * @param i1
	 *            the i1 to set
	 */
	public void setI1(List<Integer> i1) {
		this.i1 = i1;
	}

	/**
	 * @return the i2
	 */
	public List<Integer> getI2() {
		return i2;
	}

	/**
	 * @param i2
	 *            the i2 to set
	 */
	public void setI2(List<Integer> i2) {
		this.i2 = i2;
	}

	/**
	 * @return the i3
	 */
	public Integer[] getI3() {
		return i3;
	}

	/**
	 * @param i3
	 *            the i3 to set
	 */
	public void setI3(Integer[] i3) {
		this.i3 = i3;
	}

	/**
	 * @return the i4
	 */
	public Integer[] getI4() {
		return i4;
	}

	/**
	 * @param i4
	 *            the i4 to set
	 */
	public void setI4(Integer[] i4) {
		this.i4 = i4;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the type
	 */
	public ConfigTypeEnum getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(ConfigTypeEnum type) {

		this.type = type;
	}

}
