/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-11-16
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
 * @author 杜宇 duyu@feinno.com
 */
public class ClassTypeListBean extends ProtoEntity {
	@ProtoMember(1)
	private List<Short> sList;

	@ProtoMember(2)
	private List<String> strList;

	@ProtoMember(3)
	private List<Integer> iList;

	@ProtoMember(4)
	private List<Long> lList;

	@ProtoMember(5)
	private List<Float> fList;

	@ProtoMember(6)
	private List<Double> dList;

	@ProtoMember(7)
	private List<Byte> bList;

	@ProtoMember(8)
	private List<Character> cList;

	@ProtoMember(9)
	private List<Boolean> boList;

	public List<String> getStrList() {
		return strList;
	}

	public void setStrList(List<String> strList) {
		this.strList = strList;
	}

	public List<Short> getSList() {
		return sList;
	}

	public void setSList(List<Short> sList) {
		this.sList = sList;
	}

	public List<Integer> getIList() {
		return iList;
	}

	public void setIList(List<Integer> iList) {
		this.iList = iList;
	}

	public List<Long> getLList() {
		return lList;
	}

	public void setLList(List<Long> lList) {
		this.lList = lList;
	}

	public List<Float> getFList() {
		return fList;
	}

	public void setFList(List<Float> fList) {
		this.fList = fList;
	}

	public List<Double> getDList() {
		return dList;
	}

	public void setDList(List<Double> dList) {
		this.dList = dList;
	}

	public List<Byte> getBList() {
		return bList;
	}

	public void setBList(List<Byte> bList) {
		this.bList = bList;
	}

	public List<Character> getCList() {
		return cList;
	}

	public void setCList(List<Character> cList) {
		this.cList = cList;
	}

	public List<Boolean> getBoList() {
		return boList;
	}

	public void setBoList(List<Boolean> boList) {
		this.boList = boList;
	}

}
