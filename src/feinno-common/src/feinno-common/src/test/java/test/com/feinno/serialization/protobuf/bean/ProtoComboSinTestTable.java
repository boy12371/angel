package test.com.feinno.serialization.protobuf.bean;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class ProtoComboSinTestTable extends ProtoEntity {

	@ProtoMember(1)
	private SinBeanTest key;

	@ProtoMember(2)
	private SinBeanTest value;

	public final SinBeanTest getKey() {
		return key;
	}

	public final void setKey(SinBeanTest key) {
		this.key = key;
	}

	public final SinBeanTest getValue() {
		return value;
	}

	public final void setValue(SinBeanTest value) {
		this.value = value;
	}

}
