package test.com.feinno.serialization.protobuf.enumType;

import com.feinno.util.EnumType;

public final class StringEnumType extends EnumType<String, StringEnumType> {
	public static final StringEnumType s1 = new StringEnumType("Feinno1");
	public static final StringEnumType s2 = new StringEnumType("Feinno2");
	public static final StringEnumType s3 = new StringEnumType("Feinno3");
	public static final StringEnumType s4 = new StringEnumType("Feinno4");

	private StringEnumType(String value) {
		super(value);
	}

	public static StringEnumType valueOf(String type) {
		return EnumType.valueOf(StringEnumType.class, type);
	}

	public static StringEnumType defaultValue() {
		return EnumType.valueOf(StringEnumType.class, "Feinno1");

	}

}
