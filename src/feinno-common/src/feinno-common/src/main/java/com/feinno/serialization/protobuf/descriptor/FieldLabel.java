/*
 * FAE, Feinno App Engine
 *  
 * Create by  
 * 
 * Copyright (c) 2012 北京新媒传信科技有限公司
 */
package com.feinno.serialization.protobuf.descriptor;

import com.feinno.util.EnumInteger;

/**
 * Label
 * 
 * @author
 */
public enum FieldLabel implements EnumInteger {
	LABEL_OPTIONAL(1), LABEL_REQUIRED(2), LABEL_REPEATED(3), ;

	private int value;

	FieldLabel(int value) {
		this.value = value;
	}

	@Override
	public int intValue() {
		return value;
	}

	public String getName() {
		switch (this.value) {
		case 2:
			return "required";
		case 3:
			return "repeated";
		case 1:
		default:
			return "optional";
		}
	}
}
