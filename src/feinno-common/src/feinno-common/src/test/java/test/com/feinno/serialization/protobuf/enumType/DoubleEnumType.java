package test.com.feinno.serialization.protobuf.enumType;

import com.feinno.util.EnumType;

public final class DoubleEnumType extends EnumType<Double, DoubleEnumType> {

	public static final DoubleEnumType d1 = new DoubleEnumType(1d);
	public static final DoubleEnumType d2 = new DoubleEnumType(2d);
	public static final DoubleEnumType d3 = new DoubleEnumType(3d);
	public static final DoubleEnumType d4 = new DoubleEnumType(4d);

	private DoubleEnumType(Double value) {
		super(value);
	}

	public static DoubleEnumType valueOf(Double type) {
		return EnumType.valueOf(DoubleEnumType.class, type);
	}


	public static DoubleEnumType defaultValue() {
		return EnumType.valueOf(DoubleEnumType.class, 1d);

	}
}
