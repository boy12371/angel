package test.com.feinno.serialization.protobuf.enumType;

import com.feinno.util.EnumType;

public final class IntegerEnumType extends EnumType<Integer, IntegerEnumType> {

	public static IntegerEnumType i1 = new IntegerEnumType(1);
	public static IntegerEnumType i2 = new IntegerEnumType(2);
	public static IntegerEnumType i3 = new IntegerEnumType(3);
	public static IntegerEnumType i4 = new IntegerEnumType(4);

	private IntegerEnumType(Integer value) {
		super(value);
	}

	public static IntegerEnumType valueOf(Integer type) {
		return EnumType.valueOf(IntegerEnumType.class, type);
	}

	public static IntegerEnumType defaultValue() {
		return EnumType.valueOf(IntegerEnumType.class, 1);

	}

}
