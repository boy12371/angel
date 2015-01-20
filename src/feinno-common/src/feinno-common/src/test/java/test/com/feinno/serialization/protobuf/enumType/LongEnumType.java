package test.com.feinno.serialization.protobuf.enumType;

import com.feinno.util.EnumType;

public final class LongEnumType extends EnumType<Long, LongEnumType> {

	public static final LongEnumType l1 = new LongEnumType(1L);
	public static final LongEnumType l2 = new LongEnumType(2L);
	public static final LongEnumType l3 = new LongEnumType(3L);
	public static final LongEnumType l4 = new LongEnumType(4L);

	private LongEnumType(Long value) {
		super(value);
	}

	public static LongEnumType valueOf(Long type) {
		return EnumType.valueOf(LongEnumType.class, type);
	}

	public static LongEnumType defaultValue() {
		return EnumType.valueOf(LongEnumType.class, 1L);

	}

}
