/*
 * FAE, Feinno App Engine
 *  
 * Create by duyu 2011-11-22
 * 
 * Copyright (c) 2011 北京新媒传信科技有限公司
 */
package test.com.feinno.serialization.entity;

import com.feinno.util.EnumType;

/**
 * {在这里补充类的功能说明}
 * 
 * @author 杜宇 duyu@feinno.com
 * @param <T>
 * @param <K>
 */
public final class EnumTypeInt extends EnumType<Integer, EnumTypeInt> {

	public static final EnumTypeInt TestO = new EnumTypeInt(1);
	public static final EnumTypeInt TestT = new EnumTypeInt(2);
	public static final EnumTypeInt TestTh = new EnumTypeInt(3);

	private EnumTypeInt(Integer value) {
		super(value);
	}
	
	public static EnumTypeInt  defaultValue(){
		return EnumType.valueOf(EnumTypeInt.class, 1);
		
	}
	
	public static EnumTypeInt valueOf(Integer key) {
		return EnumType.valueOf(EnumTypeInt.class, key);
	}



}
