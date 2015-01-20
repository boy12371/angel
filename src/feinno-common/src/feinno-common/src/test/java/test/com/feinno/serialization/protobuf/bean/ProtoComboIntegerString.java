package test.com.feinno.serialization.protobuf.bean;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class ProtoComboIntegerString extends ProtoEntity {

	@ProtoMember(1)
	private Integer key;

	@ProtoMember(2)
	private String value;

	public final Integer getKey() {
		return key;
	}

	public final void setKey(Integer key) {
		this.key = key;
	}

	public final String getValue() {
		return value;
	}

	public final void setValue(String value) {
		this.value = value;
	}

}
