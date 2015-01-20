package test.com.feinno.serialization.entity;

import com.feinno.util.EnumInteger;

public enum TestEnum implements EnumInteger {
	TESTONE("test one", 0), TESTTWO("test two", 1);

	private String name;
	private Integer code;

	private TestEnum(String name, Integer code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int intValue() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public static TestEnum getTestEnum(Integer code) {
		for (TestEnum ct : TestEnum.values()) {
			if (ct.intValue() == code) {
				return ct;
			}
		}
		return null;
	}
}
