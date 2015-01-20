/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-11-28
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization.entity;

import com.feinno.util.EnumType;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 */
public final class EnumTypeString extends EnumType<String, EnumTypeString> {

	public static final EnumTypeString TestO = new EnumTypeString("1");
	public static final EnumTypeString TestT = new EnumTypeString("2");
	public static final EnumTypeString TestTh = new EnumTypeString("3");

	private EnumTypeString(String value) {
		super(value);
	}

	public static EnumTypeString defaultValue() {
		return EnumType.valueOf(EnumTypeString.class, "1");

	}

	public static EnumTypeString valueOf(String key) {
		return EnumType.valueOf(EnumTypeString.class, key);
	}

}
