/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-11-16
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization.entity;

import java.util.Date;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 */
public class ClassTypeBean extends ProtoEntity {

	@ProtoMember(1)
	private String str;

	@ProtoMember(2)
	private Short s;

	@ProtoMember(3)
	private Integer i;

	@ProtoMember(4)
	private Long l;

	@ProtoMember(5)
	private Float f;

	@ProtoMember(6)
	private Double d;

	@ProtoMember(7)
	private Byte b;

	@ProtoMember(8)
	private Character c;

	@ProtoMember(9)
	private Boolean bo;

	@ProtoMember(10)
	private ConfigTypeEnum type = ConfigTypeEnum.UNKOWN;
	
	@ProtoMember(10)
	private Date date;

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public Short getS() {
		return s;
	}

	public void setS(Short s) {
		this.s = s;
	}

	public Integer getI() {
		return i;
	}

	public void setI(Integer i) {
		this.i = i;
	}

	public Long getL() {
		return l;
	}

	public void setL(Long l) {
		this.l = l;
	}

	public Float getF() {
		return f;
	}

	public void setF(Float f) {
		this.f = f;
	}

	public Double getD() {
		return d;
	}

	public void setD(Double d) {
		this.d = d;
	}

	public Byte getB() {
		return b;
	}

	public void setB(Byte b) {
		this.b = b;
	}

	public Character getC() {
		return c;
	}

	public void setC(Character c) {
		this.c = c;
	}

	public Boolean getBo() {
		return bo;
	}

	public void setBo(Boolean bo) {
		this.bo = bo;
	}

	public ConfigTypeEnum getType() {
		return type;
	}

	public void setType(ConfigTypeEnum type) {
		this.type = type;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
