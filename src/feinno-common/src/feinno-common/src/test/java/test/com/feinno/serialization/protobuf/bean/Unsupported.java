package test.com.feinno.serialization.protobuf.bean;

import java.util.HashSet;
import java.util.Set;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class Unsupported extends ProtoEntity {

	@ProtoMember(1)
	private Set<String> set = new HashSet<String>();

	public final Set<String> getSet() {
		return set;
	}

	public final void setSet(Set<String> set) {
		this.set = set;
	}

}
