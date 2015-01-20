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
public final class EnumTypeShort extends EnumType<Short, EnumTypeShort> {
	public static final EnumTypeShort TestO = new EnumTypeShort((short)1);
	public static final EnumTypeShort TestT = new EnumTypeShort((short)2);
	public static final EnumTypeShort TestTh = new EnumTypeShort((short)3);
	
	private EnumTypeShort(Short value) {
		super(value);
	}
	public static EnumTypeShort  defaultValue(){
		return EnumType.valueOf(EnumTypeShort.class, 1);
		
	}
	
	public static EnumTypeShort valueOf(Short key) {
		return EnumType.valueOf(EnumTypeShort.class, key);
	}

}
