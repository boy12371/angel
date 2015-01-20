package test.com.feinno.serialization.protobuf.bean;

import java.io.Serializable;

import com.feinno.serialization.protobuf.ProtoEntity;
import com.feinno.serialization.protobuf.ProtoMember;

public class Table extends ProtoEntity implements Serializable {

	@ProtoMember(value = 1)
	private String name;

	@ProtoMember(value = 2)
	private int id;

	@ProtoMember(3)
	private String email;

	@ProtoMember(4)
	private User user;
	
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
