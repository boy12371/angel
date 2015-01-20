package test.com.feinno.serialization.protobuf.enumType;

import com.feinno.util.EnumType;

public final class ShortEnumType extends EnumType<Short, ShortEnumType> {

	public static final ShortEnumType s1 = new ShortEnumType((short) 1);
	public static final ShortEnumType s2 = new ShortEnumType((short) 2);
	public static final ShortEnumType s3 = new ShortEnumType((short) 3);
	public static final ShortEnumType s4 = new ShortEnumType((short) 4);

	private ShortEnumType(Short value) {
		super(value);
	}

	public static ShortEnumType valueOf(Short type) {
		return EnumType.valueOf(ShortEnumType.class, type);
	}

	public static ShortEnumType defaultValue() {
		return EnumType.valueOf(ShortEnumType.class, (short) 1);

	}

}
