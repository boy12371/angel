package test.com.feinno.serialization.protobuf.enumType;

import com.feinno.util.EnumType;

public final class CharacterEnumType extends EnumType<Character, CharacterEnumType> {

	public static final CharacterEnumType c1 = new CharacterEnumType('a');
	public static final CharacterEnumType c2 = new CharacterEnumType('b');
	public static final CharacterEnumType c3 = new CharacterEnumType('c');
	public static final CharacterEnumType c4 = new CharacterEnumType('d');

	private CharacterEnumType(Character value) {
		super(value);
	}

	public static CharacterEnumType valueOf(Character type) {
		return EnumType.valueOf(CharacterEnumType.class, type);
	}

	public static CharacterEnumType defaultValue() {
		return EnumType.valueOf(CharacterEnumType.class, 'a');

	}

}
