/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-11-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization.entity;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 */
public class ClassTypeArrayBean extends ProtoEntity {
	@ProtoMember(1)
	private String[] strArray;

	@ProtoMember(2)
	private Short[] sArray;

	@ProtoMember(3)
	private Integer[] iArray;

	@ProtoMember(4)
	private Long[] lArray;

	@ProtoMember(5)
	private Float[] fArray;

	@ProtoMember(6)
	private Double[] dArray;

	@ProtoMember(7)
	private Byte[] bArray;

	@ProtoMember(8)
	private Character[] cArray;

	@ProtoMember(9)
	private Boolean[] boArray;

	public String[] getStrArray() {
		return strArray;
	}

	public void setStrArray(String[] strArray) {
		this.strArray = strArray;
	}

	public Short[] getSArray() {
		return sArray;
	}

	public void setSArray(Short[] sArray) {
		this.sArray = sArray;
	}

	public Integer[] getIArray() {
		return iArray;
	}

	public void setIArray(Integer[] iArray) {
		this.iArray = iArray;
	}

	public Long[] getLArray() {
		return lArray;
	}

	public void setLArray(Long[] lArray) {
		this.lArray = lArray;
	}

	public Float[] getFArray() {
		return fArray;
	}

	public void setFArray(Float[] fArray) {
		this.fArray = fArray;
	}

	public Double[] getDArray() {
		return dArray;
	}

	public void setDArray(Double[] dArray) {
		this.dArray = dArray;
	}

	public Byte[] getBArray() {
		return bArray;
	}

	public void setBArray(Byte[] bArray) {
		this.bArray = bArray;
	}

	public Character[] getCArray() {
		return cArray;
	}

	public void setCArray(Character[] cArray) {
		this.cArray = cArray;
	}

	public Boolean[] getBoArray() {
		return boArray;
	}

	public void setBoArray(Boolean[] boArray) {
		this.boArray = boArray;
	}

}
