package test.com.feinno.serialization.protobuf.bean;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class ProtoComboStringInteger extends ProtoEntity {

	@ProtoMember(1)
	private String key;

	@ProtoMember(2)
	private Integer value;

	public final String getKey() {
		return key;
	}

	public final void setKey(String key) {
		this.key = key;
	}

	public final Integer getValue() {
		return value;
	}

	public final void setValue(Integer value) {
		this.value = value;
	}

}
