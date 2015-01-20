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
public class BaseTypeArrayBean extends ProtoEntity {
	@ProtoMember(1)
	private short[] sArray;

	@ProtoMember(2)
	private int[] iArray;

	@ProtoMember(3)
	private long[] lArray;

	@ProtoMember(4)
	private float[] fArray;

	@ProtoMember(5)
	private double[] dArray;

	@ProtoMember(6)
	private byte[] bArray;

	@ProtoMember(7)
	private char[] cArray;

	@ProtoMember(8)
	private boolean[] boArray;


	public short[] getSArray() {
		return sArray;
	}

	public void setSArray(short[] sArray) {
		this.sArray = sArray;
	}

	public int[] getIArray() {
		return iArray;
	}

	public void setIArray(int[] iArray) {
		this.iArray = iArray;
	}

	public long[] getLArray() {
		return lArray;
	}

	public void setLArray(long[] lArray) {
		this.lArray = lArray;
	}

	public float[] getFArray() {
		return fArray;
	}

	public void setFArray(float[] fArray) {
		this.fArray = fArray;
	}

	public double[] getDArray() {
		return dArray;
	}

	public void setDArray(double[] dArray) {
		this.dArray = dArray;
	}

	public byte[] getBArray() {
		return bArray;
	}

	public void setBArray(byte[] bArray) {
		this.bArray = bArray;
	}

	public char[] getCArray() {
		return cArray;
	}

	public void setCArray(char[] cArray) {
		this.cArray = cArray;
	}

	public boolean[] getBoArray() {
		return boArray;
	}

	public void setBoArray(boolean[] boArray) {
		this.boArray = boArray;
	}

}
