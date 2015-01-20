package test.com.feinno.serialization.protobuf.descriptor;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class InnerClass extends ProtoEntity {

	@ProtoMember(value = 1, required = true)
	private String name;

	@ProtoMember(2)
	private int id;

	@ProtoMember(3)
	private String email;

	@ProtoMember(4)
	private boolean marriged;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isMarriged() {
		return marriged;
	}

	public void setMarriged(boolean marriged) {
		this.marriged = marriged;
	}

}
