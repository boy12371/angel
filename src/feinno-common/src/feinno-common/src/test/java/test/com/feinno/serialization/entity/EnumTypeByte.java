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
public final class EnumTypeByte extends EnumType<Byte, EnumTypeByte> {

	public static final EnumTypeByte TestO = new EnumTypeByte((byte) 1);
	public static final EnumTypeByte TestT = new EnumTypeByte((byte) 2);
	public static final EnumTypeByte TestTh = new EnumTypeByte((byte) 3);

	private EnumTypeByte(Byte value) {
		super(value);
	}

	public static EnumTypeByte defaultValue() {
		return EnumType.valueOf(EnumTypeByte.class, 1);

	}

	public static EnumTypeByte valueOf(Byte key) {
		return EnumType.valueOf(EnumTypeByte.class, key);
	}

}
