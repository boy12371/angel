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
public final class EnumTypeChar extends EnumType<Character, EnumTypeChar> {
	public static final EnumTypeChar TestO = new EnumTypeChar((char)1);
	public static final EnumTypeChar TestT = new EnumTypeChar((char)2);
	public static final EnumTypeChar TestTh = new EnumTypeChar((char)3);

	private EnumTypeChar(Character value) {
		super(value);
	}
	public static EnumTypeChar  defaultValue(){
		return EnumType.valueOf(EnumTypeChar.class, 1);
		
	}
	
	public static EnumTypeChar valueOf(Character key) {
		return EnumType.valueOf(EnumTypeChar.class, key);
	}
}
