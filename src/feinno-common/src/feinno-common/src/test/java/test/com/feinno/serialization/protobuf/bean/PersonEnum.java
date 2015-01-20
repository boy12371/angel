package test.com.feinno.serialization.protobuf.bean;

import com.feinno.util.EnumInteger;

public enum PersonEnum implements EnumInteger {

	BEIJING(100), SHANGHAI(200), TIANJING(300), CHONGQING(400), NANJING(500), SUZHOU(600);

	private int value;

	PersonEnum(int value) {
		this.value = value;
	}

	@Override
	public int intValue() {
		return value;
	}
}
