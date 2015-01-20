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
public class BaseTypeBean extends ProtoEntity {

	@ProtoMember(1)
	private short s;

	@ProtoMember(2)
	private int i;

	@ProtoMember(3)
	private long l;

	@ProtoMember(4)
	private float f;

	@ProtoMember(5)
	private double d;

	@ProtoMember(6)
	private byte b;

	@ProtoMember(7)
	private char c;

	@ProtoMember(8)
	private boolean bo;

	public short getS() {
		return s;
	}

	public void setS(short s) {
		this.s = s;
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public long getL() {
		return l;
	}

	public void setL(long l) {
		this.l = l;
	}

	public float getF() {
		return f;
	}

	public void setF(float f) {
		this.f = f;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public byte getB() {
		return b;
	}

	public void setB(byte b) {
		this.b = b;
	}

	public char getC() {
		return c;
	}

	public void setC(char c) {
		this.c = c;
	}

	public boolean isBo() {
		return bo;
	}

	public void setBo(boolean bo) {
		this.bo = bo;
	}

}
