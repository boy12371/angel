package test.com.feinno.serialization.protobuf.descriptor;

import com.feinno.util.EnumInteger;

public enum EnumClass implements EnumInteger {

	TEST1(1), TEST2(2), TEST3(3), TEST4(4);

	private int value = 0;

	EnumClass(int value) {
		this.value = value;
	}

	@Override
	public int intValue() {
		// TODO Auto-generated method stub
		return value;
	}

}
