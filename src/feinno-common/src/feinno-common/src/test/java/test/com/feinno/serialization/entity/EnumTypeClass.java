/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-11-24
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
public class EnumTypeClass extends ProtoEntity {
	
	@ProtoMember(value = 1)
	private EnumTypeInt enumInt;
	
	@ProtoMember(value = 2)
	private EnumTypeLong enumLong;

	@ProtoMember(value = 3)
	private EnumTypeByte enumByte;
	
	@ProtoMember(value = 4)
	private EnumTypeChar enumChar;
	
	@ProtoMember(value = 5)
	private EnumTypeShort enumShort;
	
	@ProtoMember(value = 6)
	private EnumTypeString enumString;

	public EnumTypeInt getEnumInt() {
		return enumInt;
	}

	public void setEnumInt(EnumTypeInt enumInt) {
		this.enumInt = enumInt;
	}

	public EnumTypeLong getEnumLong() {
		return enumLong;
	}

	public void setEnumLong(EnumTypeLong enumLong) {
		this.enumLong = enumLong;
	}

	public EnumTypeByte getEnumByte() {
		return enumByte;
	}

	public void setEnumByte(EnumTypeByte enumByte) {
		this.enumByte = enumByte;
	}

	public EnumTypeChar getEnumChar() {
		return enumChar;
	}

	public void setEnumChar(EnumTypeChar enumChar) {
		this.enumChar = enumChar;
	}

	public EnumTypeShort getEnumShort() {
		return enumShort;
	}

	public void setEnumShort(EnumTypeShort enumShort) {
		this.enumShort = enumShort;
	}

	public EnumTypeString getEnumString() {
		return enumString;
	}

	public void setEnumString(EnumTypeString enumString) {
		this.enumString = enumString;
	}
	
	
}
