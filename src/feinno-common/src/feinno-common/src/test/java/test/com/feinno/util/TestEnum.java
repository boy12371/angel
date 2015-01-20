package test.com.feinno.util;

import com.feinno.util.EnumInteger;

public enum TestEnum implements EnumInteger {
	
	
	A(0x01),
	B(0x02),
	C(0x04);

	private int value;
	
	TestEnum(int i)
	{
		value = i;
	}

	@Override
	public int intValue() {
		return value;
	}

}
