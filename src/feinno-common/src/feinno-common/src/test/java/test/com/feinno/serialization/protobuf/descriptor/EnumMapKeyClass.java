package test.com.feinno.serialization.protobuf.descriptor;

import com.feinno.util.EnumInteger;

public enum EnumMapKeyClass implements EnumInteger {

	K1(1), K2(2), K3(3);

	private int value = 0;

	EnumMapKeyClass(int value) {
		this.value = value;
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return value;
	}

}
