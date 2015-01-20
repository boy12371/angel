package test.com.feinno.serialization.protobuf.enumType;

import com.feinno.util.EnumType;

public final class ByteEnumType extends EnumType<Byte, ByteEnumType> {

	public static final ByteEnumType byte1 = new ByteEnumType((byte) 1);
	
	public static final ByteEnumType byte2 = new ByteEnumType((byte) 2);
	
	public static final ByteEnumType byte3 = new ByteEnumType((byte) 3);

	private ByteEnumType(Byte value) {
		super(value);
	}

	public static ByteEnumType valueOf(Byte type) {
		return EnumType.valueOf(ByteEnumType.class, type);
	}

	public static ByteEnumType defaultValue() {
		return EnumType.valueOf(ByteEnumType.class, (byte) 1);

	}
}
