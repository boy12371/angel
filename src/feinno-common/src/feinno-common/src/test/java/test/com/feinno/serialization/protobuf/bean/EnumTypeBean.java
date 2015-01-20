package test.com.feinno.serialization.protobuf.bean;

import test.com.feinno.serialization.protobuf.enumType.ByteEnumType;
import test.com.feinno.serialization.protobuf.enumType.CharacterEnumType;
import test.com.feinno.serialization.protobuf.enumType.IntegerEnumType;
import test.com.feinno.serialization.protobuf.enumType.LongEnumType;
import test.com.feinno.serialization.protobuf.enumType.ShortEnumType;
import test.com.feinno.serialization.protobuf.enumType.StringEnumType;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class EnumTypeBean extends ProtoEntity {

	@ProtoMember(1)
	private IntegerEnumType integerEnumType;

	@ProtoMember(2)
	private LongEnumType longEnumType;

	@ProtoMember(3)
	private ByteEnumType byteEnumType;

	@ProtoMember(4)
	private CharacterEnumType characterEnumType;

	@ProtoMember(5)
	private ShortEnumType shortEnumType;

	@ProtoMember(6)
	private StringEnumType stringEnumType;

	public final IntegerEnumType getIntegerEnumType() {
		return integerEnumType;
	}

	public final void setIntegerEnumType(IntegerEnumType integerEnumType) {
		this.integerEnumType = integerEnumType;
	}

	public final LongEnumType getLongEnumType() {
		return longEnumType;
	}

	public final void setLongEnumType(LongEnumType longEnumType) {
		this.longEnumType = longEnumType;
	}

	public final ByteEnumType getByteEnumType() {
		return byteEnumType;
	}

	public final void setByteEnumType(ByteEnumType byteEnumType) {
		this.byteEnumType = byteEnumType;
	}

	public final CharacterEnumType getCharacterEnumType() {
		return characterEnumType;
	}

	public final void setCharacterEnumType(CharacterEnumType characterEnumType) {
		this.characterEnumType = characterEnumType;
	}

	public final ShortEnumType getShortEnumType() {
		return shortEnumType;
	}

	public final void setShortEnumType(ShortEnumType shortEnumType) {
		this.shortEnumType = shortEnumType;
	}

	public StringEnumType getStringEnumType() {
		return stringEnumType;
	}

	public void setStringEnumType(StringEnumType stringEnumType) {
		this.stringEnumType = stringEnumType;
	}

}
