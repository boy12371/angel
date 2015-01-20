package test.com.feinno.serialization.protobuf.enumType;

import com.feinno.util.EnumType;

public final class BooleanEnumType extends EnumType<Boolean, BooleanEnumType> {

	public static final BooleanEnumType TRUE = new BooleanEnumType(true);

	public static final BooleanEnumType FALSE = new BooleanEnumType(false);

	private BooleanEnumType(Boolean value) {
		super(value);
	}

	public static BooleanEnumType valueOf(Boolean type) {
		return EnumType.valueOf(BooleanEnumType.class, type);
	}

	public static BooleanEnumType defaultValue() {
		return EnumType.valueOf(BooleanEnumType.class, true);

	}
}
