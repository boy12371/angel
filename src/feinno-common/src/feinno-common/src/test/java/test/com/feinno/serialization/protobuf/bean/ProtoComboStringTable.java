package test.com.feinno.serialization.protobuf.bean;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class ProtoComboStringTable extends ProtoEntity {

	@ProtoMember(1)
	private String key;
	
	@ProtoMember(2)
	private Table value;

	public final String getKey() {
		return key;
	}

	public final void setKey(String key) {
		this.key = key;
	}

	public Table getValue() {
		return value;
	}

	public void setValue(Table value) {
		this.value = value;
	}

}
