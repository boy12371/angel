package test.com.feinno.serialization.protobuf.bean;

import java.io.Serializable;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class User extends ProtoEntity implements Serializable {

	@ProtoMember(1)
	private String name;
	
	@ProtoMember(2)
	private int id;

	@ProtoMember(3)
	private String email;

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

}
