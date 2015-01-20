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
public final class EnumTypeLong extends EnumType<Long, EnumTypeLong> {
	public static final EnumTypeLong TestO = new EnumTypeLong(1l);
	public static final EnumTypeLong TestT = new EnumTypeLong(2l);
	public static final EnumTypeLong TestTh = new EnumTypeLong(3l);

	private EnumTypeLong(Long value) {
		super(value);
	}

	public static EnumTypeLong defaultValue() {
		return EnumType.valueOf(EnumTypeLong.class, 1l);

	}

	public static EnumTypeLong valueOf(Long key) {
		return EnumType.valueOf(EnumTypeLong.class, key);
	}
}
