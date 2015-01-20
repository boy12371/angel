package test.com.feinno.serialization.protobuf.enumType;

import com.feinno.util.EnumType;

public final class FloatEnumType extends EnumType<Float, FloatEnumType> {

	public static final FloatEnumType f1 = new FloatEnumType(1f);
	public static final FloatEnumType f2 = new FloatEnumType(2f);
	public static final FloatEnumType f3 = new FloatEnumType(3f);
	public static final FloatEnumType f4 = new FloatEnumType(4f);

	private FloatEnumType(Float value) {
		super(value);
	}

	public static FloatEnumType valueOf(Float type) {
		return EnumType.valueOf(FloatEnumType.class, type);
	}


	public static FloatEnumType defaultValue() {
		return EnumType.valueOf(FloatEnumType.class, 1f);

	}
}
