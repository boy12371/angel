package test.com.feinno.serialization.protobuf.descriptor;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class MapValueClass extends ProtoEntity {

	@ProtoMember(1)
	private String name;

	@ProtoMember(2)
	private InnerClass innerClass;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InnerClass getInnerClass() {
		return innerClass;
	}

	public void setInnerClass(InnerClass innerClass) {
		this.innerClass = innerClass;
	}

}
